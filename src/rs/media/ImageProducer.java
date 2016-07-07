package rs.media;

import rs.Game;
import rs.cache.Archive;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.Hashtable;

public class ImageProducer {

    public static final ColorModel OPAQUE_COLOR_MODEL = new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
    public static final ColorModel TRANSPARENT_COLOR_MODEL = new DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF, 0xFF000000);

    public static ImageProducer derive(Archive archive, String image_archive) {
        return derive(archive, image_archive, 0);
    }

    public static ImageProducer derive(Archive archive, String image_archive, int image_index) {
        Sprite image = new Sprite(archive, image_archive, image_index);
        ImageProducer producer = new ImageProducer(image.width, image.height);
        image.draw(0, 0);
        return producer;
    }

    public final int width;
    public final int height;
    public final BufferedImage image;
    public final int[] pixels;
    public final Component component;

    public ImageProducer(int width, int height) {
        this(width, height, false);
    }

    public ImageProducer(int width, int height, boolean alpha) {
        this.width = width;
        this.height = height;
        this.component = Game.get_component();
        int count = width * height;
        this.pixels = new int[count];
        this.image = new BufferedImage(alpha ? TRANSPARENT_COLOR_MODEL : OPAQUE_COLOR_MODEL, Raster.createWritableRaster(OPAQUE_COLOR_MODEL.createCompatibleSampleModel(width, height), new DataBufferInt(pixels, count), null), false, new Hashtable<Object, Object>());
        this.prepare();
    }

    public void draw(int x, int y) {
        Game.instance.graphics.drawImage(this.image, x, y, this.component);
    }

    public void export(File f) {
        try {
            ImageIO.write(this.image, "png", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepare() {
        Canvas2D.prepare(this.width, this.height, this.pixels);
    }
}
