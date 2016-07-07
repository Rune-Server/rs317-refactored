package rs.scene.model;

import rs.Game;
import rs.cache.impl.IdentityKit;
import rs.cache.impl.Sequence;
import rs.cache.model.ActorConfig;
import rs.cache.model.ObjConfig;
import rs.cache.model.SpotAnimConfig;
import rs.io.Buffer;
import rs.media.impl.widget.CharacterDesign;
import rs.node.List;
import rs.node.impl.Renderable;
import rs.util.JString;
import rs.util.RSColor;

public class Player extends Entity {

    public static List model_cache = new List(260);

    public ActorConfig actor_override;
    public int[] colors;
    public short combat_level;
    public short[] equipment_indices;
    public byte gender;
    public byte headicon_flag;
    public int loc_end_cycle;
    public int loc_x1;
    public int loc_y1;
    public Model loc_model;
    public int loc_start_cycle;
    public int loc_x0;
    public int loc_y0;
    public int loc_x;
    public int loc_y;
    public int loc_z;
    public boolean low_lod;
    public long model_uid;
    public String name;
    public int scene_z;
    public int skill_level;
    public int team;
    public long uid;
    public boolean visible;

    public Player() {
        uid = -1L;
        low_lod = false;
        colors = new int[5];
        visible = false;
        equipment_indices = new short[12];
    }

    public Model get_built_model() {
        if (actor_override != null) {
            int frame = -1;

            if (super.seq_index >= 0 && super.seq_delay_cycle == 0) {
                frame = Sequence.instance[super.seq_index].frame_primary[super.seq_frame];
            } else if (super.move_seq_index >= 0) {
                frame = Sequence.instance[super.move_seq_index].frame_primary[super.move_seq_frame];
            }

            return actor_override.get_model(null, frame, -1);
        }

        long model_uid = this.model_uid;
        int frame1 = -1;
        int frame2 = -1;
        int shield_override = -1;
        int weapon_override = -1;

        if (super.seq_index >= 0 && super.seq_delay_cycle == 0) {
            Sequence a = Sequence.instance[super.seq_index];
            frame1 = a.frame_primary[super.seq_frame];

            if (super.move_seq_index >= 0 && super.move_seq_index != super.stand_animation) {
                frame2 = Sequence.instance[super.move_seq_index].frame_primary[super.move_seq_frame];
            }

            if (a.override_shield_index >= 0) {
                shield_override = a.override_shield_index;
                model_uid += shield_override - equipment_indices[5] << 40;
            }

            if (a.override_weapon_index >= 0) {
                weapon_override = a.override_weapon_index;
                model_uid += weapon_override - equipment_indices[3] << 48;
            }
        } else if (super.move_seq_index >= 0) {
            frame1 = Sequence.instance[super.move_seq_index].frame_primary[super.move_seq_frame];
        }

        Model model = (Model) model_cache.get(model_uid);

        if (model == null) {
            boolean use_cached = false;

            for (int i = 0; i < 12; i++) {
                int equip = equipment_indices[i];

                if (weapon_override >= 0 && i == 3) {
                    equip = weapon_override;
                }

                if (shield_override >= 0 && i == 5) {
                    equip = shield_override;
                }

                if (equip >= 256 && equip < 512 && !IdentityKit.instance[equip - 256].is_model_valid()) {
                    use_cached = true;
                }

                if (equip >= 512 && !ObjConfig.get(equip - 512).is_worn_mesh_valid(gender)) {
                    use_cached = true;
                }
            }

            if (use_cached) {
                if (uid != -1L) {
                    model = (Model) model_cache.get(uid);
                }

                if (model == null) {
                    return null;
                }
            }
        }

        if (model == null) {
            Model equip_models[] = new Model[12];
            int count = 0;

            for (int i = 0; i < 12; i++) {
                int index = equipment_indices[i];

                if (weapon_override >= 0 && i == 3) {
                    index = weapon_override;
                }

                if (shield_override >= 0 && i == 5) {
                    index = shield_override;
                }

                if (index >= 256 && index < 512) {
                    Model id_model = IdentityKit.instance[index - 256].get_mesh();
                    if (id_model != null) {
                        equip_models[count++] = id_model;
                    }
                }

                if (index >= 512) {
                    Model equip_model = ObjConfig.get(index - 512).get_worn_mesh(gender);
                    if (equip_model != null) {
                        equip_models[count++] = equip_model;
                    }
                }
            }

            model = new Model(count, equip_models);
            for (int i = 0; i < 5; i++) {
                if (colors[i] != 0) {
                    model.set_color(CharacterDesign.DESIGN_COLOR[i][0], CharacterDesign.DESIGN_COLOR[i][colors[i]]);
                    if (i == 1) {
                        model.set_color(CharacterDesign.TORSO_COLORS[0], CharacterDesign.TORSO_COLORS[colors[i]]);
                    }
                }
            }

            model.apply_vertex_weights();
            model.apply_lighting(64, 850, -30, -50, -30, true);
            model_cache.insert(model, model_uid);
            uid = model_uid;
        }

        // Doesn't apply animations.
        if (low_lod) {
            return model;
        }

        Model m = Model.temporary;
        m.replace(model, (frame1 == -1) & (frame2 == -1));

        if (frame1 != -1 && frame2 != -1) {
            m.apply_sequence_frames(Sequence.instance[super.seq_index].vertices, frame1, frame2);
        } else if (frame1 != -1) {
            m.apply_sequence_frame(frame1);
        }

        m.method466();
        m.triangle_groups = null;
        m.vertex_weights = null;
        return m;
    }

