package rs.cache.model;

import rs.Game;
import rs.cache.Archive;
import rs.cache.impl.VarBit;
import rs.io.Buffer;
import rs.io.OnDemand;
import rs.node.List;
import rs.scene.model.Model;

public class LocConfig {

    public static Buffer buffer;
    public static LocConfig[] cache;
    public static int cache_position;
    public static int count;
    public static List model_cache = new List(30);
    public static int[] pointer;
    public static List static_model_cache = new List(500);
    public static Model[] tmp_model = new Model[4];

    public static LocConfig get(int i) {
        if (i > pointer.length) {
            System.out.println("Invalid loc index: " + i);
            return null;
        }

        for (LocConfig config : LocConfig.cache) {
            if (config.index == i) {
                return config;
            }
        }

        cache_position = (cache_position + 1) % 20;
        LocConfig config = cache[cache_position];
        buffer.position = pointer[i];
        config.index = i;
        config.set_defaults();
        config.load(buffer);
        return config;
    }

    public static void nullify() {
        static_model_cache = null;
        model_cache = null;
        pointer = null;
        cache = null;
        buffer = null;
    }

    public static void unpack(Archive a) {
        buffer = new Buffer(a.get("loc.dat", null));
        Buffer s = new Buffer(a.get("loc.idx", null));
        count = s.get_ushort();
        pointer = new int[count];

        int i = 2;
        for (int j = 0; j < count; j++) {
            pointer[j] = i;
            i += s.get_ushort();
        }

        cache = new LocConfig[20];

        for (int k = 0; k < 20; k++) {
            cache[k] = new LocConfig();
        }
    }

    public String[] action;
    public boolean adjust_to_terrain;
    public boolean blocks_projectiles;
    public byte brightness;
    public boolean casts_shadow;
    public String description;
    public int face_flags;
    public boolean flat_shading;
    public boolean ghost;
    public boolean has_collisions;
    public short icon;
    public int index;
    public boolean is_decoration;
    public boolean is_solid;
    public boolean is_static;
    public short[] model_index;
    public byte[] model_type;
    public String name;
    public int[] new_color;
    public short offset_x;
    public short offset_y;
    public short offset_z;
    public int[] old_color;
    public short[] override_index;
    public int raises_item_piles;
    public boolean rotate_ccw;
    public short scale_x;
    public short scale_y;
    public short scale_z;
    public int scene_image_index;
    public short seq_index;
    public short setting_index;
    public byte size_x;
    public byte size_y;
    public byte specular;
    public short varbit_index;
    public byte wall_width;

    public LocConfig() {
        index = -1;
    }

    public Model get_assembled_model(int type, int frame, int rotation) {
        Model m = null;
        long uid;

        if (model_type == null) {
            if (type != 10) {
                return null;
            }

            uid = (long) ((index << 6) + rotation) + ((long) (frame + 1) << 32);

            m = (Model) model_cache.get(uid);

            if (m != null) {
                return m;
            }

            if (model_index == null) {
                return null;
            }

            boolean rotate = rotate_ccw ^ (rotation > 3);
            int count = model_index.length;

            for (int i = 0; i < count; i++) {
                int index = model_index[i];

                if (rotate) {
                    index += 0x10000;
                }

                m = (Model) static_model_cache.get(index);

                if (m == null) {
                    m = Model.get(index & 0xffff);

                    if (m == null) {
                        return null;
                    }

                    if (rotate) {
                        m.rotate_ccw();
                    }

                    static_model_cache.insert(m, index);
                }

                if (count > 1) {
                    tmp_model[i] = m;
                }
            }

            if (count > 1) {
                m = new Model(count, tmp_model);
            }
        } else {
            int model_list_index = -1;

            for (int i = 0; i < model_type.length; i++) {
                if (model_type[i] != type) {
                    continue;
                }
                model_list_index = i;
                break;
            }

            if (model_list_index == -1) {
                return null;
            }

            uid = (long) ((index << 6) + (model_list_index << 3) + rotation) + ((long) (frame + 1) << 32);

            Model cached_mesh = (Model) model_cache.get(uid);

            if (cached_mesh != null) {
                return cached_mesh;
            }

            int mesh_index = model_index[model_list_index];
            boolean rotate = rotate_ccw ^ (rotation > 3);

            if (rotate) {
                mesh_index += 0x10000;
            }

            m = (Model) static_model_cache.get(mesh_index);

            if (m == null) {
                m = Model.get(mesh_index & 0xffff);

                if (m == null) {
                    return null;
                }

                if (rotate) {
                    m.rotate_ccw();
                }

                static_model_cache.insert(m, mesh_index);
            }
        }

        boolean rescale, translate;

        if (scale_x != 128 || scale_y != 128 || scale_z != 128) {
            rescale = true;
        } else {
            rescale = false;
        }

        if (offset_x != 0 || offset_y != 0 || offset_z != 0) {
            translate = true;
        } else {
            translate = false;
        }

        Model model = new Model(old_color == null, frame == -1, rotation == 0 && frame == -1 && !rescale && !translate, m);

        if (frame != -1) {
            model.apply_vertex_weights();
            model.apply_sequence_frame(frame);
            model.triangle_groups = null;
            model.vertex_weights = null;
        }

        while (rotation-- > 0) {
            model.rotate_cw();
        }

        if (old_color != null) {
            model.set_colors(old_color, new_color);
        }

        if (rescale) {
            model.scale(scale_x, scale_y, scale_z);
        }

        if (translate) {
            model.translate(offset_x, offset_y, offset_z);
        }

        model.apply_lighting(64 + brightness, 768 + specular * 5, -50, -10, -50, !flat_shading);

        if (raises_item_piles == 1) {
            model.pile_height = model.height;
        }

        model_cache.insert(model, uid);
        return model;

    }

