package rs;

import rs.audio.MusicPlayer;
import rs.audio.model.WaveSound;
import rs.cache.Archive;
import rs.cache.Cache;
import rs.cache.impl.*;
import rs.cache.model.ActorConfig;
import rs.cache.model.LocConfig;
import rs.cache.model.ObjConfig;
import rs.cache.model.SpotAnimConfig;
import rs.input.Keyboard;
import rs.input.Mouse;
import rs.input.MouseRecorder;
import rs.input.model.Area;
import rs.input.model.Key;
import rs.io.Buffer;
import rs.io.ISAACCipher;
import rs.io.OnDemand;
import rs.media.*;
import rs.media.impl.*;
import rs.media.impl.Chat.State;
import rs.media.impl.Menu;
import rs.media.impl.widget.CharacterDesign;
import rs.model.Action;
import rs.model.Packet;
import rs.model.Skill;
import rs.net.Connection;
import rs.net.Jaggrab;
import rs.node.Chain;
import rs.node.impl.OnDemandRequest;
import rs.scene.Scene;
import rs.scene.model.*;
import rs.util.Bit;
import rs.util.JString;
import rs.util.RSColor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.CRC32;

public class Game extends GameShell {

    public static int net_region_x;
    public static int net_region_y;
    public static boolean aBooleanArray876[];
    public static int actor_count;
    public static int actor_indices[];
    public static Actor actors[];
    public static int anim_cycle;
    public static byte[] ANIMATED_TEXTURES = {17, 24, 34};
    public static int anInt913;
    public static int anIntArray1030[];
    public static int anIntArray1052[];
    public static int anIntArray1057[];
    public static int anIntArray1203[];
    public static int anIntArray1229[];
    public static int anIntArray873[];
    public static int anIntArray928[];
    public static int anIntArray968[];
    public static int archive_crc[];
    public static Archive archive_title;
    public static Bitmap bitmap_backbase1;
    public static Bitmap bitmap_map_scenes[];
    public static Bitmap bitmap_mapback;
    public static Bitmap bitmap_mod_icons[];
    public static Bitmap bitmap_scrollbar_down;
    public static Bitmap bitmap_scrollbar_up;
    public static Cache cache[];
    public static int cam_cinema_aim_x;
    public static int cam_cinema_aim_y;
    public static int cam_cinema_aim_z;
    public static int cam_cinema_base_speed;
    public static int cam_cinema_dest_x;
    public static int cam_cinema_dest_y;
    public static int cam_cinema_dest_z;
    public static boolean cam_cinema_mode;
    public static int cam_cinema_rot_base;
    public static int cam_cinema_rot_modifier;
    public static int cam_cinema_speed;
    public static int cam_info_cycle;
    public static int cam_pitch_mod;
    public static int cam_pitch_off;
    public static int cam_x_off;
    public static int cam_x_off_mod;
    public static int cam_y_off;
    public static int cam_y_off_mod;
    public static int cam_yaw_mod;
    public static int cam_yaw_off;
    public static int chase_cam_pitch;
    public static int chase_cam_pitch_mod;
    public static int chase_cam_x;
    public static int chase_cam_y;
    public static int chase_cam_yaw;
    public static int chase_cam_yaw_mod;
    public static int chat_pixels_3d[];
    public static int chunk_coords[];
    public static byte chunk_landscape_payload[][];
    public static byte chunk_loc_payload[][];
    public static int click_area;
    public static int click_cycle;
    public static int clicked_item_slot;
    public static int clicked_item_widget;
    public static CollisionMap collision_maps[];
    public static Connection connection;
    public static ISAACCipher connection_isaac;
    public static CRC32 crc32;
    public static int cross_cycle;
    public static int cross_type;
    public static int cross_x;
    public static int cross_y;
    public static boolean debug = false;
    public static int default_settings[];
    public static boolean dialogue_option_active;
    public static int drag_area;
    public static int drag_cycle;
    public static int drag_slot;
    public static int drag_start_x;
    public static int drag_start_y;
    public static int drag_widget;
    public static boolean dragging;
    public static boolean dragging_scrollbar;
    public static int draw_cycle;
    public static volatile boolean draw_flames;
    public static int draw_x;
    public static int draw_y;
    public static int energy_left;
    public static int entity_count;
    public static int entity_indices[];
    public static int entity_update_count;
    public static int entity_update_indices[];
    public static Exception error_exception;
    public static boolean error_loading;
    public static volatile boolean flame_thread;
    public static int free_friends_list;
    public static int frenemies_status;
    public static int friend_count;
    public static long friend_long[];
    public static String friend_name[];
    public static int friend_node[];
    public static short height_map[][][];
    public static int hovered_chat_widget;
    public static int hovered_slot;
    public static int hovered_slot_widget;
    public static int hovered_tab_widget;
    public static int hovered_viewport_widget;
    public static int ignore_count;
    public static long ignore_long[];
    public static Sprite image_compass;
    public static Sprite image_crosses[];
    public static Sprite image_head_icons[];
    public static Sprite image_hit_marks[];
    public static Sprite[] image_map_markers;
    public static Sprite[] image_mapdots;
    public static Sprite image_mapedge;
    public static Sprite image_mapfunctions[];
    public static Sprite image_minimap;
    public static Sprite image_overlay_multiway;
    public static Buffer in;
    public static int in_multi_zone;
    public static Game instance;
    public static boolean is_focused;
    public static boolean is_members = true;
    public static Chain item_pile[][][] = new Chain[4][104][104];
    public static final boolean VERIFY_CACHE = true;
    public static Socket jaggrab_socket;
    public static Landscape landscape;
    public static int landscape_uids[];
    public static long last_click_time;
    public static int last_map_base_x;
    public static int last_map_base_y;
    public static int last_plane;
    public static int last_ptype1;
    public static int last_ptype2;
    public static int last_ptype3;
    public static int last_sound_index;
    public static int last_sound_position;
    public static long last_sound_time;
    public static int last_sound_type;
    public static final int LOC_CLASS_TYPE[] = {0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3};
    public static Sprite loc_icon[];
    public static int loc_icon_count;
    public static int loc_icon_x[];
    public static int loc_icon_y[];
    public static int local_player_index;
    public static int local_rights;
    public static int local_tile_cycle[][];
    public static boolean logged_in;
    public static Buffer login_buffer;
    public static int logout_cycle;
    public static int loop_cycle;
    public static boolean low_detail = true;
    public static int LSB_BIT_MASK[];
    public static int map_base_x;
    public static int map_base_y;
    public static int map_marker_x;
    public static int map_marker_y;
    public static int map_uids[];
    public static int map_zoom_modifier;
    public static int map_zoom_offset;
    public static int mark_actor_index;
    public static int mark_off_x;
    public static int mark_off_y;
    public static int mark_player_index;
    public static int mark_type;
    public static int mark_x;
    public static int mark_y;
    public static int mark_z;
    public static final int MAX_PLAYER_COUNT = 2048;
    public static final int MAX_PLAYER_INDEX = 2047;
    public static long message_recipient_name_long;
    public static int message_status;
    public static int min_pitch;
    public static int minimap_state;
    public static int mouse_button_setting;
    public static MouseRecorder mouse_recorder;
    public static MusicPlayer music;
    public static int net_alive_cycle;
    public static int net_cycle;
    public static long next_update;
    public static int node_index = 10;
    public static OnDemand ondemand;
    public static Buffer out;
    public static String password;
    public static int path_arbitrary_dest;
    public static int path_distance[][];
    public static int path_queue_x[];
    public static int path_queue_y[];
    public static int path_waypoint[][];
    public static int plane;
    public static boolean player_action_priority[];
    public static String player_action[];
    public static Buffer player_buffer[];
    public static int player_count;
    public static int player_indices[];
    public static Player players[];
    public static int port_offset;
    public static volatile boolean process_flames;
    public static ImageProducer producer_backhmid2;
    public static ImageProducer producer_backleft1;
    public static ImageProducer producer_backleft2;
    public static ImageProducer producer_backright1;
    public static ImageProducer producer_backright2;
    public static ImageProducer producer_backtop1;
    public static ImageProducer producer_backvmid1;
    public static ImageProducer producer_backvmid2;
    public static ImageProducer producer_backvmid3;
    public static ImageProducer producer_minimap;
    public static ImageProducer producer_scene;
    public static String progress_caption;
    public static int progress_percent;
    public static Chain projectiles;
    public static int psize;
    public static int ptype;
    public static int reconnection_attempts;
    public static boolean record_mouse;
    public static boolean redraw;
    public static int redraw_cycle;
    public static int region_chunk_uids[][][];
    public static int loaded_region_x;
    public static int loaded_region_y;
    public static byte render_flags[][][];
    public static String report_abuse_input;
    public static boolean report_abuse_mute;
    public static int report_abuse_windex;
    public static boolean restrict_region;
    public static int rnd_cycle1;
    public static int rnd_cycle2;
    public static BigInteger RSA_MODULUS = new BigInteger("143690958001225849100503496893758066948984921380482659564113596152800934352119496873386875214251264258425208995167316497331786595942754290983849878549630226741961610780416197036711585670124061149988186026407785250364328460839202438651793652051153157765358767514800252431284681765433239888090564804146588087023");
    public static BigInteger RSA_PUBLIC_KEY = new BigInteger("65537");
    public static Scene scene;
    public static int scene_cycle;
    public static long scene_load_start;
    public static boolean scene_loading;
    public static int scene_state;
    public static int scroll_drag_bound;
    public static boolean selected_item;
    public static int selected_item_index;
    public static String selected_item_name;
    public static int selected_item_slot;
    public static int selected_item_widget;
    public static int selected_mask;
    public static String selected_tooltip;
    public static boolean selected_widget;
    public static int selected_widget_index;
    public static Player self;
    public static boolean send_cam_info;
    private static final long serialVersionUID = -7268339690803378968L;
    public static long server_seed;
    public static boolean server_sent_chunk;
    public static int settings[];
    public static int skill_experience[];
    public static int skill_level[];
    public static int skill_real_level[];
    public static int sound_count;
    public static int sound_delay[];
    public static int sound_index[];
    public static int sound_type[];
    public static Chain spawned_locs;
    public static String spoken[];
    public static int spoken_color[];
    public static int spoken_count;
    public static int spoken_cycle[];
    public static int spoken_effect[];
    public static int spoken_max;
    public static int spoken_off_x[];
    public static int spoken_off_y[];
    public static final int SPOKEN_PALETTE[] = {0xffff00, 0xff0000, 0x00ff00, 0x00ffff, 0xff00ff, 0xffffff};
    public static int spoken_x[];
    public static int spoken_y[];
    public static Chain spotanims;
    public static boolean stopping = false;
    public static final int[][] TAB_BUTTONS = {{539, 573, 169, 205}, {569, 599, 168, 205}, {597, 627, 168, 205}, {625, 669, 168, 203}, {666, 696, 168, 205}, {694, 724, 168, 205}, {722, 756, 169, 205}, {540, 574, 466, 502}, {572, 602, 466, 503}, {599, 629, 466, 503}, {627, 671, 467, 502}, {669, 699, 466, 503}, {696, 726, 466, 503}, {724, 758, 466, 502}};
    public static int tmp_hovered_widget;
    public static byte tmp_texels[];
    public static String username;
    public static int viewport_pixels[];
    public static int weight_carried;
    public static int welcome_info;
    public static int welcome_last_ip;
    public static int welcome_last_playdate;
    public static int welcome_notify;
    public static int welcome_unread_messages;
    public static int widget_overlay;
    public static int widget_underlay;
    public static final boolean WINTER = false;
    public static final int XP_TABLE[];
    public static int bytes_read, bytes_sent;
    public static int current_bytes_read, current_bytes_sent;

    static {
        int i = 0;

        XP_TABLE = new int[99];
        for (int level = 0; level < 99; level++) {
            int real_level = level + 1;
            int j = (int) ((double) real_level + 300D * Math.pow(2D, (double) real_level / 7D));
            i += j;
            XP_TABLE[level] = i / 4;
        }

        i = 0x2;

        LSB_BIT_MASK = new int[32];
        for (int k = 0; k < 32; k++) {
            LSB_BIT_MASK[k] = i - 1;
            i <<= 1;
        }

        //[0] = 2 - 1 = 1
        //2 + 2 = 4
        //[1] = 4 - 1 = 3
        //4 + 4 = 8
        //[2] = 8 - 1 = 7
    }

    public static void add_loc(int y, int x, int plane, int new_loc_index, int loc_rotation, int new_loc_type, int class_type) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (low_detail && plane != Game.plane) {
                return;
            }

            int loc_uid = 0;

            if (class_type == 0) {
                loc_uid = landscape.get_wall_uid(plane, x, y);
            } else if (class_type == 1) {
                loc_uid = landscape.get_wall_decoration_uid(plane, x, y);
            } else if (class_type == 2) {
                loc_uid = landscape.get_loc_uid(plane, x, y);
            } else if (class_type == 3) {
                loc_uid = landscape.get_ground_decoration_uid(plane, x, y);
            }

            if (loc_uid != 0) {
                int loc_arrangement = landscape.get_arrangement(plane, x, y, loc_uid);
                int loc_index = loc_uid >> 14 & 0x7fff;
                int loc_type = loc_arrangement & 0x1f;
                int loc_rot = loc_arrangement >> 6;

                if (class_type == 0) {
                    landscape.remove_wall(x, plane, y);
                    LocConfig lc = LocConfig.get(loc_index);

                    if (lc.has_collisions) {
                        collision_maps[plane].remove_wall(x, y, loc_type, loc_rot, lc.blocks_projectiles);
                    }
                }

                if (class_type == 1) {
                    landscape.remove_wall_decoration(0, y, plane, x);
                }

                if (class_type == 2) {
                    landscape.clear_locs(x, y, plane);
                    LocConfig lc = LocConfig.get(new_loc_index);

                    if (x + lc.size_x > 103 || y + lc.size_x > 103 || x + lc.size_y > 103 || y + lc.size_y > 103) {
                        return;
                    }

                    if (lc.has_collisions) {
                        collision_maps[plane].remove_loc(x, y, lc.size_x, lc.size_y, loc_rot, lc.blocks_projectiles);
                    }
                }

                if (class_type == 3) {
                    landscape.remove_ground_decoration(x, y, plane);
                    LocConfig lc = LocConfig.get(new_loc_index);

                    if (lc.has_collisions && lc.is_static) {
                        collision_maps[plane].method218(x, y);
                    }
                }
            }

