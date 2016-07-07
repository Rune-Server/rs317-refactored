package rs.media;

import rs.cache.Archive;
import rs.io.Buffer;

public class Bitmap extends Canvas2D {

    public static final byte FLIP_HORIZONTALLY = 0x1;
    public static final byte FLIP_VERTICALLY = 0x2;

    public int crop_height;
    public int crop_width;
    public int width;
    public int height;
    public int offset_x;
    public int offset_y;
    public int[] palette;
    public byte[] pixels;

    public Bitmap(Archive archive, String image_archive) {
        this(archive, image_archive, 0);
    }

    /**
     * Creates a new Bitmap. (A Bitmap is a space saving image which uses a palette of 256 colors and a color depth of 24.)
     *
     * @param archive       the archive to load from.
     * @param image_archive the image archive within the archive.
     * @param file_index    the image index within the image archive.
     */
    public Bitmap(Archive archive, String image_archive, int file_index) {
        Buffer data = new Buffer(archive.get(image_archive + ".dat", null));
        Buffer idx = new Buffer(archive.get("index.dat", null));

        // The index of this bitmap's header is stored in the first 2 bytes of the data file.
        idx.position = data.get_ushort();

        this.crop_width = idx.get_ushort();
        this.crop_height = idx.get_ushort();

        this.palette = new int[idx.get_ubyte()];

        for (int i = 0; i < this.palette.length - 1; i++) {
            this.palette[i + 1] = idx.get_medium();
        }

        for (int l = 0; l < file_index; l++) {
            idx.position += 2;
            data.position += idx.get_ushort() * idx.get_ushort();
            idx.position++;
        }

        this.offset_x = idx.get_ubyte();
        this.offset_y = idx.get_ubyte();
        this.width = idx.get_ushort();
        this.height = idx.get_ushort();
        int type = idx.get_ubyte();

        this.pixels = new byte[this.width * this.height];

        if (type == 0) {
            for (int i = 0; i < this.pixels.length; i++) {
                this.pixels[i] = data.get_byte();
            }
        } else if (type == 1) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixels[x + (y * this.width)] = data.get_byte();
                }
            }
        }
    }

    /**
     * Crops the image to the specified width and height.
     */
    public void crop() {
        if (this.width == this.crop_width && this.height == this.crop_height) {
            return;
        }

        byte[] pixels = new byte[this.crop_width * this.crop_height];

        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                pixels[x + this.offset_x + (y + this.offset_y) * this.crop_width] = this.pixels[i++];
            }
        }

        this.pixels = pixels;
        this.width = this.crop_width;
        this.height = this.crop_height;
        this.offset_x = 0;
        this.offset_y = 0;
    }

    /**
     * Draws the image.
     *
     * @param x the x position.
     * @param y the y position.
     */
    public void draw(int x, int y) {
        x += this.offset_x;
        y += this.offset_y;

        int dst_off = x + y * Canvas2D.width;
        int src_off = 0;

        int height = this.height;
        int width = this.width;

        int dst_step = Canvas2D.width - width;
        int src_step = 0;

        if (y < Canvas2D.left_y) {
            int y_diff = Canvas2D.left_y - y;
            height -= y_diff;
            y = Canvas2D.left_y;
            src_off += y_diff * width;
            dst_off += y_diff * Canvas2D.width;
        }

        if (y + height > Canvas2D.right_y) {
            height -= (y + height) - Canvas2D.right_y;
        }

        if (x < Canvas2D.left_x) {
            int x_diff = Canvas2D.left_x - x;
            width -= x_diff;
            x = Canvas2D.left_x;
            src_off += x_diff;
            dst_off += x_diff;
            src_step += x_diff;
            dst_step += x_diff;
        }

        if (x + width > Canvas2D.right_x) {
            int x_diff = (x + width) - Canvas2D.right_x;
            width -= x_diff;
            src_step += x_diff;
            dst_step += x_diff;
        }

        if (width <= 0 || height <= 0) {
            return;
        }

        draw(this.pixels, this.palette, src_off, dst_off, width, height, dst_step, src_step);
    }

    /**
     * Flips the image horizontally.
     */
    public Bitmap flip_horizontally() {
        byte pixels[] = new byte[this.width * this.height];

        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = this.width - 1; x >= 0; x--) {
                pixels[i++] = this.pixels[x + (y * this.width)];
            }
        }

        this.pixels = pixels;
        this.offset_x = this.crop_width - this.width - this.offset_x;
        return this;
    }

    /**
     * Flips the image vertically.
     */
    public Bitmap flip_vertically() {
        byte pixels[] = new byte[this.width * this.height];

        int i = 0;
        for (int y = this.height - 1; y >= 0; y--) {
            for (int x = 0; x < this.width; x++) {
                pixels[i++] = this.pixels[x + (y * this.width)];
            }
        }

        this.pixels = pixels;
        this.offset_y = this.crop_height - this.height - this.offset_y;
        return this;
    }

    /**
     * Shrinks the image to half its original size.
     */
    public void shrink() {
        this.crop_width >>= 1;
        this.crop_height >>= 1;

        byte pixels[] = new byte[this.crop_width * this.crop_height];
        int i = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                pixels[(x + this.offset_x >> 1) + (y + this.offset_y >> 1) * this.crop_width] = this.pixels[i++];
            }
        }

        this.pixels = pixels;
        this.width = this.crop_width;
        this.height = this.crop_height;
        this.offset_x = 0;
        this.offset_y = 0;
    }

    /**
     * Adjusts the palette's color.
     *
     * @param red   the red modifier.
     * @param green the green modifier.
     * @param blue  the blue modifier.
     */
    public void translate_rgb(int red, int green, int blue) {
        for (int i = 0; i < this.palette.length; i++) {
            int r = (this.palette[i] >> 16 & 0xff) + red;
            int g = (this.palette[i] >> 8 & 0xff) + green;
            int b = (this.palette[i] & 0xff) + blue;
            r = r > 255 ? 255 : r < 0 ? 0 : r;
            g = g > 255 ? 255 : g < 0 ? 0 : g;
            b = b > 255 ? 255 : b < 0 ? 0 : b;
            this.palette[i] = (r << 16) + (g << 8) + b;
        }
    }
}
