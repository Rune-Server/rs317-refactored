package rs.cache.impl;

import rs.io.Buffer;

public class SequenceFrame {

    public static SequenceFrame[] instance;

    public static SequenceFrame get(int i) {
        if (instance == null) {
            return null;
        }
        return instance[i];
    }

    public static void init(int count) {
        SequenceFrame.instance = new SequenceFrame[count + 1];
    }

    public static void load(byte[] payload) {
        Buffer s = new Buffer(payload);
        s.position = payload.length - 8;

        int flag_pos = s.get_ushort();
        int mod_pos = s.get_ushort();
        int len_pos = s.get_ushort();
        int skin_pos = s.get_ushort();

        int position = 0;
        Buffer info_stream = new Buffer(payload);
        info_stream.position = position;

        position += flag_pos + 2;
        Buffer flag_stream = new Buffer(payload);
        flag_stream.position = position;

        position += mod_pos;
        Buffer modifier_stream = new Buffer(payload);
        modifier_stream.position = position;

        position += len_pos;
        Buffer length_stream = new Buffer(payload);
        length_stream.position = position;

        position += skin_pos;
        Buffer skin_stream = new Buffer(payload);
        skin_stream.position = position;

        SkinList sl = new SkinList(skin_stream);

        int count = info_stream.get_ushort();
        int skins[] = new int[500];
        int vertex_x[] = new int[500];
        int vertex_y[] = new int[500];
        int vertex_z[] = new int[500];

        for (int i = 0; i < count; i++) {
            int id = info_stream.get_ushort();

            SequenceFrame a = instance[id] = new SequenceFrame();
            a.length = length_stream.get_ubyte();
            a.skinlist = sl;

            int frame_count = info_stream.get_ubyte();
            int last_index = -1;
            int frame_index = 0;

            for (int index = 0; index < frame_count; index++) {
                int vertex_mask = flag_stream.get_ubyte();

                if (vertex_mask > 0) {
                    if (sl.opcodes[index] != 0) {
                        for (int skin = index - 1; skin > last_index; skin--) {
                            if (sl.opcodes[skin] != 0) {
                                continue;
                            }
                            skins[frame_index] = skin;
                            vertex_x[frame_index] = 0;
                            vertex_y[frame_index] = 0;
                            vertex_z[frame_index] = 0;
                            frame_index++;
                            break;
                        }

                    }

                    skins[frame_index] = index;
                    int vertex = 0;

                    if (sl.opcodes[index] == 3) {
                        vertex = 128;
                    }

                    if ((vertex_mask & 1) != 0) {
                        vertex_x[frame_index] = modifier_stream.get_smart();
                    } else {
                        vertex_x[frame_index] = vertex;
                    }

                    if ((vertex_mask & 2) != 0) {
                        vertex_y[frame_index] = modifier_stream.get_smart();
                    } else {
                        vertex_y[frame_index] = vertex;
                    }

                    if ((vertex_mask & 4) != 0) {
                        vertex_z[frame_index] = modifier_stream.get_smart();
                    } else {
                        vertex_z[frame_index] = vertex;
                    }

                    last_index = index;
                    frame_index++;
                }
            }

            a.frame_count = frame_index;
            a.vertices = new int[frame_index];
            a.vertex_x = new int[frame_index];
            a.vertex_y = new int[frame_index];
            a.vertex_z = new int[frame_index];

            for (int j = 0; j < frame_index; j++) {
                a.vertices[j] = skins[j];
                a.vertex_x[j] = vertex_x[j];
                a.vertex_y[j] = vertex_y[j];
                a.vertex_z[j] = vertex_z[j];
            }

        }

    }

    public static void nullify() {
        instance = null;
    }

    public int frame_count;
    public int length;
    public SkinList skinlist;
    public int vertex_x[];
    public int vertex_y[];
    public int vertex_z[];
    public int vertices[];

    public SequenceFrame() {
    }

}