    public Model get_dialog_model() {
        if (!visible) {
            return null;
        }

        if (actor_override != null) {
            return actor_override.get_dialog_model();
        }

        boolean flag = false;

        for (int i = 0; i < 12; i++) {
            int index = equipment_indices[i];

            if (index >= 256 && index < 512 && !IdentityKit.instance[index - 256].is_dialog_model_valid()) {
                flag = true;
            }

            if (index >= 512 && !ObjConfig.get(index - 512).is_dialogue_model_valid(gender)) {
                flag = true;
            }
        }

        if (flag) {
            return null;
        }

        Model models[] = new Model[12];
        int count = 0;

        for (int i = 0; i < 12; i++) {
            int index = equipment_indices[i];

            if (index >= 256 && index < 512) {
                Model m = IdentityKit.instance[index - 256].get_dialog_model();

                if (m != null) {
                    models[count++] = m;
                }
            }

            if (index >= 512) {
                Model m = ObjConfig.get(index - 512).get_dialogue_model(gender);

                if (m != null) {
                    models[count++] = m;
                }
            }
        }

        Model m = new Model(count, models);
        for (int i = 0; i < 5; i++) {
            if (colors[i] != 0) {
                m.set_color(CharacterDesign.DESIGN_COLOR[i][0], CharacterDesign.DESIGN_COLOR[i][colors[i]]);
                if (i == 1) {
                    m.set_color(CharacterDesign.TORSO_COLORS[0], CharacterDesign.TORSO_COLORS[colors[i]]);
                }
            }
        }
        return m;
    }

    @Override
    public Model get_model() {
        if (!visible) {
            return null;
        }

        Model built = this.get_built_model();

        if (built == null) {
            return null;
        }

        super.height = ((Renderable) (built)).height;

        built.is_clickable = true;

        if (low_lod) {
            return built;
        }

        if (super.spotanim_index != -1 && super.spotanim_frame != -1) {
            SpotAnimConfig effect = SpotAnimConfig.instance[super.spotanim_index];
            Model sa_model = effect.get_model();

            if (sa_model != null) {
                Model m = new Model(true, super.spotanim_frame == -1, false, sa_model);
                m.translate(0, -super.graphic_offset_y, 0);
                m.apply_vertex_weights();
                m.apply_sequence_frame(effect.seq.frame_primary[super.spotanim_frame]);
                m.triangle_groups = null;
                m.vertex_weights = null;

                if (effect.scale != 128 || effect.height != 128) {
                    m.scale(effect.scale, effect.height, effect.scale);
                }

                m.apply_lighting(64 + effect.brightness, 850 + effect.specular, -30, -50, -30, true);
                built = new Model(2, true, new Model[]{built, m});
            }
        }

        if (this.loc_model != null) {
            if (Game.loop_cycle >= loc_end_cycle) {
                this.loc_model = null;
            }
            if (Game.loop_cycle >= loc_start_cycle && Game.loop_cycle < loc_end_cycle) {
                Model m = this.loc_model;
                m.translate(loc_x - super.scene_x, loc_z - scene_z, loc_y - super.scene_y);

                if (super.dest_rotation == 512) {
                    m.rotate_cw();
                    m.rotate_cw();
                    m.rotate_cw();
                } else if (super.dest_rotation == 1024) {
                    m.rotate_cw();
                    m.rotate_cw();
                } else if (super.dest_rotation == 1536) {
                    m.rotate_cw();
                }

                built = new Model(2, true, new Model[]{built, m});

                if (super.dest_rotation == 512) {
                    m.rotate_cw();
                } else if (super.dest_rotation == 1024) {
                    m.rotate_cw();
                    m.rotate_cw();
                } else if (super.dest_rotation == 1536) {
                    m.rotate_cw();
                    m.rotate_cw();
                    m.rotate_cw();
                }

                m.translate(super.scene_x - loc_x, scene_z - loc_z, super.scene_y - loc_y);
            }
        }

        built.is_clickable = true;
        return built;
    }

