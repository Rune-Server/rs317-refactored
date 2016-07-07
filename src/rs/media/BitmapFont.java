package rs.media;

import rs.cache.Archive;
import rs.io.Buffer;
import rs.util.Bit;
import rs.util.RSColor;

public class BitmapFont extends Canvas2D {

    public static final byte CENTER = (byte) 0x1;
    public static final byte RIGHT = (byte) 0x2;
    public static final byte SHADOW = (byte) 0x4;
    public static final byte ALLOW_TAGS = (byte) 0x8;
    public static final byte SHADOW_CENTER = SHADOW | CENTER;

    public static BitmapFont SMALL, NORMAL, BOLD, FANCY;
    public byte[] char_width;
    public int height;
    public byte[][] mask;
    public byte[] mask_height;
    public byte[] mask_width;
    public byte[] offset_x;
    public byte[] offset_y;
    public boolean strikethrough;

    public BitmapFont(String name, Archive archive) {
        this.mask = new byte[256][];
        this.mask_width = new byte[256];
        this.mask_height = new byte[256];
        this.offset_x = new byte[256];
        this.offset_y = new byte[256];
        this.char_width = new byte[256];
        this.strikethrough = false;

        Buffer data = new Buffer(archive.get(name + ".dat"));
        Buffer idx = new Buffer(archive.get("index.dat"));
        idx.position = data.get_ushort() + 4;

        int i = idx.get_ubyte();

        if (i > 0) {
            idx.position += 3 * (i - 1);
        }

        for (i = 0; i < 256; i++) {
            this.offset_x[i] = idx.get_byte();
            this.offset_y[i] = idx.get_byte();
            int width = this.mask_width[i] = (byte) idx.get_ushort();
            int height = this.mask_height[i] = (byte) idx.get_ushort();
            int type = idx.get_ubyte();
            this.mask[i] = new byte[width * height];

            if (type == 0) {
                for (int j = 0; j < this.mask[i].length; j++) {
                    this.mask[i][j] = data.get_byte();
                }
            } else if (type == 1) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        mask[i][x + y * width] = data.get_byte();
                    }
                }
            }

            if (height > this.height && i < 128) {
                this.height = height;
            }

            this.offset_x[i] = 1;
            this.char_width[i] = (byte) (width + 2);

            int k2 = 0;
            for (int y = height / 7; y < height; y++) {
                k2 += this.mask[i][y * width];
            }

            if (k2 <= height / 7) {
                this.char_width[i]--;
                this.offset_x[i] = 0;
            }

            k2 = 0;
            for (int y = height / 7; y < height; y++) {
                k2 += this.mask[i][(width - 1) + y * width];
            }

            if (k2 <= height / 7) {
                this.char_width[i]--;
            }

        }

        if (name.equals("q8_full")) {
            this.char_width[32] = this.char_width[73];
            return;
        } else {
            this.char_width[32] = this.char_width[105];
            return;
        }
    }

    public void draw(int color, String s, int x, int y) {
        if (s == null) {
            return;
        }
        y -= height;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                draw_char(mask[c], x + offset_x[c], y + offset_y[c], mask_width[c], mask_height[c], color);
            }
            x += char_width[c];
        }
    }

    public void draw(int color, String s, int x, int k, int y) {
        if (s == null) {
            return;
        }
        x -= getWidth(s) / 2;
        y -= height;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                draw_char(mask[c], x + offset_x[c], y + offset_y[c] + (int) (Math.sin((double) i / 2D + (double) k / 5D) * 5D), mask_width[c], mask_height[c], color);
            }
            x += char_width[c];
        }

    }

    public void draw(String s, int x, int y, int color) {
        draw(s, x, y, color, 0);
    }

    public void draw(String s, int x, int y, int color, int flags) {
        if (s == null) {
            return;
        }

        if (Bit.signed(flags, CENTER)) {
            x -= getWidth(s) / 2;
        }
        if (Bit.signed(flags, RIGHT)) {
            x -= getWidth(s);
        }

        this.draw_string(s, x, y, color, Bit.signed(flags, SHADOW), Bit.signed(flags, ALLOW_TAGS));
    }

    public void draw_centered_wavy(int amplitude, String s, boolean flag, int loop, int y, int x, int color) {
        if (s == null) {
            return;
        }

        double offset = 7D - (double) amplitude / 8D;

        if (offset < 0.0D) {
            offset = 0.0D;
        }

        x -= getWidth(s) / 2;
        y -= height;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                draw_char(mask[c], x + offset_x[c], y + offset_y[c] + (int) (Math.sin((double) i / 1.5D + (double) loop) * offset), mask_width[c], mask_height[c], color);
            }
            x += char_width[c];
        }

    }

    public void draw_centered_wavy2(int x, String s, int cycle, int y, int l) {
        if (s == null) {
            return;
        }

        x -= getWidth(s) / 2;
        y -= height;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                draw_char(mask[c], x + offset_x[c] + (int) (Math.sin((double) i / 5D + (double) cycle / 5D) * 5D), y + offset_y[c] + (int) (Math.sin((double) i / 3D + (double) cycle / 5D) * 5D), mask_width[c], mask_height[c], l);
            }
            x += char_width[c];
        }

    }

    public void draw_char(byte mask[], int x, int y, int width, int height, int color) {
        int dst_off = x + y * Canvas2D.width;
        int dst_step = Canvas2D.width - width;
        int msk_step = 0;
        int msk_off = 0;

        if (y < Canvas2D.left_y) {
            int i = Canvas2D.left_y - y;
            height -= i;
            y = Canvas2D.left_y;
            msk_off += i * width;
            dst_off += i * Canvas2D.width;
        }

        if (y + height >= Canvas2D.right_y) {
            height -= ((y + height) - Canvas2D.right_y) + 1;
        }

        if (x < Canvas2D.left_x) {
            int i = Canvas2D.left_x - x;
            width -= i;
            x = Canvas2D.left_x;
            msk_off += i;
            dst_off += i;
            msk_step += i;
            dst_step += i;
        }

        if (x + width >= Canvas2D.right_x) {
            int i = ((x + width) - Canvas2D.right_x) + 1;
            width -= i;
            msk_step += i;
            dst_step += i;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        draw(mask, msk_off, dst_off, width, height, dst_step, msk_step, color);
    }

    public void draw_char(int opacity, int x, byte mask[], int width, int y, int height, boolean flag, int color) {
        int destOff = x + y * Canvas2D.width;
        int destStep = Canvas2D.width - width;
        int maskStep = 0;
        int maskOff = 0;
        if (y < Canvas2D.left_y) {
            int yStep = Canvas2D.left_y - y;
            height -= yStep;
            y = Canvas2D.left_y;
            maskOff += yStep * width;
            destOff += yStep * Canvas2D.width;
        }
        if (y + height >= Canvas2D.right_y) {
            height -= ((y + height) - Canvas2D.right_y) + 1;
        }
        if (x < Canvas2D.left_x) {
            int xStep = Canvas2D.left_x - x;
            width -= xStep;
            x = Canvas2D.left_x;
            maskOff += xStep;
            destOff += xStep;
            maskStep += xStep;
            destStep += xStep;
        }
        if (x + width >= Canvas2D.right_x) {
            int step = ((x + width) - Canvas2D.right_x) + 1;
            width -= step;
            maskStep += step;
            destStep += step;
        }
        if (width <= 0 || height <= 0) {
            return;
        }

        draw(mask, maskOff, destOff, width, height, destStep, maskStep, color, opacity);
    }

    public void draw_string(boolean shadow, int x, int color, String s, int y, int opacity) {
        if (s == null) {
            return;
        }
        y -= height;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '@' && i + 4 < s.length() && s.charAt(i + 4) == '@') {
                int tagColor = get_tag_color(s.substring(i + 1, i + 4));
                if (tagColor != -1) {
                    color = tagColor;
                }
                i += 4;
            } else {
                char c = s.charAt(i);
                if (c != ' ') {
                    if (shadow) {
                        draw_char(opacity / 2, x + offset_x[c] + 1, mask[c], mask_width[c], y + offset_y[c] + 1, mask_height[c], false, 0);
                    }
                    draw_char(opacity, x + offset_x[c], mask[c], mask_width[c], y + offset_y[c], mask_height[c], false, color);
                }
                x += char_width[c];
            }
        }

    }

    public void draw_string(String s, int x, int y, int color, boolean shadow, boolean allowTags) {
        strikethrough = false;
        int startX = x;
        if (s == null) {
            return;
        }
        y -= height;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '@' && i + 4 < s.length() && s.charAt(i + 4) == '@' && allowTags) {
                int rgb = get_tag_color(s.substring(i + 1, i + 4));
                if (rgb != -1) {
                    color = rgb;
                }
                i += 4;
            } else {
                char c = s.charAt(i);
                if (c != ' ') {
                    if (shadow) {
                        draw_char(mask[c], x + offset_x[c] + 1, y + offset_y[c] + 1, mask_width[c], mask_height[c], 0);
                    }
                    draw_char(mask[c], x + offset_x[c], y + offset_y[c], mask_width[c], mask_height[c], color);
                }
                x += char_width[c];
            }
        }
        if (strikethrough) {
            draw_line_h(startX, y + (int) ((double) height * 0.7D), x - startX, 0x800000);
        }
    }

    public int get_tag_color(String s) {
        if (s.equals("str")) {
            strikethrough = true;
            return -1;
        } else if (s.equals("end")) {
            strikethrough = false;
            return -1;
        }

        return RSColor.get_tag_color(s);
    }

    public int getWidth(String s) {
        if (s == null) {
            return 0;
        }
        int width = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '@' && i + 4 < s.length() && s.charAt(i + 4) == '@') {
                i += 4;
            } else {
                width += char_width[s.charAt(i)];
            }
        }
        return width;
    }
}
