package rs.scene.model;

import rs.node.Link;

public class Tile extends Link {

    public boolean aBoolean1322;
    public boolean aBoolean1323;
    public boolean aBoolean1324;
    public int anInt1325;
    public int anInt1326;
    public int anInt1327;
    public int anInt1328;
    public Tile bridge;
    public int flags;
    public GroundDecoration ground_decoration;
    public ItemPile item_pile;
    public int loc_count;
    public int loc_flag[];
    public StaticLoc locs[];
    public OverlayTile overlay;
    public int plane;
    public int top_plane;
    public UnderlayTile underlay;
    public WallLoc wall;
    public WallDecoration wall_decoration;
    public byte x, y, z;

    public Tile(int plane, int x, int y) {
        this.locs = new StaticLoc[5];
        this.loc_flag = new int[5];
        this.plane = this.z = (byte) plane;
        this.x = (byte) x;
        this.y = (byte) y;
    }
}
