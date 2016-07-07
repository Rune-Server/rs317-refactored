package rs.cache.impl;

import rs.Game;
import rs.Signlink;
import rs.cache.Archive;
import rs.cache.model.ActorConfig;
import rs.cache.model.ObjConfig;
import rs.input.Mouse;
import rs.io.Buffer;
import rs.media.BitmapFont;
import rs.media.Canvas2D;
import rs.media.Canvas3D;
import rs.media.Sprite;
import rs.node.List;
import rs.scene.model.Model;
import rs.util.JString;

import java.io.File;

public class Widget {

    public static Widget[] instance;
    public static List model_cache = new List(30);
    public static List sprite_cache;

    public static void draw(int index, int x, int y, int scroll_amount) {
        Widget w = Widget.get(index);
        if (w != null) {
            w.draw(x, y, scroll_amount);
        }
    }

    public static Widget get(int i) {
        if (i < 0 || i >= instance.length) {
            return null;
        }
        return instance[i];
    }

    public static Sprite get_sprite(Archive a, String name, int index) {
        long uid = (JString.get_hash(name) << 8) + (long) index;
        Sprite s = (Sprite) sprite_cache.get(uid);

        if (s != null) {
            return s;
        }

        try {
            s = new Sprite(a, name, index);
            sprite_cache.insert(s, uid);
        } catch (Exception _ex) {
            return null;
        }

        return s;
    }

    public static boolean handle_sequences(int cycle, int widget_index) {
        Widget w = Widget.get(widget_index);
        if (w != null) {
            w.handle_sequences(cycle);
        }
        return false;
    }

    private static void load_clientscript(Widget w, Buffer b) {
        int count = b.get_ubyte();

        byte[] compare_type = null;
        int[] compare_value = null;

        if (count > 0) {
            compare_type = new byte[count];
            compare_value = new int[count];

            for (int i = 0; i < count; i++) {
                compare_type[i] = b.get_byte();
                compare_value[i] = b.get_ushort();
            }
        }

        count = b.get_ubyte();

        if (count > 0) {
            w.script = new Script[count];

            for (int i = 0; i < count; i++) {
                Script s = new Script(w, b, i);

                if (compare_type != null && i < compare_type.length) {
                    s.compare_type = compare_type[i];
                    s.compare_value = compare_value[i];
                }

                if (Game.debug) {
                    s.export(Signlink.get_file("cs1/" + w.index + "/" + s.index + ".cs1"));
                }

                w.script[i] = s;
            }
        }
    }

    public static void reset_animations(int index) {
        Widget w = Widget.get(index);
        if (w != null) {
            w.reset_sequence();
        }
    }

    public static void set_mesh(int type, int index, Model m) {
        model_cache.clear();
        if (m != null && type != 4) {
            model_cache.insert(m, (type << 16) + index);
        }
    }

