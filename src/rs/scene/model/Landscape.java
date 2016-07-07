package rs.scene.model;

import rs.Game;
import rs.media.Canvas2D;
import rs.media.Canvas3D;
import rs.node.Chain;
import rs.node.impl.Renderable;

public class Landscape {

    public static StaticLoc aClass28Array462[] = new StaticLoc[100];
    public static int anInt446;
    public static final byte anIntArray463[] = {53, -53, -53, 53};
    public static final byte anIntArray464[] = {-53, -53, 53, 53};
    public static final byte anIntArray465[] = {-45, 45, 45, -45};
    public static final byte anIntArray466[] = {45, 45, -45, -45};
    public static final int anIntArray478[] = {0x13, 0x37, 0x26, 0x9B, 0xFF, 0x6E, 0x89, 0xCD, 0x4C};
    public static final int anIntArray479[] = {160, 192, 80, 96, 0, 144, 80, 48, 160};
    public static final int anIntArray480[] = {76, 8, 137, 4, 0, 1, 38, 2, 19};
    public static final int anIntArray481[] = {0, 0, 2, 0, 0, 2, 1, 1, 0};
    public static final int anIntArray482[] = {2, 0, 0, 2, 0, 0, 0, 4, 4};
    public static final int anIntArray483[] = {0, 4, 4, 8, 0, 0, 8, 0, 0};
    public static final int anIntArray484[] = {1, 1, 0, 0, 0, 8, 0, 0, 8};
    public static int cam_local_x;
    public static int cam_local_y;
    public static int cam_p_cos;
    public static int cam_p_sin;
    public static int cam_x;
    public static int cam_y;
    public static int cam_y_cos;
    public static int cam_y_sin;
    public static int cam_z;
    public static int click_local_x = -1;
    public static int click_local_y = -1;
    public static int click_x;
    public static int click_y;
    public static CullingBox[] cull_boxes = new CullingBox[500];
    public static int cull_plane_count;
    public static int culling_box_count[];
    public static CullingBox culling_boxes[][];
    public static int culling_position;
    public static int cycle;
    public static boolean input_requested;
    public static int max_visible_x;
    public static int max_visible_y;
    public static int min_visible_x;
    public static int min_visible_y;
    public static int occlusion_top_plane;
    public static int scr_c_x;
    public static int scr_c_y;
    public static int scr_h;
    public static int scr_w;
    public static int scr_x;
    public static int scr_y;
    public static final int TEXTURE_HSL[] = {41, 39248, 41, 4643, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41, 41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41};
    public static Chain tile_queue = new Chain();
    public static boolean culling_map[][];
    public static boolean visibility_map[][][][] = new boolean[8][32][51][51];

    static {
        cull_plane_count = 4;
        culling_box_count = new int[cull_plane_count];
        culling_boxes = new CullingBox[cull_plane_count][500];
    }

    public static void create_culling_box(int plane, int min_x, int max_x, int min_y, int max_y, int min_vertex_height, int max_vertex_height, int type) {
        CullingBox box = new CullingBox();
        box.local_min_x = min_x / 128;
        box.local_max_x = max_x / 128;
        box.local_min_y = min_y / 128;
        box.local_max_y = max_y / 128;
        box.occlusion_type = type;
        box.min_x = min_x;
        box.max_x = max_x;
        box.min_y = min_y;
        box.max_y = max_y;
        box.max_z = max_vertex_height;
        box.min_z = min_vertex_height;
        culling_boxes[plane][culling_box_count[plane]++] = box;
    }

    public static boolean is_visible(int scene_x, int scene_y, int scene_z) {
        int x = scene_y * cam_y_sin + scene_x * cam_y_cos >> 16;
        int i1 = scene_y * cam_y_cos - scene_x * cam_y_sin >> 16;
        int z = scene_z * cam_p_sin + i1 * cam_p_cos >> 16;
        int y = scene_z * cam_p_cos - i1 * cam_p_sin >> 16;

        if (z < 50 || z > 3500) {
            return false;
        }

        int screen_x = scr_c_x + (x << 9) / z;
        int screen_y = scr_c_y + (y << 9) / z;
        return screen_x >= scr_x && screen_x <= scr_w && screen_y >= scr_y && screen_y <= scr_h;
    }

    public static void nullify() {
        aClass28Array462 = null;
        culling_box_count = null;
        culling_boxes = null;
        tile_queue = null;
        visibility_map = null;
        culling_map = null;
    }

    public static void setup_viewport(int near_z, int far_z, int screen_width, int screen_height, int z_array[]) {
        Landscape.scr_x = 0;
        Landscape.scr_y = 0;
        Landscape.scr_w = screen_width;
        Landscape.scr_h = screen_height;
        Landscape.scr_c_x = screen_width / 2;
        Landscape.scr_c_y = screen_height / 2;

        boolean visibility_map[][][][] = new boolean[9][32][53][53];

        for (int pitch = Camera.MIN_PITCH; pitch <= Camera.MAX_PITCH + 1; pitch += 32) {
            for (int yaw = 0; yaw < 2048; yaw += 64) {
                cam_p_sin = Model.sin[pitch];
                cam_p_cos = Model.cos[pitch];
                cam_y_sin = Model.sin[yaw];
                cam_y_cos = Model.cos[yaw];
                int z_index = (pitch - 128) / 32;
                int yaw_index = yaw / 64;

                for (int x = -26; x <= 26; x++) {
                    for (int y = -26; y <= 26; y++) {
                        int scene_x = x * 128;
                        int scene_y = y * 128;
                        boolean is_visible = false;

                        for (int z = -near_z; z <= far_z; z += 128) {
                            if (!is_visible(scene_x, scene_y, z_array[z_index] + z)) {
                                continue;
                            }
                            is_visible = true;
                            break;
                        }

                        visibility_map[z_index][yaw_index][x + 25 + 1][y + 25 + 1] = is_visible;
                    }
                }
            }
        }

        for (int pitch = 0; pitch < 8; pitch++) {
            for (int yaw = 0; yaw < 32; yaw++) {
                for (int x = -25; x < 25; x++) {
                    for (int y = -25; y < 25; y++) {
                        boolean visible = false;

                        label0:
                        for (int l3 = -1; l3 <= 1; l3++) {
                            for (int j4 = -1; j4 <= 1; j4++) {
                                if (visibility_map[pitch][yaw][x + l3 + 25 + 1][y + j4 + 25 + 1]) {
                                    visible = true;
                                } else if (visibility_map[pitch][(yaw + 1) % 31][x + l3 + 25 + 1][y + j4 + 25 + 1]) {
                                    visible = true;
                                } else if (visibility_map[pitch + 1][yaw][x + l3 + 25 + 1][y + j4 + 25 + 1]) {
                                    visible = true;
                                } else {
                                    if (!visibility_map[pitch + 1][(yaw + 1) % 31][x + l3 + 25 + 1][y + j4 + 25 + 1]) {
                                        continue;
                                    }
                                    visible = true;
                                }
                                break label0;
                            }
                        }

                        Landscape.visibility_map[pitch][yaw][x + 25][y + 25] = visible;
                    }
                }
            }
        }
    }

    public int anInt488;
    public int anIntArray486[];
    public int anIntArray487[];
    public short height_map[][][];
    public int loc_count;
    public StaticLoc locs[];

    /* @formatter:off */
	public int MINIMAP_OVERLAY_MASK[][] = { new int[16],
		{
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1
		},
		{
			1, 0, 0, 0,
			1, 1, 0, 0,
			1, 1, 1, 0,
			1, 1, 1, 1
		},
		{
			1, 1, 0, 0,
			1, 1, 0, 0,
			1, 0, 0, 0,
			1, 0, 0, 0
		},
		{
			0, 0, 1, 1,
			0, 0, 1, 1,
			0, 0, 0, 1,
			0, 0, 0, 1
		},
		{
			0, 1, 1, 1,
			0, 1, 1, 1,
			1, 1, 1, 1,
			1, 1, 1, 1
		},
		{
			1, 1, 1, 0,
			1, 1, 1, 0,
			1, 1, 1, 1,
			1, 1, 1, 1
		},
		{
			1, 1, 0, 0,
			1, 1, 0, 0,
			1, 1, 0, 0,
			1, 1, 0, 0
		},
		{
			0, 0, 0, 0,
			0, 0, 0, 0,
			1, 0, 0, 0,
			1, 1, 0, 0
		},
		{
			1, 1, 1, 1,
			1, 1, 1, 1,
			0, 1, 1, 1,
			0, 0, 1, 1
		},
		{
			1, 1, 1, 1,
			1, 1, 0, 0,
			1, 0, 0, 0,
			1, 0, 0, 0
		},
		{
			0, 0, 0, 0,
			0, 0, 1, 1,
			0, 1, 1, 1,
			0, 1, 1, 1
		},
		{
			0, 0, 0, 0,
			0, 0, 0, 0,
			0, 1, 1, 0,
			1, 1, 1, 1
		}
	};
	
	public int MINIMAP_OVERLAY_MASK_ROTATION[][] = {
		{
			0, 1, 2, 3,
			4, 5, 6, 7,
			8, 9, 10, 11,
			12, 13, 14, 15
		},
		{
			12, 8, 4, 0,
			13, 9, 5, 1,
			14, 10, 6, 2,
			15, 11, 7, 3
		},
		{
			15, 14, 13, 12,
			11, 10, 9, 8,
			7, 6, 5, 4,
			3, 2, 1, 0
		},
		{
			3, 7, 11, 15,
			2, 6, 10, 14,
			1, 5, 9, 13,
			0, 4, 8, 12
		}
	};
	/* @formatter:on */

    public int plane;
    public int size_x;
    public int size_y;
    public int size_z;
    public int tile_cycle[][][];
    public Tile[][][] tile_map;

    public Landscape(int size_x, int size_y, int size_z, short height_map[][][]) {
        this.locs = new StaticLoc[5000];
        this.anIntArray486 = new int[10000];
        this.anIntArray487 = new int[10000];
        this.size_z = size_z;
        this.size_x = size_x;
        this.size_y = size_y;
        this.tile_map = new Tile[size_z][size_x][size_y];
        this.tile_cycle = new int[size_z][size_x + 1][size_y + 1];
        this.height_map = height_map;
        this.reset();
    }

    public boolean add(Renderable r, int s_x, int s_y, int s_z, int plane, int angle, int radius, boolean rotate, int uid) {
        if (r == null) {
            return true;
        }

        int x1 = s_x - radius;
        int y1 = s_y - radius;
        int x2 = s_x + radius;
        int y2 = s_y + radius;

        if (rotate) {
            if (angle > 640 && angle < 1408) {
                y2 += 128;
            }
            if (angle > 1152 && angle < 1920) {
                x2 += 128;
            }
            if (angle > 1664 || angle < 384) {
                y1 -= 128;
            }
            if (angle > 128 && angle < 896) {
                x1 -= 128;
            }
        }

        x1 /= 128;
        y1 /= 128;
        x2 /= 128;
        y2 /= 128;

        return add(r, s_x, s_y, s_z, plane, x1, y1, (x2 - x1) + 1, (y2 - y1) + 1, uid, (byte) 0, angle, true);
    }

