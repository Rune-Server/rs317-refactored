package rs.media.impl;

import rs.Game;
import rs.input.Mouse;
import rs.input.model.Area;
import rs.media.BitmapFont;
import rs.media.Canvas2D;
import rs.util.JString;
import rs.util.RSColor;

public class Menu {

    public static class Option {

        public int action;
        public String caption;
        public int[] parameters = new int[3];

        public int getParameter(int i) {
            if (i >= parameters.length) {
                return -1;
            }
            return parameters[i];
        }

        public void set(Option option) {
            this.caption = option.caption;
            this.action = option.action;
            this.parameters = option.parameters;
        }

    }

    public static Area area;
    public static final int COLOR_FOREGROUND = 0x5D5447;
    public static int count;
    public static final int[] DRAG_ACTIONS = {632, 78, 867, 431, 53, 74, 454, 539, 493, 847, 447, 1125};
    public static final Option[] options = new Option[500];
    public static boolean visible;

    public static int x, y, width, height;

    static {
        for (int i = 0; i < Menu.options.length; i++) {
            Menu.options[i] = new Option();
        }
    }

    /**
     * Adds an option to the context menu.
     *
     * @param caption    the caption.
     * @param action     the action.
     * @param parameters the parameters.
     */
    public static void add(String caption, int action, int... parameters) {
        options[count].caption = caption;
        options[count].action = action;
        options[count].parameters = parameters;
        count++;
    }

    /**
     * Draws the context menu.
     */
    public static void draw() {
        Canvas2D.fill_rect(x, y, width, height, COLOR_FOREGROUND);
        Canvas2D.fill_rect(x + 1, y + 1, width - 2, 16, RSColor.BLACK);
        Canvas2D.draw_rect(x + 1, y + 18, width - 2, height - 19, RSColor.BLACK);

        BitmapFont.BOLD.draw(JString.CHOOSE_OPTION, x + 3, y + 14, COLOR_FOREGROUND);

        int cur_x = Mouse.last_x - area.x;
        int cur_y = Mouse.last_y - area.y;

        for (int i = 0; i < count; i++) {
            int option_y = y + 31 + (count - 1 - i) * 15;
            int color = RSColor.WHITE;

            if (cur_x > x && cur_x < x + width && cur_y > option_y - 13 && cur_y < option_y + 3) {
                color = 0xFFFF00;
            }

            BitmapFont.BOLD.draw(options[i].caption, x + 3, option_y, color, BitmapFont.SHADOW | BitmapFont.ALLOW_TAGS);
        }
    }

    /**
     * Draws the text located at the top left.
     */
    public static void draw_tooltip() {
        int count = Menu.count;

        if (count < 2 && !Game.selected_item && !Game.selected_widget) {
            return;
        }

        String s;

        if (Game.selected_item && count < 2) {
            s = "Use " + Game.selected_item_name + " with...";
        } else if (Game.selected_widget && count < 2) {
            s = Game.selected_tooltip + "...";
        } else {
            s = Menu.get_last_caption();
        }

        if (count > 2) {
            s = s + "@whi@ / " + (count - 2) + " more options";
        }

        BitmapFont.BOLD.draw_string(true, 4, 0xffffff, s, 15, 210);
    }

    public static int get_action(int i) {
        return options[i].action;
    }

    public static String get_caption(int i) {
        return options[i].caption;
    }

    public static int get_last_action() {
        return options[count - 1].action;
    }

    public static String get_last_caption() {
        return options[count - 1].caption;
    }

    public static Option get_last_option() {
        return options[count - 1];
    }

    public static int get_last_param(int i) {
        return options[count - 1].getParameter(i);
    }

    public static int get_param(int i, int j) {
        return options[i].getParameter(j);
    }

    /**
     * Processes the context menu.
     */
    public static void handle() {
        if (Game.drag_area != 0) {
            return;
        }
        Menu.prepare();
        Game.handle_mouse();
        Menu.sort();
    }

    /**
     * Nullifies all options.
     */
    public static void nullify() {
        for (int i = 0; i < Menu.options.length; i++) {
            Menu.options[i] = null;
        }
    }

    /**
     * Prepares the context menu.
     */
    public static void prepare() {
        Menu.reset();
        Menu.add("Cancel", 1107);
    }

    public static void reset() {
        Menu.count = 0;
    }

    /**
     * Shows the context menu.
     */
    public static void show() {
        int width = BitmapFont.BOLD.getWidth(JString.CHOOSE_OPTION);
        int height = 15 * count + 21;

        for (int i = 0; i < Menu.count; i++) {
            int s_width = BitmapFont.BOLD.getWidth(Menu.options[i].caption);
            if (s_width > width) {
                width = s_width;
            }
        }

        width += 8;

        for (Area a : Area.values()) {
            if (a.contains(Mouse.click_x, Mouse.click_y)) {
                int x = (Mouse.click_x - a.x) - (width / 2);
                int y = (Mouse.click_y - a.y);

                if (x + width > a.width) {
                    x = a.width - width;
                }

                if (x < 0) {
                    x = 0;
                }

                if (y + height > a.height) {
                    y = a.height - height;
                }

                if (y < 0) {
                    y = 0;
                }

                Menu.visible = true;
                Menu.area = a;
                Menu.x = x;
                Menu.y = y;
                Menu.width = width;
                Menu.height = height;
                break;
            }
        }
    }

    /**
     * Sorts the menu options.
     */
    public static void sort() {
        boolean unsorted = false;
        while (!unsorted) {
            unsorted = true;
            for (int i = 0; i < Menu.count - 1; i++) {
                if (Menu.get_action(i) < 1000 && Menu.get_action(i + 1) > 1000) {
                    Menu.swap(i, i + 1);
                    unsorted = false;
                }
            }
        }
    }

    /**
     * Swaps the provided options.
     *
     * @param a option a.
     * @param b option b.
     */
    public static void swap(int a, int b) {
        String caption = options[a].caption;
        int action = options[a].action;
        int[] params = options[a].parameters;
        options[a].set(options[b]);
        options[b].caption = caption;
        options[b].action = action;
        options[b].parameters = params;
    }

}
