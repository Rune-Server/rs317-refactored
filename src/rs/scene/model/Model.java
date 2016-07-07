package rs.scene.model;

import rs.cache.impl.SequenceFrame;
import rs.cache.impl.SkinList;
import rs.io.Buffer;
import rs.io.OnDemand;
import rs.media.Canvas2D;
import rs.media.Canvas3D;
import rs.node.impl.Renderable;

public class Model extends Renderable {

    public static class Header {
        public int alpha_stream_pos;
        public int anInt372;
        public int anInt374;
        public int anInt375;
        public int anInt376;
        public int anInt377;
        public int anInt378;
        public int draw_type_stream_pos;
        public byte[] payload;
        public int priority_stream_pos;
        public int texture_map_stream_pos;
        public int textured_triangle_count;
        public int textured_triangle_stream_pos;
        public int triangle_color_stream_pos;
        public int triangle_count;
        public int tskin_stream_pos;
        public short vertex_count;
    }

    public static int anInt1681;
    public static int anInt1682;
    public static int anInt1683;
    public static short replace_vertex_x[] = new short[2000];
    public static short replace_vertex_y[] = new short[2000];
    public static short replace_vertex_z[] = new short[2000];
    public static int anIntArray1625[] = new int[2000];
    public static int anIntArray1671[] = new int[1500];
    public static int anIntArray1673[] = new int[12];
    public static int anIntArray1675[] = new int[2000];
    public static int anIntArray1676[] = new int[2000];
    public static int anIntArray1677[] = new int[12];
    public static int anIntArrayArray1672[][] = new int[1500][512];
    public static int anIntArrayArray1674[][] = new int[12][2000];

    public static Header[] header;
    public static OnDemand ondemand;

    public static boolean scene_clickable;
    public static int mouse_x;
    public static int mouse_y;
    public static int hovered_count;
    public static int[] hovered_uid = new int[1000];

    public static int sin[];
    public static int cos[];

    public static Model temporary = new Model();

    public static int tmp_screen_x[] = new int[10];
    public static int tmp_screen_y[] = new int[10];
    public static int tmp_hsl[] = new int[10];

    public static int tmp_textured_x[] = new int[4096];
    public static int tmp_textured_y[] = new int[4096];
    public static int tmp_textured_z[] = new int[4096];

    public static int triangle_x[] = new int[4096];
    public static int triangle_y[] = new int[4096];
    public static int triangle_depth[] = new int[4096];
    public static boolean triangle_project[] = new boolean[4096];
    public static boolean triangle_check_bounds[] = new boolean[4096];

    public static int palette[];
    public static int shadow_decay[];

    static {
        sin = Canvas3D.sin;
        cos = Canvas3D.cos;
        palette = Canvas3D.palette;
        shadow_decay = Canvas3D.shadow_decay;
    }

    public static Model get(int i) {
        if (header == null) {
            return null;
        }
        Header h = header[i];

        if (h == null) {
            ondemand.request_model(i);
            return null;
        }

        return new Model(i);
    }

    public static void init(int size, OnDemand ondemand) {
        Model.header = new Header[size];
        Model.ondemand = ondemand;
    }

    public static boolean is_valid(int i) {
        if (header == null) {
            return false;
        }
        Header h = header[i];

        if (h == null) {
            ondemand.request_model(i);
            return false;
        }

        return true;
    }

    public static void load(byte[] data, int index) {
        if (data == null) {
            Header h = header[index] = new Header();
            h.vertex_count = 0;
            h.triangle_count = 0;
            h.textured_triangle_count = 0;
            return;
        }

        Buffer b = new Buffer(data);
        b.position = data.length - 18;

        Header h = header[index] = new Header();
        h.payload = data;

        h.vertex_count = (short) b.get_ushort();
        h.triangle_count = b.get_ushort();
        h.textured_triangle_count = b.get_ubyte();

        int has_textures = b.get_ubyte();
        int priority = b.get_ubyte();
        int has_alpha = b.get_ubyte();
        int has_skins = b.get_ubyte();
        int has_vertex_skins = b.get_ubyte();
        int xDataLength = b.get_ushort();
        int yDataLength = b.get_ushort();
        int zDataLength = b.get_ushort();
        int triDataLength = b.get_ushort();

        int position = 0;

        h.anInt372 = position;

        position += h.vertex_count;
        h.anInt378 = position;

        position += h.triangle_count;
        h.priority_stream_pos = position;

        if (priority == 255) {
            position += h.triangle_count;
        } else {
            h.priority_stream_pos = -priority - 1;
        }

        h.tskin_stream_pos = position;

        if (has_skins == 1) {
            position += h.triangle_count;
        } else {
            h.tskin_stream_pos = -1;
        }

        h.draw_type_stream_pos = position;

        if (has_textures == 1) {
            position += h.triangle_count;
        } else {
            h.draw_type_stream_pos = -1;
        }

        h.anInt376 = position;

        if (has_vertex_skins == 1) {
            position += h.vertex_count;
        } else {
            h.anInt376 = -1;
        }

        h.alpha_stream_pos = position;

        if (has_alpha == 1) {
            position += h.triangle_count;
        } else {
            h.alpha_stream_pos = -1;
        }

        h.anInt377 = position;
        position += triDataLength;

        h.triangle_color_stream_pos = position;
        position += h.triangle_count * 2;
        h.texture_map_stream_pos = position;
        position += h.textured_triangle_count * 6;
        h.textured_triangle_stream_pos = position;
        position += xDataLength;
        h.anInt374 = position;
        position += yDataLength;
        h.anInt375 = position;
        position += zDataLength;
    }

    public static void nullify() {
        header = null;
        triangle_check_bounds = null;
        triangle_project = null;
        triangle_x = null;
        triangle_y = null;
        triangle_depth = null;
        tmp_textured_x = null;
        tmp_textured_y = null;
        tmp_textured_z = null;
        anIntArray1671 = null;
        anIntArrayArray1672 = null;
        anIntArray1673 = null;
        anIntArrayArray1674 = null;
        anIntArray1675 = null;
        anIntArray1676 = null;
        anIntArray1677 = null;
        sin = null;
        cos = null;
        palette = null;
        shadow_decay = null;
    }

    public static void nullify(int index) {
        header[index] = null;
    }

    public static int set_hsl_lightness(int hsl, int lightness, int info) {
        if ((info & 2) == 2) {
            if (lightness < 0) {
                lightness = 0;
            } else if (lightness > 127) {
                lightness = 127;
            }

            lightness = 127 - lightness;
            return lightness;
        }

        lightness = lightness * (hsl & 0x7f) >> 7;

        if (lightness < 2) {
            lightness = 2;
        } else if (lightness > 126) {
            lightness = 126;
        }

        return (hsl & 0xff80) + lightness;
    }

    public int anInt1641;
    public boolean is_clickable;
    public int max_horizon;
    public int max_x;
    public int max_y;
    public int max_z;
    public int min_x;
    public int min_z;
    public int pile_height;
    public int texture_map_x[];
    public int texture_map_y[];
    public int texture_map_z[];
    public int textured_triangle_count;
    public int tri_hsl1[];
    public int tri_hsl2[];
    public int tri_hsl3[];
    public int triangle_alpha[];
    public int triangle_color[];
    public int triangle_count;
    public int triangle_groups[][];
    public int triangle_info[];
    public int triangle_priority[];
    public int triangle_tskin[];
    public short triangle_viewspace_a[];
    public short triangle_viewspace_b[];
    public short triangle_viewspace_c[];
    public int unknown2;
    public int unknown3;
    public short vertex_count;
    public int vertex_skin_types[];
    public int vertex_weights[][];
    public short[] vertex_x;
    public short[] vertex_y;
    public short[] vertex_z;
    public Vertex[] vertices;

    public Model() {
        is_clickable = false;
    }