    public Model get_model(int type, int rotation, int v_sw, int v_se, int v_ne, int v_nw, int frame) {
        Model m = get_assembled_model(type, frame, rotation);

        if (m == null) {
            return null;
        }

        if (adjust_to_terrain || flat_shading) {
            m = new Model(adjust_to_terrain, flat_shading, m);
        }

        if (adjust_to_terrain) {
            int v_avg = (v_sw + v_se + v_ne + v_nw) / 4;

            for (int i = 0; i < m.vertex_count; i++) {
                int x = m.vertex_x[i];
                int z = m.vertex_z[i];
                int l2 = v_sw + ((v_se - v_sw) * (x + 64)) / 128;
                int i3 = v_nw + ((v_ne - v_nw) * (x + 64)) / 128;
                int v_y = l2 + ((i3 - l2) * (z + 64)) / 128;
                m.vertex_y[i] += v_y - v_avg;
            }

            m.method467();
        }
        return m;
    }

    public LocConfig get_overriding_config() {
        int i = -1;

        if (varbit_index != -1) {
            VarBit varbit = VarBit.instance[varbit_index];
            int j = varbit.setting;
            int k = varbit.offset;
            int l = varbit.shift;
            int i1 = Game.LSB_BIT_MASK[l - k];
            i = Game.settings[j] >> k & i1;
        } else if (setting_index != -1) {
            i = Game.settings[setting_index];
        }

        if (i < 0 || i >= override_index.length || override_index[i] == -1) {
            return null;
        }

        return get(override_index[i]);
    }

    public boolean has_valid_model() {
        if (model_index == null) {
            return true;
        }
        boolean flag1 = true;

        for (int i = 0; i < model_index.length; i++) {
            flag1 &= Model.is_valid(model_index[i] & 0xffff);
        }

        return flag1;
    }

    public boolean is_valid_model(int type) {
        if (model_type == null) {
            if (model_index == null) {
                return true;
            }

            if (type != 10) {
                return true;
            }

            boolean valid = true;

            for (int i = 0; i < model_index.length; i++) {
                valid &= Model.is_valid(model_index[i] & 0xffff);
            }

            return valid;
        }

        for (int i = 0; i < model_type.length; i++) {
            if (model_type[i] == type) {
                return Model.is_valid(model_index[i] & 0xffff);
            }
        }

        return true;
    }

