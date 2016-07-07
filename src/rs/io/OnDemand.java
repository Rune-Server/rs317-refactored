package rs.io;

import rs.Game;
import rs.Signlink;
import rs.cache.Archive;
import rs.node.Chain;
import rs.node.Deque;
import rs.node.impl.OnDemandRequest;
import rs.util.JString;

import java.io.*;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

public class OnDemand implements Runnable {

    public static String[] CRC_FILES = {"model_crc", "anim_crc", "midi_crc", "map_crc"};
    public static String[] VERSION_FILES = {"model_version", "anim_version", "midi_version", "map_version"};

    public int anim_index[];
    public Buffer buffer;
    public Chain completed;
    public CRC32 crc32;
    public int[][] crcs;
    public OnDemandRequest current;
    public int cycle;
    public int extras_loaded;
    public int extras_total;
    public int fails;
    public byte file_priorities[][];
    public int file_versions[][];
    public Game game;
    public int highest_pri;
    public int idle_cycles;
    public Deque immediate;
    public int immediate_requests_sent;
    public InputStream in;
    public long last_socket_open;
    public int map_file[];
    public int map_index[];
    public int map_landscape[];
    public byte map_members[];
    public String message;
    public int midi_index[];
    public byte model_index[];
    public int offset;
    public OutputStream out;
    public Chain passive_requests;
    public int passive_requests_sent;
    public boolean retreiving;
    public boolean running;
    public Chain sent_requests;
    public int since_keep_alive;
    public Socket socket;
    public int to_read;
    public Chain to_request;
    public Chain wanted;

    public OnDemand() {
        sent_requests = new Chain();
        message = JString.BLANK;
        crc32 = new CRC32();
        buffer = new Buffer(500);
        file_priorities = new byte[4][];
        passive_requests = new Chain();
        running = true;
        retreiving = false;
        completed = new Chain();
        immediate = new Deque();
        file_versions = new int[4][];
        crcs = new int[4][];
        to_request = new Chain();
        wanted = new Chain();
    }

    public void clear_passive_requests() {
        synchronized (passive_requests) {
            passive_requests.clear();
        }
    }

    public boolean data_valid(int version, int crc, byte[] data) {
        if (data == null || data.length < 2) {
            return false;
        }

        int pos = data.length - 2;
        int read_version = ((data[pos] & 0xff) << 8) + (data[pos + 1] & 0xff);

        crc32.reset();
        crc32.update(data, 0, pos);

        int read_crc = (int) crc32.getValue();

        if (read_version != version) {
            return false;
        }

        if (read_crc != crc) {
            return false;
        }

        return true;
    }

    public int get_file_count(int archive) {
        return file_versions[archive].length;
    }

    public int get_map_uid(int x, int y, int type) {
        int uid = (x << 8) + y;
        for (int i = 0; i < map_index.length; i++) {
            if (map_index[i] == uid) {
                if (type == 0) {
                    return map_file[i];
                } else {
                    return map_landscape[i];
                }
            }
        }
        return -1;
    }

    public void handle_response() {
        try {
            int available = in.available();

            if (to_read == 0 && available >= 6) {
                retreiving = true;

                in.read(buffer.payload, 0, 6);

                buffer.position = 0;
                int archive = buffer.get_ubyte();
                int file = buffer.get_ushort();
                int size = buffer.get_ushort();
                int part = buffer.get_ubyte();

                current = null;

                for (OnDemandRequest r = (OnDemandRequest) sent_requests.top(); r != null; r = (OnDemandRequest) sent_requests.next()) {
                    if (r.archive == archive && r.file == file) {
                        current = r;
                    }

                    if (current != null) {
                        r.cycle = 0;
                    }
                }

                if (current != null) {
                    idle_cycles = 0;
                    if (size == 0) {
                        Signlink.error("Rej: " + archive + "," + file);
                        current.payload = null;
                        if (current.immediate) {
                            synchronized (completed) {
                                completed.push_back(current);
                            }
                        } else {
                            current.detach();
                        }
                        current = null;
                    } else {
                        if (current.payload == null && part == 0) {
                            current.payload = new byte[size];
                        }
                        if (current.payload == null && part != 0) {
                            throw new IOException("missing start of file");
                        }
                    }
                }
                offset = part * 500;
                to_read = 500;

                int limit = size - part * 500;

                if (to_read > limit) {
                    to_read = limit;
                }
            }

            if (to_read > 0 && available >= to_read) {
                retreiving = true;
                byte[] data = buffer.payload;
                int position = 0;

                if (current != null) {
                    data = current.payload;
                    position = offset;
                }

                for (int i = 0; i < to_read; i += in.read(data, i + position, to_read - i))
                    ;

                if (to_read + offset >= data.length && current != null) {
                    if (Game.cache[0] != null) {
                        Game.cache[current.archive + 1].put(data, current.file);
                    }

                    if (!current.immediate && current.archive == 3) {
                        current.immediate = true;
                        current.archive = 93;
                    }

                    if (current.immediate) {
                        synchronized (completed) {
                            completed.push_back(current);
                        }
                    } else {
                        current.detach();
                    }
                }
                to_read = 0;
                return;
            }
        } catch (IOException ioe) {
            try {
                socket.close();
            } catch (Exception e) {
            }
            socket = null;
            in = null;
            out = null;
            to_read = 0;
            ioe.printStackTrace();
        }
    }

