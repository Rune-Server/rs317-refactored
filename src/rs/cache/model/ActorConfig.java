package rs.cache.model;

import rs.Game;
import rs.cache.Archive;
import rs.cache.impl.VarBit;
import rs.io.Buffer;
import rs.node.List;
import rs.scene.model.Model;

public class ActorConfig {

    public static Buffer buffer;
    public static int buffer_position[];
    public static ActorConfig[] cache;
    public static int cache_index;
    public static int count;
    public static List model_cache = new List(30);

    public static ActorConfig get(int index) {
        for (ActorConfig config : ActorConfig.cache) {
            if (config.index == index) {
                return config;
            }
        }

        ActorConfig.cache_index = (ActorConfig.cache_index + 1) % 20;
        ActorConfig.buffer.position = ActorConfig.buffer_position[index];

        return (ActorConfig.cache[cache_index] = new ActorConfig(index, buffer));
    }

    public static void nullify() {
        ActorConfig.model_cache = null;
        ActorConfig.cache = null;
        ActorConfig.buffer = null;
        ActorConfig.buffer_position = null;
    }

    public static void unpack(Archive a) {
        ActorConfig.buffer = new Buffer(a.get("npc.dat", null));
        Buffer idx = new Buffer(a.get("npc.idx", null));
        ActorConfig.count = idx.get_ushort();

        ActorConfig.buffer_position = new int[ActorConfig.count];

        int position = 2;
        for (int i = 0; i < ActorConfig.count; i++) {
            ActorConfig.buffer_position[i] = position;
            position += idx.get_ushort();
        }

        ActorConfig.cache = new ActorConfig[20];

        for (int i = 0; i < ActorConfig.cache.length; i++) {
            ActorConfig.cache[i] = new ActorConfig();
        }
    }

    public boolean aBoolean93;
    public String[] action;
    public int brightness;
    public short combat_level;
    public String description;
    public short[] dialog_model_index;
    public byte has_options;
    public byte head_icon;
    public int index;
    public boolean interactable;
    public short[] model_index;
    public short move_seq;
    public String name;
    public int[] new_colors;
    public int[] old_colors;
    public short scale_x;
    public short scale_y;
    public short setting_index;
    public short[] override_index;
    public boolean show_on_minimap;
    public short specular;
    public short stand_sequence;
    public short turn_180_seq;
    public short turn_l_seq;
    public short turn_r_seq;
    public short turn_speed;
    public short varbit_index;

    public ActorConfig() {
        this.defaults();
    }

