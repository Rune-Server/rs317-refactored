package rs.scene.model;

import rs.cache.model.SpotAnimConfig;
import rs.node.impl.Renderable;

public class Projectile extends Renderable {

    public double aDouble1578;
    public int anInt158;
    public int arc_size;
    public int cycle_end;
    public int cycle_start;
    public boolean mobile;
    public int offset_z;
    public int parent_size;
    public int pitch;
    public int plane;
    public int rotation;
    public double scene_x;
    public double scene_y;
    public double scene_z;
    public int seq_cycle;
    public int seq_frame;
    public double speed;
    public double speed_x;
    public double speed_y;
    public double speed_z;
    public SpotAnimConfig config;
    public int start_x;
    public int start_y;
    public int start_z;
    public int target_index;

    public Projectile(int arc_size, int end_z, int cycle_start, int cycle_end, int parent_size, int plane, int start_z, int start_y, int start_x, int target, int spotanim_index) {
        this.mobile = false;
        this.config = SpotAnimConfig.instance[spotanim_index];
        this.plane = plane;
        this.start_x = start_x;
        this.start_y = start_y;
        this.start_z = start_z;
        this.cycle_start = cycle_start;
        this.cycle_end = cycle_end;
        this.arc_size = arc_size;
        this.parent_size = parent_size;
        this.target_index = target;
        this.offset_z = end_z;
        this.mobile = false;
    }

    @Override
    public Model get_model() {
        Model model = config.get_model();

        if (model == null) {
            return null;
        }

        int frame = -1;

        if (config.seq != null) {
            frame = config.seq.frame_primary[seq_frame];
        }

        Model m = new Model(true, frame == -1, false, model);

        if (frame != -1) {
            m.apply_vertex_weights();
            m.apply_sequence_frame(frame);
            m.triangle_groups = null;
            m.vertex_weights = null;
        }

        if (config.scale != 128 || config.height != 128) {
            m.scale(config.scale, config.height, config.scale);
        }

        m.set_pitch(pitch);
        m.apply_lighting(64 + config.brightness, 850 + config.specular, -30, -50, -30, true);
        return m;
    }

    public void update(int cycle) {
        mobile = true;
        scene_x += speed_x * (double) cycle;
        scene_y += speed_y * (double) cycle;
        scene_z += speed_z * (double) cycle + 0.5D * aDouble1578 * (double) cycle * (double) cycle;
        speed_z += aDouble1578 * (double) cycle;
        rotation = (int) (Math.atan2(speed_x, speed_y) * (32595 / 100)) + 1024 & 0x7ff;
        pitch = (int) (Math.atan2(speed_z, speed) * (32595 / 100)) & 0x7ff;

        if (config.seq != null) {
            for (seq_cycle += cycle; seq_cycle > config.seq.get_frame_length(seq_frame); ) {
                seq_cycle -= config.seq.get_frame_length(seq_frame) + 1;
                seq_frame++;
                if (seq_frame >= config.seq.frame_count) {
                    seq_frame = 0;
                }
            }
        }
    }

    public void update(int cycle, int x, int y, int z) {
        if (!mobile) {
            double dx = x - start_x;
            double dy = y - start_y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            scene_x = (double) start_x + (dx * (double) parent_size) / distance;
            scene_y = (double) start_y + (dy * (double) parent_size) / distance;
            scene_z = start_z;
        }

        double dt = (this.cycle_end + 1) - cycle;
        speed_x = ((double) x - scene_x) / dt;
        speed_y = ((double) y - scene_y) / dt;
        speed = Math.sqrt(speed_x * speed_x + speed_y * speed_y);

        if (!mobile) {
            speed_z = -speed * Math.tan((double) arc_size * (Math.PI / 128D));
        }

        aDouble1578 = (2D * ((double) z - scene_z - speed_z * dt)) / (dt * dt);
    }
}