    public Model(boolean copy_colors, boolean copy_opacity, boolean copy_vertices, Model m) {
        is_clickable = false;
        vertex_count = m.vertex_count;
        triangle_count = m.triangle_count;
        textured_triangle_count = m.textured_triangle_count;

        if (copy_vertices) {
            vertex_x = m.vertex_x;
            vertex_y = m.vertex_y;
            vertex_z = m.vertex_z;
        } else {
            vertex_x = new short[vertex_count];
            vertex_y = new short[vertex_count];
            vertex_z = new short[vertex_count];

            for (int i = 0; i < vertex_count; i++) {
                vertex_x[i] = m.vertex_x[i];
                vertex_y[i] = m.vertex_y[i];
                vertex_z[i] = m.vertex_z[i];
            }
        }

        if (copy_colors) {
            triangle_color = m.triangle_color;
        } else {
            triangle_color = new int[triangle_count];

            for (int i = 0; i < triangle_count; i++) {
                triangle_color[i] = m.triangle_color[i];
            }
        }

        if (copy_opacity) {
            triangle_alpha = m.triangle_alpha;
        } else {
            triangle_alpha = new int[triangle_count];

            if (m.triangle_alpha == null) {
                for (int i = 0; i < triangle_count; i++) {
                    triangle_alpha[i] = 0;
                }
            } else {
                for (int i = 0; i < triangle_count; i++) {
                    triangle_alpha[i] = m.triangle_alpha[i];
                }
            }
        }

        vertex_skin_types = m.vertex_skin_types;
        triangle_tskin = m.triangle_tskin;
        triangle_info = m.triangle_info;
        triangle_viewspace_a = m.triangle_viewspace_a;
        triangle_viewspace_b = m.triangle_viewspace_b;
        triangle_viewspace_c = m.triangle_viewspace_c;
        triangle_priority = m.triangle_priority;
        anInt1641 = m.anInt1641;
        texture_map_x = m.texture_map_x;
        texture_map_y = m.texture_map_y;
        texture_map_z = m.texture_map_z;
    }

    public Model(boolean copy_y_vertices, boolean copy_shading, Model m) {
        is_clickable = false;

        vertex_count = m.vertex_count;
        triangle_count = m.triangle_count;
        textured_triangle_count = m.textured_triangle_count;

        if (copy_y_vertices) {
            vertex_y = new short[vertex_count];
            for (int j = 0; j < vertex_count; j++) {
                vertex_y[j] = m.vertex_y[j];
            }
        } else {
            vertex_y = m.vertex_y;
        }

        if (copy_shading) {
            tri_hsl1 = new int[triangle_count];
            tri_hsl2 = new int[triangle_count];
            tri_hsl3 = new int[triangle_count];

            for (int i = 0; i < triangle_count; i++) {
                tri_hsl1[i] = m.tri_hsl1[i];
                tri_hsl2[i] = m.tri_hsl2[i];
                tri_hsl3[i] = m.tri_hsl3[i];
            }

            triangle_info = new int[triangle_count];

            if (m.triangle_info == null) {
                for (int i = 0; i < triangle_count; i++) {
                    triangle_info[i] = 0;
                }
            } else {
                for (int i = 0; i < triangle_count; i++) {
                    triangle_info[i] = m.triangle_info[i];
                }
            }

            super.normal = new Vertex[vertex_count];

            for (int i = 0; i < vertex_count; i++) {
                Vertex v1 = super.normal[i] = new Vertex();
                Vertex v2 = m.normal[i];
                v1.x = v2.x;
                v1.y = v2.y;
                v1.z = v2.z;
                v1.w = v2.w;
            }

            vertices = m.vertices;
        } else {
            tri_hsl1 = m.tri_hsl1;
            tri_hsl2 = m.tri_hsl2;
            tri_hsl3 = m.tri_hsl3;
            triangle_info = m.triangle_info;
        }

        vertex_x = m.vertex_x;
        vertex_z = m.vertex_z;
        triangle_color = m.triangle_color;
        triangle_alpha = m.triangle_alpha;
        triangle_priority = m.triangle_priority;
        anInt1641 = m.anInt1641;
        triangle_viewspace_a = m.triangle_viewspace_a;
        triangle_viewspace_b = m.triangle_viewspace_b;
        triangle_viewspace_c = m.triangle_viewspace_c;
        texture_map_x = m.texture_map_x;
        texture_map_y = m.texture_map_y;
        texture_map_z = m.texture_map_z;
        super.height = ((Renderable) (m)).height;
        max_y = m.max_y;
        max_horizon = m.max_horizon;
        unknown2 = m.unknown2;
        unknown3 = m.unknown3;
        min_x = m.min_x;
        max_z = m.max_z;
        min_z = m.min_z;
        max_x = m.max_x;
    }

    public Model(int index) {
        is_clickable = false;
        Header config = header[index];

        vertex_count = config.vertex_count;
        triangle_count = config.triangle_count;
        textured_triangle_count = config.textured_triangle_count;

        vertex_x = new short[vertex_count];
        vertex_y = new short[vertex_count];
        vertex_z = new short[vertex_count];

        triangle_viewspace_a = new short[triangle_count];
        triangle_viewspace_b = new short[triangle_count];
        triangle_viewspace_c = new short[triangle_count];

        texture_map_x = new int[textured_triangle_count];
        texture_map_y = new int[textured_triangle_count];
        texture_map_z = new int[textured_triangle_count];

        if (config.anInt376 >= 0) {
            vertex_skin_types = new int[vertex_count];
        }

        if (config.draw_type_stream_pos >= 0) {
            triangle_info = new int[triangle_count];
        }

        if (config.priority_stream_pos >= 0) {
            triangle_priority = new int[triangle_count];
        } else {
            anInt1641 = -config.priority_stream_pos - 1;
        }

        if (config.alpha_stream_pos >= 0) {
            triangle_alpha = new int[triangle_count];
        }

        if (config.tskin_stream_pos >= 0) {
            triangle_tskin = new int[triangle_count];
        }

        triangle_color = new int[triangle_count];

        Buffer color_stream = new Buffer(config.payload);
        color_stream.position = config.anInt372;

        Buffer type_stream = new Buffer(config.payload);
        type_stream.position = config.textured_triangle_stream_pos;

        Buffer priority_stream = new Buffer(config.payload);
        priority_stream.position = config.anInt374;

        Buffer alpha_stream = new Buffer(config.payload);
        alpha_stream.position = config.anInt375;

        Buffer tskin_stream = new Buffer(config.payload);
        tskin_stream.position = config.anInt376;

        int x = 0;
        int y = 0;
        int z = 0;

        // VERTEX_DIRECTION_Chunk
        for (int v = 0; v < vertex_count; v++) {
            int flag = color_stream.get_ubyte();

            int off_x = 0;
            int off_y = 0;
            int off_z = 0;

            if ((flag & 1) != 0) {
                off_x = type_stream.get_smart();
            }

            if ((flag & 2) != 0) {
                off_y = priority_stream.get_smart();
            }

            if ((flag & 4) != 0) {
                off_z = alpha_stream.get_smart();
            }

            vertex_x[v] = (short) (x + off_x);
            vertex_y[v] = (short) (y + off_y);
            vertex_z[v] = (short) (z + off_z);

            x = vertex_x[v];
            y = vertex_y[v];
            z = vertex_z[v];

            if (vertex_skin_types != null) {
                vertex_skin_types[v] = tskin_stream.get_ubyte();
            }
        }

        color_stream.position = config.triangle_color_stream_pos;
        type_stream.position = config.draw_type_stream_pos;
        priority_stream.position = config.priority_stream_pos;
        alpha_stream.position = config.alpha_stream_pos;
        tskin_stream.position = config.tskin_stream_pos;

        for (int i = 0; i < triangle_count; i++) {
            triangle_color[i] = color_stream.get_ushort();

            if (triangle_info != null) {
                triangle_info[i] = type_stream.get_ubyte();
            }

            if (triangle_priority != null) {
                triangle_priority[i] = priority_stream.get_ubyte();
            }

            if (triangle_alpha != null) {
                triangle_alpha[i] = alpha_stream.get_ubyte();
            }

            if (triangle_tskin != null) {
                triangle_tskin[i] = tskin_stream.get_ubyte();
            }
        }

        color_stream.position = config.anInt377;
        type_stream.position = config.anInt378;

        x = 0;
        y = 0;
        z = 0;
        int last = 0;

        // TRIANGLE_TYPE_Chunk
        for (int i = 0; i < triangle_count; i++) {
            int type = type_stream.get_ubyte();

            if (type == 1) {
                x = color_stream.get_smart() + last;
                last = x;
                y = color_stream.get_smart() + last;
                last = y;
                z = color_stream.get_smart() + last;
                last = z;
                triangle_viewspace_a[i] = (short) x;
                triangle_viewspace_b[i] = (short) y;
                triangle_viewspace_c[i] = (short) z;
            }

            if (type == 2) {
                y = z;
                z = color_stream.get_smart() + last;
                last = z;
                triangle_viewspace_a[i] = (short) x;
                triangle_viewspace_b[i] = (short) y;
                triangle_viewspace_c[i] = (short) z;
            }

            if (type == 3) {
                x = z;
                z = color_stream.get_smart() + last;
                last = z;
                triangle_viewspace_a[i] = (short) x;
                triangle_viewspace_b[i] = (short) y;
                triangle_viewspace_c[i] = (short) z;
            }

            if (type == 4) {
                int k4 = x;
                x = y;
                y = k4;
                z = color_stream.get_smart() + last;
                last = z;
                triangle_viewspace_a[i] = (short) x;
                triangle_viewspace_b[i] = (short) y;
                triangle_viewspace_c[i] = (short) z;
            }
        }

        color_stream.position = config.texture_map_stream_pos;

        for (int i = 0; i < textured_triangle_count; i++) {
            texture_map_x[i] = color_stream.get_ushort();
            texture_map_y[i] = color_stream.get_ushort();
            texture_map_z[i] = color_stream.get_ushort();
        }

    }