    public static void unpack(Archive archive, BitmapFont[] fonts, Archive media) {
        Widget.sprite_cache = new List(50000);
        Buffer b = new Buffer(archive.get("data"));
        Widget.instance = new Widget[b.get_ushort()];

        File cs1 = new File(Signlink.get_cache_path() + "./cs1/");
        cs1.mkdirs();

        int parent = -1;
        while (b.position < b.payload.length) {
            int index = b.get_ushort();

            if (index == 65535) {
                parent = b.get_ushort();
                index = b.get_ushort();
            }

            Widget w = instance[index] = new Widget();

            w.index = (short) index;
            w.parent = (short) parent;
            w.type = b.get_byte();
            w.option_type = b.get_byte();
            w.action_type = b.get_ushort();
            w.width = b.get_ushort();
            w.height = b.get_ushort();
            w.opacity = (byte) b.get_ubyte();
            w.hover_index = b.get_ubyte();

            if (w.hover_index != 0) {
                w.hover_index = (w.hover_index - 1 << 8) + b.get_ubyte();
            } else {
                w.hover_index = -1;
            }

            load_clientscript(w, b);

            if (w.type == 0) {
                w.scroll_height = b.get_ushort();
                w.hidden = b.get_byte() == 1;

                int count = b.get_ushort();
                w.children = new short[count];
                w.child_x = new short[count];
                w.child_y = new short[count];

                for (int i = 0; i < count; i++) {
                    w.children[i] = (short) b.get_ushort();
                    w.child_x[i] = (short) b.get_ushort();
                    w.child_y[i] = (short) b.get_ushort();
                }
            }

            if (w.type == 2) {
                w.item_index = new short[w.width * w.height];
                w.item_count = new int[w.width * w.height];
                w.items_draggable = b.get_byte() == 1;
                w.items_have_actions = b.get_byte() == 1;
                w.items_usable = b.get_byte() == 1;
                w.items_swappable = b.get_byte() == 1;
                w.item_margin_x = (short) b.get_ubyte();
                w.item_margin_y = (short) b.get_ubyte();
                w.item_slot_x = new short[20];
                w.item_slot_y = new short[20];
                w.slot_image = new Sprite[20];

                for (int i = 0; i < 20; i++) {
                    if (b.get_ubyte() == 1) {
                        w.item_slot_x[i] = (short) b.get_ushort();
                        w.item_slot_y[i] = (short) b.get_ushort();
                        String str = b.get_string();

                        if (media != null && str.length() > 0) {
                            int j = str.lastIndexOf(',');
                            w.slot_image[i] = Widget.get_sprite(media, str.substring(0, j), Integer.parseInt(str.substring(j + 1)));
                        }
                    }
                }

                w.item_actions = new String[5];
                for (int i = 0; i < 5; i++) {
                    w.item_actions[i] = b.get_string();
                    if (w.item_actions[i].length() == 0) {
                        w.item_actions[i] = null;
                    }
                }

            }

            if (w.type == 3) {
                w.filled = b.get_byte() == 1;
            }

            if (w.type == 4 || w.type == 1) {
                w.centered = b.get_byte() == 1;
                int font = b.get_ubyte();
                if (fonts != null) {
                    w.font = fonts[font];
                }
                w.shadow = b.get_byte() == 1;
            }

            if (w.type == 4) {
                w.message_disabled = b.get_string();
                w.message_enabled = b.get_string();
            }

            if (w.type == 1 || w.type == 3 || w.type == 4) {
                w.rgb_disabled = b.get_int();
            }

            if (w.type == 3 || w.type == 4) {
                w.rgb_enabled = b.get_int();
                w.color_hover_disabled = b.get_int();
                w.color_hover_enabled = b.get_int();
            }

            if (w.type == 5) {
                String str = b.get_string();

                if (media != null && str.length() > 0) {
                    int i = str.lastIndexOf(',');
                    w.image_disabled = get_sprite(media, str.substring(0, i), Integer.parseInt(str.substring(i + 1)));
                }

                str = b.get_string();

                if (media != null && str.length() > 0) {
                    int i = str.lastIndexOf(',');
                    w.image_enabled = get_sprite(media, str.substring(0, i), Integer.parseInt(str.substring(i + 1)));
                }
            }

            if (w.type == 6) {
                int i = b.get_ubyte();
                if (i != 0) {
                    w.model_type_disabled = 1;
                    w.model_index_disabled = (i - 1 << 8) + b.get_ubyte();
                }

                i = b.get_ubyte();
                if (i != 0) {
                    w.model_type_enabled = 1;
                    w.model_index_enabled = (i - 1 << 8) + b.get_ubyte();
                }

                i = b.get_ubyte();
                if (i != 0) {
                    w.seq_index_disabled = (i - 1 << 8) + b.get_ubyte();
                } else {
                    w.seq_index_disabled = -1;
                }

                i = b.get_ubyte();
                if (i != 0) {
                    w.seq_index_enabled = (i - 1 << 8) + b.get_ubyte();
                } else {
                    w.seq_index_enabled = -1;
                }

                w.model_zoom = b.get_ushort();
                w.model_pitch = b.get_ushort();
                w.model_yaw = b.get_ushort();
            }

            if (w.type == 7) {
                w.item_index = new short[w.width * w.height];
                w.item_count = new int[w.width * w.height];
                w.centered = b.get_byte() == 1;
                w.font = fonts[b.get_ubyte()];
                w.shadow = b.get_byte() == 1;
                w.rgb_disabled = b.get_int();
                w.item_margin_x = (short) b.get_ushort();
                w.item_margin_y = (short) b.get_ushort();
                w.items_have_actions = b.get_byte() == 1;
                w.item_actions = new String[5];

                for (int i = 0; i < 5; i++) {
                    w.item_actions[i] = b.get_string();
                    if (w.item_actions[i].length() == 0) {
                        w.item_actions[i] = null;
                    }
                }
            }

            if (w.option_type == 2 || w.type == 2) {
                w.option_prefix = b.get_string();
                w.option_suffix = b.get_string();
                w.option_action = b.get_ushort();
            }

            if (w.option_type == 1 || w.option_type == 4 || w.option_type == 5 || w.option_type == 6) {
                w.option = b.get_string();
                if (w.option.length() == 0) {
                    if (w.option_type == 1) {
                        w.option = JString.OK;
                    }
                    if (w.option_type == 4) {
                        w.option = JString.SELECT;
                    }
                    if (w.option_type == 5) {
                        w.option = JString.SELECT;
                    }
                    if (w.option_type == 6) {
                        w.option = JString.CONTINUE;
                    }
                }
            }
        }

        Widget.sprite_cache = null;
    }

