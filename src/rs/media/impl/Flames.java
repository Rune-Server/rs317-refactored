package rs.media.impl;

import rs.Game;
import rs.media.Bitmap;
import rs.media.Canvas2D;
import rs.media.ImageProducer;
import rs.media.Sprite;
import rs.util.RSColor;

import java.util.Arrays;

public class Flames {

    public static int cycle;
    public static int[] disolve_mask, last_disolve_mask;
    public static int[] pixels;
    public static ImageProducer[] producer;
    public static Bitmap[] bitmap_rune;
    public static Sprite[] image;
    public static int[] palette, palette_red, palette_green, palette_blue;
    public static int[] intensity_map;
    public static int[] distortion_map = new int[256];
    public static int cycle_green, cycle_blue, rune_cycle;

    public static void nullify() {
        Game.process_flames = false;

        while (Game.flame_thread) {
            Game.process_flames = false;

            try {
                Thread.sleep(50);
            } catch (Exception e) {
                /* ignore */
            }
        }

        TitleScreen.bitmap_box = null;
        TitleScreen.bitmap_button = null;

        bitmap_rune = null;
        palette = null;
        palette_red = null;
        palette_green = null;
        palette_blue = null;
        disolve_mask = null;
        last_disolve_mask = null;
        pixels = null;
        intensity_map = null;
        image = null;
    }

    public static void create_producers() {
        producer = new ImageProducer[2];
        producer[0] = new ImageProducer(128, 265);
        Canvas2D.clear();
        producer[1] = new ImageProducer(128, 265);
        Canvas2D.clear();
    }

    public static void create_images() {
        bitmap_rune = new Bitmap[12];

        image = new Sprite[2];

        for (int i = 0; i < image.length; i++) {
            image[i] = new Sprite(128, 265);
            System.arraycopy(producer[i].pixels, 0, image[i].pixels, 0, 128 * 265);
        }

        int i = 0;

        try {
            i = Integer.parseInt(Game.instance.getParameter("fl_icon"));
        } catch (Exception e) {
			/* ignore */
        }

        if (i == 0) {
            for (int j = 0; j < bitmap_rune.length; j++) {
                bitmap_rune[j] = new Bitmap(Game.archive_title, "runes", j);
            }
        } else {
            for (int j = 0; j < bitmap_rune.length; j++) {
                bitmap_rune[j] = new Bitmap(Game.archive_title, "runes", 12 + (j & 3));
            }
        }

        create_palettes();
    }

    public static void create_palettes() {
        palette_red = new int[256];

        for (int i = 0; i < 64; i++) {
            palette_red[i] = i * 0x040000;
        }
        for (int i = 0; i < 64; i++) {
            palette_red[i + 64] = 0xff0000 + (0x0400 * i);
        }
        for (int i = 0; i < 64; i++) {
            palette_red[i + 128] = 0xffff00 + (0x4 * i);
        }
        for (int i = 0; i < 64; i++) {
            palette_red[i + 192] = 0xffffff;
        }

        palette_green = new int[256];
        for (int i = 0; i < 64; i++) {
            palette_green[i] = i * 1024;
        }
        for (int i = 0; i < 64; i++) {
            palette_green[i + 64] = 65280 + 4 * i;
        }
        for (int i = 0; i < 64; i++) {
            palette_green[i + 128] = 65535 + 0x40000 * i;
        }
        for (int i = 0; i < 64; i++) {
            palette_green[i + 192] = 0xffffff;
        }

        palette_blue = new int[256];
        for (int i = 0; i < 64; i++) {
            palette_blue[i] = i * 4;
        }
        for (int i = 0; i < 64; i++) {
            palette_blue[i + 64] = 255 + 0x40000 * i;
        }
        for (int i = 0; i < 64; i++) {
            palette_blue[i + 128] = 0xff00ff + 1024 * i;
        }
        for (int i = 0; i < 64; i++) {
            palette_blue[i + 192] = 0xffffff;
        }

        palette = new int[256];
        pixels = new int[128 * 256];
        disolve_mask = new int[pixels.length];
        last_disolve_mask = new int[pixels.length];
        draw_rune(null);
        intensity_map = new int[pixels.length];
    }

    public static void draw_rune(Bitmap bitmap) {
        Arrays.fill(disolve_mask, 0);

        for (int i = 0; i < 5000; i++) {
            disolve_mask[(int) ((Math.random() * 128D) * 256D)] = (int) (Math.random() * 256D);
        }

        for (int i = 0; i < 20; i++) {
            for (int y = 1; y < 256 - 1; y++) {
                for (int x = 1; x < 127; x++) {
                    int j = x + (y << 7);
                    last_disolve_mask[j] = (disolve_mask[j - 1] + disolve_mask[j + 1] + disolve_mask[j - 128] + disolve_mask[j + 128]) >> 2;
                }
            }

            int[] mask = disolve_mask;
            disolve_mask = last_disolve_mask;
            last_disolve_mask = mask;
        }

        if (bitmap != null) {
            int i = 0;
            for (int y = 0; y < bitmap.height; y++) {
                for (int x = 0; x < bitmap.width; x++) {
                    if (bitmap.pixels[i++] != 0) {
                        disolve_mask[(x + 16 + bitmap.offset_x) + ((y + 16 + bitmap.offset_y) << 7)] = 0;
                    }
                }
            }
        }
    }

