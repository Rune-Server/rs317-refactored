package rs.scene;

import rs.Game;
import rs.cache.impl.Floor;
import rs.cache.model.LocConfig;
import rs.io.Buffer;
import rs.io.OnDemand;
import rs.media.Canvas3D;
import rs.node.impl.Renderable;
import rs.scene.model.CollisionMap;
import rs.scene.model.Landscape;
import rs.scene.model.Loc;
import rs.scene.model.Model;
import rs.util.ChunkUtil;

public class Scene {

    public static int hue_randomizer = (int) (Math.random() * 9.0) - 4;
    public static int lightness_randomizer = (int) (Math.random() * 9.0) - 4;
    public static int min_plane = 99;
    public static int plane_at_build;
    public static final int[] ROT_X_DELTA = {1, 0, -1, 0};
    public static final int[] ROT_Y_DELTA = {0, -1, 0, 1};
    public static final int[] WALL_EXT_DRAW_FLAGS = {0x10, 0x20, 0x40, 0x80};
    public static final int[] WALL_ROOT_DRAW_FLAGS = {0x1, 0x2, 0x4, 0x8};
    public static final byte WALL_SHADOW_INTENSITY = 50;

    public static void add_loc(Landscape l, int x, int y, int plane, int loc_index, int loc_type, int loc_rotation, int vertex_plane, CollisionMap cm, short[][][] height_map) {
        int v_sw = height_map[vertex_plane][x][y];
        int v_se = height_map[vertex_plane][x + 1][y];
        int v_ne = height_map[vertex_plane][x + 1][y + 1];
        int v_nw = height_map[vertex_plane][x][y + 1];
        int v_avg = (v_sw + v_se + v_ne + v_nw) >> 2;

        LocConfig c = LocConfig.get(loc_index);
        int uid = 0x40000000 | (loc_index << 14) | (y << 7) | x;

        if (!c.is_static) {
            uid += Integer.MIN_VALUE;
        }

        byte loc_info = (byte) ((loc_rotation << 6) + loc_type);

        if (loc_type == 22) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(22, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, 22, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add_ground_deco(x, y, v_avg, plane, r, uid, loc_info);

            if (c.has_collisions && c.is_static) {
                cm.set_solid(x, y);
            }
        } else if (loc_type == 10 || loc_type == 11) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(10, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, 10, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            if (r != null) {
                int rotation = 0;

                if (loc_type == 11) {
                    rotation += 512;
                }

                int size_y;
                int size_x;

                if (loc_rotation == 1 || loc_rotation == 3) {
                    size_y = c.size_y;
                    size_x = c.size_x;
                } else {
                    size_y = c.size_x;
                    size_x = c.size_y;
                }

                l.add(r, x, y, plane, size_x, size_y, v_avg, loc_info, rotation, uid);
            }

            if (c.has_collisions) {
                cm.add_loc(x, y, c.size_x, c.size_y, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type >= 12) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(loc_type, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, loc_type, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add(r, x, y, plane, 1, 1, v_avg, loc_info, 0, uid);

            if (c.has_collisions) {
                cm.add_loc(x, y, c.size_x, c.size_y, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type == 0) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(0, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, 0, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add_wall(r, null, x, y, v_avg, plane, WALL_ROOT_DRAW_FLAGS[loc_rotation], 0, loc_info, true, uid);

            if (c.has_collisions) {
                cm.add_wall(x, y, loc_type, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type == 1) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(1, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, 1, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add_wall(r, null, x, y, v_avg, plane, WALL_EXT_DRAW_FLAGS[loc_rotation], 0, loc_info, true, uid);

            if (c.has_collisions) {
                cm.add_wall(x, y, loc_type, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type == 2) {
            int next_rotation = loc_rotation + 1 & 0x3;
            Renderable r1;
            Renderable r2;

            if (c.seq_index == -1 && c.override_index == null) {
                r1 = c.get_model(2, 4 + loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
                r2 = c.get_model(2, next_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r1 = new Loc(loc_index, 4 + loc_rotation, 2, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                r2 = new Loc(loc_index, next_rotation, 2, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add_wall(r1, r2, x, y, v_avg, plane, WALL_ROOT_DRAW_FLAGS[loc_rotation], WALL_ROOT_DRAW_FLAGS[next_rotation], loc_info, true, uid);

            if (c.has_collisions) {
                cm.add_wall(x, y, loc_type, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type == 3) {
            Renderable r;

            if (c.seq_index == -1 && c.override_index == null) {
                r = c.get_model(3, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                r = new Loc(loc_index, loc_rotation, 3, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add_wall(r, null, x, y, v_avg, plane, WALL_EXT_DRAW_FLAGS[loc_rotation], 0, loc_info, true, uid);

            if (c.has_collisions) {
                cm.add_wall(x, y, loc_type, loc_rotation, c.blocks_projectiles);
            }
        } else if (loc_type == 9) {
            Renderable node;

            if (c.seq_index == -1 && c.override_index == null) {
                node = c.get_model(loc_type, loc_rotation, v_sw, v_se, v_ne, v_nw, -1);
            } else {
                node = new Loc(loc_index, loc_rotation, loc_type, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
            }

            l.add(node, x, y, plane, 1, 1, v_avg, loc_info, 0, uid);

            if (c.has_collisions) {
                cm.add_loc(x, y, c.size_x, c.size_y, loc_rotation, c.blocks_projectiles);
            }
        } else {
            if (c.adjust_to_terrain) {
                if (loc_rotation == 1) {
                    int v_sw2 = v_nw;
                    v_nw = v_ne;
                    v_ne = v_se;
                    v_se = v_sw;
                    v_sw = v_sw2;
                } else if (loc_rotation == 2) {
                    int v_sw2 = v_nw;
                    v_nw = v_se;
                    v_se = v_sw2;
                    v_sw2 = v_ne;
                    v_ne = v_sw;
                    v_sw = v_sw2;
                } else if (loc_rotation == 3) {
                    int v_ne2 = v_nw;
                    v_nw = v_sw;
                    v_sw = v_se;
                    v_se = v_ne;
                    v_ne = v_ne2;
                }
            }
            if (loc_type == 4) {
                Renderable node;

                if (c.seq_index == -1 && c.override_index == null) {
                    node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                l.add_wall_deco(node, x, y, plane, 0, 0, v_avg, loc_rotation * 512, loc_info, WALL_ROOT_DRAW_FLAGS[loc_rotation], uid);
            } else if (loc_type == 5) {
                int width = 16;
                int wall_uid = l.get_wall_uid(plane, x, y);

                if (wall_uid > 0) {
                    width = LocConfig.get(wall_uid >> 14 & 0x7fff).wall_width;
                }

                Renderable node;

                if (c.seq_index == -1 && c.override_index == null) {
                    node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                l.add_wall_deco(node, x, y, plane, ROT_X_DELTA[loc_rotation] * width, ROT_Y_DELTA[loc_rotation] * width, v_avg, loc_rotation * 512, loc_info, WALL_ROOT_DRAW_FLAGS[loc_rotation], uid);
            } else if (loc_type == 6) {
                Renderable node;

                if (c.seq_index == -1 && c.override_index == null) {
                    node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                l.add_wall_deco(node, x, y, plane, 0, 0, v_avg, loc_rotation, loc_info, 256, uid);
            } else if (loc_type == 7) {
                Renderable node;

                if (c.seq_index == -1 && c.override_index == null) {
                    node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                l.add_wall_deco(node, x, y, plane, 0, 0, v_avg, loc_rotation, loc_info, 512, uid);
            } else if (loc_type == 8) {
                Renderable node;

                if (c.seq_index == -1 && c.override_index == null) {
                    node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                l.add_wall_deco(node, x, y, plane, 0, 0, v_avg, loc_rotation, loc_info, 768, uid);
            }
        }
    }

    public static int get_lerped_cosine(int a, int b, int x, int frequency) {
        int f = (65536 - Canvas3D.cos[x * 1024 / frequency] >> 1);
        return (a * (65536 - f) >> 16) + (b * f >> 16);
    }

    public static int get_noise(int a, int b, int amplitude) {
        int x = a / amplitude;
        int x1 = a & amplitude - 1;
        int y = b / amplitude;
        int x2 = b & amplitude - 1;
        int a1 = get_noise_2d(x, y);
        int b1 = get_noise_2d(x + 1, y);
        int a2 = get_noise_2d(x, y + 1);
        int b2 = get_noise_2d(x + 1, y + 1);
        int a3 = get_lerped_cosine(a1, b1, x1, amplitude);
        int b3 = get_lerped_cosine(a2, b2, x1, amplitude);
        return get_lerped_cosine(a3, b3, x2, amplitude);
    }

    public static int get_noise_2d(int x, int y) {
        int a = (get_perlin_noise(x - 1, y - 1) + get_perlin_noise(x + 1, y - 1) + get_perlin_noise(x - 1, y + 1) + get_perlin_noise(x + 1, y + 1));
        int b = (get_perlin_noise(x - 1, y) + get_perlin_noise(x + 1, y) + get_perlin_noise(x, y - 1) + get_perlin_noise(x, y + 1));
        int c = get_perlin_noise(x, y);
        return a / 16 + b / 8 + c / 4;
    }

    public static int get_noise_height(int x, int y) {
        int height = (get_noise(x + 45365, y + 91923, 4) - 128 + (get_noise(x + 10294, y + 37821, 2) - 128 >> 1) + (get_noise(x, y, 1) - 128 >> 2));
        height = (int) ((double) height * 0.3) + 35;

        if (height < 10) {
            height = 10;
        } else if (height > 60) {
            height = 60;
        }

        return height;
    }

    public static int get_perlin_noise(int x, int y) {
        int a = x + y * 57;
        a = a << 13 ^ a;
        int b = a * (a * a * 15731 + 789221) + 1376312589 & 0x7fffffff;
        return b >> 19 & 0xFF;
    }

    public static boolean loc_loaded(int loc_index, int loc_type) {
        LocConfig config = LocConfig.get(loc_index);

        if (loc_type == 11) {
            loc_type = 10;
        }

        if (loc_type >= 5 && loc_type <= 8) {
            loc_type = 4;
        }

        return config.is_valid_model(loc_type);
    }

    public static boolean locs_fully_loaded(int map_x, int map_y, byte[] landscape_payload) {
        boolean valid = true;
        Buffer s = new Buffer(landscape_payload);
        int loc_index = -1;

        for (; ; ) {
            int i = s.get_usmart();

            if (i == 0) {
                break;
            }

            loc_index += i;
            int xy = 0;
            boolean verify_next = false;

            for (; ; ) {
                if (verify_next) {
                    int lsb = s.get_usmart();

                    if (lsb == 0) {
                        break;
                    }

                    s.get_ubyte();
                } else {
                    int msb = s.get_usmart();

                    if (msb == 0) {
                        break;
                    }

                    xy += msb - 1;
                    int loc_y = xy & 0x3f;
                    int loc_x = xy >> 6 & 0x3f;
                    int loc_type = s.get_ubyte() >> 2;
                    int x = loc_x + map_x;
                    int y = loc_y + map_y;

                    if (x > 0 && y > 0 && x < 103 && y < 103) {
                        LocConfig config = LocConfig.get(loc_index);

                        if (loc_type != 22 || !Game.low_detail || config.is_static || config.is_decoration) {
                            valid &= config.has_valid_model();
                            verify_next = true;
                        }
                    }
                }
            }
        }
        return valid;
    }

    public static void request_loc_models(Buffer b, OnDemand requester) {
        int index = -1;
        for (; ; ) {
            int lsb = b.get_usmart();

            if (lsb == 0) {
                break;
            }

            index += lsb;
            LocConfig config = LocConfig.get(index);
            config.request_models(requester);

            for (; ; ) {
                int i = b.get_usmart();
                if (i == 0) {
                    break;
                }
                b.get_ubyte();
            }
        }
    }

    public static int set_hsl_lightness(int hsl, int l) {
        if (hsl == -1) {
            return 12345678;
        }

        l = l * (hsl & 0x7f) / 128;

        if (l < 2) {
            l = 2;
        } else if (l > 126) {
            l = 126;
        }

        return (hsl & 0xff80) + l;
    }

    public static int trim_hsl(int hue, int saturation, int lightness) {
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

    public int[] blended_direction;
    public int[] blended_hue;
    public int[] blended_hue_divisor;
    public int[] blended_lightless;
    public int[] blended_saturation;
    public short[][][] height_map;
    public int landscape_size_x;
    public int landscape_size_y;
    public int[][] light_map;
    public byte[][][] overlay_flo_index;
    public byte[][][] overlay_rotation;
    public byte[][][] overlay_shape;
    public byte[][][] render_flags;
    public byte[][][] shadow_map;
    public int[][][] tile_culling_bitsets;
    public byte[][][] underlay_flo_index;

    public Scene(int size_x, int size_y, short[][][] height_map, byte[][][] render_flags) {
        Scene.min_plane = 99;
        this.landscape_size_x = size_x;
        this.landscape_size_y = size_y;
        this.height_map = height_map;
        this.render_flags = render_flags;
        this.underlay_flo_index = new byte[4][landscape_size_x][landscape_size_y];
        this.overlay_flo_index = new byte[4][landscape_size_x][landscape_size_y];
        this.overlay_shape = new byte[4][landscape_size_x][landscape_size_y];
        this.overlay_rotation = new byte[4][landscape_size_x][landscape_size_y];
        this.tile_culling_bitsets = new int[4][landscape_size_x + 1][landscape_size_y + 1];
        this.shadow_map = new byte[4][landscape_size_x + 1][landscape_size_y + 1];
        this.light_map = new int[landscape_size_x + 1][landscape_size_y + 1];
        this.blended_hue = new int[landscape_size_y];
        this.blended_saturation = new int[landscape_size_y];
        this.blended_lightless = new int[landscape_size_y];
        this.blended_hue_divisor = new int[landscape_size_y];
        this.blended_direction = new int[landscape_size_y];
    }

    public void add_loc(Landscape landscape, CollisionMap collision, int loc_index, int type, int x, int y, int plane, int rotation) {
        if (!Game.low_detail || (render_flags[0][x][y] & 0x2) != 0 || ((render_flags[plane][x][y] & 0x10) == 0 && set_visibility_plane(plane, x, y) == plane_at_build)) {
            if (plane < min_plane) {
                min_plane = plane;
            }

            int v_sw = height_map[plane][x][y];
            int v_se = height_map[plane][x + 1][y];
            int v_ne = height_map[plane][x + 1][y + 1];
            int v_nw = height_map[plane][x][y + 1];
            int v_avg = (v_sw + v_se + v_ne + v_nw) >> 2;

            LocConfig c = LocConfig.get(loc_index);
            int uid = 0x40000000 | (loc_index << 14) | (y << 7) | x;

            if (!c.is_static) {
                uid += Integer.MIN_VALUE;
            }

            byte arrangement = (byte) ((rotation << 6) + type);

            if (type == 22) {
                if (!Game.low_detail || c.is_static || c.is_decoration) {
                    Renderable r;

                    if (c.seq_index == -1 && c.override_index == null) {
                        r = c.get_model(22, rotation, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        r = new Loc(loc_index, rotation, 22, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_ground_deco(x, y, v_avg, plane, r, uid, arrangement);

                    if (c.has_collisions && c.is_static && collision != null) {
                        collision.set_solid(x, y);
                    }

                }
            } else if (type == 10 || type == 11) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(10, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, 10, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                if (r != null) {
                    int angle = 0;
                    int size_y;
                    int size_x;

                    if (type == 11) {
                        angle += 256;
                    }

                    if (rotation == 1 || rotation == 3) {
                        size_y = c.size_y;
                        size_x = c.size_x;
                    } else {
                        size_y = c.size_x;
                        size_x = c.size_y;
                    }

                    if (landscape.add(r, x, y, plane, size_x, size_y, v_avg, arrangement, angle, uid) && c.casts_shadow) {
                        Model mesh;

                        if (r instanceof Model) {
                            mesh = (Model) r;
                        } else {
                            mesh = c.get_model(10, rotation, v_sw, v_se, v_ne, v_nw, -1);
                        }

                        if (mesh != null) {
                            for (int x_off = 0; x_off <= size_y; x_off++) {
                                for (int y_off = 0; y_off <= size_x; y_off++) {
                                    int i = mesh.max_horizon / 4;

                                    if (i > 30) {
                                        i = 30;
                                    }

                                    if (i > (shadow_map[plane][x + x_off][y + y_off])) {
                                        shadow_map[plane][x + x_off][y + y_off] = (byte) i;
                                    }
                                }
                            }
                        }

                    }
                }

                if (c.has_collisions && collision != null) {
                    collision.add_loc(x, y, c.size_x, c.size_y, rotation, c.blocks_projectiles);
                }

            } else if (type >= 12) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(type, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, type, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add(r, x, y, plane, 1, 1, v_avg, arrangement, 0, uid);

                if (type >= 12 && type <= 17 && type != 13 && plane > 0) {
                    tile_culling_bitsets[plane][x][y] |= 0x924;
                }

                if (c.has_collisions && collision != null) {
                    collision.add_loc(x, y, c.size_x, c.size_y, rotation, c.blocks_projectiles);
                }

            } else if (type == 0) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(0, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, 0, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add_wall(r, null, x, y, v_avg, plane, WALL_ROOT_DRAW_FLAGS[rotation], 0, arrangement, true, uid);

                if (rotation == 0) {
                    if (c.casts_shadow) {
                        shadow_map[plane][x][y] = WALL_SHADOW_INTENSITY;
                        shadow_map[plane][x][y + 1] = WALL_SHADOW_INTENSITY;
                    }

                    if (c.is_solid) {
                        tile_culling_bitsets[plane][x][y] |= 0x249;
                    }
                } else if (rotation == 1) {
                    if (c.casts_shadow) {
                        shadow_map[plane][x][y + 1] = WALL_SHADOW_INTENSITY;
                        shadow_map[plane][x + 1][y + 1] = WALL_SHADOW_INTENSITY;
                    }

                    if (c.is_solid) {
                        tile_culling_bitsets[plane][x][y + 1] |= 0x492;
                    }
                } else if (rotation == 2) {
                    if (c.casts_shadow) {
                        shadow_map[plane][x + 1][y] = WALL_SHADOW_INTENSITY;
                        shadow_map[plane][x + 1][y + 1] = WALL_SHADOW_INTENSITY;
                    }

                    if (c.is_solid) {
                        tile_culling_bitsets[plane][x + 1][y] |= 0x249;
                    }
                } else if (rotation == 3) {
                    if (c.casts_shadow) {
                        shadow_map[plane][x][y] = WALL_SHADOW_INTENSITY;
                        shadow_map[plane][x + 1][y] = WALL_SHADOW_INTENSITY;
                    }

                    if (c.is_solid) {
                        tile_culling_bitsets[plane][x][y] |= 0x492;
                    }
                }

                if (c.has_collisions && collision != null) {
                    collision.add_wall(x, y, type, rotation, c.blocks_projectiles);
                }

                if (c.wall_width != 16) {
                    landscape.set_wall_deco_margin(x, y, plane, c.wall_width);
                }

            } else if (type == 1) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(1, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, 1, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add_wall(r, null, x, y, v_avg, plane, WALL_EXT_DRAW_FLAGS[rotation], 0, arrangement, true, uid);

                if (c.casts_shadow) {
                    if (rotation == 0) {
                        shadow_map[plane][x][y + 1] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 1) {
                        shadow_map[plane][x + 1][y + 1] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 2) {
                        shadow_map[plane][x + 1][y] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 3) {
                        shadow_map[plane][x][y] = WALL_SHADOW_INTENSITY;
                    }
                }

                if (c.has_collisions && collision != null) {
                    collision.add_wall(x, y, type, rotation, c.blocks_projectiles);
                }
            } else if (type == 2) {
                int next_rotation = rotation + 1 & 0x3;
                Renderable r1;
                Renderable r2;

                if (c.seq_index == -1 && c.override_index == null) {
                    r1 = c.get_model(2, 4 + rotation, v_sw, v_se, v_ne, v_nw, -1);
                    r2 = c.get_model(2, next_rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r1 = new Loc(loc_index, 4 + rotation, 2, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    r2 = new Loc(loc_index, next_rotation, 2, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add_wall(r1, r2, x, y, v_avg, plane, WALL_ROOT_DRAW_FLAGS[rotation], WALL_ROOT_DRAW_FLAGS[next_rotation], arrangement, true, uid);

                if (c.is_solid) {
                    if (rotation == 0) {
                        tile_culling_bitsets[plane][x][y] |= 0x249;
                        tile_culling_bitsets[plane][x][y + 1] |= 0x492;
                    } else if (rotation == 1) {
                        tile_culling_bitsets[plane][x][y + 1] |= 0x492;
                        tile_culling_bitsets[plane][x + 1][y] |= 0x249;
                    } else if (rotation == 2) {
                        tile_culling_bitsets[plane][x + 1][y] |= 0x249;
                        tile_culling_bitsets[plane][x][y] |= 0x492;
                    } else if (rotation == 3) {
                        tile_culling_bitsets[plane][x][y] |= 0x492;
                        tile_culling_bitsets[plane][x][y] |= 0x249;
                    }
                }

                if (c.has_collisions && collision != null) {
                    collision.add_wall(x, y, type, rotation, c.blocks_projectiles);
                }

                if (c.wall_width != 16) {
                    landscape.set_wall_deco_margin(x, y, plane, c.wall_width);
                }
            } else if (type == 3) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(3, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, 3, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add_wall(r, null, x, y, v_avg, plane, WALL_EXT_DRAW_FLAGS[rotation], 0, arrangement, true, uid);

                if (c.casts_shadow) {
                    if (rotation == 0) {
                        shadow_map[plane][x][y + 1] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 1) {
                        shadow_map[plane][x + 1][y + 1] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 2) {
                        shadow_map[plane][x + 1][y] = WALL_SHADOW_INTENSITY;
                    } else if (rotation == 3) {
                        shadow_map[plane][x][y] = WALL_SHADOW_INTENSITY;
                    }
                }

                if (c.has_collisions && collision != null) {
                    collision.add_wall(x, y, type, rotation, c.blocks_projectiles);
                }

            } else if (type == 9) {
                Renderable r;

                if (c.seq_index == -1 && c.override_index == null) {
                    r = c.get_model(type, rotation, v_sw, v_se, v_ne, v_nw, -1);
                } else {
                    r = new Loc(loc_index, rotation, type, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                }

                landscape.add(r, x, y, plane, 1, 1, v_avg, arrangement, 0, uid);

                if (c.has_collisions && collision != null) {
                    collision.add_loc(x, y, c.size_x, c.size_y, rotation, c.blocks_projectiles);
                }
            } else {
                if (c.adjust_to_terrain) {
                    if (rotation == 1) {
                        int new_v_sw = v_nw;
                        v_nw = v_ne;
                        v_ne = v_se;
                        v_se = v_sw;
                        v_sw = new_v_sw;
                    } else if (rotation == 2) {
                        int new_v_sw = v_nw;
                        v_nw = v_se;
                        v_se = new_v_sw;
                        new_v_sw = v_ne;
                        v_ne = v_sw;
                        v_sw = new_v_sw;
                    } else if (rotation == 3) {
                        int new_v_sw = v_nw;
                        v_nw = v_sw;
                        v_sw = v_se;
                        v_se = v_ne;
                        v_ne = new_v_sw;
                    }
                }

                if (type == 4) {
                    Renderable r;

                    if (c.seq_index == -1 && c.override_index == null) {
                        r = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        r = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_wall_deco(r, x, y, plane, 0, 0, v_avg, rotation * 512, arrangement, WALL_ROOT_DRAW_FLAGS[rotation], uid);
                } else if (type == 5) {
                    int width = 16;
                    int wall_uid = landscape.get_wall_uid(plane, x, y);

                    if (wall_uid > 0) {
                        width = LocConfig.get(wall_uid >> 14 & 0x7fff).wall_width;
                    }

                    Renderable r;

                    if (c.seq_index == -1 && c.override_index == null) {
                        r = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        r = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_wall_deco(r, x, y, plane, ROT_X_DELTA[rotation] * width, ROT_Y_DELTA[rotation] * width, v_avg, rotation * 512, arrangement, WALL_ROOT_DRAW_FLAGS[rotation], uid);
                } else if (type == 6) {
                    Renderable node;

                    if (c.seq_index == -1 && c.override_index == null) {
                        node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_wall_deco(node, x, y, plane, 0, 0, v_avg, rotation, arrangement, 0x100, uid);
                } else if (type == 7) {
                    Renderable node;

                    if (c.seq_index == -1 && c.override_index == null) {
                        node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_wall_deco(node, x, y, plane, 0, 0, v_avg, rotation, arrangement, 0x200, uid);
                } else if (type == 8) {
                    Renderable node;

                    if (c.seq_index == -1 && c.override_index == null) {
                        node = c.get_model(4, 0, v_sw, v_se, v_ne, v_nw, -1);
                    } else {
                        node = new Loc(loc_index, 0, 4, v_se, v_ne, v_sw, v_nw, c.seq_index, true);
                    }

                    landscape.add_wall_deco(node, x, y, plane, 0, 0, v_avg, rotation, arrangement, 0x300, uid);
                }
            }
        }
    }

    public void create_land_mesh(CollisionMap[] cm, Landscape l) {
        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 104; x++) {
                for (int z = 0; z < 104; z++) {
                    if ((render_flags[plane][x][z] & 0x1) == 1) {
                        int plane_ = plane;

                        if ((render_flags[1][x][z] & 0x2) == 2) {
                            plane_--;
                        }

                        if (plane_ >= 0) {
                            cm[plane_].set_solid(x, z);
                        }
                    }
                }
            }
        }

        hue_randomizer += (int) (Math.random() * 5.0) - 2;

        if (hue_randomizer < -8) {
            hue_randomizer = -8;
        }

        if (hue_randomizer > 8) {
            hue_randomizer = 8;
        }

        lightness_randomizer += (int) (Math.random() * 5.0) - 2;

        if (lightness_randomizer < -16) {
            lightness_randomizer = -16;
        }

        if (lightness_randomizer > 16) {
            lightness_randomizer = 16;
        }

        for (int plane = 0; plane < 4; plane++) {
            byte[][] shadow_intensity = shadow_map[plane];
            int initial_light_intensity = 96;
            int specular_factor = 768;
            int light_x = -50;
            int light_z = -10;
            int light_y = -50;
            int light_length = (int) Math.sqrt((double) (light_x * light_x + light_z * light_z + light_y * light_y));
            int specular_distribution = specular_factor * light_length >> 8;

            for (int y = 1; y < landscape_size_y - 1; y++) {
                for (int x = 1; x < landscape_size_x - 1; x++) {
                    int x_height_diff = (height_map[plane][x + 1][y] - height_map[plane][x - 1][y]);
                    int y_height_diff = (height_map[plane][x][y + 1] - height_map[plane][x][y - 1]);
                    int normal_length = (int) Math.sqrt((double) (x_height_diff * x_height_diff + 65536 + y_height_diff * y_height_diff));
                    int normalized_x = (x_height_diff << 8) / normal_length;
                    int normalized_z = 65536 / normal_length;
                    int normalized_y = (y_height_diff << 8) / normal_length;
                    int intensity = initial_light_intensity + (light_x * normalized_x + light_z * normalized_z + light_y * normalized_y) / specular_distribution;
                    int weighted_shadow_intensity = ((shadow_intensity[x - 1][y] >> 2) + (shadow_intensity[x + 1][y] >> 3) + (shadow_intensity[x][y - 1] >> 2) + (shadow_intensity[x][y + 1] >> 3) + (shadow_intensity[x][y] >> 1));
                    light_map[x][y] = intensity - weighted_shadow_intensity;
                }
            }

            for (int y = 0; y < landscape_size_y; y++) {
                blended_hue[y] = 0;
                blended_saturation[y] = 0;
                blended_lightless[y] = 0;
                blended_hue_divisor[y] = 0;
                blended_direction[y] = 0;
            }

            for (int x = -5; x < landscape_size_x + 5; x++) {
                for (int y = 0; y < landscape_size_y; y++) {
                    int x_positive_offset = x + 5;
                    if (x_positive_offset >= 0 && x_positive_offset < landscape_size_x) {
                        int flo_index = underlay_flo_index[plane][x_positive_offset][y] & 0xFF;
                        if (flo_index > 0) {
                            Floor floor = Floor.instance[flo_index - 1];
                            blended_hue[y] += floor.hue;
                            blended_saturation[y] += floor.saturation;
                            blended_lightless[y] += floor.lightness;
                            blended_hue_divisor[y] += floor.hue_divisor;
                            blended_direction[y]++;
                        }
                    }

                    int x_negative_offset = x - 5;
                    if (x_negative_offset >= 0 && x_negative_offset < landscape_size_x) {
                        int flo_index = underlay_flo_index[plane][x_negative_offset][y] & 0xFF;
                        if (flo_index > 0) {
                            Floor f = Floor.instance[flo_index - 1];
                            blended_hue[y] -= f.hue;
                            blended_saturation[y] -= f.saturation;
                            blended_lightless[y] -= f.lightness;
                            blended_hue_divisor[y] -= f.hue_divisor;
                            blended_direction[y]--;
                        }
                    }
                }

                if (x >= 1 && x < landscape_size_x - 1) {
                    int blended_hue = 0;
                    int blended_saturation = 0;
                    int blended_brightness = 0;
                    int blended_hue_divisor = 0;
                    int blended_direction_tracker = 0;

                    for (int y = -5; y < landscape_size_y + 5; y++) {
                        int y_positive_offset = y + 5;
                        if (y_positive_offset >= 0 && y_positive_offset < landscape_size_y) {
                            blended_hue += this.blended_hue[y_positive_offset];
                            blended_saturation += this.blended_saturation[y_positive_offset];
                            blended_brightness += blended_lightless[y_positive_offset];
                            blended_hue_divisor += this.blended_hue_divisor[y_positive_offset];
                            blended_direction_tracker += blended_direction[y_positive_offset];
                        }

                        int y_negative_offset = y - 5;
                        if (y_negative_offset >= 0 && y_negative_offset < landscape_size_y) {
                            blended_hue -= this.blended_hue[y_negative_offset];
                            blended_saturation -= this.blended_saturation[y_negative_offset];
                            blended_brightness -= blended_lightless[y_negative_offset];
                            blended_hue_divisor -= this.blended_hue_divisor[y_negative_offset];
                            blended_direction_tracker -= blended_direction[y_negative_offset];
                        }

                        if (y >= 1 && y < landscape_size_y - 1 && (!Game.low_detail || (render_flags[0][x][y] & 0x2) != 0 || ((render_flags[plane][x][y] & 0x10) == 0 && (set_visibility_plane(plane, x, y) == plane_at_build)))) {
                            if (plane < min_plane) {
                                min_plane = plane;
                            }

                            int underlay_id = underlay_flo_index[plane][x][y] & 0xFF;
                            int overlay_id = overlay_flo_index[plane][x][y] & 0xFF;

                            if (underlay_id > 0 || overlay_id > 0) {
                                short v_sw = height_map[plane][x][y];
                                short v_se = height_map[plane][x + 1][y];
                                short v_ne = height_map[plane][x + 1][y + 1];
                                short v_nw = height_map[plane][x][y + 1];
                                int l_sw = light_map[x][y];
                                int l_se = light_map[x + 1][y];
                                int l_ne = light_map[x + 1][y + 1];
                                int l_nw = light_map[x][y + 1];
                                int hsl = -1;
                                int hsl_randomized = -1;

                                if (underlay_id > 0) {
                                    int hue = blended_hue * 256 / blended_hue_divisor;
                                    int saturation = blended_saturation / blended_direction_tracker;
                                    int brightness = blended_brightness / blended_direction_tracker;
                                    hsl = trim_hsl(hue, saturation, brightness);
                                    hue = hue + hue_randomizer & 0xFF;
                                    brightness += lightness_randomizer;

                                    if (brightness < 0) {
                                        brightness = 0;
                                    } else if (brightness > 255) {
                                        brightness = 255;
                                    }

                                    hsl_randomized = trim_hsl(hue, saturation, brightness);
                                }

                                if (plane > 0) {
                                    boolean hide_underlay = true;

                                    if (underlay_id == 0 && overlay_shape[plane][x][y] != 0) {
                                        hide_underlay = false;
                                    }

                                    if (overlay_id > 0 && !Floor.instance[overlay_id - 1].show_underlay) {
                                        hide_underlay = false;
                                    }

                                    if (hide_underlay && v_sw == v_se && v_sw == v_ne && v_sw == v_nw) {
                                        tile_culling_bitsets[plane][x][y] |= 0x924;
                                    }
                                }

                                int rgb_randomized = 0;

                                if (hsl != -1) {
                                    rgb_randomized = Canvas3D.palette[set_hsl_lightness(hsl_randomized, 96)];
                                }

                                if (overlay_id == 0) {
                                    l.add_tile(plane, x, y, 0, 0, (byte) -1, v_sw, v_se, v_ne, v_nw, set_hsl_lightness(hsl, l_sw), set_hsl_lightness(hsl, l_se), set_hsl_lightness(hsl, l_ne), set_hsl_lightness(hsl, l_nw), 0, 0, 0, 0, rgb_randomized, 0);
                                } else {
                                    int shape = overlay_shape[plane][x][y] + 1;
                                    byte rotation = overlay_rotation[plane][x][y];
                                    Floor f = Floor.instance[overlay_id - 1];
                                    byte texture = f.texture_index;
                                    int hsl_bitset;
                                    int rgb_bitset;

                                    if (texture >= 0) {
                                        hsl_bitset = Canvas3D.get_average_texture_rgb(texture);
                                        rgb_bitset = -1;
                                    } else if (f.color2 == 0xFF00FF) {
                                        hsl_bitset = 0;
                                        rgb_bitset = -2;
                                        texture = -1;
                                    } else {
                                        rgb_bitset = trim_hsl(f.hue2, f.saturation, f.lightness);
                                        hsl_bitset = Canvas3D.palette[get_rgb(f.color, 96)];
                                    }

                                    l.add_tile(plane, x, y, shape, rotation, texture, v_sw, v_se, v_ne, v_nw, set_hsl_lightness(hsl, l_sw), set_hsl_lightness(hsl, l_se), set_hsl_lightness(hsl, l_ne), set_hsl_lightness(hsl, l_nw), get_rgb(rgb_bitset, l_sw), get_rgb(rgb_bitset, l_se), get_rgb(rgb_bitset, l_ne), get_rgb(rgb_bitset, l_nw), rgb_randomized, hsl_bitset);
                                }
                            }
                        }
                    }
                }
            }

            for (int y = 1; y < landscape_size_y - 1; y++) {
                for (int x = 1; x < landscape_size_x - 1; x++) {
                    l.set_visible_planes(plane, x, y, set_visibility_plane(plane, x, y));
                }
            }
        }

        l.apply_untextured_objects(-50, -10, -50, 64, 768);

        for (int x = 0; x < landscape_size_x; x++) {
            for (int y = 0; y < landscape_size_y; y++) {
                if ((render_flags[1][x][y] & 0x2) == 2) {
                    l.add_bridge(x, y);
                }
            }
        }

        int render_rule1 = 1;
        int render_rule2 = 2;
        int render_rule3 = 4;

        for (int k = 0; k < 4; k++) {
            if (k > 0) {
                render_rule1 <<= 3;
                render_rule2 <<= 3;
                render_rule3 <<= 3;
            }

            for (int plane = 0; plane <= k; plane++) {
                for (int y = 0; y <= landscape_size_y; y++) {
                    for (int x = 0; x <= landscape_size_x; x++) {
                        if ((tile_culling_bitsets[plane][x][y] & render_rule1) != 0) {
                            int min_y = y;
                            int max_y = y;
                            int min_plane = plane;
                            int max_plane = plane;

                            for (; min_y > 0; min_y--) {
                                if (((tile_culling_bitsets[plane][x][min_y - 1]) & render_rule1) == 0) {
                                    break;
                                }
                            }

                            for (; max_y < landscape_size_y; max_y++) {
                                if (((tile_culling_bitsets[plane][x][max_y + 1]) & render_rule1) == 0) {
                                    break;
                                }
                            }

                            find_min_plane:
                            for (; min_plane > 0; min_plane--) {
                                for (int occluded_y = min_y; occluded_y <= max_y; occluded_y++) {
                                    if (((tile_culling_bitsets[min_plane - 1][x][occluded_y]) & render_rule1) == 0) {
                                        break find_min_plane;
                                    }
                                }
                            }

                            find_max_plane:
                            for (; max_plane < k; max_plane++) {
                                for (int occluded_y = min_y; occluded_y <= max_y; occluded_y++) {
                                    if (((tile_culling_bitsets[max_plane + 1][x][occluded_y]) & render_rule1) == 0) {
                                        break find_max_plane;
                                    }
                                }
                            }

                            int surface = (max_plane + 1 - min_plane) * (max_y - min_y + 1);

                            if (surface >= 8) {
                                int i = 240;
                                int max_v_z = (height_map[max_plane][x][min_y] - i);
                                int min_v_z = height_map[min_plane][x][min_y];
                                Landscape.create_culling_box(k, x * 128, x * 128, min_y * 128, max_y * 128 + 128, min_v_z, max_v_z, 1);

                                for (int occluded_plane = min_plane; occluded_plane <= max_plane; occluded_plane++) {
                                    for (int occluded_y = min_y; occluded_y <= max_y; occluded_y++) {
                                        tile_culling_bitsets[occluded_plane][x][occluded_y] &= ~render_rule1;
                                    }
                                }
                            }
                        }

                        if ((tile_culling_bitsets[plane][x][y] & render_rule2) != 0) {
                            int min_x = x;
                            int max_x = x;
                            int min_plane = plane;
                            int max_plane = plane;

                            for (; min_x > 0; min_x--) {
                                if (((tile_culling_bitsets[plane][min_x - 1][y]) & render_rule2) == 0) {
                                    break;
                                }
                            }

                            for (; max_x < landscape_size_x; max_x++) {
                                if (((tile_culling_bitsets[plane][max_x + 1][y]) & render_rule2) == 0) {
                                    break;
                                }
                            }

                            find_lowest_plane:
                            for (; min_plane > 0; min_plane--) {
                                for (int occluded_x = min_x; occluded_x <= max_x; occluded_x++) {
                                    if (((tile_culling_bitsets[min_plane - 1][occluded_x][y]) & render_rule2) == 0) {
                                        break find_lowest_plane;
                                    }
                                }
                            }

                            find_highest_plane:
                            for (; max_plane < k; max_plane++) {
                                for (int occluded_x = min_x; occluded_x <= max_x; occluded_x++) {
                                    if (((tile_culling_bitsets[max_plane + 1][occluded_x][y]) & render_rule2) == 0) {
                                        break find_highest_plane;
                                    }
                                }
                            }

                            int surface = (max_plane + 1 - min_plane) * (max_x - min_x + 1);
                            if (surface >= 8) {
                                int i = 240;
                                int max_v_z = (height_map[max_plane][min_x][y] - i);
                                int min_v_z = height_map[min_plane][min_x][y];
                                Landscape.create_culling_box(k, min_x * 128, max_x * 128 + 128, y * 128, y * 128, min_v_z, max_v_z, 2);

                                for (int j = min_plane; j <= max_plane; j++) {
                                    for (int occluded_x = min_x; occluded_x <= max_x; occluded_x++) {
                                        tile_culling_bitsets[j][occluded_x][y] &= ~render_rule2;
                                    }
                                }
                            }
                        }

                        if ((tile_culling_bitsets[plane][x][y] & render_rule3) != 0) {
                            int lowest_occlusion_x = x;
                            int highest_occlusion_x = x;
                            int lowest_occlusion_y = y;
                            int highest_occlussion_y = y;

                            for (; lowest_occlusion_y > 0; lowest_occlusion_y--) {
                                if (((tile_culling_bitsets[plane][x][lowest_occlusion_y - 1]) & render_rule3) == 0) {
                                    break;
                                }
                            }

                            for (; highest_occlussion_y < landscape_size_y; highest_occlussion_y++) {
                                if (((tile_culling_bitsets[plane][x][highest_occlussion_y + 1]) & render_rule3) == 0) {
                                    break;
                                }
                            }

                            find_lowest_occlussion_x:
                            for (; lowest_occlusion_x > 0; lowest_occlusion_x--) {
                                for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++) {
                                    if (((tile_culling_bitsets[plane][lowest_occlusion_x - 1][occluded_y]) & render_rule3) == 0) {
                                        break find_lowest_occlussion_x;
                                    }
                                }
                            }

                            find_highest_occlussion_x:
                            for (; highest_occlusion_x < landscape_size_x; highest_occlusion_x++) {
                                for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++) {
                                    if (((tile_culling_bitsets[plane][highest_occlusion_x + 1][occluded_y]) & render_rule3) == 0) {
                                        break find_highest_occlussion_x;
                                    }
                                }
                            }

                            if ((highest_occlusion_x - lowest_occlusion_x + 1) * (highest_occlussion_y - lowest_occlusion_y + 1) >= 4) {
                                int lowest_occlussion_vertex_height = height_map[plane][lowest_occlusion_x][lowest_occlusion_y];
                                Landscape.create_culling_box(k, lowest_occlusion_x * 128, highest_occlusion_x * 128 + 128, lowest_occlusion_y * 128, highest_occlussion_y * 128 + 128, lowest_occlussion_vertex_height, lowest_occlussion_vertex_height, 4);

                                for (int occluded_x = lowest_occlusion_x; occluded_x <= highest_occlusion_x; occluded_x++) {
                                    for (int occluded_y = lowest_occlusion_y; occluded_y <= highest_occlussion_y; occluded_y++) {
                                        tile_culling_bitsets[plane][occluded_x][occluded_y] &= ~render_rule3;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void fit_edges(int chunk_x, int chunk_y, int size_x, int size_y) {
        for (int y = chunk_y; y <= chunk_y + size_y; y++) {
            for (int x = chunk_x; x <= chunk_x + size_x; x++) {
                if (x >= 0 && x < landscape_size_x && y >= 0 && y < landscape_size_y) {
                    shadow_map[0][x][y] = (byte) 127;

                    if (x == chunk_x && x > 0) {
                        height_map[0][x][y] = height_map[0][x - 1][y];
                    }

                    if (x == chunk_x + size_x && x < landscape_size_x - 1) {
                        height_map[0][x][y] = height_map[0][x + 1][y];
                    }

                    if (y == chunk_y && y > 0) {
                        height_map[0][x][y] = height_map[0][x][y - 1];
                    }

                    if (y == chunk_y + size_y && y < landscape_size_y - 1) {
                        height_map[0][x][y] = height_map[0][x][y + 1];
                    }
                }
            }
        }
    }

    public int get_rgb(int hsl, int brightness) {
        if (hsl == -2) {
            return 12345678;
        }

        if (hsl == -1) {
            if (brightness < 0) {
                brightness = 0;
            } else if (brightness > 127) {
                brightness = 127;
            }
            brightness = 127 - brightness;
            return brightness;
        }

        brightness = brightness * (hsl & 0x7f) / 128;

        if (brightness < 2) {
            brightness = 2;
        } else if (brightness > 126) {
            brightness = 126;
        }

        return (hsl & 0xff80) + brightness;
    }

    public void load_chunk(CollisionMap[] collision, int map_x, int map_y, int chunk_x, int chunk_y, int chunk_plane, byte[] chunk_payload, int chunk_rotation, int plane) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (map_x + x > 0 && map_x + x < 103 && map_y + y > 0 && map_y + y < 103) {
                    collision[plane].flags[map_x + x][(map_y + y)] &= ~0x1000000;
                }
            }
        }

        Buffer s = new Buffer(chunk_payload);

        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    if (z == chunk_plane && x >= chunk_x && x < chunk_x + 8 && y >= chunk_y && y < chunk_y + 8) {
                        load_land(s, map_x + ChunkUtil.rotate_land_x(x, y, chunk_rotation), map_y + ChunkUtil.rotate_land_y(x, y, chunk_rotation), 0, 0, plane, chunk_rotation);
                    } else {
                        load_land(s, -1, -1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    public void load_land(Buffer s, int x, int y, int tile_x, int tile_y, int plane, int chunk_type) {
        if (x >= 0 && x < 104 && y >= 0 && y < 104) {
            render_flags[plane][x][y] = (byte) 0;

            for (; ; ) {
                int opcode = s.get_ubyte();

                if (opcode == 0) {
                    if (plane == 0) {
                        height_map[0][x][y] = (short) (-get_noise_height(932731 + x + tile_x, 556238 + y + tile_y) * 8);
                    } else {
                        height_map[plane][x][y] = (short) (height_map[plane - 1][x][y] - 240);
                        break;
                    }
                    break;
                }

                if (opcode == 1) {
                    int height = s.get_ubyte();

                    if (height == 1) {
                        height = 0;
                    }

                    if (plane == 0) {
                        height_map[0][x][y] = (short) (-height * 8);
                    } else {
                        height_map[plane][x][y] = (short) (height_map[plane - 1][x][y] - height * 8);
                        break;
                    }
                    break;
                }

                if (opcode <= 49) {
                    overlay_flo_index[plane][x][y] = s.get_byte();
                    overlay_shape[plane][x][y] = (byte) ((opcode - 2) / 4);
                    overlay_rotation[plane][x][y] = (byte) (opcode - 2 + chunk_type & 0x3);
                } else if (opcode <= 81) {
                    render_flags[plane][x][y] = (byte) (opcode - 49);
                } else {
                    underlay_flo_index[plane][x][y] = (byte) (opcode - 81);
                }
            }
        } else {
            for (; ; ) {
                int i = s.get_ubyte();

                if (i == 0) {
                    break;
                }

                if (i == 1) {
                    s.get_ubyte();
                    break;
                }

                if (i <= 49) {
                    s.get_ubyte();
                }
            }
        }
    }

    public void load_land(CollisionMap[] collision, byte[] payload, int chunk_x, int chunk_y, int tile_x, int tile_y) {
        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    if (chunk_x + x > 0 && chunk_x + x < 103 && chunk_y + y > 0 && chunk_y + y < 103) {
                        collision[plane].flags[chunk_x + x][chunk_y + y] &= ~0x1000000;
                    }
                }
            }
        }

        Buffer s = new Buffer(payload);

        for (int plane = 0; plane < 4; plane++) {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    load_land(s, x + chunk_x, y + chunk_y, tile_x, tile_y, plane, 0);
                }
            }
        }
    }

    public void load_locs(CollisionMap[] collision_maps, Landscape landscape, int map_x, int map_y, int chunk_x, int chunk_y, int chunk_plane, int map_plane, byte[] payload, int chunk_rotation) {
        Buffer s = new Buffer(payload);
        int index = -1;
        for (; ; ) {
            int msb = s.get_usmart();

            if (msb == 0) {
                break;
            }

            index += msb;
            int uid = 0;

            for (; ; ) {
                int lsb = s.get_usmart();

                if (lsb == 0) {
                    break;
                }

                uid += lsb - 1;
                int loc_y = uid & 0x3f;
                int loc_x = uid >> 6 & 0x3f;
                int loc_plane = uid >> 12;
                int loc_arrangement = s.get_ubyte();
                int loc_type = loc_arrangement >> 2;
                int loc_rotation = loc_arrangement & 0x3;

                if (loc_plane == chunk_plane && loc_x >= chunk_x && loc_x < chunk_x + 8 && loc_y >= chunk_y && loc_y < chunk_y + 8) {
                    LocConfig config = LocConfig.get(index);

                    int local_x = map_x + ChunkUtil.rotate_loc_x(loc_x, loc_y, config.size_x, config.size_y, chunk_rotation);
                    int local_y = map_y + ChunkUtil.rotate_loc_y(loc_x, loc_y, config.size_x, config.size_y, chunk_rotation);

                    if (local_x > 0 && local_y > 0 && local_x < 103 && local_y < 103) {
                        int plane = loc_plane;

                        if ((render_flags[1][local_x][local_y] & 0x2) == 2) {
                            plane--;
                        }

                        CollisionMap collision_map = null;

                        if (plane >= 0) {
                            collision_map = collision_maps[plane];
                        }

                        add_loc(landscape, collision_map, index, loc_type, local_x, local_y, map_plane, loc_rotation + chunk_rotation & 0x3);
                    }
                }
            }
        }
    }

    public void load_locs(int region_x, CollisionMap[] collision_maps, int region_y, Landscape landscape, byte[] payload) {
        Buffer s = new Buffer(payload);
        int loc_index = -1;

        for (; ; ) {
            int xtra = s.get_usmart();

            if (xtra == 0) {
                break;
            }

            loc_index += xtra;
            int region_coord = 0;

            for (; ; ) {
                int coord_bits = s.get_usmart();

                if (coord_bits == 0) {
                    break;
                }

                region_coord += coord_bits - 1;
                int base_y = region_coord & 0x3f;
                int base_x = region_coord >> 6 & 0x3f;
                int loc_plane = region_coord >> 12;
                int loc_arrangement = s.get_ubyte();
                int loc_type = loc_arrangement >> 2;
                int loc_rotation = loc_arrangement & 0x3;
                int loc_x = base_x + region_x;
                int loc_y = base_y + region_y;

                if (loc_x > 0 && loc_y > 0 && loc_x < 103 && loc_y < 103) {
                    int plane = loc_plane;

                    if ((render_flags[1][loc_x][loc_y] & 0x2) == 2) {
                        plane--;
                    }

                    CollisionMap collision = null;

                    if (plane >= 0) {
                        collision = collision_maps[plane];
                    }

                    add_loc(landscape, collision, loc_index, loc_type, loc_x, loc_y, loc_plane, loc_rotation);
                }
            }
        }
    }

    public int set_visibility_plane(int plane, int x, int y) {
        if ((render_flags[plane][x][y] & 0x8) != 0) {
            return 0;
        }
        if (plane > 0 && (render_flags[1][x][y] & 0x2) != 0) {
            return plane - 1;
        }
        return plane;
    }
}