    public Model(int model_count, boolean flag, Model models[]) {
        is_clickable = false;
        boolean has_info = false;
        boolean has_priority = false;
        boolean has_alpha = false;
        boolean has_color = false;
        vertex_count = 0;
        triangle_count = 0;
        textured_triangle_count = 0;
        anInt1641 = -1;

        for (int k = 0; k < model_count; k++) {
            Model m = models[k];
            if (m != null) {
                vertex_count += m.vertex_count;
                triangle_count += m.triangle_count;
                textured_triangle_count += m.textured_triangle_count;
                has_info |= m.triangle_info != null;

                if (m.triangle_priority != null) {
                    has_priority = true;
                } else {
                    if (anInt1641 == -1) {
                        anInt1641 = m.anInt1641;
                    }
                    if (anInt1641 != m.anInt1641) {
                        has_priority = true;
                    }
                }

                has_alpha |= m.triangle_alpha != null;
                has_color |= m.triangle_color != null;
            }
        }

        vertex_x = new short[vertex_count];
        vertex_y = new short[vertex_count];
        vertex_z = new short[vertex_count];

        triangle_viewspace_a = new short[triangle_count];
        triangle_viewspace_b = new short[triangle_count];
        triangle_viewspace_c = new short[triangle_count];

        tri_hsl1 = new int[triangle_count];
        tri_hsl2 = new int[triangle_count];
        tri_hsl3 = new int[triangle_count];

        texture_map_x = new int[textured_triangle_count];
        texture_map_y = new int[textured_triangle_count];
        texture_map_z = new int[textured_triangle_count];

        if (has_info) {
            triangle_info = new int[triangle_count];
        }

        if (has_priority) {
            triangle_priority = new int[triangle_count];
        }

        if (has_alpha) {
            triangle_alpha = new int[triangle_count];
        }
        if (has_color) {
            triangle_color = new int[triangle_count];
        }

        vertex_count = 0;
        triangle_count = 0;
        textured_triangle_count = 0;

        int i = 0;

        for (int j = 0; j < model_count; j++) {
            Model m = models[j];

            if (m != null) {
                int v_count = vertex_count;

                for (int k = 0; k < m.vertex_count; k++) {
                    vertex_x[vertex_count] = m.vertex_x[k];
                    vertex_y[vertex_count] = m.vertex_y[k];
                    vertex_z[vertex_count] = m.vertex_z[k];
                    vertex_count++;
                }

                for (int k = 0; k < m.triangle_count; k++) {
                    triangle_viewspace_a[triangle_count] = (short) (m.triangle_viewspace_a[k] + v_count);
                    triangle_viewspace_b[triangle_count] = (short) (m.triangle_viewspace_b[k] + v_count);
                    triangle_viewspace_c[triangle_count] = (short) (m.triangle_viewspace_c[k] + v_count);

                    tri_hsl1[triangle_count] = m.tri_hsl1[k];
                    tri_hsl2[triangle_count] = m.tri_hsl2[k];
                    tri_hsl3[triangle_count] = m.tri_hsl3[k];

                    if (has_info) {
                        if (m.triangle_info == null) {
                            triangle_info[triangle_count] = 0;
                        } else {
                            int j2 = m.triangle_info[k];
                            if ((j2 & 2) == 2) {
                                j2 += i << 2;
                            }
                            triangle_info[triangle_count] = j2;
                        }
                    }

                    if (has_priority) {
                        if (m.triangle_priority == null) {
                            triangle_priority[triangle_count] = m.anInt1641;
                        } else {
                            triangle_priority[triangle_count] = m.triangle_priority[k];
                        }
                    }

                    if (has_alpha) {
                        if (m.triangle_alpha == null) {
                            triangle_alpha[triangle_count] = 0;
                        } else {
                            triangle_alpha[triangle_count] = m.triangle_alpha[k];
                        }
                    }

                    if (has_color && m.triangle_color != null) {
                        triangle_color[triangle_count] = m.triangle_color[k];
                    }

                    triangle_count++;
                }

                for (int k = 0; k < m.textured_triangle_count; k++) {
                    texture_map_x[textured_triangle_count] = m.texture_map_x[k] + v_count;
                    texture_map_y[textured_triangle_count] = m.texture_map_y[k] + v_count;
                    texture_map_z[textured_triangle_count] = m.texture_map_z[k] + v_count;
                    textured_triangle_count++;
                }

                i += m.textured_triangle_count;
            }
        }

        method466();
    }

    public Model(int model_count, Model models[]) {
        is_clickable = false;
        boolean has_info = false;
        boolean has_priorities = false;
        boolean has_alpha = false;
        boolean has_tskins = false;
        vertex_count = 0;
        triangle_count = 0;
        textured_triangle_count = 0;
        anInt1641 = -1;

        for (int i = 0; i < model_count; i++) {
            Model m = models[i];
            if (m != null) {
                vertex_count += m.vertex_count;
                triangle_count += m.triangle_count;
                textured_triangle_count += m.textured_triangle_count;
                has_info |= m.triangle_info != null;

                if (m.triangle_priority != null) {
                    has_priorities = true;
                } else {
                    if (anInt1641 == -1) {
                        anInt1641 = m.anInt1641;
                    }
                    if (anInt1641 != m.anInt1641) {
                        has_priorities = true;
                    }
                }

                has_alpha |= m.triangle_alpha != null;
                has_tskins |= m.triangle_tskin != null;
            }
        }

        vertex_x = new short[vertex_count];
        vertex_y = new short[vertex_count];
        vertex_z = new short[vertex_count];
        vertex_skin_types = new int[vertex_count];

        triangle_viewspace_a = new short[triangle_count];
        triangle_viewspace_b = new short[triangle_count];
        triangle_viewspace_c = new short[triangle_count];

        texture_map_x = new int[textured_triangle_count];
        texture_map_y = new int[textured_triangle_count];
        texture_map_z = new int[textured_triangle_count];

        if (has_info) {
            triangle_info = new int[triangle_count];
        }

        if (has_priorities) {
            triangle_priority = new int[triangle_count];
        }

        if (has_alpha) {
            triangle_alpha = new int[triangle_count];
        }

        if (has_tskins) {
            triangle_tskin = new int[triangle_count];
        }

        triangle_color = new int[triangle_count];
        vertex_count = 0;
        triangle_count = 0;
        textured_triangle_count = 0;

        int l = 0;
        for (int i = 0; i < model_count; i++) {
            Model m = models[i];
            if (m != null) {
                for (int j = 0; j < m.triangle_count; j++) {
                    if (has_info) {
                        if (m.triangle_info == null) {
                            triangle_info[triangle_count] = 0;
                        } else {
                            int k1 = m.triangle_info[j];
                            if ((k1 & 2) == 2) {
                                k1 += l << 2;
                            }
                            triangle_info[triangle_count] = k1;
                        }
                    }

                    if (has_priorities) {
                        if (m.triangle_priority == null) {
                            triangle_priority[triangle_count] = m.anInt1641;
                        } else {
                            triangle_priority[triangle_count] = m.triangle_priority[j];
                        }
                    }

                    if (has_alpha) {
                        if (m.triangle_alpha == null) {
                            triangle_alpha[triangle_count] = 0;
                        } else {
                            triangle_alpha[triangle_count] = m.triangle_alpha[j];
                        }
                    }

                    if (has_tskins && m.triangle_tskin != null) {
                        triangle_tskin[triangle_count] = m.triangle_tskin[j];
                    }

                    triangle_color[triangle_count] = m.triangle_color[j];
                    triangle_viewspace_a[triangle_count] = method465(m, m.triangle_viewspace_a[j]);
                    triangle_viewspace_b[triangle_count] = method465(m, m.triangle_viewspace_b[j]);
                    triangle_viewspace_c[triangle_count] = method465(m, m.triangle_viewspace_c[j]);
                    triangle_count++;
                }

                for (int j = 0; j < m.textured_triangle_count; j++) {
                    texture_map_x[textured_triangle_count] = method465(m, m.texture_map_x[j]);
                    texture_map_y[textured_triangle_count] = method465(m, m.texture_map_y[j]);
                    texture_map_z[textured_triangle_count] = method465(m, m.texture_map_z[j]);
                    textured_triangle_count++;
                }

                l += m.textured_triangle_count;
            }
        }

    }

