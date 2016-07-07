package rs.media;

import rs.node.CacheLink;

import java.util.Arrays;

public class Canvas2D extends CacheLink {

    public static enum DrawType {
        IGNORE_BLACK, RGB, RGBA, TRANSLUCENT_IGNORE_BLACK
    }

    public static int alpha;
    public static int width;
    public static int height;
    public static int center_y;
    public static int center_x;
    public static int bound;
    public static int left_x;
    public static int left_y;
    public static int right_x;
    public static int right_y;
    public static int pixels[];

    /**
     * Sets all the pixels to black.
     */
    public static void clear() {
        Arrays.fill(Canvas2D.pixels, 0);
    }

    /**
     * Draws the color using a mask.
     *
     * @param mask     the mask.
     * @param maskOff  the mask offset.
     * @param destOff  the destination offset.
     * @param width    the image width.
     * @param height   the image height.
     * @param destStep the destination step.
     * @param maskStep the mask step.
     * @param color    the color.
     */
    public static void draw(byte mask[], int maskOff, int destOff, int width, int height, int destStep, int maskStep, int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (mask[maskOff++] != 0) {
                    Canvas2D.pixels[destOff++] = color;
                } else {
                    destOff++;
                }
            }
            destOff += destStep;
            maskOff += maskStep;
        }
    }

    /**
     * Draws the color using a mask. (Transparency)
     *
     * @param mask     the mask.
     * @param maskOff  the mask offset.
     * @param destOff  the destination offset.
     * @param width    the image width.
     * @param height   the image height.
     * @param destStep the destination step.
     * @param maskStep the mask step.
     * @param color    the color.
     * @param opacity  the opacity of the color.
     */
    public static void draw(byte mask[], int maskOff, int destOff, int width, int height, int destStep, int maskStep, int color, int opacity) {
        color = ((color & 0xff00ff) * opacity & 0xff00ff00) + ((color & 0xff00) * opacity & 0xff0000) >> 8;
        opacity = 256 - opacity;
        for (int y = -height; y < 0; y++) {
            for (int x = -width; x < 0; x++) {
                if (mask[maskOff++] != 0) {
                    int rgb = Canvas2D.pixels[destOff];
                    Canvas2D.pixels[destOff++] = (((rgb & 0xff00ff) * opacity & 0xff00ff00) + ((rgb & 0xff00) * opacity & 0xff0000) >> 8) + color;
                } else {
                    destOff++;
                }
            }
            destOff += destStep;
            maskOff += maskStep;
        }
    }

    /**
     * Draws the image using the provided palette.
     *
     * @param src      the pixels being written.
     * @param palette  the palette.
     * @param srcOff   the source offset.
     * @param destOff  the destination offset.
     * @param width    the image width.
     * @param height   the image height.
     * @param destStep the destination step.
     * @param srcStep  the source step.
     */
    public static void draw(byte[] src, int[] palette, int srcOff, int destOff, int width, int height, int destStep, int srcStep) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte color = src[srcOff++];
                if (color != 0) {
                    Canvas2D.pixels[destOff++] = palette[color & 0xFF];
                } else {
                    destOff++;
                }
            }

            destOff += destStep;
            srcOff += srcStep;
        }
    }

    /**
     * Draws the image to the raster using a mask.
     *
     * @param src      the pixels being written.
     * @param mask     the mask used to decide which pixels to write.
     * @param srcOff   the source offset.
     * @param maskOff  the mask offset.
     * @param width    the image width.
     * @param height   the image height.
     * @param destStep the destination step.
     * @param srcStep  the source step.
     */
    public static void draw(int src[], byte mask[], int srcOff, int maskOff, int width, int height, int destStep, int srcStep) {
        int color = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                color = src[srcOff++];
                if (color != 0 && mask[maskOff] == 0) {
                    Canvas2D.pixels[maskOff++] = color;
                } else {
                    maskOff++;
                }
            }
            maskOff += destStep;
            srcOff += srcStep;
        }
    }

    /**
     * Draws the image to the raster with the provided options.
     *
     * @param src      the pixels being written.
     * @param srcOff   the source offset.
     * @param destOff  the destination offset.
     * @param width    the image width.
     * @param height   the image height.
     * @param destStep the destination step.
     * @param srcStep  the source step.
     * @param type     the method used for writing the image to the raster.
     */
    public static void draw(int src[], int srcOff, int destOff, int width, int height, int destStep, int srcStep, DrawType type) {
        int rgb = 0;
        switch (type) {
            case RGB: {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Canvas2D.pixels[destOff++] = src[srcOff++];
                    }
                    destOff += destStep;
                    srcOff += srcStep;
                }
                break;
            }
            case TRANSLUCENT_IGNORE_BLACK: {
                int a = 256 - alpha;
                for (int y = -height; y < 0; y++) {
                    for (int x = -width; x < 0; x++) {
                        rgb = src[srcOff++];
                        if (rgb != 0) {
                            int originalRGB = Canvas2D.pixels[destOff];
                            Canvas2D.pixels[destOff++] = ((rgb & 0xff00ff) * alpha + (originalRGB & 0xff00ff) * a & 0xff00ff00) + ((rgb & 0xff00) * alpha + (originalRGB & 0xff00) * a & 0xff0000) >> 8;
                        } else {
                            destOff++;
                        }
                    }
                    destOff += destStep;
                    srcOff += srcStep;
                }
                break;
            }
            case IGNORE_BLACK: {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        rgb = src[srcOff++];
                        if (rgb != 0) {
                            Canvas2D.pixels[destOff++] = rgb;
                        } else {
                            destOff++;
                        }
                    }

                    destOff += destStep;
                    srcOff += srcStep;
                }
                break;
            }
            default: {
            }
        }
    }

    /**
     * Draws a line from point1 to point2.
     *
     * @param x0    the start x.
     * @param y0    the start y.
     * @param x1    the end x.
     * @param y1    the end y.
     * @param color the color.
     */
    public static void draw_line(int x0, int y0, int x1, int y1, int color) {
        if (x0 < 0) {
            x0 = 0;
        }
        if (y0 < 0) {
            y0 = 0;
        }
        if (x1 > Canvas2D.width) {
            x1 = Canvas2D.width;
        }
        if (y1 > Canvas2D.height) {
            y1 = Canvas2D.height;
        }
        int x_difference = Math.abs(x1 - x0);
        int y_difference = Math.abs(y1 - y0);
        int x_delta = (x0 < x1 ? 1 : -1);
        int y_delta = (y0 < y1 ? 1 : -1);
        int slope = x_difference - y_difference;
        int err2 = 0;

        for (; ; ) {
            Canvas2D.plot(x0, y0, color);

            if (x0 == x1 && y0 == y1) {
                break;
            }

            err2 = 2 * slope;

            if (err2 > -y_difference) {
                slope -= y_difference;
                x0 += x_delta;
            }

            if (x0 == x1 && y0 == y1) {
                Canvas2D.plot(x0, y0, color);
                break;
            }

            if (err2 < x_difference) {
                slope += x_difference;
                y0 += y_delta;
            }
        }
    }

    /**
     * Draws a horizontal line with the specified color.
     *
     * @param x   the x.
     * @param y   the y.
     * @param len the length.
     * @param rgb the color.
     */
    public static void draw_line_h(int x, int y, int len, int rgb) {
        if (y < Canvas2D.left_y || y >= Canvas2D.right_y) {
            return;
        }
        if (x < Canvas2D.left_x) {
            len -= Canvas2D.left_x - x;
            x = Canvas2D.left_x;
        }
        if (x + len > Canvas2D.right_x) {
            len = Canvas2D.right_x - x;
        }
        int pos = x + y * Canvas2D.width;
        for (int i = 0; i < len; i++) {
            Canvas2D.pixels[pos + i] = rgb;
        }
    }

    /**
     * Draws a horizontal line with the specified color and opacity.
     *
     * @param x       the x.
     * @param y       the y.
     * @param length  the length.
     * @param color   the color.
     * @param opacity the opacity.
     */
    public static void draw_line_h(int x, int y, int length, int color, int opacity) {
        if (y < Canvas2D.left_y || y >= Canvas2D.right_y) {
            return;
        }
        if (x < Canvas2D.left_x) {
            length -= Canvas2D.left_x - x;
            x = Canvas2D.left_x;
        }
        if (x + length > Canvas2D.right_x) {
            length = Canvas2D.right_x - x;
        }
        int alpha = 256 - opacity;
        int red = (color >> 16 & 0xff) * opacity;
        int green = (color >> 8 & 0xff) * opacity;
        int blue = (color & 0xff) * opacity;
        int position = x + y * width;
        for (int i = 0; i < length; i++) {
            Canvas2D.pixels[position] = Canvas2D.mix(red, green, blue, pixels[position++], alpha);
        }

    }

    /**
     * Draws a vertical line with the specified color.
     *
     * @param x      the x.
     * @param y      the y.
     * @param length the length.
     * @param rgb    the color.
     */
    public static void draw_line_v(int x, int y, int length, int rgb) {
        if (x < Canvas2D.left_x || x >= Canvas2D.right_x) {
            return;
        }
        if (y < Canvas2D.left_y) {
            length -= Canvas2D.left_y - y;
            y = Canvas2D.left_y;
        }
        if (y + length > Canvas2D.right_y) {
            length = Canvas2D.right_y - y;
        }
        int position = x + y * Canvas2D.width;
        for (int i = 0; i < length; i++) {
            Canvas2D.pixels[position + i * Canvas2D.width] = rgb;
        }
    }

    /**
     * Draws a vertical line with the specified color and opacity.
     *
     * @param x       the x.
     * @param y       the y.
     * @param length  the length.
     * @param color   the color.
     * @param opacity the opacity.
     */
    public static void draw_line_v(int x, int y, int length, int color, int opacity) {
        if (x < left_x || x >= right_x) {
            return;
        }
        if (y < left_y) {
            length -= left_y - y;
            y = left_y;
        }
        if (y + length > right_y) {
            length = right_y - y;
        }
        int alpha = 256 - opacity;
        int red = (color >> 16 & 0xff) * opacity;
        int green = (color >> 8 & 0xff) * opacity;
        int blue = (color & 0xff) * opacity;
        int position = x + y * width;
        for (int i = 0; i < length; i++) {
            Canvas2D.pixels[position] = Canvas2D.mix(red, green, blue, pixels[position], alpha);
            position += Canvas2D.width;
        }
    }

    /**
     * Draws a rectangle with the specified color.
     *
     * @param x      the x.
     * @param y      the y.
     * @param width  the width.
     * @param height the height.
     * @param color  the color.
     */
    public static void draw_rect(int x, int y, int width, int height, int color) {
        draw_line_h(x, y, width, color);
        draw_line_h(x, (y + height) - 1, width, color);
        draw_line_v(x, y, height, color);
        draw_line_v((x + width) - 1, y, height, color);
    }

    /**
     * Draws a rectangle with the specified color and opacity.
     *
     * @param x       the x.
     * @param y       the y.
     * @param width   the width.
     * @param height  the height.
     * @param color   the color.
     * @param opacity the opacity.
     */
    public static void draw_rect(int x, int y, int width, int height, int color, int opacity) {
        draw_line_h(x, y, width, color, opacity);
        draw_line_h(x, (y + height) - 1, width, color, opacity);
        if (height >= 3) {
            draw_line_v(x, y + 1, height - 2, color, opacity);
            draw_line_v((x + width) - 1, y + 1, height - 2, color, opacity);
        }
    }

    /**
     * Fills a rectangle with the specified color.
     *
     * @param x      the x.
     * @param y      the y.
     * @param width  the width.
     * @param height the y.
     * @param color  the color.
     */
    public static void fill_rect(int x, int y, int width, int height, int color) {
        if (x < Canvas2D.left_x) {
            width -= Canvas2D.left_x - x;
            x = Canvas2D.left_x;
        }
        if (y < Canvas2D.left_y) {
            height -= Canvas2D.left_y - y;
            y = Canvas2D.left_y;
        }
        if (x + width > Canvas2D.right_x) {
            width = Canvas2D.right_x - x;
        }
        if (y + height > Canvas2D.right_y) {
            height = Canvas2D.right_y - y;
        }
        int step = Canvas2D.width - width;
        int position = x + y * Canvas2D.width;
        for (int i = -height; i < 0; i++) {
            for (int j = -width; j < 0; j++) {
                Canvas2D.pixels[position++] = color;
            }
            position += step;
        }
    }

    /**
     * Fills a rectangle with the specified color and opacity.
     *
     * @param x       the x.
     * @param y       the y.
     * @param width   the width.
     * @param height  the height.
     * @param color   the color.
     * @param opacity the opacity.
     */
    public static void fill_rect(int x, int y, int width, int height, int color, int opacity) {
        if (x < Canvas2D.left_x) {
            width -= Canvas2D.left_x - x;
            x = Canvas2D.left_x;
        }
        if (y < Canvas2D.left_y) {
            height -= Canvas2D.left_y - y;
            y = Canvas2D.left_y;
        }
        if (x + width > Canvas2D.right_x) {
            width = Canvas2D.right_x - x;
        }
        if (y + height > Canvas2D.right_y) {
            height = Canvas2D.right_y - y;
        }
        int alpha = 256 - opacity;
        int red = (color >> 16 & 0xff) * opacity;
        int green = (color >> 8 & 0xff) * opacity;
        int blue = (color & 0xff) * opacity;
        int step = Canvas2D.width - width;
        int position = x + y * Canvas2D.width;
        for (int i = 0; i < height; i++) {
            for (int j = -width; j < 0; j++) {
                Canvas2D.pixels[position] = Canvas2D.mix(red, green, blue, pixels[position++], alpha);
            }
            position += step;
        }
    }

    public static String getString() {
        return new StringBuilder("[Raster: ").append(width).append(", ").append(height).append(']').toString();
    }

    /**
     * Mixes two colors together.
     *
     * @param red   the red to add.
     * @param green the green to add.
     * @param blue  the blue to add.
     * @param color the color being mixed.
     * @param alpha the alpha of the color being added.
     * @return
     */
    public static int mix(int red, int green, int blue, int color, int alpha) {
        int r = (color >> 16 & 0xFF) * alpha;
        int g = (color >> 8 & 0xFF) * alpha;
        int b = (color & 0xFF) * alpha;
        return ((red + r >> 8) << 16) + ((green + g >> 8) << 8) + (blue + b >> 8);
    }

    public static void plot(int x, int y, int color) {
        Canvas2D.pixels[x + (y * Canvas2D.width)] = color;
    }

    /**
     * Applies the pixels to the Raster for drawing.
     *
     * @param width  the width.
     * @param height the height.
     * @param pixels the pixels. (INT_RGB)
     */
    public static void prepare(int width, int height, int[] pixels) {
        Canvas2D.pixels = pixels;
        Canvas2D.width = width;
        Canvas2D.height = height;
        Canvas2D.set_bounds(0, 0, width, height);
    }

    /**
     * Resets the bounds.
     */
    public static void reset() {
        Canvas2D.left_x = 0;
        Canvas2D.left_y = 0;
        Canvas2D.right_x = width;
        Canvas2D.right_y = height;
        Canvas2D.bound = right_x - 1;
        Canvas2D.center_x = right_x / 2;
        Canvas2D.center_y = right_y / 2;
    }

    /**
     * Sets the bounds of the Raster.
     *
     * @param x0 the top left x coordinate.
     * @param y0 the top left y coordinate.
     * @param x1 the bottom right x coordinate.
     * @param y1 the bottom right y coordinate.
     */
    public static void set_bounds(int x0, int y0, int x1, int y1) {
        if (x0 < 0) {
            x0 = 0;
        }

        if (y0 < 0) {
            y0 = 0;
        }

        if (x1 > Canvas2D.width) {
            x1 = Canvas2D.width;
        }

        if (y1 > Canvas2D.height) {
            y1 = Canvas2D.height;
        }

        Canvas2D.left_x = x0;
        Canvas2D.left_y = y0;
        Canvas2D.right_x = x1;
        Canvas2D.right_y = y1;
        Canvas2D.bound = right_x - 1;
        Canvas2D.center_x = right_x / 2;
        Canvas2D.center_y = right_y / 2;
    }

}