    public boolean add(Renderable r, int l_x, int l_y, int l_z, int size_x, int size_y, int v_height, byte arrangement, int angle, int uid) {
        if (r == null) {
            return true;
        } else {
            int scene_x = l_x * 128 + 64 * size_y;
            int scene_y = l_y * 128 + 64 * size_x;
            return add(r, scene_x, scene_y, v_height, l_z, l_x, l_y, size_y, size_x, uid, arrangement, angle, false);
        }
    }

    public boolean add(Renderable node, int x, int y, int z, int plane, int local_x0, int local_y0, int local_x1, int local_y1, int uid, byte arrangement, int angle, boolean append) {
        for (int x0 = local_x0; x0 < local_x0 + local_x1; x0++) {
            for (int y0 = local_y0; y0 < local_y0 + local_y1; y0++) {
                if (x0 < 0 || y0 < 0 || x0 >= size_x || y0 >= size_y) {
                    return false;
                }
                Tile t = tile_map[plane][x0][y0];
                if (t != null && t.loc_count >= 5) {
                    return false;
                }
            }
        }

        StaticLoc sl = new StaticLoc();
        sl.uid = uid;
        sl.arrangement = arrangement;
        sl.plane = plane;
        sl.x = x;
        sl.y = y;
        sl.z = z;
        sl.node = node;
        sl.rotation = angle;
        sl.local_x0 = local_x0;
        sl.local_y0 = local_y0;
        sl.local_x1 = (local_x0 + local_x1) - 1;
        sl.local_y1 = (local_y0 + local_y1) - 1;

        for (int x0 = local_x0; x0 < local_x0 + local_x1; x0++) {
            for (int y0 = local_y0; y0 < local_y0 + local_y1; y0++) {
                int flag = 0;

                if (x0 > local_x0) {
                    flag++;
                }

                if (x0 < (local_x0 + local_x1) - 1) {
                    flag += 4;
                }

                if (y0 > local_y0) {
                    flag += 8;
                }

                if (y0 < (local_y0 + local_y1) - 1) {
                    flag += 2;
                }

                for (int z0 = plane; z0 >= 0; z0--) {
                    if (tile_map[z0][x0][y0] == null) {
                        tile_map[z0][x0][y0] = new Tile(z0, x0, y0);
                    }
                }

                Tile t = tile_map[plane][x0][y0];
                t.locs[t.loc_count] = sl;
                t.loc_flag[t.loc_count] = flag;
                t.flags |= flag;
                t.loc_count++;
            }

        }

        if (append) {
            locs[loc_count++] = sl;
        }
        return true;
    }

    public boolean add(Renderable r, int x, int y, int z, int plane, int x1, int y1, int x2, int y2, int rotation, int uid) {
        if (r == null) {
            return true;
        }
        return add(r, x, y, z, plane, x1, y1, (x2 - x1) + 1, (y2 - y1) + 1, uid, (byte) 0, rotation, true);
    }

    public void add_bridge(int x, int y) {
        Tile _t = tile_map[0][x][y];

        for (int z = 0; z < 3; z++) {
            Tile t = tile_map[z][x][y] = tile_map[z + 1][x][y];
            if (t != null) {
                t.z--;
                for (int i = 0; i < t.loc_count; i++) {
                    StaticLoc sl = t.locs[i];
                    if ((sl.uid >> 29 & 3) == 2 && sl.local_x0 == x && sl.local_y0 == y) {
                        sl.plane--;
                    }
                }

            }
        }

        if (tile_map[0][x][y] == null) {
            tile_map[0][x][y] = new Tile(0, x, y);
        }

        tile_map[0][x][y].bridge = _t;
        tile_map[3][x][y] = null;
    }

    public void add_ground_deco(int x, int y, int z, int plane, Renderable r, int uid, byte arrangement) {
        if (r == null) {
            return;
        }

        GroundDecoration gd = new GroundDecoration();
        gd.node = r;
        gd.s_x = x * 128 + 64;
        gd.s_y = y * 128 + 64;
        gd.s_z = z;
        gd.uid = uid;
        gd.arrangement = arrangement;

        if (tile_map[plane][x][y] == null) {
            tile_map[plane][x][y] = new Tile(plane, x, y);
        }

        tile_map[plane][x][y].ground_decoration = gd;
    }

    public void add_item_pile(int x, int y, int z, int plane, Renderable r_top, Renderable r_mid, Renderable r_btm, int uid) {
        ItemPile ip = new ItemPile();
        ip.x = x * 128 + 64;
        ip.y = y * 128 + 64;
        ip.z = z;
        ip.uid = uid;
        ip.top = r_top;
        ip.middle = r_mid;
        ip.bottom = r_btm;

        int min_z = 0;

        Tile t = tile_map[plane][x][y];

        if (t != null) {
            for (int i = 0; i < t.loc_count; i++) {
                if (t.locs[i].node instanceof Model) {
                    int offset = ((Model) t.locs[i].node).pile_height;

                    if (offset > min_z) {
                        min_z = offset;
                    }
                }
            }
        }

        ip.off_z = min_z;

        if (tile_map[plane][x][y] == null) {
            tile_map[plane][x][y] = new Tile(plane, x, y);
        }

        tile_map[plane][x][y].item_pile = ip;
    }

    public void add_tile(int plane, int x, int y, int shape, int rotation, byte texture_index, short v_sw, short v_se, short v_ne, short v_nw, int hsl_sw, int hsl_se, int hsl_ne, int hsl_nw, int rgb_sw, int rgb_se, int rgb_ne, int rgb_nw, int rgb_bitset, int hsl_bitset) {
        if (shape == 0) {
            UnderlayTile t = new UnderlayTile(hsl_sw, hsl_se, hsl_ne, hsl_nw, (byte) -1, rgb_bitset, false);

            for (int z = plane; z >= 0; z--) {
                if (tile_map[z][x][y] == null) {
                    tile_map[z][x][y] = new Tile(z, x, y);
                }
            }

            tile_map[plane][x][y].underlay = t;
        } else if (shape == 1) {
            UnderlayTile t = new UnderlayTile(rgb_sw, rgb_se, rgb_ne, rgb_nw, texture_index, hsl_bitset, v_sw == v_se && v_sw == v_ne && v_sw == v_nw);

            for (int z = plane; z >= 0; z--) {
                if (tile_map[z][x][y] == null) {
                    tile_map[z][x][y] = new Tile(z, x, y);
                }
            }

            tile_map[plane][x][y].underlay = t;
        } else {
            OverlayTile t = new OverlayTile(x, y, v_sw, v_se, v_ne, v_nw, rgb_sw, rgb_se, rgb_ne, rgb_nw, rgb_bitset, hsl_sw, hsl_se, hsl_ne, hsl_nw, hsl_bitset, texture_index, rotation, shape);

            for (int z = plane; z >= 0; z--) {
                if (tile_map[z][x][y] == null) {
                    tile_map[z][x][y] = new Tile(z, x, y);
                }
            }

            tile_map[plane][x][y].overlay = t;
        }
    }

    public void add_wall(Renderable r1, Renderable r2, int x, int y, int z, int plane, int rotation_flag, int corner_flag, byte arrangement, boolean flag, int uid) {
        if (r1 == null && r2 == null) {
            return;
        }

        WallLoc wl = new WallLoc();
        wl.uid = uid;
        wl.arrangement = arrangement;
        wl.s_x = x * 128 + 64;
        wl.s_y = y * 128 + 64;
        wl.s_z = z;
        wl.root = r1;
        wl.extension = r2;
        wl.rotation_flag = rotation_flag;
        wl.corner_flag = corner_flag;

        for (int current_plane = plane; current_plane >= 0; current_plane--) {
            if (tile_map[current_plane][x][y] == null) {
                tile_map[current_plane][x][y] = new Tile(current_plane, x, y);
            }
        }

        tile_map[plane][x][y].wall = wl;
    }

    public void add_wall_deco(Renderable r, int x, int y, int plane, int offset_x, int offset_y, int offset_z, int rotation, byte arrangement, int flags, int uid) {
        if (r == null) {
            return;
        }

        WallDecoration wd = new WallDecoration();
        wd.uid = uid;
        wd.arrangement = arrangement;
        wd.x = x * 128 + 64 + offset_x;
        wd.y = y * 128 + 64 + offset_y;
        wd.z = offset_z;
        wd.node = r;
        wd.flags = flags;
        wd.rotation = rotation;

        for (int i = plane; i >= 0; i--) {
            if (tile_map[i][x][y] == null) {
                tile_map[i][x][y] = new Tile(i, x, y);
            }
        }

        tile_map[plane][x][y].wall_decoration = wd;
    }

    public int adjust_hsl_lightness(int hsl, int l) {
        l = 127 - l;
        l = (l * (hsl & 0x7f)) / 0xA0;

        if (l < 2) {
            l = 2;
        } else if (l > 126) {
            l = 126;
        }

        return (hsl & 0xff80) + l;
    }

    public void apply_untextured_objects(int light_x, int light_y, int light_z, int light_brightness, int specular_factor) {
        int light_len = (int) Math.sqrt(light_x * light_x + light_y * light_y + light_z * light_z);
        int specular_distribution = specular_factor * light_len >> 8;

        for (int plane = 0; plane < size_z; plane++) {
            for (int x = 0; x < size_x; x++) {
                for (int y = 0; y < size_y; y++) {
                    Tile t = tile_map[plane][x][y];
                    if (t != null) {
                        WallLoc wl = t.wall;
                        if (wl != null && wl.root != null && wl.root.normal != null) {
                            method307((Model) wl.root, x, y, plane, 1, 1);
                            if (wl.extension != null && wl.extension.normal != null) {
                                method307((Model) wl.extension, x, y, plane, 1, 1);
                                method308((Model) wl.root, (Model) wl.extension, 0, 0, 0, false);
                                ((Model) wl.extension).apply_lighting(light_brightness, specular_distribution, light_x, light_y, light_z);
                            }
                            ((Model) wl.root).apply_lighting(light_brightness, specular_distribution, light_x, light_y, light_z);
                        }

                        for (int k2 = 0; k2 < t.loc_count; k2++) {
                            StaticLoc sl = t.locs[k2];
                            if (sl != null && sl.node != null && sl.node.normal != null) {
                                method307((Model) sl.node, x, y, plane, (sl.local_x1 - sl.local_x0) + 1, (sl.local_y1 - sl.local_y0) + 1);
                                ((Model) sl.node).apply_lighting(light_brightness, specular_distribution, light_x, light_y, light_z);
                            }
                        }

                        GroundDecoration gd = t.ground_decoration;
                        if (gd != null && gd.node.normal != null) {
                            method306((Model) gd.node, x, y, plane);
                            ((Model) gd.node).apply_lighting(light_brightness, specular_distribution, light_x, light_y, light_z);
                        }
                    }
                }

            }

        }

    }

