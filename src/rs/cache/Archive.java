package rs.cache;

import rs.bzip2.BZip2;
import rs.io.Buffer;

import java.util.HashMap;
import java.util.Map;

public class Archive {

    public class Entry {
        public int hash;
        public int packed_size;
        public int position;
        public int unpacked_size;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[Entry: hash:");
            sb.append(hash).append(", unpacked:");
            sb.append(unpacked_size).append(", packed:");
            sb.append(packed_size).append(", pos:");
            sb.append(position).append(']');
            return sb.toString();
        }
    }

    public static final int CONTEXT_HEADER_SIZE = 10;

    public static int get_hash(String s) {
        int hash = 0;
        s = s.toUpperCase();
        for (int j = 0; j < s.length(); j++) {
            hash = (hash * 61 + s.charAt(j)) - 32;
        }
        return hash;
    }

    public Buffer buffer;
    public Map<Integer, Entry> entries = new HashMap<>();
    public boolean extracted;

    public Archive(Buffer buffer) {
        int unpacked_size = buffer.get_medium();
        int packed_size = buffer.get_medium();

        if (packed_size != unpacked_size) {
            byte[] unpacked = new byte[unpacked_size];
            BZip2.decompress(unpacked, unpacked_size, buffer.payload, packed_size, 6);
            buffer = new Buffer(unpacked);
            this.extracted = true;
        }

        int count = buffer.get_ushort();
        int position = buffer.position + (count * CONTEXT_HEADER_SIZE);

        for (int i = 0; i < count; i++) {
            Entry e = new Entry();
            e.hash = buffer.get_int();
            e.unpacked_size = buffer.get_medium();
            e.packed_size = buffer.get_medium();
            e.position = position;
            position += e.packed_size;
            this.entries.put(e.hash, e);
        }

        this.buffer = buffer;
    }

    public byte[] get(String s) {
        return this.get(s, null);
    }

    public byte[] get(String s, byte[] dst) {
        int hash = Archive.get_hash(s);

        if (!this.entries.containsKey(hash)) {
            return null;
        }

        Entry e = this.entries.get(hash);

        if (dst == null) {
            dst = new byte[e.unpacked_size];
        }

        if (!this.extracted) {
            BZip2.decompress(dst, e.unpacked_size, this.buffer.payload, e.packed_size, e.position);
        } else {
            System.arraycopy(this.buffer.payload, e.position, dst, 0, e.unpacked_size);
        }

        return dst;
    }
}