    public void apply_lighting(int light_brightness, int specular_distribution, int light_x, int light_y, int light_z) {
        for (int i = 0; i < triangle_count; i++) {
            int x = triangle_viewspace_a[i];
            int y = triangle_viewspace_b[i];
            int z = triangle_viewspace_c[i];

            if (triangle_info == null) {
                int hsl = triangle_color[i];
                Vertex v = super.normal[x];

                int lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl1[i] = set_hsl_lightness(hsl, lightness, 0);

                v = super.normal[y];
                lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl2[i] = set_hsl_lightness(hsl, lightness, 0);

                v = super.normal[z];
                lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl3[i] = set_hsl_lightness(hsl, lightness, 0);
            } else if ((triangle_info[i] & 1) == 0) {
                int hsl = triangle_color[i];
                int info = triangle_info[i];

                Vertex v = super.normal[x];
                int lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl1[i] = set_hsl_lightness(hsl, lightness, info);

                v = super.normal[y];
                lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl2[i] = set_hsl_lightness(hsl, lightness, info);

                v = super.normal[z];
                lightness = light_brightness + (light_x * v.x + light_y * v.y + light_z * v.z) / (specular_distribution * v.w);
                tri_hsl3[i] = set_hsl_lightness(hsl, lightness, info);
            }
        }

        super.normal = null;
        this.vertices = null;
        this.vertex_skin_types = null;
        this.triangle_tskin = null;

        if (this.triangle_info != null) {
            for (int i = 0; i < this.triangle_count; i++) {
                if ((this.triangle_info[i] & 2) == 2) {
                    return;
                }
            }
        }

        this.triangle_color = null;
    }

    public void apply_lighting(int light_brightness, int specular_factor, int light_x, int light_y, int light_z, boolean smooth_shading) {
        int light_length = (int) Math.sqrt(light_x * light_x + light_y * light_y + light_z * light_z);
        int specular_distribution = specular_factor * light_length >> 8;

        if (tri_hsl1 == null) {
            tri_hsl1 = new int[triangle_count];
            tri_hsl2 = new int[triangle_count];
            tri_hsl3 = new int[triangle_count];
        }

        if (super.normal == null) {
            super.normal = new Vertex[vertex_count];

            for (int i = 0; i < vertex_count; i++) {
                super.normal[i] = new Vertex();
            }
        }

        for (int i = 0; i < triangle_count; i++) {
            int x_i = triangle_viewspace_a[i];
            int y_i = triangle_viewspace_b[i];
            int z_i = triangle_viewspace_c[i];

            int j3 = vertex_x[y_i] - vertex_x[x_i];
            int k3 = vertex_y[y_i] - vertex_y[x_i];
            int l3 = vertex_z[y_i] - vertex_z[x_i];

            int i4 = vertex_x[z_i] - vertex_x[x_i];
            int j4 = vertex_y[z_i] - vertex_y[x_i];
            int k4 = vertex_z[z_i] - vertex_z[x_i];

            int x = k3 * k4 - j4 * l3;
            int y = l3 * i4 - k4 * j3;
            int z;

            for (z = j3 * j4 - i4 * k3; x > 8192 || y > 8192 || z > 8192 || x < -8192 || y < -8192 || z < -8192; z >>= 1) {
                x >>= 1;
                y >>= 1;
            }

            int length = (int) Math.sqrt(x * x + y * y + z * z);

            if (length <= 0) {
                length = 1;
            }

            x = (x * 256) / length;
            y = (y * 256) / length;
            z = (z * 256) / length;

            if (triangle_info == null || (triangle_info[i] & 1) == 0) {
                Vertex v = super.normal[x_i];
                v.x += x;
                v.y += y;
                v.z += z;
                v.w++;
                v = super.normal[y_i];
                v.x += x;
                v.y += y;
                v.z += z;
                v.w++;
                v = super.normal[z_i];
                v.x += x;
                v.y += y;
                v.z += z;
                v.w++;
            } else {
                tri_hsl1[i] = set_hsl_lightness(triangle_color[i], light_brightness + (light_x * x + light_y * y + light_z * z) / (specular_distribution + specular_distribution / 2), triangle_info[i]);
            }
        }

        if (smooth_shading) {
            apply_lighting(light_brightness, specular_distribution, light_x, light_y, light_z);
        } else {
            vertices = new Vertex[vertex_count];

            for (int i = 0; i < vertex_count; i++) {
                Vertex v = super.normal[i];
                Vertex n_v = vertices[i] = new Vertex();
                n_v.x = v.x;
                n_v.y = v.y;
                n_v.z = v.z;
                n_v.w = v.w;
            }

        }
        if (smooth_shading) {
            method466();
            return;
        } else {
            method468();
            return;
        }
    }

    public void apply_sequence_frame(int seq_index) {
        if (vertex_weights == null) {
            return;
        }

        if (seq_index == -1) {
            return;
        }

        SequenceFrame s = SequenceFrame.get(seq_index);

        if (s == null) {
            return;
        }

        SkinList skin = s.skinlist;
        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;

        for (int frame = 0; frame < s.frame_count; frame++) {
            int v_index = s.vertices[frame];
            this.transform(skin.opcodes[v_index], skin.vertices[v_index], s.vertex_x[frame], s.vertex_y[frame], s.vertex_z[frame]);
        }
    }

    public void apply_sequence_frames(int[] vertices, int frame1, int frame2) {
        if (frame1 == -1) {
            return;
        }

        if (vertices == null || frame2 == -1) {
            apply_sequence_frame(frame1);
            return;
        }

        SequenceFrame af1 = SequenceFrame.get(frame1);

        if (af1 == null) {
            return;
        }

        SequenceFrame af2 = SequenceFrame.get(frame2);

        if (af2 == null) {
            apply_sequence_frame(frame1);
            return;
        }

        SkinList slist = af1.skinlist;

        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;

        int position = 0;
        int vertex = vertices[position++];

        for (int frame = 0; frame < af1.frame_count; frame++) {
            int v;
            for (v = af1.vertices[frame]; v > vertex; vertex = vertices[position++]) {
                ;
            }
            if (v != vertex || slist.opcodes[v] == 0) {
                this.transform(slist.opcodes[v], slist.vertices[v], af1.vertex_x[frame], af1.vertex_y[frame], af1.vertex_z[frame]);
            }
        }

        anInt1681 = 0;
        anInt1682 = 0;
        anInt1683 = 0;

        position = 0;
        vertex = vertices[position++];

        for (int frame = 0; frame < af2.frame_count; frame++) {
            int v;
            for (v = af2.vertices[frame]; v > vertex; vertex = vertices[position++]) {
            }
            if (v == vertex || slist.opcodes[v] == 0) {
                this.transform(slist.opcodes[v], slist.vertices[v], af2.vertex_x[frame], af2.vertex_y[frame], af2.vertex_z[frame]);
            }
        }

    }

    public void apply_vertex_weights() {
        if (vertex_skin_types != null) {
            int weight_counts[] = new int[256];
            int top_label = 0;

            for (int i = 0; i < vertex_count; i++) {
                int label = vertex_skin_types[i];
                weight_counts[label]++;

                if (label > top_label) {
                    top_label = label;
                }
            }

            vertex_weights = new int[top_label + 1][];

            for (int i = 0; i <= top_label; i++) {
                vertex_weights[i] = new int[weight_counts[i]];
                weight_counts[i] = 0;
            }

            for (int i = 0; i < vertex_count; i++) {
                int label = vertex_skin_types[i];
                vertex_weights[label][weight_counts[label]++] = i;
            }

            vertex_skin_types = null;
            weight_counts = null;
        }

        if (triangle_tskin != null) {
            int skin_counts[] = new int[256];
            int top_skin = 0;

            for (int i = 0; i < triangle_count; i++) {
                int skin = triangle_tskin[i];
                skin_counts[skin]++;

                if (skin > top_skin) {
                    top_skin = skin;
                }
            }

            triangle_groups = new int[top_skin + 1][];

            for (int i = 0; i <= top_skin; i++) {
                triangle_groups[i] = new int[skin_counts[i]];
                skin_counts[i] = 0;
            }

            for (int i = 0; i < triangle_count; i++) {
                int group = triangle_tskin[i];
                triangle_groups[group][skin_counts[group]++] = i;
            }

            triangle_tskin = null;
            skin_counts = null;
        }
    }

