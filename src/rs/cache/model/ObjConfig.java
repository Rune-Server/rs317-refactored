package rs.cache.model;

import rs.Game;
import rs.cache.Archive;
import rs.io.Buffer;
import rs.media.Canvas2D;
import rs.media.Canvas3D;
import rs.media.Sprite;
import rs.node.List;
import rs.node.impl.Renderable;
import rs.scene.model.Model;
import rs.util.JString;

public class ObjConfig {

    public static ObjConfig[] cache;
    public static int cache_position;
    public static int count;
    public static List model_cache = new List(50);
    public static int pointer[];
    public static List sprite_cache = new List(100);
    public static Buffer buffer;

    public static ObjConfig get(int index) {
        for (ObjConfig config : ObjConfig.cache) {
            if (config.index == index) {
                return config;
            }
        }

        if (index < 0 || index >= pointer.length) {
            return null;
        }

        cache_position = (cache_position + 1) % 10;
        ObjConfig config = cache[cache_position];
        ObjConfig.buffer.position = ObjConfig.pointer[index];

        config.index = (short) index;
        config.defaults();
        config.load(ObjConfig.buffer);

        if (config.note_template_index != -1) {
            config.to_note();
        }

        if (!Game.is_members && config.is_members) {
            config.name = JString.MEMBERS_OBJECT;
            config.description = JString.LOGIN_TO_A_MEMBERS_SERVER_TO_USE_THIS_OBJECT;
            config.ground_action = null;
            config.action = null;
            config.team = 0;
        }

        return config;
    }

    public static Sprite get_sprite(int index, int count, int outline_color) {
        if (outline_color == 0) {
            Sprite s = (Sprite) sprite_cache.get(index);
            if (s != null && s.crop_height != count && s.crop_height != -1) {
                s.detach();
                s = null;
            }
            if (s != null) {
                return s;
            }
        }

        ObjConfig c = get(index);

        if (c == null) {
            return null;
        }

        if (c.stack_index == null) {
            count = -1;
        }

        if (count > 1) {
            int i = -1;
            for (int j = 0; j < 10; j++) {
                if (count >= c.stack_amount[j] && c.stack_amount[j] != 0) {
                    i = c.stack_index[j];
                }
            }
            if (i != -1) {
                c = get(i);
            }
        }

        Model m = c.get_model(1);

        if (m == null) {
            return null;
        }

        Sprite s1 = null;

        if (c.note_template_index != -1) {
            s1 = get_sprite(c.note_item_index, 10, -1);

            if (s1 == null) {
                return null;
            }
        }

        Sprite s = new Sprite(32, 32);

        int _center_x = Canvas3D.center_x;
        int _center_y = Canvas3D.center_y;
        int _pixels3d[] = Canvas3D.pixels;
        int _pixels2d[] = Canvas2D.pixels;
        int _width = Canvas2D.width;
        int _height = Canvas2D.height;
        int _x1 = Canvas2D.left_x;
        int _x2 = Canvas2D.right_x;
        int _y1 = Canvas2D.left_y;
        int _y2 = Canvas2D.right_y;

        Canvas3D.texturize = false;
        Canvas2D.prepare(32, 32, s.pixels);
        Canvas2D.fill_rect(0, 0, 32, 32, 0);
        Canvas3D.prepare();

        int dist = c.icon_dist;

        if (outline_color == -1) {
            dist = (int) ((double) dist * 1.50D);
        }

        if (outline_color > 0) {
            dist = (int) ((double) dist * 1.04D);
        }

        int sin = Canvas3D.sin[c.icon_pitch] * dist >> 16;
        int cos = Canvas3D.cos[c.icon_pitch] * dist >> 16;

        m.draw(0, c.icon_yaw, c.icon_roll, c.icon_pitch, c.icon_x, sin + (((Renderable) (m)).height / 2) + c.icon_y, cos + c.icon_y);

        for (int x = 31; x >= 0; x--) {
            for (int y = 31; y >= 0; y--) {
                if (s.pixels[x + y * 32] == 0) {
                    if (x > 0 && s.pixels[(x - 1) + y * 32] > 1) {
                        s.pixels[x + y * 32] = 1;
                    } else if (y > 0 && s.pixels[x + (y - 1) * 32] > 1) {
                        s.pixels[x + y * 32] = 1;
                    } else if (x < 31 && s.pixels[x + 1 + y * 32] > 1) {
                        s.pixels[x + y * 32] = 1;
                    } else if (y < 31 && s.pixels[x + (y + 1) * 32] > 1) {
                        s.pixels[x + y * 32] = 1;
                    }
                }
            }
        }

        if (outline_color > 0) {
            for (int x = 31; x >= 0; x--) {
                for (int y = 31; y >= 0; y--) {
                    if (s.pixels[x + y * 32] == 0) {
                        if (x > 0 && s.pixels[(x - 1) + y * 32] == 1) {
                            s.pixels[x + y * 32] = outline_color;
                        } else if (y > 0 && s.pixels[x + (y - 1) * 32] == 1) {
                            s.pixels[x + y * 32] = outline_color;
                        } else if (x < 31 && s.pixels[x + 1 + y * 32] == 1) {
                            s.pixels[x + y * 32] = outline_color;
                        } else if (y < 31 && s.pixels[x + (y + 1) * 32] == 1) {
                            s.pixels[x + y * 32] = outline_color;
                        }
                    }
                }
            }
        } else if (outline_color == 0) {
            for (int x = 31; x >= 0; x--) {
                for (int y = 31; y >= 0; y--) {
                    if (s.pixels[x + y * 32] == 0 && x > 0 && y > 0 && s.pixels[(x - 1) + (y - 1) * 32] > 0) {
                        s.pixels[x + y * 32] = 0x302020;
                    }
                }
            }
        }

        if (c.note_template_index != -1) {
            int w = s1.crop_width;
            int h = s1.crop_height;
            s1.crop_width = 32;
            s1.crop_height = 32;
            s1.draw_masked(0, 0);
            s1.crop_width = w;
            s1.crop_height = h;
        }

        if (outline_color == 0) {
            sprite_cache.insert(s, index);
        }

        Canvas2D.prepare(_width, _height, _pixels2d);
        Canvas2D.set_bounds(_x1, _y1, _x2, _y2);
        Canvas3D.center_x = _center_x;
        Canvas3D.center_y = _center_y;
        Canvas3D.pixels = _pixels3d;
        Canvas3D.texturize = true;

        if (c.stackable) {
            s.crop_width = 33;
        } else {
            s.crop_width = 32;
        }

        s.crop_height = count;
        return s;
    }

