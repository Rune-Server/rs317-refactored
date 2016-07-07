package rs.scene.model;

public class UnderlayTile {

    public int hsl_ne;
    public int hsl_nw;
    public int hsl_se;
    public int hsl_sw;
    public boolean is_flat;
    public int rgb;
    public byte texture_index;

    public UnderlayTile(int hsl_sw, int hsl_se, int hsl_ne, int hsl_nw, byte texture_index, int rgb_bitset, boolean is_flat) {
        this.hsl_sw = hsl_sw;
        this.hsl_ne = hsl_ne;
        this.hsl_nw = hsl_nw;
        this.hsl_se = hsl_se;
        this.texture_index = texture_index;
        this.rgb = rgb_bitset;
        this.is_flat = is_flat;
    }
}