    public int action_type;
    public boolean centered;
    public short child_x[];
    public short child_y[];
    public short children[];
    public int color_hover_disabled;
    public int color_hover_enabled;
    public boolean filled;
    public BitmapFont font;
    public int height;
    public boolean hidden;
    public int hover_index;
    public Sprite image_disabled;
    public Sprite image_enabled;
    public short index;
    public String item_actions[];
    public int item_count[];
    public short item_index[];
    public short item_margin_x;
    public short item_margin_y;
    public short item_slot_x[];
    public short item_slot_y[];
    public boolean items_draggable;
    public boolean items_have_actions;
    public boolean items_swappable;
    public boolean items_usable;
    public String message_disabled;
    public String message_enabled;
    public int model_index_disabled;
    public int model_index_enabled;
    public int model_pitch;
    public int model_type_disabled;
    public int model_type_enabled;
    public int model_yaw;
    public int model_zoom;
    public byte opacity;
    public String option;
    public int option_action;
    public String option_prefix;
    public String option_suffix;
    public byte option_type;
    public short parent;
    public int rgb_disabled;
    public int rgb_enabled;
    public Script[] script;
    public int scroll_amount;
    public int scroll_height;
    public int seq_index_disabled;
    public int seq_index_enabled;
    public int sequence_cycle;
    public int sequence_frame;
    public boolean shadow;
    public Sprite slot_image[];
    public byte type;
    public boolean visible = true;
    public int width;
    public short x;
    public short y;