    public static void nullify() {
        ObjConfig.model_cache = null;
        ObjConfig.sprite_cache = null;
        ObjConfig.pointer = null;
        ObjConfig.cache = null;
        ObjConfig.buffer = null;
    }

    public static void unpack(Archive a) {
        ObjConfig.buffer = new Buffer(a.get("obj.dat", null));
        Buffer b = new Buffer(a.get("obj.idx", null));

        ObjConfig.count = b.get_ushort();
        ObjConfig.pointer = new int[count];

        int position = 2;

        for (int i = 0; i < ObjConfig.count; i++) {
            ObjConfig.pointer[i] = position;
            position += b.get_ushort();
        }

        ObjConfig.cache = new ObjConfig[10];

        for (int i = 0; i < ObjConfig.cache.length; i++) {
            ObjConfig.cache[i] = new ObjConfig();
        }
    }

    public String action[];
    public short brightness;
    public String description;
    public short female_dialog_model1;
    public short female_dialog_model2;
    public short female_model1;
    public short female_model2;
    public short female_model3;
    public byte female_off_y;
    public String ground_action[];
    public short icon_dist;
    public short icon_pitch;
    public short icon_roll;
    public short icon_x;
    public short icon_y;
    public short icon_yaw;
    public short index;
    public boolean is_members;
    public short male_dialog_model1;
    public short male_dialog_model2;
    public short male_model1;
    public short male_model2;
    public short male_model3;
    public byte male_off_y;
    public short model_index;
    public String name;
    public int new_color[];
    public short note_item_index;
    public short note_template_index;
    public int old_color[];
    public int pile_priority;
    public short scale_x;
    public short scale_y;
    public short scale_z;
    public short specular;
    public int stack_amount[];
    public short[] stack_index;
    public boolean stackable;
    public byte team;

    public ObjConfig() {
        index = -1;
    }

    public void defaults() {
        model_index = 0;
        name = null;
        description = null;
        old_color = null;
        new_color = null;
        icon_dist = 2000;
        icon_pitch = 0;
        icon_yaw = 0;
        icon_roll = 0;
        icon_x = 0;
        icon_y = 0;
        stackable = false;
        pile_priority = 1;
        is_members = false;
        ground_action = null;
        action = null;
        male_model1 = -1;
        male_model2 = -1;
        male_off_y = 0;
        female_model1 = -1;
        female_model2 = -1;
        female_off_y = 0;
        male_model3 = -1;
        female_model3 = -1;
        male_dialog_model1 = -1;
        male_dialog_model2 = -1;
        female_dialog_model1 = -1;
        female_dialog_model2 = -1;
        stack_index = null;
        stack_amount = null;
        note_item_index = -1;
        note_template_index = -1;
        scale_x = 128;
        scale_y = 128;
        scale_z = 128;
        brightness = 0;
        specular = 0;
        team = 0;
    }