    public void draw(int pitch, int yaw, int roll, int camera_pitch, int camera_x, int camera_y, int camera_z) {
        int center_x = Canvas3D.center_x;
        int center_y = Canvas3D.center_y;
        int pitch_sin = Model.sin[pitch];
        int pitch_cos = Model.cos[pitch];
        int yaw_sin = Model.sin[yaw];
        int yaw_cos = Model.cos[yaw];
        int roll_sin = Model.sin[roll];
        int roll_cos = Model.cos[roll];
        int arc_sin = Model.sin[camera_pitch];
        int arc_cos = Model.cos[camera_pitch];
        int cam_dist = camera_y * arc_sin + camera_z * arc_cos >> 16;

        for (int i = 0; i < vertex_count; i++) {
            int x = vertex_x[i];
            int y = vertex_y[i];
            int z = vertex_z[i];

            if (roll != 0) {
                int x2 = y * roll_sin + x * roll_cos >> 16;
                y = y * roll_cos - x * roll_sin >> 16;
                x = x2;
            }

            if (pitch != 0) {
                int y2 = y * pitch_cos - z * pitch_sin >> 16;
                z = y * pitch_sin + z * pitch_cos >> 16;
                y = y2;
            }

            if (yaw != 0) {
                int x2 = z * yaw_sin + x * yaw_cos >> 16;
                z = z * yaw_cos - x * yaw_sin >> 16;
                x = x2;
            }

            x += camera_x;
            y += camera_y;
            z += camera_z;

            int y2 = y * arc_cos - z * arc_sin >> 16;
            z = y * arc_sin + z * arc_cos >> 16;
            y = y2;

            triangle_depth[i] = z - cam_dist;
            triangle_x[i] = center_x + (x << 9) / z;
            triangle_y[i] = center_y + (y << 9) / z;

            if (textured_triangle_count > 0) {
                tmp_textured_x[i] = x;
                tmp_textured_y[i] = y;
                tmp_textured_z[i] = z;
            }
        }

        try {
            draw(false, false, 0);
            return;
        } catch (Exception _ex) {
            return;
        }
    }

