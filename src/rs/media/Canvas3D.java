package rs.media;

import rs.Game;
import rs.cache.Archive;

import java.util.Arrays;
import java.util.Random;

public class Canvas3D extends Canvas2D {

    public static int cycle;

    public static int center_x;
    public static int center_y;
    public static boolean check_bounds;

    public static int[] light_decay;
    public static int[] shadow_decay;

    public static int opacity;
    public static boolean opaque;

    public static int palette[] = new int[0x10000];
    public static int pixels[];
    public static int sin[];
    public static int cos[];

    public static int[][] texel_cache = new int[50][];
    public static int texel_pointer;
    public static int[][] texel_pool;

    public static Bitmap[] texture = new Bitmap[50];
    public static int[] texture_average_rgb = new int[50];
    public static int texture_count;
    public static int texture_cycle[] = new int[50];
    public static int texture_palette[][] = new int[50][];
    public static boolean[] texture_translucent = new boolean[50];
    public static boolean texturize = true;

    public static Random random = new Random();

    static {
        shadow_decay = new int[512];
        light_decay = new int[2048];

        sin = new int[2048];
        cos = new int[2048];

        for (int i = 1; i < 512; i++) {
            shadow_decay[i] = 0x8000 / i;
        }

        for (int i = 1; i < 2048; i++) {
            light_decay[i] = 0x10000 / i;
        }

        for (int i = 0; i < 2048; i++) {
            sin[i] = (int) (65536D * Math.sin((double) i * 0.0030679615D));
            cos[i] = (int) (65536D * Math.cos((double) i * 0.0030679615D));
        }

    }

    public static void clear_textures() {
        texel_pool = null;

        for (int j = 0; j < 50; j++) {
            texel_cache[j] = null;
        }
    }

    public static void create_palette(double brightness) {
        brightness += Math.random() * 0.030D - 0.015D;
        int position = 0;

        double twothird = 2D / 3D;
        double onethird = twothird / 2D;

        for (int y = 0; y < 512; y++) {
            double d1 = (double) (y / 8) / 64D + (1D / 128D);
            double d2 = (double) (y & 7) / 8D + (1D / 16D);

            for (int x = 0; x < 128; x++) {
                double d3 = (double) x / 128D;
                double d4 = d3;
                double d5 = d3;
                double d6 = d3;

                if (d2 != 0.0D) {
                    double d7;
                    if (d3 < 0.5D) {
                        d7 = d3 * (1.0D + d2);
                    } else {
                        d7 = (d3 + d2) - d3 * d2;
                    }

                    double d8 = 2D * d3 - d7;
                    double d9 = d1 + onethird;

                    if (d9 > 1.0D) {
                        d9--;
                    }

                    double d10 = d1;
                    double d11 = d1 - onethird;

                    if (d11 < 0.0D) {
                        d11++;
                    }

                    if (6D * d9 < 1.0D) {
                        d4 = d8 + (d7 - d8) * 6D * d9;
                    } else if (2D * d9 < 1.0D) {
                        d4 = d7;
                    } else if (3D * d9 < 2D) {
                        d4 = d8 + (d7 - d8) * (twothird - d9) * 6D;
                    } else {
                        d4 = d8;
                    }

                    if (6D * d10 < 1.0D) {
                        d5 = d8 + (d7 - d8) * 6D * d10;
                    } else if (2D * d10 < 1.0D) {
                        d5 = d7;
                    } else if (3D * d10 < 2D) {
                        d5 = d8 + (d7 - d8) * (twothird - d10) * 6D;
                    } else {
                        d5 = d8;
                    }

                    if (6D * d11 < 1.0D) {
                        d6 = d8 + (d7 - d8) * 6D * d11;
                    } else if (2D * d11 < 1.0D) {
                        d6 = d7;
                    } else if (3D * d11 < 2D) {
                        d6 = d8 + (d7 - d8) * (twothird - d11) * 6D;
                    } else {
                        d6 = d8;
                    }
                }

                int red = (int) (d4 * 256D);
                int green = (int) (d5 * 256D);
                int blue = (int) (d6 * 256D);
                int rgb = (red << 16) + (green << 8) + blue;
                rgb = Canvas3D.linear_rgb_brightness(rgb, brightness);

                if (rgb == 0) {
                    rgb = 1;
                }

                palette[position++] = rgb;
            }
        }

        for (int i = 0; i < 50; i++) {
            if (texture[i] != null) {
                int palette[] = texture[i].palette;
                texture_palette[i] = new int[palette.length];
                for (int j = 0; j < palette.length; j++) {
                    texture_palette[i][j] = Canvas3D.linear_rgb_brightness(palette[j], brightness);

                    if ((texture_palette[i][j] & 0xf8f8ff) == 0 && j != 0) {
                        texture_palette[i][j] = 1;
                    }
                }
            }
        }

        for (int i = 0; i < 50; i++) {
            update_texture(i);
        }

    }