    public Model get_dialogue_model(int gender) {
        int a = male_dialog_model1;
        int b = male_dialog_model2;

        if (gender == 1) {
            a = female_dialog_model1;
            b = female_dialog_model2;
        }

        if (a == -1) {
            return null;
        }

        Model mesh = Model.get(a);

        if (b != -1) {
            mesh = new Model(2, new Model[]{mesh, Model.get(b)});
        }

        if (old_color != null) {
            mesh.set_colors(old_color, new_color);
        }
        return mesh;
    }

    public Model get_model(int count) {
        if (stack_index != null && count > 1) {
            int index = -1;
            for (int i = 0; i < 10; i++) {
                if (count >= stack_amount[i] && stack_amount[i] != 0) {
                    index = stack_index[i];
                }
            }
            if (index != -1) {
                return ObjConfig.get(index).get_model(1);
            }
        }

        Model mesh = (Model) model_cache.get(index);

        if (mesh != null) {
            return mesh;
        }

        mesh = Model.get(model_index);

        if (mesh == null) {
            return null;
        }

        if (scale_x != 128 || scale_y != 128 || scale_z != 128) {
            mesh.scale(scale_x, scale_y, scale_z);
        }

        if (old_color != null) {
            mesh.set_colors(old_color, new_color);
        }

        mesh.apply_lighting(64 + brightness, 768 + specular, -50, -10, -50, true);
        mesh.is_clickable = true;
        model_cache.insert(mesh, index);

        return mesh;

    }

    public Model get_widget_mesh() {
        return this.get_widget_mesh(1);
    }

    public Model get_widget_mesh(int count) {
        if (stack_index != null && count > 1) {
            int stack = -1;
            for (int i = 0; i < 10; i++) {
                if (count >= stack_amount[i] && stack_amount[i] != 0) {
                    stack = stack_index[i];
                }
            }

            if (stack != -1) {
                return get(stack).get_widget_mesh();
            }
        }

        Model mesh = Model.get(model_index);

        if (mesh == null) {
            return null;
        }

        if (old_color != null) {
            mesh.set_colors(old_color, new_color);
        }

        return mesh;
    }

    public Model get_worn_mesh(int gender) {
        int i1 = male_model1;
        int i2 = male_model2;
        int i3 = male_model3;

        if (gender == 1) {
            i1 = female_model1;
            i2 = female_model2;
            i3 = female_model3;
        }

        if (i1 == -1) {
            return null;
        }

        Model mesh = Model.get(i1);

        if (i2 != -1) {
            if (i3 != -1) {
                mesh = new Model(3, new Model[]{mesh, Model.get(i2), Model.get(i3)});
            } else {
                mesh = new Model(2, new Model[]{mesh, Model.get(i2)});
            }
        }

        if (gender == 0 && male_off_y != 0) {
            mesh.translate(0, male_off_y, 0);
        }

        if (gender == 1 && female_off_y != 0) {
            mesh.translate(0, female_off_y, 0);
        }

        if (old_color != null) {
            mesh.set_colors(old_color, new_color);
        }
        return mesh;
    }

    public boolean is_dialogue_model_valid(int gender) {
        int index1 = male_dialog_model1;
        int index2 = male_dialog_model2;

        if (gender == 1) {
            index1 = female_dialog_model1;
            index2 = female_dialog_model2;
        }

        if (index1 == -1) {
            return true;
        }

        boolean valid = true;

        if (!Model.is_valid(index1)) {
            valid = false;
        }

        if (index2 != -1 && !Model.is_valid(index2)) {
            valid = false;
        }

        return valid;
    }

    public boolean is_worn_mesh_valid(int gender) {
        int i1 = male_model1;
        int i2 = male_model2;
        int i3 = male_model3;

        if (gender == 1) {
            i1 = female_model1;
            i2 = female_model2;
            i3 = female_model3;
        }

        if (i1 == -1) {
            return true;
        }

        if (!Model.is_valid(i1)) {
            return false;
        }

        if (i2 != -1 && !Model.is_valid(i2)) {
            return false;
        }

        if (i3 != -1 && !Model.is_valid(i3)) {
            return false;
        }

        return true;
    }