    public void load(Buffer b) {
        int i = -1;
        LOAD:
        do {
            int opcode;
            do {
                opcode = b.get_ubyte();
                if (opcode == 0) {
                    break LOAD;
                }

                if (opcode == 1) {
                    int count = b.get_ubyte();
                    if (count > 0) {
                        if (model_index == null || Game.low_detail) {
                            model_index = new short[count];
                            model_type = new byte[count];
                            for (int j = 0; j < count; j++) {
                                model_index[j] = (short) b.get_ushort();
                                model_type[j] = b.get_byte();
                            }
                        } else {
                            b.position += count * 3;
                        }
                    }
                } else if (opcode == 2) {
                    name = b.get_string();
                } else if (opcode == 3) {
                    description = b.get_string();
                } else if (opcode == 5) {
                    int count = b.get_ubyte();
                    if (count > 0) {
                        if (model_index == null || Game.low_detail) {
                            model_type = null;
                            model_index = new short[count];
                            for (int j = 0; j < count; j++) {
                                model_index[j] = (short) b.get_ushort();
                            }
                        } else {
                            b.position += count * 2;
                        }
                    }
                } else if (opcode == 14) {
                    size_x = (byte) b.get_ubyte();
                } else if (opcode == 15) {
                    size_y = (byte) b.get_ubyte();
                } else if (opcode == 17) {
                    has_collisions = false;
                } else if (opcode == 18) {
                    blocks_projectiles = false;
                } else if (opcode == 19) {
                    i = b.get_ubyte();
                    if (i == 1) {
                        is_static = true;
                    }
                } else if (opcode == 21) {
                    adjust_to_terrain = true;
                } else if (opcode == 22) {
                    flat_shading = true;
                } else if (opcode == 23) {
                    is_solid = true;
                } else if (opcode == 24) {
                    seq_index = (short) b.get_ushort();
                } else if (opcode == 28) {
                    wall_width = b.get_byte();
                } else if (opcode == 29) {
                    brightness = b.get_byte();
                } else if (opcode == 39) {
                    specular = b.get_byte();
                } else if (opcode >= 30 && opcode < 39) {
                    if (action == null) {
                        action = new String[5];
                    }

                    action[opcode - 30] = b.get_string();

                    if (action[opcode - 30].equalsIgnoreCase("hidden")) {
                        action[opcode - 30] = null;
                    }
                } else if (opcode == 40) {
                    int j = b.get_ubyte();
                    old_color = new int[j];
                    new_color = new int[j];

                    for (int k = 0; k < j; k++) {
                        old_color[k] = b.get_ushort();
                        new_color[k] = b.get_ushort();
                    }
                } else if (opcode == 60) {
                    icon = (short) b.get_ushort();
                } else if (opcode == 62) {
                    rotate_ccw = true;
                } else if (opcode == 64) {
                    casts_shadow = false;
                } else if (opcode == 65) {
                    scale_x = (short) b.get_ushort();
                } else if (opcode == 66) {
                    scale_y = (short) b.get_ushort();
                } else if (opcode == 67) {
                    scale_z = (short) b.get_ushort();
                } else if (opcode == 68) {
                    scene_image_index = b.get_ushort();
                } else if (opcode == 69) {
                    face_flags = b.get_ubyte();
                } else if (opcode == 70) {
                    offset_x = (short) b.get_ushort();
                } else if (opcode == 71) {
                    offset_y = (short) b.get_ushort();
                } else if (opcode == 72) {
                    offset_z = (short) b.get_ushort();
                } else if (opcode == 73) {
                    is_decoration = true;
                } else if (opcode == 74) {
                    ghost = true;
                } else {
                    if (opcode != 75) {
                        continue;
                    }
                    raises_item_piles = b.get_ubyte();
                }
                continue LOAD;
            } while (opcode != 77);

            varbit_index = (short) b.get_ushort();
            setting_index = (short) b.get_ushort();
            override_index = new short[b.get_ubyte() + 1];

            for (int j = 0; j <= override_index.length - 1; j++) {
                override_index[j] = (short) b.get_ushort();
            }
        } while (true);

        if (i == -1) {
            is_static = false;

            if (model_index != null && (model_type == null || model_type[0] == 10)) {
                is_static = true;
            }

            if (action != null) {
                is_static = true;
            }
        }

        if (ghost) {
            has_collisions = false;
            blocks_projectiles = false;
        }

        if (raises_item_piles == -1) {
            raises_item_piles = has_collisions ? 1 : 0;
        }
    }

    public void request_models(OnDemand ondemand) {
        if (model_index == null) {
            return;
        }

        for (int i = 0; i < model_index.length; i++) {
            ondemand.request(model_index[i] & 0xffff, 0);
        }
    }

    public void set_defaults() {
        model_index = null;
        model_type = null;
        name = null;
        description = null;
        old_color = null;
        new_color = null;
        size_x = 1;
        size_y = 1;
        has_collisions = true;
        blocks_projectiles = true;
        is_static = false;
        adjust_to_terrain = false;
        flat_shading = false;
        is_solid = false;
        seq_index = -1;
        wall_width = 16;
        brightness = 0;
        specular = 0;
        action = null;
        icon = -1;
        scene_image_index = -1;
        rotate_ccw = false;
        casts_shadow = true;
        scale_x = 128;
        scale_y = 128;
        scale_z = 128;
        face_flags = 0;
        offset_x = 0;
        offset_y = 0;
        offset_z = 0;
        is_decoration = false;
        ghost = false;
        raises_item_piles = -1;
        varbit_index = -1;
        setting_index = -1;
        override_index = null;
    }

}