            if (new_loc_index >= 0) {
                int vertex_plane = plane;

                if (vertex_plane < 3 && (render_flags[1][x][y] & 2) == 2) {
                    vertex_plane++;
                }

                Scene.add_loc(landscape, x, y, plane, new_loc_index, new_loc_type, loc_rotation, vertex_plane, collision_maps[plane], height_map);
            }
        }
    }

    public static void clear_caches() {
        LocConfig.static_model_cache.clear();
        LocConfig.model_cache.clear();
        ActorConfig.model_cache.clear();
        ObjConfig.model_cache.clear();
        ObjConfig.sprite_cache.clear();
        Player.model_cache.clear();
        SpotAnimConfig.model_cache.clear();
    }

    public static void clear_ingame_producers() {
        Chat.producer = null;
        Chat.Settings.producer = null;
        Game.producer_minimap = null;
        Game.producer_scene = null;
        Sidebar.clear_producers();
    }

    public static void close_music_player() {
        method891(false);

        if (music.var2 > 0) {
            music.setVolume(256);
            music.var2 = 0;
        }

        music.halt();
        music = null;
    }

    public static void close_widgets() {
        out.put_opcode(130);
        if (Sidebar.widget_index != -1) {
            Sidebar.widget_index = -1;
            Sidebar.draw = true;
            dialogue_option_active = false;
            Sidebar.draw_tabs = true;
        }
        if (Chat.get_overlay() != -1) {
            Chat.set_overlay(-1);
            dialogue_option_active = false;
        }
        widget_overlay = -1;
    }

    public static void create_ingame_producers() {
        if (Chat.producer != null) {
            return;
        }

        // Lets the garbage collector get the login screen producers.
        TitleScreen.clear_producers();

        // Set the wrest up.
        Chat.producer = new ImageProducer(479, 96);
        Game.producer_minimap = new ImageProducer(172, 156);
        Canvas2D.clear();
        Game.bitmap_mapback.draw(0, 0);
        Game.producer_scene = new ImageProducer(512, 334);
        Canvas2D.clear();
        Chat.Settings.producer = new ImageProducer(496, 50);
        Sidebar.create_producers();

        Game.redraw = true;
    }

    public static void draw_2d_overlay() {
        Chat.Private.draw();

        if (cross_type == 1) {
            image_crosses[cross_cycle / 100].draw_masked(cross_x - 8 - 4, cross_y - 8 - 4);
        }

        if (cross_type == 2) {
            image_crosses[4 + cross_cycle / 100].draw_masked(cross_x - 8 - 4, cross_y - 8 - 4);
        }

        if (widget_underlay != -1) {
            Widget.handle_sequences(anim_cycle, widget_underlay);
            Widget.draw(widget_underlay, 0, 0, 0);
        }

        if (widget_overlay != -1) {
            Widget.handle_sequences(anim_cycle, widget_overlay);
            Widget.draw(widget_overlay, 0, 0, 0);
        }

        handle_message_status();

        if (!Menu.visible) {
            Menu.handle();
            Menu.draw_tooltip();
        } else if (Menu.area == Area.VIEWPORT) {
            Menu.draw();
        }

        if (in_multi_zone == 1) {
            image_overlay_multiway.draw_masked(472, 296);
        }

        if (Game.debug) {
            int x = 507;
            int y = 17;

            StringBuilder sb = new StringBuilder();
            sb.append(JString.FPS_).append(counter.get());

            BitmapFont.NORMAL.draw(sb.toString(), x, y, 0xFFFF00, BitmapFont.RIGHT | BitmapFont.SHADOW);
            y += 17;

            Runtime runtime = Runtime.getRuntime();
            int memUsed = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);

            sb.delete(0, sb.length());
            sb.append(JString.MEM_).append(memUsed).append('k');

            BitmapFont.NORMAL.draw(sb.toString(), x, y, 0xFFFF00, BitmapFont.RIGHT | BitmapFont.SHADOW);
            y += 17;

            sb.delete(0, sb.length());
            sb.append(JString.OUT_).append(bytes_sent).append(JString.BS_);
            sb.append(' ');
            sb.append(JString.IN_).append(bytes_read).append(JString.BS_);

            BitmapFont.NORMAL.draw(sb.toString(), x, y, 0xFFFF00, BitmapFont.RIGHT | BitmapFont.SHADOW);
            y += 17;
        }

        if (next_update > 0) {
            int remaining = (int) (next_update - System.currentTimeMillis());

            if (remaining < 0) {
                next_update = 0;
            }

            int minutes = remaining / 60000;
            int seconds = (remaining / 1000) % 60;

            BitmapFont.NORMAL.draw(JString.SYSTEM_UPDATE_IN + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds), 4, 328, 0xFFFF00, BitmapFont.SHADOW);
        }
    }

    public static void draw_actors(boolean flag) {
        for (int i = 0; i < actor_count; i++) {
            Actor a = actors[actor_indices[i]];
            int uid = 0x20000000 + (actor_indices[i] << 14);

            if (a == null || !a.is_visible() || a.config.aBoolean93 != flag) {
                continue;
            }

            int local_x = a.scene_x >> 7;
            int local_y = a.scene_y >> 7;

            if (local_x < 0 || local_x >= 104 || local_y < 0 || local_y >= 104) {
                continue;
            }

            if (a.size == 1 && (a.scene_x & 0x7f) == 64 && (a.scene_y & 0x7f) == 64) {
                if (local_tile_cycle[local_x][local_y] == scene_cycle) {
                    continue;
                }

                local_tile_cycle[local_x][local_y] = scene_cycle;
            }

            if (!a.config.interactable) {
                uid += 0x80000000;
            }

            landscape.add(a, a.scene_x, a.scene_y, get_land_z(a.scene_x, a.scene_y, plane), plane, a.rotation, (a.size - 1) * 64 + 60, a.can_rotate, uid);
        }
    }

    public static void draw_error_screen() {
        Graphics g = Game.instance.graphics;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 765, 503);
        process_flames = false;
        g.setFont(new Font("Helvetica", Font.BOLD, 14));
        g.setColor(Color.YELLOW);

        int x = 30;
        int y = 35;

        g.drawString(error_exception.getClass().getSimpleName() + ": " + error_exception.getMessage(), x, y);
        x += 20;
        y += 20;

        for (StackTraceElement s : error_exception.getStackTrace()) {
            g.drawString(s.toString(), x, y);
            y += 16;
        }
    }

    public static void draw_flames() {
        flame_thread = true;
        try {
            long last_time = System.currentTimeMillis();
            int i = 0;
            int delay = 20;
            while (process_flames) {
                Flames.cycle++;
                Flames.handle();
                Flames.handle();
                Flames.handle_palette();
                if (++i > 10) {
                    long current_time = System.currentTimeMillis();
                    int difference = (int) (current_time - last_time) / 10 - delay;
                    delay = 40 - difference;
                    if (delay < 5) {
                        delay = 5;
                    }
                    i = 0;
                    last_time = current_time;
                }
                try {
                    Thread.sleep(delay);
                } catch (Exception _ex) {
                }
            }
        } catch (Exception e) {
        }
        flame_thread = false;
    }

    public static void draw_game() {
        if (redraw) {
            redraw = false;
            producer_backleft1.draw(0, 4);
            producer_backleft2.draw(0, 357);
            producer_backright1.draw(722, 4);
            producer_backright2.draw(743, 205);
            producer_backtop1.draw(0, 0);
            producer_backvmid1.draw(516, 4);
            producer_backvmid2.draw(516, 205);
            producer_backvmid3.draw(496, 357);
            producer_backhmid2.draw(0, 338);
            Sidebar.draw = true;
            Chat.redraw = true;
            Chat.Settings.redraw = true;
            Sidebar.draw_tabs = true;
            if (scene_state != 2) {
                producer_scene.draw(4, 4);
                producer_minimap.draw(550, 4);
            }
        }

        if (scene_state == 2) {
            draw_scene();
        }

        if (Menu.visible && Menu.area == Area.TAB) {
            Sidebar.draw = true;
        }

        if (Sidebar.widget_index != -1) {
            boolean redraw = Widget.handle_sequences(anim_cycle, Sidebar.widget_index);
            if (redraw) {
                Sidebar.draw = true;
            }
        }

        if (click_area == 2) {
            Sidebar.draw = true;
        }

        if (drag_area == 2) {
            Sidebar.draw = true;
        }

        Sidebar.draw();
        Chat.update();

        if (scene_state == 2) {
            draw_minimap();
            producer_minimap.draw(550, 4);
        }

        Sidebar.draw_tabs();
        Chat.Settings.draw();
        anim_cycle = 0;
    }

    public static void draw_marker() {
        if (mark_type != 2) {
            return;
        }

        set_draw_xy((mark_x - map_base_x << 7) + mark_off_x, (mark_y - map_base_y << 7) + mark_off_y, mark_z * 2);

        if (draw_x > -1 && loop_cycle % 20 < 10) {
            image_head_icons[2].draw_masked(draw_x - 12, draw_y - 28);
        }
    }

    public static void draw_minimap() {
        producer_minimap.prepare();

        if (minimap_state == 2) {
            byte mask[] = bitmap_mapback.pixels;
            for (int i = 0; i < mask.length; i++) {
                if (mask[i] == 0) {
                    Canvas2D.pixels[i] = 0;
                }
            }
            image_compass.draw(0, 0, 33, 33, chase_cam_yaw, 256, 25, 25, anIntArray1057, anIntArray968);
            producer_scene.prepare();
            return;
        }

        int yaw = chase_cam_yaw + cam_yaw_off & 0x7ff;
        int pivot_x = 48 + (self.scene_x >> 5);
        int pivot_y = 464 - (self.scene_y >> 5);

        image_minimap.draw(25, 5, 146, 151, yaw, 256 + map_zoom_offset, pivot_x, pivot_y, anIntArray1229, anIntArray1052);
        image_compass.draw(0, 0, 33, 33, yaw, 256, 25, 25, anIntArray1057, anIntArray968);

        for (int i = 0; i < loc_icon_count; i++) {
            int map_x = (loc_icon_x[i] * 4 + 2) - self.scene_x / 32;
            int map_y = (loc_icon_y[i] * 4 + 2) - self.scene_y / 32;
            draw_to_minimap(loc_icon[i], map_x, map_y);
        }

        if (Game.item_pile != null) {
            for (int x = 0; x < 104; x++) {
                for (int y = 0; y < 104; y++) {
                    Chain pile = Game.item_pile[plane][x][y];
                    if (pile != null) {
                        int map_x = (x * 4 + 2) - self.scene_x / 32;
                        int map_y = (y * 4 + 2) - self.scene_y / 32;
                        draw_to_minimap(image_mapdots[0], map_x, map_y);
                    }
                }
            }
        }

        for (int i = 0; i < actor_count; i++) {
            Actor a = actors[actor_indices[i]];

            if (a != null && a.is_visible()) {
                ActorConfig ac = a.config;

                if (ac.override_index != null) {
                    ac = ac.get_overriding_config();
                }

                if (ac != null && ac.show_on_minimap && ac.interactable) {
                    int map_x = a.scene_x / 32 - self.scene_x / 32;
                    int map_y = a.scene_y / 32 - self.scene_y / 32;
                    draw_to_minimap(image_mapdots[1], map_x, map_y);
                }
            }
        }

        for (int i = 0; i < player_count; i++) {
            Player p = players[player_indices[i]];

            if (p != null && p.is_visible()) {
                int map_x = p.scene_x / 32 - self.scene_x / 32;
                int map_y = p.scene_y / 32 - self.scene_y / 32;

                boolean is_friend = false;
                long name_long = JString.get_long(p.name);

                for (int j = 0; j < friend_count; j++) {
                    if (name_long != friend_long[j] || friend_node[j] == 0) {
                        continue;
                    }
                    is_friend = true;
                    break;
                }

                boolean is_teammate = false;

                if (self.team != 0 && p.team != 0 && self.team == p.team) {
                    is_teammate = true;
                }

                if (is_friend) {
                    draw_to_minimap(image_mapdots[3], map_x, map_y);
                } else if (is_teammate) {
                    draw_to_minimap(image_mapdots[4], map_x, map_y);
                } else {
                    draw_to_minimap(image_mapdots[2], map_x, map_y);
                }
            }
        }

        if (mark_type != 0 && loop_cycle % 20 < 10) {
            if (mark_type == 1 && mark_actor_index >= 0 && mark_actor_index < actors.length) {
                Actor a = actors[mark_actor_index];
                if (a != null) {
                    int map_x = a.scene_x / 32 - self.scene_x / 32;
                    int map_y = a.scene_y / 32 - self.scene_y / 32;
                    draw_minimap_mark(image_map_markers[1], map_x, map_y);
                }
            }

            if (mark_type == 2) {
                int map_x = ((mark_x - map_base_x) * 4 + 2) - self.scene_x / 32;
                int map_y = ((mark_y - map_base_y) * 4 + 2) - self.scene_y / 32;
                draw_minimap_mark(image_map_markers[1], map_x, map_y);
            }

            if (mark_type == 10 && mark_player_index >= 0 && mark_player_index < players.length) {
                Player p = players[mark_player_index];
                if (p != null) {
                    int map_x = p.scene_x / 32 - self.scene_x / 32;
                    int map_y = p.scene_y / 32 - self.scene_y / 32;
                    draw_minimap_mark(image_map_markers[1], map_x, map_y);
                }
            }
        }

        if (map_marker_x != 0) {
            int map_x = (map_marker_x * 4 + 2) - self.scene_x / 32;
            int map_y = (map_marker_y * 4 + 2) - self.scene_y / 32;
            draw_to_minimap(image_map_markers[0], map_x, map_y);
        }

        image_mapdots[2].draw_masked(96, 77);
        producer_scene.prepare();
    }

    public static void draw_minimap_mark(Sprite s, int map_x, int map_y) {
        int len = map_x * map_x + map_y * map_y;

        if (len > 4225 && len < 0x15f90) {
            int yaw = chase_cam_yaw + cam_yaw_off & 0x7ff;
            int sin = Model.sin[yaw];
            int cos = Model.cos[yaw];
            sin = (sin * 256) / (map_zoom_offset + 256);
            cos = (cos * 256) / (map_zoom_offset + 256);
            int x = map_y * sin + map_x * cos >> 16;
            int y = map_y * cos - map_x * sin >> 16;
            double angle = Math.atan2(x, y);
            int draw_x = (int) (Math.sin(angle) * 63D);
            int draw_y = (int) (Math.cos(angle) * 57D);
            image_mapedge.draw_rotated((94 + draw_x + 4) - 10, 83 - draw_y - 20, 15, 15, 20, 20, 256, angle);
        } else {
            draw_to_minimap(s, map_x, map_y);
        }
    }

    public static void draw_minimap_tile(int x, int y, int plane, int wall_rgb, int door_rgb) {
        int uid = landscape.get_wall_uid(plane, x, y);

        if (uid != 0) {
            int arrangement = landscape.get_arrangement(plane, x, y, uid);
            int rotation = arrangement >> 6 & 3;
            int type = arrangement & 0x1f;
            int rgb = wall_rgb;

            if (uid > 0) {
                rgb = door_rgb;
            }

            int pixels[] = image_minimap.pixels;
            int i = 24624 + x * 4 + (103 - y) * 512 * 4;

            LocConfig lc = LocConfig.get(uid >> 14 & 0x7fff);

            if (lc.scene_image_index != -1) {
                Bitmap b = bitmap_map_scenes[lc.scene_image_index];
                if (b != null) {
                    int map_x = (lc.size_x * 4 - b.width) / 2;
                    int map_y = (lc.size_y * 4 - b.height) / 2;
                    b.draw(48 + x * 4 + map_x, 48 + (104 - y - lc.size_y) * 4 + map_y);
                }
            } else {
                if (type == 0 || type == 2) {
                    if (rotation == 0) {
                        pixels[i] = rgb;
                        pixels[i + 512] = rgb;
                        pixels[i + 1024] = rgb;
                        pixels[i + 1536] = rgb;
                    } else if (rotation == 1) {
                        pixels[i] = rgb;
                        pixels[i + 1] = rgb;
                        pixels[i + 2] = rgb;
                        pixels[i + 3] = rgb;
                    } else if (rotation == 2) {
                        pixels[i + 3] = rgb;
                        pixels[i + 3 + 512] = rgb;
                        pixels[i + 3 + 1024] = rgb;
                        pixels[i + 3 + 1536] = rgb;
                    } else if (rotation == 3) {
                        pixels[i + 1536] = rgb;
                        pixels[i + 1536 + 1] = rgb;
                        pixels[i + 1536 + 2] = rgb;
                        pixels[i + 1536 + 3] = rgb;
                    }
                }

                if (type == 2) {
                    if (rotation == 3) {
                        pixels[i] = rgb;
                        pixels[i + 512] = rgb;
                        pixels[i + 1024] = rgb;
                        pixels[i + 1536] = rgb;
                    } else if (rotation == 0) {
                        pixels[i] = rgb;
                        pixels[i + 1] = rgb;
                        pixels[i + 2] = rgb;
                        pixels[i + 3] = rgb;
                    } else if (rotation == 1) {
                        pixels[i + 3] = rgb;
                        pixels[i + 3 + 512] = rgb;
                        pixels[i + 3 + 1024] = rgb;
                        pixels[i + 3 + 1536] = rgb;
                    } else if (rotation == 2) {
                        pixels[i + 1536] = rgb;
                        pixels[i + 1536 + 1] = rgb;
                        pixels[i + 1536 + 2] = rgb;
                        pixels[i + 1536 + 3] = rgb;
                    }
                }

                if (type == 3) {
                    if (rotation == 0) {
                        pixels[i] = rgb;
                    } else if (rotation == 1) {
                        pixels[i + 3] = rgb;
                    } else if (rotation == 2) {
                        pixels[i + 3 + 1536] = rgb;
                    } else if (rotation == 3) {
                        pixels[i + 1536] = rgb;
                    }
                }
            }
        }

        uid = landscape.get_loc_uid(plane, x, y);

        if (uid != 0) {
            int arrangement = landscape.get_arrangement(plane, x, y, uid);
            int rotation = arrangement >> 6 & 3;
            int type = arrangement & 0x1f;

            LocConfig lc = LocConfig.get(uid >> 14 & 0x7fff);

            if (lc.scene_image_index != -1) {
                Bitmap b = bitmap_map_scenes[lc.scene_image_index];

                if (b != null) {
                    int map_x = (lc.size_x * 4 - b.width) / 2;
                    int map_y = (lc.size_y * 4 - b.height) / 2;
                    b.draw(48 + x * 4 + map_x, 48 + (104 - y - lc.size_y) * 4 + map_y);
                }
            } else if (type == 9) {
                int color = 0xEEEEEE;

                if (uid > 0) {
                    color = 0xEE0000;
                }

                int pixels[] = image_minimap.pixels;
                int i = 24624 + x * 4 + (103 - y) * 512 * 4;

                if (rotation == 0 || rotation == 2) {
                    pixels[i + 1536] = color;
                    pixels[i + 1024 + 1] = color;
                    pixels[i + 512 + 2] = color;
                    pixels[i + 3] = color;
                } else {
                    pixels[i] = color;
                    pixels[i + 512 + 1] = color;
                    pixels[i + 1024 + 2] = color;
                    pixels[i + 1536 + 3] = color;
                }
            }
        }

        uid = landscape.get_ground_decoration_uid(plane, x, y);

        if (uid != 0) {
            LocConfig lc = LocConfig.get(uid >> 14 & 0x7fff);
            if (lc.scene_image_index != -1) {
                Bitmap b = bitmap_map_scenes[lc.scene_image_index];
                if (b != null) {
                    int map_x = (lc.size_x * 4 - b.width) / 2;
                    int map_y = (lc.size_y * 4 - b.height) / 2;
                    b.draw(48 + x * 4 + map_x, 48 + (104 - y - lc.size_y) * 4 + map_y);
                }
            }
        }
    }

    public static void draw_players(boolean only_local) {
        if (self.scene_x >> 7 == map_marker_x && self.scene_y >> 7 == map_marker_y) {
            map_marker_x = 0;
        }

        int count = player_count;

        if (only_local) {
            count = 1;
        }

        for (int i = 0; i < count; i++) {
            Player p;
            int uid;

            if (only_local) {
                p = self;
                uid = MAX_PLAYER_INDEX << 14;
            } else {
                p = players[player_indices[i]];
                uid = player_indices[i] << 14;
            }

            if (p == null || !p.is_visible()) {
                continue;
            }

            p.low_lod = false;

            if ((low_detail && player_count > 50 || player_count > 200) && !only_local && p.move_seq_index == p.stand_animation) {
                p.low_lod = true;
            }

            int local_x = p.scene_x >> 7;
            int local_y = p.scene_y >> 7;

            if (local_x < 0 || local_x >= 104 || local_y < 0 || local_y >= 104) {
                continue;
            }

            if (p.loc_model != null && loop_cycle >= p.loc_start_cycle && loop_cycle < p.loc_end_cycle) {
                p.low_lod = false;
                p.scene_z = get_land_z(p.scene_x, p.scene_y, plane);
                landscape.add(p, p.scene_x, p.scene_y, p.scene_z, plane, p.loc_x0, p.loc_y0, p.loc_x1, p.loc_y1, p.rotation, uid);
                continue;
            }

            if ((p.scene_x & 0x7f) == 64 && (p.scene_y & 0x7f) == 64) {
                if (local_tile_cycle[local_x][local_y] == scene_cycle) {
                    continue;
                }
                local_tile_cycle[local_x][local_y] = scene_cycle;
            }

            p.scene_z = get_land_z(p.scene_x, p.scene_y, plane);
            landscape.add(p, p.scene_x, p.scene_y, p.scene_z, plane, p.rotation, 60, p.can_rotate, uid);
        }

    }

    public static void draw_projectiles() {
        for (Projectile p = (Projectile) projectiles.top(); p != null; p = (Projectile) projectiles.next()) {
            if (p.plane != plane || loop_cycle > p.cycle_end) {
                p.detach();
                return;
            }

            if (loop_cycle >= p.cycle_start) {
                if (p.target_index > 0) {
                    Actor a = actors[p.target_index - 1];
                    if (a != null && a.scene_x >= 0 && a.scene_x < 13312 && a.scene_y >= 0 && a.scene_y < 13312) {
                        p.update(loop_cycle, a.scene_x, a.scene_y, get_land_z(a.scene_x, a.scene_y, p.plane) - p.offset_z);
                    }
                }

                if (p.target_index < 0) {
                    int index = -p.target_index - 1;
                    Player pl;

                    if (index == local_player_index) {
                        pl = self;
                    } else {
                        pl = players[index];
                    }

                    if (pl != null && pl.scene_x >= 0 && pl.scene_x < 13312 && pl.scene_y >= 0 && pl.scene_y < 13312) {
                        p.update(loop_cycle, pl.scene_x, pl.scene_y, get_land_z(pl.scene_x, pl.scene_y, p.plane) - p.offset_z);
                    }
                }

                p.update(anim_cycle);
                landscape.add(p, (int) p.scene_x, (int) p.scene_y, (int) p.scene_z, plane, p.rotation, 60, false, -1);

            }
        }
    }

    public static void draw_scene() {
        scene_cycle++;

        draw_players(true);
        draw_actors(true);
        draw_players(false);
        draw_actors(false);
        draw_projectiles();
        draw_spotanims();

        if (!cam_cinema_mode) {
            int i = chase_cam_pitch;

            if (min_pitch / 256 > i) {
                i = min_pitch / 256;
            }

            if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i) {
                i = anIntArray1203[4] + 128;
            }

            int yaw = chase_cam_yaw + cam_pitch_off & 0x7ff;
            Camera.set(chase_cam_x, chase_cam_y, get_land_z(self.scene_x, self.scene_y, plane) - 50, 600 + i * 3, i, yaw);
        }

        int occlusion_top_plane;

        if (!cam_cinema_mode) {
            occlusion_top_plane = get_max_visible_plane();
        } else {
            occlusion_top_plane = get_max_visible_plane_cinematic();
        }

        int cam_x = Camera.x;
        int cam_y = Camera.y;
        int cam_z = Camera.z;
        int cam_pitch = Camera.pitch;
        int cam_yaw = Camera.yaw;

        for (int i = 0; i < 5; i++) {
            if (aBooleanArray876[i]) {
                int amount = (int) ((Math.random() * (double) (anIntArray873[i] * 2 + 1) - (double) anIntArray873[i]) + Math.sin((double) anIntArray1030[i] * ((double) anIntArray928[i] / 100D)) * (double) anIntArray1203[i]);
                if (i == 0) {
                    Camera.x += amount;
                } else if (i == 1) {
                    Camera.z += amount;
                } else if (i == 2) {
                    Camera.y += amount;
                } else if (i == 3) {
                    Camera.yaw = Camera.yaw + amount & 0x7ff;
                } else if (i == 4) {
                    Camera.pitch += amount;

                    if (Camera.pitch < 128) {
                        Camera.pitch = 128;
                    }

                    if (Camera.pitch > 383) {
                        Camera.pitch = 383;
                    }
                }
            }
        }

        int cycle_3d = Canvas3D.cycle;

        Model.scene_clickable = true;
        Model.hovered_count = 0;
        Model.mouse_x = Mouse.last_x - 4;
        Model.mouse_y = Mouse.last_y - 4;
        Canvas2D.clear();

        landscape.draw(Camera.x, Camera.y, Camera.yaw, Camera.z, occlusion_top_plane, Camera.pitch);
        landscape.clear_locs();

        draw_scene_2d();
        draw_marker();
        scroll_textures(cycle_3d);
        draw_2d_overlay();

        producer_scene.draw(4, 4);
        Camera.x = cam_x;
        Camera.z = cam_z;
        Camera.y = cam_y;
        Camera.pitch = cam_pitch;
        Camera.yaw = cam_yaw;
    }

    public static void draw_scene_2d() {
        spoken_count = 0;

        for (int i = -1; i < player_count + actor_count; i++) {
            Entity e;

            if (i == -1) {
                e = self;
            } else if (i < player_count) {
                e = players[player_indices[i]];
            } else {
                e = actors[actor_indices[i - player_count]];
            }

            if (e == null || !e.is_visible()) {
                continue;
            }

            if (e instanceof Actor) {
                ActorConfig c = ((Actor) e).config;

                if (c.override_index != null) {
                    c = c.get_overriding_config();
                }

                if (c == null) {
                    continue;
                }
            }

            if (i < player_count) {
                int offset_y = 30;
                Player p = (Player) e;

                if (p.headicon_flag != 0) {
                    set_draw_xy(e, e.height + 15);

                    if (draw_x > -1) {
                        for (int icon = 0; icon < 8; icon++) {
                            if ((p.headicon_flag & (1 << icon)) != 0) {
                                Sprite s = image_head_icons[icon];

                                if (s != null) {
                                    s.draw_masked(draw_x - 12, draw_y - offset_y);
                                    offset_y += 25;
                                }
                            }
                        }
                    }
                }

                if (i >= 0 && mark_type == 10 && mark_player_index == player_indices[i]) {
                    set_draw_xy(e, e.height + 15);

                    if (draw_x > -1) {
                        image_head_icons[1].draw_masked(draw_x - 12, draw_y - offset_y);
                    }
                }
            } else {
                ActorConfig ac = ((Actor) e).config;

                if (ac.head_icon >= 0 && ac.head_icon < image_head_icons.length) {
                    set_draw_xy(e, e.height + 15);

                    if (draw_x > -1) {
                        image_head_icons[ac.head_icon].draw_masked(draw_x - 12, draw_y - 30);
                    }
                }

                if (mark_type == 1 && mark_actor_index == actor_indices[i - player_count] && loop_cycle % 20 < 10) {
                    set_draw_xy(e, e.height + 15);

                    if (draw_x > -1) {
                        image_head_icons[2].draw_masked(draw_x - 12, draw_y - 28);
                    }
                }
            }

            if (e.spoken_message != null && (i >= player_count || Chat.Settings.values[0] == 0 || Chat.Settings.values[0] == 3 || Chat.Settings.values[0] == 1 && friend_exists(((Player) e).name))) {
                set_draw_xy(e, e.height);

                if (draw_x > -1 && spoken_count < spoken_max) {
                    spoken_off_x[spoken_count] = BitmapFont.BOLD.getWidth(e.spoken_message) / 2;
                    spoken_off_y[spoken_count] = BitmapFont.BOLD.height;

                    spoken_x[spoken_count] = draw_x;
                    spoken_y[spoken_count] = draw_y;

                    spoken_color[spoken_count] = e.spoken_color;
                    spoken_effect[spoken_count] = e.spoken_effect;
                    spoken_cycle[spoken_count] = e.spoken_life;
                    spoken[spoken_count++] = e.spoken_message;

                    if (Chat.Settings.show_effects && e.spoken_effect >= 1 && e.spoken_effect <= 3) {
                        spoken_off_y[spoken_count] += 10;
                        spoken_y[spoken_count] += 5;
                    }

                    if (Chat.Settings.show_effects && e.spoken_effect == 4) {
                        spoken_off_x[spoken_count] = 60;
                    }

                    if (Chat.Settings.show_effects && e.spoken_effect == 5) {
                        spoken_off_y[spoken_count] += 5;
                    }
                }
            }

            if (e.combat_cycle > loop_cycle) {
                set_draw_xy(e, e.height + 15);

                if (draw_x > -1) {
                    Canvas2D.fill_rect(draw_x - 15, draw_y - 3, 30, 5, 0xFF0000);
                    Canvas2D.fill_rect(draw_x - 15, draw_y - 3, (int) (30 * (e.current_health / (double) e.max_health)), 5, 0x00FF00);
                }
            }

            for (int mark = 0; mark < 4; mark++) {
                if (e.hit_cycle[mark] > loop_cycle) {
                    set_draw_xy(e, e.height / 2);

                    if (draw_x > -1) {
                        if (mark == 1) {
                            draw_y -= 20;
                        } else if (mark == 2) {
                            draw_x -= 15;
                            draw_y -= 10;
                        } else if (mark == 3) {
                            draw_x += 15;
                            draw_y -= 10;
                        }

                        image_hit_marks[e.hit_type[mark]].draw_masked(draw_x - 12, draw_y - 12);
                        BitmapFont.SMALL.draw(String.valueOf(e.hit_damage[mark]), draw_x - 1, draw_y + 3, 0xFFFFFF, BitmapFont.SHADOW_CENTER);
                    }
                }
            }

        }

        for (int i = 0; i < spoken_count; i++) {
            int x = spoken_x[i];
            int y = spoken_y[i];
            int off_x = spoken_off_x[i];
            int off_y = spoken_off_y[i];

            boolean flag = true;
            while (flag) {
                flag = false;
                for (int j = 0; j < i; j++) {
                    if (y + 2 > spoken_y[j] - spoken_off_y[j] && y - off_y < spoken_y[j] + 2 && x - off_x < spoken_x[j] + spoken_off_x[j] && x + off_x > spoken_x[j] - spoken_off_x[j] && spoken_y[j] - spoken_off_y[j] < y) {
                        y = spoken_y[j] - spoken_off_y[j];
                        flag = true;
                    }
                }
            }

            draw_x = spoken_x[i];
            draw_y = spoken_y[i] = y;
            String s = spoken[i];

            if (Chat.Settings.show_effects) {
                int color = 0xffff00;

                if (spoken_color[i] < 6) {
                    color = SPOKEN_PALETTE[spoken_color[i]];
                }

                if (spoken_color[i] == 6) {
                    color = scene_cycle % 20 >= 10 ? 0xffff00 : 0xff0000;
                }

                if (spoken_color[i] == 7) {
                    color = scene_cycle % 20 >= 10 ? 65535 : 255;
                }

                if (spoken_color[i] == 8) {
                    color = scene_cycle % 20 >= 10 ? 0x80ff80 : 45056;
                }

                if (spoken_color[i] == 9) {
                    int cycle = 150 - spoken_cycle[i];

                    if (cycle < 50) {
                        color = 0xff0000 + (0x0500 * cycle);
                    } else if (cycle < 100) {
                        color = 0xffff00 - 0x50000 * (cycle - 50);
                    } else if (cycle < 150) {
                        color = 0xFF00 + 5 * (cycle - 100);
                    }
                }

                if (spoken_color[i] == 10) {
                    int cycle = 150 - spoken_cycle[i];

                    if (cycle < 50) {
                        color = 0xff0000 + 5 * cycle;
                    } else if (cycle < 100) {
                        color = 0xff00ff - 0x50000 * (cycle - 50);
                    } else if (cycle < 150) {
                        color = (255 + 0x50000 * (cycle - 100)) - 5 * (cycle - 100);
                    }
                }

                if (spoken_color[i] == 11) {
                    int cycle = 150 - spoken_cycle[i];

                    if (cycle < 50) {
                        color = 0xffffff - 0x50005 * cycle;
                    } else if (cycle < 100) {
                        color = 65280 + 0x50005 * (cycle - 50);
                    } else if (cycle < 150) {
                        color = 0xffffff - 0x50000 * (cycle - 100);
                    }
                }

                if (spoken_effect[i] <= 1 || spoken_effect[i] > 5) {
                    BitmapFont.BOLD.draw(s, draw_x, draw_y, color, BitmapFont.SHADOW_CENTER);
                }

                if (spoken_effect[i] == 2) {
                    BitmapFont.BOLD.draw_centered_wavy2(draw_x, s, scene_cycle, draw_y + 1, 0);
                    BitmapFont.BOLD.draw_centered_wavy2(draw_x, s, scene_cycle, draw_y, color);
                }

                if (spoken_effect[i] == 3) {
                    BitmapFont.BOLD.draw_centered_wavy(150 - spoken_cycle[i], s, true, scene_cycle, draw_y + 1, draw_x, 0);
                    BitmapFont.BOLD.draw_centered_wavy(150 - spoken_cycle[i], s, true, scene_cycle, draw_y, draw_x, color);
                }

                if (spoken_effect[i] == 4) { // Scroll
                    Canvas2D.set_bounds(draw_x - 50, 0, draw_x + 50, 334);
                    BitmapFont.BOLD.draw(s, (draw_x + 50) - (((150 - spoken_cycle[i]) * (BitmapFont.BOLD.getWidth(s) + 100)) / 150), draw_y, color, BitmapFont.SHADOW);
                    Canvas2D.reset();
                }

                if (spoken_effect[i] == 5) { // Slide
                    int cycle = 150 - spoken_cycle[i];
                    int offsetY = 0;

                    if (cycle < 25) {
                        offsetY = cycle - 25;
                    } else if (cycle > 125) {
                        offsetY = cycle - 125;
                    }

                    Canvas2D.set_bounds(0, draw_y - BitmapFont.BOLD.height - 1, 512, draw_y + 5);
                    BitmapFont.BOLD.draw(s, draw_x, draw_y + offsetY, color, BitmapFont.SHADOW_CENTER);
                    Canvas2D.reset();
                }
            } else {
                BitmapFont.BOLD.draw(s, draw_x, draw_y, 0xFFFF00, BitmapFont.SHADOW_CENTER);
            }
        }
    }

    public static void draw_scrollbar(int x, int y, int height, int scroll_height, int scroll_amount) {
        int grip_length = ((height - 32) * height) / scroll_height;

        if (grip_length < 8) {
            grip_length = 8;
        }

        int offset_y = ((height - 32 - grip_length) * scroll_amount) / (scroll_height - height);

        bitmap_scrollbar_up.draw(x, y);
        bitmap_scrollbar_down.draw(x, (y + height) - 16);

        Canvas2D.fill_rect(x, y + 16, 16, height - 32, RSColor.SCROLLBAR_BACKGROUND);
        Canvas2D.fill_rect(x, y + 16 + offset_y, 16, grip_length, RSColor.SCROLLBAR_FOREGROUND);
        Canvas2D.draw_line_v(x, y + 16 + offset_y, grip_length, RSColor.SCROLLBAR_HIGHLIGHT);
        Canvas2D.draw_line_v(x + 1, y + 16 + offset_y, grip_length, RSColor.SCROLLBAR_HIGHLIGHT);
        Canvas2D.draw_line_h(x, y + 16 + offset_y, 16, RSColor.SCROLLBAR_HIGHLIGHT);
        Canvas2D.draw_line_h(x, y + 17 + offset_y, 16, RSColor.SCROLLBAR_HIGHLIGHT);
        Canvas2D.draw_line_v(x + 15, y + 16 + offset_y, grip_length, RSColor.SCROLLBAR_LOWLIGHT);
        Canvas2D.draw_line_v(x + 14, y + 17 + offset_y, grip_length - 1, RSColor.SCROLLBAR_LOWLIGHT);
        Canvas2D.draw_line_h(x, y + 15 + offset_y + grip_length, 16, RSColor.SCROLLBAR_LOWLIGHT);
        Canvas2D.draw_line_h(x + 1, y + 14 + offset_y + grip_length, 15, RSColor.SCROLLBAR_LOWLIGHT);
    }

    public static void draw_spotanims() {
        SpotAnim a = (SpotAnim) spotanims.top();
        for (; a != null; a = (SpotAnim) spotanims.next()) {
            if (a.plane != plane || a.seq_finished) {
                a.detach();
            } else if (loop_cycle >= a.cycle_end) {
                a.update(anim_cycle);

                if (a.seq_finished) {
                    a.detach();
                } else {
                    landscape.add(a, a.x, a.y, a.z, a.plane, 0, 60, false, -1);
                }
            }
        }
    }

    public static void draw_to_minimap(Sprite s, int x, int y) {
        int angle = chase_cam_yaw + cam_yaw_off & 0x7ff;
        int len = x * x + y * y;

        if (len > 6400) {
            return;
        }

        int sin = Model.sin[angle];
        int cos = Model.cos[angle];
        sin = (sin * 256) / (map_zoom_offset + 256);
        cos = (cos * 256) / (map_zoom_offset + 256);
        int map_x = y * sin + x * cos >> 16;
        int map_y = y * cos - x * sin >> 16;

        if (len > 2500) {
            s.draw_to(bitmap_mapback, ((94 + map_x) - s.crop_width / 2) + 4, 83 - map_y - s.crop_height / 2 - 4);
            return;
        } else {
            s.draw_masked(((94 + map_x) - s.crop_width / 2) + 4, 83 - map_y - s.crop_height / 2 - 4);
            return;
        }
    }

    public static String format_item_amount(int amount) {
        StringBuilder b = new StringBuilder(String.valueOf(amount));

        for (int i = b.length() - 3; i > 0; i -= 3) {
            b.insert(i, ',');
        }

        String s = b.toString();
        int len = b.length();

        if (len > 12) {
            b.insert(0, JString.GR2).replace(6, 22, JString.BLANK).append(JString._BILLION_).append(JString.WHI).append('(');
        } else if (len > 8) {
            b.insert(0, JString.GRE).replace(8, 18, JString.BLANK).append(JString._MILLION_).append(JString.WHI).append('(');
        } else if (len > 4) {
            b.insert(0, JString.CYA).replace(8, 14, JString.BLANK).append(JString.K_).append(JString.WHI).append('(');
        }

        return b.insert(0, ' ').append(s).append(')').toString();
    }

    public static boolean frenemy_option_valid(Widget w, boolean flag) {
        int i = w.action_type;
        if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
            if (i >= 801) {
                i -= 701;
            } else if (i >= 701) {
                i -= 601;
            } else if (i >= 101) {
                i -= 101;
            } else {
                i--;
            }
            Menu.add("Remove @whi@" + friend_name[i], 792);
            Menu.add("Message @whi@" + friend_name[i], 639);
            return true;
        } else if (i >= 401 && i <= 500) {
            Menu.add("Remove @whi@" + w.message_disabled, 322);
            return true;
        } else {
            return false;
        }
    }

    public static void friend_add(long name_long) {
        if (name_long == 0L) {
            return;
        }

        if (friend_count >= 100 && free_friends_list != 1) {
            Chat.put(JString.FRIENDS_FULL, 0);
            return;
        }

        if (friend_count >= 200) {
            Chat.put(JString.FRIENDS_FULL, 0);
            return;
        }

        String name = JString.get_formatted_string(name_long);

        for (int i = 0; i < friend_count; i++) {
            if (friend_long[i] == name_long) {
                Chat.put(name + " is already on your friend list", 0);
                return;
            }
        }

        for (int j = 0; j < ignore_count; j++) {
            if (ignore_long[j] == name_long) {
                Chat.put("Please remove " + name + " from your ignore list first", 0);
                return;
            }
        }

        if (name.equals(self.name)) {
            return;
        } else {
            friend_name[friend_count] = name;
            friend_long[friend_count] = name_long;
            friend_node[friend_count] = 0;
            friend_count++;
            Sidebar.draw = true;
            out.put_opcode(188);
            out.put_long(name_long);
            return;
        }
    }

    public static boolean friend_exists(String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < friend_count; i++) {
            if (s.equalsIgnoreCase(friend_name[i])) {
                return true;
            }
        }
        return s.equalsIgnoreCase(self.name);
    }

    public static void friend_remove(long name_long) {
        try {
            if (name_long == 0L) {
                return;
            }

            for (int i = 0; i < friend_count; i++) {
                if (friend_long[i] != name_long) {
                    continue;
                }

                friend_count--;
                Sidebar.draw = true;
                for (int j = i; j < friend_count; j++) {
                    friend_name[j] = friend_name[j + 1];
                    friend_node[j] = friend_node[j + 1];
                    friend_long[j] = friend_long[j + 1];
                }

                out.put_opcode(215);
                out.put_long(name_long);
                break;
            }
        } catch (RuntimeException runtimeexception) {
            Signlink.error("18622, " + name_long + ", " + runtimeexception.toString());
            throw new RuntimeException();
        }
    }

    public static void generate_minimap(int z) {
        int pixels[] = image_minimap.pixels;

        Arrays.fill(pixels, 0);

        for (int y = 1; y < 103; y++) {
            int i = 24628 + (103 - y) * 512 * 4;
            for (int x = 1; x < 103; x++) {
                if ((render_flags[z][x][y] & 0x18) == 0) {
                    landscape.draw_minimap_tile(pixels, i, 512, z, x, y);
                }

                if (z < 3 && (render_flags[z + 1][x][y] & 8) != 0) {
                    landscape.draw_minimap_tile(pixels, i, 512, z + 1, x, y);
                }

                i += 4;
            }

        }

        int wall_color = ((238 + (int) (Math.random() * 20D)) - 10 << 16) + ((238 + (int) (Math.random() * 20D)) - 10 << 8) + ((238 + (int) (Math.random() * 20D)) - 10);
        int door_color = (238 + (int) (Math.random() * 20D)) - 10 << 16;
        image_minimap.prepare();

        for (int y = 1; y < 103; y++) {
            for (int x = 1; x < 103; x++) {
                if ((render_flags[z][x][y] & 0x18) == 0) {
                    draw_minimap_tile(x, y, z, wall_color, door_color);
                }
                if (z < 3 && (render_flags[z + 1][x][y] & 8) != 0) {
                    draw_minimap_tile(x, y, z + 1, wall_color, door_color);
                }
            }

        }

        producer_scene.prepare();
        loc_icon_count = 0;

        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                int uid = landscape.get_ground_decoration_uid(plane, x, y);
                if (uid != 0) {
                    int i = LocConfig.get(uid >> 14 & 0x7fff).icon;
                    if (i >= 0) {
                        int map_x = x;
                        int map_y = y;
                        if (i != 22 && i != 29 && i != 34 && i != 36 && i != 46 && i != 47 && i != 48) {
                            int collision_map[][] = collision_maps[plane].flags;
                            for (int i4 = 0; i4 < 10; i4++) {
                                int random = (int) (Math.random() * 4D);
                                if (random == 0 && map_x > 0 && map_x > x - 3 && (collision_map[map_x - 1][map_y] & 0x1280108) == 0) {
                                    map_x--;
                                } else if (random == 1 && map_x < 103 && map_x < x + 3 && (collision_map[map_x + 1][map_y] & 0x1280180) == 0) {
                                    map_x++;
                                } else if (random == 2 && map_y > 0 && map_y > y - 3 && (collision_map[map_x][map_y - 1] & 0x1280102) == 0) {
                                    map_y--;
                                } else if (random == 3 && map_y < 103 && map_y < y + 3 && (collision_map[map_x][map_y + 1] & 0x1280120) == 0) {
                                    map_y++;
                                }
                            }

                        }
                        loc_icon[loc_icon_count] = image_mapfunctions[i];
                        loc_icon_x[loc_icon_count] = map_x;
                        loc_icon_y[loc_icon_count] = map_y;
                        loc_icon_count++;
                    }
                }
            }

        }

    }

    public static Archive get_archive(String caption, int archive_index, String archive_name, int archive_crc, int percent) {
        byte[] data = null;

        try {
            if (cache[0] != null) {
                data = cache[0].get(archive_index);
            }
        } catch (Exception _ex) {
        }

        if (VERIFY_CACHE) {
            if (data != null) {
                crc32.reset();
                crc32.update(data);
                int crc = (int) crc32.getValue();
                if (crc != archive_crc) {
                    data = null;
                }
            }
        }

        if (data != null) {
            return new Archive(new Buffer(data));
        }

        while (data == null) {
            String error = JString.UNKNOWN_ERROR;
            instance.draw_progress(JString.REQUESTING_ + caption, percent);

            try {
                DataInputStream in = Jaggrab.request(archive_name + archive_crc);

                byte index_data[] = new byte[6];
                in.readFully(index_data, 0, 6);

                Buffer idx = new Buffer(index_data);
                idx.position = 3;

                int size = idx.get_medium() + 6;
                int read = 6;

                data = new byte[size];
                System.arraycopy(index_data, 0, data, 0, 6);

                while (read < size) {
                    int remaining = size - read;

                    if (remaining > 1000) {
                        remaining = 1000;
                    }

                    int bytes_read = in.read(data, read, remaining);

                    if (bytes_read < 0) {
                        error = "Length error: " + read + "/" + size;
                        throw new IOException("EOF");
                    }

                    read += bytes_read;
                    instance.draw_progress("Loading " + caption + " - " + (read * 100) / size + "%", percent);
                }
                in.close();

                try {
                    if (cache[0] != null) {
                        cache[0].put(data, archive_index);
                    }
                } catch (Exception _ex) {
                    cache[0] = null;
                }

                if (VERIFY_CACHE) {
                    if (data != null) {
                        crc32.reset();
                        crc32.update(data);

                        int readCRC = (int) crc32.getValue();
                        if (readCRC != archive_crc) {
                            data = null;
                            error = "Checksum error: " + readCRC;
                        }
                    }
                }
            } catch (IOException ioe) {
                if (error.equals(JString.UNKNOWN_ERROR)) {
                    error = JString.CONNECTION_ERROR;
                }

                data = null;
            } catch (NullPointerException _ex) {
                error = JString.NULL_ERROR;
                data = null;

                if (!Signlink.error) {
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException _ex) {
                error = JString.BOUNDS_ERROR;
                data = null;

                if (!Signlink.error) {
                    return null;
                }
            } catch (Exception _ex) {
                error = JString.UNEXPECTED_ERROR;
                data = null;

                if (!Signlink.error) {
                    return null;
                }
            }

            if (Game.stopping) {
                return null;
            }

            if (data == null) {
                for (int i = 5; i > 0; i--) {
                    try {
                        instance.draw_progress(error + " - Retrying in " + i, 20);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
        return new Archive(new Buffer(data));
    }

    public static Component get_component() {
        return instance.getComponent();
    }

    public static int get_land_z(int x, int y, int plane) {
        int local_x = x >> 7;
        int local_y = y >> 7;

        if (local_x < 0 || local_y < 0 || local_x > 103 || local_y > 103) {
            return 0;
        }

        int z = plane;

        if (z < 3 && (render_flags[1][local_x][local_y] & 2) == 2) {
            z++;
        }

        int tile_x = x & 0x7f;
        int tile_y = y & 0x7f;
        int i2 = height_map[z][local_x][local_y] * (128 - tile_x) + height_map[z][local_x + 1][local_y] * tile_x >> 7;
        int j2 = height_map[z][local_x][local_y + 1] * (128 - tile_x) + height_map[z][local_x + 1][local_y + 1] * tile_x >> 7;
        return ((i2 * (128 - tile_y) + (j2 * tile_y)) >> 7);
    }

    public static int get_max_visible_plane() {
        int occlusion_plane = 3;

        // Once the pitch is below this, start checking to see if the camera is
        // within a roof tile.
        if (Camera.pitch < 310) {
            int cam_local_x = Camera.x >> 7;
            int cam_local_y = Camera.y >> 7;
            int local_x = self.scene_x >> 7;
            int local_y = self.scene_y >> 7;

            if ((render_flags[plane][cam_local_x][cam_local_y] & 4) != 0) {
                occlusion_plane = plane;
            }

            int d_x;
            if (local_x > cam_local_x) {
                d_x = local_x - cam_local_x;
            } else {
                d_x = cam_local_x - local_x;
            }

            int d_y;
            if (local_y > cam_local_y) {
                d_y = local_y - cam_local_y;
            } else {
                d_y = cam_local_y - local_y;
            }

            if (d_x > d_y) {
                int i2 = (d_y * 0x10000) / d_x;
                int k2 = 0x8000;
                while (cam_local_x != local_x) {
                    if (cam_local_x < local_x) {
                        cam_local_x++;
                    } else if (cam_local_x > local_x) {
                        cam_local_x--;
                    }

                    if ((render_flags[plane][cam_local_x][cam_local_y] & 4) != 0) {
                        occlusion_plane = plane;
                    }

                    k2 += i2;
                    if (k2 >= 0x10000) {
                        k2 -= 0x10000;

                        if (cam_local_y < local_y) {
                            cam_local_y++;
                        } else if (cam_local_y > local_y) {
                            cam_local_y--;
                        }

                        if ((render_flags[plane][cam_local_x][cam_local_y] & 4) != 0) {
                            occlusion_plane = plane;
                        }
                    }
                }
            } else {
                int j2 = (d_x * 0x10000) / d_y;
                int l2 = 0x8000;

                while (cam_local_y != local_y) {
                    if (cam_local_y < local_y) {
                        cam_local_y++;
                    } else if (cam_local_y > local_y) {
                        cam_local_y--;
                    }

                    if ((render_flags[plane][cam_local_x][cam_local_y] & 4) != 0) {
                        occlusion_plane = plane;
                    }

                    l2 += j2;
                    if (l2 >= 0x10000) {
                        l2 -= 0x10000;

                        if (cam_local_x < local_x) {
                            cam_local_x++;
                        } else if (cam_local_x > local_x) {
                            cam_local_x--;
                        }

                        if ((render_flags[plane][cam_local_x][cam_local_y] & 4) != 0) {
                            occlusion_plane = plane;
                        }
                    }
                }
            }
        }

        if ((render_flags[plane][self.scene_x >> 7][self.scene_y >> 7] & 4) != 0) {
            occlusion_plane = plane;
        }

        return occlusion_plane;
    }

    public static int get_max_visible_plane_cinematic() {
        int ground_z = get_land_z(Camera.x, Camera.y, plane);
        if (ground_z - Camera.z < 800 && (render_flags[plane][Camera.x >> 7][Camera.y >> 7] & 4) != 0) {
            return plane;
        } else {
            return 3;
        }
    }

    public static int get_scene_load_state(byte byte0) {
        for (int i = 0; i < chunk_loc_payload.length; i++) {
            if (chunk_loc_payload[i] == null && map_uids[i] != -1) {
                return -1;
            }
            if (chunk_landscape_payload[i] == null && landscape_uids[i] != -1) {
                return -2;
            }
        }

        boolean valid = true;

        for (int i = 0; i < chunk_loc_payload.length; i++) {
            byte landscape_payload[] = chunk_landscape_payload[i];

            if (landscape_payload != null) {
                int x = 10;
                int y = 10;

                if (!server_sent_chunk) {
                    x = (chunk_coords[i] >> 8) * 64 - map_base_x;
                    y = (chunk_coords[i] & 0xff) * 64 - map_base_y;
                }

                valid &= Scene.locs_fully_loaded(x, y, landscape_payload);
            }
        }

        if (!valid) {
            return -3;
        }

        if (scene_loading) {
            return -4;
        } else {
            scene_state = 2;
            Scene.plane_at_build = plane;
            retrieve_scene(true);
            out.put_opcode(121);
            return 0;
        }
    }

    public static void handle() {
        if (logout_cycle > 0) {
            logout_cycle--;
        }

        for (int i = 0; i < 5; i++) {
            if (!handle_net()) {
                break;
            }
        }

        if (!logged_in) {
            return;
        }

        synchronized (mouse_recorder.synchronize) {
            if (record_mouse) {
                if (Mouse.click_button != 0 || mouse_recorder.off >= 40) {
                    out.put_opcode(45);
                    out.put_byte(0);

                    int start = out.position;
                    int j3 = 0;
                    for (int j4 = 0; j4 < mouse_recorder.off; j4++) {
                        if (start - out.position >= 240) {
                            break;
                        }

                        j3++;

                        int y = mouse_recorder.y[j4];

                        if (y < 0) {
                            y = 0;
                        } else if (y > 502) {
                            y = 502;
                        }

                        int x = mouse_recorder.x[j4];

                        if (x < 0) {
                            x = 0;
                        } else if (x > 764) {
                            x = 764;
                        }

                        int coord = y * 765 + x;

                        if (mouse_recorder.y[j4] == -1 && mouse_recorder.x[j4] == -1) {
                            x = -1;
                            y = -1;
                            coord = 0x7ffff;
                        }

                        if (x == mouse_recorder.last_x && y == mouse_recorder.last_y) {
                            if (mouse_recorder.cycle < 2047) {
                                mouse_recorder.cycle++;
                            }
                        } else {
                            int xd = x - mouse_recorder.last_x;
                            int yd = y - mouse_recorder.last_y;
                            mouse_recorder.last_x = x;
                            mouse_recorder.last_y = y;

                            if (mouse_recorder.cycle < 8 && xd >= -32 && xd <= 31 && yd >= -32 && yd <= 31) {
                                xd += 32;
                                yd += 32;
                                out.put_short((mouse_recorder.cycle << 12) + (xd << 6) + yd);
                                mouse_recorder.cycle = 0;
                            } else if (mouse_recorder.cycle < 8) {
                                out.put_medium(0x800000 + (mouse_recorder.cycle << 19) + coord);
                                mouse_recorder.cycle = 0;
                            } else {
                                out.put_int(0xc0000000 + (mouse_recorder.cycle << 19) + coord);
                                mouse_recorder.cycle = 0;
                            }
                        }
                    }

                    out.put_length(out.position - start);

                    if (j3 >= mouse_recorder.off) {
                        mouse_recorder.off = 0;
                    } else {
                        mouse_recorder.off -= j3;
                        for (int i = 0; i < mouse_recorder.off; i++) {
                            mouse_recorder.x[i] = mouse_recorder.x[i + j3];
                            mouse_recorder.y[i] = mouse_recorder.y[i + j3];
                        }
                    }
                }
            } else {
                mouse_recorder.off = 0;
            }
        }

        if (Mouse.click_button != 0) {
            long dt = (Mouse.click_time - last_click_time) / 50L;

            if (dt > 4095L) {
                dt = 4095L;
            }

            last_click_time = Mouse.click_time;

            int x = Mouse.click_x;
            int y = Mouse.click_y;
            x = x > 764 ? 764 : x < 0 ? 0 : x;
            y = y > 502 ? 502 : y < 0 ? 0 : y;

            int coord = y * 765 + x;
            int button = Mouse.click_button == 2 ? 1 : 0;

            out.put_opcode(241);
            out.put_int((((int) dt) << 20) + (button << 19) + coord);
        }

        if (cam_info_cycle > 0) {
            cam_info_cycle--;
        }

        if (Key.LEFT.is_down() || Key.RIGHT.is_down() || Key.UP.is_down() || Key.DOWN.is_down()) {
            send_cam_info = true;
        }

        if (send_cam_info && cam_info_cycle <= 0) {
            cam_info_cycle = 100;
            send_cam_info = false;
            out.put_opcode(86);
            out.put_short(chase_cam_pitch);
            out.put_short_a(chase_cam_yaw);
        }

        if (instance.focused && !is_focused) {
            is_focused = true;
            out.put_opcode(3);
            out.put_byte(1);
        }

        if (!instance.focused && is_focused) {
            is_focused = false;
            out.put_opcode(3);
            out.put_byte(0);
        }

        handle_scene();
        handle_loc_creation();
        handle_audio();
        net_cycle++;

        if (net_cycle > 750) {
            handle_connection_lost();
        }

        handle_players();
        handle_actors();
        handle_spoken();
        anim_cycle++;

        if (cross_type != 0) {
            cross_cycle += 20;
            if (cross_cycle >= 400) {
                cross_type = 0;
            }
        }

        if (click_area != 0) {
            redraw_cycle++;
            if (redraw_cycle >= 15) {
                if (click_area == 2) {
                    Sidebar.draw = true;
                }
                if (click_area == 3) {
                    Chat.redraw = true;
                }
                click_area = 0;
            }
        }

        if (drag_area != 0) {
            drag_cycle++;

            if (Mouse.last_x > drag_start_x + 5 || Mouse.last_x < drag_start_x - 5 || Mouse.last_y > drag_start_y + 5 || Mouse.last_y < drag_start_y - 5) {
                dragging = true;
            }

            if (Mouse.drag_button == 0) {
                if (drag_area == 2) {
                    Sidebar.draw = true;
                }

                if (drag_area == 3) {
                    Chat.redraw = true;
                }

                drag_area = 0;

                int option_count = Menu.count;

                if (dragging && drag_cycle >= 5) {
                    hovered_slot_widget = -1;

                    Menu.handle();

                    if (hovered_slot_widget == drag_widget && hovered_slot != drag_slot) {
                        Widget w = Widget.instance[drag_widget];
                        int type = 0;

                        if (anInt913 == 1 && w.action_type == 206) {
                            type = 1;
                        }

                        if (w.item_index[hovered_slot] <= 0) {
                            type = 0;
                        }

                        if (w.items_swappable) {
                            int old_slot = drag_slot;
                            int new_slot = hovered_slot;
                            w.item_index[new_slot] = w.item_index[old_slot];
                            w.item_count[new_slot] = w.item_count[old_slot];
                            w.item_index[old_slot] = -1;
                            w.item_count[old_slot] = 0;
                        } else if (type == 1) {
                            int old_slot = drag_slot;
                            for (int new_slot = hovered_slot; old_slot != new_slot; ) {
                                if (old_slot > new_slot) {
                                    w.swap_slots(old_slot, old_slot - 1);
                                    old_slot--;
                                } else if (old_slot < new_slot) {
                                    w.swap_slots(old_slot, old_slot + 1);
                                    old_slot++;
                                }
                            }

                        } else {
                            w.swap_slots(drag_slot, hovered_slot);
                        }

                        out.put_opcode(214);
                        out.put_le_short_a(drag_widget);
                        out.put_byte_c(type);
                        out.put_le_short_a(drag_slot);
                        out.put_le_short(hovered_slot);
                    }
                } else if (mouse_button_setting == 1 && option_count > 2) {
                    Menu.show();
                } else if (option_count > 0) {
                    handle_menu_option(option_count - 1);
                }

                redraw_cycle = 10;
                Mouse.click_button = 0;
            }
        }

        if (Landscape.click_local_x != -1) {
            int local_x = Landscape.click_local_x;
            int local_y = Landscape.click_local_y;

            boolean valid_path = walk_to(0, 0, 0, self.path_x[0], self.path_y[0], local_x, local_y, 0, 0, 0, true);
            Landscape.click_local_x = -1;

            if (valid_path) {
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 1;
                cross_cycle = 0;
            }
        }

        if (Mouse.click_button == 1 && Chat.get_message(Chat.MESSAGE_INPUT) != null) {
            Chat.set_message(Chat.MESSAGE_INPUT, null);
            Mouse.click_button = 0;
        }

        handle_widget_mouse();
        handle_minimap_mouse();
        handle_tab_mouse();

        Chat.Settings.handle();

        if (Mouse.is_dragging(1) || Mouse.clicked(1)) {
            click_cycle++;
        }

        if (scene_state == 2) {
            handle_camera();
        }

        if (scene_state == 2 && cam_cinema_mode) {
            handle_cinematic_camera();
        }

        for (int i1 = 0; i1 < 5; i1++) {
            anIntArray1030[i1]++;
        }

        handle_keyboard();

        instance.idle_cycle++;

        if (instance.idle_cycle > 4500) {
            logout_cycle = 250;
            instance.idle_cycle -= 500;
            out.put_opcode(202);
        }

        rnd_cycle1++;
        if (rnd_cycle1 > 500) {
            rnd_cycle1 = 0;
            int rnd = (int) (Math.random() * 8D);

            if ((rnd & 1) == 1) {
                cam_x_off += cam_x_off_mod;
            }

            if ((rnd & 2) == 2) {
                cam_y_off += cam_y_off_mod;
            }

            if ((rnd & 4) == 4) {
                cam_pitch_off += Camera.pitch_modifier;
            }
        }

        if (cam_x_off < -50) {
            cam_x_off_mod = 2;
        }

        if (cam_x_off > 50) {
            cam_x_off_mod = -2;
        }

        if (cam_y_off < -55) {
            cam_y_off_mod = 2;
        }

        if (cam_y_off > 55) {
            cam_y_off_mod = -2;
        }

        if (cam_pitch_off < -40) {
            Camera.pitch_modifier = 1;
        }

        if (cam_pitch_off > 40) {
            Camera.pitch_modifier = -1;
        }

        rnd_cycle2++;

        if (rnd_cycle2 > 500) {
            rnd_cycle2 = 0;
            int rnd = (int) (Math.random() * 8D);

            if ((rnd & 1) == 1) {
                cam_yaw_off += Camera.yaw_modifier;
            }

            if ((rnd & 2) == 2) {
                map_zoom_offset += map_zoom_modifier;
            }
        }

        if (cam_yaw_off < -60) {
            Camera.yaw_modifier = 2;
        }

        if (cam_yaw_off > 60) {
            Camera.yaw_modifier = -2;
        }

        if (map_zoom_offset < -20) {
            map_zoom_modifier = 1;
        }

        if (map_zoom_offset > 10) {
            map_zoom_modifier = -1;
        }

        net_alive_cycle++;

        if (net_alive_cycle > 50) {
            out.put_opcode(0);
        }

        if (loop_cycle % 50 == 0) {
            bytes_read = current_bytes_read;
            bytes_sent = current_bytes_sent;
            current_bytes_read = 0;
            current_bytes_sent = 0;
        }

        try {
            if (connection != null && out.position > 0) {
                connection.put_bytes(out.payload, 0, out.position);
                current_bytes_sent += out.position;
                out.position = 0;
                net_alive_cycle = 0;
                return;
            }
        } catch (IOException _ex) {
            handle_connection_lost();
            return;
        } catch (Exception exception) {
            net_disconnect();
        }
    }

    public static void handle_actor_menu_options(ActorConfig ac, int x, int y, int index) {
        if (Menu.count >= 400) {
            return;
        }

        if (ac.override_index != null) {
            ac = ac.get_overriding_config();
        }

        if (ac == null) {
            return;
        }

        if (!ac.interactable) {
            return;
        }

        String s = ac.name;

        if (ac.combat_level != 0) {
            s = s + RSColor.get_level_tag(ac.combat_level);
        }

        if (selected_item) {
            Menu.add("Use " + selected_item_name + " with @yel@" + s, 582, x, y, index);
            return;
        }

        if (selected_widget) {
            if (Bit.signed(selected_mask, 0x2)) {
                Menu.add(selected_tooltip + " @yel@" + s, 413, x, y, index);
                return;
            }
        } else {
            if (ac.action != null) {
                for (int i = 4; i >= 0; i--) {
                    if (ac.action[i] != null && !ac.action[i].equalsIgnoreCase(JString.ATTACK)) {
                        Menu.add(ac.action[i] + " @yel@" + s, Action.ACTOR[i], x, y, index);
                    }
                }

            }
            if (ac.action != null) {
                for (int i = 4; i >= 0; i--) {
                    if (ac.action[i] != null && ac.action[i].equalsIgnoreCase(JString.ATTACK)) {
                        int off = 0;
                        if (ac.combat_level > self.combat_level) {
                            off = 2000;
                        }
                        Menu.add(ac.action[i] + " @yel@" + s, Action.ACTOR[i] + off, x, y, index);
                    }
                }
            }
            Menu.add("Examine @yel@" + s, 1025, x, y, index);
        }
    }

    public static void handle_actors() {
        for (int i = 0; i < actor_count; i++) {
            int actor_index = actor_indices[i];
            Actor a = actors[actor_index];
            if (a != null) {
                handle_entity(a);
            }
        }
    }

    public static void handle_audio() {
        for (int i = 0; i < sound_count; i++) {
            if (sound_delay[i] <= 0) {
                boolean played = false;

                try {
                    if (sound_index[i] == last_sound_index && sound_type[i] == last_sound_type) {
                        // TODO: Replay WAV
                    } else {
                        Buffer b = WaveSound.get(sound_type[i], sound_index[i]);

                        if (System.currentTimeMillis() + (long) (b.position / 22) > last_sound_time + (long) (last_sound_position / 22)) {
                            last_sound_position = b.position;
                            last_sound_time = System.currentTimeMillis();

                            if ( /*SoundSaved()*/last_sound_position > 0) {
                                last_sound_index = sound_index[i];
                                last_sound_type = sound_type[i];
                            } else {
                                played = true;
                            }
                        }
                    }
                } catch (Exception e) {
                }

                if (!played || sound_delay[i] == -5) {
                    sound_count--;

                    for (int j = i; j < sound_count; j++) {
                        sound_index[j] = sound_index[j + 1];
                        sound_type[j] = sound_type[j + 1];
                        sound_delay[j] = sound_delay[j + 1];
                    }

                    i--;
                } else {
                    sound_delay[i] = -5;
                }
            } else {
                sound_delay[i]--;
            }
        }

        if (music.tmpDelay > 0) {
            music.tmpDelay -= 20;

            if (music.tmpDelay < 0) {
                music.tmpDelay = 0;
            }

            if (music.tmpDelay == 0 && music.volume0 != 0 && music.currentSong != -1) {
                play_music_instantly(music.volume0, music.currentSong, 0, 0, false);
            }
        }
    }

    public static void handle_camera() {
        try {
            int scene_x = self.scene_x;
            int scene_y = self.scene_y;

            if (chase_cam_x - scene_x < -500 || chase_cam_x - scene_x > 500 || chase_cam_y - scene_y < -500 || chase_cam_y - scene_y > 500) {
                chase_cam_x = scene_x;
                chase_cam_y = scene_y;
            }

            if (chase_cam_x != scene_x) {
                chase_cam_x += (scene_x - chase_cam_x) / 16;
            }

            if (chase_cam_y != scene_y) {
                chase_cam_y += (scene_y - chase_cam_y) / 16;
            }

            if (Key.LEFT.is_down()) {
                chase_cam_yaw_mod += (-24 - chase_cam_yaw_mod) / 2;
            } else if (Key.RIGHT.is_down()) {
                chase_cam_yaw_mod += (24 - chase_cam_yaw_mod) / 2;
            } else {
                chase_cam_yaw_mod /= 2;
            }

            if (Key.UP.is_down()) {
                chase_cam_pitch_mod += (12 - chase_cam_pitch_mod) / 2;
            } else if (Key.DOWN.is_down()) {
                chase_cam_pitch_mod += (-12 - chase_cam_pitch_mod) / 2;
            } else {
                chase_cam_pitch_mod /= 2;
            }

            chase_cam_yaw = chase_cam_yaw + chase_cam_yaw_mod / 2 & 0x7ff;
            chase_cam_pitch += chase_cam_pitch_mod / 2;

            if (chase_cam_pitch < Camera.MIN_PITCH) {
                chase_cam_pitch = Camera.MIN_PITCH;
            }

            if (chase_cam_pitch > Camera.MAX_PITCH) {
                chase_cam_pitch = Camera.MAX_PITCH;
            }

            int local_x = chase_cam_x >> 7;
            int local_y = chase_cam_y >> 7;
            int local_plane = get_land_z(chase_cam_x, chase_cam_y, plane);
            int highest_z = 0;

            if (local_x > 3 && local_y > 3 && local_x < 100 && local_y < 100) {
                for (int x = local_x - 4; x <= local_x + 4; x++) {
                    for (int y = local_y - 4; y <= local_y + 4; y++) {
                        int plane = Game.plane;

                        if (plane < 3 && (render_flags[1][x][y] & 2) == 2) {
                            plane++;
                        }

                        int last_z = local_plane - height_map[plane][x][y];
                        if (last_z > highest_z) {
                            highest_z = last_z;
                        }
                    }

                }

            }

            int j2 = highest_z * 192;

            if (j2 > 0x17f00) {
                j2 = 0x17f00;
            }

            if (j2 < 32768) {
                j2 = 32768;
            }

            if (j2 > min_pitch) {
                min_pitch += (j2 - min_pitch) / 24;
                return;
            }

            if (j2 < min_pitch) {
                min_pitch += (j2 - min_pitch) / 80;
                return;
            }
        } catch (Exception _ex) {
            Signlink.error("glfc_ex " + self.scene_x + "," + self.scene_y + "," + chase_cam_x + "," + chase_cam_y + "," + loaded_region_x + "," + loaded_region_y + "," + map_base_x + "," + map_base_y);
            throw new RuntimeException(JString.EEK);
        }
    }

    public static void handle_cinematic_camera() {
        int cam_x = cam_cinema_dest_x * 128 + 64;
        int cam_y = cam_cinema_dest_y * 128 + 64;
        int cam_z = get_land_z(cam_x, cam_y, plane) - cam_cinema_dest_z;

        if (Camera.x < cam_x) {
            Camera.x += cam_cinema_base_speed + ((cam_x - Camera.x) * cam_cinema_speed) / 1000;
            if (Camera.x > cam_x) {
                Camera.x = cam_x;
            }
        }

        if (Camera.x > cam_x) {
            Camera.x -= cam_cinema_base_speed + ((Camera.x - cam_x) * cam_cinema_speed) / 1000;
            if (Camera.x < cam_x) {
                Camera.x = cam_x;
            }
        }

        if (Camera.z < cam_z) {
            Camera.z += cam_cinema_base_speed + ((cam_z - Camera.z) * cam_cinema_speed) / 1000;
            if (Camera.z > cam_z) {
                Camera.z = cam_z;
            }
        }

        if (Camera.z > cam_z) {
            Camera.z -= cam_cinema_base_speed + ((Camera.z - cam_z) * cam_cinema_speed) / 1000;
            if (Camera.z < cam_z) {
                Camera.z = cam_z;
            }
        }

        if (Camera.y < cam_y) {
            Camera.y += cam_cinema_base_speed + ((cam_y - Camera.y) * cam_cinema_speed) / 1000;
            if (Camera.y > cam_y) {
                Camera.y = cam_y;
            }
        }

        if (Camera.y > cam_y) {
            Camera.y -= cam_cinema_base_speed + ((Camera.y - cam_y) * cam_cinema_speed) / 1000;
            if (Camera.y < cam_y) {
                Camera.y = cam_y;
            }
        }

        cam_x = cam_cinema_aim_x * 128 + 64;
        cam_y = cam_cinema_aim_y * 128 + 64;
        cam_z = get_land_z(cam_x, cam_y, plane) - cam_cinema_aim_z;

        int x_diff = cam_x - Camera.x;
        int z_diff = cam_z - Camera.z;
        int y_diff = cam_y - Camera.y;
        int length = (int) Math.sqrt(x_diff * x_diff + y_diff * y_diff);
        int cam_pitch = (int) (Math.atan2(z_diff, length) * 325.94900000000001D) & 0x7ff;
        int cam_yaw = (int) (Math.atan2(x_diff, y_diff) * -325.94900000000001D) & 0x7ff;

        if (cam_pitch < 128) {
            cam_pitch = 128;
        }

        if (cam_pitch > 383) {
            cam_pitch = 383;
        }

        if (Camera.pitch < cam_pitch) {
            Camera.pitch += cam_cinema_rot_base + ((cam_pitch - Camera.pitch) * cam_cinema_rot_modifier) / 1000;
            if (Camera.pitch > cam_pitch) {
                Camera.pitch = cam_pitch;
            }
        }

        if (Camera.pitch > cam_pitch) {
            Camera.pitch -= cam_cinema_rot_base + ((Camera.pitch - cam_pitch) * cam_cinema_rot_modifier) / 1000;
            if (Camera.pitch < cam_pitch) {
                Camera.pitch = cam_pitch;
            }
        }

        int yaw_diff = cam_yaw - Camera.yaw;

        if (yaw_diff > 1024) {
            yaw_diff -= 2048;
        }

        if (yaw_diff < -1024) {
            yaw_diff += 2048;
        }

        if (yaw_diff > 0) {
            Camera.yaw += cam_cinema_rot_base + (yaw_diff * cam_cinema_rot_modifier) / 1000;
            Camera.yaw &= 0x7ff;
        }
        if (yaw_diff < 0) {
            Camera.yaw -= cam_cinema_rot_base + (-yaw_diff * cam_cinema_rot_modifier) / 1000;
            Camera.yaw &= 0x7ff;
        }

        int yaw_dest = cam_yaw - Camera.yaw;

        if (yaw_dest > 1024) {
            yaw_dest -= 2048;
        }

        if (yaw_dest < -1024) {
            yaw_dest += 2048;
        }

        if (yaw_dest < 0 && yaw_diff > 0 || yaw_dest > 0 && yaw_diff < 0) {
            Camera.yaw = cam_yaw;
        }
    }

    public static void handle_connection_lost() {
        if (logout_cycle > 0) {
            net_disconnect();
            return;
        }

        producer_scene.prepare();
        BitmapFont.NORMAL.draw(JString.CONNECTION_LOST, 256, 143, 0xFFFFFF, BitmapFont.SHADOW_CENTER);
        BitmapFont.NORMAL.draw(JString.ATTEMPTING_TO_REESTABLISH, 256, 158, 0xFFFFFF, BitmapFont.SHADOW_CENTER);
        producer_scene.draw(4, 4);
        minimap_state = 0;
        map_marker_x = 0;
        logged_in = false;
        reconnection_attempts = 0;
        net_login(username, password, true);

        if (!logged_in) {
            net_disconnect();
        }

        try {
            connection.close();
        } catch (Exception e) {
        }
    }

    public static void handle_entity(Entity e) {
        if (e.scene_x < 128 || e.scene_y < 128 || e.scene_x >= 13184 || e.scene_y >= 13184) {
            e.seq_index = -1;
            e.spotanim_index = -1;
            e.move_cycle_end = 0;
            e.move_cycle_start = 0;
            e.scene_x = e.path_x[0] * 128 + e.size * 64;
            e.scene_y = e.path_y[0] * 128 + e.size * 64;
            e.reset_positions();
        }

        if (e == self && (e.scene_x < 1536 || e.scene_y < 1536 || e.scene_x >= 11776 || e.scene_y >= 11776)) {
            e.seq_index = -1;
            e.spotanim_index = -1;
            e.move_cycle_end = 0;
            e.move_cycle_start = 0;
            e.scene_x = e.path_x[0] * 128 + e.size * 64;
            e.scene_y = e.path_y[0] * 128 + e.size * 64;
            e.reset_positions();
        }

        if (e.move_cycle_end > loop_cycle) {
            handle_entity_late_movement(e);
        } else if (e.move_cycle_start >= loop_cycle) {
            handle_entity_movement_variables(e);
        } else {
            handle_entity_movement(e);
        }

        handle_entity_rotation(e);
        handle_entity_sequence(e);
    }

    public static void handle_entity_late_movement(Entity e) {
        int dt = e.move_cycle_end - loop_cycle;
        int dest_x = e.move_start_x * 128 + e.size * 64;
        int dest_y = e.move_start_y * 128 + e.size * 64;

        e.scene_x += (dest_x - e.scene_x) / dt;
        e.scene_y += (dest_y - e.scene_y) / dt;
        e.resync_walk_cycle = 0;

        if (e.move_direction == 0) {
            e.dest_rotation = 1024;
        } else if (e.move_direction == 1) {
            e.dest_rotation = 1536;
        } else if (e.move_direction == 2) {
            e.dest_rotation = 0;
        } else if (e.move_direction == 3) {
            e.dest_rotation = 512;
        }
    }

    public static void handle_entity_movement(Entity e) {
        e.move_seq_index = e.stand_animation;

        if (e.path_position == 0) {
            e.resync_walk_cycle = 0;
            return;
        }

        if (e.seq_index != -1 && e.seq_delay_cycle == 0) {
            Sequence a = Sequence.instance[e.seq_index];
            if (e.still_path_position > 0 && a.speed_flag == 0) {
                e.resync_walk_cycle++;
                return;
            }
            if (e.still_path_position <= 0 && a.walk_flag == 0) {
                e.resync_walk_cycle++;
                return;
            }
        }

        int scene_x = e.scene_x;
        int scene_y = e.scene_y;
        int dest_x = e.path_x[e.path_position - 1] * 128 + e.size * 64;
        int dest_y = e.path_y[e.path_position - 1] * 128 + e.size * 64;

        if (dest_x - scene_x > 256 || dest_x - scene_x < -256 || dest_y - scene_y > 256 || dest_y - scene_y < -256) {
            e.scene_x = dest_x;
            e.scene_y = dest_y;
            return;
        }

        if (scene_x < dest_x) {
            if (scene_y < dest_y) {
                e.dest_rotation = 1280;
            } else if (scene_y > dest_y) {
                e.dest_rotation = 1792;
            } else {
                e.dest_rotation = 1536;
            }
        } else if (scene_x > dest_x) {
            if (scene_y < dest_y) {
                e.dest_rotation = 768;
            } else if (scene_y > dest_y) {
                e.dest_rotation = 256;
            } else {
                e.dest_rotation = 512;
            }
        } else if (scene_y < dest_y) {
            e.dest_rotation = 1024;
        } else {
            e.dest_rotation = 0;
        }

        int angle_diff = e.dest_rotation - e.rotation & 0x7ff;

        if (angle_diff > 1024) {
            angle_diff -= 2048;
        }

        int index = e.turn_180_animation;

        if (angle_diff >= -256 && angle_diff <= 256) {
            index = e.walk_animation;
        } else if (angle_diff >= 256 && angle_diff < 768) {
            index = e.turn_l_animation;
        } else if (angle_diff >= -768 && angle_diff <= -256) {
            index = e.turn_r_animation;
        }

        if (index == -1) {
            index = e.walk_animation;
        }

        e.move_seq_index = index;

        int speed = 4;

        if (e.rotation != e.dest_rotation && e.face_entity == -1 && e.turn_speed != 0) {
            speed = 2;
        }

        if (e.path_position > 2) {
            speed = 6;
        }

        if (e.path_position > 3) {
            speed = 8;
        }

        if (e.resync_walk_cycle > 0 && e.path_position > 1) {
            speed = 8;
            e.resync_walk_cycle--;
        }

        if (e.path_run[e.path_position - 1]) {
            speed <<= 1;
        }

        if (speed >= 8 && e.move_seq_index == e.walk_animation && e.run_animation != -1) {
            e.move_seq_index = e.run_animation;
        }

        if (scene_x < dest_x) {
            e.scene_x += speed;
            if (e.scene_x > dest_x) {
                e.scene_x = dest_x;
            }
        } else if (scene_x > dest_x) {
            e.scene_x -= speed;
            if (e.scene_x < dest_x) {
                e.scene_x = dest_x;
            }
        }

        if (scene_y < dest_y) {
            e.scene_y += speed;
            if (e.scene_y > dest_y) {
                e.scene_y = dest_y;
            }
        } else if (scene_y > dest_y) {
            e.scene_y -= speed;
            if (e.scene_y < dest_y) {
                e.scene_y = dest_y;
            }
        }

        if (e.scene_x == dest_x && e.scene_y == dest_y) {
            e.path_position--;
            if (e.still_path_position > 0) {
                e.still_path_position--;
            }
        }
    }

    public static void handle_entity_movement_variables(Entity e) {
        if (e.move_cycle_start == loop_cycle || e.seq_index == -1 || e.seq_delay_cycle != 0 || e.seq_cycle + 1 > Sequence.instance[e.seq_index].get_frame_length(e.seq_frame)) {
            int walk_dt = e.move_cycle_start - e.move_cycle_end;
            int dt = loop_cycle - e.move_cycle_end;
            int start_x = e.move_start_x * 128 + e.size * 64;
            int start_y = e.move_start_y * 128 + e.size * 64;
            int end_x = e.move_end_x * 128 + e.size * 64;
            int end_y = e.move_end_y * 128 + e.size * 64;
            e.scene_x = (start_x * (walk_dt - dt) + end_x * dt) / walk_dt;
            e.scene_y = (start_y * (walk_dt - dt) + end_y * dt) / walk_dt;
        }

        e.resync_walk_cycle = 0;

        if (e.move_direction == 0) {
            e.dest_rotation = 1024;
        }

        if (e.move_direction == 1) {
            e.dest_rotation = 1536;
        }

        if (e.move_direction == 2) {
            e.dest_rotation = 0;
        }

        if (e.move_direction == 3) {
            e.dest_rotation = 512;
        }

        e.rotation = e.dest_rotation;
    }

    public static void handle_entity_rotation(Entity e) {
        if (e.turn_speed == 0) {
            return;
        }

        if (e.face_entity != -1 && e.face_entity < 32768) {
            Actor a = actors[e.face_entity];

            if (a != null) {
                int dx = e.scene_x - a.scene_x;
                int dy = e.scene_y - a.scene_y;

                if (dx != 0 || dy != 0) {
                    e.dest_rotation = (int) (Math.atan2(dx, dy) * 325.94900000000001D) & 0x7ff;
                }
            }
        }

        if (e.face_entity >= 32768) {
            int player_index = e.face_entity - 32768;

            if (player_index == local_player_index) {
                player_index = MAX_PLAYER_INDEX;
            }

            Player p = players[player_index];

            if (p != null) {
                int dx = e.scene_x - p.scene_x;
                int dy = e.scene_y - p.scene_y;

                if (dx != 0 || dy != 0) {
                    e.dest_rotation = (int) (Math.atan2(dx, dy) * 325.94900000000001D) & 0x7ff;
                }
            }
        }

        if ((e.face_x != 0 || e.face_y != 0) && (e.path_position == 0 || e.resync_walk_cycle > 0)) {
            int dx = e.scene_x - (e.face_x - map_base_x - map_base_x) * 64;
            int dy = e.scene_y - (e.face_y - map_base_y - map_base_y) * 64;

            if (dx != 0 || dy != 0) {
                e.dest_rotation = (int) (Math.atan2(dx, dy) * 325.94900000000001D) & 0x7ff;
            }

            e.face_x = 0;
            e.face_y = 0;
        }

        int da = e.dest_rotation - e.rotation & 0x7ff;

        if (da != 0) {
            if (da < e.turn_speed || da > 2048 - e.turn_speed) {
                e.rotation = e.dest_rotation;
            } else if (da > 1024) {
                e.rotation -= e.turn_speed;
            } else {
                e.rotation += e.turn_speed;
            }

            e.rotation &= 0x7ff;

            if (e.move_seq_index == e.stand_animation && e.rotation != e.dest_rotation) {
                if (e.stand_turn_animation != -1) {
                    e.move_seq_index = e.stand_turn_animation;
                    return;
                }
                e.move_seq_index = e.walk_animation;
            }
        }
    }

    public static void handle_entity_sequence(Entity e) {
        e.can_rotate = false;

        if (e.move_seq_index != -1) {
            Sequence s = Sequence.instance[e.move_seq_index];
            e.move_seq_cycle++;

            // If it's time to go to the next frame.
            if (e.move_seq_frame < s.frame_count && e.move_seq_cycle > s.get_frame_length(e.move_seq_frame)) {
                e.move_seq_cycle = 0;
                e.move_seq_frame++;
            }

            // If we've past the frame count in this sequence, reset the
            // animation.
            if (e.move_seq_frame >= s.frame_count) {
                e.move_seq_cycle = 0;
                e.move_seq_frame = 0;
            }
        }

        if (e.spotanim_index != -1 && loop_cycle >= e.spotanim_cycle_end) {
            if (e.spotanim_frame < 0) {
                e.spotanim_frame = 0;
            }

            Sequence s = null;

            if (e.spotanim_index >= 0 && e.spotanim_index < SpotAnimConfig.count) {
                s = SpotAnimConfig.instance[e.spotanim_index].seq;
            }

            if (s != null) {
                for (e.spotanim_cycle++; e.spotanim_frame < s.frame_count && e.spotanim_cycle > s.get_frame_length(e.spotanim_frame); e.spotanim_frame++) {
                    e.spotanim_cycle -= s.get_frame_length(e.spotanim_frame);
                }

                if (e.spotanim_frame >= s.frame_count && (e.spotanim_frame < 0 || e.spotanim_frame >= s.frame_count)) {
                    e.spotanim_index = -1;
                }
            } else {
                System.out.println("Error spotanim " + e.spotanim_index + " doesn't exist!");
                e.spotanim_index = -1;
            }
        }

        if (e.seq_index != -1 && e.seq_delay_cycle <= 1) {
            if (e.seq_index < 0 || e.seq_index >= Sequence.instance.length) {
                System.out.println("Error: sequence " + e.seq_index + " doesn't exist!");
                e.seq_index = -1;
            } else {
                Sequence s = Sequence.instance[e.seq_index];

                if (s.speed_flag == 1 && e.still_path_position > 0 && e.move_cycle_end <= loop_cycle && e.move_cycle_start < loop_cycle) {
                    e.seq_delay_cycle = 1;
                    return;
                }
            }
        }

        if (e.seq_index != -1 && e.seq_delay_cycle == 0) {
            if (e.seq_index < 0 || e.seq_index >= Sequence.instance.length) {
                System.out.println("Error: sequence " + e.seq_index + " doesn't exist!");
                e.seq_index = -1;
            } else {
                Sequence s = Sequence.instance[e.seq_index];

                for (e.seq_cycle++; e.seq_frame < s.frame_count && e.seq_cycle > s.get_frame_length(e.seq_frame); e.seq_frame++) {
                    e.seq_cycle -= s.get_frame_length(e.seq_frame);
                }

                if (e.seq_frame >= s.frame_count) {
                    e.seq_frame -= s.padding;
                    e.seq_reset_cycle++;

                    if (e.seq_reset_cycle >= s.reset_cycle) {
                        e.seq_index = -1;
                    }

                    if (e.seq_frame < 0 || e.seq_frame >= s.frame_count) {
                        e.seq_index = -1;
                    }
                }
                e.can_rotate = s.can_rotate;
            }
        }

        if (e.seq_delay_cycle > 0) {
            e.seq_delay_cycle--;
        }
    }

    public static void handle_keyboard() {
        do {
            int key = Keyboard.next();

            if (key == -1) {
                break;
            }

            if (widget_overlay != -1 && widget_overlay == report_abuse_windex) {
                if (key == KeyEvent.VK_BACK_SPACE && report_abuse_input.length() > 0) {
                    report_abuse_input = report_abuse_input.substring(0, report_abuse_input.length() - 1);
                }

                if ((key >= 97 && key <= 122 || key >= 65 && key <= 90 || key >= 48 && key <= 57 || key == 32) && report_abuse_input.length() < 12) {
                    report_abuse_input += (char) key;
                }
            } else {
                Chat.handle_keyboard(key);
            }
        } while (true);
    }

    public static void handle_loc(SpawntLoc sl) {
        int loc_uid = 0;
        int loc_index = -1;
        int loc_type = 0;
        int loc_rotation = 0;

        if (sl.class_type == 0) {
            loc_uid = landscape.get_wall_uid(sl.plane, sl.x, sl.y);
        }

        if (sl.class_type == 1) {
            loc_uid = landscape.get_wall_decoration_uid(sl.plane, sl.x, sl.y);
        }

        if (sl.class_type == 2) {
            loc_uid = landscape.get_loc_uid(sl.plane, sl.x, sl.y);
        }

        if (sl.class_type == 3) {
            loc_uid = landscape.get_ground_decoration_uid(sl.plane, sl.x, sl.y);
        }

        if (loc_uid != 0) {
            int uid = landscape.get_arrangement(sl.plane, sl.x, sl.y, loc_uid);
            loc_index = loc_uid >> 14 & 0x7fff;
            loc_type = uid & 0x1f;
            loc_rotation = uid >> 6;
        }

        sl.index = loc_index;
        sl.type = loc_type;
        sl.rotation = loc_rotation;
    }

    public static void handle_loc_creation() {
        if (scene_state == 2) {
            for (SpawntLoc sl = (SpawntLoc) spawned_locs.top(); sl != null; sl = (SpawntLoc) spawned_locs.next()) {
                if (sl.cycle > 0) {
                    sl.cycle--;
                }

                if (sl.cycle == 0) {
                    if (sl.index < 0 || Scene.loc_loaded(sl.index, sl.type)) {
                        add_loc(sl.y, sl.x, sl.plane, sl.index, sl.rotation, sl.type, sl.class_type);
                        sl.detach();
                    }
                } else {
                    if (sl.spawn_cycle > 0) {
                        sl.spawn_cycle--;
                    }

                    if (sl.spawn_cycle == 0 && sl.x >= 1 && sl.y >= 1 && sl.x <= 102 && sl.y <= 102 && (sl.loc_index < 0 || Scene.loc_loaded(sl.loc_index, sl.loc_type))) {
                        add_loc(sl.y, sl.x, sl.plane, sl.loc_index, sl.loc_rotation, sl.loc_type, sl.class_type);
                        sl.spawn_cycle = -1;

                        if (sl.loc_index == sl.index && sl.index == -1) {
                            sl.detach();
                        } else if (sl.loc_index == sl.index && sl.loc_rotation == sl.rotation && sl.loc_type == sl.type) {
                            sl.detach();
                        }
                    }
                }
            }

        }
    }

    public static void handle_locs() {
        SpawntLoc sl = (SpawntLoc) spawned_locs.top();
        for (; sl != null; sl = (SpawntLoc) spawned_locs.next()) {
            if (sl.cycle == -1) {
                sl.spawn_cycle = 0;
                handle_loc(sl);
            } else {
                sl.detach();
            }
        }
    }

    public static void handle_menu_option(int option) {
        if (option < 0) {
            return;
        }

        Chat.clear();

        int param1 = Menu.get_param(option, 0);
        int param2 = Menu.get_param(option, 1);
        int param3 = Menu.get_param(option, 2);
        int action = Menu.get_action(option);

        if (action >= 2000) {
            action -= 2000;
        }

        if (debug) {
            StringBuilder sb = new StringBuilder();
            sb.append("Action ").append(action).append(": ");
            sb.append(Arrays.toString(new int[]{param1, param2, param3}));
            System.out.println(sb.toString());
        }

        if (action == 582) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(57);
                out.put_short_a(selected_item_index);
                out.put_short_a(param3);
                out.put_le_short(selected_item_slot);
                out.put_short_a(selected_item_widget);
            }
        }

        if (action == 234) {
            boolean flag1 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag1) {
                flag1 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(236);
            out.put_le_short(param2 + map_base_y);
            out.put_short(param3);
            out.put_le_short(param1 + map_base_x);
        }

        if (action == 62 && interact_with_loc(param2, param1, param3)) {
            out.put_opcode(192);
            out.put_short(selected_item_widget);
            out.put_le_short(param3 >> 14 & 0x7fff);
            out.put_le_short_a(param2 + map_base_y);
            out.put_le_short(selected_item_slot);
            out.put_le_short_a(param1 + map_base_x);
            out.put_short(selected_item_index);
        }

        if (action == 511) {
            boolean flag2 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag2) {
                flag2 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(25);
            out.put_le_short(selected_item_widget);
            out.put_short_a(selected_item_index);
            out.put_short(param3);
            out.put_short_a(param2 + map_base_y);
            out.put_le_short_a(selected_item_slot);
            out.put_short(param1 + map_base_x);
        }

        if (action == 74) {
            out.put_opcode(122);
            out.put_le_short_a(param2);
            out.put_short_a(param1);
            out.put_le_short(param3);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 315) {
            if (param2 == 5985) {
                report_abuse_mute = !report_abuse_mute;
            } else {
                Widget w = Widget.get(param2);
                boolean valid = true;

                if (w.action_type > 0) {
                    valid = handle_widget(w);
                }

                if (valid) {
                    out.put_opcode(185);
                    out.put_short(param2);
                }
            }
        }

        if (action == 561) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(128);
                out.put_short(param3);
            }
        }

        if (action == 20) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(155);
                out.put_le_short(param3);
            }
        }

        if (action == 779) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(153);
                out.put_le_short(param3);
            }
        }

        if (action == 516) {
            if (!Menu.visible) {
                landscape.clicked(Mouse.click_y - 4, Mouse.click_x - 4);
            } else {
                landscape.clicked(param2 - 4, param1 - 4);
            }
        }

        if (action == 1062) {
            interact_with_loc(param2, param1, param3);
            out.put_opcode(228);
            out.put_short_a(param3 >> 14 & 0x7fff);
            out.put_short_a(param2 + map_base_y);
            out.put_short(param1 + map_base_x);
        }

        if (action == 679 && !dialogue_option_active) {
            out.put_opcode(40);
            out.put_short(param2);
            dialogue_option_active = true;
        }

        if (action == 431) {
            out.put_opcode(129);
            out.put_short_a(param1);
            out.put_short(param2);
            out.put_short_a(param3);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            } else if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 337 || action == 42 || action == 792 || action == 322) {
            String s = Menu.get_caption(option);
            int i = s.indexOf(JString.WHI);
            if (i != -1) {
                long l = JString.get_long(s.substring(i + 5).trim());
                switch (action) {
                    case 337: {
                        friend_add(l);
                        break;
                    }
                    case 792: {
                        friend_remove(l);
                        break;
                    }
                    case 42: {
                        ignore_add(l);
                        break;
                    }
                    case 322: {
                        ignore_remove(l);
                        break;
                    }
                }
            }
        }

        if (action == 53) {
            out.put_opcode(135);
            out.put_le_short(param1);
            out.put_short_a(param2);
            out.put_le_short(param3);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 539) {
            out.put_opcode(16);
            out.put_short_a(param3);
            out.put_le_short_a(param1);
            out.put_le_short_a(param2);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 484 || action == 6) {
            String s = Menu.get_caption(option);
            int j = s.indexOf(JString.WHI);

            if (j != -1) {
                s = s.substring(j + 5).trim();
                String name = JString.get_formatted_string(JString.get_long(s));
                boolean found = false;

                for (int i = 0; i < player_count; i++) {
                    Player p = players[player_indices[i]];

                    if (p == null || p.name == null || !p.name.equalsIgnoreCase(name)) {
                        continue;
                    }

                    walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);

                    if (action == 484) {
                        out.put_opcode(139);
                        out.put_le_short(player_indices[i]);
                    }

                    if (action == 6) {
                        out.put_opcode(128);
                        out.put_short(player_indices[i]);
                    }

                    found = true;
                    break;
                }

                if (!found) {
                    Chat.put("Unable to find " + name, 0);
                }
            }
        }

        if (action == 870) {
            out.put_opcode(53);
            out.put_short(param1);
            out.put_short_a(selected_item_slot);
            out.put_le_short_a(param3);
            out.put_short(selected_item_widget);
            out.put_le_short(selected_item_index);
            out.put_short(param2);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 847) {
            out.put_opcode(87);
            out.put_short_a(param3);
            out.put_short(param2);
            out.put_short_a(param1);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 626) {
            Widget w = Widget.instance[param2];
            selected_widget = true;
            selected_widget_index = param2;
            selected_mask = w.option_action;
            selected_item = false;
            Sidebar.draw = true;

            String prefix = w.option_prefix;

            if (prefix.indexOf(' ') != -1) {
                prefix = prefix.substring(0, prefix.indexOf(' '));
            }

            String suffix = w.option_prefix;

            if (suffix.indexOf(' ') != -1) {
                suffix = suffix.substring(suffix.indexOf(' ') + 1);
            }

            selected_tooltip = prefix + ' ' + w.option_suffix + ' ' + suffix;

            if (selected_mask == 16) {
                Sidebar.draw = true;
                Sidebar.open_tab(3);
                Sidebar.draw_tabs = true;
            }
            return;
        }

        if (action == 78) {
            out.put_opcode(117);
            out.put_le_short_a(param2);
            out.put_le_short_a(param3);
            out.put_le_short(param1);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 27) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(73);
                out.put_le_short(param3);
            }
        }

        if (action == 213) {
            boolean flag3 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag3) {
                flag3 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(79);
            out.put_le_short(param2 + map_base_y);
            out.put_short(param3);
            out.put_short_a(param1 + map_base_x);
        }

        if (action == 632) {
            out.put_opcode(145);
            out.put_short_a(param2);
            out.put_short_a(param1);
            out.put_short_a(param3);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            } else if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 493) {
            out.put_opcode(75);
            out.put_le_short_a(param2);
            out.put_le_short(param1);
            out.put_short_a(param3);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            } else if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 652) {
            boolean flag4 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag4) {
                flag4 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(156);
            out.put_short_a(param1 + map_base_x);
            out.put_le_short(param2 + map_base_y);
            out.put_le_short_a(param3);
        }

        if (action == 94) {
            boolean flag5 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag5) {
                flag5 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(181);
            out.put_le_short(param2 + map_base_y);
            out.put_short(param3);
            out.put_le_short(param1 + map_base_x);
            out.put_short_a(selected_widget_index);
        }

        if (action == 646) {
            out.put_opcode(185);
            out.put_short(param2);
            Widget w = Widget.instance[param2];

            if (w.script != null && w.script[0].intcode[0] == 5) {
                int setting = w.script[0].intcode[1];

                if (settings[setting] != w.script[0].compare_value) {
                    settings[setting] = w.script[0].compare_value;
                    handle_varp(setting);
                    Sidebar.draw = true;
                }
            }
        }

        if (action == 225) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(17);
                out.put_le_short_a(param3);
            }
        }

        if (action == 965) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(21);
                out.put_short(param3);
            }
        }

        if (action == 413) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(131);
                out.put_le_short_a(param3);
                out.put_short_a(selected_widget_index);
            }
        }

        if (action == 200) {
            close_widgets();
        }

        if (action == 1025) {
            Actor a = actors[param3];

            if (a != null) {
                ActorConfig c = a.config;

                if (c.override_index != null) {
                    c = c.get_overriding_config();
                }

                if (c != null) {
                    Chat.put(c.description != null ? c.description : "It's a " + c.name + ".");
                }
            }
        }

        if (action == 900) {
            interact_with_loc(param2, param1, param3);
            out.put_opcode(252);
            out.put_le_short_a(param3 >> 14 & 0x7fff);
            out.put_le_short(param2 + map_base_y);
            out.put_short_a(param1 + map_base_x);
        }

        if (action == 412) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(72);
                out.put_short_a(param3);
            }
        }

        if (action == 365) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(249);
                out.put_short_a(param3);
                out.put_le_short(selected_widget_index);
            }
        }

        if (action == 729) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(39);
                out.put_le_short(param3);
            }
        }

        if (action == 577) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(139);
                out.put_le_short(param3);
            }
        }

        if (action == 956 && interact_with_loc(param2, param1, param3)) {
            out.put_opcode(35);
            out.put_le_short(param1 + map_base_x);
            out.put_short_a(selected_widget_index);
            out.put_short_a(param2 + map_base_y);
            out.put_le_short(param3 >> 14 & 0x7fff);
        }

        if (action == 567) {
            boolean flag6 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag6) {
                flag6 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(23);
            out.put_le_short(param2 + map_base_y);
            out.put_le_short(param3);
            out.put_le_short(param1 + map_base_x);
        }

        if (action == 867) {
            out.put_opcode(43);
            out.put_le_short(param2);
            out.put_short_a(param3);
            out.put_short_a(param1);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 543) {
            out.put_opcode(237);
            out.put_short(param1);
            out.put_short_a(param3);
            out.put_short(param2);
            out.put_short_a(selected_widget_index);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 606) {
            String s = Menu.get_caption(option);
            int j = s.indexOf(JString.WHI);

            if (j != -1) {
                if (widget_overlay == -1) {
                    close_widgets();
                    report_abuse_input = s.substring(j + 5).trim();
                    report_abuse_mute = false;

                    for (int i = 0; i < Widget.instance.length; i++) {
                        if (Widget.instance[i] == null || Widget.instance[i].action_type != 600) {
                            continue;
                        }
                        report_abuse_windex = widget_overlay = Widget.instance[i].parent;
                        break;
                    }

                } else {
                    Chat.put("Please close the interface you have open before using 'report abuse'", 0);
                }
            }
        }

        if (action == 491) {
            Player p = players[param3];
            if (p != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], p.path_x[0], p.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(14);
                out.put_short_a(selected_item_widget);
                out.put_short(param3);
                out.put_short(selected_item_index);
                out.put_le_short(selected_item_slot);
            }
        }

        if (action == 639) {
            String s = Menu.get_caption(option);
            int j = s.indexOf(JString.WHI);
            if (j != -1) {
                long l = JString.get_long(s.substring(j + 5).trim());
                int friend_index = -1;
                for (int i = 0; i < friend_count; i++) {
                    if (friend_long[i] != l) {
                        continue;
                    }
                    friend_index = i;
                    break;
                }

                if (friend_index != -1 && friend_node[friend_index] > 0) {
                    message_recipient_name_long = friend_long[friend_index];
                    Chat.set(State.SEND_MESSAGE, "Enter a message to send to " + friend_name[friend_index]);
                }
            }
        }

        if (action == 454) {
            out.put_opcode(41);
            out.put_short(param3);
            out.put_short_a(param1);
            out.put_short_a(param2);
            redraw_cycle = 0;
            clicked_item_widget = param2;
            clicked_item_slot = param1;
            click_area = 2;

            if (Widget.instance[param2].parent == widget_overlay) {
                click_area = 1;
            }

            if (Widget.instance[param2].parent == Chat.get_overlay()) {
                click_area = 3;
            }
        }

        if (action == 478) {
            Actor a = actors[param3];
            if (a != null) {
                walk_to(2, 1, 1, self.path_x[0], self.path_y[0], a.path_x[0], a.path_y[0], 0, 0, 0, false);
                cross_x = Mouse.click_x;
                cross_y = Mouse.click_y;
                cross_type = 2;
                cross_cycle = 0;
                out.put_opcode(18);
                out.put_le_short(param3);
            }
        }

        if (action == 113) {
            interact_with_loc(param2, param1, param3);
            out.put_opcode(70);
            out.put_le_short(param1 + map_base_x);
            out.put_short(param2 + map_base_y);
            out.put_le_short_a(param3 >> 14 & 0x7fff);
        }

        if (action == 872) {
            interact_with_loc(param2, param1, param3);
            out.put_opcode(234);
            out.put_le_short_a(param1 + map_base_x);
            out.put_short_a(param3 >> 14 & 0x7fff);
            out.put_le_short_a(param2 + map_base_y);
        }

        if (action == 502) {
            interact_with_loc(param2, param1, param3);
            out.put_opcode(132);
            out.put_le_short_a(param1 + map_base_x);
            out.put_short(param3 >> 14 & 0x7fff);
            out.put_short_a(param2 + map_base_y);
        }

        if (action == 1125) {
            ObjConfig oc = ObjConfig.get(param3);
            Widget w = Widget.instance[param2];

            if (w != null && oc != null) {
                if (w.item_count[param1] >= 100_000) {
                    Chat.put(w.item_count[param1] + " x " + oc.name);
                } else {
                    Chat.put(oc.description != null ? oc.description : "It's a " + oc.name + ".");
                }
            }
        }

        if (action == 169) {
            out.put_opcode(185);
            out.put_short(param2);
            Widget w = Widget.instance[param2];

            if (w.script != null && w.script[0].intcode[0] == 5) {
                int pointer = w.script[0].intcode[1];
                settings[pointer] = 1 - settings[pointer];
                handle_varp(pointer);
                Sidebar.draw = true;
            }
        }

        if (action == 447) {
            selected_item = true;
            selected_item_slot = param1;
            selected_item_widget = param2;
            selected_item_index = param3;
            selected_item_name = ObjConfig.get(param3).name;
            selected_widget = false;
            Sidebar.draw = true;
            return;
        }

        if (action == 1226) {
            LocConfig c = LocConfig.get(param3 >> 14 & 0x7fff);

            if (c != null) {
                Chat.put(c.description != null ? c.description : "It's a " + c.name + ".");
            }
        }

        if (action == 244) {
            boolean flag7 = walk_to(2, 0, 0, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            if (!flag7) {
                flag7 = walk_to(2, 1, 1, self.path_x[0], self.path_y[0], param1, param2, 0, 0, 0, false);
            }
            cross_x = Mouse.click_x;
            cross_y = Mouse.click_y;
            cross_type = 2;
            cross_cycle = 0;
            out.put_opcode(253);
            out.put_le_short(param1 + map_base_x);
            out.put_le_short_a(param2 + map_base_y);
            out.put_short_a(param3);
        }

        if (action == 1448) {
            ObjConfig c = ObjConfig.get(param3);

            if (c != null) {
                Chat.put(c.description != null ? c.description : "It's a " + c.name + ".");
            }
        }

        selected_item = false;
        selected_widget = false;
        Sidebar.draw = true;
    }

    public static void handle_message_status() {
        message_status = 0;

        int x = (self.scene_x >> 7) + map_base_x;
        int y = (self.scene_y >> 7) + map_base_y;

        if (x >= 3053 && x <= 3156 && y >= 3056 && y <= 3136) {
            message_status = 1;
        }

        if (x >= 3072 && x <= 3118 && y >= 9492 && y <= 9535) {
            message_status = 1;
        }

        if (message_status == 1 && x >= 3139 && x <= 3199 && y >= 3008 && y <= 3062) {
            message_status = 0;
        }
    }

    public static void handle_minimap_mouse() {
        if (minimap_state != 0) {
            return;
        }
        if (Mouse.click_button == 1) {
            int x = Mouse.click_x - 25 - 550;
            int y = Mouse.click_y - 5 - 4;
            if (x >= 0 && y >= 0 && x < 146 && y < 151) {
                x -= 73;
                y -= 75;
                int angle = chase_cam_yaw + cam_yaw_off & 0x7ff;
                int sin = Canvas3D.sin[angle];
                int cos = Canvas3D.cos[angle];
                sin = sin * (map_zoom_offset + 256) >> 8;
                cos = cos * (map_zoom_offset + 256) >> 8;
                int k1 = y * sin + x * cos >> 11;
                int l1 = y * cos - x * sin >> 11;
                int dest_x = self.scene_x + k1 >> 7;
                int dest_y = self.scene_y - l1 >> 7;
                walk_to(1, 0, 0, self.path_x[0], self.path_y[0], dest_x, dest_y, 0, 0, 0, true);
            }
        }
    }

    public static void handle_mouse() {
        tmp_hovered_widget = 0;

        if (Mouse.within(Area.VIEWPORT)) {
            if (widget_overlay != -1) {
                Game.handle_widget_mouse(Widget.get(widget_overlay), 4, 4, Mouse.last_x, Mouse.last_y, 0);
            } else {
                handle_viewport_mouse();
            }
        }

        if (tmp_hovered_widget != hovered_viewport_widget) {
            hovered_viewport_widget = tmp_hovered_widget;
        }

        tmp_hovered_widget = 0;

        if (Mouse.within(Area.TAB)) {
            if (Sidebar.widget_index != -1) {
                handle_widget_mouse(Widget.instance[Sidebar.widget_index], 553, 205, Mouse.last_x, Mouse.last_y, 0);
            } else {
                handle_widget_mouse(Widget.instance[Sidebar.selected_tab.widget], 553, 205, Mouse.last_x, Mouse.last_y, 0);
            }
        }

        if (tmp_hovered_widget != hovered_tab_widget) {
            Sidebar.draw = true;
            hovered_tab_widget = tmp_hovered_widget;
        }

        tmp_hovered_widget = 0;

        if (Mouse.within(Area.CHAT)) {
            if (Chat.get_overlay() != -1) {
                handle_widget_mouse(Widget.instance[Chat.get_overlay()], 17, 357, Mouse.last_x, Mouse.last_y, 0);
            } else if (Mouse.last_y < 434 && Mouse.last_x < 426) {
                Chat.handle_mouse(Mouse.last_x - 17, Mouse.last_y - 357);
            }
        }

        if (Chat.get_overlay() != -1 && tmp_hovered_widget != hovered_chat_widget) {
            Chat.redraw = true;
            hovered_chat_widget = tmp_hovered_widget;
        }

        Mouse.wheel_amount = 0;
    }

    public static boolean handle_net() {
        if (connection == null) {
            return false;
        }

        try {
            int available = connection.get_available();

            if (available == 0) {
                return false;
            }

            if (Game.ptype == -1) {
                connection.get_bytes(in.payload, 0, 1);
                ptype = in.payload[0] & 0xff;

                if (connection_isaac != null) {
                    ptype = ptype - connection_isaac.nextInt() & 0xff;
                }

                psize = Packet.SIZE[ptype];
                available--;
            }

            if (psize == -1) {
                if (available > 0) {
                    connection.get_bytes(in.payload, 0, 1);
                    psize = in.payload[0] & 0xff;
                    available--;
                } else {
                    return false;
                }
            }

            if (psize == -2) {
                if (available > 1) {
                    connection.get_bytes(in.payload, 0, 2);
                    in.position = 0;
                    psize = in.get_ushort();
                    available -= 2;
                } else {
                    return false;
                }
            }

            if (available < psize) {
                return false;
            }

            in.position = 0;
            connection.get_bytes(in.payload, 0, psize);
            current_bytes_read += psize + 1;
            net_cycle = 0;
            last_ptype3 = last_ptype2;
            last_ptype2 = last_ptype1;
            last_ptype1 = ptype;

            if (Game.ptype == 81) {
                update_players(psize, in);
                scene_loading = false;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 176) {
                welcome_info = in.get_ubyte_c();
                welcome_unread_messages = in.get_ushort_a();
                welcome_notify = in.get_ubyte();
                welcome_last_ip = in.get_ime_int();
                welcome_last_playdate = in.get_ushort();

                if (welcome_last_ip != 0 && widget_overlay == -1) {
                    Signlink.get_dns(JString.get_address(welcome_last_ip));
                    close_widgets();
                    char c = '\u028A';

                    if (welcome_info != 201 || welcome_notify == 1) {
                        c = '\u028F';
                    }

                    report_abuse_input = "";
                    report_abuse_mute = false;
                    for (int i = 0; i < Widget.instance.length; i++) {
                        if (Widget.instance[i] == null || Widget.instance[i].action_type != c) {
                            continue;
                        }
                        widget_overlay = Widget.instance[i].parent;
                        break;
                    }

                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 64) {
                net_region_x = in.get_ubyte_c();
                net_region_y = in.get_ubyte_s();

                for (int x = net_region_x; x < net_region_x + 8; x++) {
                    for (int y = net_region_y; y < net_region_y + 8; y++) {
                        if (Game.item_pile[plane][x][y] != null) {
                            Game.item_pile[plane][x][y] = null;
                            update_item_pile(x, y);
                        }
                    }

                }

                for (SpawntLoc sl = (SpawntLoc) spawned_locs.top(); sl != null; sl = (SpawntLoc) spawned_locs.next()) {
                    if (sl.x >= net_region_x && sl.x < net_region_x + 8 && sl.y >= net_region_y && sl.y < net_region_y + 8 && sl.plane == plane) {
                        sl.cycle = 0;
                    }
                }

                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the disabled mesh index of a widget.
            if (Game.ptype == 185) {
                int index = in.get_le_ushort_a();
                Widget w = Widget.get(index);

                if (w != null) {
                    w.model_type_disabled = 3;
                    if (self.actor_override != null) {
                        w.model_index_disabled = (int) (0x12345678L + self.actor_override.index);
                    } else {
                        w.model_index_disabled = (int) (0x12345678L + self.actor_override.index);
                    }
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Stops camera shake and cinematic mode.
            if (Game.ptype == 107) {
                cam_cinema_mode = false;

                for (int i = 0; i < 5; i++) {
                    aBooleanArray876[i] = false;
                }

                Game.ptype = -1;
                return true;
            }

            // INFO: Clears the items of an inventory.
            if (Game.ptype == 72) {
                int index = in.get_le_ushort();
                Widget w = Widget.instance[index];

                if (w != null) {
                    for (int i = 0; i < w.item_index.length; i++) {
                        w.item_index[i] = -1;
                        w.item_index[i] = 0;
                    }
                }

                Game.ptype = -1;
                return true;
            }

            // INFO: Loads the ignore list.
            if (Game.ptype == 214) {
                ignore_count = psize / 8;

                for (int i = 0; i < ignore_count; i++) {
                    ignore_long[i] = in.get_long();
                }

                Game.ptype = -1;
                return true;
            }

            // INFO: Moves the camera cinematically
            if (Game.ptype == 166) {
                cam_cinema_mode = true;
                cam_cinema_dest_x = in.get_ubyte();
                cam_cinema_dest_y = in.get_ubyte();
                cam_cinema_dest_z = in.get_ushort();
                cam_cinema_base_speed = in.get_ubyte();
                cam_cinema_speed = in.get_ubyte();
                if (cam_cinema_speed >= 100) {
                    Camera.x = cam_cinema_dest_x * 128 + 64;
                    Camera.y = cam_cinema_dest_y * 128 + 64;
                    Camera.z = get_land_z(Camera.x, Camera.y, plane) - cam_cinema_dest_z;
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the stat level/experience.
            if (Game.ptype == 134) {
                int stat_index = in.get_ubyte();
                int stat_experience = in.get_me_int();
                int stat_level = in.get_ubyte();
                skill_experience[stat_index] = stat_experience;
                skill_level[stat_index] = stat_level;
                skill_real_level[stat_index] = 1;
                for (int i = 0; i < 98; i++) {
                    if (stat_experience >= XP_TABLE[i]) {
                        skill_real_level[stat_index] = i + 2;
                    }
                }
                Sidebar.draw = true;
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the tab widget.
            if (Game.ptype == 71) {
                int widget = in.get_ushort();
                int tab = in.get_ubyte_a();

                if (widget == 65535) {
                    widget = -1;
                }

                Sidebar.TAB[tab].widget = widget;
                Sidebar.draw = true;
                Sidebar.draw_tabs = true;
                Game.ptype = -1;
                return true;
            }

            // INFO: Plays a song.
            if (Game.ptype == 74) {
                int song = in.get_le_ushort();

                if (song == 65535) {
                    song = -1;
                }

                if (song != -1 || music.tmpDelay != 0) {
                    if (song != -1 && music.currentSong != song && music.volume0 != 0 && music.tmpDelay == 0) {
                        play_music(0, 10, music.volume0, false, 0, song);
                    } else {
                        stop_music(false);
                    }
                }

                music.currentSong = song;
                Game.ptype = -1;
                return true;
            }

            // INFO: Plays temporary music.
            if (Game.ptype == 121) {
                int song = in.get_le_ushort_a();
                int delay = in.get_ushort_a();

                if (song == 65535) {
                    song = -1;
                }

                if (music.volume0 != 0 && song != -1) {
                    play_music_instantly(music.volume0, song, 0, 1, false);
                    music.tmpDelay = delay;
                }

                Game.ptype = -1;
                return true;
            }

            // INFO: Forcefully disconnects.
            if (Game.ptype == 109) {
                net_disconnect();
                Game.ptype = -1;
                return false;
            }

            // INFO: Offsets the specified widget.
            if (Game.ptype == 70) {
                int x = in.get_short();
                int y = in.get_le_short();
                int index = in.get_le_ushort();
                Widget w = Widget.instance[index];

                if (w != null) {
                    w.x = (short) x;
                    w.y = (short) y;
                }

                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 73 || ptype == 241) {
                int region_x = Game.loaded_region_x;
                int region_y = Game.loaded_region_y;

                if (Game.ptype == 73) {
                    region_x = in.get_ushort_a();
                    region_y = in.get_ushort();
                    server_sent_chunk = false;
                }

                // INFO: Modified protocol
                if (Game.ptype == 241) {
                    region_y = in.get_ushort();

                    in.start_bit_access();
                    for (int plane = 0; plane < 4; plane++) {
                        for (int chunk_x = 0; chunk_x < 13; chunk_x++) {
                            for (int chunk_y = 0; chunk_y < 13; chunk_y++) {
                                int i = in.get_bits(1);
                                if (i == 1) {
                                    region_chunk_uids[plane][chunk_x][chunk_y] = in.get_bits(26);
                                } else {
                                    region_chunk_uids[plane][chunk_x][chunk_y] = -1;
                                }
                            }

                        }

                    }
                    in.stop_bit_access();

                    region_x = in.get_ushort_a();
                    server_sent_chunk = true;
                }

                if (Game.loaded_region_x == region_x && Game.loaded_region_y == region_y && scene_state == 2) {
                    Game.ptype = -1;
                    return true;
                }

                Game.loaded_region_x = region_x;
                Game.loaded_region_y = region_y;
                Game.map_base_x = (Game.loaded_region_x - 6) * 8;
                Game.map_base_y = (Game.loaded_region_y - 6) * 8;
                Game.restrict_region = false;

                if ((Game.loaded_region_x / 8 == 48 || Game.loaded_region_x / 8 == 49) && Game.loaded_region_y / 8 == 48) {
                    restrict_region = true;
                }

                if (Game.loaded_region_x / 8 == 48 && Game.loaded_region_y / 8 == 148) {
                    restrict_region = true;
                }

                scene_state = 1;
                scene_load_start = System.currentTimeMillis();
                producer_scene.prepare();
                BitmapFont.NORMAL.draw("Loading - please wait.", 256, 150, 0xFFFFFF, BitmapFont.SHADOW_CENTER);
                producer_scene.draw(4, 4);

                if (Game.ptype == 73) {
                    int count = 0;

                    for (int chunk_x = (Game.loaded_region_x - 6) / 8; chunk_x <= (Game.loaded_region_x + 6) / 8; chunk_x++) {
                        for (int chunk_y = (Game.loaded_region_y - 6) / 8; chunk_y <= (Game.loaded_region_y + 6) / 8; chunk_y++) {
                            count++;
                        }
                    }

                    chunk_loc_payload = new byte[count][];
                    chunk_landscape_payload = new byte[count][];
                    chunk_coords = new int[count];
                    map_uids = new int[count];
                    landscape_uids = new int[count];
                    count = 0;

                    for (int chunk_x = (Game.loaded_region_x - 6) / 8; chunk_x <= (Game.loaded_region_x + 6) / 8; chunk_x++) {
                        for (int chunk_y = (Game.loaded_region_y - 6) / 8; chunk_y <= (Game.loaded_region_y + 6) / 8; chunk_y++) {
                            chunk_coords[count] = (chunk_x << 8) + chunk_y;

                            if (restrict_region && (chunk_y == 49 || chunk_y == 149 || chunk_y == 147 || chunk_x == 50 || chunk_x == 49 && chunk_y == 47)) {
                                map_uids[count] = -1;
                                landscape_uids[count] = -1;
                                count++;
                            } else {
                                int map_uid = map_uids[count] = ondemand.get_map_uid(chunk_x, chunk_y, 0);

                                if (map_uid != -1) {
                                    ondemand.send_request(3, map_uid);
                                }

                                int landscape_uid = landscape_uids[count] = ondemand.get_map_uid(chunk_x, chunk_y, 1);

                                if (landscape_uid != -1) {
                                    ondemand.send_request(3, landscape_uid);
                                }
                                count++;
                            }
                        }
                    }
                } else if (Game.ptype == 241) {
                    int count = 0;
                    int chunk_coords[] = new int[26 * 26];

                    for (int plane = 0; plane < 4; plane++) {
                        for (int x = 0; x < 13; x++) {
                            for (int y = 0; y < 13; y++) {
                                int chunk_uid = region_chunk_uids[plane][x][y];
                                if (chunk_uid != -1) {
                                    int chunk_x = chunk_uid >> 14 & 0x3ff;
                                    int chunk_y = chunk_uid >> 3 & 0x7ff;
                                    int chunk_coord = (chunk_x / 8 << 8) + chunk_y / 8;
                                    for (int i = 0; i < count; i++) {
                                        if (chunk_coords[i] != chunk_coord) {
                                            continue;
                                        }
                                        chunk_coord = -1;
                                        break;
                                    }

                                    if (chunk_coord != -1) {
                                        chunk_coords[count++] = chunk_coord;
                                    }
                                }
                            }
                        }
                    }

                    chunk_loc_payload = new byte[count][];
                    chunk_landscape_payload = new byte[count][];
                    Game.chunk_coords = new int[count];
                    map_uids = new int[count];
                    landscape_uids = new int[count];

                    for (int i = 0; i < count; i++) {
                        int chunk_coord = Game.chunk_coords[i] = chunk_coords[i];
                        int chunk_x = chunk_coord >> 8 & 0xFF;
                        int chunk_y = chunk_coord & 0xFF;

                        int map_uid = map_uids[i] = ondemand.get_map_uid(chunk_x, chunk_y, 0);

                        if (map_uid != -1) {
                            ondemand.send_request(3, map_uid);
                        }

                        int landscape_uid = landscape_uids[i] = ondemand.get_map_uid(chunk_x, chunk_y, 1);

                        if (landscape_uid != -1) {
                            ondemand.send_request(3, landscape_uid);
                        }
                    }
                }

                int base_dx = map_base_x - last_map_base_x;
                int base_dy = map_base_y - last_map_base_y;
                last_map_base_x = map_base_x;
                last_map_base_y = map_base_y;

                for (int i = 0; i < 16384; i++) {
                    Actor a = actors[i];
                    if (a != null) {
                        for (int j = 0; j < 10; j++) {
                            a.path_x[j] -= base_dx;
                            a.path_y[j] -= base_dy;
                        }
                        a.scene_x -= base_dx * 128;
                        a.scene_y -= base_dy * 128;
                    }
                }

                for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
                    Player p = players[i];
                    if (p != null) {
                        for (int j = 0; j < 10; j++) {
                            p.path_x[j] -= base_dx;
                            p.path_y[j] -= base_dy;
                        }
                        p.scene_x -= base_dx * 128;
                        p.scene_y -= base_dy * 128;
                    }
                }

                scene_loading = true;

                byte x1 = 0;
                byte x2 = 104;
                byte dx = 1;

                if (base_dx < 0) {
                    x1 = 103;
                    x2 = -1;
                    dx = -1;
                }

                byte y1 = 0;
                byte y2 = 104;
                byte dy = 1;

                if (base_dy < 0) {
                    y1 = 103;
                    y2 = -1;
                    dy = -1;
                }

                if (Game.item_pile != null) {
                    for (int x = x1; x != x2; x += dx) {
                        for (int y = y1; y != y2; y += dy) {
                            int old_x = x + base_dx;
                            int old_y = y + base_dy;
                            for (int plane = 0; plane < 4; plane++) {
                                if (old_x >= 0 && old_y >= 0 && old_x < 104 && old_y < 104) {
                                    Game.item_pile[plane][x][y] = Game.item_pile[plane][old_x][old_y];
                                } else {
                                    Game.item_pile[plane][x][y] = null;
                                }
                            }
                        }
                    }
                }

                for (SpawntLoc sl = (SpawntLoc) spawned_locs.top(); sl != null; sl = (SpawntLoc) spawned_locs.next()) {
                    sl.x -= base_dx;
                    sl.y -= base_dy;
                    if (sl.x < 0 || sl.y < 0 || sl.x >= 104 || sl.y >= 104) {
                        sl.detach();
                    }
                }

                if (map_marker_x != 0) {
                    map_marker_x -= base_dx;
                    map_marker_y -= base_dy;
                }

                cam_cinema_mode = false;
                Game.ptype = -1;
                return true;
            }

            // INFO: Resets the animations of the widget.
            if (Game.ptype == 208) {
                int index = in.get_le_short();
                if (index >= 0) {
                    Widget.reset_animations(index);
                }
                widget_underlay = index;
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the minimap state.
            if (Game.ptype == 99) {
                minimap_state = in.get_ubyte();
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the widget's disabled mesh.
            if (Game.ptype == 75) {
                int mesh_index = in.get_le_ushort_a();
                int index = in.get_le_ushort_a();
                Widget.instance[index].model_type_disabled = 2;
                Widget.instance[index].model_index_disabled = mesh_index;
                Game.ptype = -1;
                return true;
            }

            // Info: Sets the next update time.
            if (Game.ptype == 114) {
                next_update = System.currentTimeMillis() + (in.get_le_ushort() * 1000);
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 60) {
                net_region_x = in.get_ubyte();
                net_region_y = in.get_ubyte_c();

                while (in.position < psize) {
                    handle_packet(in, in.get_ubyte());
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 35) {
                int l3 = in.get_ubyte();
                int k11 = in.get_ubyte();
                int j17 = in.get_ubyte();
                int k21 = in.get_ubyte();
                aBooleanArray876[l3] = true;
                anIntArray873[l3] = k11;
                anIntArray1203[l3] = j17;
                anIntArray928[l3] = k21;
                anIntArray1030[l3] = 0;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 174) {
                int index = in.get_ushort();
                int type = in.get_ubyte();
                int delay = in.get_ushort();
                if (!low_detail && sound_count < 50) {
                    sound_index[sound_count] = index;
                    sound_type[sound_count] = type;
                    sound_delay[sound_count] = delay + WaveSound.delay[index];
                    sound_count++;
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Changes a player option.
            if (Game.ptype == 104) {
                int option = in.get_ubyte_c();
                int priority = in.get_ubyte_a();
                String action = in.get_string();

                if (option >= 1 && option <= 5) {
                    if (action.equalsIgnoreCase("null")) {
                        action = null;
                    }
                    player_action[option - 1] = action;
                    player_action_priority[option - 1] = priority == 0;
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Removes the map marker.
            if (Game.ptype == 78) {
                map_marker_x = 0;
                Game.ptype = -1;
                return true;
            }

            // INFO: Adds a message to the chatbox.
            if (Game.ptype == 253) {
                String message = in.get_string();

                if (message.endsWith(":tradereq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long l = JString.get_long(name);
                    boolean ignored = false;

                    for (int i = 0; i < ignore_count; i++) {
                        if (ignore_long[i] != l) {
                            continue;
                        }
                        ignored = true;
                        break;
                    }

                    if (!ignored && message_status == 0) {
                        Chat.put(name, "wishes to trade with you.", Chat.TYPE_TRADE_REQUEST);
                    }
                } else if (message.endsWith(":duelreq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long l = JString.get_long(name);
                    boolean ignored = false;

                    for (int i = 0; i < ignore_count; i++) {
                        if (ignore_long[i] != l) {
                            continue;
                        }
                        ignored = true;
                        break;
                    }

                    if (!ignored && message_status == 0) {
                        Chat.put(name, "wishes to duel with you.", Chat.TYPE_DUEL_REQUEST);
                    }
                } else if (message.endsWith(":chalreq:")) {
                    String name = message.substring(0, message.indexOf(":"));
                    long l = JString.get_long(name);
                    boolean ignored = false;

                    for (int i = 0; i < ignore_count; i++) {
                        if (ignore_long[i] != l) {
                            continue;
                        }
                        ignored = true;
                        break;
                    }

                    if (!ignored && message_status == 0) {
                        Chat.put(name, message.substring(message.indexOf(":") + 1, message.length() - 9), 8);
                    }
                } else {
                    Chat.put(message, 0);
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Resets all actor animations.
            if (Game.ptype == 1) {
                for (int i = 0; i < players.length; i++) {
                    if (players[i] != null) {
                        players[i].seq_index = -1;
                    }
                }
                for (int i = 0; i < actors.length; i++) {
                    if (actors[i] != null) {
                        actors[i].seq_index = -1;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 50) {
                long l = in.get_long();
                int world = in.get_ubyte();
                String name = JString.get_formatted_string(l);

                for (int i = 0; i < friend_count; i++) {
                    if (l != friend_long[i]) {
                        continue;
                    }
                    if (friend_node[i] != world) {
                        friend_node[i] = world;
                        Sidebar.draw = true;
                        if (world > 0) {
                            Chat.put(name + " has logged in.", Chat.TYPE_NOTIFY_PRIVATE);
                        }
                        if (world == 0) {
                            Chat.put(name + " has logged out.", Chat.TYPE_NOTIFY_PRIVATE);
                        }
                    }
                    name = null;
                    break;
                }

                if (name != null && friend_count < 200) {
                    friend_long[friend_count] = l;
                    friend_name[friend_count] = name;
                    friend_node[friend_count] = world;
                    friend_count++;
                    Sidebar.draw = true;
                }

                for (boolean flag6 = false; !flag6; ) {
                    flag6 = true;
                    for (int i = 0; i < friend_count - 1; i++) {
                        if (friend_node[i] != node_index && friend_node[i + 1] == node_index || friend_node[i] == 0 && friend_node[i + 1] != 0) {
                            int j31 = friend_node[i];
                            friend_node[i] = friend_node[i + 1];
                            friend_node[i + 1] = j31;
                            String s10 = friend_name[i];
                            friend_name[i] = friend_name[i + 1];
                            friend_name[i + 1] = s10;
                            long l32 = friend_long[i];
                            friend_long[i] = friend_long[i + 1];
                            friend_long[i + 1] = l32;
                            Sidebar.draw = true;
                            flag6 = false;
                        }
                    }

                }

                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 110) {
                if (Sidebar.selected_tab.index == 12) {
                    Sidebar.draw = true;
                }

                Game.energy_left = in.get_ubyte();
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 254) {
                Game.mark_type = in.get_ubyte();

                if (Game.mark_type == 1) {
                    Game.mark_actor_index = in.get_ushort();
                }

                if (Game.mark_type >= 2 && Game.mark_type <= 6) {
                    if (Game.mark_type == 2) {
                        Game.mark_off_x = 64;
                        Game.mark_off_y = 64;
                    }

                    if (Game.mark_type == 3) {
                        Game.mark_off_x = 0;
                        Game.mark_off_y = 64;
                    }

                    if (Game.mark_type == 4) {
                        Game.mark_off_x = 128;
                        Game.mark_off_y = 64;
                    }

                    if (Game.mark_type == 5) {
                        Game.mark_off_x = 64;
                        Game.mark_off_y = 0;
                    }

                    if (Game.mark_type == 6) {
                        Game.mark_off_x = 64;
                        Game.mark_off_y = 128;
                    }

                    Game.mark_type = 2;
                    Game.mark_x = in.get_ushort();
                    Game.mark_y = in.get_ushort();
                    Game.mark_z = in.get_ubyte();
                }

                if (Game.mark_type == 10) {
                    Game.mark_player_index = in.get_ushort();
                }

                Game.ptype = -1;
                return true;
            }

            // Sets both the main widget and sidebar widget.
            if (Game.ptype == 248) {
                int overlay_widget = in.get_ushort_a();
                int sidebar_widget = in.get_ushort();
                Chat.clear_overlay();
                Chat.clear();
                Game.widget_overlay = overlay_widget;
                Sidebar.widget_index = sidebar_widget;
                Sidebar.draw = true;
                Sidebar.draw_tabs = true;
                Game.dialogue_option_active = false;
                Game.ptype = -1;
                return true;
            }

            // Sets the scroll amount of the specified widget.
            if (Game.ptype == 79) {
                int index = in.get_le_ushort();
                int amount = in.get_ushort_a();
                Widget w = Widget.instance[index];

                if (w != null && w.type == 0) {
                    if (amount < 0) {
                        amount = 0;
                    }

                    if (amount > w.scroll_height - w.height) {
                        amount = w.scroll_height - w.height;
                    }
                    w.scroll_amount = amount;
                }

                Game.ptype = -1;
                return true;
            }

            // Resets settings to their defaults?
            if (Game.ptype == 68) {
                for (int i = 0; i < settings.length; i++) {
                    if (settings[i] != default_settings[i]) {
                        settings[i] = default_settings[i];
                        handle_varp(i);
                        Sidebar.draw = true;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 196) {
                long name_long = in.get_long();
                in.get_int(); // ignore dis
                int rights = in.get_ubyte();

                boolean not_ignored = true;

                if (rights <= 1) {
                    for (int i = 0; i < ignore_count; i++) {
                        if (ignore_long[i] != name_long) {
                            continue;
                        }
                        not_ignored = false;
                        break;
                    }

                }
                if (not_ignored && message_status == 0) {
                    StringBuilder b = new StringBuilder();
                    if (rights > 0) {
                        b.append("@cr").append(rights).append("@");
                    }
                    b.append(JString.get_formatted_string(name_long));
                    Chat.put(b.toString(), JString.get_formatted(psize - 13, in), rights > 0 ? 7 : 3);
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 85) {
                net_region_y = in.get_ubyte_c();
                net_region_x = in.get_ubyte_c();
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 24) {
                Sidebar.set_flashing(in.get_ubyte_s());

                if (Sidebar.flashing_tab == Sidebar.selected_tab) {
                    if (Sidebar.flashing_tab.index == 3) {
                        Sidebar.open_tab(1);
                    } else {
                        Sidebar.open_tab(3);
                    }
                    Sidebar.draw = true;
                }
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the model information of the widget.
            if (Game.ptype == 246) {
                Widget w = Widget.get(in.get_le_ushort());
                int zoom = in.get_ushort();
                int index = in.get_ushort();

                if (w == null) {
                    Game.ptype = -1;
                    return true;
                }

                if (index == 65535) {
                    w.model_type_disabled = 0;
                    Game.ptype = -1;
                    return true;
                } else {
                    ObjConfig oc = ObjConfig.get(index);
                    w.model_type_disabled = 4;
                    w.model_index_disabled = index;
                    w.model_pitch = oc.icon_pitch;
                    w.model_yaw = oc.icon_yaw;
                    w.model_zoom = (oc.icon_dist * 100) / zoom;
                    Game.ptype = -1;
                    return true;
                }
            }

            if (Game.ptype == 171) {
                boolean hidden = in.get_bool();
                int index = in.get_ushort();
                Widget.instance[index].hidden = hidden;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 142) {
                int index = in.get_le_ushort();
                Widget.reset_animations(index);
                Chat.clear_overlay();
                Chat.clear();
                Sidebar.widget_index = index;
                Sidebar.draw = true;
                Sidebar.draw_tabs = true;
                widget_overlay = -1;
                dialogue_option_active = false;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 126) {
                String message = in.get_string();
                int index = in.get_ushort_a();
                Widget w = Widget.get(index);

                if (w != null) {
                    Widget.instance[index].message_disabled = message;
                    if (Widget.instance[index].parent == Sidebar.selected_tab.widget) {
                        Sidebar.draw = true;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 206) {
                for (int i = 0; i < Chat.Settings.values.length; i++) {
                    Chat.Settings.values[i] = in.get_ubyte();
                }
                Chat.Settings.redraw = true;
                Chat.redraw = true;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 240) {
                if (Sidebar.selected_tab.index == 12) {
                    Sidebar.draw = true;
                }
                weight_carried = in.get_short();
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 8) {
                int widget_index = in.get_le_ushort_a();
                int model_index = in.get_ushort();
                Widget.instance[widget_index].model_type_disabled = 1;
                Widget.instance[widget_index].model_index_disabled = model_index;
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the disabled color of the widget.
            if (Game.ptype == 122) {
                int widget_index = in.get_le_ushort_a();

                // COLOR FORMAT:
                // R: 0-31 G: 0-31 B: 0-31
                // ((r >> 3) << 16) | ((g >> 3) << 16) | (b >> 3)
                // or just clamp it serverside to 0 to 31 each channel.
                int color = in.get_le_ushort_a();
                int red = color >> 10 & 0x1F;
                int green = color >> 5 & 0x1F;
                int blue = color & 0x1F;
                Widget.instance[widget_index].rgb_disabled = (red << 19) | (green << 11) | (blue << 3);
                Game.ptype = -1;
                return true;
            }

            // INFO: Sets the items to the specified container index.
            if (Game.ptype == 53) {
                Sidebar.draw = true;
                int index = in.get_ushort();
                int size = in.get_ushort();

                Widget w = Widget.instance[index];

                try {
                    for (int i = 0; i < size; i++) {
                        int count = in.get_ubyte();

                        if (count == 255) {
                            count = in.get_ime_int();
                        }

                        w.item_index[i] = (short) in.get_le_ushort_a();
                        w.item_count[i] = count;
                    }
                } catch (Exception e) {
                    /* ignore out of bounds exception */
                }

                // Clears the wrest of the items.
                for (int i = size; i < w.item_index.length; i++) {
                    w.item_index[i] = 0;
                    w.item_count[i] = 0;
                }

                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 230) {
                int zoom = in.get_ushort_a();
                int index = in.get_ushort();
                int pitch = in.get_ushort();
                int yaw = in.get_le_ushort_a();
                Widget.instance[index].model_pitch = pitch;
                Widget.instance[index].model_yaw = yaw;
                Widget.instance[index].model_zoom = zoom;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 221) {
                frenemies_status = in.get_ubyte();
                Sidebar.draw = true;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 177) {
                cam_cinema_mode = true;
                cam_cinema_aim_x = in.get_ubyte();
                cam_cinema_aim_y = in.get_ubyte();
                cam_cinema_aim_z = in.get_ushort();
                cam_cinema_rot_base = in.get_ubyte();
                cam_cinema_rot_modifier = in.get_ubyte();

                if (cam_cinema_rot_modifier >= 100) {
                    int x = cam_cinema_aim_x * 128 + 64;
                    int y = cam_cinema_aim_y * 128 + 64;
                    int z = get_land_z(x, y, plane) - cam_cinema_aim_z;
                    int x_diff = x - Camera.x;
                    int z_diff = z - Camera.z;
                    int y_diff = y - Camera.y;
                    int length = (int) Math.sqrt(x_diff * x_diff + y_diff * y_diff);

                    Camera.pitch = (int) (Math.atan2(z_diff, length) * 325.94900000000001D) & 0x7ff;
                    Camera.yaw = (int) (Math.atan2(x_diff, y_diff) * -325.94900000000001D) & 0x7ff;

                    if (Camera.pitch < 128) {
                        Camera.pitch = 128;
                    }

                    if (Camera.pitch > 383) {
                        Camera.pitch = 383;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 249) {
                free_friends_list = in.get_ubyte_a();
                local_player_index = in.get_le_ushort_a();
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 65) {
                update_actors(psize, in);
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 27) {
                Chat.set(State.ENTER_AMOUNT);
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 187) {
                Chat.set(State.ENTER_NAME);
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 97) {
                int index = in.get_ushort();

                try {
                    Widget.reset_animations(index);
                } catch (Exception e) {
                    e.printStackTrace();
                    Game.ptype = -1;
                    return true;
                }

                if (Sidebar.widget_index != -1) {
                    Sidebar.widget_index = -1;
                    Sidebar.draw = true;
                    Sidebar.draw_tabs = true;
                }

                Chat.clear_overlay();
                Chat.clear();
                widget_overlay = index;
                dialogue_option_active = false;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 218) {
                Chat.set_underlay(in.get_short_a());
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 87) {
                int index = in.get_le_ushort();
                int value = in.get_me_int();

                default_settings[index] = value;

                if (settings[index] != value) {
                    settings[index] = value;
                    handle_varp(index);
                    Sidebar.draw = true;

                    if (Chat.get_underlay() != -1) {
                        Chat.redraw = true;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 36) {
                int index = in.get_le_ushort();
                byte value = in.get_byte();

                default_settings[index] = value;

                if (settings[index] != value) {
                    settings[index] = value;
                    handle_varp(index);
                    Sidebar.draw = true;

                    if (Chat.get_underlay() != -1) {
                        Chat.redraw = true;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 61) {
                in_multi_zone = in.get_ubyte();
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 200) {
                Widget w = Widget.get(in.get_ushort());
                int seq_index = in.get_short();

                if (w == null) {
                    Game.ptype = -1;
                    return true;
                }

                w.seq_index_disabled = seq_index;

                if (seq_index == -1) {
                    w.sequence_frame = 0;
                    w.sequence_cycle = 0;
                }

                Game.ptype = -1;
                return true;
            }
            if (Game.ptype == 219) {
                if (Sidebar.widget_index != -1) {
                    Sidebar.widget_index = -1;
                    Sidebar.draw = true;
                    Sidebar.draw_tabs = true;
                }

                Chat.clear_overlay();
                Chat.clear();
                widget_overlay = -1;
                dialogue_option_active = false;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 34) {
                Sidebar.draw = true;
                Widget w = Widget.instance[in.get_ushort()];

                while (in.position < psize) {
                    int slot = in.get_usmart();
                    int index = in.get_ushort();
                    int count = in.get_ubyte();

                    if (count == 255) {
                        count = in.get_int();
                    }

                    if (slot >= 0 && slot < w.item_index.length) {
                        w.item_index[slot] = (short) index;
                        w.item_count[slot] = count;
                    }
                }
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 105 || ptype == 84 || ptype == 147 || ptype == 215 || ptype == 4 || ptype == 117 || ptype == 156 || ptype == 44 || ptype == 160 || ptype == 101 || ptype == 151) {
                handle_packet(in, ptype);
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 106) {
                Sidebar.open_tab(in.get_ubyte_c());
                Sidebar.draw = true;
                Sidebar.draw_tabs = true;
                Game.ptype = -1;
                return true;
            }

            if (Game.ptype == 164) {
                int index = in.get_le_ushort();

                Widget.reset_animations(index);

                if (Sidebar.widget_index != -1) {
                    Sidebar.widget_index = -1;
                    Sidebar.draw = true;
                    Sidebar.draw_tabs = true;
                }

                Chat.set_overlay(index);
                widget_overlay = -1;
                dialogue_option_active = false;
                Game.ptype = -1;
                return true;
            }

            Signlink.error("T1 - " + ptype + "," + psize + " - " + last_ptype2 + "," + last_ptype3);
            net_disconnect();
        } catch (IOException e) {
            handle_connection_lost();
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("T2");
            sb.append(" - last:").append(ptype).append(',').append(last_ptype2).append(',').append(last_ptype3);
            sb.append(" - psize:").append(psize);
            sb.append(" - pos:").append(self.get_tile_x()).append(',').append(self.get_tile_y()).append('\n');

            for (int i = 0; i < psize && i < in.position; i++) {
                sb.append(Integer.toHexString((byte) in.payload[i])).append(',');
            }

            Signlink.error(sb.toString());
            e.printStackTrace();
            net_disconnect();
        }
        return true;
    }

    public static void handle_new_actors(int position, Buffer b) {
        while (b.bit_off + 21 < position * 8) {
            int actor_index = b.get_bits(14);

            if (actor_index == 16383) {
                break;
            }

            if (actors[actor_index] == null) {
                actors[actor_index] = new Actor();
            }

            Actor a = actors[actor_index];
            actor_indices[actor_count++] = actor_index;
            a.update_cycle = loop_cycle;

            int y = b.get_bits(5);

            if (y > 15) {
                y -= 32;
            }

            int x = b.get_bits(5);

            if (x > 15) {
                x -= 32;
            }

            int discard_walk_queue = b.get_bits(1);

            a.config = ActorConfig.get(b.get_bits(12));

            if (b.get_bits(1) == 1) { // Update Required
                entity_indices[entity_count++] = actor_index;
            }

            a.size = a.config.has_options;
            a.turn_speed = a.config.turn_speed;
            a.walk_animation = a.config.move_seq;
            a.turn_180_animation = a.config.turn_180_seq;
            a.turn_r_animation = a.config.turn_r_seq;
            a.turn_l_animation = a.config.turn_l_seq;
            a.stand_animation = a.config.stand_sequence;
            a.move_to(self.path_x[0] + x, self.path_y[0] + y, discard_walk_queue == 1);
        }
        b.stop_bit_access();
    }

    public static void handle_ondemand() {
        do {
            OnDemandRequest r;
            do {
                r = ondemand.next();

                if (r == null) {
                    return;
                }

                if (r.archive == 0) {
                    Model.load(r.payload, r.file);

                    if ((ondemand.mesh_flags(r.file) & 0x62) != 0) {
                        Sidebar.draw = true;

                        if (Chat.get_overlay() != -1) {
                            Chat.redraw = true;
                        }
                    }
                }

                if (r.archive == 1 && r.payload != null) {
                    SequenceFrame.load(r.payload);
                }

                if (r.archive == 2 && r.payload != null) {
                    music.buffer = r.payload;
                    play_music(0, 10, music.volume0, false, 0, r.file);
                }

                if (r.archive == 3 && scene_state == 1) {
                    for (int i = 0; i < chunk_loc_payload.length; i++) {
                        if (map_uids[i] == r.file) {
                            chunk_loc_payload[i] = r.payload;
                            if (r.payload == null) {
                                map_uids[i] = -1;
                            }
                            break;
                        }

                        if (landscape_uids[i] != r.file) {
                            continue;
                        }

                        chunk_landscape_payload[i] = r.payload;

                        if (r.payload == null) {
                            landscape_uids[i] = -1;
                        }
                        break;
                    }

                }
            } while (r.archive != 93 || !ondemand.has_landscape(r.file));
            Scene.request_loc_models(new Buffer(r.payload), ondemand);
        } while (true);
    }

    public static void handle_packet(Buffer b, int opcode) {
        // INFO: Update Ground Item
        if (opcode == 84) {
            int coord = b.get_ubyte();
            int x = net_region_x + (coord >> 4 & 7);
            int y = net_region_y + (coord & 7);
            int index = b.get_ushort();
            int old_stack_index = b.get_ushort();
            int new_stack_index = b.get_ushort();

            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Chain c = Game.item_pile[plane][x][y];
                if (c != null) {
                    for (Item item = (Item) c.top(); item != null; item = (Item) c.next()) {
                        if (item.index != (index & 0x7fff) || item.stack_index != old_stack_index) {
                            continue;
                        }
                        item.stack_index = new_stack_index;
                        break;
                    }

                    update_item_pile(x, y);
                }
            }
            return;
        }

        if (opcode == 105) {
            int coord = b.get_ubyte();
            int x = net_region_x + (coord >> 4 & 0x7);
            int y = net_region_y + (coord & 0x7);
            int index = b.get_ushort();
            int values = b.get_ubyte();
            int radius = values >> 4 & 0xf;
            int type = values & 7;

            if (self.path_x[0] >= x - radius && self.path_x[0] <= x + radius && self.path_y[0] >= y - radius && self.path_y[0] <= y + radius && !low_detail && sound_count < 50) {
                sound_index[sound_count] = index;
                sound_type[sound_count] = type;
                sound_delay[sound_count] = WaveSound.delay[index];
                sound_count++;
            }
        }

        if (opcode == 215) {
            int index = b.get_ushort_a();
            int coords = b.get_ubyte_s();
            int x = net_region_x + (coords >> 4 & 7);
            int y = net_region_y + (coords & 7);
            int entity_index = b.get_ushort_a();
            int stack = b.get_ushort();

            if (x >= 0 && y >= 0 && x < 104 && y < 104 && entity_index != local_player_index) {
                Item item = new Item();
                item.index = (short) index;
                item.stack_index = stack;

                if (Game.item_pile[plane][x][y] == null) {
                    Game.item_pile[plane][x][y] = new Chain();
                }

                Game.item_pile[plane][x][y].push_back(item);
                update_item_pile(x, y);
            }
            return;
        }

        if (opcode == 156) {
            int coords = b.get_ubyte_a();
            int x = net_region_x + (coords >> 4 & 7);
            int y = net_region_y + (coords & 7);
            int index = b.get_ushort();
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Chain pile = Game.item_pile[plane][x][y];
                if (pile != null) {
                    for (Item item = (Item) pile.top(); item != null; item = (Item) pile.next()) {
                        if (item.index != (index & 0x7fff)) {
                            continue;
                        }
                        item.detach();
                        break;
                    }
                    if (pile.top() == null) {
                        Game.item_pile[plane][x][y] = null;
                    }
                    update_item_pile(x, y);
                }
            }
            return;
        }

        if (opcode == 160) {
            int coords = b.get_ubyte_s();
            int x = net_region_x + (coords >> 4 & 7);
            int y = net_region_y + (coords & 7);
            int attributes = b.get_ubyte_s();
            int type = attributes >> 2;
            int rotation = attributes & 3;
            int class_type = LOC_CLASS_TYPE[type];
            int sequence = b.get_ushort_a();

            if (x >= 0 && y >= 0 && x < 103 && y < 103) {
                int v_sw = height_map[plane][x][y];
                int v_se = height_map[plane][x + 1][y];
                int v_ne = height_map[plane][x + 1][y + 1];
                int v_nw = height_map[plane][x][y + 1];

                if (class_type == 0) {
                    WallLoc wl = landscape.get_wall(plane, x, y);
                    if (wl != null) {
                        int index = wl.uid >> 14 & 0x7fff;
                        if (type == 2) {
                            wl.root = new Loc(index, 4 + rotation, 2, v_se, v_ne, v_sw, v_nw, sequence, false);
                            wl.extension = new Loc(index, rotation + 1 & 3, 2, v_se, v_ne, v_sw, v_nw, sequence, false);
                        } else {
                            wl.root = new Loc(index, rotation, type, v_se, v_ne, v_sw, v_nw, sequence, false);
                        }
                    }
                } else if (class_type == 1) {
                    WallDecoration wd = landscape.get_wall_decoration(x, 866, y, plane);
                    if (wd != null) {
                        wd.node = new Loc(wd.uid >> 14 & 0x7fff, 0, 4, v_se, v_ne, v_sw, v_nw, sequence, false);
                    }
                } else if (class_type == 2) {
                    StaticLoc sl = landscape.get_loc(x, y, plane);
                    if (type == 11) {
                        type = 10;
                    }
                    if (sl != null) {
                        sl.node = new Loc(sl.uid >> 14 & 0x7fff, rotation, type, v_se, v_ne, v_sw, v_nw, sequence, false);
                    }
                } else if (class_type == 3) {
                    GroundDecoration gd = landscape.get_ground_decoration(x, y, plane);
                    if (gd != null) {
                        gd.node = new Loc(gd.uid >> 14 & 0x7fff, rotation, 22, v_se, v_ne, v_sw, v_nw, sequence, false);
                    }
                }
            }
            return;
        }

        if (opcode == 147) {
            int offset = b.get_ubyte_s();
            int x = net_region_x + (offset >> 4 & 7);
            int y = net_region_y + (offset & 7);
            int player_index = b.get_ushort();
            byte x2 = b.get_byte_s();
            int start = b.get_le_ushort();
            byte y2 = b.get_byte_c();
            int end = b.get_ushort();
            int arrangement = b.get_ubyte_s();
            byte x1 = b.get_byte();
            int loc_index = b.get_ushort();
            byte y1 = b.get_byte_c();

            int type = arrangement >> 2;
            int rotation = arrangement & 3;
            int class_type = LOC_CLASS_TYPE[type];
            Player p;

            if (player_index == local_player_index) {
                p = self;
            } else {
                p = players[player_index];
            }

            if (p != null) {
                LocConfig lc = LocConfig.get(loc_index);
                int v_sw = height_map[plane][x][y];
                int v_se = height_map[plane][x + 1][y];
                int v_ne = height_map[plane][x + 1][y + 1];
                int v_nw = height_map[plane][x][y + 1];
                Model mesh = lc.get_model(type, rotation, v_sw, v_se, v_ne, v_nw, -1);

                if (mesh != null) {
                    spawn_loc(-1, x, y, plane, 0, 0, end + 1, start + 1, class_type);

                    p.loc_start_cycle = start + loop_cycle;
                    p.loc_end_cycle = end + loop_cycle;

                    p.loc_model = mesh;
                    int loc_size_x = lc.size_x;
                    int loc_size_y = lc.size_y;

                    if (rotation == 1 || rotation == 3) {
                        loc_size_x = lc.size_y;
                        loc_size_y = lc.size_x;
                    }

                    p.loc_x = x * 128 + loc_size_x * 64;
                    p.loc_y = y * 128 + loc_size_y * 64;
                    p.loc_z = get_land_z(p.loc_x, p.loc_y, plane);

                    if (x1 > x2) {
                        byte new_size_x2 = x1;
                        x1 = x2;
                        x2 = new_size_x2;
                    }
                    if (y1 > y2) {
                        byte new_size_y2 = y1;
                        y1 = y2;
                        y2 = new_size_y2;
                    }

                    p.loc_x0 = x + x1;
                    p.loc_y0 = y + y1;
                    p.loc_x1 = x + x2;
                    p.loc_y1 = y + y2;
                }
            }
        }

        if (opcode == 151) {
            int offset = b.get_ubyte_a();
            int x = net_region_x + (offset >> 4 & 7);
            int y = net_region_y + (offset & 7);
            int loc_index = b.get_le_ushort();
            int loc_info = b.get_ubyte_s();
            int loc_type = loc_info >> 2;
            int loc_rotation = loc_info & 3;
            int class_type = LOC_CLASS_TYPE[loc_type];

            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                spawn_loc(loc_index, x, y, plane, loc_type, loc_rotation, -1, 0, class_type);
            }
            return;
        }

        if (opcode == 4) {
            int offset = b.get_ubyte();
            int x = net_region_x + (offset >> 4 & 7);
            int y = net_region_y + (offset & 7);
            int index = b.get_ushort();
            int z = b.get_ubyte();
            int delay = b.get_ushort();

            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                x = x * 128 + 64;
                y = y * 128 + 64;
                spotanims.push_back(new SpotAnim(x, y, get_land_z(x, y, plane) - z, plane, loop_cycle, delay, index));
            }
            return;
        }

        if (opcode == 44) {
            short item_index = (short) b.get_le_ushort_a();
            int stack_index = b.get_ushort();
            int offset = b.get_ubyte();
            int x = net_region_x + (offset >> 4 & 7);
            int y = net_region_y + (offset & 7);

            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                Item i = new Item();
                i.index = item_index;
                i.stack_index = stack_index;

                if (Game.item_pile[plane][x][y] == null) {
                    Game.item_pile[plane][x][y] = new Chain();
                }

                Game.item_pile[plane][x][y].push_back(i);
                update_item_pile(x, y);
            }
            return;
        }

        if (opcode == 101) {
            int attributes = b.get_ubyte_c();
            int type = attributes >> 2;
            int rotation = attributes & 3;
            int class_type = LOC_CLASS_TYPE[type];
            int coord = b.get_ubyte();
            int x = net_region_x + (coord >> 4 & 7);
            int y = net_region_y + (coord & 7);
            if (x >= 0 && y >= 0 && x < 104 && y < 104) {
                spawn_loc(-1, x, y, plane, type, rotation, -1, 0, class_type);
            }
            return;
        }

        if (opcode == 117) {
            int offset = b.get_ubyte();
            int src_x = net_region_x + (offset >> 4 & 7);
            int src_y = net_region_y + (offset & 7);
            int end_x = src_x + b.get_byte();
            int end_y = src_y + b.get_byte();
            int target = b.get_short();
            int effect = b.get_ushort();
            int src_z = b.get_ubyte() * 4;
            int end_z = b.get_ubyte() * 4;
            int delay = b.get_ushort();
            int speed = b.get_ushort();
            int slope = b.get_ubyte();
            int source_size = b.get_ubyte();

            if (src_x >= 0 && src_y >= 0 && src_x < 104 && src_y < 104 && end_x >= 0 && end_y >= 0 && end_x < 104 && end_y < 104 && effect != 65535) {

                src_x = src_x * 128 + 64;
                src_y = src_y * 128 + 64;
                end_x = end_x * 128 + 64;
                end_y = end_y * 128 + 64;

                Projectile p = new Projectile(slope, end_z, delay + loop_cycle, speed + loop_cycle, source_size, plane, get_land_z(src_x, src_y, plane) - src_z, src_y, src_x, target, effect);
                p.update(delay + loop_cycle, end_x, end_y, get_land_z(end_x, end_y, plane) - end_z);
                projectiles.push_back(p);
            }
        }
    }

    public static void handle_player_menu_options(Player p, int x, int y, int index) {
        if (p == self) {
            return;
        }

        if (Menu.count >= 400) {
            return;
        }

        String suffix = p.toString();

        if (selected_item) {
            Menu.add("Use " + selected_item_name + " with @whi@" + suffix, 491, x, y, index);
        } else if (selected_widget) {
            if ((selected_mask & 8) == 8) {
                Menu.add(selected_tooltip + " @whi@" + suffix, 365, x, y, index);
            }
        } else {
            for (int i = 4; i >= 0; i--) {
                if (player_action[i] != null) {
                    int offset = 0;
                    if (player_action[i].equalsIgnoreCase("attack")) {
                        if (p.combat_level > self.combat_level) {
                            offset = 2000;
                        }
                        if (self.team != 0 && p.team != 0) {
                            if (self.team == p.team) {
                                offset = 2000;
                            } else {
                                offset = 0;
                            }
                        }
                    } else if (player_action_priority[i]) {
                        offset = 2000;
                    }

                    Menu.add(player_action[i] + " @whi@" + suffix, Action.PLAYER[i] + offset, x, y, index);
                }
            }
        }

        for (int i = 0; i < Menu.count; i++) {
            if (Menu.get_action(i) == 516) {
                Menu.options[i].caption = "Walk here @whi@" + suffix;
                break;
            }
        }

    }

    public static void handle_players() {
        for (int i = -1; i < player_count; i++) {
            int index;

            if (i == -1) {
                index = MAX_PLAYER_INDEX;
            } else {
                index = player_indices[i];
            }

            Player p = players[index];

            if (p != null) {
                handle_entity(p);
            }
        }

    }

    public static void handle_scene() {
        if (low_detail && scene_state == 2 && Scene.plane_at_build != plane) {
            producer_scene.prepare();
            BitmapFont.NORMAL.draw("Loading - please wait.", 256, 150, 0xFFFFFF, BitmapFont.SHADOW_CENTER);
            producer_scene.draw(4, 4);
            scene_state = 1;
            scene_load_start = System.currentTimeMillis();
        }

        if (scene_state == 1) {
            int state = get_scene_load_state((byte) -95);
            if (state != 0 && System.currentTimeMillis() - scene_load_start > 0x360_000L) {
                Signlink.error(username + " glcfb " + server_seed + "," + state + "," + low_detail + "," + cache[0] + "," + ondemand.immediate_request_count() + "," + plane + "," + loaded_region_x + "," + loaded_region_y);
                scene_load_start = System.currentTimeMillis();
            }
        }

        if (scene_state == 2 && plane != last_plane) {
            last_plane = plane;
            generate_minimap(plane);
        }
    }

    public static void handle_scrollbar(Widget w, int x, int y, int m_x, int m_y, int height, int scroll_height, boolean is_tab_area) {
        if (dragging_scrollbar) {
            scroll_drag_bound = 32;
        } else {
            scroll_drag_bound = 0;
        }

        dragging_scrollbar = false;

        if (Mouse.wheel_amount != 0 && m_x >= (x - w.width) && m_x <= x && m_y >= y && m_y <= y + height) {
            w.scroll(Mouse.wheel_amount);
            Mouse.wheel_amount = 0;

            if (is_tab_area) {
                Sidebar.draw = true;
                return;
            }
        } else if (m_x >= x && m_x < x + 16 && m_y >= y && m_y < y + 16) {
            w.scroll_amount -= click_cycle * 4;

            if (is_tab_area) {
                Sidebar.draw = true;
                return;
            }
        } else if (m_x >= x && m_x < x + 16 && m_y >= (y + height) - 16 && m_y < y + height) {
            w.scroll_amount += click_cycle * 4;

            if (is_tab_area) {
                Sidebar.draw = true;
                return;
            }
        } else if (m_x >= x - scroll_drag_bound && m_x < x + 16 + scroll_drag_bound && m_y >= y + 16 && m_y < (y + height) - 16 && click_cycle > 0) {
            int grip_length = ((height - 32) * height) / scroll_height;

            if (grip_length < 8) {
                grip_length = 8;
            }

            int i2 = m_y - y - 16 - grip_length / 2;
            int j2 = height - 32 - grip_length;

            w.scroll_amount = ((scroll_height - height) * i2) / j2;

            if (is_tab_area) {
                Sidebar.draw = true;
            }

            dragging_scrollbar = true;
        }
    }

    public static void handle_spoken() {
        for (int i = -1; i < player_count; i++) {
            int player_index;

            if (i == -1) {
                player_index = MAX_PLAYER_INDEX;
            } else {
                player_index = player_indices[i];
            }

            Player p = players[player_index];

            if (p != null && p.spoken_life > 0) {
                p.spoken_life--;

                if (p.spoken_life == 0) {
                    p.spoken_message = null;
                }
            }
        }

        for (int i = 0; i < actor_count; i++) {
            int actor_index = actor_indices[i];
            Actor a = actors[actor_index];

            if (a != null && a.spoken_life > 0) {
                a.spoken_life--;

                if (a.spoken_life == 0) {
                    a.spoken_message = null;
                }
            }
        }

    }

    public static void handle_tab_mouse() {
        if (Mouse.click_button == 1) {
            int tab = 0;
            for (int[] i : TAB_BUTTONS) {
                if (Mouse.click_x >= i[0] && Mouse.click_x <= i[1] && Mouse.click_y >= i[2] && Mouse.click_y <= i[3] && Sidebar.TAB[tab].widget != -1) {
                    Sidebar.draw = true;
                    Sidebar.open_tab(tab);
                    Sidebar.draw_tabs = true;
                    break;
                }
                tab++;
            }
        }
    }

    public static void handle_varp(int index) {
        int type = Varp.instance[index].index;

        if (type == 0) {
            return;
        }

        int setting = Game.settings[index];

        switch (type) {
            case 1: { // Brightness
                Canvas3D.create_palette(1d - (double) (setting / 10d));
                ObjConfig.sprite_cache.clear();
                redraw = true;
                break;
            }

            case 2: {
                break;
            }

            case 3: { // Music Volume
                int volume = (int) (256f * (1f - (setting / 4f)));

                if (volume != music.volume0) {
                    if (music.volume0 != 0 && music.currentSong == -1) {
                        if (volume != 0) {
                            set_music_volume(volume);
                        } else {
                            stop_music(false);
                            music.tmpDelay = 0;
                        }
                    } else {
                        play_music_instantly(volume, music.currentSong, 0, 0, false);
                        music.tmpDelay = 0;
                    }

                    music.volume0 = volume;
                }
                break;
            }

            case 4: { // Sound Volume
                break;
            }

            case 5: { // One/Two Button Mouse Setting
                mouse_button_setting = setting;
                break;
            }

            case 6: {
                Chat.Settings.show_effects = setting == 0;
                break;
            }

            case 8: {
                Chat.Settings.private_area = setting;
                Chat.Settings.redraw = true;
                break;
            }

            case 9: {
                anInt913 = setting;
                break;
            }
        }
    }

    public static void handle_viewport_mouse() {
        if (!selected_item && !selected_widget) {
            Menu.add("Walk here", 516, Mouse.last_x, Mouse.last_y);
        }

        int last_uid = -1;

        for (int idx = 0; idx < Model.hovered_count; idx++) {
            int uid = Model.hovered_uid[idx];
            int x = uid & 0x7f;
            int y = uid >> 7 & 0x7f;
            int type = uid >> 29 & 3;
            int index = uid >> 14 & 0x7fff;

            if (uid == last_uid) {
                continue;
            }

            last_uid = uid;

            if (type == 2 && landscape.get_arrangement(plane, x, y, uid) >= 0) {
                LocConfig lc = LocConfig.get(index);

                if (lc.override_index != null) {
                    lc = lc.get_overriding_config();
                }

                if (lc == null) {
                    continue;
                }

                if (selected_item) {
                    Menu.add("Use " + selected_item_name + " with @cya@" + lc.name, 62, x, y, uid);
                } else if (selected_widget) {
                    if ((selected_mask & 4) == 4) {
                        Menu.add(selected_tooltip + " @cya@" + lc.name, 956, x, y, uid);
                    }
                } else {
                    if (lc.action != null) {
                        for (int j = 4; j > -1; j--) {
                            if (lc.action[j] != null) {
                                Menu.add(lc.action[j] + " @cya@" + lc.name, Action.OBJECT[j], x, y, uid);
                            }
                        }
                    }
                    Menu.add("Examine @cya@" + lc.name, 1226, x, y, lc.index << 14);
                }
            }

            if (type == 1) {
                Actor a = actors[index];
                if (a.config.has_options == 1 && (a.scene_x & 0x7f) == 64 && (a.scene_y & 0x7f) == 64) {
                    for (int i = 0; i < actor_count; i++) {
                        Actor a1 = actors[actor_indices[i]];
                        if (a1 != null && a1 != a && a1.config.has_options == 1 && a1.scene_x == a.scene_x && a1.scene_y == a.scene_y) {
                            handle_actor_menu_options(a1.config, x, y, actor_indices[i]);
                        }
                    }

                    for (int i = 0; i < player_count; i++) {
                        Player p = players[player_indices[i]];
                        if (p != null && p.scene_x == a.scene_x && p.scene_y == a.scene_y) {
                            handle_player_menu_options(p, x, y, player_indices[i]);
                        }
                    }

                }
                handle_actor_menu_options(a.config, x, y, index);
            }

            if (type == 0) {
                Player p = players[index];
                if ((p.scene_x & 0x7f) == 64 && (p.scene_y & 0x7f) == 64) {
                    for (int k2 = 0; k2 < actor_count; k2++) {
                        Actor a = actors[actor_indices[k2]];
                        if (a != null && a.config.has_options == 1 && a.scene_x == p.scene_x && a.scene_y == p.scene_y) {
                            handle_actor_menu_options(a.config, x, y, actor_indices[k2]);
                        }
                    }

                    for (int i3 = 0; i3 < player_count; i3++) {
                        Player p1 = players[player_indices[i3]];
                        if (p1 != null && p1 != p && p1.scene_x == p.scene_x && p1.scene_y == p.scene_y) {
                            handle_player_menu_options(p1, x, y, player_indices[i3]);
                        }
                    }

                }
                handle_player_menu_options(p, x, y, index);
            }

            if (type == 3) {
                Chain c = Game.item_pile[plane][x][y];
                if (c != null) {
                    for (Item item = (Item) c.bottom(); item != null; item = (Item) c.previous()) {
                        ObjConfig oc = ObjConfig.get(item.index);
                        if (selected_item) {
                            Menu.add("Use " + selected_item_name + " with @lre@" + oc.name, 511, x, y, item.index);
                        } else if (selected_widget) {
                            if ((selected_mask & 1) == 1) {
                                Menu.add(selected_tooltip + " @lre@" + oc.name, 94, x, y, item.index);
                            }
                        } else {
                            for (int i = 4; i >= 0; i--) {
                                if (oc.ground_action != null && oc.ground_action[i] != null) {
                                    Menu.add(oc.ground_action[i] + " @lre@" + oc.name, Action.GROUND_ITEM[i], x, y, item.index);
                                } else if (i == 2) {
                                    Menu.add("Take @lre@" + oc.name, 234, x, y, item.index);
                                }
                            }
                            Menu.add("Examine @lre@" + oc.name, 1448, x, y, item.index);
                        }
                    }

                }
            }
        }
    }

    public static boolean handle_widget(Widget w) {
        int type = w.action_type;

        if (CharacterDesign.handle(w, type)) {
            return true;
        }

        if (frenemies_status == 2) {
            if (type == 201) {
                Chat.set(State.ADD_FRIEND, "Enter name of friend to add to list");
            }
            if (type == 202) {
                Chat.set(State.REMOVE_FRIEND, "Enter name of friend to delete from list");
            }
        }

        if (type == 205) {
            logout_cycle = 250;
            return true;
        }

        if (type == 501) {
            Chat.set(State.ADD_IGNORE, "Enter name of player to add to list");
        }

        if (type == 502) {
            Chat.set(State.REMOVE_IGNORE, "Enter name of player to delete from list");
        }

        if (type >= 601 && type <= 612) {
            close_widgets();
            if (report_abuse_input.length() > 0) {
                out.put_opcode(218);
                out.put_long(JString.get_long(report_abuse_input));
                out.put_byte(type - 601);
                out.put_byte(report_abuse_mute ? 1 : 0);
            }
        }
        return false;
    }

    public static void handle_widget_mouse() {
        if (drag_area != 0) {
            return;
        }

        int click_button = Mouse.click_button;
        int menu_count = Menu.count;

        if (selected_widget && Mouse.click_x >= 516 && Mouse.click_y >= 160 && Mouse.click_x <= 765 && Mouse.click_y <= 205) {
            click_button = 0;
        }

        if (Menu.visible) {
            if (click_button != 1) {
                Area area = Menu.area;

                int x = Mouse.last_x - area.x;
                int y = Mouse.last_y - area.y;
                int menu_x = Menu.x;
                int menu_y = Menu.y;
                int menu_w = Menu.width;
                int menu_h = Menu.height;

                if (x < menu_x - 10 || x > menu_x + menu_w + 10 || y < menu_y - 10 || y > menu_y + menu_h + 10) {
                    Menu.visible = false;
                    if (area == Area.TAB) {
                        Sidebar.draw = true;
                    } else if (area == Area.CHAT) {
                        Chat.redraw = true;
                    }
                }
            }

            if (click_button == 1) {
                Area area = Menu.area;

                int x = Menu.x;
                int y = Menu.y;
                int width = Menu.width;
                int click_x = Mouse.click_x - area.x;
                int click_y = Mouse.click_y - area.y;

                int active_option = -1;
                for (int option = 0; option < menu_count; option++) {
                    int option_y = y + 31 + (menu_count - 1 - option) * 15;
                    if (click_x > x && click_x < x + width && click_y > option_y - 13 && click_y < option_y + 3) {
                        active_option = option;
                    }
                }

                if (active_option != -1) {
                    handle_menu_option(active_option);
                }

                Menu.visible = false;

                if (area == Area.TAB) {
                    Sidebar.draw = true;
                } else if (area == Area.CHAT) {
                    Chat.redraw = true;
                }

                return;
            }
        } else {
            if (click_button == 1 && menu_count > 0) {
                int action = Menu.get_last_action();

                for (int i : Action.DRAG) {
                    if (i == action) {
                        int slot = Menu.get_last_param(0);
                        int index = Menu.get_last_param(1);
                        Widget w = Widget.instance[index];

                        if (w.items_draggable || w.items_swappable) {
                            dragging = false;
                            drag_cycle = 0;
                            drag_widget = index;
                            drag_slot = slot;
                            drag_area = 2;
                            drag_start_x = Mouse.click_x;
                            drag_start_y = Mouse.click_y;

                            if (Widget.instance[index].parent == widget_overlay) {
                                drag_area = 1;
                            }

                            if (Widget.instance[index].parent == Chat.get_overlay()) {
                                drag_area = 3;
                            }
                            return;
                        }

                        break;
                    }
                }
            }

            if (click_button == 1 && (mouse_button_setting == 1) && menu_count > 2) {
                click_button = 2;
            }

            if (click_button == 1 && menu_count > 0) {
                handle_menu_option(menu_count - 1);
            }

            if (click_button == 2 && menu_count > 0) {
                Menu.show();
            }
        }
    }

    public static void handle_widget_mouse(Widget w, int screen_x, int screen_y, int mouse_x, int mouse_y, int scroll_amount) {
        if (w == null) {
            return;
        }

        if (w.type != 0 || w.children == null || w.hidden) {
            return;
        }

        if (!w.visible) {
            return;
        }

        if (mouse_x < screen_x || mouse_y < screen_y || mouse_x > screen_x + w.width || mouse_y > screen_y + w.height) {
            return;
        }

        for (int index = 0; index < w.children.length; index++) {
            int x = w.child_x[index] + screen_x;
            int y = (w.child_y[index] + screen_y) - scroll_amount;

            Widget child = Widget.instance[w.children[index]];

            if (!child.visible) {
                continue;
            }

            x += child.x;
            y += child.y;

            boolean hovered = false;

            if (mouse_x >= x && mouse_y >= y && mouse_x < x + child.width && mouse_y < y + child.height) {
                hovered = true;
            }

            if ((child.hover_index >= 0 || child.color_hover_disabled != 0) && hovered) {
                if (child.hover_index >= 0) {
                    tmp_hovered_widget = child.hover_index;
                } else {
                    tmp_hovered_widget = child.index;
                }
            }

            if (child.type == 0) {
                handle_widget_mouse(child, x, y, mouse_x, mouse_y, child.scroll_amount);

                if (child.scroll_height > child.height) {
                    handle_scrollbar(child, x + child.width, y, mouse_x, mouse_y, child.height, child.scroll_height, true);
                }
            } else {
                if (child.option_type == 1 && hovered) {
                    boolean no_options = false;

                    if (child.action_type != 0) {
                        no_options = frenemy_option_valid(child, false);
                    }

                    if (!no_options) {
                        Menu.add(child.option, 315, -1, child.index);
                    }
                }

                if (child.option_type == 2 && !selected_widget && hovered) {
                    String s = child.option_prefix;
                    if (s.indexOf(' ') != -1) {
                        s = s.substring(0, s.indexOf(' '));
                    }
                    Menu.add(s + " @gre@" + child.option_suffix, 626, -1, child.index);
                }

                if (child.option_type == 3 && hovered) {
                    Menu.add("Close", 200, -1, child.index);
                }

                if (child.option_type == 4 && hovered) {
                    Menu.add(child.option, 169, -1, child.index);
                }

                if (child.option_type == 5 && hovered) {
                    Menu.add(child.option, 646, -1, child.index);
                }

                if (child.option_type == 6 && !dialogue_option_active && hovered) {
                    Menu.add(child.option, 679, -1, child.index);
                }

                if (child.type == 2) {
                    int slot = 0;

                    for (int column = 0; column < child.height; column++) {
                        for (int row = 0; row < child.width; row++) {
                            int slot_x = x + row * (32 + child.item_margin_x);
                            int slot_y = y + column * (32 + child.item_margin_y);

                            if (slot < 20) {
                                slot_x += child.item_slot_x[slot];
                                slot_y += child.item_slot_y[slot];
                            }

                            if (mouse_x >= slot_x && mouse_y >= slot_y && mouse_x < slot_x + 32 && mouse_y < slot_y + 32) {
                                hovered_slot = slot;
                                hovered_slot_widget = child.index;

                                if (child.item_index[slot] > 0) {
                                    ObjConfig oc = ObjConfig.get(child.item_index[slot] - 1);

                                    if (oc == null) {
                                        Menu.add("Invalid Item", -1);
                                        continue;
                                    }

                                    if (selected_item && child.items_have_actions) {
                                        if (child.index != selected_item_widget || slot != selected_item_slot) {
                                            Menu.add("Use " + selected_item_name + " with @lre@" + oc.name, 870, slot, child.index, oc.index);
                                        }
                                    } else if (selected_widget && child.items_have_actions) {
                                        if ((selected_mask & 0x10) == 16) {
                                            Menu.add(selected_tooltip + " @lre@" + oc.name, 543, slot, child.index, oc.index);
                                        }
                                    } else {
                                        if (child.items_have_actions) {
                                            for (int i = 4; i >= 3; i--) {
                                                if (oc.action != null && oc.action[i] != null) {
                                                    Menu.add(oc.action[i] + " @lre@" + oc.name, Action.ITEM[i], slot, child.index, oc.index);
                                                } else if (i == 4) {
                                                    Menu.add("Drop @lre@" + oc.name, 847, slot, child.index, oc.index);
                                                }
                                            }
                                        }

                                        if (child.items_usable) {
                                            Menu.add("Use @lre@" + oc.name, 447, slot, child.index, oc.index);
                                        }

                                        if (child.items_have_actions && oc.action != null) {
                                            for (int i = 2; i >= 0; i--) {
                                                if (oc.action[i] != null) {
                                                    Menu.add(oc.action[i] + " @lre@" + oc.name, Action.ITEM[i], slot, child.index, oc.index);
                                                }
                                            }
                                        }

                                        if (child.item_actions != null) {
                                            for (int i = 4; i >= 0; i--) {
                                                if (child.item_actions[i] != null) {
                                                    Menu.add(child.item_actions[i] + " @lre@" + oc.name, Action.WIDGET_ITEM[i], slot, child.index, oc.index);
                                                }
                                            }
                                        }

                                        Menu.add("Examine @lre@" + oc.name, 1125, slot, child.index, oc.index);
                                    }
                                }
                            }
                            slot++;
                        }
                    }
                }
            }
        }
    }

    public static void ignore_add(long name_long) {
        if (name_long == 0L) {
            return;
        }

        if (ignore_count >= 100) {
            Chat.put("Your ignore list is full. Max of 100 hit", 0);
            return;
        }

        String name = JString.get_formatted_string(name_long);

        for (int i = 0; i < ignore_count; i++) {
            if (ignore_long[i] == name_long) {
                Chat.put(name + " is already on your ignore list", 0);
                return;
            }
        }

        for (int i = 0; i < friend_count; i++) {
            if (friend_long[i] == name_long) {
                Chat.put("Please remove " + name + " from your friend list first", 0);
                return;
            }
        }

        ignore_long[ignore_count++] = name_long;
        Sidebar.draw = true;
        out.put_opcode(133);
        out.put_long(name_long);
    }

    public static void ignore_remove(long name_long) {
        try {
            if (name_long == 0L) {
                return;
            }
            for (int j = 0; j < ignore_count; j++) {
                if (ignore_long[j] == name_long) {
                    ignore_count--;
                    Sidebar.draw = true;
                    for (int k = j; k < ignore_count; k++) {
                        ignore_long[k] = ignore_long[k + 1];
                    }
                    out.put_opcode(74);
                    out.put_long(name_long);
                    return;
                }
            }
            return;
        } catch (RuntimeException runtimeexception) {
            Signlink.error("47229, " + name_long + ", " + runtimeexception.toString());
        }
        throw new RuntimeException();
    }

    public static boolean interact_with_loc(int x, int y, int uid) {
        int index = uid >> 14 & 0x7fff;
        int loc_info = landscape.get_arrangement(plane, y, x, uid);

        if (loc_info == -1) {
            return false;
        }

        int type = loc_info & 0x1f;
        int rotation = loc_info >> 6 & 3;

        if (type == 10 || type == 11 || type == 22) {
            LocConfig lc = LocConfig.get(index);
            int size_x;
            int size_y;

            if (rotation == 0 || rotation == 2) {
                size_x = lc.size_x;
                size_y = lc.size_y;
            } else {
                size_x = lc.size_y;
                size_y = lc.size_x;
            }

            int face_flags = lc.face_flags;

            if (rotation != 0) {
                face_flags = (face_flags << rotation & 0xf) + (face_flags >> 4 - rotation);
            }

            Game.walk_to(2, size_x, size_y, self.path_x[0], self.path_y[0], y, x, 0, face_flags, 0, false);
        } else {
            Game.walk_to(2, 0, 0, self.path_x[0], self.path_y[0], y, x, type + 1, 0, rotation, false);
        }

        cross_x = Mouse.click_x;
        cross_y = Mouse.click_y;
        cross_type = 2;
        cross_cycle = 0;
        return true;
    }

    public static void main(String[] args) {
        try {
            System.out.println("RS2 user client - release #317");
            for (int i = 0; i < args.length; i++) {
                String s = args[i].toLowerCase();

                switch (s) {
                    case "-cache": {
                        System.setProperty("rt317.cache", args[++i]);
                        break;
                    }
                    case "-debug": {
                        Game.debug = true;
                        break;
                    }
                    case "-node": {
                        try {
                            Game.node_index = Integer.parseInt(args[++i]);
                        } catch (Exception e) {
							/* empty */
                        }
                        break;
                    }
                    case "-offset": {
                        try {
                            Game.port_offset = Integer.parseInt(args[++i]);
                        } catch (Exception e) {
							/* empty */
                        }
                        break;
                    }
                    case "-highmem": {
                        Game.low_detail = false;
                        break;
                    }
                    case "-members": {
                        Game.is_members = true;
                        break;
                    }
                    case "-alwaysontop": {
                        System.setProperty("rt317.alwaysontop", "1");
                        break;
                    }
                }
            }
            Signlink.start(InetAddress.getLocalHost());
            new Game().init(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void method891(boolean bool) {
        music.method853(0, null, bool);
    }

    public static void net_disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception _ex) {
        }

        connection = null;
        logged_in = false;

        TitleScreen.state = TitleScreen.State.WELCOME;

        username = JString.BLANK;
        password = JString.BLANK;

        clear_caches();
        landscape.reset();

        for (int i = 0; i < 4; i++) {
            collision_maps[i].defaults();
        }

        System.gc();

        music.stop();
        ondemand.send_request(2, 484);
    }

    public static void net_login(String username, String password, boolean reconnection) {
        Signlink.error_name = username;

        try {
            if (!reconnection) {
                TitleScreen.set_message(JString.BLANK, JString.CONNECTING_TO_SERVER);
                TitleScreen.draw(true);
            }

            Game.connection = new Connection(instance, instance.get_socket(43594 + port_offset));

            long name_long = JString.get_long(username);
            int name_hash = (int) (name_long >> 16 & 0x1FL);

            out.position = 0;
            out.put_byte(14);
            out.put_byte(name_hash);

            Game.connection.put_bytes(out.payload, 0, 2);

            for (int i = 0; i < 8; i++) {
                Game.connection.get_byte();
            }

            int opcode = Game.connection.get_byte();
            int first_opcode = opcode;

            if (opcode == 0) {
                Game.connection.get_bytes(in.payload, 0, 8);
                in.position = 0;

                Game.server_seed = in.get_long();

                int seed[] = new int[4];
                seed[0] = (int) (Math.random() * 99999999D);
                seed[1] = (int) (Math.random() * 99999999D);
                seed[2] = (int) (server_seed >> 32);
                seed[3] = (int) server_seed;

                out.position = 0;
                out.put_byte(10);
                out.put_int(seed[0]);
                out.put_int(seed[1]);
                out.put_int(seed[2]);
                out.put_int(seed[3]);
                out.put_int(Signlink.uid);
                out.put_string(username);
                out.put_string(password);
                out.encrypt(RSA_PUBLIC_KEY, RSA_MODULUS);

                login_buffer.position = 0;
                login_buffer.put_byte(reconnection ? 18 : 16);
                login_buffer.put_byte(out.position + 36 + 1 + 1 + 2);
                login_buffer.put_byte(255);
                login_buffer.put_short(317);
                login_buffer.put_byte(low_detail ? 1 : 0);

                for (int l1 = 0; l1 < 9; l1++) {
                    login_buffer.put_int(archive_crc[l1]);
                }

                login_buffer.put_bytes(out.payload, out.position, 0);
                out.isaac = new ISAACCipher(seed);

                for (int i = 0; i < 4; i++) {
                    seed[i] += 50;
                }

                connection_isaac = new ISAACCipher(seed);
                connection.put_bytes(login_buffer.payload, 0, login_buffer.position);
                opcode = connection.get_byte();
            }
            if (opcode == 1) {
                try {
                    Thread.sleep(2000L);
                } catch (Exception _ex) {
					/* empty */
                }
                net_login(username, password, reconnection);
                return;
            }
            if (opcode == 2) {
                Game.local_rights = connection.get_byte();
                Game.record_mouse = connection.get_byte() == 1;
                Game.last_click_time = 0L;
                Game.mouse_recorder.cycle = 0;
                Game.mouse_recorder.off = 0;
                Game.instance.focused = true;
                Game.is_focused = true;
                Game.logged_in = true;

                // Net
                Game.out.position = 0;
                Game.in.position = 0;
                Game.ptype = -1;
                Game.last_ptype1 = -1;
                Game.last_ptype2 = -1;
                Game.last_ptype3 = -1;
                Game.psize = 0;
                Game.net_cycle = 0;
                Game.next_update = 0;
                Game.logout_cycle = 0;
                Game.mark_type = 0;
                Menu.reset();
                Menu.visible = false;
                Game.instance.idle_cycle = 0;
                Chat.reset();
                Game.selected_item = false;
                Game.selected_widget = false;
                Game.scene_state = 0;
                Game.sound_count = 0;

                Game.cam_x_off = (int) (Math.random() * 100D) - 50;
                Game.cam_y_off = (int) (Math.random() * 110D) - 55;
                Game.cam_pitch_off = (int) (Math.random() * 80D) - 40;
                Game.cam_yaw_off = (int) (Math.random() * 120D) - 60;
                Game.map_zoom_offset = (int) (Math.random() * 30D) - 20;
                Game.chase_cam_yaw = (int) (Math.random() * 20D) - 10 & 0x7ff;

                Game.minimap_state = 0;
                Game.last_plane = -1;
                Game.map_marker_x = 0;
                Game.map_marker_y = 0;
                Game.player_count = 0;
                Game.actor_count = 0;

                for (int i = 0; i < MAX_PLAYER_COUNT; i++) {
                    Game.players[i] = null;
                    Game.player_buffer[i] = null;
                }

                for (int i = 0; i < 16384; i++) {
                    Game.actors[i] = null;
                }

                Game.self = Game.players[MAX_PLAYER_INDEX] = new Player();
                Game.projectiles.clear();
                Game.spotanims.clear();

                for (int plane = 0; plane < 4; plane++) {
                    for (int x = 0; x < 104; x++) {
                        for (int y = 0; y < 104; y++) {
                            Game.item_pile[plane][x][y] = null;
                        }
                    }
                }

                Game.spawned_locs = new Chain();
                Game.frenemies_status = 0;
                Game.friend_count = 0;
                Game.widget_overlay = -1;
                Sidebar.widget_index = -1;
                Sidebar.open_tab(3);
                Sidebar.flashing_tab = null;
                Game.widget_underlay = -1;
                Game.dialogue_option_active = false;
                Menu.visible = false;
                Game.in_multi_zone = 0;
                CharacterDesign.reset();

                for (int i = 0; i < 5; i++) {
                    Game.player_action[i] = null;
                    Game.player_action_priority[i] = false;
                }

                create_ingame_producers();
                return;
            }

            if (opcode == 3) {
                TitleScreen.set_message(JString.BLANK, JString.INVALID_CREDENTIALS);
                return;
            }

            if (opcode == 4) {
                TitleScreen.set_message(JString.ACCOUNT_DISABLED, JString.CHECK_MESSAGES);
                return;
            }

            if (opcode == 5) {
                TitleScreen.set_message(JString.ALREADY_LOGGED_IN, JString.TRY_AGAIN_IN_60);
                return;
            }

            if (opcode == 6) {
                TitleScreen.set_message(JString.UPDATED, JString.RELOAD_PAGE);
                return;
            }

            if (opcode == 7) {
                TitleScreen.set_message(JString.WORLD_FULL, JString.DIFFERENT_WORLD);
                return;
            }

            if (opcode == 8) {
                TitleScreen.set_message(JString.UNABLE_TO_CONNECT, JString.SERVER_OFFLINE);
                return;
            }

            if (opcode == 9) {
                TitleScreen.set_message(JString.LOGIN_LIMIT, JString.TOO_MANY_CONNECTIONS);
                return;
            }

            if (opcode == 10) {
                TitleScreen.set_message(JString.UNABLE_TO_CONNECT, JString.BAD_SESSION);
                return;
            }

            if (opcode == 11) {
                TitleScreen.set_message(JString.REJECTED_SESSION, JString.TRY_AGAIN);
                return;
            }

            if (opcode == 12) {
                TitleScreen.set_message(JString.NEED_MEMBERS, JString.PLEASE_SUBSCRIBE);
                return;
            }

            if (opcode == 13) {
                TitleScreen.set_message(JString.COULDNT_LOGIN, JString.USE_DIFFERENT_WORLD);
                return;
            }

            if (opcode == 14) {
                TitleScreen.set_message(JString.SERVER_UPDATING, JString.WAIT_1_MINUTE);
                return;
            }

            if (opcode == 15) {
                Game.logged_in = true;
                Game.out.position = 0;
                Game.in.position = 0;
                Game.ptype = -1;
                Game.last_ptype1 = -1;
                Game.last_ptype2 = -1;
                Game.last_ptype3 = -1;
                Game.psize = 0;
                Game.net_cycle = 0;
                Game.next_update = 0;
                Menu.reset();
                Menu.visible = false;
                Game.scene_load_start = System.currentTimeMillis();
                return;
            }

            if (opcode == 16) {
                TitleScreen.set_message(JString.LOGIN_EXCEEDED, JString.WAIT_1_MINUTE);
                return;
            }

            if (opcode == 17) {
                TitleScreen.set_message(JString.WITHIN_MEMBERS, JString.MOVE_TO_FREE);
                return;
            }

            if (opcode == 20) {
                TitleScreen.set_message(JString.INVALID_SERVER, JString.DIFFERENT_WORLD);
                return;
            }
            if (opcode == 21) {
                for (int k1 = connection.get_byte(); k1 >= 0; k1--) {
                    TitleScreen.set_message(JString.JUST_LEFT_WORLD, JString.TRANSFERRED + k1 + " seconds");
                    TitleScreen.draw(true);
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception _ex) {
                    }
                }

                net_login(username, password, reconnection);
                return;
            }
            if (opcode == -1) {
                if (first_opcode == 0) {
                    if (reconnection_attempts < 2) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception _ex) {
                        }
                        reconnection_attempts++;
                        net_login(username, password, reconnection);
                        return;
                    } else {
                        TitleScreen.set_message(JString.NO_RESPONSE, JString.WAIT_1_MINUTE);
                        return;
                    }
                } else {
                    TitleScreen.set_message(JString.NO_RESPONSE, JString.DIFFERENT_WORLD);
                    return;
                }
            } else {
                System.out.println("response:" + opcode);
                TitleScreen.set_message(JString.UNEXPECTED_RESPONSE, JString.DIFFERENT_WORLD);
                return;
            }
        } catch (IOException _ex) {
            TitleScreen.set_message(JString.BLANK, JString.ERROR_CONNECTING);
        }
    }

    public static synchronized void play_music(int fileVar1, int loop1, int volume2, boolean loopM1, int fileVar3, int fileVar2) {
        music.loop1 = loop1;
        music.loop2 = -1;
        music.fvar1 = fileVar1;
        music.fvar2 = fileVar2;
        music.fvar3 = fileVar3;
        music.loopmusic1 = loopM1;
        music.volume2 = volume2;
        music.fetch = true;
    }

    public static synchronized void play_music_instantly(int volume2, int fileVar2, int fileVar1, int fileVar3, boolean loopMusic1) {
        music.fetch = true;
        music.volume2 = volume2;
        music.fvar1 = fileVar1;
        music.fvar2 = fileVar2;
        music.fvar3 = fileVar3;
        music.loop1 = -1;
        music.loop2 = -1;
        music.loopmusic1 = loopMusic1;
    }

    public static void retrieve_scene(boolean flag) {
        try {
            last_plane = -1;
            spotanims.clear();
            projectiles.clear();
            Canvas3D.clear_textures();
            clear_caches();
            landscape.reset();
            System.gc();

            for (int i = 0; i < 4; i++) {
                collision_maps[i].defaults();
            }

            for (int z = 0; z < 4; z++) {
                for (int x = 0; x < 104; x++) {
                    for (int y = 0; y < 104; y++) {
                        render_flags[z][x][y] = 0;
                    }
                }
            }

            scene = new Scene(104, 104, height_map, render_flags);
            int length = chunk_loc_payload.length;
            out.put_opcode(0);

            if (!server_sent_chunk) {
                for (int chunk = 0; chunk < length; chunk++) {
                    int chunk_x = (chunk_coords[chunk] >> 8) * 64 - map_base_x;
                    int chunk_y = (chunk_coords[chunk] & 0xff) * 64 - map_base_y;
                    byte payload[] = chunk_loc_payload[chunk];
                    if (payload != null) {
                        scene.load_land(collision_maps, payload, chunk_x, chunk_y, (loaded_region_x - 6) * 8, (loaded_region_y - 6) * 8);
                    }
                }

                for (int chunk = 0; chunk < length; chunk++) {
                    int chunk_x = (chunk_coords[chunk] >> 8) * 64 - map_base_x;
                    int chunk_y = (chunk_coords[chunk] & 0xff) * 64 - map_base_y;
                    if (chunk_loc_payload[chunk] == null && loaded_region_y < 800) {
                        scene.fit_edges(chunk_x, chunk_y, 64, 64);
                    }
                }

                out.put_opcode(0);

                for (int chunk = 0; chunk < length; chunk++) {
                    byte payload[] = chunk_landscape_payload[chunk];
                    if (payload != null) {
                        int region_x = (chunk_coords[chunk] >> 8) * 64 - map_base_x;
                        int region_y = (chunk_coords[chunk] & 0xff) * 64 - map_base_y;
                        scene.load_locs(region_x, collision_maps, region_y, landscape, payload);
                    }
                }
            } else {
                for (int map_plane = 0; map_plane < 4; map_plane++) {
                    for (int x = 0; x < 13; x++) {
                        for (int y = 0; y < 13; y++) {
                            int region_uid = region_chunk_uids[map_plane][x][y];
                            if (region_uid != -1) {
                                int chunk_plane = region_uid >> 24 & 3;
                                int chunk_rot = region_uid >> 1 & 3;
                                int chunk_x = region_uid >> 14 & 0x3ff;
                                int chunk_y = region_uid >> 3 & 0x7ff;
                                int chunk_uid = (chunk_x / 8 << 8) + chunk_y / 8;

                                for (int chunk = 0; chunk < chunk_coords.length; chunk++) {
                                    if (chunk_coords[chunk] != chunk_uid || chunk_loc_payload[chunk] == null) {
                                        continue;
                                    }
                                    scene.load_chunk(collision_maps, x * 8, y * 8, (chunk_x & 7) * 8, (chunk_y & 7) * 8, chunk_plane, chunk_loc_payload[chunk], chunk_rot, map_plane);
                                    break;
                                }
                            }
                        }
                    }
                }

                for (int x = 0; x < 13; x++) {
                    for (int y = 0; y < 13; y++) {
                        int region_uid = region_chunk_uids[0][x][y];
                        if (region_uid == -1) {
                            scene.fit_edges(x * 8, y * 8, 8, 8);
                        }
                    }

                }

                out.put_opcode(0);

                for (int map_plane = 0; map_plane < 4; map_plane++) {
                    for (int x = 0; x < 13; x++) {
                        for (int y = 0; y < 13; y++) {
                            int chunk_uid = region_chunk_uids[map_plane][x][y];
                            if (chunk_uid != -1) {
                                int chunk_plane = chunk_uid >> 24 & 3;
                                int chunk_rot = chunk_uid >> 1 & 3;
                                int chunk_x = chunk_uid >> 14 & 0x3ff;
                                int chunk_y = chunk_uid >> 3 & 0x7ff;
                                int chunk_coord = (chunk_x / 8 << 8) + chunk_y / 8;
                                for (int chunk = 0; chunk < chunk_coords.length; chunk++) {
                                    if (chunk_coords[chunk] != chunk_coord || chunk_landscape_payload[chunk] == null) {
                                        continue;
                                    }
                                    scene.load_locs(collision_maps, landscape, x * 8, y * 8, (chunk_x & 7) * 8, (chunk_y & 7) * 8, chunk_plane, map_plane, chunk_landscape_payload[chunk], chunk_rot);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            out.put_opcode(0);
            scene.create_land_mesh(collision_maps, landscape);
            producer_scene.prepare();
            out.put_opcode(0);

            if (low_detail) {
                landscape.set_plane(Scene.min_plane);
            } else {
                landscape.set_plane(0);
            }

            for (int x = 0; x < 104; x++) {
                for (int y = 0; y < 104; y++) {
                    update_item_pile(x, y);
                }
            }

            handle_locs();
        } catch (Exception exception) {
        }

        LocConfig.static_model_cache.clear();

        if (low_detail && Signlink.cache_file != null) {
            int mesh_count = ondemand.get_file_count(0);
            for (int mesh_index = 0; mesh_index < mesh_count; mesh_index++) {
                int flags = ondemand.mesh_flags(mesh_index);
                if ((flags & 0x79) == 0) {
                    Model.nullify(mesh_index);
                }
            }

        }

        System.gc();
        Canvas3D.setup_texel_pools();
        ondemand.clear_passive_requests();

        int chunk_west = (loaded_region_x - 6) / 8 - 1;
        int chunk_east = (loaded_region_x + 6) / 8 + 1;
        int chunk_north = (loaded_region_y - 6) / 8 - 1;
        int chunk_south = (loaded_region_y + 6) / 8 + 1;

        if (restrict_region) {
            chunk_west = 49;
            chunk_east = 50;
            chunk_north = 49;
            chunk_south = 50;
        }

        for (int chunk_x = chunk_west; chunk_x <= chunk_east; chunk_x++) {
            for (int chunk_y = chunk_north; chunk_y <= chunk_south; chunk_y++) {
                if (chunk_x == chunk_west || chunk_x == chunk_east || chunk_y == chunk_north || chunk_y == chunk_south) {
                    int map_uid = ondemand.get_map_uid(chunk_x, chunk_y, 0);
                    if (map_uid != -1) {
                        ondemand.request(map_uid, 3);
                    }
                    int landscape_uid = ondemand.get_map_uid(chunk_x, chunk_y, 1);
                    if (landscape_uid != -1) {
                        ondemand.request(landscape_uid, 3);
                    }
                }
            }
        }
    }

    public static void scroll_textures(int cycle) {
        if (!low_detail) {
            for (byte i : ANIMATED_TEXTURES) {
                if (Canvas3D.texture_cycle[i] >= cycle) {
                    Bitmap b = Canvas3D.texture[i];
                    int len = (b.width * b.height) - 1;
                    int off = (b.width * anim_cycle * 2);
                    byte[] pixels = b.pixels;
                    byte[] texels = tmp_texels;

                    for (int j = 0; j <= len; j++) {
                        texels[j] = pixels[j - off & len];
                    }

                    b.pixels = texels;
                    tmp_texels = pixels;
                    Canvas3D.update_texture(i);
                }
            }
        }
    }

    public static void set_draw_xy(Entity e, int offset_z) {
        set_draw_xy(e.scene_x, e.scene_y, offset_z);
    }

    public static void set_draw_xy(int x, int y, int offset_z) {
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            draw_x = -1;
            draw_y = -1;
            return;
        }

        int z = get_land_z(x, y, plane) - offset_z;
        x -= Camera.x;
        z -= Camera.z;
        y -= Camera.y;

        int pitch_sin = Model.sin[Camera.pitch];
        int pitch_cos = Model.cos[Camera.pitch];
        int yaw_sin = Model.sin[Camera.yaw];
        int yaw_cos = Model.cos[Camera.yaw];

        int i = y * yaw_sin + x * yaw_cos >> 16;
        y = y * yaw_cos - x * yaw_sin >> 16;
        x = i;

        i = z * pitch_cos - y * pitch_sin >> 16;
        y = z * pitch_sin + y * pitch_cos >> 16;
        z = i;

        if (y >= 50) {
            draw_x = Canvas3D.center_x + (x << 9) / y;
            draw_y = Canvas3D.center_y + (z << 9) / y;
            return;
        }

        draw_x = -1;
        draw_y = -1;
    }

    public static void set_music_volume(int volume) {
        if (music.fetch) {
            music.volume2 = volume;
        } else {
            if (music.var2 == 0) {
                if (music.var1 >= 0) {
                    music.var1 = volume;
                    music.setVolumeVelocity(volume, 0);
                }
            } else if (music.buffer != null) {
                music.volume3 = volume;
            }
        }
    }

    public static void spawn_loc(int loc_index, int loc_x, int loc_y, int loc_z, int loc_type, int loc_rotation, int cycle, int spawn_cycle, int class_type) {
        SpawntLoc sl = null;

        for (SpawntLoc _sl = (SpawntLoc) spawned_locs.top(); _sl != null; _sl = (SpawntLoc) spawned_locs.next()) {
            if (_sl.plane != loc_z || _sl.x != loc_x || _sl.y != loc_y || _sl.class_type != class_type) {
                continue;
            }
            sl = _sl;
            break;
        }

        if (sl == null) {
            sl = new SpawntLoc();
            sl.plane = loc_z;
            sl.class_type = class_type;
            sl.x = loc_x;
            sl.y = loc_y;
            handle_loc(sl);
            spawned_locs.push_back(sl);
        }

        sl.loc_index = loc_index;
        sl.loc_type = loc_type;
        sl.loc_rotation = loc_rotation;
        sl.spawn_cycle = spawn_cycle;
        sl.cycle = cycle;
    }

    public static synchronized void stop_music(boolean loop) {
        method891(loop);
        music.fetch = false;
    }

    public static int[] transform(int x, int y, int z) {
        x -= Camera.x;
        y -= Camera.y;
        z -= Camera.z;

        int pitch_sin = Model.sin[Camera.pitch];
        int pitch_cos = Model.cos[Camera.pitch];
        int yaw_sin = Model.sin[Camera.yaw];
        int yaw_cos = Model.cos[Camera.yaw];

        int i = y * yaw_sin + x * yaw_cos >> 16;
        y = y * yaw_cos - x * yaw_sin >> 16;
        x = i;

        i = z * pitch_cos - y * pitch_sin >> 16;
        y = z * pitch_sin + y * pitch_cos >> 16;
        z = i;

        return new int[]{x, y, z, Canvas3D.center_x + (x << 9) / y, Canvas3D.center_y + (z << 9) / y};
    }

    public static void update_actor_masks(Buffer b) {
        for (int i = 0; i < entity_count; i++) {
            Actor a = actors[entity_indices[i]];
            int mask = b.get_ubyte();

            if ((mask & 0x10) != 0) { // Animation
                int seq_index = b.get_le_ushort();

                if (seq_index == 65535) {
                    seq_index = -1;
                }

                int delay = b.get_ubyte();

                if (seq_index == a.seq_index && seq_index != -1) {
                    int type = Sequence.instance[seq_index].type;
                    if (type == 1) {
                        a.seq_frame = 0;
                        a.seq_cycle = 0;
                        a.seq_delay_cycle = delay;
                        a.seq_reset_cycle = 0;
                    }
                    if (type == 2) {
                        a.seq_reset_cycle = 0;
                    }
                } else if (seq_index == -1 || a.seq_index == -1 || Sequence.instance[seq_index].priority >= Sequence.instance[a.seq_index].priority) {
                    a.seq_index = seq_index;
                    a.seq_frame = 0;
                    a.seq_cycle = 0;
                    a.seq_delay_cycle = delay;
                    a.seq_reset_cycle = 0;
                    a.still_path_position = a.path_position;
                }
            }

            if ((mask & 8) != 0) { // Hit
                int damage = b.get_ubyte_a();
                int type = b.get_ubyte_c();
                a.hit(type, damage, loop_cycle);
                a.combat_cycle = loop_cycle + 300;
                a.current_health = b.get_ubyte_a();
                a.max_health = b.get_ubyte();
            }

            if ((mask & 0x80) != 0) { // Graphics
                a.spotanim_index = b.get_ushort();

                int info = b.get_int();
                a.graphic_offset_y = info >> 16;
                a.spotanim_cycle_end = loop_cycle + (info & 0xFFFF);

                a.spotanim_frame = 0;
                a.spotanim_cycle = 0;

                if (a.spotanim_cycle_end > loop_cycle) {
                    a.spotanim_frame = -1;
                }

                if (a.spotanim_index == 65535) {
                    a.spotanim_index = -1;
                }
            }

            if ((mask & 0x20) != 0) { // Face Entity
                a.face_entity = b.get_ushort();

                if (a.face_entity == 65535) {
                    a.face_entity = -1;
                }
            }

            if ((mask & 1) != 0) { // Forced Chat
                a.spoken_message = b.get_string();
                a.spoken_life = 100;
            }

            if ((mask & 0x40) != 0) { // Hit 2
                int damage = b.get_ubyte_c();
                int type = b.get_ubyte_s();
                a.hit(type, damage, loop_cycle);
                a.combat_cycle = loop_cycle + 300;
                a.current_health = b.get_ubyte_s();
                a.max_health = b.get_ubyte_c();
            }

            if ((mask & 2) != 0) { // Transform
                a.config = ActorConfig.get(b.get_le_ushort_a());
                a.size = a.config.has_options;
                a.turn_speed = a.config.turn_speed;
                a.walk_animation = a.config.move_seq;
                a.turn_180_animation = a.config.turn_180_seq;
                a.turn_r_animation = a.config.turn_r_seq;
                a.turn_l_animation = a.config.turn_l_seq;
                a.stand_animation = a.config.stand_sequence;
            }

            if ((mask & 4) != 0) { // Face Coordinate
                a.face_x = b.get_le_ushort();
                a.face_y = b.get_le_ushort();
            }
        }
    }

    public static void update_actor_movement(Buffer b) {
        b.start_bit_access();
        int actor_count = b.get_bits(8);

        if (actor_count < Game.actor_count) {
            for (int l = actor_count; l < Game.actor_count; l++) {
                entity_update_indices[entity_update_count++] = actor_indices[l];
            }
        }

        if (actor_count > Game.actor_count) {
            Signlink.error(username + " Too many npcs");
            throw new RuntimeException(JString.EEK);
        }

        Game.actor_count = 0;

        for (int i = 0; i < actor_count; i++) {
            int actor_index = actor_indices[i];
            Actor a = actors[actor_index];
            int movement_update = b.get_bits(1);

            if (movement_update == 0) {
                actor_indices[Game.actor_count++] = actor_index;
                a.update_cycle = loop_cycle;
            } else {
                int move_type = b.get_bits(2);

                switch (move_type) {
                    case 0: {// No Movement
                        actor_indices[Game.actor_count++] = actor_index;
                        a.update_cycle = loop_cycle;
                        entity_indices[entity_count++] = actor_index;
                        break;
                    }
                    case 1: {// 1 Tile Movement
                        actor_indices[Game.actor_count++] = actor_index;
                        a.update_cycle = loop_cycle;
                        a.move(b.get_bits(3), false);

                        if (b.get_bits(1) == 1) { // Update Required
                            entity_indices[entity_count++] = actor_index;
                        }
                        break;
                    }
                    case 2: {// 2 Tile Movement
                        actor_indices[Game.actor_count++] = actor_index;
                        a.update_cycle = loop_cycle;
                        a.move(b.get_bits(3), true);
                        a.move(b.get_bits(3), true);

                        if (b.get_bits(1) == 1) { // Update Required
                            entity_indices[entity_count++] = actor_index;
                        }
                        break;
                    }
                    case 3: {// Unknown
                        entity_update_indices[entity_update_count++] = actor_index;
                        break;
                    }
                }
            }
        }
    }

    public static void update_actors(int psize, Buffer b) {
        entity_update_count = 0;
        entity_count = 0;
        update_actor_movement(b);
        handle_new_actors(psize, b);
        update_actor_masks(b);

        for (int i = 0; i < entity_update_count; i++) {
            int actor_index = entity_update_indices[i];

            if (actors[actor_index].update_cycle != loop_cycle) {
                actors[actor_index].config = null;
                actors[actor_index] = null;
            }
        }

        if (b.position != psize) {
            Signlink.error(username + " size mismatch in getactorpos - pos:" + b.position + " psize:" + psize);
            throw new RuntimeException(JString.EEK);
        }

        for (int i = 0; i < actor_count; i++) {
            if (actors[actor_indices[i]] == null) {
                Signlink.error(username + " null entry in actor list - pos:" + i + " size:" + actor_count);
                throw new RuntimeException(JString.EEK);
            }
        }
    }

    public static void update_item_pile(int x, int y) {
        Chain c = Game.item_pile[plane][x][y];

        if (c == null) {
            landscape.remove_item_pile(plane, x, y);
            return;
        }

        int b = 0xFA0A1F01;

        Item top = null;
        Item middle = null;
        Item bottom = null;

        for (Item i = (Item) c.top(); i != null; i = (Item) c.next()) {
            ObjConfig oc = ObjConfig.get(i.index);
            int a = oc.pile_priority;

            if (oc.stackable) {
                a *= i.stack_index + 1;
            }

            if (a > b) {
                b = a;
                top = i;
            }
        }

        c.push(top);

        for (Item item = (Item) c.top(); item != null; item = (Item) c.next()) {
            if (item.index != top.index && bottom == null) {
                bottom = item;
            }
            if (item.index != top.index && item.index != bottom.index && middle == null) {
                middle = item;
            }
        }

        int uid = x + (y << 7) + 0x60000000;
        landscape.add_item_pile(x, y, get_land_z(x * 128 + 64, y * 128 + 64, plane), plane, top, middle, bottom, uid);
    }

    public static void update_localplayer_movement(Buffer b) {
        b.start_bit_access();

        if (b.get_bits(1) == 0) {
            return;
        }

        int move_type = b.get_bits(2);

        if (move_type == 0) {
            entity_indices[entity_count++] = MAX_PLAYER_INDEX;
            return;
        }

        if (move_type == 1) { // Walk
            int direction = b.get_bits(3);
            self.move(direction, false);

            if (b.get_bits(1) == 1) { // Update Required
                entity_indices[entity_count++] = MAX_PLAYER_INDEX;
            }
            return;
        }

        if (move_type == 2) { // Run
            self.move(b.get_bits(3), true);
            self.move(b.get_bits(3), true);

            if (b.get_bits(1) == 1) { // Update Required
                entity_indices[entity_count++] = MAX_PLAYER_INDEX;
            }
            return;
        }

        if (move_type == 3) { // Teleport
            plane = b.get_bits(2);
            int discard_move_queue = b.get_bits(1);

            if (b.get_bits(1) == 1) { // Update Required
                entity_indices[entity_count++] = MAX_PLAYER_INDEX;
            }

            int y = b.get_bits(7);
            int x = b.get_bits(7);
            self.move_to(x, y, discard_move_queue == 1);
        }
    }

    public static void update_new_players(Buffer b, int position) {
        while (b.bit_off + 10 < position * 8) {
            int player_index = b.get_bits(11);

            if (player_index == 2047) {
                break;
            }

            if (players[player_index] == null) {
                players[player_index] = new Player();
                if (player_buffer[player_index] != null) {
                    players[player_index].update(player_buffer[player_index]);
                }
            }

            player_indices[player_count++] = player_index;
            Player p = players[player_index];
            p.update_cycle = loop_cycle;

            if (b.get_bits(1) == 1) { // Update Required
                entity_indices[entity_count++] = player_index;
            }

            int discard_walk_queue = b.get_bits(1);
            int x = b.get_bits(5);
            int y = b.get_bits(5);

            if (x > 15) {
                x -= 32;
            }

            if (y > 15) {
                y -= 32;
            }

            p.move_to(self.path_x[0] + y, self.path_y[0] + x, discard_walk_queue == 1);
        }
        b.stop_bit_access();
    }

    public static void update_player_mask(int mask, int index, Buffer b, Player p) {
        if ((mask & 0x400) != 0) {
            p.move_start_x = b.get_ubyte_s();
            p.move_start_y = b.get_ubyte_s();
            p.move_end_x = b.get_ubyte_s();
            p.move_end_y = b.get_ubyte_s();
            p.move_cycle_end = b.get_le_ushort_a() + loop_cycle;
            p.move_cycle_start = b.get_ushort_a() + loop_cycle;
            p.move_direction = b.get_ubyte_s();
            p.reset_positions();
        }

        if ((mask & 0x100) != 0) { // Graphics
            p.spotanim_index = b.get_le_ushort();
            int info = b.get_int();
            p.graphic_offset_y = info >> 16;
            p.spotanim_cycle_end = loop_cycle + (info & 0xffff);
            p.spotanim_frame = 0;
            p.spotanim_cycle = 0;

            if (p.spotanim_cycle_end > loop_cycle) {
                p.spotanim_frame = -1;
            }

            if (p.spotanim_index == 65535) {
                p.spotanim_index = -1;
            }
        }

        if ((mask & 8) != 0) { // Sequence
            int sequence_index = b.get_le_ushort();
            int delay = b.get_ubyte_c();

            System.out.println("sequence: " + sequence_index);

            if (sequence_index == 65535) {
                sequence_index = -1;
            }

            if (sequence_index == p.seq_index && sequence_index != -1) {
                int type = Sequence.instance[sequence_index].type;
                if (type == 1) {
                    p.seq_frame = 0;
                    p.seq_cycle = 0;
                    p.seq_delay_cycle = delay;
                    p.seq_reset_cycle = 0;
                } else if (type == 2) {
                    p.seq_reset_cycle = 0;
                }
            } else if (sequence_index == -1 || p.seq_index == -1 || Sequence.instance[sequence_index].priority >= Sequence.instance[p.seq_index].priority) {
                p.seq_index = sequence_index;
                p.seq_frame = 0;
                p.seq_cycle = 0;
                p.seq_delay_cycle = delay;
                p.seq_reset_cycle = 0;
                p.still_path_position = p.path_position;
            }
        }

        if ((mask & 4) != 0) { // Forced Chat
            p.spoken_message = b.get_string();

            if (p.spoken_message.charAt(0) == '~') {
                p.spoken_message = p.spoken_message.substring(1);
                Chat.put(p.name, p.spoken_message, 2);
            } else if (p == self) {
                Chat.put(p.name, p.spoken_message, 2);
            }

            p.spoken_color = 0;
            p.spoken_effect = 0;
            p.spoken_life = 150;
        }

        if ((mask & 0x80) != 0) { // Chat
            int settings = b.get_le_ushort();
            int rights = b.get_ubyte();
            int length = b.get_ubyte_c();
            int start_off = b.position;

            if (p.name != null && p.visible) {
                long name_long = JString.get_long(p.name);

                boolean ignored = false;
                if (rights <= 1) {
                    for (int i = 0; i < ignore_count; i++) {
                        if (ignore_long[i] != name_long) {
                            continue;
                        }
                        ignored = true;
                        break;
                    }
                }

                if (!ignored && message_status == 0) {
                    try {
                        Chat.buffer.position = 0;
                        b.get_bytes_reversed(Chat.buffer.payload, 0, length);
                        Chat.buffer.position = 0;

                        p.spoken_message = JString.get_formatted(length, Chat.buffer);
                        p.spoken_effect = settings & 0xFF;
                        p.spoken_color = settings >> 8;
                        p.spoken_life = 150;

                        StringBuilder sb = new StringBuilder();

                        if (rights > 0) {
                            sb.append("@cr").append(rights).append("@");
                        }

                        sb.append(p.name);

                        Chat.put(sb.toString(), p.spoken_message, rights == 0 ? Chat.TYPE_PLAYER : Chat.TYPE_MODERATOR);
                    } catch (Exception e) {
                        Signlink.error("cde2");
                    }
                }

            }
            b.position = start_off + length;
        }

        if ((mask & 1) != 0) { // Face Entity
            p.face_entity = b.get_le_ushort();

            if (p.face_entity == 65535) {
                p.face_entity = -1;
            }
        }

        if ((mask & 0x10) != 0) { // Update Appearance
            byte[] payload = new byte[b.get_ubyte_c()];
            b.get_bytes(payload, 0, payload.length);
            Buffer pb = new Buffer(payload);
            player_buffer[index] = pb;
            p.update(pb);
        }

        if ((mask & 2) != 0) { // Face Coordinates
            p.face_x = b.get_le_ushort_a();
            p.face_y = b.get_le_ushort();
        }

        if ((mask & 0x20) != 0) { // Damage Update
            int damage = b.get_ubyte();
            int type = b.get_ubyte_a();
            p.hit(type, damage, loop_cycle);
            p.combat_cycle = loop_cycle + 300;
            p.current_health = b.get_ubyte_c();
            p.max_health = b.get_ubyte();
        }

        if ((mask & 0x200) != 0) { // Damage Update 2
            int damage = b.get_ubyte();
            int type = b.get_ubyte_s();
            p.hit(type, damage, loop_cycle);
            p.combat_cycle = loop_cycle + 300;
            p.current_health = b.get_ubyte();
            p.max_health = b.get_ubyte_c();
        }
    }

    public static void update_player_masks(Buffer b) {
        for (int i = 0; i < entity_count; i++) {
            int index = entity_indices[i];
            Player p = players[index];

            int mask = b.get_ubyte();

            if ((mask & 0x40) != 0) {
                mask += b.get_ubyte() << 8;
            }

            update_player_mask(mask, index, b, p);
        }
    }

    public static void update_player_movement(Buffer b) {
        int player_count = b.get_bits(8);

        if (player_count < Game.player_count) {
            for (int k = player_count; k < Game.player_count; k++) {
                entity_update_indices[entity_update_count++] = player_indices[k];
            }
        }

        if (player_count > Game.player_count) {
            Signlink.error(username + " Too many players");
            throw new RuntimeException(JString.EEK);
        }

        Game.player_count = 0;

        for (int i = 0; i < player_count; i++) {
            int player_index = player_indices[i];
            Player p = players[player_index];

            // If we don't need a movement update
            if (b.get_bits(1) == 0) {
                player_indices[Game.player_count++] = player_index;
                p.update_cycle = loop_cycle;
            } else {
                int move_type = b.get_bits(2);

                switch (move_type) {
                    case 0: { // No Movement
                        player_indices[Game.player_count++] = player_index;
                        p.update_cycle = loop_cycle;
                        entity_indices[entity_count++] = player_index;
                        break;
                    }
                    case 1: { // Walk
                        player_indices[Game.player_count++] = player_index;
                        p.update_cycle = loop_cycle;
                        p.move(b.get_bits(3), false);

                        if (b.get_bits(1) == 1) { // Update Required
                            entity_indices[entity_count++] = player_index;
                        }
                        break;
                    }
                    case 2: { // Run
                        player_indices[Game.player_count++] = player_index;
                        p.update_cycle = loop_cycle;
                        p.move(b.get_bits(3), true);
                        p.move(b.get_bits(3), true);

                        if (b.get_bits(1) == 1) { // Update Required
                            entity_indices[entity_count++] = player_index;
                        }
                        break;
                    }
                    case 3: { // Unknown
                        entity_update_indices[entity_update_count++] = player_index;
                        break;
                    }
                }
            }
        }
    }

    public static void update_players(int psize, Buffer b) {
        entity_update_count = 0;
        entity_count = 0;

        update_localplayer_movement(b);
        update_player_movement(b);
        update_new_players(b, psize);
        update_player_masks(b);

        for (int i = 0; i < entity_update_count; i++) {
            int player_index = entity_update_indices[i];
            if (players[player_index].update_cycle != loop_cycle) {
                players[player_index] = null;
            }
        }

        if (b.position != psize) {
            Signlink.error("Error packet size mismatch in getplayer pos:" + b.position + " psize:" + psize);
            throw new RuntimeException(JString.EEK);
        }

        for (int i = 0; i < player_count; i++) {
            if (players[player_indices[i]] == null) {
                Signlink.error(username + " null entry in pl list - pos:" + i + " size:" + player_count);
                throw new RuntimeException(JString.EEK);
            }
        }
    }

    public static void update_widget(Widget w) {
        int type = w.action_type;

        if (CharacterDesign.update(w, type)) {
            return;
        }

        if (type >= 1 && type <= 100 || type >= 701 && type <= 800) {
            if (type == 1 && frenemies_status == 0) {
                w.message_disabled = JString.LOADING_FRIEND_LIST;
                w.option_type = 0;
                return;
            }
            if (type == 1 && frenemies_status == 1) {
                w.message_disabled = JString.CONNECTING_TO_FRIENDSERVER;
                w.option_type = 0;
                return;
            }
            if (type == 2 && frenemies_status != 2) {
                w.message_disabled = JString.PLEASE_WAIT;
                w.option_type = 0;
                return;
            }
            int count = Game.friend_count;

            if (frenemies_status != 2) {
                count = 0;
            }

            if (type > 700) {
                type -= 601;
            } else {
                type--;
            }

            if (type >= count) {
                w.message_disabled = JString.BLANK;
                w.option_type = 0;
                return;
            } else {
                w.message_disabled = friend_name[type];
                w.option_type = 1;
                return;
            }
        }

        if (type >= 101 && type <= 200 || type >= 801 && type <= 900) {
            int count = Game.friend_count;

            if (frenemies_status != 2) {
                count = 0;
            }

            if (type > 800) {
                type -= 701;
            } else {
                type -= 101;
            }

            if (type >= count) {
                w.message_disabled = JString.BLANK;
                w.option_type = 0;
                return;
            }

            if (friend_node[type] == 0) {
                w.message_disabled = JString.OFFLINE;
            } else if (friend_node[type] == node_index) {
                w.message_disabled = JString.WORLD + (friend_node[type] - 9);
            } else {
                w.message_disabled = JString.WORLD + (friend_node[type] - 9);
            }

            w.option_type = 1;
            return;
        }

        if (type == 203) {
            int count = friend_count;

            if (frenemies_status != 2) {
                count = 0;
            }

            w.scroll_height = count * 15 + 20;

            if (w.scroll_height <= w.height) {
                w.scroll_height = w.height + 1;
            }
            return;
        }

        if (type >= 401 && type <= 500) {
            if ((type -= 401) == 0 && frenemies_status == 0) {
                w.message_disabled = JString.LOADING_IGNORE_LIST;
                w.option_type = 0;
                return;
            }

            if (type == 1 && frenemies_status == 0) {
                w.message_disabled = JString.PLEASE_WAIT;
                w.option_type = 0;
                return;
            }

            int count = ignore_count;

            if (frenemies_status == 0) {
                count = 0;
            }

            if (type >= count) {
                w.message_disabled = JString.BLANK;
                w.option_type = 0;
                return;
            } else {
                w.message_disabled = JString.get_formatted_string(ignore_long[type]);
                w.option_type = 1;
                return;
            }
        }

        if (type == 503) {
            w.scroll_height = ignore_count * 15 + 20;
            if (w.scroll_height <= w.height) {
                w.scroll_height = w.height + 1;
            }
            return;
        }

        if (type == 600) {
            w.message_disabled = report_abuse_input;

            if (loop_cycle % 20 < 10) {
                w.message_disabled += '|';
            } else {
                w.message_disabled += ' ';
            }
            return;
        }

        if (type == 620) {
            if (report_abuse_mute) {
                w.rgb_disabled = 0x00ff00;
                w.message_disabled = JString.MUTE_ON;
            } else {
                w.rgb_disabled = 0xffffff;
                w.message_disabled = JString.MUTE_OFF;
            }
        }

        if (type == 650 || type == 655) {
            if (welcome_last_ip != 0) {
                String s;
                if (welcome_last_playdate == 0) {
                    s = JString.EARLIER_TODAY;
                } else if (welcome_last_playdate == 1) {
                    s = JString.YESTERDAY;
                } else {
                    s = welcome_last_playdate + ' ' + JString.DAYS_AGO;
                }
                w.message_disabled = "You last logged in " + s + " from: " + Signlink.dns;
            } else {
                w.message_disabled = JString.BLANK;
            }
        }

        if (type == 651) {
            if (welcome_unread_messages == 0) {
                w.message_disabled = JString.UNREAD_MESSAGES_0;
                w.rgb_disabled = 0xFFFF00;
            }
            if (welcome_unread_messages == 1) {
                w.message_disabled = JString.UNREAD_MESSAGE_1;
                w.rgb_disabled = 0xFF00;
            }
            if (welcome_unread_messages > 1) {
                w.message_disabled = welcome_unread_messages + ' ' + JString.UNREAD_MESSAGES;
                w.rgb_disabled = 0xFF00;
            }
        }

        if (type == 652) {
            if (welcome_info == 201) {
                if (welcome_notify == 1) {
                    w.message_disabled = JString.NON_MEMBERS_1;
                } else {
                    w.message_disabled = JString.BLANK;
                }
            } else if (welcome_info == 200) {
                w.message_disabled = JString.NO_RECOVERIES;
            } else {
                String s1;
                if (welcome_info == 0) {
                    s1 = "Earlier today";
                } else if (welcome_info == 1) {
                    s1 = "Yesterday";
                } else {
                    s1 = welcome_info + ' ' + JString.DAYS_AGO;
                }
                w.message_disabled = s1 + " you changed your recovery questions";
            }
        }

        if (type == 653) {
            if (welcome_info == 201) {
                if (welcome_notify == 1) {
                    w.message_disabled = JString.NON_MEMBERS_2;
                } else {
                    w.message_disabled = JString.BLANK;
                }
            } else if (welcome_info == 200) {
                w.message_disabled = JString.SECURE_YOUR_ACCOUNT;
            } else {
                w.message_disabled = JString.CANCEL_RECOVERIES;
            }
        }

        if (type == 654) {
            if (welcome_info == 201) {
                if (welcome_notify == 1) {
                    w.message_disabled = JString.NON_MEMBERS_3;
                    return;
                } else {
                    w.message_disabled = JString.BLANK;
                    return;
                }
            }
            if (welcome_info == 200) {
                w.message_disabled = JString.ACCOUNT_MANAGEMENT;
                return;
            }
            w.message_disabled = JString.ACCOUNT_MANAGEMENT;
        }
    }

    public static boolean walk_to(int click_type, int size_x, int size_y, int start_x, int start_y, int dest_x, int dest_y, int type, int face_flags, int rotation, boolean arbitrary) {
        byte map_size_x = 104;
        byte map_size_y = 104;

        for (int x1 = 0; x1 < map_size_x; x1++) {
            for (int y1 = 0; y1 < map_size_y; y1++) {
                path_waypoint[x1][y1] = 0;
                path_distance[x1][y1] = 0x5f5e0ff;
            }

        }

        int x = start_x;
        int y = start_y;
        path_waypoint[start_x][start_y] = 99;
        path_distance[start_x][start_y] = 0;

        int next = 0;
        int current = 0;
        path_queue_x[next] = start_x;
        path_queue_y[next++] = start_y;

        boolean reached = false;
        int path_length = path_queue_x.length;
        int ai[][] = collision_maps[plane].flags;

        while (current != next) {
            x = path_queue_x[current];
            y = path_queue_y[current];
            current = (current + 1) % path_length;

            if (x == dest_x && y == dest_y) {
                reached = true;
                break;
            }

            if (type != 0) {
                if ((type < 5 || type == 10) && collision_maps[plane].at_wall(x, y, dest_x, dest_y, type - 1, rotation)) {
                    reached = true;
                    break;
                }
                if (type < 10 && collision_maps[plane].at_decoration(x, y, dest_x, dest_y, type - 1, rotation)) {
                    reached = true;
                    break;
                }
            }

            if (size_x != 0 && size_y != 0 && collision_maps[plane].at_object(x, y, dest_x, dest_y, size_x, size_y, face_flags)) {
                reached = true;
                break;
            }

            int distance = path_distance[x][y] + 1;

            if (x > 0 && path_waypoint[x - 1][y] == 0 && (ai[x - 1][y] & 0x1280108) == 0) {
                path_queue_x[next] = x - 1;
                path_queue_y[next] = y;
                next = (next + 1) % path_length;
                path_waypoint[x - 1][y] = 2;
                path_distance[x - 1][y] = distance;
            }

            if (x < map_size_x - 1 && path_waypoint[x + 1][y] == 0 && (ai[x + 1][y] & 0x1280180) == 0) {
                path_queue_x[next] = x + 1;
                path_queue_y[next] = y;
                next = (next + 1) % path_length;
                path_waypoint[x + 1][y] = 8;
                path_distance[x + 1][y] = distance;
            }

            if (y > 0 && path_waypoint[x][y - 1] == 0 && (ai[x][y - 1] & 0x1280102) == 0) {
                path_queue_x[next] = x;
                path_queue_y[next] = y - 1;
                next = (next + 1) % path_length;
                path_waypoint[x][y - 1] = 1;
                path_distance[x][y - 1] = distance;
            }

            if (y < map_size_y - 1 && path_waypoint[x][y + 1] == 0 && (ai[x][y + 1] & 0x1280120) == 0) {
                path_queue_x[next] = x;
                path_queue_y[next] = y + 1;
                next = (next + 1) % path_length;
                path_waypoint[x][y + 1] = 4;
                path_distance[x][y + 1] = distance;
            }

            if (x > 0 && y > 0 && path_waypoint[x - 1][y - 1] == 0 && (ai[x - 1][y - 1] & 0x128010e) == 0 && (ai[x - 1][y] & 0x1280108) == 0 && (ai[x][y - 1] & 0x1280102) == 0) {
                path_queue_x[next] = x - 1;
                path_queue_y[next] = y - 1;
                next = (next + 1) % path_length;
                path_waypoint[x - 1][y - 1] = 3;
                path_distance[x - 1][y - 1] = distance;
            }

            if (x < map_size_x - 1 && y > 0 && path_waypoint[x + 1][y - 1] == 0 && (ai[x + 1][y - 1] & 0x1280183) == 0 && (ai[x + 1][y] & 0x1280180) == 0 && (ai[x][y - 1] & 0x1280102) == 0) {
                path_queue_x[next] = x + 1;
                path_queue_y[next] = y - 1;
                next = (next + 1) % path_length;
                path_waypoint[x + 1][y - 1] = 9;
                path_distance[x + 1][y - 1] = distance;
            }

            if (x > 0 && y < map_size_y - 1 && path_waypoint[x - 1][y + 1] == 0 && (ai[x - 1][y + 1] & 0x1280138) == 0 && (ai[x - 1][y] & 0x1280108) == 0 && (ai[x][y + 1] & 0x1280120) == 0) {
                path_queue_x[next] = x - 1;
                path_queue_y[next] = y + 1;
                next = (next + 1) % path_length;
                path_waypoint[x - 1][y + 1] = 6;
                path_distance[x - 1][y + 1] = distance;
            }

            if (x < map_size_x - 1 && y < map_size_y - 1 && path_waypoint[x + 1][y + 1] == 0 && (ai[x + 1][y + 1] & 0x12801e0) == 0 && (ai[x + 1][y] & 0x1280180) == 0 && (ai[x][y + 1] & 0x1280120) == 0) {
                path_queue_x[next] = x + 1;
                path_queue_y[next] = y + 1;
                next = (next + 1) % path_length;
                path_waypoint[x + 1][y + 1] = 12;
                path_distance[x + 1][y + 1] = distance;
            }
        }

        path_arbitrary_dest = 0;

        if (!reached) {
            if (arbitrary) {
                int max_distance = 100;
                for (int dev = 1; dev < 2; dev++) {
                    for (int move_x = dest_x - dev; move_x <= dest_x + dev; move_x++) {
                        for (int move_y = dest_y - dev; move_y <= dest_y + dev; move_y++) {
                            if (move_x >= 0 && move_y >= 0 && move_x < 104 && move_y < 104 && path_distance[move_x][move_y] < max_distance) {
                                max_distance = path_distance[move_x][move_y];
                                x = move_x;
                                y = move_y;
                                path_arbitrary_dest = 1;
                                reached = true;
                            }
                        }

                    }

                    if (reached) {
                        break;
                    }
                }

            }
            if (!reached) {
                return false;
            }
        }

        current = 0;
        path_queue_x[current] = x;
        path_queue_y[current++] = y;
        int skip_check;

        for (int waypoint = skip_check = path_waypoint[x][y]; x != start_x || y != start_y; waypoint = path_waypoint[x][y]) {
            if (waypoint != skip_check) {
                skip_check = waypoint;
                path_queue_x[current] = x;
                path_queue_y[current++] = y;
            }
            if ((waypoint & 2) != 0) {
                x++;
            } else if ((waypoint & 8) != 0) {
                x--;
            }
            if ((waypoint & 1) != 0) {
                y++;
            } else if ((waypoint & 4) != 0) {
                y--;
            }
        }

        if (current > 0) {
            int index = current;

            if (index > 25) {
                index = 25;
            }

            current--;

            int path_x = path_queue_x[current];
            int path_y = path_queue_y[current];

            if (click_type == 0) {
                out.put_opcode(164);
            } else if (click_type == 1) {
                out.put_opcode(248);
            } else if (click_type == 2) {
                out.put_opcode(98);
            }

            // XXX: Removed dummy
            out.put_byte(index + index + 3);
            out.put_le_short_a(path_x + map_base_x);

            map_marker_x = path_queue_x[0];
            map_marker_y = path_queue_y[0];

            for (int i = 1; i < index; i++) {
                current--;
                out.put_byte(path_queue_x[current] - path_x);
                out.put_byte(path_queue_y[current] - path_y);
            }

            out.put_le_short(path_y + map_base_y);
            out.put_byte_c(Key.CTRL.is_down() ? 1 : 0);

            return true;
        }
        return click_type != 1;
    }

    public Game() {
        Game.instance = this;

        path_distance = new int[104][104];
        friend_node = new int[200];
        process_flames = false;
        actors = new Actor[16384];
        actor_indices = new int[16384];
        entity_update_indices = new int[1000];
        login_buffer = Buffer.create(1);
        widget_overlay = -1;
        skill_experience = new int[Skill.COUNT];
        anIntArray873 = new int[5];
        last_sound_index = -1;
        aBooleanArray876 = new boolean[5];
        draw_flames = false;
        report_abuse_input = JString.BLANK;
        local_player_index = -1;
        players = new Player[MAX_PLAYER_COUNT];
        player_indices = new int[MAX_PLAYER_COUNT];
        entity_indices = new int[MAX_PLAYER_COUNT];
        player_buffer = new Buffer[MAX_PLAYER_COUNT];
        Camera.pitch_modifier = 1;
        path_waypoint = new int[104][104];
        tmp_texels = new byte[16384];
        skill_level = new int[Skill.COUNT];
        ignore_long = new long[100];
        error_loading = false;
        anIntArray928 = new int[5];
        local_tile_cycle = new int[104][104];
        crc32 = new CRC32();
        is_focused = true;
        friend_long = new long[200];
        flame_thread = false;
        draw_x = -1;
        draw_y = -1;
        anIntArray968 = new int[33];
        cache = new Cache[5];
        settings = new int[2000];
        dragging_scrollbar = false;
        spoken_max = 50;
        spoken_x = new int[spoken_max];
        spoken_y = new int[spoken_max];
        spoken_off_y = new int[spoken_max];
        spoken_off_x = new int[spoken_max];
        spoken_color = new int[spoken_max];
        spoken_effect = new int[spoken_max];
        spoken_cycle = new int[spoken_max];
        spoken = new String[spoken_max];
        last_plane = -1;
        image_hit_marks = new Sprite[20];
        projectiles = new Chain();
        send_cam_info = false;
        widget_underlay = -1;
        anIntArray1030 = new int[5];
        image_mapfunctions = new Sprite[100];
        skill_real_level = new int[Skill.COUNT];
        default_settings = new int[2000];
        anIntArray1052 = new int[151];
        Sidebar.flashing_tab = null;
        spotanims = new Chain();
        anIntArray1057 = new int[33];
        bitmap_map_scenes = new Bitmap[100];
        loc_icon_x = new int[1000];
        loc_icon_y = new int[1000];
        scene_loading = false;
        friend_name = new String[200];
        in = Buffer.create(1);
        archive_crc = new int[9];
        image_head_icons = new Sprite[20];
        image_map_markers = new Sprite[2];
        image_mapdots = new Sprite[5];
        player_action = new String[5];
        player_action_priority = new boolean[5];
        region_chunk_uids = new int[4][13][13];
        cam_y_off_mod = 2;
        loc_icon = new Sprite[1000];
        restrict_region = false;
        dialogue_option_active = false;
        image_crosses = new Sprite[8];
        logged_in = false;
        report_abuse_mute = false;
        server_sent_chunk = false;
        cam_cinema_mode = false;
        map_zoom_modifier = 1;
        username = JString.BLANK;
        password = JString.BLANK;
        report_abuse_windex = -1;
        spawned_locs = new Chain();
        chase_cam_pitch = Camera.MIN_PITCH;
        Sidebar.widget_index = -1;
        out = Buffer.create(1);
        anIntArray1203 = new int[5];
        sound_index = new int[50];
        Camera.yaw_modifier = 2;
        bitmap_mod_icons = new Bitmap[3];
        anIntArray1229 = new int[151];
        collision_maps = new CollisionMap[4];
        sound_type = new int[50];
        dragging = false;
        sound_delay = new int[50];
        redraw = false;
        TitleScreen.set_message(JString.BLANK, JString.BLANK);
        cam_x_off_mod = 2;
        path_queue_x = new int[4000];
        path_queue_y = new int[4000];
        last_sound_type = -1;
    }

    @Override
    public void draw() {
        if (error_loading) {
            draw_error_screen();
            return;
        }

        draw_cycle++;

        if (!logged_in) {
            TitleScreen.draw(false);
        } else {
            draw_game();
        }

        click_cycle = 0;
    }

    @Override
    public void draw_progress(String caption, int percent) {
        progress_percent = percent;
        progress_caption = caption;

        TitleScreen.create_producers();

        if (archive_title == null) {
            super.draw_progress(caption, percent);
            return;
        }

        TitleScreen.producer_box.prepare();
        {
            int x = TitleScreen.producer_box.width / 2;
            int y = TitleScreen.producer_box.height / 2;

            y -= 48;

            BitmapFont.BOLD.draw(JString.LOADING_PLEASE_WAIT, x, y, RSColor.WHITE, BitmapFont.SHADOW_CENTER);

            y += 10;

            Canvas2D.draw_rect(x - 152, y, 304, 34, 0x8C1111);
            Canvas2D.fill_rect(x - 151, y + 1, 302, 32, 0);
            Canvas2D.fill_rect(x - 150, y + 2, (int) (300 * (percent / 100D)), 30, 0x8C1111);

            y += 17;

            BitmapFont.BOLD.draw(caption, x, y + 5, RSColor.WHITE, BitmapFont.SHADOW_CENTER);
        }
        TitleScreen.producer_box.draw(202, 171);

        if (redraw) {
            redraw = false;
            if (!process_flames) {
                Flames.producer[0].draw(0, 0);
                Flames.producer[1].draw(637, 0);
            }

            ImageProducer[] background = TitleScreen.producer_background;
            background[0].draw(128, 0);
            background[1].draw(202, 371);
            background[2].draw(0, 265);
            background[3].draw(562, 265);
            background[4].draw(128, 171);
            background[5].draw(562, 171);
        }
    }

    @Override
    public Socket get_socket(int port) throws IOException {
        return new Socket(InetAddress.getByName(getCodeBase().getHost()), port);
    }

    public URL getCodeBase() {
        try {
            if (instance.frame != null) {
                return new URL("http://" + JString.SERVER + ':' + (80 + port_offset));
            }
        } catch (Exception _ex) {
        }
        return instance.getCodeBase();
    }

    @Override
    public Component getComponent() {
        if (instance.frame != null) {
            return instance.frame.canvas;
        }

        return this;
    }

    public void init() {
        node_index = Integer.parseInt(getParameter("nodeid"));
        port_offset = Integer.parseInt(getParameter("portoff"));

        String s = getParameter("lowmem");

        if (s != null && s.equals("1")) {
            low_detail = true;
        } else {
            low_detail = false;
        }

        s = getParameter("free");

        if (s != null && s.equals("1")) {
            is_members = false;
        } else {
            is_members = true;
        }

        this.init(false);
    }

    @Override
    public void process() {
        if (error_loading) {
            return;
        }

        loop_cycle++;

        music.handle();

        if (!logged_in) {
            TitleScreen.handle();
        } else {
            handle();
        }

        handle_ondemand();
    }

    @Override
    public void redraw() {
        redraw = true;
    }

    public void retrieve_checksums() {
        archive_crc[8] = 0;
        String error = JString.UNKNOWN_ERROR;

        while (archive_crc[8] == 0) {
            draw_progress(JString.CONNECTING_TO_SERVER, 20);

            try {
                DataInputStream in = Jaggrab.request("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
                Buffer b = new Buffer(new byte[40]);
                in.readFully(b.payload, 0, 40);
                in.close();

                for (int i = 0; i < 9; i++) {
                    archive_crc[i] = b.get_int();
                }

                int server_crc = b.get_int();
                int client_crc = 1234;

                for (int l1 = 0; l1 < 9; l1++) {
                    client_crc = (client_crc << 1) + archive_crc[l1];
                }

                if (server_crc != client_crc) {
                    archive_crc[8] = 0;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                error = JString.EOF_ERROR;
                archive_crc[8] = 0;
            } catch (IOException e) {
                error = JString.CONNECTION_ERROR;
                archive_crc[8] = 0;
            } catch (Exception e) {
                error = JString.UNEXPECTED_ERROR;
                archive_crc[8] = 0;
            }

            if (archive_crc[8] == 0) {
                for (int i = 5; i > 0; i--) {
                    try {
                        draw_progress(error + " - Retrying in " + i, 20);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        if (draw_flames) {
            draw_flames();
            return;
        }

        super.run();
    }

    @Override
    public void shutdown() {
        Signlink.error = false;

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception _ex) {
        }

        connection = null;

        close_music_player();

        if (mouse_recorder != null) {
            mouse_recorder.active = false;
        }

        mouse_recorder = null;
        ondemand.stop();
        ondemand = null;
        out = null;
        login_buffer = null;
        in = null;
        chunk_coords = null;
        chunk_loc_payload = null;
        chunk_landscape_payload = null;
        map_uids = null;
        landscape_uids = null;
        height_map = null;
        render_flags = null;
        landscape = null;
        collision_maps = null;
        path_waypoint = null;
        path_distance = null;
        path_queue_x = null;
        path_queue_y = null;
        tmp_texels = null;
        producer_minimap = null;
        producer_scene = null;
        Chat.producer = null;
        Chat.Settings.producer = null;
        producer_backleft1 = null;
        producer_backleft2 = null;
        producer_backright1 = null;
        producer_backright2 = null;
        producer_backtop1 = null;
        producer_backvmid1 = null;
        producer_backvmid2 = null;
        producer_backvmid3 = null;
        producer_backhmid2 = null;
        bitmap_mapback = null;
        bitmap_backbase1 = null;
        Sidebar.nullify();
        image_compass = null;
        image_hit_marks = null;
        image_head_icons = null;
        image_overlay_multiway = null;
        image_map_markers = null;
        image_crosses = null;
        image_mapdots = null;
        bitmap_map_scenes = null;
        image_mapfunctions = null;
        local_tile_cycle = null;
        players = null;
        player_indices = null;
        entity_indices = null;
        player_buffer = null;
        entity_update_indices = null;
        actors = null;
        actor_indices = null;
        Game.item_pile = null;
        spawned_locs = null;
        projectiles = null;
        spotanims = null;
        settings = null;
        loc_icon_x = null;
        loc_icon_y = null;
        loc_icon = null;
        image_minimap = null;

        friend_name = null;
        friend_long = null;
        friend_node = null;

        TitleScreen.nullify();
        Flames.nullify();
        Menu.nullify();
        LocConfig.nullify();
        ActorConfig.nullify();
        ObjConfig.nullify();
        Floor.instance = null;

        IdentityKit.instance = null;
        Widget.instance = null;
        Sequence.instance = null;

        SpotAnimConfig.instance = null;
        SpotAnimConfig.model_cache = null;

        Varp.instance = null;
        Player.model_cache = null;

        Canvas3D.nullify();
        Landscape.nullify();
        Model.nullify();
        SequenceFrame.nullify();
        System.gc();
    }

    @Override
    public void startup() {
        this.draw_progress(JString.STARTING_UP, 20);

        if (Signlink.cache_file != null) {
            for (int i = 0; i < 5; i++) {
                Game.cache[i] = new Cache(i + 1, Signlink.cache_file, Signlink.cache_index[i]);
            }
        }

        try {
            if (VERIFY_CACHE) {
                retrieve_checksums();
            }

            archive_title = get_archive("title screen", 1, "title", archive_crc[1], 25);

            BitmapFont.SMALL = new BitmapFont("p11_full", archive_title);
            BitmapFont.NORMAL = new BitmapFont("p12_full", archive_title);
            BitmapFont.BOLD = new BitmapFont("b12_full", archive_title);
            BitmapFont.FANCY = new BitmapFont("q8_full", archive_title);

            TitleScreen.create_background();
            TitleScreen.create_images();

            Archive archive_config = get_archive("config", 2, "config", archive_crc[2], 30);
            Archive archive_widget = get_archive("interface", 3, "interface", archive_crc[3], 35);
            Archive archive_media = get_archive("2d graphics", 4, "media", archive_crc[4], 40);
            Archive archive_texture = get_archive("textures", 6, "textures", archive_crc[6], 45);
            Archive archive_sound = get_archive("sound effects", 8, "sounds", archive_crc[8], 55);

            render_flags = new byte[4][104][104];
            height_map = new short[4][105][105];

            landscape = new Landscape(104, 104, 4, height_map);

            for (int plane = 0; plane < 4; plane++) {
                collision_maps[plane] = new CollisionMap(104, 104);
            }

            image_minimap = new Sprite(512, 512);

            Archive archive_version = get_archive("update list", 5, "versionlist", archive_crc[5], 60);

            draw_progress(JString.CONNECTING_TO_UPDATE_SERVER, 60);

            ondemand = new OnDemand();
            ondemand.setup(archive_version, this);

            SequenceFrame.init(ondemand.seq_frame_count());
            Model.init(ondemand.get_file_count(0), ondemand);

            music = new MusicPlayer();

            if (!low_detail) {
                ondemand.send_request(2, 484);

                while (ondemand.immediate_request_count() > 0) {
                    handle_ondemand();

                    try {
                        Thread.sleep(100L);
                    } catch (Exception _ex) {
                    }

                    if (ondemand.fails > 3) {
                        return;
                    }
                }
            }

            int count = 0;

            draw_progress(JString.REQUESTING_ANIMATIONS, 65);
            {
                count = ondemand.get_file_count(1);

                for (int i = 0; i < count; i++) {
                    ondemand.send_request(1, i);
                }

                while (ondemand.immediate_request_count() > 0) {
                    int remaining = count - ondemand.immediate_request_count();

                    if (remaining > 0) {
                        draw_progress("Loading animations - " + ((remaining * 100) / count) + "%", 65);
                    }

                    handle_ondemand();

                    try {
                        Thread.sleep(100L);
                    } catch (Exception _ex) {
                    }

                    if (ondemand.fails > 3) {
                        return;
                    }
                }
            }

            draw_progress(JString.REQUESTING_MODELS, 70);
            {
                count = ondemand.get_file_count(0);

                for (int i = 0; i < count; i++) {
                    int flags = ondemand.mesh_flags(i);
                    if ((flags & 1) != 0) {
                        ondemand.send_request(0, i);
                    }
                }

                count = ondemand.immediate_request_count();

                while (ondemand.immediate_request_count() > 0) {
                    int remaining = count - ondemand.immediate_request_count();

                    if (remaining > 0) {
                        draw_progress("Loading models - " + (remaining * 100) / count + "%", 70);
                    }

                    handle_ondemand();

                    try {
                        Thread.sleep(100L);
                    } catch (Exception _ex) {
                    }
                }
            }

            draw_progress(JString.REQUESTING_MAPS, 75);
            {
                if (cache[0] != null) {
                    ondemand.send_request(3, ondemand.get_map_uid(47, 48, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(47, 48, 1));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 48, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 48, 1));
                    ondemand.send_request(3, ondemand.get_map_uid(49, 48, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(49, 48, 1));
                    ondemand.send_request(3, ondemand.get_map_uid(47, 47, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(47, 47, 1));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 47, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 47, 1));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 148, 0));
                    ondemand.send_request(3, ondemand.get_map_uid(48, 148, 1));
                    count = ondemand.immediate_request_count();

                    while (ondemand.immediate_request_count() > 0) {
                        int remaining = count - ondemand.immediate_request_count();

                        if (remaining > 0) {
                            draw_progress("Loading maps - " + (remaining * 100) / count + "%", 75);
                        }

                        handle_ondemand();

                        try {
                            Thread.sleep(100L);
                        } catch (Exception _ex) {
                        }
                    }
                }

                count = ondemand.get_file_count(0);

                for (int i = 0; i < count; i++) {
                    int flags = ondemand.mesh_flags(i);
                    byte priority = 0;

                    if ((flags & 8) != 0) {
                        priority = 10;
                    } else if ((flags & 0x20) != 0) {
                        priority = 9;
                    } else if ((flags & 0x10) != 0) {
                        priority = 8;
                    } else if ((flags & 0x40) != 0) {
                        priority = 7;
                    } else if ((flags & 0x80) != 0) {
                        priority = 6;
                    } else if ((flags & 2) != 0) {
                        priority = 5;
                    } else if ((flags & 4) != 0) {
                        priority = 4;
                    }

                    if ((flags & 1) != 0) {
                        priority = 3;
                    }

                    if (priority != 0) {
                        ondemand.verify(priority, 0, i);
                    }
                }

                ondemand.request_regions(is_members);

                if (!low_detail) {
                    for (int i = 1; i < ondemand.get_file_count(2); i++) {
                        if (ondemand.has_midi(i)) {
                            ondemand.verify((byte) 1, 2, i);
                        }
                    }
                }
            }

            draw_progress(JString.UNPACKING_MEDIA, 80);
            {
                Chat.background = new Bitmap(archive_media, "chatback", 0);
                Chat.Settings.background = new Bitmap(archive_media, "backbase1", 0);

                bitmap_mapback = new Bitmap(archive_media, "mapback", 0);

                image_compass = new Sprite(archive_media, "compass", 0);
                image_mapedge = new Sprite(archive_media, "mapedge", 0);
                image_mapedge.crop();

                Sidebar.load(archive_media);

                for (int i = 0; i < image_map_markers.length; i++) {
                    image_map_markers[i] = new Sprite(archive_media, JString.MAP_MARKER, i);
                }

                for (int i = 0; i < image_crosses.length; i++) {
                    image_crosses[i] = new Sprite(archive_media, JString.CROSS, i);
                }

                for (int i = 0; i < image_mapdots.length; i++) {
                    image_mapdots[i] = new Sprite(archive_media, JString.MAP_DOTS, i);
                }

                for (int i = 0; i < 2; i++) {
                    bitmap_mod_icons[i] = new Bitmap(archive_media, JString.MOD_ICONS, i);
                }

                bitmap_mod_icons[2] = bitmap_mod_icons[1];

                try {
                    for (int i = 0; i < 100; i++) {
                        bitmap_map_scenes[i] = new Bitmap(archive_media, JString.MAP_SCENE, i);
                    }
                } catch (Exception e) {
                }

                try {
                    for (int i = 0; i < 100; i++) {
                        image_mapfunctions[i] = new Sprite(archive_media, JString.MAP_FUNCTION, i);
                    }
                } catch (Exception e) {
                }

                try {
                    for (int i = 0; i < 20; i++) {
                        image_hit_marks[i] = new Sprite(archive_media, JString.HITMARKS, i);
                    }
                } catch (Exception e) {
                }

                try {
                    for (int i = 0; i < 8; i++) {
                        image_head_icons[i] = new Sprite(archive_media, JString.HEADICONS, i);
                    }
                } catch (Exception e) {
                }

                try {
                    image_overlay_multiway = image_head_icons[1];
                } catch (Exception e) {

                }

                bitmap_scrollbar_up = new Bitmap(archive_media, JString.SCROLLBAR, 0);
                bitmap_scrollbar_down = new Bitmap(archive_media, JString.SCROLLBAR, 1);

                producer_backleft1 = ImageProducer.derive(archive_media, "backleft1");
                producer_backleft2 = ImageProducer.derive(archive_media, "backleft2");
                producer_backright1 = ImageProducer.derive(archive_media, "backright1");
                producer_backright2 = ImageProducer.derive(archive_media, "backright2");
                producer_backtop1 = ImageProducer.derive(archive_media, "backtop1");
                producer_backvmid1 = ImageProducer.derive(archive_media, "backvmid1");
                producer_backvmid2 = ImageProducer.derive(archive_media, "backvmid2");
                producer_backvmid3 = ImageProducer.derive(archive_media, "backvmid3");
                producer_backhmid2 = ImageProducer.derive(archive_media, "backhmid2");

                int red_offset = (int) (Math.random() * 11D) - 5;
                int green_offset = (int) (Math.random() * 11D) - 5;
                int blue_offset = (int) (Math.random() * 11D) - 5;
                int offset = (int) (Math.random() * 21D) - 10;

                for (int i = 0; i < 100; i++) {
                    if (image_mapfunctions[i] != null) {
                        image_mapfunctions[i].translate_rgb(red_offset + offset, green_offset + offset, blue_offset + offset);
                    }
                    if (bitmap_map_scenes[i] != null) {
                        bitmap_map_scenes[i].translate_rgb(red_offset + offset, green_offset + offset, blue_offset + offset);
                    }
                }
            }

            draw_progress(JString.UNPACKING_TEXTURES, 83);
            {
                Canvas3D.unpack_textures(archive_texture);
                Canvas3D.create_palette(0.8D);
                Canvas3D.setup_texel_pools();
            }

            draw_progress(JString.UNPACKING_CONFIG, 86);
            {
                Sequence.unpack(archive_config);
                LocConfig.unpack(archive_config);
                Floor.unpack(archive_config);
                ObjConfig.unpack(archive_config);
                ActorConfig.unpack(archive_config);
                IdentityKit.unpack(archive_config);
                SpotAnimConfig.unpack(archive_config);
                Varp.unpack(archive_config);
                VarBit.unpack(archive_config);
            }

            draw_progress(JString.UNPACKING_SOUNDS, 90);
            {
                if (!low_detail) {
                    WaveSound.unpack(new Buffer(archive_sound.get("sounds.dat")));
                }
            }

            draw_progress(JString.UNPACKING_WIDGETS, 95);
            {
                Widget.unpack(archive_widget, new BitmapFont[]{BitmapFont.SMALL, BitmapFont.NORMAL, BitmapFont.BOLD, BitmapFont.FANCY}, archive_media);
            }

            draw_progress(JString.PREPARING_GAME_ENGINE, 100);
            {

                for (int y = 0; y < 33; y++) {
                    int k6 = 999;
                    int i7 = 0;
                    for (int x = 0; x < 34; x++) {
                        if (bitmap_mapback.pixels[x + (y * bitmap_mapback.width)] == 0) {
                            if (k6 == 999) {
                                k6 = x;
                            }
                            continue;
                        }
                        if (k6 == 999) {
                            continue;
                        }
                        i7 = x;
                        break;
                    }

                    anIntArray968[y] = k6;
                    anIntArray1057[y] = i7 - k6;
                }

                for (int y = 5; y < 156; y++) {
                    int j7 = 999;
                    int l7 = 0;
                    for (int x = 25; x < 172; x++) {
                        if (bitmap_mapback.pixels[x + y * bitmap_mapback.width] == 0 && (x > 34 || y > 34)) {
                            if (j7 == 999) {
                                j7 = x;
                            }
                            continue;
                        }
                        if (j7 == 999) {
                            continue;
                        }
                        l7 = x;
                        break;
                    }

                    anIntArray1052[y - 5] = j7 - 25;
                    anIntArray1229[y - 5] = l7 - j7;
                }

                Canvas3D.prepare(479, 96);
                chat_pixels_3d = Canvas3D.pixels;
                Canvas3D.prepare(190, 261);
                Sidebar.pixels_3d = Canvas3D.pixels;
                Canvas3D.prepare(512, 334);
                viewport_pixels = Canvas3D.pixels;

                int z_array[] = new int[9];
                for (int z_index = 0; z_index < 9; z_index++) {
                    int k8 = 128 + ((z_index * 32) + 15);
                    int l8 = 600 + (k8 * 3);
                    int sin = Canvas3D.sin[k8];
                    z_array[z_index] = l8 * sin >> 16;
                }

                Landscape.setup_viewport(500, 800, 512, 334, z_array);
                mouse_recorder = new MouseRecorder();
                start_thread(mouse_recorder, 10);
            }

            return;
        } catch (Exception e) {
            Signlink.error("loaderror " + progress_caption + ' ' + progress_percent);
            error_loading = true;
            error_exception = e;
            e.printStackTrace();
        }
    }
}
