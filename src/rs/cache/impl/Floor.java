package rs.cache.impl;

import rs.cache.Archive;
import rs.io.Buffer;

public class Floor {

    public static int count;
    public static Floor[] instance;

    public static void unpack(Archive a) {
        Buffer s = new Buffer(a.get("flo.dat", null));

        Floor.count = s.get_ushort();
        Floor.instance = new Floor[Floor.count];

        for (int i = 0; i < Floor.count; i++) {
            Floor.instance[i] = new Floor(s);
        }
    }

    public int color;
    public int color2;
    public int hue;
    public int hue_divisor;
    public int hue2;
    public int lightness;
    public String name;
    public int saturation;
    public boolean show_underlay;
    public byte texture_index;

    public Floor(Buffer buffer) {
        this.defaults();

        do {
            int opcode = buffer.get_ubyte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                set_color(this.color2 = buffer.get_medium());
            } else if (opcode == 2) {
                this.texture_index = buffer.get_byte();
            } else if (opcode == 3) {
            } else if (opcode == 5) {
                this.show_underlay = false;
            } else if (opcode == 6) {
                this.name = buffer.get_string();
            } else if (opcode == 7) {
                int hue2 = this.hue2;
                int saturation = this.saturation;
                int lightness = this.lightness;
                int hue = this.hue;
                set_color(buffer.get_medium());
                this.hue2 = hue2;
                this.saturation = saturation;
                this.lightness = lightness;
                this.hue = hue;
                this.hue_divisor = hue;
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }

    public void defaults() {
        texture_index = -1;
        show_underlay = true;
    }

    public int trim_hsl(int hue, int saturation, int lightness) {
        if (lightness > 179) {
            saturation /= 2;
        }
        if (lightness > 192) {
            saturation /= 2;
        }
        if (lightness > 217) {
            saturation /= 2;
        }
        if (lightness > 243) {
            saturation /= 2;
        }
        return (hue / 4 << 10) + (saturation / 32 << 7) + lightness / 2;
    }

    public void set_color(int rgb) {
        double red = (double) (rgb >> 16 & 0xff) / 256D;
        double green = (double) (rgb >> 8 & 0xff) / 256D;
        double blue = (double) (rgb & 0xff) / 256D;
        double d3 = red;

        if (green < d3) {
            d3 = green;
        }

        if (blue < d3) {
            d3 = blue;
        }

        double d4 = red;

        if (green > d4) {
            d4 = green;
        }

        if (blue > d4) {
            d4 = blue;
        }

        double d5 = 0.0D;
        double d6 = 0.0D;
        double d7 = (d3 + d4) / 2D;

        if (d3 != d4) {
            if (d7 < 0.5D) {
                d6 = (d4 - d3) / (d4 + d3);
            }
            if (d7 >= 0.5D) {
                d6 = (d4 - d3) / (2D - d4 - d3);
            }
            if (red == d4) {
                d5 = (green - blue) / (d4 - d3);
            } else if (green == d4) {
                d5 = 2D + (blue - red) / (d4 - d3);
            } else if (blue == d4) {
                d5 = 4D + (red - green) / (d4 - d3);
            }
        }

        d5 /= 6D;
        this.hue2 = (int) (d5 * 256D);
        this.saturation = (int) (d6 * 256D);
        this.lightness = (int) (d7 * 256D);

        if (this.saturation < 0) {
            this.saturation = 0;
        } else if (saturation > 255) {
            this.saturation = 255;
        }

        if (this.lightness < 0) {
            this.lightness = 0;
        } else if (this.lightness > 255) {
            this.lightness = 255;
        }

        if (d7 > 0.5D) {
            this.hue_divisor = (int) ((1.0D - d7) * d6 * 512D);
        } else {
            this.hue_divisor = (int) (d7 * d6 * 512D);
        }

        if (this.hue_divisor < 1) {
            this.hue_divisor = 1;
        }

        this.hue = (int) (d5 * (double) this.hue_divisor);

        int hue = (this.hue2 + (int) (Math.random() * 16D)) - 8;

        if (hue < 0) {
            hue = 0;
        } else if (hue > 255) {
            hue = 255;
        }

        int saturation = (this.saturation + (int) (Math.random() * 48D)) - 24;

        if (saturation < 0) {
            saturation = 0;
        } else if (saturation > 255) {
            saturation = 255;
        }

        int lightness = (this.lightness + (int) (Math.random() * 48D)) - 24;

        if (lightness < 0) {
            lightness = 0;
        } else if (lightness > 255) {
            lightness = 255;
        }

        this.color = trim_hsl(hue, saturation, lightness);
    }
}