    public void draw(int x, int y) {
        switch (this.type) {
            case 2: {
                int slot = 0;

                for (int slot_y = 0; slot_y < this.height; slot_y++) {
                    for (int slot_x = 0; slot_x < this.width; slot_x++) {
                        int draw_x = x + slot_x * (32 + this.item_margin_x);
                        int draw_y = y + slot_y * (32 + this.item_margin_y);

                        if (slot < 20) {
                            draw_x += this.item_slot_x[slot];
                            draw_y += this.item_slot_y[slot];
                        }

                        if (this.item_index[slot] > 0) {
                            int drag_dx = 0;
                            int drag_dy = 0;
                            int item_index = this.item_index[slot] - 1;

                            if (draw_x > Canvas2D.left_x - 32 && draw_x < Canvas2D.right_x && draw_y > Canvas2D.left_y - 32 && draw_y < Canvas2D.right_y || Game.drag_area != 0 && Game.drag_slot == slot) {
                                int outline_rgb = 0;

                                if (Game.selected_item && Game.selected_item_slot == slot && Game.selected_item_widget == this.index) {
                                    outline_rgb = 0xFFFFFF;
                                }

                                Sprite s = ObjConfig.get_sprite(item_index, this.item_count[slot], outline_rgb);

                                if (s != null) {
                                    if (Game.drag_area != 0 && Game.drag_slot == slot && Game.drag_widget == this.index) {

                                        drag_dx = Mouse.last_x - Game.drag_start_x;
                                        drag_dy = Mouse.last_y - Game.drag_start_y;

                                        if (drag_dx < 5 && drag_dx > -5) {
                                            drag_dx = 0;
                                        }

                                        if (drag_dy < 5 && drag_dy > -5) {
                                            drag_dy = 0;
                                        }

                                        if (Game.drag_cycle < 5) {
                                            drag_dx = 0;
                                            drag_dy = 0;
                                        }

                                        s.draw(draw_x + drag_dx, draw_y + drag_dy, 128);

                                        if (draw_y + drag_dy < Canvas2D.left_y && this.scroll_amount > 0) {
                                            int scroll_dec = (Game.anim_cycle * (Canvas2D.left_y - draw_y - drag_dy)) / 3;

                                            if (scroll_dec > Game.anim_cycle * 10) {
                                                scroll_dec = Game.anim_cycle * 10;
                                            }

                                            if (scroll_dec > this.scroll_amount) {
                                                scroll_dec = this.scroll_amount;
                                            }

                                            this.scroll_amount -= scroll_dec;
                                            Game.drag_start_y += scroll_dec;
                                        }

                                        if (draw_y + drag_dy + 32 > Canvas2D.right_y && this.scroll_amount < this.scroll_height - this.height) {
                                            int scroll_inc = (Game.anim_cycle * ((draw_y + drag_dy + 32) - Canvas2D.right_y)) / 3;

                                            if (scroll_inc > Game.anim_cycle * 10) {
                                                scroll_inc = Game.anim_cycle * 10;
                                            }

                                            if (scroll_inc > this.scroll_height - this.height - this.scroll_amount) {
                                                scroll_inc = this.scroll_height - this.height - this.scroll_amount;
                                            }

                                            this.scroll_amount += scroll_inc;
                                            Game.drag_start_y -= scroll_inc;
                                        }
                                    } else if (Game.click_area != 0 && Game.clicked_item_slot == slot && Game.clicked_item_widget == this.index) {
                                        s.draw(draw_x, draw_y, 128);
                                    } else {
                                        s.draw_masked(draw_x, draw_y);
                                    }

                                    if (s.crop_width == 33 || this.item_count[slot] != 1) {
                                        int count = this.item_count[slot];
                                        String s1 = null;

                                        if (count >= 10_000_000) {
                                            s1 = String.valueOf(count / 1_000_000) + 'M';
                                        } else if (count >= 100_000) {
                                            s1 = String.valueOf(count / 1_000) + 'K';
                                        } else {
                                            s1 = String.valueOf(count);
                                        }

                                        if (s1 == null) {
                                            s1 = "-2";
                                        }

                                        BitmapFont.SMALL.draw(s1, draw_x + drag_dx, draw_y + drag_dy + 9, 0xFFFF00, BitmapFont.SHADOW);
                                    }
                                }
                            }

                        } else if (this.slot_image != null && slot < 20) {
                            Sprite s = this.slot_image[slot];

                            if (s != null) {
                                s.draw_masked(draw_x, draw_y);
                            }
                        }

                        slot++;
                    }
                }
                return;
            }

            case 3: {
                boolean hovered = false;

                if (Game.hovered_chat_widget == this.index || Game.hovered_tab_widget == this.index || Game.hovered_viewport_widget == this.index) {
                    hovered = true;
                }

                int color;

                if (this.is_enabled()) {
                    color = this.rgb_enabled;
                    if (hovered && this.color_hover_enabled != 0) {
                        color = this.color_hover_enabled;
                    }
                } else {
                    color = this.rgb_disabled;
                    if (hovered && this.color_hover_disabled != 0) {
                        color = this.color_hover_disabled;
                    }
                }

                if (this.opacity == 0) {
                    if (this.filled) {
                        Canvas2D.fill_rect(x, y, this.width, this.height, color);
                    } else {
                        Canvas2D.draw_rect(x, y, this.width, this.height, color);
                    }
                } else if (this.filled) {
                    Canvas2D.fill_rect(x, y, this.width, this.height, color, 256 - (this.opacity & 0xFF));
                } else {
                    Canvas2D.draw_rect(x, y, this.width, this.height, color, 256 - (this.opacity & 0xFF));
                }
                return;
            }

            case 4: {
                BitmapFont f = this.font;
                String s = this.message_disabled;

                boolean hovered = false;

                if (Game.hovered_chat_widget == this.index || Game.hovered_tab_widget == this.index || Game.hovered_viewport_widget == this.index) {
                    hovered = true;
                }

                int rgb;

                if (this.is_enabled()) {
                    rgb = this.rgb_enabled;
                    if (hovered && this.color_hover_enabled != 0) {
                        rgb = this.color_hover_enabled;
                    }
                    if (this.message_enabled.length() > 0) {
                        s = this.message_enabled;
                    }
                } else {
                    rgb = this.rgb_disabled;
                    if (hovered && this.color_hover_disabled != 0) {
                        rgb = this.color_hover_disabled;
                    }
                }

                if (this.option_type == 6 && Game.dialogue_option_active) {
                    s = JString.PLEASE_WAIT;
                    rgb = this.rgb_disabled;
                }

                if (Canvas2D.width == 479) {
                    if (rgb == 0xFFFF00) {
                        rgb = 0x0000FF;
                    }
                    if (rgb == 0x00C000) {
                        rgb = 0xFFFFFF;
                    }
                }

                for (int draw_y = y + f.height; s.length() > 0; draw_y += f.height) {
                    if (s.indexOf('%') != -1) {
                        for (int j = 1; j < 5; j++) {
                            do {
                                int k = s.indexOf("%" + j);

                                if (k == -1) {
                                    break;
                                }

                                int value = -2;

                                if (this.script != null && j - 1 < this.script.length) {
                                    Script scr = this.script[j - 1];

                                    if (scr != null) {
                                        value = scr.execute();
                                    }
                                }

                                s = s.substring(0, k) + (value > 999_999_999 ? '*' : value) + s.substring(k + 2);
                            } while (true);
                        }
                    }

                    int separator = s.indexOf("\\n");
                    String message;

                    if (separator != -1) {
                        message = s.substring(0, separator);
                        s = s.substring(separator + 2);
                    } else {
                        message = s;
                        s = "";
                    }

                    if (this.centered) {
                        f.draw(message, x + (this.width / 2), draw_y, rgb, (this.shadow ? BitmapFont.SHADOW : 0) | BitmapFont.CENTER | BitmapFont.ALLOW_TAGS);
                    } else {
                        f.draw(message, x, draw_y, rgb, (this.shadow ? BitmapFont.SHADOW : 0) + BitmapFont.ALLOW_TAGS);
                    }
                }
                return;
            }

            case 5: {
                Sprite s;

                if (this.is_enabled()) {
                    s = this.image_enabled;
                } else {
                    s = this.image_disabled;
                }

                if (s != null) {
                    s.draw_masked(x, y);
                }
                return;
            }

            case 6: {
                int center_x = Canvas3D.center_x;
                int center_y = Canvas3D.center_y;

                Canvas3D.center_x = x + this.width / 2;
                Canvas3D.center_y = y + this.height / 2;

                int sin = Canvas3D.sin[this.model_pitch] * this.model_zoom >> 16;
                int cos = Canvas3D.cos[this.model_pitch] * this.model_zoom >> 16;

                boolean enabled = this.is_enabled();
                int anim_index = enabled ? this.seq_index_enabled : this.seq_index_disabled;

                Model m;

                if (anim_index == -1) {
                    m = this.get_mesh(-1, -1, enabled);
                } else {
                    Sequence s = Sequence.instance[anim_index];
                    m = this.get_mesh(s.frame_primary[this.sequence_frame], s.frame_secondary[this.sequence_frame], enabled);
                }

                if (m != null) {
                    m.draw(0, this.model_yaw, 0, this.model_pitch, 0, sin, cos);
                }

                Canvas3D.center_x = center_x;
                Canvas3D.center_y = center_y;
                return;
            }

            case 7: {
                BitmapFont rsf = this.font;
                int slot = 0;

                for (int slot_y = 0; slot_y < this.height; slot_y++) {
                    for (int slot_x = 0; slot_x < this.width; slot_x++) {
                        if (this.item_index[slot] > 0) {
                            ObjConfig oc = ObjConfig.get(this.item_index[slot] - 1);
                            String message = oc.name;

                            if (oc.stackable || this.item_count[slot] != 1) {
                                message = message + " x" + Game.format_item_amount(this.item_count[slot]);
                            }

                            int draw_x = x + slot_x * (115 + this.item_margin_x);
                            int draw_y = y + slot_y * (12 + this.item_margin_y);

                            if (this.centered) {
                                rsf.draw(message, draw_x + this.width / 2, draw_y, this.rgb_disabled, this.shadow ? BitmapFont.SHADOW : 0);
                            } else {
                                rsf.draw_string(message, draw_x, draw_y, this.rgb_disabled, this.shadow, false);
                            }
                        }
                        slot++;
                    }
                }
                return;
            }
        }
    }