    public static void draw_flat_triangle(int a_x, int a_y, int b_x, int b_y, int c_x, int c_y, int rgb) {
        // slopes
        int a_to_b = 0, b_to_c = 0, c_to_a = 0;

        if (b_y != a_y) {
            a_to_b = (b_x - a_x << 16) / (b_y - a_y);
        }

        if (c_y != b_y) {
            b_to_c = (c_x - b_x << 16) / (c_y - b_y);
        }

        if (c_y != a_y) {
            c_to_a = (a_x - c_x << 16) / (a_y - c_y);
        }

        //B    /|
        //    / |
        //   /  |
        //  /   |
        //A ----- C
        if (a_y <= b_y && a_y <= c_y) {
            if (a_y >= Canvas2D.right_y) {
                return;
            }

            if (b_y > Canvas2D.right_y) {
                b_y = Canvas2D.right_y;
            }

            if (c_y > Canvas2D.right_y) {
                c_y = Canvas2D.right_y;
            }

            if (b_y < c_y) {
                c_x = a_x <<= 16;

                if (a_y < 0) {
                    c_x -= c_to_a * a_y;
                    a_x -= a_to_b * a_y;
                    a_y = 0;
                }

                b_x <<= 16;

                if (b_y < 0) {
                    b_x -= b_to_c * b_y;
                    b_y = 0;
                }

                if (a_y != b_y && c_to_a < a_to_b || a_y == b_y && c_to_a > b_to_c) {
                    c_y -= b_y;
                    b_y -= a_y;

                    for (a_y = pixels[a_y]; --b_y >= 0; a_y += Canvas2D.width) {
                        draw_scanline(Canvas2D.pixels, a_y, rgb, c_x >> 16, a_x >> 16);
                        c_x += c_to_a;
                        a_x += a_to_b;
                    }

                    while (--c_y >= 0) {
                        draw_scanline(Canvas2D.pixels, a_y, rgb, c_x >> 16, b_x >> 16);

                        c_x += c_to_a;
                        b_x += b_to_c;
                        a_y += Canvas2D.width;
                    }
                    return;
                }

                c_y -= b_y;
                b_y -= a_y;

                for (a_y = pixels[a_y]; --b_y >= 0; a_y += Canvas2D.width) {
                    draw_scanline(Canvas2D.pixels, a_y, rgb, a_x >> 16, c_x >> 16);
                    c_x += c_to_a;
                    a_x += a_to_b;
                }

                while (--c_y >= 0) {
                    draw_scanline(Canvas2D.pixels, a_y, rgb, b_x >> 16, c_x >> 16);
                    c_x += c_to_a;
                    b_x += b_to_c;
                    a_y += Canvas2D.width;
                }
                return;
            }

            b_x = a_x <<= 16;

            if (a_y < 0) {
                b_x -= c_to_a * a_y;
                a_x -= a_to_b * a_y;
                a_y = 0;
            }

            c_x <<= 16;

            if (c_y < 0) {
                c_x -= b_to_c * c_y;
                c_y = 0;
            }

            if (a_y != c_y && c_to_a < a_to_b || a_y == c_y && b_to_c > a_to_b) {
                b_y -= c_y;
                c_y -= a_y;

                for (a_y = pixels[a_y]; --c_y >= 0; a_y += Canvas2D.width) {
                    draw_scanline(Canvas2D.pixels, a_y, rgb, b_x >> 16, a_x >> 16);

                    b_x += c_to_a;
                    a_x += a_to_b;
                }

                while (--b_y >= 0) {
                    draw_scanline(Canvas2D.pixels, a_y, rgb, c_x >> 16, a_x >> 16);

                    c_x += b_to_c;
                    a_x += a_to_b;
                    a_y += Canvas2D.width;
                }
                return;
            }

            b_y -= c_y;
            c_y -= a_y;

            for (a_y = pixels[a_y]; --c_y >= 0; a_y += Canvas2D.width) {
                draw_scanline(Canvas2D.pixels, a_y, rgb, a_x >> 16, b_x >> 16);
                b_x += c_to_a;
                a_x += a_to_b;
            }

            while (--b_y >= 0) {
                draw_scanline(Canvas2D.pixels, a_y, rgb, a_x >> 16, c_x >> 16);
                c_x += b_to_c;
                a_x += a_to_b;
                a_y += Canvas2D.width;
            }
            return;
        }

        //A |\
        //  | \
        //  |  \
        //  |   \
        //C ----- B
        if (b_y <= c_y) {
            if (b_y >= Canvas2D.right_y) {
                return;
            }

            if (c_y > Canvas2D.right_y) {
                c_y = Canvas2D.right_y;
            }

            if (a_y > Canvas2D.right_y) {
                a_y = Canvas2D.right_y;
            }

            if (c_y < a_y) {
                a_x = b_x <<= 16;

                if (b_y < 0) {
                    a_x -= a_to_b * b_y;
                    b_x -= b_to_c * b_y;
                    b_y = 0;
                }

                c_x <<= 16;

                if (c_y < 0) {
                    c_x -= c_to_a * c_y;
                    c_y = 0;
                }

                if (b_y != c_y && a_to_b < b_to_c || b_y == c_y && a_to_b > c_to_a) {
                    a_y -= c_y;
                    c_y -= b_y;

                    for (b_y = pixels[b_y]; --c_y >= 0; b_y += Canvas2D.width) {
                        draw_scanline(Canvas2D.pixels, b_y, rgb, a_x >> 16, b_x >> 16);
                        a_x += a_to_b;
                        b_x += b_to_c;
                    }

                    while (--a_y >= 0) {
                        draw_scanline(Canvas2D.pixels, b_y, rgb, a_x >> 16, c_x >> 16);
                        a_x += a_to_b;
                        c_x += c_to_a;
                        b_y += Canvas2D.width;
                    }
                    return;
                }

                a_y -= c_y;
                c_y -= b_y;

                for (b_y = pixels[b_y]; --c_y >= 0; b_y += Canvas2D.width) {
                    draw_scanline(Canvas2D.pixels, b_y, rgb, b_x >> 16, a_x >> 16);
                    a_x += a_to_b;
                    b_x += b_to_c;
                }

                while (--a_y >= 0) {
                    draw_scanline(Canvas2D.pixels, b_y, rgb, c_x >> 16, a_x >> 16);
                    a_x += a_to_b;
                    c_x += c_to_a;
                    b_y += Canvas2D.width;
                }
                return;
            }

            c_x = b_x <<= 16;

            if (b_y < 0) {
                c_x -= a_to_b * b_y;
                b_x -= b_to_c * b_y;
                b_y = 0;
            }

            a_x <<= 16;

            if (a_y < 0) {
                a_x -= c_to_a * a_y;
                a_y = 0;
            }

            if (a_to_b < b_to_c) {
                c_y -= a_y;
                a_y -= b_y;
                for (b_y = pixels[b_y]; --a_y >= 0; b_y += Canvas2D.width) {
                    draw_scanline(Canvas2D.pixels, b_y, rgb, c_x >> 16, b_x >> 16);
                    c_x += a_to_b;
                    b_x += b_to_c;
                }

                while (--c_y >= 0) {
                    draw_scanline(Canvas2D.pixels, b_y, rgb, a_x >> 16, b_x >> 16);
                    a_x += c_to_a;
                    b_x += b_to_c;
                    b_y += Canvas2D.width;
                }
                return;
            }

            c_y -= a_y;
            a_y -= b_y;

            for (b_y = pixels[b_y]; --a_y >= 0; b_y += Canvas2D.width) {
                draw_scanline(Canvas2D.pixels, b_y, rgb, b_x >> 16, c_x >> 16);
                c_x += a_to_b;
                b_x += b_to_c;
            }

            while (--c_y >= 0) {
                draw_scanline(Canvas2D.pixels, b_y, rgb, b_x >> 16, a_x >> 16);
                a_x += c_to_a;
                b_x += b_to_c;
                b_y += Canvas2D.width;
            }
            return;
        }

        if (c_y >= Canvas2D.right_y) {
            return;
        }

        if (a_y > Canvas2D.right_y) {
            a_y = Canvas2D.right_y;
        }

        if (b_y > Canvas2D.right_y) {
            b_y = Canvas2D.right_y;
        }

        if (a_y < b_y) {
            b_x = c_x <<= 16;

            if (c_y < 0) {
                b_x -= b_to_c * c_y;
                c_x -= c_to_a * c_y;
                c_y = 0;
            }

            a_x <<= 16;

            if (a_y < 0) {
                a_x -= a_to_b * a_y;
                a_y = 0;
            }

            if (b_to_c < c_to_a) {
                b_y -= a_y;
                a_y -= c_y;

                for (c_y = pixels[c_y]; --a_y >= 0; c_y += Canvas2D.width) {
                    draw_scanline(Canvas2D.pixels, c_y, rgb, b_x >> 16, c_x >> 16);
                    b_x += b_to_c;
                    c_x += c_to_a;
                }

                while (--b_y >= 0) {
                    draw_scanline(Canvas2D.pixels, c_y, rgb, b_x >> 16, a_x >> 16);
                    b_x += b_to_c;
                    a_x += a_to_b;
                    c_y += Canvas2D.width;
                }
                return;
            }

            b_y -= a_y;
            a_y -= c_y;

            for (c_y = pixels[c_y]; --a_y >= 0; c_y += Canvas2D.width) {
                draw_scanline(Canvas2D.pixels, c_y, rgb, c_x >> 16, b_x >> 16);
                b_x += b_to_c;
                c_x += c_to_a;
            }

            while (--b_y >= 0) {
                draw_scanline(Canvas2D.pixels, c_y, rgb, a_x >> 16, b_x >> 16);
                b_x += b_to_c;
                a_x += a_to_b;
                c_y += Canvas2D.width;
            }
            return;
        }

        a_x = c_x <<= 16;

        if (c_y < 0) {
            a_x -= b_to_c * c_y;
            c_x -= c_to_a * c_y;
            c_y = 0;
        }

        b_x <<= 16;

        if (b_y < 0) {
            b_x -= a_to_b * b_y;
            b_y = 0;
        }

        if (b_to_c < c_to_a) {
            a_y -= b_y;
            b_y -= c_y;

            for (c_y = pixels[c_y]; --b_y >= 0; c_y += Canvas2D.width) {
                draw_scanline(Canvas2D.pixels, c_y, rgb, a_x >> 16, c_x >> 16);
                a_x += b_to_c;
                c_x += c_to_a;
            }

            while (--a_y >= 0) {
                draw_scanline(Canvas2D.pixels, c_y, rgb, b_x >> 16, c_x >> 16);
                b_x += a_to_b;
                c_x += c_to_a;
                c_y += Canvas2D.width;
            }
            return;
        }

        a_y -= b_y;
        b_y -= c_y;

        for (c_y = pixels[c_y]; --b_y >= 0; c_y += Canvas2D.width) {
            draw_scanline(Canvas2D.pixels, c_y, rgb, c_x >> 16, a_x >> 16);
            a_x += b_to_c;
            c_x += c_to_a;
        }

        while (--a_y >= 0) {
            draw_scanline(Canvas2D.pixels, c_y, rgb, c_x >> 16, b_x >> 16);

            b_x += a_to_b;
            c_x += c_to_a;
            c_y += Canvas2D.width;
        }
    }