    public void draw_triangle(int i) {
        if (triangle_project[i]) {
            draw_triangle2(i);
            return;
        }

        int x_i = triangle_viewspace_a[i];
        int y_i = triangle_viewspace_b[i];
        int z_i = triangle_viewspace_c[i];

        Canvas3D.check_bounds = triangle_check_bounds[i];

        if (triangle_alpha == null) {
            Canvas3D.opacity = 0;
        } else {
            Canvas3D.opacity = triangle_alpha[i];
        }

        int type;

        if (triangle_info == null) {
            type = 0;
        } else {
            type = triangle_info[i] & 3;
        }

        switch (type) {
            case 0: { // Shaded
                Canvas3D.draw_shaded_triangle(triangle_x[x_i], triangle_y[x_i], triangle_x[y_i], triangle_y[y_i], triangle_x[z_i], triangle_y[z_i], tri_hsl1[i], tri_hsl2[i], tri_hsl3[i]);
                return;
            }
            case 1: { // Flat
                Canvas3D.draw_flat_triangle(triangle_x[x_i], triangle_y[x_i], triangle_x[y_i], triangle_y[y_i], triangle_x[z_i], triangle_y[z_i], palette[tri_hsl1[i]]);
                return;
            }
            case 2: { // Shaded Texture
                int j = triangle_info[i] >> 2;
                int x = texture_map_x[j];
                int y = texture_map_y[j];
                int z = texture_map_z[j];
                Canvas3D.draw_textured_triangle(triangle_x[x_i], triangle_y[x_i], triangle_x[y_i], triangle_y[y_i], triangle_x[z_i], triangle_y[z_i], tri_hsl1[i], tri_hsl2[i], tri_hsl3[i], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                return;
            }
            case 3: { // Flat Texture
                int j = triangle_info[i] >> 2;
                int x = texture_map_x[j];
                int y = texture_map_y[j];
                int z = texture_map_z[j];
                Canvas3D.draw_textured_triangle(triangle_x[x_i], triangle_y[x_i], triangle_x[y_i], triangle_y[y_i], triangle_x[z_i], triangle_y[z_i], tri_hsl1[i], tri_hsl1[i], tri_hsl1[i], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                return;
            }
        }
    }

    public short method465(Model model, int vertex) {
        short j = -1;
        short x = model.vertex_x[vertex];
        short y = model.vertex_y[vertex];
        short z = model.vertex_z[vertex];

        for (short i = 0; i < vertex_count; i++) {
            if (x != vertex_x[i] || y != vertex_y[i] || z != vertex_z[i]) {
                continue;
            }
            j = i;
            break;
        }

        if (j == -1) {
            vertex_x[vertex_count] = x;
            vertex_y[vertex_count] = y;
            vertex_z[vertex_count] = z;

            if (model.vertex_skin_types != null) {
                vertex_skin_types[vertex_count] = model.vertex_skin_types[vertex];
            }

            j = vertex_count++;
        }

        return j;
    }

    public void method466() {
        super.height = 0;
        max_horizon = 0;
        max_y = 0;

        for (int i = 0; i < vertex_count; i++) {
            int x = vertex_x[i];
            int y = vertex_y[i];
            int z = vertex_z[i];

            if (-y > super.height) {
                super.height = -y;
            }

            if (y > max_y) {
                max_y = y;
            }

            int horizon = x * x + z * z;

            if (horizon > max_horizon) {
                max_horizon = horizon;
            }
        }

        max_horizon = (int) (Math.sqrt(max_horizon) + 0.99D);
        unknown2 = (int) (Math.sqrt(max_horizon * max_horizon + super.height * super.height) + 0.99D);
        unknown3 = unknown2 + (int) (Math.sqrt(max_horizon * max_horizon + max_y * max_y) + 0.99D);
    }

    public void method467() {
        super.height = 0;
        max_y = 0;

        for (int i = 0; i < vertex_count; i++) {
            int v_y = vertex_y[i];

            if (-v_y > super.height) {
                super.height = -v_y;
            }

            if (v_y > max_y) {
                max_y = v_y;
            }
        }

        unknown2 = (int) (Math.sqrt(max_horizon * max_horizon + super.height * super.height) + 0.99D);
        unknown3 = unknown2 + (int) (Math.sqrt(max_horizon * max_horizon + max_y * max_y) + 0.99D);
    }

    public void method468() {
        super.height = 0;
        max_horizon = 0;
        max_y = 0;
        min_x = 999999;
        max_x = 0xfff0bdc1;
        max_z = 0xfffe7961;
        min_z = 99999;

        for (int i = 0; i < vertex_count; i++) {
            int x = vertex_x[i];
            int y = vertex_y[i];
            int z = vertex_z[i];

            if (x < min_x) {
                min_x = x;
            }

            if (x > max_x) {
                max_x = x;
            }

            if (z < min_z) {
                min_z = z;
            }

            if (z > max_z) {
                max_z = z;
            }

            if (-y > super.height) {
                super.height = -y;
            }

            if (y > max_y) {
                max_y = y;
            }

            int horizon = x * x + z * z;

            if (horizon > max_horizon) {
                max_horizon = horizon;
            }
        }

        max_horizon = (int) Math.sqrt(max_horizon);
        unknown2 = (int) Math.sqrt(max_horizon * max_horizon + super.height * super.height);
        unknown3 = unknown2 + (int) Math.sqrt(max_horizon * max_horizon + max_y * max_y);
    }

    // TODO: Figure out the sorting method for this.
    public void draw(boolean project, boolean hoverable, int uid) {
        for (int i = 0; i < unknown3; i++) {
            anIntArray1671[i] = 0;
        }

        for (int i = 0; i < triangle_count; i++) {
            if (triangle_info == null || triangle_info[i] != -1) {
                int a = triangle_viewspace_a[i];
                int b = triangle_viewspace_b[i];
                int c = triangle_viewspace_c[i];

                int x1 = triangle_x[a];
                int x2 = triangle_x[b];
                int x3 = triangle_x[c];

                if (project && (x1 == -5000 || x2 == -5000 || x3 == -5000)) {
                    triangle_project[i] = true;
                    int depth = (triangle_depth[a] + triangle_depth[b] + triangle_depth[c]) / 3 + unknown2;
                    anIntArrayArray1672[depth][anIntArray1671[depth]++] = i;
                } else {
                    if (hoverable && tri_contains(mouse_x, mouse_y, x1, triangle_y[a], triangle_y[b], x2, triangle_y[c], x3)) {
                        hovered_uid[hovered_count++] = uid;
                        hoverable = false;
                    }

                    if ((x1 - x2) * (triangle_y[c] - triangle_y[b]) - (triangle_y[a] - triangle_y[b]) * (x3 - x2) > 0) {
                        triangle_project[i] = false;

                        if (x1 < 0 || x2 < 0 || x3 < 0 || x1 > Canvas2D.bound || x2 > Canvas2D.bound || x3 > Canvas2D.bound) {
                            triangle_check_bounds[i] = true;
                        } else {
                            triangle_check_bounds[i] = false;
                        }

                        int depth = (triangle_depth[a] + triangle_depth[b] + triangle_depth[c]) / 3 + unknown2;

                        anIntArrayArray1672[depth][anIntArray1671[depth]++] = i;
                    }
                }
            }
        }

        if (triangle_priority == null) {
            for (int i = unknown3 - 1; i >= 0; i--) {
                int l1 = anIntArray1671[i];

                if (l1 > 0) {
                    int[] triangles = anIntArrayArray1672[i];

                    for (int k = 0; k < l1; k++) {
                        draw_triangle(triangles[k]);
                    }
                }
            }
            return;
        }

        for (int priority = 0; priority < 12; priority++) {
            anIntArray1673[priority] = 0;
            anIntArray1677[priority] = 0;
        }

        for (int i = unknown3 - 1; i >= 0; i--) {
            int k2 = anIntArray1671[i];
            if (k2 > 0) {
                int[] triangles = anIntArrayArray1672[i];

                for (int j = 0; j < k2; j++) {
                    int triangle = triangles[j];
                    int priority = triangle_priority[triangle];
                    int j6 = anIntArray1673[priority]++;

                    anIntArrayArray1674[priority][j6] = triangle;

                    if (priority < 10) {
                        anIntArray1677[priority] += i;
                    } else if (priority == 10) {
                        anIntArray1675[j6] = i;
                    } else {
                        anIntArray1676[j6] = i;
                    }
                }

            }
        }

        int l2 = 0;
        if (anIntArray1673[1] > 0 || anIntArray1673[2] > 0) {
            l2 = (anIntArray1677[1] + anIntArray1677[2]) / (anIntArray1673[1] + anIntArray1673[2]);
        }

        int k3 = 0;
        if (anIntArray1673[3] > 0 || anIntArray1673[4] > 0) {
            k3 = (anIntArray1677[3] + anIntArray1677[4]) / (anIntArray1673[3] + anIntArray1673[4]);
        }

        int j4 = 0;
        if (anIntArray1673[6] > 0 || anIntArray1673[8] > 0) {
            j4 = (anIntArray1677[6] + anIntArray1677[8]) / (anIntArray1673[6] + anIntArray1673[8]);
        }

        int i = 0;
        int k6 = anIntArray1673[10];
        int triangles[] = anIntArrayArray1674[10];
        int ai3[] = anIntArray1675;

        if (i == k6) {
            i = 0;
            k6 = anIntArray1673[11];
            triangles = anIntArrayArray1674[11];
            ai3 = anIntArray1676;
        }

        int i5;
        if (i < k6) {
            i5 = ai3[i];
        } else {
            i5 = -1000;
        }

        for (int l6 = 0; l6 < 10; l6++) {
            while (l6 == 0 && i5 > l2) {
                draw_triangle(triangles[i++]);

                if (i == k6 && triangles != anIntArrayArray1674[11]) {
                    i = 0;
                    k6 = anIntArray1673[11];
                    triangles = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }

                if (i < k6) {
                    i5 = ai3[i];
                } else {
                    i5 = -1000;
                }
            }

            while (l6 == 3 && i5 > k3) {
                draw_triangle(triangles[i++]);
                if (i == k6 && triangles != anIntArrayArray1674[11]) {
                    i = 0;
                    k6 = anIntArray1673[11];
                    triangles = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i < k6) {
                    i5 = ai3[i];
                } else {
                    i5 = -1000;
                }
            }

            while (l6 == 5 && i5 > j4) {
                draw_triangle(triangles[i++]);
                if (i == k6 && triangles != anIntArrayArray1674[11]) {
                    i = 0;
                    k6 = anIntArray1673[11];
                    triangles = anIntArrayArray1674[11];
                    ai3 = anIntArray1676;
                }
                if (i < k6) {
                    i5 = ai3[i];
                } else {
                    i5 = -1000;
                }
            }
            int i7 = anIntArray1673[l6];
            int ai4[] = anIntArrayArray1674[l6];
            for (int j7 = 0; j7 < i7; j7++) {
                draw_triangle(ai4[j7]);
            }
        }

        while (i5 != -1000) {
            draw_triangle(triangles[i++]);
            if (i == k6 && triangles != anIntArrayArray1674[11]) {
                i = 0;
                triangles = anIntArrayArray1674[11];
                k6 = anIntArray1673[11];
                ai3 = anIntArray1676;
            }
            if (i < k6) {
                i5 = ai3[i];
            } else {
                i5 = -1000;
            }
        }
    }

    public void draw_triangle2(int i) {
        int center_x = Canvas3D.center_x;
        int center_y = Canvas3D.center_y;

        int j = 0;

        int a = triangle_viewspace_a[i];
        int b = triangle_viewspace_b[i];
        int c = triangle_viewspace_c[i];

        int a_z = tmp_textured_z[a];
        int b_z = tmp_textured_z[b];
        int c_z = tmp_textured_z[c];

        if (a_z >= 50) {
            tmp_screen_x[j] = triangle_x[a];
            tmp_screen_y[j] = triangle_y[a];
            tmp_hsl[j++] = tri_hsl1[i];
        } else {
            int x = tmp_textured_x[a];
            int y = tmp_textured_y[a];
            int hsl = tri_hsl1[i];

            if (c_z >= 50) {
                int decay = (50 - a_z) * shadow_decay[c_z - a_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[c] - x) * decay >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[c] - y) * decay >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl3[i] - hsl) * decay >> 16);
            }

            if (b_z >= 50) {
                int decay = (50 - a_z) * shadow_decay[b_z - a_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[b] - x) * decay >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[b] - y) * decay >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl2[i] - hsl) * decay >> 16);
            }
        }

        if (b_z >= 50) {
            tmp_screen_x[j] = triangle_x[b];
            tmp_screen_y[j] = triangle_y[b];
            tmp_hsl[j++] = tri_hsl2[i];
        } else {
            int x = tmp_textured_x[b];
            int y = tmp_textured_y[b];
            int hsl = tri_hsl2[i];

            if (a_z >= 50) {
                int i6 = (50 - b_z) * shadow_decay[a_z - b_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[a] - x) * i6 >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[a] - y) * i6 >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl1[i] - hsl) * i6 >> 16);
            }

            if (c_z >= 50) {
                int j6 = (50 - b_z) * shadow_decay[c_z - b_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[c] - x) * j6 >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[c] - y) * j6 >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl3[i] - hsl) * j6 >> 16);
            }
        }

        if (c_z >= 50) {
            tmp_screen_x[j] = triangle_x[c];
            tmp_screen_y[j] = triangle_y[c];
            tmp_hsl[j++] = tri_hsl3[i];
        } else {
            int x = tmp_textured_x[c];
            int y = tmp_textured_y[c];
            int hsl = tri_hsl3[i];
            if (b_z >= 50) {
                int k6 = (50 - c_z) * shadow_decay[b_z - c_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[b] - x) * k6 >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[b] - y) * k6 >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl2[i] - hsl) * k6 >> 16);
            }
            if (a_z >= 50) {
                int l6 = (50 - c_z) * shadow_decay[a_z - c_z];
                tmp_screen_x[j] = center_x + (x + ((tmp_textured_x[a] - x) * l6 >> 16) << 9) / 50;
                tmp_screen_y[j] = center_y + (y + ((tmp_textured_y[a] - y) * l6 >> 16) << 9) / 50;
                tmp_hsl[j++] = hsl + ((tri_hsl1[i] - hsl) * l6 >> 16);
            }
        }

        int x0 = tmp_screen_x[0];
        int x1 = tmp_screen_x[1];
        int x2 = tmp_screen_x[2];
        int y0 = tmp_screen_y[0];
        int y1 = tmp_screen_y[1];
        int y2 = tmp_screen_y[2];

        if ((x0 - x1) * (y2 - y1) - (y0 - y1) * (x2 - x1) > 0) {
            Canvas3D.check_bounds = false;
            if (j == 3) {
                if (x0 < 0 || x1 < 0 || x2 < 0 || x0 > Canvas2D.bound || x1 > Canvas2D.bound || x2 > Canvas2D.bound) {
                    Canvas3D.check_bounds = true;
                }

                int type;

                if (triangle_info == null) {
                    type = 0;
                } else {
                    type = triangle_info[i] & 3;
                }

                if (type == 0) {
                    Canvas3D.draw_shaded_triangle(x0, y0, x1, y1, x2, y2, tmp_hsl[0], tmp_hsl[1], tmp_hsl[2]);
                } else if (type == 1) {
                    Canvas3D.draw_flat_triangle(x0, y0, x1, y1, x2, y2, palette[tri_hsl1[i]]);
                } else if (type == 2) {
                    int k = triangle_info[i] >> 2;
                    int x = texture_map_x[k];
                    int y = texture_map_y[k];
                    int z = texture_map_z[k];
                    Canvas3D.draw_textured_triangle(x0, y0, x1, y1, x2, y2, tmp_hsl[0], tmp_hsl[1], tmp_hsl[2], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                } else if (type == 3) {
                    int k = triangle_info[i] >> 2;
                    int x = texture_map_x[k];
                    int y = texture_map_y[k];
                    int z = texture_map_z[k];
                    Canvas3D.draw_textured_triangle(x0, y0, x1, y1, x2, y2, tri_hsl1[i], tri_hsl1[i], tri_hsl1[i], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                }
            } else if (j == 4) {
                if (x0 < 0 || x1 < 0 || x2 < 0 || x0 > Canvas2D.bound || x1 > Canvas2D.bound || x2 > Canvas2D.bound || tmp_screen_x[3] < 0 || tmp_screen_x[3] > Canvas2D.bound) {
                    Canvas3D.check_bounds = true;
                }

                int type;

                if (triangle_info == null) {
                    type = 0;
                } else {
                    type = triangle_info[i] & 3;
                }

                if (type == 0) {
                    Canvas3D.draw_shaded_triangle(x0, y0, x1, y1, x2, y2, tmp_hsl[0], tmp_hsl[1], tmp_hsl[2]);
                    Canvas3D.draw_shaded_triangle(x0, y0, x2, y2, tmp_screen_x[3], tmp_screen_y[3], tmp_hsl[0], tmp_hsl[2], tmp_hsl[3]);
                } else if (type == 1) {
                    int rgb = palette[tri_hsl1[i]];
                    Canvas3D.draw_flat_triangle(x0, y0, x1, y1, x2, y2, rgb);
                    Canvas3D.draw_flat_triangle(x0, y0, x2, y2, tmp_screen_x[3], tmp_screen_y[3], rgb);
                } else if (type == 2) {
                    int k = triangle_info[i] >> 2;
                    int x = texture_map_x[k];
                    int y = texture_map_y[k];
                    int z = texture_map_z[k];
                    Canvas3D.draw_textured_triangle(x0, y0, x1, y1, x2, y2, tmp_hsl[0], tmp_hsl[1], tmp_hsl[2], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                    Canvas3D.draw_textured_triangle(x0, y0, x2, y2, tmp_screen_x[3], tmp_screen_y[3], tmp_hsl[0], tmp_hsl[2], tmp_hsl[3], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                } else if (type == 3) {
                    int k = triangle_info[i] >> 2;
                    int x = texture_map_x[k];
                    int y = texture_map_y[k];
                    int z = texture_map_z[k];
                    Canvas3D.draw_textured_triangle(x0, y0, x1, y1, x2, y2, tri_hsl1[i], tri_hsl1[i], tri_hsl1[i], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                    Canvas3D.draw_textured_triangle(x0, y0, x2, y2, tmp_screen_x[3], tmp_screen_y[3], tri_hsl1[i], tri_hsl1[i], tri_hsl1[i], tmp_textured_x[x], tmp_textured_y[x], tmp_textured_z[x], tmp_textured_x[y], tmp_textured_y[y], tmp_textured_z[y], tmp_textured_x[z], tmp_textured_y[z], tmp_textured_z[z], triangle_color[i]);
                }
            }
        }
    }

    @Override
    public void render(int rotation, int pitch_sin, int pitch_cos, int yaw_sin, int yaw_cos, int x, int y, int z, int uid) {
        int j2 = y * yaw_cos - x * yaw_sin >> 16;
        int cam_dist = z * pitch_sin + j2 * pitch_cos >> 16;
        int l2 = max_horizon * pitch_cos >> 16;
        int angle = cam_dist + l2;

        if (angle <= 50 || cam_dist >= 3500) {
            return;
        }

        int j3 = y * yaw_sin + x * yaw_cos >> 16;

        int x1 = j3 - max_horizon << 9;
        if (x1 / angle >= Canvas2D.center_x) {
            return;
        }

        int x2 = j3 + max_horizon << 9;
        if (x2 / angle <= -Canvas2D.center_x) {
            return;
        }

        int i4 = z * pitch_cos - j2 * pitch_sin >> 16;
        int j4 = max_horizon * pitch_sin >> 16;

        int y2 = i4 + j4 << 9;
        if (y2 / angle <= -Canvas2D.center_y) {
            return;
        }

        int l4 = j4 + (super.height * pitch_cos >> 16);
        int y1 = i4 - l4 << 9;
        if (y1 / angle >= Canvas2D.center_y) {
            return;
        }

        int j5 = l2 + (super.height * pitch_sin >> 16);
        boolean flag = false;

        if (cam_dist - j5 <= 50) {
            flag = true;
        }

        boolean hoverable = false;

        if (uid > 0 && scene_clickable) {
            int k5 = cam_dist - l2;

            if (k5 <= 50) {
                k5 = 50;
            }

            if (j3 > 0) {
                x1 /= angle;
                x2 /= k5;
            } else {
                x2 /= angle;
                x1 /= k5;
            }

            if (i4 > 0) {
                y1 /= angle;
                y2 /= k5;
            } else {
                y2 /= angle;
                y1 /= k5;
            }

            int dx = mouse_x - Canvas3D.center_x;
            int dy = mouse_y - Canvas3D.center_y;
            if (dx > x1 && dx < x2 && dy > y1 && dy < y2) {
                if (is_clickable) {
                    hovered_uid[hovered_count++] = uid;
                } else {
                    hoverable = true;
                }
            }
        }

        int center_x = Canvas3D.center_x;
        int center_y = Canvas3D.center_y;
        int sin = 0;
        int cos = 0;

        if (rotation != 0) {
            sin = Model.sin[rotation];
            cos = Model.cos[rotation];
        }

        for (int v = 0; v < vertex_count; v++) {
            int v_x = vertex_x[v];
            int v_y = vertex_y[v];
            int v_z = vertex_z[v];

            if (rotation != 0) {
                int j8 = v_z * sin + v_x * cos >> 16;
                v_z = v_z * cos - v_x * sin >> 16;
                v_x = j8;
            }

            v_x += x;
            v_y += z;
            v_z += y;

            int i = v_z * yaw_sin + v_x * yaw_cos >> 16;
            v_z = v_z * yaw_cos - v_x * yaw_sin >> 16;
            v_x = i;

            i = v_y * pitch_cos - v_z * pitch_sin >> 16;
            v_z = v_y * pitch_sin + v_z * pitch_cos >> 16;
            v_y = i;

            triangle_depth[v] = v_z - cam_dist;

            if (v_z >= 50) {
                triangle_x[v] = center_x + (v_x << 9) / v_z;
                triangle_y[v] = center_y + (v_y << 9) / v_z;
            } else {
                triangle_x[v] = -5000;
                flag = true;
            }

            if (flag || textured_triangle_count > 0) {
                tmp_textured_x[v] = v_x;
                tmp_textured_y[v] = v_y;
                tmp_textured_z[v] = v_z;
            }
        }

        try {
            draw(flag, hoverable, uid);
        } catch (Exception e) {
        }
    }

    public void replace(Model m, boolean flag) {
        vertex_count = m.vertex_count;
        triangle_count = m.triangle_count;
        textured_triangle_count = m.textured_triangle_count;

        if (replace_vertex_x.length < vertex_count) {
            replace_vertex_x = new short[vertex_count + 100];
            replace_vertex_y = new short[vertex_count + 100];
            replace_vertex_z = new short[vertex_count + 100];
        }

        vertex_x = replace_vertex_x;
        vertex_y = replace_vertex_y;
        vertex_z = replace_vertex_z;

        for (int k = 0; k < vertex_count; k++) {
            vertex_x[k] = m.vertex_x[k];
            vertex_y[k] = m.vertex_y[k];
            vertex_z[k] = m.vertex_z[k];
        }

        if (flag) {
            triangle_alpha = m.triangle_alpha;
        } else {
            if (anIntArray1625.length < triangle_count) {
                anIntArray1625 = new int[triangle_count + 100];
            }
            triangle_alpha = anIntArray1625;

            if (m.triangle_alpha == null) {
                for (int l = 0; l < triangle_count; l++) {
                    triangle_alpha[l] = 0;
                }
            } else {
                for (int i1 = 0; i1 < triangle_count; i1++) {
                    triangle_alpha[i1] = m.triangle_alpha[i1];
                }

            }
        }

        triangle_info = m.triangle_info;
        triangle_color = m.triangle_color;
        triangle_priority = m.triangle_priority;
        anInt1641 = m.anInt1641;
        triangle_groups = m.triangle_groups;
        vertex_weights = m.vertex_weights;
        triangle_viewspace_a = m.triangle_viewspace_a;
        triangle_viewspace_b = m.triangle_viewspace_b;
        triangle_viewspace_c = m.triangle_viewspace_c;
        tri_hsl1 = m.tri_hsl1;
        tri_hsl2 = m.tri_hsl2;
        tri_hsl3 = m.tri_hsl3;
        texture_map_x = m.texture_map_x;
        texture_map_y = m.texture_map_y;
        texture_map_z = m.texture_map_z;
    }

    public void rotate_ccw() {
        for (int i = 0; i < vertex_count; i++) {
            vertex_z[i] = (short) -vertex_z[i];
        }

        for (int i = 0; i < triangle_count; i++) {
            int n_z = triangle_viewspace_a[i];
            triangle_viewspace_a[i] = triangle_viewspace_c[i];
            triangle_viewspace_c[i] = (short) n_z;
        }
    }

    public void rotate_cw() {
        for (int i = 0; i < vertex_count; i++) {
            int new_z = vertex_x[i];
            vertex_x[i] = vertex_z[i];
            vertex_z[i] = (short) -new_z;
        }
    }

    public void scale(int x, int y, int z) {
        for (int i = 0; i < vertex_count; i++) {
            vertex_x[i] = (short) ((vertex_x[i] * x) / 128);
            vertex_y[i] = (short) ((vertex_y[i] * y) / 128);
            vertex_z[i] = (short) ((vertex_z[i] * z) / 128);
        }

    }

    public void set_color(int from, int to) {
        for (int i = 0; i < this.triangle_count; i++) {
            if (this.triangle_color[i] == from) {
                this.triangle_color[i] = to;
            }
        }
    }

    public void set_colors(int[] from, int[] to) {
        if (from.length != to.length) {
            return;
        }
        for (int i = 0; i < from.length; i++) {
            this.set_color(from[i], to[i]);
        }
    }

    public void set_pitch(int pitch) {
        int sin = Model.sin[pitch];
        int cos = Model.cos[pitch];
        for (int i = 0; i < vertex_count; i++) {
            int new_y = vertex_y[i] * cos - vertex_z[i] * sin >> 16;
            vertex_z[i] = (short) (vertex_y[i] * sin + vertex_z[i] * cos >> 16);
            vertex_y[i] = (short) new_y;
        }
    }

    public void transform(int opcode, int vertices[], int x, int y, int z) {
        int vertex_count = vertices.length;

        if (opcode == 0) {
            int j1 = 0;
            anInt1681 = 0;
            anInt1682 = 0;
            anInt1683 = 0;

            for (int i = 0; i < vertex_count; i++) {
                int j = vertices[i];
                if (j < vertex_weights.length) {
                    int v_weights[] = vertex_weights[j];
                    for (int v_weight = 0; v_weight < v_weights.length; v_weight++) {
                        int weight_vertex = v_weights[v_weight];
                        anInt1681 += vertex_x[weight_vertex];
                        anInt1682 += vertex_y[weight_vertex];
                        anInt1683 += vertex_z[weight_vertex];
                        j1++;
                    }
                }
            }

            if (j1 > 0) {
                anInt1681 = anInt1681 / j1 + x;
                anInt1682 = anInt1682 / j1 + y;
                anInt1683 = anInt1683 / j1 + z;
                return;
            } else {
                anInt1681 = x;
                anInt1682 = y;
                anInt1683 = z;
                return;
            }
        }

        // Translation
        if (opcode == 1) {
            for (int i = 0; i < vertex_count; i++) {
                int j = vertices[i];
                if (j < vertex_weights.length) {
                    int vertice_indices[] = vertex_weights[j];
                    for (int k = 0; k < vertice_indices.length; k++) {
                        int v_index = vertice_indices[k];
                        vertex_x[v_index] += x;
                        vertex_y[v_index] += y;
                        vertex_z[v_index] += z;
                    }
                }
            }
            return;
        }

        // Rotation
        if (opcode == 2) {
            for (int i = 0; i < vertex_count; i++) {
                int j = vertices[i];
                if (j < vertex_weights.length) {
                    int vertice_indices[] = vertex_weights[j];
                    for (int k = 0; k < vertice_indices.length; k++) {
                        int v_index = vertice_indices[k];
                        vertex_x[v_index] -= anInt1681;
                        vertex_y[v_index] -= anInt1682;
                        vertex_z[v_index] -= anInt1683;
                        int pitch = (x & 0xff) * 8;
                        int yaw = (y & 0xff) * 8;
                        int roll = (z & 0xff) * 8;

                        if (roll != 0) {
                            int sin = Model.sin[roll];
                            int cos = Model.cos[roll];
                            int new_x = vertex_y[v_index] * sin + vertex_x[v_index] * cos >> 16;
                            vertex_y[v_index] = (short) (vertex_y[v_index] * cos - vertex_x[v_index] * sin >> 16);
                            vertex_x[v_index] = (short) new_x;
                        }

                        if (pitch != 0) {
                            int sin = Model.sin[pitch];
                            int cos = Model.cos[pitch];
                            int new_y = vertex_y[v_index] * cos - vertex_z[v_index] * sin >> 16;
                            vertex_z[v_index] = (short) (vertex_y[v_index] * sin + vertex_z[v_index] * cos >> 16);
                            vertex_y[v_index] = (short) new_y;
                        }

                        if (yaw != 0) {
                            int sin = Model.sin[yaw];
                            int cos = Model.cos[yaw];
                            int new_z = vertex_z[v_index] * sin + vertex_x[v_index] * cos >> 16;
                            vertex_z[v_index] = (short) (vertex_z[v_index] * cos - vertex_x[v_index] * sin >> 16);
                            vertex_x[v_index] = (short) new_z;
                        }

                        vertex_x[v_index] += anInt1681;
                        vertex_y[v_index] += anInt1682;
                        vertex_z[v_index] += anInt1683;
                    }

                }
            }

            return;
        }

        if (opcode == 3) {
            for (int i = 0; i < vertex_count; i++) {
                int j = vertices[i];
                if (j < vertex_weights.length) {
                    int vertex_index[] = vertex_weights[j];
                    for (int k = 0; k < vertex_index.length; k++) {
                        int v = vertex_index[k];
                        vertex_x[v] -= anInt1681;
                        vertex_y[v] -= anInt1682;
                        vertex_z[v] -= anInt1683;
                        vertex_x[v] = (short) ((vertex_x[v] * x) / 128);
                        vertex_y[v] = (short) ((vertex_y[v] * y) / 128);
                        vertex_z[v] = (short) ((vertex_z[v] * z) / 128);
                        vertex_x[v] += anInt1681;
                        vertex_y[v] += anInt1682;
                        vertex_z[v] += anInt1683;
                    }

                }
            }

            return;
        }

        // Set Alpha
        if (opcode == 5 && triangle_groups != null && triangle_alpha != null) {
            for (int i = 0; i < vertex_count; i++) {
                int group = vertices[i];

                if (group < triangle_groups.length) {
                    int tri_indices[] = triangle_groups[group];

                    for (int k = 0; k < tri_indices.length; k++) {
                        int tri_index = tri_indices[k];

                        triangle_alpha[tri_index] += x * 8;

                        if (triangle_alpha[tri_index] < 0) {
                            triangle_alpha[tri_index] = 0;
                        } else if (triangle_alpha[tri_index] > 255) {
                            triangle_alpha[tri_index] = 255;
                        }
                    }

                }
            }

        }
    }

    public void translate(int x, int y, int z) {
        for (int i = 0; i < vertex_count; i++) {
            vertex_x[i] += x;
            vertex_y[i] += y;
            vertex_z[i] += z;
        }

    }

    public boolean tri_contains(int x, int y, int x1, int y1, int y2, int x2, int y3, int x3) {
        if (y < y1 && y < y2 && y < y3) {
            return false;
        }
        if (y > y1 && y > y2 && y > y3) {
            return false;
        }
        if (x < x1 && x < x2 && x < x3) {
            return false;
        }
        return x <= x1 || x <= x2 || x <= x3;
    }

}