    public void load(Buffer b) {
        do {
            int i = b.get_ubyte();

            if (i == 0) {
                return;
            }

            if (i == 1) {
                model_index = (short) b.get_ushort();
            } else if (i == 2) {
                name = b.get_string();
            } else if (i == 3) {
                description = b.get_string();
            } else if (i == 4) {
                icon_dist = (short) b.get_ushort();
            } else if (i == 5) {
                icon_pitch = (short) b.get_ushort();
            } else if (i == 6) {
                icon_yaw = (short) b.get_ushort();
            } else if (i == 7) {
                int x = b.get_ushort();

                if (x > 32767) {
                    x -= 0x10000;
                }

                icon_x = (short) x;
            } else if (i == 8) {
                int y = b.get_ushort();

                if (y > 32767) {
                    y -= 0x10000;
                }

                icon_y = (short) y;
            } else if (i == 10) {
                b.get_ushort();
            } else if (i == 11) {
                stackable = true;
            } else if (i == 12) {
                pile_priority = b.get_int();
            } else if (i == 16) {
                is_members = true;
            } else if (i == 23) {
                male_model1 = (short) b.get_ushort();
                male_off_y = b.get_byte();
            } else if (i == 24) {
                male_model2 = (short) b.get_ushort();
            } else if (i == 25) {
                female_model1 = (short) b.get_ushort();
                female_off_y = b.get_byte();
            } else if (i == 26) {
                female_model2 = (short) b.get_ushort();
            } else if (i >= 30 && i < 35) {
                if (ground_action == null) {
                    ground_action = new String[5];
                }

                ground_action[i - 30] = b.get_string();
            } else if (i >= 35 && i < 40) {
                if (action == null) {
                    action = new String[5];
                }

                int j = i - 35;

                String s = b.get_string();

                for (String s1 : JString.COMMON_OBJ_ACTIONS) {
                    if (s.equals(s1)) {
                        action[j] = s1;
                        break;
                    }
                }

                if (action[j] == null) {
                    action[j] = s;
                }
            } else if (i == 40) {
                int j = b.get_ubyte();
                old_color = new int[j];
                new_color = new int[j];
                for (int k = 0; k < j; k++) {
                    old_color[k] = b.get_ushort();
                    new_color[k] = b.get_ushort();
                }

            } else if (i == 78) {
                male_model3 = (short) b.get_ushort();
            } else if (i == 79) {
                female_model3 = (short) b.get_ushort();
            } else if (i == 90) {
                male_dialog_model1 = (short) b.get_ushort();
            } else if (i == 91) {
                female_dialog_model1 = (short) b.get_ushort();
            } else if (i == 92) {
                male_dialog_model2 = (short) b.get_ushort();
            } else if (i == 93) {
                female_dialog_model2 = (short) b.get_ushort();
            } else if (i == 95) {
                icon_roll = (short) b.get_ushort();
            } else if (i == 97) {
                note_item_index = (short) b.get_ushort();
            } else if (i == 98) {
                note_template_index = (short) b.get_ushort();
            } else if (i >= 100 && i < 110) {
                if (stack_index == null) {
                    stack_index = new short[10];
                    stack_amount = new int[10];
                }
                stack_index[i - 100] = (short) b.get_ushort();
                stack_amount[i - 100] = b.get_ushort();
            } else if (i == 110) {
                scale_x = (short) b.get_ushort();
            } else if (i == 111) {
                scale_y = (short) b.get_ushort();
            } else if (i == 112) {
                scale_z = (short) b.get_ushort();
            } else if (i == 113) {
                brightness = b.get_byte();
            } else if (i == 114) {
                specular = (short) (b.get_byte() * 5);
            } else if (i == 115) {
                team = b.get_byte();
            }
        } while (true);
    }

    public void to_note() {
        ObjConfig a = get(note_template_index);
        model_index = a.model_index;
        icon_dist = a.icon_dist;
        icon_pitch = a.icon_pitch;
        icon_yaw = a.icon_yaw;
        icon_roll = a.icon_roll;
        icon_x = a.icon_x;
        icon_y = a.icon_y;
        old_color = a.old_color;
        new_color = a.new_color;

        ObjConfig b = get(note_item_index);
        name = b.name;
        is_members = b.is_members;
        pile_priority = b.pile_priority;

        StringBuilder s = new StringBuilder().append("Swap this note at any bank for a");

        if (JString.is_vowel(b.name, 0)) {
            s.append('n');
        }

        description = s.append(' ').append(b.name).append('.').toString();
        stackable = true;
    }

}