    public static void draw_gradient_scanline(int dest[], int dest_off, int color, int position, int length, int x2, int hsl, int x4) {
        if (texturize) {
            int cs1;

            if (check_bounds) {
                if (x2 - length > 3) {
                    cs1 = (x4 - hsl) / (x2 - length);
                } else {
                    cs1 = 0;
                }

                if (x2 > Canvas2D.bound) {
                    x2 = Canvas2D.bound;
                }

                if (length < 0) {
                    hsl -= length * cs1;
                    length = 0;
                }

                if (length >= x2) {
                    return;
                }

                dest_off += length;
                position = x2 - length >> 2;
                cs1 <<= 2;
            } else {
                if (length >= x2) {
                    return;
                }

                dest_off += length;
                position = x2 - length >> 2;

                if (position > 0) {
                    cs1 = (x4 - hsl) * shadow_decay[position] >> 15;
                } else {
                    cs1 = 0;
                }
            }

            if (Canvas3D.opacity == 0) {
                while (--position >= 0) {
                    color = Canvas3D.palette[hsl >> 8];
                    hsl += cs1;
                    dest[dest_off++] = color;
                    dest[dest_off++] = color;
                    dest[dest_off++] = color;
                    dest[dest_off++] = color;
                }
                position = x2 - length & 3;
                if (position > 0) {
                    color = Canvas3D.palette[hsl >> 8];
                    do {
                        dest[dest_off++] = color;
                    } while (--position > 0);
                    return;
                }
            } else {
                int opacity = Canvas3D.opacity;
                int alpha = 256 - Canvas3D.opacity;
                while (--position >= 0) {
                    color = Canvas3D.palette[hsl >> 8];
                    hsl += cs1;
                    color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((color & 0xff00) * alpha >> 8 & 0xff00);
                    dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
                    dest_off++;
                    dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
                    dest_off++;
                    dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
                    dest_off++;
                    dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
                    dest_off++;
                }
                position = x2 - length & 3;
                if (position > 0) {
                    color = Canvas3D.palette[hsl >> 8];
                    color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((color & 0xff00) * alpha >> 8 & 0xff00);
                    do {
                        dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
                        dest_off++;
                    } while (--position > 0);
                }
            }
            return;
        }

        if (length >= x2) {
            return;
        }

        int color_step = (x4 - hsl) / (x2 - length);

        if (check_bounds) {
            if (x2 > Canvas2D.bound) {
                x2 = Canvas2D.bound;
            }
            if (length < 0) {
                hsl -= length * color_step;
                length = 0;
            }
            if (length >= x2) {
                return;
            }
        }

        dest_off += length;
        position = x2 - length;

        if (Canvas3D.opacity == 0) {
            do {
                dest[dest_off++] = palette[hsl >> 8];
                hsl += color_step;
            } while (--position > 0);
            return;
        }

        int opacity = Canvas3D.opacity;
        int alpha = 256 - Canvas3D.opacity;

        do {
            color = palette[hsl >> 8];
            hsl += color_step;
            color = ((color & 0xff00ff) * alpha >> 8 & 0xff00ff) + ((color & 0xff00) * alpha >> 8 & 0xff00);
            dest[dest_off] = color + ((dest[dest_off] & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((dest[dest_off] & 0xff00) * opacity >> 8 & 0xff00);
            dest_off++;
        } while (--position > 0);
    }

    public static void draw_scanline(int dst[], int off, int rgb, int x1, int x2) {
        if (check_bounds) {
            if (x2 > Canvas2D.bound) {
                x2 = Canvas2D.bound;
            }
            if (x1 < 0) {
                x1 = 0;
            }
        }

        if (x1 >= x2) {
            return;
        }

        off += x1;
        int length = x2 - x1;

        if (Canvas3D.opacity == 0) {
            while (length-- > 0) {
                dst[off++] = rgb;
            }
        } else {
            int opacity = 256 - Canvas3D.opacity;
            rgb = ((rgb & 0xff00ff) * opacity >> 8 & 0xff00ff) + ((rgb & 0xff00) * opacity >> 8 & 0xff00);

            while (length-- >= 0) {
                dst[off] = rgb + ((dst[off] & 0xff00ff) * Canvas3D.opacity >> 8 & 0xff00ff) + ((dst[off] & 0xff00) * Canvas3D.opacity >> 8 & 0xff00);
                off++;
            }
        }
    }

    public static void draw_shaded_triangle(int x1, int y1, int x2, int y2, int x3, int y3, int hsl1, int hsl2, int hsl3) {
        // Triangle Slopes
        int s1 = 0, s2 = 0, s3 = 0;
        // Color Slopes
        int cs1 = 0, cs2 = 0, cs3 = 0;

        if (y2 != y1) {
            s1 = (x2 - x1 << 16) / (y2 - y1);
            cs1 = (hsl2 - hsl1 << 15) / (y2 - y1);
        }

        if (y3 != y2) {
            s2 = (x3 - x2 << 16) / (y3 - y2);
            cs2 = (hsl3 - hsl2 << 15) / (y3 - y2);
        }

        if (y3 != y1) {
            s3 = (x1 - x3 << 16) / (y1 - y3);
            cs3 = (hsl1 - hsl3 << 15) / (y1 - y3);
        }

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Canvas2D.right_y) {
                return;
            }

            if (y2 > Canvas2D.right_y) {
                y2 = Canvas2D.right_y;
            }

            if (y3 > Canvas2D.right_y) {
                y3 = Canvas2D.right_y;
            }

            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 15;

                if (y1 < 0) {
                    x3 -= s3 * y1;
                    x1 -= s1 * y1;
                    hsl3 -= cs3 * y1;
                    hsl1 -= cs1 * y1;
                    y1 = 0;
                }

                x2 <<= 16;
                hsl2 <<= 15;

                if (y2 < 0) {
                    x2 -= s2 * y2;
                    hsl2 -= cs2 * y2;
                    y2 = 0;
                }

                if (y1 != y2 && s3 < s1 || y1 == y2 && s3 > s2) {
                    y3 -= y2;
                    y2 -= y1;

                    for (y1 = pixels[y1]; --y2 >= 0; y1 += Canvas2D.width) {
                        draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
                        x3 += s3;
                        x1 += s1;
                        hsl3 += cs3;
                        hsl1 += cs1;
                    }

                    while (--y3 >= 0) {
                        draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
                        x3 += s3;
                        x2 += s2;
                        hsl3 += cs3;
                        hsl2 += cs2;
                        y1 += Canvas2D.width;
                    }
                    return;
                }

                y3 -= y2;
                y2 -= y1;

                for (y1 = pixels[y1]; --y2 >= 0; y1 += Canvas2D.width) {
                    draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
                    x3 += s3;
                    x1 += s1;
                    hsl3 += cs3;
                    hsl1 += cs1;
                }

                while (--y3 >= 0) {
                    draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
                    x3 += s3;
                    x2 += s2;
                    hsl3 += cs3;
                    hsl2 += cs2;
                    y1 += Canvas2D.width;
                }
                return;
            }

            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 15;

            if (y1 < 0) {
                x2 -= s3 * y1;
                x1 -= s1 * y1;
                hsl2 -= cs3 * y1;
                hsl1 -= cs1 * y1;
                y1 = 0;
            }

            x3 <<= 16;
            hsl3 <<= 15;

            if (y3 < 0) {
                x3 -= s2 * y3;
                hsl3 -= cs2 * y3;
                y3 = 0;
            }

            if (y1 != y3 && s3 < s1 || y1 == y3 && s2 > s1) {
                y2 -= y3;
                y3 -= y1;

                for (y1 = pixels[y1]; --y3 >= 0; y1 += Canvas2D.width) {
                    draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
                    x2 += s3;
                    x1 += s1;
                    hsl2 += cs3;
                    hsl1 += cs1;
                }

                while (--y2 >= 0) {
                    draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
                    x3 += s2;
                    x1 += s1;
                    hsl3 += cs2;
                    hsl1 += cs1;
                    y1 += Canvas2D.width;
                }
                return;
            }

            y2 -= y3;
            y3 -= y1;

            for (y1 = pixels[y1]; --y3 >= 0; y1 += Canvas2D.width) {
                draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
                x2 += s3;
                x1 += s1;
                hsl2 += cs3;
                hsl1 += cs1;
            }

            while (--y2 >= 0) {
                draw_gradient_scanline(Canvas2D.pixels, y1, 0, 0, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
                x3 += s2;
                x1 += s1;
                hsl3 += cs2;
                hsl1 += cs1;
                y1 += Canvas2D.width;
            }
            return;
        }

        if (y2 <= y3) {
            if (y2 >= Canvas2D.right_y) {
                return;
            }

            if (y3 > Canvas2D.right_y) {
                y3 = Canvas2D.right_y;
            }

            if (y1 > Canvas2D.right_y) {
                y1 = Canvas2D.right_y;
            }

            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 15;

                if (y2 < 0) {
                    x1 -= s1 * y2;
                    x2 -= s2 * y2;
                    hsl1 -= cs1 * y2;
                    hsl2 -= cs2 * y2;
                    y2 = 0;
                }

                x3 <<= 16;
                hsl3 <<= 15;

                if (y3 < 0) {
                    x3 -= s3 * y3;
                    hsl3 -= cs3 * y3;
                    y3 = 0;
                }

                if (y2 != y3 && s1 < s2 || y2 == y3 && s1 > s3) {
                    y1 -= y3;
                    y3 -= y2;

                    for (y2 = pixels[y2]; --y3 >= 0; y2 += Canvas2D.width) {
                        draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
                        x1 += s1;
                        x2 += s2;
                        hsl1 += cs1;
                        hsl2 += cs2;
                    }

                    while (--y1 >= 0) {
                        draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
                        x1 += s1;
                        x3 += s3;
                        hsl1 += cs1;
                        hsl3 += cs3;
                        y2 += Canvas2D.width;
                    }

                    return;
                }

                y1 -= y3;
                y3 -= y2;

                for (y2 = pixels[y2]; --y3 >= 0; y2 += Canvas2D.width) {
                    draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
                    x1 += s1;
                    x2 += s2;
                    hsl1 += cs1;
                    hsl2 += cs2;
                }

                while (--y1 >= 0) {
                    draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
                    x1 += s1;
                    x3 += s3;
                    hsl1 += cs1;
                    hsl3 += cs3;
                    y2 += Canvas2D.width;
                }
                return;
            }

            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 15;

            if (y2 < 0) {
                x3 -= s1 * y2;
                x2 -= s2 * y2;
                hsl3 -= cs1 * y2;
                hsl2 -= cs2 * y2;
                y2 = 0;
            }

            x1 <<= 16;
            hsl1 <<= 15;

            if (y1 < 0) {
                x1 -= s3 * y1;
                hsl1 -= cs3 * y1;
                y1 = 0;
            }

            if (s1 < s2) {
                y3 -= y1;
                y1 -= y2;

                for (y2 = pixels[y2]; --y1 >= 0; y2 += Canvas2D.width) {
                    draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
                    x3 += s1;
                    x2 += s2;
                    hsl3 += cs1;
                    hsl2 += cs2;
                }

                while (--y3 >= 0) {
                    draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
                    x1 += s3;
                    x2 += s2;
                    hsl1 += cs3;
                    hsl2 += cs2;
                    y2 += Canvas2D.width;
                }
                return;
            }

            y3 -= y1;
            y1 -= y2;

            for (y2 = pixels[y2]; --y1 >= 0; y2 += Canvas2D.width) {
                draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
                x3 += s1;
                x2 += s2;
                hsl3 += cs1;
                hsl2 += cs2;
            }

            while (--y3 >= 0) {
                draw_gradient_scanline(Canvas2D.pixels, y2, 0, 0, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
                x1 += s3;
                x2 += s2;
                hsl1 += cs3;
                hsl2 += cs2;
                y2 += Canvas2D.width;
            }
            return;
        }

        if (y3 >= Canvas2D.right_y) {
            return;
        }

        if (y1 > Canvas2D.right_y) {
            y1 = Canvas2D.right_y;
        }

        if (y2 > Canvas2D.right_y) {
            y2 = Canvas2D.right_y;
        }

        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 15;

            if (y3 < 0) {
                x2 -= s2 * y3;
                x3 -= s3 * y3;
                hsl2 -= cs2 * y3;
                hsl3 -= cs3 * y3;
                y3 = 0;
            }

            x1 <<= 16;
            hsl1 <<= 15;

            if (y1 < 0) {
                x1 -= s1 * y1;
                hsl1 -= cs1 * y1;
                y1 = 0;
            }

            if (s2 < s3) {
                y2 -= y1;
                y1 -= y3;

                for (y3 = pixels[y3]; --y1 >= 0; y3 += Canvas2D.width) {
                    draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
                    x2 += s2;
                    x3 += s3;
                    hsl2 += cs2;
                    hsl3 += cs3;
                }

                while (--y2 >= 0) {
                    draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7);
                    x2 += s2;
                    x1 += s1;
                    hsl2 += cs2;
                    hsl1 += cs1;
                    y3 += Canvas2D.width;
                }
                return;
            }