    public void draw(int x, int y, int scroll_amount) {
        if (!this.visible) {
            return;
        }

        if (this.type != 0) {
            this.draw(x, y);
            return;
        }

        if (this.children == null) {
            return;
        }

        if (this.hidden && Game.hovered_viewport_widget != this.index && Game.hovered_tab_widget != this.index && Game.hovered_chat_widget != this.index) {
            return;
        }

        int _x1 = Canvas2D.left_x;
        int _y1 = Canvas2D.left_y;
        int _x2 = Canvas2D.right_x;
        int _y2 = Canvas2D.right_y;

        Canvas2D.set_bounds(x, y, x + this.width, y + this.height);

        for (int i = 0; i < this.children.length; i++) {
            int child_x = this.child_x[i] + x;
            int child_y = (this.child_y[i] + y) - scroll_amount;

            Widget child = Widget.instance[this.children[i]];

            if (!child.visible) {
                continue;
            }

            child_x += child.x;
            child_y += child.y;

            if (child.action_type > 0) {
                Game.update_widget(child);
            }

            if (child.type == 0) {
                if (child.scroll_amount > child.scroll_height - child.height) {
                    child.scroll_amount = child.scroll_height - child.height;
                }

                if (child.scroll_amount < 0) {
                    child.scroll_amount = 0;
                }

                child.draw(child_x, child_y, child.scroll_amount);

                if (child.scroll_height > child.height) {
                    Game.draw_scrollbar(child_x + child.width, child_y, child.height, child.scroll_height, child.scroll_amount);
                }
            } else {
                child.draw(child_x, child_y);
            }
        }

        Canvas2D.set_bounds(_x1, _y1, _x2, _y2);
    }