    public boolean has_landscape(int index) {
        for (int k = 0; k < map_index.length; k++) {
            if (map_landscape[k] == index) {
                return true;
            }
        }
        return false;
    }

    public boolean has_midi(int i) {
        return midi_index[i] == 1;
    }

    public int immediate_request_count() {
        synchronized (immediate) {
            return immediate.count();
        }
    }

    public void local_complete() {
        OnDemandRequest r;

        synchronized (wanted) {
            r = (OnDemandRequest) wanted.pop();
        }

        while (r != null) {
            retreiving = true;
            byte data[] = null;

            if (Game.cache[0] != null) {
                data = Game.cache[r.archive + 1].get(r.file);
            }

            if (!data_valid(file_versions[r.archive][r.file], crcs[r.archive][r.file], data)) {
                data = null;
            }

            synchronized (wanted) {
                if (data == null) {
                    to_request.push_back(r);
                } else {
                    r.payload = data;
                    synchronized (completed) {
                        completed.push_back(r);
                    }
                }
                r = (OnDemandRequest) wanted.pop();
            }
        }
    }

    public int mesh_flags(int i) {
        return model_index[i] & 0xff;
    }

    public OnDemandRequest next() {
        OnDemandRequest r;

        synchronized (completed) {
            r = (OnDemandRequest) completed.pop();
        }

        if (r == null) {
            return null;
        }

        synchronized (immediate) {
            r.uncache();
        }

        if (r.payload == null) {
            return r;
        }

        GZIPInputStream in = null;
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            in = new GZIPInputStream(new ByteArrayInputStream(r.payload));

            byte[] buffer = new byte[2048]; // 2kb
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("error unzipping", e);
        } finally {
            if (out != null) {
                r.payload = out.toByteArray();

                try {
                    out.close();
                } catch (IOException e) {
                    /* nothing */
                }
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
					/* nothing */
                }
            }
        }
        return r;
    }

    public void passive_requests() {
        while (immediate_requests_sent == 0 && passive_requests_sent < 10) {
            if (highest_pri == 0) {
                break;
            }

            OnDemandRequest r;

            synchronized (passive_requests) {
                r = (OnDemandRequest) passive_requests.pop();
            }

            while (r != null) {
                if (file_priorities[r.archive][r.file] != 0) {
                    file_priorities[r.archive][r.file] = 0;
                    sent_requests.push_back(r);
                    send_request(r);
                    retreiving = true;

                    if (extras_loaded < extras_total) {
                        extras_loaded++;
                    }

                    message = "Loading extra files - " + extras_loaded + "/" + extras_total;
                    passive_requests_sent++;
                    if (passive_requests_sent == 10) {
                        return;
                    }
                }
                synchronized (passive_requests) {
                    r = (OnDemandRequest) passive_requests.pop();
                }
            }

            for (int archive = 0; archive < 4; archive++) {
                byte priorities[] = this.file_priorities[archive];
                int count = priorities.length;

                for (int i = 0; i < count; i++) {
                    if (priorities[i] == highest_pri) {
                        priorities[i] = 0;
                        OnDemandRequest r1 = new OnDemandRequest();
                        r1.archive = archive;
                        r1.file = i;
                        r1.immediate = false;
                        sent_requests.push_back(r1);
                        send_request(r1);
                        retreiving = true;

                        if (extras_loaded < extras_total) {
                            extras_loaded++;
                        }

                        message = "Loading extra files - " + extras_loaded + "/" + extras_total;
                        passive_requests_sent++;

                        if (passive_requests_sent == 10) {
                            return;
                        }
                    }
                }

            }

            highest_pri--;
        }
    }

    public void remaining_request() {
        immediate_requests_sent = 0;
        passive_requests_sent = 0;

        for (OnDemandRequest request = (OnDemandRequest) sent_requests.top(); request != null; request = (OnDemandRequest) sent_requests.next()) {
            if (request.immediate) {
                immediate_requests_sent++;
            } else {
                passive_requests_sent++;
            }
        }

        while (immediate_requests_sent < 10) {
            OnDemandRequest request = (OnDemandRequest) to_request.pop();

            if (request == null) {
                break;
            }

            if (file_priorities[request.archive][request.file] != 0) {
                extras_loaded++;
            }

            file_priorities[request.archive][request.file] = 0;
            sent_requests.push_back(request);
            immediate_requests_sent++;
            send_request(request);
            retreiving = true;
        }
    }

    public void request(int file, int archive) {
        if (Game.cache[0] == null) {
            return;
        }
        if (file_versions[archive][file] == 0) {
            return;
        }
        if (file_priorities[archive][file] == 0) {
            return;
        }
        if (highest_pri == 0) {
            return;
        }
        OnDemandRequest request = new OnDemandRequest();
        request.archive = archive;
        request.file = file;
        request.immediate = false;
        synchronized (passive_requests) {
            passive_requests.push_back(request);
        }
    }

    public void request_model(int i) {
        send_request(0, i);
    }

    public void request_regions(boolean members) {
        int length = map_index.length;
        for (int i = 0; i < length; i++) {
            if (members || map_members[i] != 0) {
                verify((byte) 2, 3, map_landscape[i]);
                verify((byte) 2, 3, map_file[i]);
            }
        }

    }

    public void run() {
        try {
            while (running) {
                cycle++;

                try {
                    if (highest_pri == 0 && Game.cache[0] != null) {
                        Thread.sleep(50);
                    } else {
                        Thread.sleep(20);
                    }
                } catch (Exception e) {
                }

                retreiving = true;

                for (int i = 0; i < 100; i++) {
                    if (!retreiving) {
                        break;
                    }

                    retreiving = false;

                    local_complete();
                    remaining_request();

                    if (immediate_requests_sent == 0 && i >= 5) {
                        break;
                    }

                    passive_requests();

                    if (in != null) {
                        handle_response();
                    }
                }

                boolean idle = false;

                for (OnDemandRequest r = (OnDemandRequest) sent_requests.top(); r != null; r = (OnDemandRequest) sent_requests.next()) {
                    if (r.immediate) {
                        idle = true;
                        r.cycle++;

                        if (r.cycle > 50) {
                            r.cycle = 0;
                            send_request(r);
                        }
                    }
                }

                if (!idle) {
                    for (OnDemandRequest request = (OnDemandRequest) sent_requests.top(); request != null; request = (OnDemandRequest) sent_requests.next()) {
                        idle = true;
                        request.cycle++;
                        if (request.cycle > 50) {
                            request.cycle = 0;
                            send_request(request);
                        }
                    }

                }

                if (idle) {
                    idle_cycles++;

                    if (idle_cycles > 750) {
                        try {
                            socket.close();
                        } catch (Exception _ex) {
							/* nada */
                        }

                        socket = null;
                        in = null;
                        out = null;
                        to_read = 0;
                    }
                } else {
                    idle_cycles = 0;
                    message = "";
                }

                if (Game.logged_in && socket != null && out != null && (highest_pri > 0 || Game.cache[0] == null)) {
                    since_keep_alive++;
                    if (since_keep_alive > 500) {
                        since_keep_alive = 0;

                        buffer.position = 0;
                        buffer.put_byte(0);
                        buffer.put_byte(0);
                        buffer.put_byte(0);
                        buffer.put_byte(10);

                        try {
                            out.write(buffer.payload, 0, 4);
                        } catch (IOException _ex) {
                            idle_cycles = 5000;
                            _ex.printStackTrace();
                        }
                    }
                }
            }
            return;
        } catch (Exception e) {
            Signlink.error("od_ex " + e.getMessage());
        }
    }

    public void send_request(int archive, int file) {
        if (archive < 0 || archive > file_versions.length || file < 0 || file >= file_versions[archive].length) {
            return;
        }

        if (file_versions[archive][file] == 0) {
            return;
        }

        synchronized (immediate) {
            for (OnDemandRequest r = (OnDemandRequest) immediate.top(); r != null; r = (OnDemandRequest) immediate.next()) {
                if (r.archive == archive && r.file == file) {
                    return;
                }
            }

            OnDemandRequest r = new OnDemandRequest();

            r.archive = archive;
            r.file = file;
            r.immediate = true;

            synchronized (wanted) {
                wanted.push_back(r);
            }

            immediate.push(r);
        }
    }

    public void send_request(OnDemandRequest node) {
        try {
            if (socket == null) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - last_socket_open < 4000L) {
                    return;
                }
                last_socket_open = currentTime;

                socket = Game.instance.get_socket(43594 + Game.port_offset);
                in = socket.getInputStream();
                out = socket.getOutputStream();

                out.write(15);

                for (int j = 0; j < 8; j++) {
                    in.read();
                }

                idle_cycles = 0;
            }

            buffer.position = 0;
            buffer.put_byte(node.archive);
            buffer.put_short(node.file);
            buffer.put_byte(node.immediate ? 2 : Game.logged_in ? 0 : 1);

            out.write(buffer.payload, 0, 4);
            since_keep_alive = 0;
            fails = -10000;
        } catch (IOException e) {
            try {
                socket.close();
            } catch (Exception _ex) {
            }
            socket = null;
            in = null;
            out = null;
            to_read = 0;
            fails++;
            e.printStackTrace();
        }
    }

    public int seq_frame_count() {
        return anim_index.length;
    }

    public void setup(Archive archive, Game game) {
        for (int i = 0; i < VERSION_FILES.length; i++) {
            Buffer b = new Buffer(archive.get(VERSION_FILES[i]));
            int count = b.payload.length / 2;

            file_versions[i] = new int[count];
            file_priorities[i] = new byte[count];

            for (int j = 0; j < count; j++) {
                file_versions[i][j] = b.get_ushort();
            }
        }

        for (int i = 0; i < CRC_FILES.length; i++) {
            Buffer b = new Buffer(archive.get(CRC_FILES[i]));

            crcs[i] = new int[b.payload.length / 4];

            for (int j = 0; j < crcs[i].length; j++) {
                crcs[i][j] = b.get_int();
            }
        }

        byte[] data = null;
        int size = -1;

        // Model
        {
            data = archive.get("model_index");
            size = file_versions[0].length;

            model_index = new byte[size];

            for (int i = 0; i < size; i++) {
                if (i < data.length) {
                    model_index[i] = data[i];
                } else {
                    model_index[i] = 0x0;
                }
            }
        }

        // Map
        {
            Buffer b = new Buffer(archive.get("map_index"));
            size = b.payload.length / 7;

            map_index = new int[size];
            map_file = new int[size];
            map_landscape = new int[size];
            map_members = new byte[size];

            for (int i = 0; i < size; i++) {
                map_index[i] = b.get_ushort();
                map_file[i] = b.get_ushort();
                map_landscape[i] = b.get_ushort();
                map_members[i] = b.get_byte();
            }
        }

        // Animation
        {
            Buffer b = new Buffer(archive.get("anim_index"));
            size = b.payload.length / 2;

            anim_index = new int[size];

            for (int i = 0; i < size; i++) {
                anim_index[i] = b.get_ushort();
            }
        }

        // Midi
        {
            Buffer b = new Buffer(archive.get("midi_index"));
            size = b.payload.length;

            midi_index = new int[size];

            for (int i = 0; i < size; i++) {
                midi_index[i] = b.get_ubyte();
            }
        }

        this.running = true;
        Game.instance.start_thread(this, 2);
    }

    public void stop() {
        this.running = false;
    }

    public void verify(byte priority, int archive, int file) {
        if (Game.cache[0] == null) {
            return;
        }

        if (file_versions[archive][file] == 0) {
            return;
        }

        if (data_valid(file_versions[archive][file], crcs[archive][file], Game.cache[archive + 1].get(file))) {
            return;
        }

        file_priorities[archive][file] = priority;

        if (priority > highest_pri) {
            highest_pri = priority;
        }

        extras_total++;
    }
}
