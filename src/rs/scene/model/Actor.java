package rs.scene.model;

import rs.cache.impl.Sequence;
import rs.cache.model.ActorConfig;
import rs.cache.model.SpotAnimConfig;
import rs.node.impl.Renderable;

public class Actor extends Entity {

    public ActorConfig config;

    public Actor() {
    }

    public Model get_built_model() {
        int frame1 = -1;

        if (super.seq_index >= 0 && super.seq_delay_cycle == 0) {
            frame1 = Sequence.instance[super.seq_index].frame_primary[super.seq_frame];
            int frame2 = -1;

            if (super.move_seq_index >= 0 && super.move_seq_index != super.stand_animation) {
                frame2 = Sequence.instance[super.move_seq_index].frame_primary[super.move_seq_frame];
            }

            return config.get_model(Sequence.instance[super.seq_index].vertices, frame1, frame2);
        }

        if (super.move_seq_index >= 0) {
            frame1 = Sequence.instance[super.move_seq_index].frame_primary[super.move_seq_frame];
        }

        return config.get_model(null, frame1, -1);
    }

    @Override
    public Model get_model() {
        if (config == null) {
            return null;
        }

        Model model = get_built_model();

        if (model == null) {
            return null;
        }

        super.height = ((Renderable) (model)).height;

        if (super.spotanim_index != -1 && super.spotanim_frame != -1) {
            SpotAnimConfig c = SpotAnimConfig.instance[super.spotanim_index];
            Model m = c.get_model();

            if (m != null) {
                int anim = c.seq.frame_primary[super.spotanim_frame];

                Model m1 = new Model(true, anim == -1, false, m);
                m1.translate(0, -super.graphic_offset_y, 0);
                m1.apply_vertex_weights();
                m1.apply_sequence_frame(anim);
                m1.triangle_groups = null;
                m1.vertex_weights = null;

                if (c.scale != 128 || c.height != 128) {
                    m1.scale(c.scale, c.height, c.scale);
                }

                m1.apply_lighting(64 + c.brightness, 850 + c.specular, -30, -50, -30, true);
                model = new Model(2, true, new Model[]{model, m1});
            }

        }

        if (config.has_options == 1) {
            model.is_clickable = true;
        }
        return model;
    }

    @Override
    public boolean is_visible() {
        return config != null;
    }
}