    public Model get_mesh(int type, int index) {
        Model m = (Model) model_cache.get((type << 16) + index);

        if (m != null) {
            return m;
        }

        if (type == 1) {
            m = Model.get(index);
        } else if (type == 2) {
            m = ActorConfig.get(index).get_dialog_model();
        } else if (type == 3) {
            m = Game.self.get_dialog_model();
        } else if (type == 4) {
            m = ObjConfig.get(index).get_widget_mesh(50);
        } else if (type == 5) {
            m = null;
        }

        if (m != null) {
            model_cache.insert(m, (type << 16) + index);
        }
        return m;
    }

    public Model get_mesh(int frame1, int frame2, boolean enabled) {
        Model m;

        if (enabled) {
            m = get_mesh(this.model_type_enabled, this.model_index_enabled);
        } else {
            m = get_mesh(this.model_type_disabled, this.model_index_disabled);
        }

        if (m == null) {
            return null;
        }

        if (frame1 == -1 && frame2 == -1 && m.triangle_color == null) {
            return m;
        }

        m = new Model(true, (frame1 == -1) & (frame2 == -1), false, m);

        if (frame1 != -1 || frame2 != -1) {
            m.apply_vertex_weights();
        }

        if (frame1 != -1) {
            m.apply_sequence_frame(frame1);
        }

        if (frame2 != -1) {
            m.apply_sequence_frame(frame2);
        }

        m.apply_lighting(64, 768, -50, -10, -50, true);
        return m;
    }