            y2 -= y1;
            y1 -= y3;

            for (y3 = pixels[y3]; --y1 >= 0; y3 += Canvas2D.width) {
                draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
                x2 += s2;
                x3 += s3;
                hsl2 += cs2;
                hsl3 += cs3;
            }

            while (--y2 >= 0) {
                draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7);
                x2 += s2;
                x1 += s1;
                hsl2 += cs2;
                hsl1 += cs1;
                y3 += Canvas2D.width;
            }
            return;
        }

        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 15;

        if (y3 < 0) {
            x1 -= s2 * y3;
            x3 -= s3 * y3;
            hsl1 -= cs2 * y3;
            hsl3 -= cs3 * y3;
            y3 = 0;
        }

        x2 <<= 16;
        hsl2 <<= 15;

        if (y2 < 0) {
            x2 -= s1 * y2;
            hsl2 -= cs1 * y2;
            y2 = 0;
        }

        if (s2 < s3) {
            y1 -= y2;
            y2 -= y3;

            for (y3 = pixels[y3]; --y2 >= 0; y3 += Canvas2D.width) {
                draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7);
                x1 += s2;
                x3 += s3;
                hsl1 += cs2;
                hsl3 += cs3;
            }

            while (--y1 >= 0) {
                draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7);
                x2 += s1;
                x3 += s3;
                hsl2 += cs1;
                hsl3 += cs3;
                y3 += Canvas2D.width;
            }
            return;
        }

        y1 -= y2;
        y2 -= y3;

        for (y3 = pixels[y3]; --y2 >= 0; y3 += Canvas2D.width) {
            draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7);
            x1 += s2;
            x3 += s3;
            hsl1 += cs2;
            hsl3 += cs3;
        }

        while (--y1 >= 0) {
            draw_gradient_scanline(Canvas2D.pixels, y3, 0, 0, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7);
            x2 += s1;
            x3 += s3;
            hsl2 += cs1;
            hsl3 += cs3;
            y3 += Canvas2D.width;
        }
    }

    public static void draw_texture_scanline(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3) {
        if (l >= i1) {
            return;
        }
        int j3;
        int k3;
        if (check_bounds) {
            j3 = (k1 - j1) / (i1 - l);
            if (i1 > Canvas2D.bound) {
                i1 = Canvas2D.bound;
            }
            if (l < 0) {
                j1 -= l * j3;
                l = 0;
            }
            if (l >= i1) {
                return;
            }
            k3 = i1 - l >> 3;
            j3 <<= 12;
            j1 <<= 9;
        } else {
            if (i1 - l > 7) {
                k3 = i1 - l >> 3;
                j3 = (k1 - j1) * shadow_decay[k3] >> 6;
            } else {
                k3 = 0;
                j3 = 0;
            }
            j1 <<= 9;
        }
        k += l;
        if (Game.low_detail) {
            int i4 = 0;
            int k4 = 0;
            int k6 = l - center_x;
            l1 += (k2 >> 3) * k6;
            i2 += (l2 >> 3) * k6;
            j2 += (i3 >> 3) * k6;
            int i5 = j2 >> 12;
            if (i5 != 0) {
                i = l1 / i5;
                j = i2 / i5;
                if (i < 0) {
                    i = 0;
                } else if (i > 4032) {
                    i = 4032;
                }
            }
            l1 += k2;
            i2 += l2;
            j2 += i3;
            i5 = j2 >> 12;
            if (i5 != 0) {
                i4 = l1 / i5;
                k4 = i2 / i5;
                if (i4 < 7) {
                    i4 = 7;
                } else if (i4 > 4032) {
                    i4 = 4032;
                }
            }
            int i7 = i4 - i >> 3;
            int k7 = k4 - j >> 3;
            i += (j1 & 0x600000) >> 3;
            int i8 = j1 >> 23;
            if (opaque) {
                while (k3-- > 0) {
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i += i7;
                    j += k7;
                    ai[k++] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    i = i4;
                    j = k4;
                    l1 += k2;
                    i2 += l2;
                    j2 += i3;
                    int j5 = j2 >> 12;
                    if (j5 != 0) {
                        i4 = l1 / j5;
                        k4 = i2 / j5;
                        if (i4 < 7) {
                            i4 = 7;
                        } else if (i4 > 4032) {
                            i4 = 4032;
                        }
                    }
                    i7 = i4 - i >> 3;
                    k7 = k4 - j >> 3;
                    j1 += j3;
                    i += (j1 & 0x600000) >> 3;
                    i8 = j1 >> 23;
                }
                for (k3 = i1 - l & 7; k3-- > 0; ) {
                    ai[k] = ai1[(j & 0xfc0) + (i >> 6)] >>> i8;
                    k++;
                    i += i7;
                    j += k7;
                }

                return;
            }
            while (k3-- > 0) {
                int k8;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i += i7;
                j += k7;
                if ((k8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = k8;
                }
                k++;
                i = i4;
                j = k4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                int k5 = j2 >> 12;
                if (k5 != 0) {
                    i4 = l1 / k5;
                    k4 = i2 / k5;
                    if (i4 < 7) {
                        i4 = 7;
                    } else if (i4 > 4032) {
                        i4 = 4032;
                    }
                }
                i7 = i4 - i >> 3;
                k7 = k4 - j >> 3;
                j1 += j3;
                i += (j1 & 0x600000) >> 3;
                i8 = j1 >> 23;
            }
            for (k3 = i1 - l & 7; k3-- > 0; ) {
                int l8;
                if ((l8 = ai1[(j & 0xfc0) + (i >> 6)] >>> i8) != 0) {
                    ai[k] = l8;
                }
                k++;
                i += i7;
                j += k7;
            }

            return;
        }
        int j4 = 0;
        int l4 = 0;
        int l6 = l - center_x;
        l1 += (k2 >> 3) * l6;
        i2 += (l2 >> 3) * l6;
        j2 += (i3 >> 3) * l6;
        int l5 = j2 >> 14;
        if (l5 != 0) {
            i = l1 / l5;
            j = i2 / l5;
            if (i < 0) {
                i = 0;
            } else if (i > 16256) {
                i = 16256;
            }
        }
        l1 += k2;
        i2 += l2;
        j2 += i3;
        l5 = j2 >> 14;
        if (l5 != 0) {
            j4 = l1 / l5;
            l4 = i2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - i >> 3;
        int l7 = l4 - j >> 3;
        i += j1 & 0x600000;
        int j8 = j1 >> 23;
        if (opaque) {
            while (k3-- > 0) {
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i = j4;
                j = l4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                int i6 = j2 >> 14;
                if (i6 != 0) {
                    j4 = l1 / i6;
                    l4 = i2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                j7 = j4 - i >> 3;
                l7 = l4 - j >> 3;
                j1 += j3;
                i += j1 & 0x600000;
                j8 = j1 >> 23;
            }
            for (k3 = i1 - l & 7; k3-- > 0; ) {
                ai[k++] = ai1[(j & 0x3f80) + (i >> 7)] >>> j8;
                i += j7;
                j += l7;
            }

            return;
        }
        while (k3-- > 0) {
            int i9;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i += j7;
            j += l7;
            if ((i9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = i9;
            }
            k++;
            i = j4;
            j = l4;
            l1 += k2;
            i2 += l2;
            j2 += i3;
            int j6 = j2 >> 14;
            if (j6 != 0) {
                j4 = l1 / j6;
                l4 = i2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - i >> 3;
            l7 = l4 - j >> 3;
            j1 += j3;
            i += j1 & 0x600000;
            j8 = j1 >> 23;
        }
        for (int l3 = i1 - l & 7; l3-- > 0; ) {
            int j9;
            if ((j9 = ai1[(j & 0x3f80) + (i >> 7)] >>> j8) != 0) {
                ai[k] = j9;
            }
            k++;
            i += j7;
            j += l7;
        }

    }

    public static void draw_textured_triangle(int x1, int y1, int x2, int y2, int x3, int y3, int hsl1, int hsl2, int hsl3, int sx1, int sy1, int sz1, int sx2, int sy2, int sz2, int sx3, int sy3, int sz3, int texture) {
        int[] texels = get_texture_pixels(texture);
        opaque = !texture_translucent[texture];
        sx2 = sx1 - sx2;
        sy2 = sy1 - sy2;
        sz2 = sz1 - sz2;
        sx3 -= sx1;
        sy3 -= sy1;
        sz3 -= sz1;
        int l4 = sx3 * sy1 - sy3 * sx1 << 14;
        int i5 = sy3 * sz1 - sz3 * sy1 << 8;
        int j5 = sz3 * sx1 - sx3 * sz1 << 5;
        int k5 = sx2 * sy1 - sy2 * sx1 << 14;
        int l5 = sy2 * sz1 - sz2 * sy1 << 8;
        int i6 = sz2 * sx1 - sx2 * sz1 << 5;
        int j6 = sy2 * sx3 - sx2 * sy3 << 14;
        int k6 = sz2 * sy3 - sy2 * sz3 << 8;
        int l6 = sx2 * sz3 - sz2 * sx3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (hsl2 - hsl1 << 16) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (hsl3 - hsl2 << 16) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (hsl1 - hsl3 << 16) / (y1 - y3);
        }
        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Canvas2D.right_y) {
                return;
            }
            if (y2 > Canvas2D.right_y) {
                y2 = Canvas2D.right_y;
            }
            if (y3 > Canvas2D.right_y) {
                y3 = Canvas2D.right_y;
            }
            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 16;
                if (y1 < 0) {
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    hsl3 -= j8 * y1;
                    hsl1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                hsl2 <<= 16;
                if (y2 < 0) {
                    x2 -= k7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - center_y;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = pixels[y1];
                    while (--y2 >= 0) {
                        draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x3 >> 16, x1 >> 16, hsl3 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                        x3 += i8;
                        x1 += i7;
                        hsl3 += j8;
                        hsl1 += j7;
                        y1 += Canvas2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x3 >> 16, x2 >> 16, hsl3 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                        x3 += i8;
                        x2 += k7;
                        hsl3 += j8;
                        hsl2 += l7;
                        y1 += Canvas2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = pixels[y1];
                while (--y2 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x1 >> 16, x3 >> 16, hsl1 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                    x3 += i8;
                    x1 += i7;
                    hsl3 += j8;
                    hsl1 += j7;
                    y1 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x2 >> 16, x3 >> 16, hsl2 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                    x3 += i8;
                    x2 += k7;
                    hsl3 += j8;
                    hsl2 += l7;
                    y1 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 16;
            if (y1 < 0) {
                x2 -= i8 * y1;
                x1 -= i7 * y1;
                hsl2 -= j8 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            hsl3 <<= 16;
            if (y3 < 0) {
                x3 -= k7 * y3;
                hsl3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - center_y;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = pixels[y1];
                while (--y3 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x2 >> 16, x1 >> 16, hsl2 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                    x2 += i8;
                    x1 += i7;
                    hsl2 += j8;
                    hsl1 += j7;
                    y1 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x3 >> 16, x1 >> 16, hsl3 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                    x3 += k7;
                    x1 += i7;
                    hsl3 += l7;
                    hsl1 += j7;
                    y1 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = pixels[y1];
            while (--y3 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x1 >> 16, x2 >> 16, hsl1 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                x2 += i8;
                x1 += i7;
                hsl2 += j8;
                hsl1 += j7;
                y1 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y1, x1 >> 16, x3 >> 16, hsl1 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                x3 += k7;
                x1 += i7;
                hsl3 += l7;
                hsl1 += j7;
                y1 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Canvas2D.right_y) {
                return;
            }
            if (y3 > Canvas2D.right_y) {
                y3 = Canvas2D.right_y;
            }
            if (y1 > Canvas2D.right_y) {
                y1 = Canvas2D.right_y;
            }
            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 16;
                if (y2 < 0) {
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    hsl1 -= j7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                hsl3 <<= 16;
                if (y3 < 0) {
                    x3 -= i8 * y3;
                    hsl3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - center_y;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = pixels[y2];
                    while (--y3 >= 0) {
                        draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x1 >> 16, x2 >> 16, hsl1 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                        x1 += i7;
                        x2 += k7;
                        hsl1 += j7;
                        hsl2 += l7;
                        y2 += Canvas2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x1 >> 16, x3 >> 16, hsl1 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                        x1 += i7;
                        x3 += i8;
                        hsl1 += j7;
                        hsl3 += j8;
                        y2 += Canvas2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = pixels[y2];
                while (--y3 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x2 >> 16, x1 >> 16, hsl2 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                    x1 += i7;
                    x2 += k7;
                    hsl1 += j7;
                    hsl2 += l7;
                    y2 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x3 >> 16, x1 >> 16, hsl3 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                    x1 += i7;
                    x3 += i8;
                    hsl1 += j7;
                    hsl3 += j8;
                    y2 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 16;
            if (y2 < 0) {
                x3 -= i7 * y2;
                x2 -= k7 * y2;
                hsl3 -= j7 * y2;
                hsl2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 16;
            if (y1 < 0) {
                x1 -= i8 * y1;
                hsl1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - center_y;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = pixels[y2];
                while (--y1 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x3 >> 16, x2 >> 16, hsl3 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                    x3 += i7;
                    x2 += k7;
                    hsl3 += j7;
                    hsl2 += l7;
                    y2 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x1 >> 16, x2 >> 16, hsl1 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                    x1 += i8;
                    x2 += k7;
                    hsl1 += j8;
                    hsl2 += l7;
                    y2 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = pixels[y2];
            while (--y1 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x2 >> 16, x3 >> 16, hsl2 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                x3 += i7;
                x2 += k7;
                hsl3 += j7;
                hsl2 += l7;
                y2 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y2, x2 >> 16, x1 >> 16, hsl2 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                x1 += i8;
                x2 += k7;
                hsl1 += j8;
                hsl2 += l7;
                y2 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y3 >= Canvas2D.right_y) {
            return;
        }
        if (y1 > Canvas2D.right_y) {
            y1 = Canvas2D.right_y;
        }
        if (y2 > Canvas2D.right_y) {
            y2 = Canvas2D.right_y;
        }
        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 16;
            if (y3 < 0) {
                x2 -= k7 * y3;
                x3 -= i8 * y3;
                hsl2 -= l7 * y3;
                hsl3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 16;
            if (y1 < 0) {
                x1 -= i7 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            int k9 = y3 - center_y;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = pixels[y3];
                while (--y1 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x2 >> 16, x3 >> 16, hsl2 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                    x2 += k7;
                    x3 += i8;
                    hsl2 += l7;
                    hsl3 += j8;
                    y3 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x2 >> 16, x1 >> 16, hsl2 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
                    x2 += k7;
                    x1 += i7;
                    hsl2 += l7;
                    hsl1 += j7;
                    y3 += Canvas2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = pixels[y3];
            while (--y1 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x3 >> 16, x2 >> 16, hsl3 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                x2 += k7;
                x3 += i8;
                hsl2 += l7;
                hsl3 += j8;
                y3 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x1 >> 16, x2 >> 16, hsl1 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
                x2 += k7;
                x1 += i7;
                hsl2 += l7;
                hsl1 += j7;
                y3 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 16;
        if (y3 < 0) {
            x1 -= k7 * y3;
            x3 -= i8 * y3;
            hsl1 -= l7 * y3;
            hsl3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        hsl2 <<= 16;
        if (y2 < 0) {
            x2 -= i7 * y2;
            hsl2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - center_y;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = pixels[y3];
            while (--y2 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x1 >> 16, x3 >> 16, hsl1 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                x1 += k7;
                x3 += i8;
                hsl1 += l7;
                hsl3 += j8;
                y3 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x2 >> 16, x3 >> 16, hsl2 >> 8, hsl3 >> 8, l4, k5, j6, i5, l5, k6);
                x2 += i7;
                x3 += i8;
                hsl2 += j7;
                hsl3 += j8;
                y3 += Canvas2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = pixels[y3];
        while (--y2 >= 0) {
            draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x3 >> 16, x1 >> 16, hsl3 >> 8, hsl1 >> 8, l4, k5, j6, i5, l5, k6);
            x1 += k7;
            x3 += i8;
            hsl1 += l7;
            hsl3 += j8;
            y3 += Canvas2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            draw_texture_scanline(Canvas2D.pixels, texels, 0, 0, y3, x3 >> 16, x2 >> 16, hsl3 >> 8, hsl2 >> 8, l4, k5, j6, i5, l5, k6);
            x2 += i7;
            x3 += i8;
            hsl2 += j7;
            hsl3 += j8;
            y3 += Canvas2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
    }

    public static int get_average_texture_rgb(int texture) {
        if (texture_average_rgb[texture] != 0) {
            return texture_average_rgb[texture];
        }

        int red = 0;
        int green = 0;
        int blue = 0;
        int count = texture_palette[texture].length;

        for (int i = 0; i < count; i++) {
            red += texture_palette[texture][i] >> 16 & 0xff;
            green += texture_palette[texture][i] >> 8 & 0xff;
            blue += texture_palette[texture][i] & 0xff;
        }

        int rgb = linear_rgb_brightness(((red / count) << 16) + ((green / count) << 8) + (blue / count), 1.3999999999999999D);

        if (rgb == 0) {
            rgb = 1;
        }

        texture_average_rgb[texture] = rgb;
        return rgb;
    }

    public static int[] get_texture_pixels(int index) {
        texture_cycle[index] = cycle++;

        if (texel_cache[index] != null) {
            return texel_cache[index];
        }

        int[] texels;

        if (texel_pointer > 0) {
            texels = texel_pool[--texel_pointer];
            texel_pool[texel_pointer] = null;
        } else {
            int cycle = 0;
            int texture_index = -1;
            for (int i = 0; i < texture_count; i++)
                if (texel_cache[i] != null && (texture_cycle[i] < cycle || texture_index == -1)) {
                    cycle = texture_cycle[i];
                    texture_index = i;
                }

            texels = texel_cache[texture_index];
            texel_cache[texture_index] = null;
        }

        texel_cache[index] = texels;
        Bitmap b = texture[index];
        int[] palette = texture_palette[index];

        if (Game.low_detail) {
            texture_translucent[index] = false;
            for (int i = 0; i < 4096; i++) {
                int texel = texels[i] = palette[b.pixels[i]] & 0xf8f8ff;

                if (texel == 0) {
                    texture_translucent[index] = true;
                }

                texels[4096 + i] = texel - (texel >>> 3) & 0xf8f8ff;
                texels[8192 + i] = texel - (texel >>> 2) & 0xf8f8ff;
                texels[12288 + i] = texel - (texel >>> 2) - (texel >>> 3) & 0xf8f8ff;
            }

        } else {
            if (b.width == 64) {
                for (int y = 0; y < 128; y++) {
                    for (int x = 0; x < 128; x++) {
                        texels[x + (y << 7)] = palette[b.pixels[(x >> 1) + ((y >> 1) << 6)]];
                    }
                }
            } else {
                for (int i = 0; i < 16384; i++) {
                    texels[i] = palette[b.pixels[i]];
                }
            }

            texture_translucent[index] = false;

            for (int i = 0; i < 16384; i++) {
                texels[i] &= 0xf8f8ff;

                int texel = texels[i];

                if (texel == 0) {
                    texture_translucent[index] = true;
                }

                texels[16384 + i] = texel - (texel >>> 3) & 0xf8f8ff;
                texels[32768 + i] = texel - (texel >>> 2) & 0xf8f8ff;
                texels[49152 + i] = texel - (texel >>> 2) - (texel >>> 3) & 0xf8f8ff;
            }
        }
        return texels;
    }

    public static int linear_rgb_brightness(int rgb, double brightness) {
        double r = Math.pow((double) (rgb >> 16) / 256D, brightness);
        double g = Math.pow((double) (rgb >> 8 & 0xff) / 256D, brightness);
        double b = Math.pow((double) (rgb & 0xff) / 256D, brightness);
        return ((int) (r * 256D) << 16) + ((int) (g * 256D) << 8) + (int) (b * 256D);
    }

    public static void nullify() {
        shadow_decay = null;
        sin = null;
        cos = null;
        pixels = null;
        texture = null;
        texture_translucent = null;
        texture_average_rgb = null;
        texel_pool = null;
        texel_cache = null;
        texture_cycle = null;
        palette = null;
        texture_palette = null;
    }

    public static void prepare() {
        pixels = new int[Canvas2D.height];

        for (int i = 0; i < Canvas2D.height; i++) {
            pixels[i] = Canvas2D.width * i;
        }

        center_x = Canvas2D.width / 2;
        center_y = Canvas2D.height / 2;
    }

    public static void prepare(int width, int height) {
        pixels = new int[height];

        for (int i = 0; i < height; i++) {
            pixels[i] = width * i;
        }

        center_x = width / 2;
        center_y = height / 2;
    }

    public static void setup_texel_pools() {
        if (texel_pool == null) {
            texel_pointer = 20;

            if (Game.low_detail) {
                texel_pool = new int[texel_pointer][16384];
            } else {
                texel_pool = new int[texel_pointer][0x10000];
            }

            for (int k = 0; k < 50; k++) {
                texel_cache[k] = null;
            }
        }
    }

    public static void unpack_textures(Archive archive) {
        texture_count = 0;
        for (int i = 0; i < 50; i++) {
            try {
                texture[i] = new Bitmap(archive, String.valueOf(i), 0);

                if (Game.WINTER) {
                    boolean snow = false;

                    if (i == 6 || i == 8 || i == 21 || i == 27 || i == 30 || i == 33 || i == 41 || i == 44 || i == 46) {
                        snow = true;
                    }

                    if (snow) {
                        Bitmap texture = Canvas3D.texture[i];

                        int[] palette = Arrays.copyOf(texture.palette, texture.palette.length);
                        texture.palette = new int[palette.length + 1];
                        System.arraycopy(palette, 0, texture.palette, 0, palette.length);
                        texture.palette[texture.palette.length - 1] = 0xCCCCCC;
                        byte color = (byte) (texture.palette.length - 1);

                        for (int x = 0; x < texture.width; x++) {
                            for (int y = 0; y < texture.height; y++) {
                                int pos = (x + ((y * texture.width)));
                                if (texture.pixels[pos] == 0) {
                                    continue;
                                }
                                if (random.nextInt(128) == 1) {
                                    if (x > 5 && x < texture.width - 5 && y > 5 && y < texture.height - 5) {
                                        for (int j = 1; j < 5; j++) {
                                            texture.pixels[(x + ((y - j) * texture.width))] = color;
                                            texture.pixels[(x + ((y + j) * texture.width))] = color;
                                            texture.pixels[((x - j) + (y * texture.width))] = color;
                                            texture.pixels[((x + j) + (y * texture.width))] = color;
                                            texture.pixels[((x + j) + ((y - j) * texture.width))] = color;
                                            texture.pixels[((x + j) + ((y + j) * texture.width))] = color;
                                            texture.pixels[((x - j) + ((y + j) * texture.width))] = color;
                                            texture.pixels[((x - j) + ((y - j) * texture.width))] = color;
                                        }
                                    }
                                    texture.pixels[pos] = color;
                                }
                            }
                        }
                    }
                }

                if (Game.low_detail && texture[i].crop_width == 128) {
                    texture[i].shrink();
                } else {
                    texture[i].crop();
                }

                texture_count++;
            } catch (Exception _ex) {
            }
        }
    }

    public static void update_texture(int i) {
        if (texel_cache[i] == null)
            return;
        texel_pool[texel_pointer++] = texel_cache[i];
        texel_cache[i] = null;
    }
}
