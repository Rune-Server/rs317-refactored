package rs.media.impl;

import rs.Game;
import rs.cache.Archive;
import rs.cache.impl.Widget;
import rs.input.model.Area;
import rs.media.Bitmap;
import rs.media.Canvas3D;
import rs.media.ImageProducer;

import static rs.util.JString.*;

public class Sidebar {

    public static class Tab {

        public Bitmap icon;
        public int icon_x, icon_y;
        public final int index;
        public Bitmap redstone;
        public int widget = -1;
        public int x, y;

        private Tab(int i, Bitmap redstone, Bitmap icon, int x, int y, int icon_x, int icon_y) {
            this.index = i;
            this.redstone = redstone;
            this.icon = icon;
            this.x = x;
            this.y = y;
            this.icon_x = icon_x;
            this.icon_y = icon_y;
        }

        public void draw_icon() {
            icon.draw(icon_x, icon_y);
        }

        public void draw_redstone() {
            redstone.draw(x, y);
        }

    }

    public static Bitmap backhmid1, backbase2, invback;
    public static boolean draw;
    public static boolean draw_tabs;
    public static Tab flashing_tab;
    public static final Bitmap[] ICON = new Bitmap[14];
    public static int[] pixels_3d;
    public static ImageProducer producer;
    public static ImageProducer producer_lower_tab;
    public static ImageProducer producer_upper_tab;
    public static final Bitmap[] REDSTONE = new Bitmap[10];
    public static Tab selected_tab;
    public static final Tab[] TAB = new Tab[14];
    public static int widget_index;

    public static void clear_producers() {
        producer = null;
        producer_upper_tab = null;
        producer_lower_tab = null;
    }

    public static void create_producers() {
        producer = new ImageProducer(190, 261);
        producer_lower_tab = new ImageProducer(269, 37);
        producer_upper_tab = new ImageProducer(249, 45);
    }

    public static void draw() {
        if (!draw) {
            return;
        }
        draw = false;

        producer.prepare();
        Canvas3D.pixels = pixels_3d;
        invback.draw(0, 0);

        if (widget_index != -1) {
            Widget.draw(widget_index, 0, 0, 0);
        } else if (selected_tab.widget != -1) {
            Widget.draw(selected_tab.widget, 0, 0, 0);
        }

        if (Menu.visible && Menu.area == Area.TAB) {
            Menu.draw();
        }

        producer.draw(553, 205);
        Game.producer_scene.prepare();
        Canvas3D.pixels = Game.viewport_pixels;
    }

    public static void draw_tabs() {
        if (flashing_tab != null) {
            draw_tabs = true;
        }

        if (draw_tabs) {
            draw_tabs = false;

            if (flashing_tab != null && flashing_tab == selected_tab) {
                flashing_tab = null;
                Game.out.put_opcode(120);
                Game.out.put_byte(selected_tab.index);
            }

            producer_upper_tab.prepare();
            {
                backhmid1.draw(0, 0);

                if (widget_index == -1) {
                    for (int i = 0; i < 7; i++) {
                        Tab tab = TAB[i];

                        if (tab == selected_tab) {
                            tab.draw_redstone();
                        }

                        if (tab.widget == -1) {
                            continue;
                        }

                        if (flashing_tab == tab) {
                            if (Game.loop_cycle % 20 < 10) {
                                tab.draw_icon();
                            }
                            continue;
                        }

                        tab.draw_icon();
                    }
                }
            }
            producer_upper_tab.draw(516, 160);

            producer_lower_tab.prepare();
            {
                backbase2.draw(0, 0);

                if (widget_index == -1) {
                    for (int i = 7; i < 14; i++) {
                        Tab tab = TAB[i];

                        if (tab == selected_tab) {
                            tab.draw_redstone();
                        }

                        if (tab.widget == -1) {
                            continue;
                        }

                        if (flashing_tab == tab) {
                            if (Game.loop_cycle % 20 < 10) {
                                tab.draw_icon();
                            }
                            continue;
                        }

                        tab.draw_icon();
                    }
                }
            }
            producer_lower_tab.draw(496, 466);
            Game.producer_scene.prepare();
        }
    }

