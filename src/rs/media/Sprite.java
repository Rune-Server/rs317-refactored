package rs.media;

import rs.cache.Archive;
import rs.io.Buffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.io.File;

public class Sprite extends Canvas2D {

    public int crop_height;
    public int crop_width;
    public int width;
    public int height;
    public int offset_x;
    public int offset_y;
    public int[] pixels;

    /**
     * Creates a sprite from the specified archive, image archive, and image index.
     *
     * @param archive       the archive.
     * @param image_archive the image archive.
     * @param image_index   the image index.
     */
    public Sprite(Archive archive, String image_archive, int image_index) {
        Buffer data = new Buffer(archive.get(image_archive + ".dat", null));
        Buffer idx = new Buffer(archive.get("index.dat", null));
        idx.position = data.get_ushort();

        this.crop_width = idx.get_ushort();
        this.crop_height = idx.get_ushort();

        int palette[] = new int[idx.get_ubyte()];

        for (int k = 0; k < palette.length - 1; k++) {
            palette[k + 1] = idx.get_medium();

            if (palette[k + 1] == 0) {
                palette[k + 1] = 1;
            }
        }

        for (int i = 0; i < image_index; i++) {
            idx.position += 2;
            data.position += idx.get_ushort() * idx.get_ushort();
            idx.position++;
        }

        this.offset_x = idx.get_ubyte();
        this.offset_y = idx.get_ubyte();
        this.width = idx.get_ushort();
        this.height = idx.get_ushort();
        int type = idx.get_ubyte();

        this.pixels = new int[this.width * this.height];

        if (type == 0) {
            for (int i = 0; i < this.pixels.length; i++) {
                this.pixels[i] = palette[data.get_ubyte()];
            }
        } else if (type == 1) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixels[x + (y * this.width)] = palette[data.get_ubyte()];
                }
            }
        }
    }

    public Sprite(byte[] data, Component c) {
        try {
            Image i = Toolkit.getDefaultToolkit().createImage(data);
            MediaTracker t = new MediaTracker(c);
            t.addImage(i, 0);
            t.waitForAll();
            this.width = i.getWidth(c);
            this.height = i.getHeight(c);
            this.crop_width = width;
            this.crop_height = height;
            this.offset_x = 0;
            this.offset_y = 0;
            this.pixels = new int[width * height];
            PixelGrabber g = new PixelGrabber(i, 0, 0, width, height, pixels, 0, width);
            g.grabPixels();
            return;
        } catch (Exception _ex) {
            System.out.println("Error converting jpg");
        }
    }

    /**
     * Creates an empty sprite with the provided width and height.
     *
     * @param w the width.
     * @param h the height.
     */
    public Sprite(int w, int h) {
        this.pixels = new int[w * h];
        this.crop_width = w;
        this.crop_height = h;
        this.width = this.crop_width;
        this.height = this.crop_height;
        this.offset_x = 0;
        this.offset_y = 0;
    }

    /**
     * Crops this image.
     */
    public void crop() {
        int pixels[] = new int[this.crop_width * this.crop_height];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                pixels[(y + this.offset_y) * this.crop_width + (x + this.offset_x)] = this.pixels[x + (y * this.width)];
            }
        }

        this.pixels = pixels;
        this.width = this.crop_width;
        this.height = this.crop_height;
        this.offset_x = 0;
        this.offset_y = 0;
    }

    /**
     * Draws the image into the provided bitmap's pixels.
     *
     * @param bitmap the bitmap to draw into.
     * @param x      the x position.
     * @param y      the y position.
     */
    public void draw_to(Bitmap bitmap, int x, int y) {
        x += this.offset_x;
        y += this.offset_y;

        int dst_off = x + y * Canvas2D.width;
        int src_off = 0;

        int height = this.height;
        int width = this.width;

        int dst_step = Canvas2D.width - width;
        int src_step = 0;

        if (y < Canvas2D.left_y) {
            int i = Canvas2D.left_y - y;
            height -= i;
            y = Canvas2D.left_y;
            src_off += i * width;
            dst_off += i * Canvas2D.width;
        }

        if (y + height > Canvas2D.right_y) {
            height -= (y + height) - Canvas2D.right_y;
        }

        if (x < Canvas2D.left_x) {
            int i = Canvas2D.left_x - x;
            width -= i;
            x = Canvas2D.left_x;
            src_off += i;
            dst_off += i;
            src_step += i;
            dst_step += i;
        }

        if (x + width > Canvas2D.right_x) {
            int i = (x + width) - Canvas2D.right_x;
            width -= i;
            src_step += i;
            dst_step += i;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        Canvas2D.draw(pixels, bitmap.pixels, src_off, dst_off, width, height, dst_step, src_step);
    }

    /**
     * Draws the image with no transparency. (Opaque)
     *
     * @param x the x.
     * @param y the y.
     */
    public void draw(int x, int y) {
        x += this.offset_x;
        y += this.offset_y;

        int height = this.height;
        int width = this.width;

        int dst_off = x + y * Canvas2D.width;
        int dst_step = Canvas2D.width - width;

        int src_off = 0;
        int src_step = 0;

        if (y < Canvas2D.left_y) {
            int yOffset = Canvas2D.left_y - y;
            height -= yOffset;
            y = Canvas2D.left_y;
            src_off += yOffset * width;
            dst_off += yOffset * Canvas2D.width;
        }

        if (y + height > Canvas2D.right_y) {
            height -= (y + height) - Canvas2D.right_y;
        }

        if (x < Canvas2D.left_x) {
            int xOffset = Canvas2D.left_x - x;
            width -= xOffset;
            x = Canvas2D.left_x;
            src_off += xOffset;
            dst_off += xOffset;
            src_step += xOffset;
            dst_step += xOffset;
        }

        if (x + width > Canvas2D.right_x) {
            int widthOffset = (x + width) - Canvas2D.right_x;
            width -= widthOffset;
            src_step += widthOffset;
            dst_step += widthOffset;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        Canvas2D.draw(pixels, src_off, dst_off, width, height, dst_step, src_step, DrawType.RGB);
    }

    /**
     * Draws the image with the provided alpha level;
     *
     * @param x     the x.
     * @param y     the y.
     * @param alpha the transparency level.
     */
    public void draw(int x, int y, int alpha) {
        x += this.offset_x;
        y += this.offset_y;

        int dst_off = x + y * Canvas2D.width;
        int src_off = 0;

        int height = this.height;
        int width = this.width;

        int dst_step = Canvas2D.width - width;
        int src_step = 0;

        if (y < Canvas2D.left_y) {
            int i = Canvas2D.left_y - y;
            height -= i;
            y = Canvas2D.left_y;
            src_off += i * width;
            dst_off += i * Canvas2D.width;
        }

        if (y + height > Canvas2D.right_y) {
            height -= (y + height) - Canvas2D.right_y;
        }

        if (x < Canvas2D.left_x) {
            int i = Canvas2D.left_x - x;
            width -= i;
            x = Canvas2D.left_x;
            src_off += i;
            dst_off += i;
            src_step += i;
            dst_step += i;
        }

        if (x + width > Canvas2D.right_x) {
            int i = (x + width) - Canvas2D.right_x;
            width -= i;
            src_step += i;
            dst_step += i;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        Canvas2D.alpha = alpha;
        Canvas2D.draw(this.pixels, src_off, dst_off, width, height, dst_step, src_step, DrawType.TRANSLUCENT_IGNORE_BLACK);
    }

    public void draw_rotated(int x, int y, int pivot_x, int pivot_y, int width, int height, int zoom, double angle) {
        try {
            int center_y = -width / 2;
            int center_x = -height / 2;

            int sin = (int) (Math.sin(angle) * 65536D);
            int cos = (int) (Math.cos(angle) * 65536D);

            sin = sin * zoom >> 8;
            cos = cos * zoom >> 8;

            int src_off_x = (pivot_x << 16) + (center_x * sin + center_y * cos);
            int src_off_y = (pivot_y << 16) + (center_x * cos - center_y * sin);

            int dst_off = x + y * Canvas2D.width;

            for (y = 0; y < height; y++) {
                int i = dst_off;
                int off_x = src_off_x;
                int off_y = src_off_y;

                for (x = -width; x < 0; x++) {
                    int color = this.pixels[(off_x >> 16) + (off_y >> 16) * this.width];

                    if (color != 0) {
                        Canvas2D.pixels[i++] = color;
                    } else {
                        i++;
                    }

                    off_x += cos;
                    off_y -= sin;
                }

                src_off_x += sin;
                src_off_y += cos;
                dst_off += Canvas2D.width;
            }

            return;
        } catch (Exception _ex) {
            return;
        }
    }

    public void draw(int x, int y, int width, int height, int radians, int zoom, int pivot_x, int pivot_y, int ai[], int ai1[]) {
        try {
            int center_x = -width / 2;
            int center_y = -height / 2;

            int sin = (int) (Math.sin((double) radians / 326.11000000000001D) * 65536D);
            int cos = (int) (Math.cos((double) radians / 326.11000000000001D) * 65536D);
            sin = sin * zoom >> 8;
            cos = cos * zoom >> 8;

            int src_off_x = (pivot_x << 16) + (center_y * sin + center_x * cos);
            int src_off_y = (pivot_y << 16) + (center_y * cos - center_x * sin);

            int dst_off = x + y * Canvas2D.width;

            for (y = 0; y < height; y++) {
                int i4 = ai1[y];
                int dst_offset = dst_off + i4;
                int offset_x = src_off_x + cos * i4;
                int offset_y = src_off_y - sin * i4;

                for (x = -ai[y]; x < 0; x++) {
                    Canvas2D.pixels[dst_offset++] = this.pixels[(offset_x >> 16) + (offset_y >> 16) * this.width];
                    offset_x += cos;
                    offset_y -= sin;
                }

                src_off_x += sin;
                src_off_y += cos;
                dst_off += Canvas2D.width;
            }
        } catch (Exception _ex) {
        }
    }

    /**
     * Draws the image ignoring all 0x000000 colored pixels. (Black)
     *
     * @param x the x.
     * @param y the y.
     */
    public void draw_masked(int x, int y) {
        x += this.offset_x;
        y += this.offset_y;
        int dst_off = x + y * Canvas2D.width;
        int src_off = 0;
        int height = this.height;
        int width = this.width;
        int dst_step = Canvas2D.width - width;
        int src_step = 0;

        if (y < Canvas2D.left_y) {
            int yOffset = Canvas2D.left_y - y;
            height -= yOffset;
            y = Canvas2D.left_y;
            src_off += yOffset * width;
            dst_off += yOffset * Canvas2D.width;
        }

        if (y + height > Canvas2D.right_y) {
            height -= (y + height) - Canvas2D.right_y;
        }

        if (x < Canvas2D.left_x) {
            int xOffset = Canvas2D.left_x - x;
            width -= xOffset;
            x = Canvas2D.left_x;
            src_off += xOffset;
            dst_off += xOffset;
            src_step += xOffset;
            dst_step += xOffset;
        }

        if (x + width > Canvas2D.right_x) {
            int xOffset = (x + width) - Canvas2D.right_x;
            width -= xOffset;
            src_step += xOffset;
            dst_step += xOffset;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        Canvas2D.draw(this.pixels, src_off, dst_off, width, height, dst_step, src_step, DrawType.IGNORE_BLACK);
    }

    public void export(File f) {
        try {
            BufferedImage i = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            int[] pixels = ((DataBufferInt) i.getRaster().getDataBuffer()).getData();
            System.arraycopy(this.pixels, 0, pixels, 0, this.pixels.length);
            ImageIO.write(i, "png", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepare() {
        Canvas2D.prepare(this.width, this.height, this.pixels);
    }

    public void translate_rgb(int r, int g, int b) {
        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];
            if (color != 0) {
                int red = color >> 16 & 0xff;
                red += r;
                if (red < 1) {
                    red = 1;
                } else if (red > 255) {
                    red = 255;
                }
                int green = color >> 8 & 0xff;
                green += g;
                if (green < 1) {
                    green = 1;
                } else if (green > 255) {
                    green = 255;
                }
                int blue = color & 0xff;
                blue += b;
                if (blue < 1) {
                    blue = 1;
                } else if (blue > 255) {
                    blue = 255;
                }
                pixels[i] = (red << 16) + (green << 8) + blue;
            }
        }
    }
}
