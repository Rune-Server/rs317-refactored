package rs.scene.model;

import rs.Game;
import rs.cache.impl.Sequence;
import rs.node.impl.Renderable;

public class Entity extends Renderable {

    public static final byte[] DIRECTION_DELTA_X = {-1, 0, 1, -1, 1, -1, 0, 1};
    public static final byte[] DIRECTION_DELTA_Y = {1, 1, 1, 0, 0, -1, -1, -1};

    public int seq_cycle;
    public int seq_frame;
    public int seq_index;
    public int seq_reset_cycle;
    public int seq_delay_cycle;
    public boolean can_rotate;
    public int combat_cycle;
    public int current_health;
    public int dest_rotation;
    public int face_entity;
    public int face_x;
    public int face_y;
    public int spotanim_cycle;
    public int spotanim_cycle_end;
    public int spotanim_frame;
    public int spotanim_index;
    public int graphic_offset_y;
    public int height;
    public int hit_cycle[];
    public int hit_damage[];
    public int hit_type[];
    public int max_health;
    public int move_seq_cycle;
    public int move_seq_frame;
    public int move_seq_index;
    public int move_cycle_end;
    public int move_cycle_start;
    public int move_direction;
    public int move_end_x;
    public int move_end_y;
    public int move_start_x;
    public int move_start_y;
    public int path_position;
    public boolean path_run[];
    public int path_x[];
    public int path_y[];
    public int resync_walk_cycle;
    public int rotation;
    public int run_animation;
    public int scene_x;
    public int scene_y;
    public int size;
    public int spoken_color;
    public int spoken_effect;
    public int spoken_life;
    public String spoken_message;
    public int stand_animation;
    public int stand_turn_animation;
    public int still_path_position;
    public int turn_180_animation;
    public int turn_l_animation;
    public int turn_r_animation;
    public int turn_speed;
    public int update_cycle;
    public int walk_animation;

    public Entity() {
        this.path_x = new int[10];
        this.path_y = new int[10];
        this.face_entity = -1;
        this.turn_speed = 32;
        this.run_animation = -1;
        this.height = 200;
        this.stand_animation = -1;
        this.stand_turn_animation = -1;
        this.hit_damage = new int[4];
        this.hit_type = new int[4];
        this.hit_cycle = new int[4];
        this.move_seq_index = -1;
        this.spotanim_index = -1;
        this.seq_index = -1;
        this.combat_cycle = -1000;
        this.spoken_life = 100;
        this.size = 1;
        this.can_rotate = false;
        this.path_run = new boolean[10];
        this.walk_animation = -1;
        this.turn_180_animation = -1;
        this.turn_r_animation = -1;
        this.turn_l_animation = -1;
    }

    public int get_local_x() {
        return this.scene_x >> 7;
    }

    public int get_local_y() {
        return this.scene_y >> 7;
    }

    public int get_tile_x() {
        return Game.map_base_x + (this.scene_x >> 7);
    }

    public int get_tile_y() {
        return Game.map_base_y + (this.scene_y >> 7);
    }

    public void hit(int type, int damage, int tick) {
        for (int i = 0; i < 4; i++) {
            if (hit_cycle[i] <= tick) {
                hit_damage[i] = damage;
                hit_type[i] = type;
                hit_cycle[i] = tick + 70;
                return;
            }
        }
    }

    public boolean is_visible() {
        return false;
    }

    public void move(int direction, boolean running) {
        int x = path_x[0];
        int y = path_y[0];

        x += DIRECTION_DELTA_X[direction];
        y += DIRECTION_DELTA_Y[direction];

        if (seq_index != -1 && Sequence.instance[seq_index].walk_flag == 1) {
            seq_index = -1;
        }

        if (path_position < 9) {
            path_position++;
        }

        for (int l = path_position; l > 0; l--) {
            path_x[l] = path_x[l - 1];
            path_y[l] = path_y[l - 1];
            path_run[l] = path_run[l - 1];
        }

        path_x[0] = x;
        path_y[0] = y;
        path_run[0] = running;
    }

    public void move_to(int x, int y, boolean discard_walk_queue) {
        if (seq_index != -1 && Sequence.instance[seq_index].walk_flag == 1) {
            seq_index = -1;
        }

        if (!discard_walk_queue) {
            int d_x = x - path_x[0];
            int d_y = y - path_y[0];

            if (d_x >= -8 && d_x <= 8 && d_y >= -8 && d_y <= 8) {
                if (path_position < 9) {
                    path_position++;
                }

                for (int i = path_position; i > 0; i--) {
                    path_x[i] = path_x[i - 1];
                    path_y[i] = path_y[i - 1];
                    path_run[i] = path_run[i - 1];
                }

                path_x[0] = x;
                path_y[0] = y;
                path_run[0] = false;
                return;
            }
        }

        path_position = 0;
        still_path_position = 0;
        resync_walk_cycle = 0;
        path_x[0] = x;
        path_y[0] = y;
        scene_x = path_x[0] * 128 + size * 64;
        scene_y = path_y[0] * 128 + size * 64;
    }

    public void reset_positions() {
        path_position = 0;
        still_path_position = 0;
    }
}
