package rs.scene.model;

import rs.Game;
import rs.cache.impl.Sequence;
import rs.cache.impl.VarBit;
import rs.cache.model.LocConfig;
import rs.node.impl.Renderable;

public class Loc extends Renderable {

    public Sequence seq;
    public int seq_cycle;
    public int cycle;
    public short index;
    public short[] override_index;
    public int rotation;
    public int setting_index;
    public int type;
    public int v_ne;
    public int v_nw;
    public int v_se;
    public int v_sw;
    public int varbit_index;

    public Loc(int index, int rotation, int type, int v_se, int v_ne, int v_sw, int v_nw, int sequence, boolean random_frame) {
        this.index = (short) index;
        this.type = type;
        this.rotation = rotation;
        this.v_sw = v_sw;
        this.v_se = v_se;
        this.v_ne = v_ne;
        this.v_nw = v_nw;

        if (sequence != -1) {
            this.seq = Sequence.instance[sequence];
            this.seq_cycle = 0;
            this.cycle = Game.loop_cycle;
            if (random_frame && this.seq.padding != -1) {
                this.seq_cycle = (int) (Math.random() * (double) this.seq.frame_count);
                this.cycle -= (int) (Math.random() * (double) this.seq.get_frame_length(seq_cycle));
            }
        }

        LocConfig config = LocConfig.get(this.index);
        this.varbit_index = config.varbit_index;
        this.setting_index = config.setting_index;
        this.override_index = config.override_index;
    }

    @Override
    public Model get_model() {
        int frame = -1;

        if (seq != null) {
            int c_d = Game.loop_cycle - cycle;

            if (c_d > 100 && seq.padding > 0) {
                c_d = 100;
            }

            while (c_d > seq.get_frame_length(seq_cycle)) {
                c_d -= seq.get_frame_length(seq_cycle);

                seq_cycle++;

                if (seq_cycle < seq.frame_count) {
                    continue;
                }

                seq_cycle -= seq.padding;

                if (seq_cycle >= 0 && seq_cycle < seq.frame_count) {
                    continue;
                }

                seq = null;
                break;
            }

            cycle = Game.loop_cycle - c_d;

            if (seq != null) {
                frame = seq.frame_primary[seq_cycle];
            }
        }

        LocConfig config;

        if (override_index != null) {
            config = get_overriding_config();
        } else {
            config = LocConfig.get(index);
        }

        if (config == null) {
            return null;
        } else {
            return config.get_model(type, rotation, v_sw, v_se, v_ne, v_nw, frame);
        }
    }

    public LocConfig get_overriding_config() {
        int i = -1;

        if (varbit_index != -1) {
            VarBit varbit = VarBit.instance[varbit_index];
            int varp_index = varbit.setting;
            int offset = varbit.offset;
            int shift = varbit.shift;
            int j1 = Game.LSB_BIT_MASK[shift - offset];
            i = Game.settings[varp_index] >> offset & j1;
        } else if (setting_index != -1) {
            i = Game.settings[setting_index];
        }

        if (i < 0 || i >= override_index.length || override_index[i] == -1) {
            return null;
        }

        return LocConfig.get(override_index[i]);
    }
}