    public void clear_locs() {
        for (int i = 0; i < loc_count; i++) {
            remove(locs[i]);
            locs[i] = null;
        }
        loc_count = 0;
    }

    public void clear_locs(int x, int y, int z) {
        Tile t = tile_map[z][x][y];
        if (t == null) {
            return;
        }
        for (int i = 0; i < t.loc_count; i++) {
            StaticLoc sl = t.locs[i];
            if ((sl.uid >> 29 & 3) == 2 && sl.local_x0 == x && sl.local_y0 == y) {
                remove(sl);
                return;
            }
        }

    }

    public void clicked(int click_y, int click_x) {
        input_requested = true;
        Landscape.click_x = click_x;
        Landscape.click_y = click_y;
        click_local_x = -1;
        click_local_y = -1;
    }

    public void draw(int cam_x, int cam_y, int cam_yaw, int cam_z, int occlusion_top_plane, int cam_pitch) {
        if (cam_x < 0) {
            cam_x = 0;
        } else if (cam_x >= size_x * 128) {
            cam_x = size_x * 128 - 1;
        }

        if (cam_y < 0) {
            cam_y = 0;
        } else if (cam_y >= size_y * 128) {
            cam_y = size_y * 128 - 1;
        }

        Landscape.cycle++;
        Landscape.cam_p_sin = Model.sin[cam_pitch];
        Landscape.cam_p_cos = Model.cos[cam_pitch];
        Landscape.cam_y_sin = Model.sin[cam_yaw];
        Landscape.cam_y_cos = Model.cos[cam_yaw];
        Landscape.culling_map = visibility_map[(cam_pitch - 128) / 32][cam_yaw / 64];
        Landscape.cam_x = cam_x;
        Landscape.cam_z = cam_z;
        Landscape.cam_y = cam_y;
        Landscape.cam_local_x = cam_x / 128;
        Landscape.cam_local_y = cam_y / 128;
        Landscape.occlusion_top_plane = occlusion_top_plane;

        Landscape.min_visible_x = cam_local_x - 25;

        if (min_visible_x < 0) {
            min_visible_x = 0;
        }

        Landscape.min_visible_y = cam_local_y - 25;

        if (min_visible_y < 0) {
            min_visible_y = 0;
        }

        Landscape.max_visible_x = cam_local_x + 25;

        if (max_visible_x > size_x) {
            max_visible_x = size_x;
        }

        Landscape.max_visible_y = cam_local_y + 25;

        if (max_visible_y > size_y) {
            max_visible_y = size_y;
        }

        update_culling();
        Landscape.anInt446 = 0;

        for (int z = plane; z < size_z; z++) {
            Tile _t[][] = tile_map[z];
            for (int x = min_visible_x; x < max_visible_x; x++) {
                for (int y = min_visible_y; y < max_visible_y; y++) {
                    Tile t = _t[x][y];
                    if (t != null) {
                        if (t.top_plane > occlusion_top_plane || !culling_map[(x - cam_local_x) + 25][(y - cam_local_y) + 25] && height_map[z][x][y] - cam_z < 2000) {
                            t.aBoolean1322 = false;
                            t.aBoolean1323 = false;
                            t.anInt1325 = 0;
                        } else {
                            t.aBoolean1322 = true;
                            t.aBoolean1323 = true;
                            if (t.loc_count > 0) {
                                t.aBoolean1324 = true;
                            } else {
                                t.aBoolean1324 = false;
                            }
                            anInt446++;
                        }
                    }
                }

            }

        }

        for (int z = plane; z < size_z; z++) {
            Tile _t[][] = tile_map[z];

            for (int x_off = -25; x_off <= 0; x_off++) {
                int x0 = cam_local_x + x_off;
                int x1 = cam_local_x - x_off;

                if (x0 >= min_visible_x || x1 < max_visible_x) {
                    for (int y_off = -25; y_off <= 0; y_off++) {
                        int y0 = cam_local_y + y_off;
                        int y1 = cam_local_y - y_off;

                        if (x0 >= min_visible_x) {
                            if (y0 >= min_visible_y) {
                                Tile t = _t[x0][y0];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, true);
                                }
                            }
                            if (y1 < max_visible_y) {
                                Tile t = _t[x0][y1];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, true);
                                }
                            }
                        }

                        if (x1 < max_visible_x) {
                            if (y0 >= min_visible_y) {
                                Tile t = _t[x1][y0];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, true);
                                }
                            }
                            if (y1 < max_visible_y) {
                                Tile t = _t[x1][y1];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, true);
                                }
                            }
                        }

                        if (anInt446 == 0) {
                            input_requested = false;
                            return;
                        }
                    }
                }
            }
        }

        for (int z = plane; z < size_z; z++) {
            Tile _t[][] = tile_map[z];
            for (int x_off = -25; x_off <= 0; x_off++) {
                int x0 = cam_local_x + x_off;
                int x1 = cam_local_x - x_off;
                if (x0 >= min_visible_x || x1 < max_visible_x) {
                    for (int y_off = -25; y_off <= 0; y_off++) {
                        int y0 = cam_local_y + y_off;
                        int y1 = cam_local_y - y_off;
                        if (x0 >= min_visible_x) {
                            if (y0 >= min_visible_y) {
                                Tile t = _t[x0][y0];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, false);
                                }
                            }
                            if (y1 < max_visible_y) {
                                Tile t = _t[x0][y1];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, false);
                                }
                            }
                        }
                        if (x1 < max_visible_x) {
                            if (y0 >= min_visible_y) {
                                Tile t = _t[x1][y0];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, false);
                                }
                            }
                            if (y1 < max_visible_y) {
                                Tile t = _t[x1][y1];
                                if (t != null && t.aBoolean1322) {
                                    method314(t, false);
                                }
                            }
                        }
                        if (anInt446 == 0) {
                            input_requested = false;
                            return;
                        }
                    }

                }
            }

        }

        input_requested = false;
    }

    public void draw_minimap_tile(int pixels[], int start, int width, int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];

        if (t == null) {
            return;
        }

        UnderlayTile ut = t.underlay;

        if (ut != null) {
            int rgb = ut.rgb;

            if (rgb == 0) {
                return;
            }

            for (int i = 0; i < 4; i++) {
                pixels[start] = rgb;
                pixels[start + 1] = rgb;
                pixels[start + 2] = rgb;
                pixels[start + 3] = rgb;
                start += width;
            }
            return;
        }

        OverlayTile ot = t.overlay;

        if (ot == null) {
            return;
        }

        int rgb = ot.rgb;
        int hsl = ot.hsl;
        int mask[] = MINIMAP_OVERLAY_MASK[ot.shape];
        int rotate[] = MINIMAP_OVERLAY_MASK_ROTATION[ot.rotation];
        int i = 0;

        if (rgb != 0) {
            for (int j = 0; j < 4; j++) {
                pixels[start] = mask[rotate[i++]] != 0 ? hsl : rgb;
                pixels[start + 1] = mask[rotate[i++]] != 0 ? hsl : rgb;
                pixels[start + 2] = mask[rotate[i++]] != 0 ? hsl : rgb;
                pixels[start + 3] = mask[rotate[i++]] != 0 ? hsl : rgb;
                start += width;
            }
            return;
        }

        for (int j = 0; j < 4; j++) {
            if (mask[rotate[i++]] != 0) {
                pixels[start] = hsl;
            }

            if (mask[rotate[i++]] != 0) {
                pixels[start + 1] = hsl;
            }

            if (mask[rotate[i++]] != 0) {
                pixels[start + 2] = hsl;
            }

            if (mask[rotate[i++]] != 0) {
                pixels[start + 3] = hsl;
            }

            start += width;
        }

    }

    public void draw_overlay_tile(OverlayTile ot, int local_x, int local_y, int pitch_sin, int pitch_cos, int yaw_sin, int yaw_cos) {
        int i = ot.triangle_x.length;

        for (int j = 0; j < i; j++) {
            int x = ot.triangle_x[j] - cam_x;
            int y = ot.triangle_y[j] - cam_z;
            int z = ot.triangle_z[j] - cam_y;

            int w = z * yaw_sin + x * yaw_cos >> 16;
            z = z * yaw_cos - x * yaw_sin >> 16;
            x = w;

            w = y * pitch_cos - z * pitch_sin >> 16;
            z = y * pitch_sin + z * pitch_cos >> 16;
            y = w;

            if (z < 50) {
                return;
            }

            if (ot.triangle_texture_index != null) {
                OverlayTile.tmp_triangle_x[j] = x;
                OverlayTile.tmp_triangle_y[j] = y;
                OverlayTile.tmp_triangle_z[j] = z;
            }

            // Setting the temporary on-screen values.
            OverlayTile.tmp_screen_x[j] = Canvas3D.center_x + ((x << 9) / z);
            OverlayTile.tmp_screen_y[j] = Canvas3D.center_y + ((y << 9) / z);
        }

        Canvas3D.opacity = 0;
        i = ot.vertex_x.length;

        for (int j = 0; j < i; j++) {
            int v_x_i = ot.vertex_x[j];
            int v_y_i = ot.vertex_y[j];
            int v_z_i = ot.vertex_z[j];
            int x1 = OverlayTile.tmp_screen_x[v_x_i];
            int x2 = OverlayTile.tmp_screen_x[v_y_i];
            int x3 = OverlayTile.tmp_screen_x[v_z_i];
            int y1 = OverlayTile.tmp_screen_y[v_x_i];
            int y2 = OverlayTile.tmp_screen_y[v_y_i];
            int y3 = OverlayTile.tmp_screen_y[v_z_i];

            if ((x1 - x2) * (y3 - y2) - (y1 - y2) * (x3 - x2) > 0) {
                Canvas3D.check_bounds = false;

                if (x1 < 0 || x2 < 0 || x3 < 0 || x1 > Canvas2D.bound || x2 > Canvas2D.bound || x3 > Canvas2D.bound) {
                    Canvas3D.check_bounds = true;
                }

                // Used for clicking on the map.
                if (input_requested && tri_contains(click_x, click_y, x1, y1, x2, y2, x3, y3)) {
                    click_local_x = local_x;
                    click_local_y = local_y;
                }

                if (ot.triangle_texture_index == null || ot.triangle_texture_index[j] == -1) {
                    if (ot.vertex_color_a[j] != 12345678) {
                        Canvas3D.draw_shaded_triangle(x1, y1, x2, y2, x3, y3, ot.vertex_color_a[j], ot.vertex_color_b[j], ot.vertex_color_c[j]);
                    }
                } else if (!Game.low_detail) {
                    if (ot.ignore_uv) {
                        Canvas3D.draw_textured_triangle(x1, y1, x2, y2, x3, y3, ot.vertex_color_a[j], ot.vertex_color_b[j], ot.vertex_color_c[j], OverlayTile.tmp_triangle_x[0], OverlayTile.tmp_triangle_y[0], OverlayTile.tmp_triangle_z[0], OverlayTile.tmp_triangle_x[1], OverlayTile.tmp_triangle_y[1], OverlayTile.tmp_triangle_z[1], OverlayTile.tmp_triangle_x[3], OverlayTile.tmp_triangle_y[3], OverlayTile.tmp_triangle_z[3], ot.triangle_texture_index[j]);
                    } else {
                        Canvas3D.draw_textured_triangle(x1, y1, x2, y2, x3, y3, ot.vertex_color_a[j], ot.vertex_color_b[j], ot.vertex_color_c[j], OverlayTile.tmp_triangle_x[v_x_i], OverlayTile.tmp_triangle_y[v_x_i], OverlayTile.tmp_triangle_z[v_x_i], OverlayTile.tmp_triangle_x[v_y_i], OverlayTile.tmp_triangle_y[v_y_i], OverlayTile.tmp_triangle_z[v_y_i], OverlayTile.tmp_triangle_x[v_z_i], OverlayTile.tmp_triangle_y[v_z_i], OverlayTile.tmp_triangle_z[v_z_i], ot.triangle_texture_index[j]);
                    }
                } else {
                    int hsl = TEXTURE_HSL[ot.triangle_texture_index[j]];
                    Canvas3D.draw_shaded_triangle(x1, y1, x2, y2, x3, y3, adjust_hsl_lightness(hsl, ot.vertex_color_a[j]), adjust_hsl_lightness(hsl, ot.vertex_color_b[j]), adjust_hsl_lightness(hsl, ot.vertex_color_c[j]));
                }
            }
        }
    }

    public void draw_underlay_tile(UnderlayTile ut, int plane, int cam_pitch_sin, int cam_pitch_cos, int cam_yaw_sin, int cam_yaw_cos, int x, int y) {
        int nw_x;
        int sw_x = nw_x = (x << 7) - cam_x;
        int se_z;
        int sw_z = se_z = (y << 7) - cam_y;
        int ne_x;
        int se_x = ne_x = sw_x + 128;
        int nw_z;
        int ne_z = nw_z = sw_z + 128;

        int sw_y = height_map[plane][x][y] - cam_z;
        int se_y = height_map[plane][x + 1][y] - cam_z;
        int ne_y = height_map[plane][x + 1][y + 1] - cam_z;
        int nw_y = height_map[plane][x][y + 1] - cam_z;

        int i = sw_z * cam_yaw_sin + sw_x * cam_yaw_cos >> 16;
        sw_z = sw_z * cam_yaw_cos - sw_x * cam_yaw_sin >> 16;
        sw_x = i;

        i = sw_y * cam_pitch_cos - sw_z * cam_pitch_sin >> 16;
        sw_z = sw_y * cam_pitch_sin + sw_z * cam_pitch_cos >> 16;
        sw_y = i;

        if (sw_z < 50) {
            return;
        }

        i = se_z * cam_yaw_sin + se_x * cam_yaw_cos >> 16;
        se_z = se_z * cam_yaw_cos - se_x * cam_yaw_sin >> 16;
        se_x = i;

        i = se_y * cam_pitch_cos - se_z * cam_pitch_sin >> 16;
        se_z = se_y * cam_pitch_sin + se_z * cam_pitch_cos >> 16;
        se_y = i;

        if (se_z < 50) {
            return;
        }

        i = ne_z * cam_yaw_sin + ne_x * cam_yaw_cos >> 16;
        ne_z = ne_z * cam_yaw_cos - ne_x * cam_yaw_sin >> 16;
        ne_x = i;

        i = ne_y * cam_pitch_cos - ne_z * cam_pitch_sin >> 16;
        ne_z = ne_y * cam_pitch_sin + ne_z * cam_pitch_cos >> 16;
        ne_y = i;

        if (ne_z < 50) {
            return;
        }

        i = nw_z * cam_yaw_sin + nw_x * cam_yaw_cos >> 16;
        nw_z = nw_z * cam_yaw_cos - nw_x * cam_yaw_sin >> 16;
        nw_x = i;

        i = nw_y * cam_pitch_cos - nw_z * cam_pitch_sin >> 16;
        nw_z = nw_y * cam_pitch_sin + nw_z * cam_pitch_cos >> 16;
        nw_y = i;

        if (nw_z < 50) {
            return;
        }

        int x1 = Canvas3D.center_x + (sw_x << 9) / sw_z;
        int y1 = Canvas3D.center_y + (sw_y << 9) / sw_z;
        int x2 = Canvas3D.center_x + (se_x << 9) / se_z;
        int y2 = Canvas3D.center_y + (se_y << 9) / se_z;
        int x3 = Canvas3D.center_x + (ne_x << 9) / ne_z;
        int y3 = Canvas3D.center_y + (ne_y << 9) / ne_z;
        int x4 = Canvas3D.center_x + (nw_x << 9) / nw_z;
        int y4 = Canvas3D.center_y + (nw_y << 9) / nw_z;

        Canvas3D.opacity = 0;
        if ((x3 - x4) * (y2 - y4) - (y3 - y4) * (x2 - x4) > 0) {
            Canvas3D.check_bounds = false;

            if (x3 < 0 || x4 < 0 || x2 < 0 || x3 > Canvas2D.bound || x4 > Canvas2D.bound || x2 > Canvas2D.bound) {
                Canvas3D.check_bounds = true;
            }

            if (input_requested && tri_contains(click_x, click_y, x3, y3, x4, y4, x2, y2)) {
                click_local_x = x;
                click_local_y = y;
            }

            if (ut.texture_index == -1) {
                if (ut.hsl_ne != 12345678) {
                    Canvas3D.draw_shaded_triangle(x3, y3, x4, y4, x2, y2, ut.hsl_ne, ut.hsl_nw, ut.hsl_se);
                }
            } else if (!Game.low_detail) {
                if (ut.is_flat) {
                    Canvas3D.draw_textured_triangle(x3, y3, x4, y4, x2, y2, ut.hsl_ne, ut.hsl_nw, ut.hsl_se, sw_x, sw_y, sw_z, se_x, se_y, se_z, nw_x, nw_y, nw_z, ut.texture_index);
                } else {
                    Canvas3D.draw_textured_triangle(x3, y3, x4, y4, x2, y2, ut.hsl_ne, ut.hsl_nw, ut.hsl_se, ne_x, ne_y, ne_z, nw_x, nw_y, nw_z, se_x, se_y, se_z, ut.texture_index);
                }
            } else {
                int i7 = TEXTURE_HSL[ut.texture_index];
                Canvas3D.draw_shaded_triangle(x3, y3, x4, y4, x2, y2, adjust_hsl_lightness(i7, ut.hsl_ne), adjust_hsl_lightness(i7, ut.hsl_nw), adjust_hsl_lightness(i7, ut.hsl_se));
            }
        }

        if ((x1 - x2) * (y4 - y2) - (y1 - y2) * (x4 - x2) > 0) {
            Canvas3D.check_bounds = false;

            if (x1 < 0 || x2 < 0 || x4 < 0 || x1 > Canvas2D.bound || x2 > Canvas2D.bound || x4 > Canvas2D.bound) {
                Canvas3D.check_bounds = true;
            }

            if (input_requested && tri_contains(click_x, click_y, x1, y1, x2, y2, x4, y4)) {
                click_local_x = x;
                click_local_y = y;
            }

            if (ut.texture_index == -1) {
                if (ut.hsl_sw != 12345678) {
                    Canvas3D.draw_shaded_triangle(x1, y1, x2, y2, x4, y4, ut.hsl_sw, ut.hsl_se, ut.hsl_nw);
                }
            } else {
                if (!Game.low_detail) {
                    Canvas3D.draw_textured_triangle(x1, y1, x2, y2, x4, y4, ut.hsl_sw, ut.hsl_se, ut.hsl_nw, sw_x, sw_y, sw_z, se_x, se_y, se_z, nw_x, nw_y, nw_z, ut.texture_index);
                } else {
                    int j7 = TEXTURE_HSL[ut.texture_index];
                    Canvas3D.draw_shaded_triangle(x1, y1, x2, y2, x4, y4, adjust_hsl_lightness(j7, ut.hsl_sw), adjust_hsl_lightness(j7, ut.hsl_se), adjust_hsl_lightness(j7, ut.hsl_nw));
                }
            }
        }
    }

    public int get_arrangement(int plane, int x, int y, int uid) {
        Tile t = tile_map[plane][x][y];

        if (t == null) {
            return -1;
        }

        if (t.wall != null && t.wall.uid == uid) {
            return t.wall.arrangement & 0xff;
        }

        if (t.wall_decoration != null && t.wall_decoration.uid == uid) {
            return t.wall_decoration.arrangement & 0xff;
        }

        if (t.ground_decoration != null && t.ground_decoration.uid == uid) {
            return t.ground_decoration.arrangement & 0xff;
        }

        for (int i = 0; i < t.loc_count; i++) {
            if (t.locs[i].uid == uid) {
                return t.locs[i].arrangement & 0xff;
            }
        }
        return -1;
    }

    public GroundDecoration get_ground_decoration(int x, int y, int z) {
        Tile t = tile_map[z][x][y];
        if (t == null || t.ground_decoration == null) {
            return null;
        }
        return t.ground_decoration;
    }

    public int get_ground_decoration_uid(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];
        if (t == null || t.ground_decoration == null) {
            return 0;
        } else {
            return t.ground_decoration.uid;
        }
    }

    public StaticLoc get_loc(int x, int y, int plane) {
        Tile t = tile_map[plane][x][y];
        if (t == null) {
            return null;
        }
        for (int i = 0; i < t.loc_count; i++) {
            StaticLoc sl = t.locs[i];
            if ((sl.uid >> 29 & 3) == 2 && sl.local_x0 == x && sl.local_y0 == y) {
                return sl;
            }
        }
        return null;
    }

    public int get_loc_uid(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];
        if (t == null) {
            return 0;
        }
        for (int l = 0; l < t.loc_count; l++) {
            StaticLoc sl = t.locs[l];
            if ((sl.uid >> 29 & 3) == 2 && sl.local_x0 == x && sl.local_y0 == y) {
                return sl.uid;
            }
        }
        return 0;
    }

    public WallLoc get_wall(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];
        if (t == null) {
            return null;
        } else {
            return t.wall;
        }
    }

    public WallDecoration get_wall_decoration(int x, int j, int y, int z) {
        Tile t = tile_map[z][x][y];
        if (t == null) {
            return null;
        } else {
            return t.wall_decoration;
        }
    }

    public int get_wall_decoration_uid(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];
        if (t == null || t.wall_decoration == null) {
            return 0;
        } else {
            return t.wall_decoration.uid;
        }
    }

    public int get_wall_uid(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];

        if (t == null || t.wall == null) {
            return 0;
        }

        return t.wall.uid;
    }

    public boolean is_culled(int x, int y, int z) {
        for (int i = 0; i < culling_position; i++) {
            CullingBox box = cull_boxes[i];

            if (box.culling_mode == 1) {
                int dx = box.min_x - x;

                if (dx > 0) {
                    int min_y = box.min_y + (box.anInt801 * dx >> 8);
                    int max_y = box.max_y + (box.anInt802 * dx >> 8);
                    int min_z = box.max_z + (box.anInt803 * dx >> 8);
                    int max_z = box.min_z + (box.anInt804 * dx >> 8);

                    if (y >= min_y && y <= max_y && z >= min_z && z <= max_z) {
                        return true;
                    }
                }
            } else if (box.culling_mode == 2) {
                int dx = x - box.min_x;

                if (dx > 0) {
                    int min_y = box.min_y + (box.anInt801 * dx >> 8);
                    int max_y = box.max_y + (box.anInt802 * dx >> 8);
                    int min_z = box.max_z + (box.anInt803 * dx >> 8);
                    int max_z = box.min_z + (box.anInt804 * dx >> 8);

                    if (y >= min_y && y <= max_y && z >= min_z && z <= max_z) {
                        return true;
                    }
                }
            } else if (box.culling_mode == 3) {
                int dy = box.min_y - y;

                if (dy > 0) {
                    int min_x = box.min_x + (box.anInt799 * dy >> 8);
                    int max_x = box.max_x + (box.anInt800 * dy >> 8);
                    int min_z = box.max_z + (box.anInt803 * dy >> 8);
                    int max_z = box.min_z + (box.anInt804 * dy >> 8);

                    if (x >= min_x && x <= max_x && z >= min_z && z <= max_z) {
                        return true;
                    }
                }
            } else if (box.culling_mode == 4) {
                int dy = y - box.min_y;

                if (dy > 0) {
                    int min_x = box.min_x + (box.anInt799 * dy >> 8);
                    int max_x = box.max_x + (box.anInt800 * dy >> 8);
                    int min_z = box.max_z + (box.anInt803 * dy >> 8);
                    int max_z = box.min_z + (box.anInt804 * dy >> 8);

                    if (x >= min_x && x <= max_x && z >= min_z && z <= max_z) {
                        return true;
                    }
                }
            } else if (box.culling_mode == 5) {
                int dz = z - box.max_z;

                if (dz > 0) {
                    int min_x = box.min_x + (box.anInt799 * dz >> 8);
                    int max_x = box.max_x + (box.anInt800 * dz >> 8);
                    int min_y = box.min_y + (box.anInt801 * dz >> 8);
                    int max_y = box.max_y + (box.anInt802 * dz >> 8);

                    if (x >= min_x && x <= max_x && y >= min_y && y <= max_y) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean is_culled(int plane, int local_x, int local_y, int height) {
        if (!is_tile_culled(plane, local_x, local_y)) {
            return false;
        }

        int tile_x = local_x << 7;
        int tile_y = local_y << 7;

        return is_culled(tile_x + 1, tile_y + 1, height_map[plane][local_x][local_y] - height) && is_culled((tile_x + 128) - 1, tile_y + 1, height_map[plane][local_x + 1][local_y] - height) && is_culled((tile_x + 128) - 1, (tile_y + 128) - 1, height_map[plane][local_x + 1][local_y + 1] - height) && is_culled(tile_x + 1, (tile_y + 128) - 1, height_map[plane][local_x][local_y + 1] - height);
    }

    public boolean is_tile_culled(int plane, int x, int y) {
        int i = tile_cycle[plane][x][y];

        if (i == -cycle) {
            return false;
        }

        if (i == cycle) {
            return true;
        }

        int s_x = x << 7;
        int s_y = y << 7;

        if (is_culled(s_x + 1, s_y + 1, height_map[plane][x][y]) && is_culled((s_x + 128) - 1, s_y + 1, height_map[plane][x + 1][y]) && is_culled((s_x + 128) - 1, (s_y + 128) - 1, height_map[plane][x + 1][y + 1]) && is_culled(s_x + 1, (s_y + 128) - 1, height_map[plane][x][y + 1])) {
            tile_cycle[plane][x][y] = cycle;
            return true;
        } else {
            tile_cycle[plane][x][y] = -cycle;
            return false;
        }
    }

    public boolean is_wall_culled(int z, int x, int y, int type) {
        if (!is_tile_culled(z, x, y)) {
            return false;
        }

        int s_x = x << 7;
        int s_y = y << 7;
        int s_z = height_map[z][x][y] - 1;
        int lv1 = s_z - 120;
        int lv2 = s_z - 230;
        int lv3 = s_z - 238;

        if (type < 0x10) {
            if (type == 0x1) {
                if (s_x > cam_x) {
                    if (!is_culled(s_x, s_y, s_z)) {
                        return false;
                    }
                    if (!is_culled(s_x, s_y + 128, s_z)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!is_culled(s_x, s_y, lv1)) {
                        return false;
                    }
                    if (!is_culled(s_x, s_y + 128, lv1)) {
                        return false;
                    }
                }
                if (!is_culled(s_x, s_y, lv2)) {
                    return false;
                }
                return is_culled(s_x, s_y + 128, lv2);
            }
            if (type == 0x2) {
                if (s_y < cam_y) {
                    if (!is_culled(s_x, s_y + 128, s_z)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y + 128, s_z)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!is_culled(s_x, s_y + 128, lv1)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y + 128, lv1)) {
                        return false;
                    }
                }
                if (!is_culled(s_x, s_y + 128, lv2)) {
                    return false;
                }
                return is_culled(s_x + 128, s_y + 128, lv2);
            }
            if (type == 0x4) {
                if (s_x < cam_x) {
                    if (!is_culled(s_x + 128, s_y, s_z)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y + 128, s_z)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!is_culled(s_x + 128, s_y, lv1)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y + 128, lv1)) {
                        return false;
                    }
                }
                if (!is_culled(s_x + 128, s_y, lv2)) {
                    return false;
                }
                return is_culled(s_x + 128, s_y + 128, lv2);
            }
            if (type == 0x8) {
                if (s_y > cam_y) {
                    if (!is_culled(s_x, s_y, s_z)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y, s_z)) {
                        return false;
                    }
                }
                if (z > 0) {
                    if (!is_culled(s_x, s_y, lv1)) {
                        return false;
                    }
                    if (!is_culled(s_x + 128, s_y, lv1)) {
                        return false;
                    }
                }
                if (!is_culled(s_x, s_y, lv2)) {
                    return false;
                }
                return is_culled(s_x + 128, s_y, lv2);
            }
        }

        if (!is_culled(s_x + 64, s_y + 64, lv3)) {
            return false;
        }

        if (type == 0x10) {
            return is_culled(s_x, s_y + 128, lv2);
        } else if (type == 0x20) {
            return is_culled(s_x + 128, s_y + 128, lv2);
        } else if (type == 0x40) {
            return is_culled(s_x + 128, s_y, lv2);
        } else if (type == 0x80) {
            return is_culled(s_x, s_y, lv2);
        }

        System.out.println("Warning unsupported wall type");
        return true;
    }

    public void method306(Model mesh, int x, int y, int plane) {
        if (x < size_x) {
            Tile t = tile_map[plane][x + 1][y];
            if (t != null && t.ground_decoration != null && t.ground_decoration.node.normal != null) {
                method308(mesh, (Model) t.ground_decoration.node, 128, 0, 0, true);
            }
        }
        if (y < size_x) {
            Tile t = tile_map[plane][x][y + 1];
            if (t != null && t.ground_decoration != null && t.ground_decoration.node.normal != null) {
                method308(mesh, (Model) t.ground_decoration.node, 0, 0, 128, true);
            }
        }
        if (x < size_x && y < size_y) {
            Tile t = tile_map[plane][x + 1][y + 1];
            if (t != null && t.ground_decoration != null && t.ground_decoration.node.normal != null) {
                method308(mesh, (Model) t.ground_decoration.node, 128, 0, 128, true);
            }
        }
        if (x < size_x && y > 0) {
            Tile t = tile_map[plane][x + 1][y - 1];
            if (t != null && t.ground_decoration != null && t.ground_decoration.node.normal != null) {
                method308(mesh, (Model) t.ground_decoration.node, 128, 0, -128, true);
            }
        }
    }

    public void method307(Model m, int l_x, int l_y, int max_plane, int j, int k) {
        boolean flag = true;
        int start_x = l_x;
        int end_x = l_x + j;
        int start_y = l_y - 1;
        int end_y = l_y + k;

        for (int z = max_plane; z <= max_plane + 1; z++) {
            if (z != this.size_z) {
                for (int x = start_x; x <= end_x; x++) {
                    if (x >= 0 && x < size_x) {
                        for (int y = start_y; y <= end_y; y++) {
                            if (y >= 0 && y < size_y && (!flag || x >= end_x || y >= end_y || y < l_y && x != l_x)) {
                                Tile t = tile_map[z][x][y];

                                if (t != null) {
                                    int v_avg = (height_map[z][x][y] + height_map[z][x + 1][y] + height_map[z][x][y + 1] + height_map[z][x + 1][y + 1]) / 4 - (height_map[max_plane][l_x][l_y] + height_map[max_plane][l_x + 1][l_y] + height_map[max_plane][l_x][l_y + 1] + height_map[max_plane][l_x + 1][l_y + 1]) / 4;
                                    WallLoc wl = t.wall;

                                    if (wl != null && wl.root != null && wl.root.normal != null) {
                                        method308(m, (Model) wl.root, (x - l_x) * 128 + (1 - j) * 64, v_avg, (y - l_y) * 128 + (1 - k) * 64, flag);
                                    }

                                    if (wl != null && wl.extension != null && wl.extension.normal != null) {
                                        method308(m, (Model) wl.extension, (x - l_x) * 128 + (1 - j) * 64, v_avg, (y - l_y) * 128 + (1 - k) * 64, flag);
                                    }

                                    for (int i = 0; i < t.loc_count; i++) {
                                        StaticLoc sl = t.locs[i];
                                        if (sl != null && sl.node != null && sl.node.normal != null) {
                                            int w = (sl.local_x1 - sl.local_x0) + 1;
                                            int l = (sl.local_y1 - sl.local_y0) + 1;
                                            method308(m, (Model) sl.node, (sl.local_x0 - l_x) * 128 + (w - j) * 64, v_avg, (sl.local_y0 - l_y) * 128 + (l - k) * 64, flag);
                                        }
                                    }

                                }
                            }
                        }

                    }
                }

                start_x--;
                flag = false;
            }
        }

    }

    public void method308(Model m1, Model m2, int x_offset, int y_offset, int z_offset, boolean flag) {
        anInt488++;

        int i = 0;
        short v_x[] = m2.vertex_x;
        int v_c = m2.vertex_count;

        // Loop through every vertex
        for (int j = 0; j < m1.vertex_count; j++) {
            Vertex v1 = ((Renderable) (m1)).normal[j];
            Vertex v2 = m1.vertices[j];

            if (v2.w != 0) {

                int y = m1.vertex_y[j] - y_offset;
                if (y <= m2.max_y) {

                    int x = m1.vertex_x[j] - x_offset;
                    if (x >= m2.min_x && x <= m2.max_x) {

                        int z = m1.vertex_z[j] - z_offset;
                        if (z >= m2.min_z && z <= m2.max_z) {

                            for (int k = 0; k < v_c; k++) {
                                Vertex v3 = ((Renderable) (m2)).normal[k];
                                Vertex v4 = m2.vertices[k];

                                if (x == v_x[k] && z == m2.vertex_z[k] && y == m2.vertex_y[k] && v4.w != 0) {
                                    v1.x += v4.x;
                                    v1.y += v4.y;
                                    v1.z += v4.z;
                                    v1.w += v4.w;

                                    v3.x += v2.x;
                                    v3.y += v2.y;
                                    v3.z += v2.z;
                                    v3.w += v2.w;

                                    i++;

                                    anIntArray486[j] = anInt488;
                                    anIntArray487[k] = anInt488;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (i < 3 || !flag) {
            return;
        }

        for (int k1 = 0; k1 < m1.triangle_count; k1++) {
            if (anIntArray486[m1.triangle_viewspace_a[k1]] == anInt488 && anIntArray486[m1.triangle_viewspace_b[k1]] == anInt488 && anIntArray486[m1.triangle_viewspace_c[k1]] == anInt488) {
                m1.triangle_info[k1] = -1;
            }
        }

        for (int l1 = 0; l1 < m2.triangle_count; l1++) {
            if (anIntArray487[m2.triangle_viewspace_a[l1]] == anInt488 && anIntArray487[m2.triangle_viewspace_b[l1]] == anInt488 && anIntArray487[m2.triangle_viewspace_c[l1]] == anInt488) {
                m2.triangle_info[l1] = -1;
            }
        }
    }

    public void method314(Tile tile, boolean flag) {
        tile_queue.push_back(tile);

        do {
            Tile _t;

            do {
                _t = (Tile) tile_queue.pop();
                if (_t == null) {
                    return;
                }
            } while (!_t.aBoolean1323);

            int x = _t.x;
            int y = _t.y;
            int z = _t.z;
            int plane = _t.plane;
            Tile[][] tiles = tile_map[z];

            if (_t.aBoolean1322) {
                if (flag) {
                    if (z > 0) {
                        Tile t = tile_map[z - 1][x][y];
                        if (t != null && t.aBoolean1323) {
                            continue;
                        }
                    }

                    if (x <= cam_local_x && x > min_visible_x) {
                        Tile t = tiles[x - 1][y];
                        if (t != null && t.aBoolean1323 && (t.aBoolean1322 || (_t.flags & 1) == 0)) {
                            continue;
                        }
                    }

                    if (x >= cam_local_x && x < max_visible_x - 1) {
                        Tile t = tiles[x + 1][y];
                        if (t != null && t.aBoolean1323 && (t.aBoolean1322 || (_t.flags & 4) == 0)) {
                            continue;
                        }
                    }

                    if (y <= cam_local_y && y > min_visible_y) {
                        Tile t = tiles[x][y - 1];
                        if (t != null && t.aBoolean1323 && (t.aBoolean1322 || (_t.flags & 8) == 0)) {
                            continue;
                        }
                    }

                    if (y >= cam_local_y && y < max_visible_y - 1) {
                        Tile t = tiles[x][y + 1];
                        if (t != null && t.aBoolean1323 && (t.aBoolean1322 || (_t.flags & 2) == 0)) {
                            continue;
                        }
                    }
                } else {
                    flag = true;
                }

                _t.aBoolean1322 = false;

                if (_t.bridge != null) {
                    Tile t = _t.bridge;

                    if (t.underlay != null) {
                        if (!is_tile_culled(0, x, y)) {
                            draw_underlay_tile(t.underlay, 0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, x, y);
                        }
                    } else if (t.overlay != null && !is_tile_culled(0, x, y)) {
                        draw_overlay_tile(t.overlay, x, y, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos);
                    }

                    WallLoc wl = t.wall;

                    if (wl != null) {
                        wl.root.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }

                    for (int i = 0; i < t.loc_count; i++) {
                        StaticLoc sl = t.locs[i];

                        if (sl != null) {
                            sl.node.render(sl.rotation, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, sl.x - cam_x, sl.y - cam_y, sl.z - cam_z, sl.uid);
                        }
                    }
                }

                boolean draw_item_pile = false;

                if (_t.underlay != null) {
                    if (!is_tile_culled(plane, x, y)) {
                        draw_item_pile = true;
                        draw_underlay_tile(_t.underlay, plane, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, x, y);
                    }
                } else if (_t.overlay != null && !is_tile_culled(plane, x, y)) {
                    draw_item_pile = true;
                    draw_overlay_tile(_t.overlay, x, y, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos);
                }

                int cam_dir = 0;
                int render_flags = 0;
                WallLoc wl = _t.wall;
                WallDecoration wd = _t.wall_decoration;

                if (wl != null || wd != null) {
                    if (cam_local_x == x) {
                        cam_dir++;
                    } else if (cam_local_x < x) {
                        cam_dir += 2;
                    }

                    if (cam_local_y == y) {
                        cam_dir += 3;
                    } else if (cam_local_y > y) {
                        cam_dir += 6;
                    }

                    render_flags = anIntArray478[cam_dir];
                    _t.anInt1328 = anIntArray480[cam_dir];

                    // Camera Direction (Relative to)
                    // 0: cam_x > x && cam_y < y	(South-east)
                    // 1: cam_x == x && cam_y < y	(South)
                    // 2: cam_x < x  && cam_y < y	(South-west)
                    // 3: cam_y == y && cam_x > x	(East)
                    // 4: cam_x == x && cam_y == y	(Above)
                    // 5: cam_x < x && cam_y == y	(West)
                    // 6: cam_y > y && cam_x > x	(North-east)
                    // 7: cam_x == x && cam_y > y	(North)
                    // 8: cam_x < x && cam_y > y	(North-west)

                    // 0x13 = 0x10	| 0x2	| 0x1
                    // 0x37 = 0x20	| 0x10	| 0x4	| 0x2	| 0x1
                    // 0x26 = 0x20	| 0x4	| 0x2
                    // 0x9B = 0x80	| 0x10	| 0x8	| 0x2	| 0x1
                    // 0xFF = 0x80	| 0x40	| 0x20	| 0x10	| 0x8 | 0x4 | 0x2 | 0x1
                    // 0x6E = 0x40	| 0x20	| 0x8	| 0x4	| 0x2
                    // 0x89 = 0x80	| 0x8	| 0x1
                    // 0xCD = 0x80	| 0x40	| 0x8	| 0x4	| 0x1
                    // 0x4C	= 0x40	| 0x8	| 0x4
                    //
                    //anIntArray478[] = { 0x13, 0x37, 0x26, 0x9B, 0xFF, 0x6E, 0x89, 0xCD, 0x4C };

                    // 160	= 0x80 | 0x20
                    // 192	= 0x80 | 0x40
                    // 80	= 0x40 | 0x10
                    // 96	= 0x40 | 0x20
                    // 0	= 0x0
                    // 144	= 0x80 | 0x10
                    // 80	= 0x40 | 0x10
                    // 48	= 0x20 | 0x10
                    // 160	= 0x80 | 0x20
                    //
                    //anIntArray479[] = { 160, 192, 80, 96, 0, 144, 80, 48, 160 };

                    // 76	= 0x40 | 0x8 | 0x4	(Ext-N, Root-E, Root-N)
                    // 8	= 0x8				(Root-E)
                    // 137	= 0x80 | 0x8 | 0x1	(Ext-E, Root-E, Root-S)
                    // 4	= 0x4				(Root-N)
                    // 0	= 0x0				(None)
                    // 1	= 0x1				(Root-S)
                    // 38	= 0x20 | 0x4 | 0x2	(Root-W, Root-N, Root-W)
                    // 2	= 0x2				(Root-W)
                    // 19	= 0x10 | 0x2 | 0x1	(Ext-S, Root-W, Root-S)
                    //
                    //anIntArray480[] = { 76, 8, 137, 4, 0, 1, 38, 2, 19 };

                    // South, West, North, East
                    // WALL_CORNER_FLAG = { 0x10, 0x20, 0x40, 0x80 };
                    // WALL_ROTATION_FLAG = { 0x1, 0x2, 0x4, 0x8 };
                }

                if (wl != null) {
                    if ((wl.rotation_flag & anIntArray479[cam_dir]) != 0) {
                        if (wl.rotation_flag == 0x10) {
                            _t.anInt1325 = 0x3; // 0x2 | 0x1
                            _t.anInt1326 = anIntArray481[cam_dir];
                            _t.anInt1327 = 0x3 - _t.anInt1326;
                        } else if (wl.rotation_flag == 0x20) {
                            _t.anInt1325 = 0x6; // 0x4 | 0x2
                            _t.anInt1326 = anIntArray482[cam_dir];
                            _t.anInt1327 = 0x6 - _t.anInt1326;
                        } else if (wl.rotation_flag == 0x40) {
                            _t.anInt1325 = 0xC; // 0x8 | 0x4
                            _t.anInt1326 = anIntArray483[cam_dir];
                            _t.anInt1327 = 0xC - _t.anInt1326;
                        } else {
                            _t.anInt1325 = 0x9; // 0x8 | 0x1
                            _t.anInt1326 = anIntArray484[cam_dir];
                            _t.anInt1327 = 0x9 - _t.anInt1326;
                        }
                    } else {
                        _t.anInt1325 = 0;
                    }

                    if ((wl.rotation_flag & render_flags) != 0 && !is_wall_culled(plane, x, y, wl.rotation_flag)) {
                        wl.root.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }

                    if ((wl.corner_flag & render_flags) != 0 && !is_wall_culled(plane, x, y, wl.corner_flag)) {
                        wl.extension.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }
                }

                if (wd != null && !is_culled(plane, x, y, wd.node.height)) {
                    if ((wd.flags & render_flags) != 0) {
                        wd.node.render(wd.rotation, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wd.x - cam_x, wd.y - cam_y, wd.z - cam_z, wd.uid);
                    } else if ((wd.flags & 0x300) != 0) {
                        int xd = wd.x - cam_x;
                        int zd = wd.z - cam_z;
                        int yd = wd.y - cam_y;
                        int rotation = wd.rotation;

                        int k9;
                        if (rotation == 1 || rotation == 2) {
                            k9 = -xd;
                        } else {
                            k9 = xd;
                        }

                        int k10;
                        if (rotation == 2 || rotation == 3) {
                            k10 = -yd;
                        } else {
                            k10 = yd;
                        }

                        if ((wd.flags & 0x100) != 0 && k10 < k9) {
                            int i11 = xd + anIntArray463[rotation];
                            int k11 = yd + anIntArray464[rotation];
                            wd.node.render(rotation * 512 + 256, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, i11, k11, zd, wd.uid);
                        }

                        if ((wd.flags & 0x200) != 0 && k10 > k9) {
                            int j11 = xd + anIntArray465[rotation];
                            int l11 = yd + anIntArray466[rotation];
                            wd.node.render(rotation * 512 + 1280 & 0x7ff, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, j11, l11, zd, wd.uid);
                        }
                    }
                }

                if (draw_item_pile) {
                    GroundDecoration d = _t.ground_decoration;

                    if (d != null) {
                        d.node.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, d.s_x - cam_x, d.s_y - cam_y, d.s_z - cam_z, d.uid);
                    }

                    ItemPile i = _t.item_pile;

                    if (i != null && i.off_z == 0) {
                        if (i.bottom != null) {
                            i.bottom.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, i.x - cam_x, i.y - cam_y, i.z - cam_z, i.uid);
                        }
                        if (i.middle != null) {
                            i.middle.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, i.x - cam_x, i.y - cam_y, i.z - cam_z, i.uid);
                        }
                        if (i.top != null) {
                            i.top.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, i.x - cam_x, i.y - cam_y, i.z - cam_z, i.uid);
                        }
                    }
                }

                int t_flags = _t.flags;

                if (t_flags != 0) {
                    if (x < cam_local_x && (t_flags & 4) != 0) {
                        Tile t1 = tiles[x + 1][y];

                        if (t1 != null && t1.aBoolean1323) {
                            tile_queue.push_back(t1);
                        }
                    }

                    if (y < cam_local_y && (t_flags & 2) != 0) {
                        Tile t1 = tiles[x][y + 1];

                        if (t1 != null && t1.aBoolean1323) {
                            tile_queue.push_back(t1);
                        }
                    }

                    if (x > cam_local_x && (t_flags & 1) != 0) {
                        Tile t1 = tiles[x - 1][y];

                        if (t1 != null && t1.aBoolean1323) {
                            tile_queue.push_back(t1);
                        }
                    }

                    if (y > cam_local_y && (t_flags & 8) != 0) {
                        Tile t1 = tiles[x][y - 1];

                        if (t1 != null && t1.aBoolean1323) {
                            tile_queue.push_back(t1);
                        }
                    }
                }
            }

            if (_t.anInt1325 != 0) {
                boolean flag2 = true;
                for (int k1 = 0; k1 < _t.loc_count; k1++) {
                    if (_t.locs[k1].cycle == cycle || (_t.loc_flag[k1] & _t.anInt1325) != _t.anInt1326) {
                        continue;
                    }
                    flag2 = false;
                    break;
                }

                if (flag2) {
                    WallLoc wl = _t.wall;

                    if (!is_wall_culled(plane, x, y, wl.rotation_flag)) {
                        wl.root.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }

                    _t.anInt1325 = 0;
                }
            }

            if (_t.aBoolean1324) {
                try {
                    int i1 = _t.loc_count;
                    _t.aBoolean1324 = false;
                    int l1 = 0;
                    label0:
                    for (int k2 = 0; k2 < i1; k2++) {
                        StaticLoc l = _t.locs[k2];

                        if (l.cycle == cycle) {
                            continue;
                        }

                        for (int piece_x = l.local_x0; piece_x <= l.local_x1; piece_x++) {
                            for (int piece_y = l.local_y0; piece_y <= l.local_y1; piece_y++) {
                                Tile t = tiles[piece_x][piece_y];
                                if (t.aBoolean1322) {
                                    _t.aBoolean1324 = true;
                                } else {
                                    if (t.anInt1325 == 0) {
                                        continue;
                                    }
                                    int flags = 0;

                                    if (piece_x > l.local_x0) {
                                        flags++;
                                    }

                                    if (piece_x < l.local_x1) {
                                        flags += 0x4;
                                    }

                                    if (piece_y > l.local_y0) {
                                        flags += 0x8;
                                    }

                                    if (piece_y < l.local_y1) {
                                        flags += 0x2;
                                    }

                                    if ((flags & t.anInt1325) != _t.anInt1327) {
                                        continue;
                                    }

                                    _t.aBoolean1324 = true;
                                }
                                continue label0;
                            }

                        }

                        aClass28Array462[l1++] = l;

                        int i5 = cam_local_x - l.local_x0;
                        int i6 = l.local_x1 - cam_local_x;

                        if (i6 > i5) {
                            i5 = i6;
                        }

                        int i7 = cam_local_y - l.local_y0;
                        int j8 = l.local_y1 - cam_local_y;

                        if (j8 > i7) {
                            l.anInt527 = i5 + j8;
                        } else {
                            l.anInt527 = i5 + i7;
                        }
                    }

                    while (l1 > 0) {
                        int i3 = -50;
                        int l3 = -1;

                        for (int j5 = 0; j5 < l1; j5++) {
                            StaticLoc sl = aClass28Array462[j5];

                            if (sl.cycle != cycle) {
                                if (sl.anInt527 > i3) {
                                    i3 = sl.anInt527;
                                    l3 = j5;
                                } else if (sl.anInt527 == i3) {
                                    int x_cam_diff = sl.x - cam_x;
                                    int y_cam_diff = sl.y - cam_y;
                                    int l9 = aClass28Array462[l3].x - cam_x;
                                    int l10 = aClass28Array462[l3].y - cam_y;

                                    if ((x_cam_diff * x_cam_diff + y_cam_diff * y_cam_diff) > l9 * l9 + l10 * l10) {
                                        l3 = j5;
                                    }
                                }
                            }
                        }

                        if (l3 == -1) {
                            break;
                        }

                        StaticLoc sl = aClass28Array462[l3];
                        sl.cycle = cycle;

                        if (!method323(plane, sl.local_x0, sl.local_x1, sl.local_y0, sl.local_y1, sl.node.height)) {
                            sl.node.render(sl.rotation, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, sl.x - cam_x, sl.y - cam_y, sl.z - cam_z, sl.uid);
                        }

                        for (int piece_x = sl.local_x0; piece_x <= sl.local_x1; piece_x++) {
                            for (int piece_y = sl.local_y0; piece_y <= sl.local_y1; piece_y++) {
                                Tile t = tiles[piece_x][piece_y];
                                if (t.anInt1325 != 0) {
                                    tile_queue.push_back(t);
                                } else if ((piece_x != x || piece_y != y) && t.aBoolean1323) {
                                    tile_queue.push_back(t);
                                }
                            }

                        }

                    }
                    if (_t.aBoolean1324) {
                        continue;
                    }
                } catch (Exception _ex) {
                    _t.aBoolean1324 = false;
                }
            }

            if (!_t.aBoolean1323 || _t.anInt1325 != 0) {
                continue;
            }

            if (x <= cam_local_x && x > min_visible_x) {
                Tile t = tiles[x - 1][y];
                if (t != null && t.aBoolean1323) {
                    continue;
                }
            }

            if (x >= cam_local_x && x < max_visible_x - 1) {
                Tile t = tiles[x + 1][y];
                if (t != null && t.aBoolean1323) {
                    continue;
                }
            }

            if (y <= cam_local_y && y > min_visible_y) {
                Tile t = tiles[x][y - 1];
                if (t != null && t.aBoolean1323) {
                    continue;
                }
            }

            if (y >= cam_local_y && y < max_visible_y - 1) {
                Tile t = tiles[x][y + 1];
                if (t != null && t.aBoolean1323) {
                    continue;
                }
            }

            _t.aBoolean1323 = false;
            anInt446--;

            ItemPile ip = _t.item_pile;

            if (ip != null && ip.off_z != 0) {
                if (ip.bottom != null) {
                    ip.bottom.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, ip.x - cam_x, ip.y - cam_y, ip.z - cam_z - ip.off_z, ip.uid);
                }

                if (ip.middle != null) {
                    ip.middle.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, ip.x - cam_x, ip.y - cam_y, ip.z - cam_z - ip.off_z, ip.uid);
                }

                if (ip.top != null) {
                    ip.top.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, ip.x - cam_x, ip.y - cam_y, ip.z - cam_z - ip.off_z, ip.uid);
                }
            }

            if (_t.anInt1328 != 0) {
                WallDecoration wd = _t.wall_decoration;
                if (wd != null && !is_culled(plane, x, y, wd.node.height)) {
                    if ((wd.flags & _t.anInt1328) != 0) {
                        wd.node.render(wd.rotation, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wd.x - cam_x, wd.y - cam_y, wd.z - cam_z, wd.uid);
                    } else if ((wd.flags & 0x300) != 0) {
                        int x_d = wd.x - cam_x;
                        int z_d = wd.z - cam_z;
                        int y_d = wd.y - cam_y;
                        int rotation = wd.rotation;

                        int j6;
                        if (rotation == 1 || rotation == 2) {
                            j6 = -x_d;
                        } else {
                            j6 = x_d;
                        }

                        int l7;
                        if (rotation == 2 || rotation == 3) {
                            l7 = -y_d;
                        } else {
                            l7 = y_d;
                        }

                        if ((wd.flags & 0x100) != 0 && l7 >= j6) {
                            int s_x = x_d + anIntArray463[rotation];
                            int s_y = y_d + anIntArray464[rotation];
                            wd.node.render(rotation * 512 + 256, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, s_x, s_y, z_d, wd.uid);
                        }

                        if ((wd.flags & 0x200) != 0 && l7 <= j6) {
                            int s_x = x_d + anIntArray465[rotation];
                            int s_y = y_d + anIntArray466[rotation];
                            wd.node.render(rotation * 512 + 1280 & 0x7ff, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, s_x, s_y, z_d, wd.uid);
                        }
                    }
                }

                WallLoc wl = _t.wall;

                if (wl != null) {
                    if ((wl.corner_flag & _t.anInt1328) != 0 && !is_wall_culled(plane, x, y, wl.corner_flag)) {
                        wl.extension.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }
                    if ((wl.rotation_flag & _t.anInt1328) != 0 && !is_wall_culled(plane, x, y, wl.rotation_flag)) {
                        wl.root.render(0, cam_p_sin, cam_p_cos, cam_y_sin, cam_y_cos, wl.s_x - cam_x, wl.s_y - cam_y, wl.s_z - cam_z, wl.uid);
                    }
                }
            }

            if (z < size_z - 1) {
                Tile t = tile_map[z + 1][x][y];
                if (t != null && t.aBoolean1323) {
                    tile_queue.push_back(t);
                }
            }

            if (x < cam_local_x) {
                Tile t = tiles[x + 1][y];
                if (t != null && t.aBoolean1323) {
                    tile_queue.push_back(t);
                }
            }

            if (y < cam_local_y) {
                Tile t = tiles[x][y + 1];
                if (t != null && t.aBoolean1323) {
                    tile_queue.push_back(t);
                }
            }

            if (x > cam_local_x) {
                Tile t = tiles[x - 1][y];
                if (t != null && t.aBoolean1323) {
                    tile_queue.push_back(t);
                }
            }

            if (y > cam_local_y) {
                Tile t = tiles[x][y - 1];
                if (t != null && t.aBoolean1323) {
                    tile_queue.push_back(t);
                }
            }
        } while (true);
    }

    public boolean method323(int plane, int x1, int x2, int y1, int y2, int z_off) {
        if (x1 == x2 && y1 == y2) {
            if (!is_tile_culled(plane, x1, y1)) {
                return false;
            }
            int x = x1 << 7;
            int y = y1 << 7;

            return is_culled(x + 1, y + 1, height_map[plane][x1][y1] - z_off) && is_culled((x + 128) - 1, y + 1, height_map[plane][x1 + 1][y1] - z_off) && is_culled((x + 128) - 1, (y + 128) - 1, height_map[plane][x1 + 1][y1 + 1] - z_off) && is_culled(x + 1, (y + 128) - 1, height_map[plane][x1][y1 + 1] - z_off);
        }

        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if (tile_cycle[plane][x][y] == -cycle) {
                    return false;
                }
            }
        }

        int x = (x1 << 7) + 1;
        int y = (y1 << 7) + 2;
        int z = height_map[plane][x1][y1] - z_off;

        if (!is_culled(x, y, z)) {
            return false;
        }

        int tile_end_x = (x2 << 7) - 1;

        if (!is_culled(tile_end_x, y, z)) {
            return false;
        }

        int tile_end_y = (y2 << 7) - 1;

        if (!is_culled(x, tile_end_y, z)) {
            return false;
        }

        return is_culled(tile_end_x, tile_end_y, z);
    }

    public void remove(StaticLoc sl) {
        for (int x = sl.local_x0; x <= sl.local_x1; x++) {
            for (int y = sl.local_y0; y <= sl.local_y1; y++) {
                Tile t = tile_map[sl.plane][x][y];

                if (t != null) {
                    for (int i = 0; i < t.loc_count; i++) {
                        if (t.locs[i] != sl) {
                            continue;
                        }

                        t.loc_count--;

                        for (int j = i; j < t.loc_count; j++) {
                            t.locs[j] = t.locs[j + 1];
                            t.loc_flag[j] = t.loc_flag[j + 1];
                        }

                        t.locs[t.loc_count] = null;
                        break;
                    }

                    t.flags = 0;

                    for (int i = 0; i < t.loc_count; i++) {
                        t.flags |= t.loc_flag[i];
                    }
                }
            }
        }
    }

    public void remove_ground_decoration(int x, int y, int z) {
        Tile t = tile_map[z][x][y];
        if (t == null) {
            return;
        }
        t.ground_decoration = null;
    }

    public void remove_item_pile(int plane, int x, int y) {
        Tile t = tile_map[plane][x][y];
        if (t == null) {
            return;
        } else {
            t.item_pile = null;
            return;
        }
    }

    public void remove_wall(int x, int z, int y) {
        Tile t = tile_map[z][x][y];
        if (t == null) {
            return;
        }
        t.wall = null;
    }

    public void remove_wall_decoration(int i, int y, int z, int x) {
        Tile t = tile_map[z][x][y];
        if (t == null) {
            return;
        }
        t.wall_decoration = null;
    }

    public void reset() {
        for (int z = 0; z < size_z; z++) {
            for (int x = 0; x < size_x; x++) {
                for (int y = 0; y < size_y; y++) {
                    tile_map[z][x][y] = null;
                }
            }
        }

        for (int x = 0; x < cull_plane_count; x++) {
            for (int y = 0; y < culling_box_count[x]; y++) {
                culling_boxes[x][y] = null;
            }
            culling_box_count[x] = 0;
        }

        for (int i = 0; i < loc_count; i++) {
            locs[i] = null;
        }

        loc_count = 0;
        for (int i = 0; i < aClass28Array462.length; i++) {
            aClass28Array462[i] = null;
        }
    }

    public void set_plane(int plane) {
        this.plane = plane;

        for (int x = 0; x < size_x; x++) {
            for (int y = 0; y < size_y; y++) {
                if (tile_map[plane][x][y] == null) {
                    tile_map[plane][x][y] = new Tile(plane, x, y);
                }
            }
        }
    }

    public void set_tile_underlay_color(int x, int y, int z, int hue, int saturation, int l_sw, int l_se, int l_ne, int l_nw) {
        UnderlayTile ut = this.tile_map[z][x][y].underlay;

        if (ut == null) {
            return;
        }

        int hs = (hue << 10) | (saturation << 7);

        ut.hsl_sw = hs + l_sw;
        ut.hsl_ne = hs + l_ne;
        ut.hsl_nw = hs + l_nw;
        ut.hsl_se = hs + l_se;
        ut.rgb = 0;
    }

    public void set_visible_planes(int plane, int x, int y, int max_plane) {
        Tile t = tile_map[plane][x][y];
        if (t == null) {
            return;
        }
        tile_map[plane][x][y].top_plane = max_plane;
    }

    public void set_wall_deco_margin(int x, int y, int z, int i) {
        Tile t = tile_map[z][x][y];

        if (t == null) {
            return;
        }

        WallDecoration deco = t.wall_decoration;

        if (deco == null) {
            return;
        }

        int s_x = x * 128 + 64;
        int s_y = y * 128 + 64;
        deco.x = s_x + ((deco.x - s_x) * i) / 16;
        deco.y = s_y + ((deco.y - s_y) * i) / 16;
    }

    public boolean tri_contains(int x, int y, int x1, int y1, int x2, int y2, int x3, int y3) {
        if (y < y1 && y < y2 && y < y3) {
            return false;
        }
        if (y > y1 && y > y2 && y > y3) {
            return false;
        }
        if (x < x1 && x < x2 && x < x3) {
            return false;
        }
        if (x > x1 && x > x2 && x > x3) {
            return false;
        }
        int i2 = (y - y1) * (x2 - x1) - (x - x1) * (y2 - y1);
        int j2 = (y - y3) * (x1 - x3) - (x - x3) * (y1 - y3);
        int k2 = (y - y2) * (x3 - x2) - (x - x2) * (y3 - y2);
        return i2 * k2 > 0 && k2 * j2 > 0;
    }

    public void update_culling() {
        int box_count = culling_box_count[occlusion_top_plane];
        CullingBox culls[] = culling_boxes[occlusion_top_plane];
        culling_position = 0;

        for (int i = 0; i < box_count; i++) {
            CullingBox c = culls[i];

            if (c.occlusion_type == 1) {
                int z_index = (c.local_min_x - cam_local_x) + 25;

                if (z_index < 0 || z_index > 50) {
                    continue;
                }

                int yaw_index = (c.local_min_y - cam_local_y) + 25;

                if (yaw_index < 0) {
                    yaw_index = 0;
                }

                int dy = (c.local_max_y - cam_local_y) + 25;

                if (dy > 50) {
                    dy = 50;
                }

                boolean flag = false;

                while (yaw_index <= dy) {
                    if (culling_map[z_index][yaw_index++]) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    continue;
                }

                int dx = cam_x - c.min_x;

                if (dx > 32) {
                    c.culling_mode = 1;
                } else {
                    if (dx >= -32) {
                        continue;
                    }
                    c.culling_mode = 2;
                    dx = -dx;
                }

                c.anInt801 = (c.min_y - cam_y << 8) / dx;
                c.anInt802 = (c.max_y - cam_y << 8) / dx;
                c.anInt803 = (c.max_z - cam_z << 8) / dx;
                c.anInt804 = (c.min_z - cam_z << 8) / dx;
                cull_boxes[culling_position++] = c;
                continue;
            }

            if (c.occlusion_type == 2) {
                int yaw_index = (c.local_min_y - cam_local_y) + 25;

                if (yaw_index < 0 || yaw_index > 50) {
                    continue;
                }

                int z_index = (c.local_min_x - cam_local_x) + 25;

                if (z_index < 0) {
                    z_index = 0;
                }

                int dx = (c.local_max_x - cam_local_x) + 25;

                if (dx > 50) {
                    dx = 50;
                }

                boolean flag1 = false;

                while (z_index <= dx) {
                    if (culling_map[z_index++][yaw_index]) {
                        flag1 = true;
                        break;
                    }
                }

                if (!flag1) {
                    continue;
                }

                int dy = cam_y - c.min_y;

                if (dy > 32) {
                    c.culling_mode = 3;
                } else {
                    if (dy >= -32) {
                        continue;
                    }
                    c.culling_mode = 4;
                    dy = -dy;
                }

                c.anInt799 = (c.min_x - cam_x << 8) / dy;
                c.anInt800 = (c.max_x - cam_x << 8) / dy;
                c.anInt803 = (c.max_z - cam_z << 8) / dy;
                c.anInt804 = (c.min_z - cam_z << 8) / dy;

                cull_boxes[culling_position++] = c;
            } else if (c.occlusion_type == 4) {
                int dz = c.max_z - cam_z;

                if (dz > 128) {
                    int start_yaw = (c.local_min_y - cam_local_y) + 25;

                    if (start_yaw < 0) {
                        start_yaw = 0;
                    }

                    int end_yaw = (c.local_max_y - cam_local_y) + 25;

                    if (end_yaw > 50) {
                        end_yaw = 50;
                    }

                    if (start_yaw <= end_yaw) {
                        int start_z = (c.local_min_x - cam_local_x) + 25;

                        if (start_z < 0) {
                            start_z = 0;
                        }

                        int end_z = (c.local_max_x - cam_local_x) + 25;

                        if (end_z > 50) {
                            end_z = 50;
                        }

                        boolean flag2 = false;

                        label0:
                        {
                            for (int z_index = start_z; z_index <= end_z; z_index++) {
                                for (int yaw_index = start_yaw; yaw_index <= end_yaw; yaw_index++) {

                                    if (!culling_map[z_index][yaw_index]) {
                                        continue;
                                    }

                                    flag2 = true;
                                    break label0;
                                }
                            }
                        }

                        if (flag2) {
                            c.culling_mode = 5;
                            c.anInt799 = (c.min_x - cam_x << 8) / dz;
                            c.anInt800 = (c.max_x - cam_x << 8) / dz;
                            c.anInt801 = (c.min_y - cam_y << 8) / dz;
                            c.anInt802 = (c.max_y - cam_y << 8) / dz;
                            cull_boxes[culling_position++] = c;
                        }
                    }
                }
            }
        }
    }

}