    public static void handle() {
        int height = 256;

        // Pixels starts off as an intensity map.

        // Generate the base of the flame
        for (int x = 10; x < 117; x++) {
            int k = (int) (Math.random() * 100D);
            if (k < 50) {
                pixels[x + (height - 2 << 7)] = 255;
            }
        }

        // Add little flame sparklets in random places.
        for (int l = 0; l < 100; l++) {
            int x = (int) (Math.random() * 124D) + 2;
            int y = (int) (Math.random() * 128D) + 128;
            pixels[x + (y << 7)] = 192;
        }

        // Blur the intensity map.
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < 127; x++) {
                int i = x + (y << 7);
                intensity_map[i] = (pixels[i - 1] + pixels[i + 1] + pixels[i - 128] + pixels[i + 128]) / 4;
            }

        }

        rune_cycle += 128;
        if (rune_cycle > disolve_mask.length) {
            rune_cycle -= disolve_mask.length;
            draw_rune(bitmap_rune[(int) (Math.random() * 12D)]);
        }

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < 127; x++) {
                int i = x + (y << 7);
                int j = intensity_map[i + 128] - disolve_mask[i + rune_cycle & disolve_mask.length - 1] / 5;

                if (j < 0) {
                    j = 0;
                }

                pixels[i] = j;
            }

        }

        for (int y = 0; y < height - 1; y++) {
            distortion_map[y] = distortion_map[y + 1];
        }

        distortion_map[height - 1] = (int) (Math.sin((double) Game.loop_cycle / 14D) * 16D + Math.sin((double) Game.loop_cycle / 15D) * 14D + Math.sin((double) Game.loop_cycle / 16D) * 12D);

        if (cycle_green > 0) {
            cycle_green -= 4;
        }

        if (cycle_blue > 0) {
            cycle_blue -= 4;
        }

        if (cycle_green == 0 && cycle_blue == 0) {
            int l3 = (int) (Math.random() * 2000D);
            if (l3 == 0) {
                cycle_green = 1024;
            }
            if (l3 == 1) {
                cycle_blue = 1024;
            }
        }
    }

    public static void handle_palette() {
        if (cycle_green > 0) {
            for (int i = 0; i < 256; i++) {
                if (cycle_green > 768) {
                    palette[i] = RSColor.mix(palette_red[i], palette_green[i], 1024 - cycle_green);
                } else if (cycle_green > 256) {
                    palette[i] = palette_green[i];
                } else {
                    palette[i] = RSColor.mix(palette_green[i], palette_red[i], 256 - cycle_green);
                }
            }
        } else if (cycle_blue > 0) {
            for (int i = 0; i < 256; i++) {
                if (cycle_blue > 768) {
                    palette[i] = RSColor.mix(palette_red[i], palette_blue[i], 1024 - cycle_blue);
                } else if (cycle_blue > 256) {
                    palette[i] = palette_blue[i];
                } else {
                    palette[i] = RSColor.mix(palette_blue[i], palette_red[i], 256 - cycle_blue);
                }
            }
        } else {
            System.arraycopy(palette_red, 0, palette, 0, palette.length);
        }

        System.arraycopy(image[0].pixels, 0, producer[0].pixels, 0, producer[0].pixels.length);

        int height = 256;
        int src_off = 0;
        int dst_off = 1152;
        for (int y = 1; y < height - 1; y++) {
            int l1 = (distortion_map[y] * (height - y)) / height;
            int i = 24 + l1;

            if (i < 0) {
                i = 0;
            }

            src_off += i;

            for (int j = i; j < 128; j++) {
                int rgb = pixels[src_off++];

                if (rgb != 0) {
                    int old = rgb;
                    int alpha = 256 - rgb;
                    rgb = palette[rgb];
                    int src = producer[0].pixels[dst_off];
                    producer[0].pixels[dst_off++] = ((rgb & 0xff00ff) * old + (src & 0xff00ff) * alpha & 0xff00ff00) + ((rgb & 0xff00) * old + (src & 0xff00) * alpha & 0xff0000) >> 8;
                } else {
                    dst_off++;
                }
            }

            dst_off += i;
        }

        producer[0].draw(0, 0);

        System.arraycopy(image[1].pixels, 0, producer[1].pixels, 0, producer[1].pixels.length);

        src_off = 0;
        dst_off = 1176;
        for (int y = 1; y < height - 1; y++) {
            int i3 = (distortion_map[y] * (height - y)) / height;
            int i = 103 - i3;
            dst_off += i3;
            for (int j = 0; j < i; j++) {
                int rgb = pixels[src_off++];
                if (rgb != 0) {
                    int old = rgb;
                    int alpha = 256 - rgb;
                    rgb = palette[rgb];
                    int src = producer[1].pixels[dst_off];
                    producer[1].pixels[dst_off++] = ((rgb & 0xff00ff) * old + (src & 0xff00ff) * alpha & 0xff00ff00) + ((rgb & 0xff00) * old + (src & 0xff00) * alpha & 0xff0000) >> 8;
                } else {
                    dst_off++;
                }
            }

            src_off += 128 - i;
            dst_off += 128 - i - i3;
        }

        producer[1].draw(637, 0);
    }
}