    @Override
    public boolean is_visible() {
        return visible;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);

        if (this.skill_level == 0) {
            sb.append(RSColor.get_level_tag(this.combat_level));
        } else {
            sb.append(" (skill-");
            sb.append(this.skill_level);
            sb.append(')');
        }

        return sb.toString();
    }

    public void update(Buffer s) {
        s.position = 0;

        this.gender = s.get_byte();
        this.headicon_flag = s.get_byte();

        this.actor_override = null;
        this.team = 0;

        for (int i = 0; i < 12; i++) {
            int lsb = s.get_ubyte();

            if (lsb == 0) {
                this.equipment_indices[i] = 0;
                continue;
            }

            this.equipment_indices[i] = (short) ((lsb << 8) + s.get_ubyte());

            if (i == 0 && this.equipment_indices[0] == 65535) {
                this.actor_override = ActorConfig.get(s.get_ushort());
                break;
            }

            if (this.equipment_indices[i] >= 512 && this.equipment_indices[i] - 512 < ObjConfig.count) {
                int team = ObjConfig.get(this.equipment_indices[i] - 512).team;

                if (team != 0) {
                    this.team = team;
                }
            }

        }

        for (int i = 0; i < 5; i++) {
            int j = s.get_ubyte();

            if (j < 0 || j >= CharacterDesign.DESIGN_COLOR[i].length) {
                j = 0;
            }

            this.colors[i] = j;
        }

        super.stand_animation = s.get_ushort();
        if (super.stand_animation == 65535) {
            super.stand_animation = -1;
        }

        super.stand_turn_animation = s.get_ushort();
        if (super.stand_turn_animation == 65535) {
            super.stand_turn_animation = -1;
        }

        super.walk_animation = s.get_ushort();
        if (super.walk_animation == 65535) {
            super.walk_animation = -1;
        }

        super.turn_180_animation = s.get_ushort();
        if (super.turn_180_animation == 65535) {
            super.turn_180_animation = -1;
        }

        super.turn_r_animation = s.get_ushort();
        if (super.turn_r_animation == 65535) {
            super.turn_r_animation = -1;
        }

        super.turn_l_animation = s.get_ushort();
        if (super.turn_l_animation == 65535) {
            super.turn_l_animation = -1;
        }

        super.run_animation = s.get_ushort();
        if (super.run_animation == 65535) {
            super.run_animation = -1;
        }

        this.name = JString.get_formatted_string(s.get_long());
        this.combat_level = (short) s.get_ubyte();
        this.skill_level = s.get_ushort();
        this.visible = true;
        this.model_uid = 0L;

        for (int i = 0; i < 12; i++) {
            this.model_uid <<= 4;
            if (equipment_indices[i] >= 256) {
                this.model_uid += equipment_indices[i] - 256;
            }
        }

        if (equipment_indices[0] >= 256) {
            this.model_uid += equipment_indices[0] - 256 >> 4;
        }

        if (equipment_indices[1] >= 256) {
            this.model_uid += equipment_indices[1] - 256 >> 8;
        }

        for (int i = 0; i < 5; i++) {
            this.model_uid <<= 3;
            this.model_uid += colors[i];
        }

        this.model_uid <<= 1;
        this.model_uid += gender;
    }

}