    public ActorConfig(int index, Buffer b) {
        this.defaults();
        this.index = index;

        do {
            int opcode = b.get_ubyte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                model_index = new short[b.get_ubyte()];
                for (int i = 0; i < model_index.length; i++) {
                    model_index[i] = (short) b.get_ushort();
                }
            } else if (opcode == 2) {
                name = b.get_string();
            } else if (opcode == 3) {
                description = b.get_string();
            } else if (opcode == 12) {
                has_options = b.get_byte();
            } else if (opcode == 13) {
                stand_sequence = (short) b.get_ushort();
            } else if (opcode == 14) {
                move_seq = (short) b.get_ushort();
            } else if (opcode == 17) {
                move_seq = (short) b.get_ushort();
                turn_180_seq = (short) b.get_ushort();
                turn_r_seq = (short) b.get_ushort();
                turn_l_seq = (short) b.get_ushort();
            } else if (opcode >= 30 && opcode < 40) {
                if (action == null) {
                    action = new String[5];
                }
                action[opcode - 30] = b.get_string();

                if (action[opcode - 30].equalsIgnoreCase("hidden")) {
                    action[opcode - 30] = null;
                }
            } else if (opcode == 40) {
                int count = b.get_ubyte();
                old_colors = new int[count];
                new_colors = new int[count];
                for (int i = 0; i < count; i++) {
                    old_colors[i] = b.get_ushort();
                    new_colors[i] = b.get_ushort();
                }

            } else if (opcode == 60) {
                int count = b.get_ubyte();
                dialog_model_index = new short[count];

                for (int i = 0; i < count; i++) {
                    dialog_model_index[i] = (short) b.get_ushort();
                }
            } else if (opcode == 90) {
                b.get_ushort();
            } else if (opcode == 91) {
                b.get_ushort();
            } else if (opcode == 92) {
                b.get_ushort();
            } else if (opcode == 93) {
                show_on_minimap = false;
            } else if (opcode == 95) {
                combat_level = (short) b.get_ushort();
            } else if (opcode == 97) {
                scale_x = (short) b.get_ushort();
            } else if (opcode == 98) {
                scale_y = (short) b.get_ushort();
            } else if (opcode == 99) {
                aBoolean93 = true;
            } else if (opcode == 100) {
                brightness = b.get_byte();
            } else if (opcode == 101) {
                specular = (short) (b.get_byte() * 5);
            } else if (opcode == 102) {
                head_icon = (byte) (short) b.get_ushort();
            } else if (opcode == 103) {
                turn_speed = (short) b.get_ushort();
            } else if (opcode == 106) {
                varbit_index = (short) b.get_ushort();
                setting_index = (short) b.get_ushort();

                override_index = new short[b.get_ubyte() + 1];

                for (int i = 0; i <= override_index.length; i++) {
                    override_index[i] = (short) b.get_ushort();
                }

            } else if (opcode == 107) {
                interactable = false;
            }
        } while (true);
    }

    public void defaults() {
        this.turn_l_seq = -1;
        this.varbit_index = -1;
        this.turn_180_seq = -1;
        this.setting_index = -1;
        this.combat_level = -1;
        this.move_seq = -1;
        this.has_options = 1;
        this.head_icon = -1;
        this.stand_sequence = -1;
        this.index = -1;
        this.turn_speed = 32;
        this.turn_r_seq = -1;
        this.interactable = true;
        this.scale_y = 128;
        this.show_on_minimap = true;
        this.scale_x = 128;
        this.aBoolean93 = false;
    }

    public Model get_dialog_model() {
        if (override_index != null) {
            ActorConfig config = get_overriding_config();
            if (config == null) {
                return null;
            } else {
                return config.get_dialog_model();
            }
        }

        if (dialog_model_index == null) {
            return null;
        }

        boolean valid = false;

        for (int i = 0; i < dialog_model_index.length; i++) {
            if (!Model.is_valid(dialog_model_index[i])) {
                valid = true;
            }
        }

        if (valid) {
            return null;
        }

        Model[] models = new Model[dialog_model_index.length];

        for (int i = 0; i < dialog_model_index.length; i++) {
            models[i] = Model.get(dialog_model_index[i]);
        }

        Model m;

        if (models.length == 1) {
            m = models[0];
        } else {
            m = new Model(models.length, models);
        }

        if (old_colors != null) {
            m.set_colors(old_colors, new_colors);
        }
        return m;
    }

    public Model get_model(int vertices[], int frame1, int frame2) {
        if (override_index != null) {
            ActorConfig config = get_overriding_config();

            if (config == null) {
                return null;
            } else {
                return config.get_model(vertices, frame1, frame2);
            }
        }

        Model model = (Model) model_cache.get(index);

        if (model == null) {
            boolean flag = false;
            for (int i1 = 0; i1 < model_index.length; i1++) {
                if (!Model.is_valid(model_index[i1])) {
                    flag = true;
                }
            }

            if (flag) {
                return null;
            }
            Model models[] = new Model[model_index.length];

            for (int i = 0; i < model_index.length; i++) {
                models[i] = Model.get(model_index[i]);
            }

            if (models.length == 1) {
                model = models[0];
            } else {
                model = new Model(models.length, models);
            }

            if (old_colors != null) {
                model.set_colors(old_colors, new_colors);
            }

            model.apply_vertex_weights();
            model.apply_lighting(64 + brightness, 850 + specular, -30, -50, -30, true);
            model_cache.insert(model, index);
        }

        Model m = Model.temporary;
        m.replace(model, (frame1 == -1) & (frame2 == -1));

        if (frame1 != -1 && frame2 != -1) {
            m.apply_sequence_frames(vertices, frame1, frame2);
        } else if (frame1 != -1) {
            m.apply_sequence_frame(frame1);
        }

        if (scale_x != 128 || scale_y != 128) {
            m.scale(scale_x, scale_y, scale_x);
        }

        m.method466();
        m.triangle_groups = null;
        m.vertex_weights = null;

        if (has_options == 1) {
            m.is_clickable = true;
        }

        return m;
    }

    public ActorConfig get_overriding_config() {
        int value = -1;

        if (varbit_index != -1) {
            VarBit varbit = VarBit.instance[varbit_index];
            int index = varbit.setting;
            int offset = varbit.offset;
            int position = varbit.shift;
            int max_value = Game.LSB_BIT_MASK[position - offset];
            value = Game.settings[index] >> offset & max_value;
        } else if (setting_index != -1) {
            value = Game.settings[setting_index];
        }

        if (value < 0 || value >= override_index.length || override_index[value] == -1) {
            return null;
        }

        return get(override_index[value]);
    }

}