    public boolean handle_sequences(int cycle) {
        boolean update = false;

        if (this.children == null) {
            return false;
        }

        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] == -1) {
                break;
            }

            Widget w = Widget.instance[this.children[i]];

            if (w.type == 1) {
                update |= w.handle_sequences(cycle);
            }

            if (w.type == 6 && (w.seq_index_disabled != -1 || w.seq_index_enabled != -1)) {
                int seq_index = w.is_enabled() ? w.seq_index_enabled : w.seq_index_disabled;

                if (seq_index != -1) {
                    Sequence sequence = Sequence.instance[seq_index];
                    for (w.sequence_cycle += cycle; w.sequence_cycle > sequence.get_frame_length(w.sequence_frame); ) {
                        w.sequence_cycle -= sequence.get_frame_length(w.sequence_frame) + 1;
                        w.sequence_frame++;
                        if (w.sequence_frame >= sequence.frame_count) {
                            w.sequence_frame -= sequence.padding;
                            if (w.sequence_frame < 0 || w.sequence_frame >= sequence.frame_count) {
                                w.sequence_frame = 0;
                            }
                        }
                        update = true;
                    }
                }
            }
        }

        return update;
    }

    public boolean is_enabled() {
        if (this.script == null || this.script.length == 0) {
            return false;
        }

        for (Script s : this.script) {
            int a = s.execute();
            int b = s.compare_value;

            switch (s.compare_type) {
                case 2:
                    if (a >= b) {
                        return false;
                    }
                    break;
                case 3:
                    if (a <= b) {
                        return false;
                    }
                    break;
                case 4:
                    if (a == b) {
                        return false;
                    }
                    break;
                default:
                    if (a != b) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public void reset_sequence() {
        if (this.children == null) {
            return;
        }

        for (int i = 0; i < this.children.length; i++) {
            if (this.children[i] == -1) {
                break;
            }

            Widget w = Widget.get(this.children[i]);

            if (w.type == 1) {
                w.reset_sequence();
            }

            w.sequence_frame = 0;
            w.sequence_cycle = 0;
        }
    }

    public void scroll(int amount) {
        this.scroll_amount += (this.scroll_height / 30.25D) * amount;

        if (this.scroll_amount < 0) {
            this.scroll_amount = 0;
        }

        int max = this.scroll_height - this.height;

        if (this.scroll_amount > max) {
            this.scroll_amount = max;
        }
    }

    public void swap_slots(int from, int to) {
        int original = item_index[from];
        item_index[from] = item_index[to];
        item_index[to] = (short) original;
        original = item_count[from];
        item_count[from] = item_count[to];
        item_count[to] = original;
    }

}