    public static Tab get_tab(int i) {
        if (i > TAB.length || i < 0) {
            i = 0;
        }
        return TAB[i];
    }

    public static void load(Archive archive) {
        invback = new Bitmap(archive, "invback");
        backbase2 = new Bitmap(archive, "backbase2");
        backhmid1 = new Bitmap(archive, "backhmid1");

        try {
            for (int i = 0; i < ICON.length; i++) {
                ICON[i] = new Bitmap(archive, SIDE_ICONS, i);
            }
        } catch (Exception e) {
        }

        REDSTONE[0] = new Bitmap(archive, REDSTONE_1);
        REDSTONE[1] = new Bitmap(archive, REDSTONE_2);
        REDSTONE[2] = new Bitmap(archive, REDSTONE_3);
        REDSTONE[3] = new Bitmap(archive, REDSTONE_1).flip_horizontally();
        REDSTONE[4] = new Bitmap(archive, REDSTONE_2).flip_horizontally();
        REDSTONE[5] = new Bitmap(archive, REDSTONE_1).flip_vertically();
        REDSTONE[6] = new Bitmap(archive, REDSTONE_2).flip_vertically();
        REDSTONE[7] = new Bitmap(archive, REDSTONE_3).flip_vertically();
        REDSTONE[8] = new Bitmap(archive, REDSTONE_1).flip_horizontally().flip_vertically();
        REDSTONE[9] = new Bitmap(archive, REDSTONE_2).flip_horizontally().flip_vertically();

        int i = 0;
        TAB[i] = new Tab(i++, REDSTONE[0], ICON[0], 22, 10, 29, 13); // Combat Style
        TAB[i] = new Tab(i++, REDSTONE[1], ICON[1], 54, 8, 53, 11); // Skills
        TAB[i] = new Tab(i++, REDSTONE[1], ICON[2], 82, 8, 82, 11); // Quest Journal
        TAB[i] = new Tab(i++, REDSTONE[2], ICON[3], 110, 8, 115, 12); // Inventory
        TAB[i] = new Tab(i++, REDSTONE[4], ICON[4], 153, 8, 153, 13); // Equipment
        TAB[i] = new Tab(i++, REDSTONE[4], ICON[5], 181, 8, 180, 11); // Prayer
        TAB[i] = new Tab(i++, REDSTONE[3], ICON[6], 209, 9, 208, 13); // Magic
        TAB[i] = new Tab(i++, REDSTONE[6], ICON[7], 42, 0, 42, 0); // Empty
        TAB[i] = new Tab(i++, REDSTONE[6], ICON[7], 74, 0, 74, 2); // Friends
        TAB[i] = new Tab(i++, REDSTONE[6], ICON[8], 102, 0, 102, 3); // Ignores
        TAB[i] = new Tab(i++, REDSTONE[7], ICON[9], 130, 1, 137, 4); // Logout
        TAB[i] = new Tab(i++, REDSTONE[9], ICON[10], 173, 0, 174, 2); // Game Options
        TAB[i] = new Tab(i++, REDSTONE[9], ICON[11], 201, 0, 201, 2); // Player Options
        TAB[i] = new Tab(i++, REDSTONE[8], ICON[12], 229, 0, 226, 2); // Music

        open_tab(3);

    }

    public static void nullify() {
        for (int i = 0; i < ICON.length; i++) {
            ICON[i] = null;
        }
        for (int i = 0; i < REDSTONE.length; i++) {
            REDSTONE[i] = null;
        }
        selected_tab = null;
        flashing_tab = null;
        invback = null;
        backbase2 = null;
        backhmid1 = null;
    }

    public static void open_tab(int i) {
        selected_tab = get_tab(i);
    }

    public static void set_flashing(int i) {
        flashing_tab = get_tab(i);
    }

}
