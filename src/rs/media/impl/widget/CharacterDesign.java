package rs.media.impl.widget;

import rs.Game;
import rs.cache.impl.IdentityKit;
import rs.cache.impl.Sequence;
import rs.cache.impl.Widget;
import rs.media.Sprite;
import rs.scene.model.Model;

public class CharacterDesign {

    public static Sprite button_disabled;
    public static Sprite button_enabled;

    public static boolean male = true;
    public static boolean update;

    public static int[] selected_colors = new int[5];
    public static int[] selected_identity_kits = new int[7];

    /* @formatter:off */
	public static final int DESIGN_COLOR[][] = {
		// Hair
		{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983, 54193 },
		// Torso
		{ 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003, 25239 },
		// Legs
		{ 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621, 4783, 1341, 16578, 35003 },
		// Feet
		{ 4626, 11146, 6439, 12, 4758, 10270 },
		// Skin
		{ 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574 }
	};
	
	public static final int TORSO_COLORS[] = {
		9104, 10275, 7595, 3610, 7975, 8526, 918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486
	};
	/* @formatter:on */

    public static boolean handle(Widget widget, int type) {
        if (type >= 314 && type <= 323) {
            int part = (type - 314) / 2;
            int option = type & 1;
            int current = selected_colors[part];

            if (option == 0 && --current < 0) {
                current = DESIGN_COLOR[part].length - 1;
            }

            if (option == 1 && ++current >= DESIGN_COLOR[part].length) {
                current = 0;
            }

            selected_colors[part] = current;
            update = true;
            return true;
        }

        if (type == 324 && !male) {
            male = true;
            reset();
            return true;
        }

        if (type == 325 && male) {
            male = false;
            reset();
            return true;
        }

        if (type == 326) {
            Game.out.put_opcode(101);
            Game.out.put_byte(male ? 0 : 1);

            for (int i = 0; i < 7; i++) {
                Game.out.put_byte(selected_identity_kits[i]);
            }

            for (int i = 0; i < 5; i++) {
                Game.out.put_byte(selected_colors[i]);
            }
            return true;
        }

        if (type >= 300 && type <= 313) {
            int part = (type - 300) / 2;
            boolean next = (type & 1) == 1;
            int identity_kit = selected_identity_kits[part];

            if (identity_kit != -1) {
                do {
                    if (!next && --identity_kit < 0) {
                        identity_kit = IdentityKit.count - 1;
                    }
                    if (next && ++identity_kit >= IdentityKit.count) {
                        identity_kit = 0;
                    }
                }
                while (IdentityKit.instance[identity_kit].unselectable || IdentityKit.instance[identity_kit].anInt657 != part + (male ? 0 : 7));

                selected_identity_kits[part] = identity_kit;
                update = true;
            }

            return true;
        }

        return false;
    }

    public static void reset() {
        update = true;
        for (int part = 0; part < 7; part++) {
            selected_identity_kits[part] = -1;
            for (int identity_kit = 0; identity_kit < IdentityKit.count; identity_kit++) {
                if (IdentityKit.instance[identity_kit].unselectable || IdentityKit.instance[identity_kit].anInt657 != part + (male ? 0 : 7)) {
                    continue;
                }
                selected_identity_kits[part] = identity_kit;
                break;
            }
        }
    }

    public static boolean update(Widget widget, int type) {
        if (type == 327) {
            widget.model_zoom = 600;
            widget.model_pitch = 150;
            widget.model_yaw = (int) (Math.sin((double) Game.loop_cycle / 48D) * 256D) & 0x07FF;

            if (update) {
                for (int i = 0; i < 7; i++) {
                    int index = selected_identity_kits[i];
                    if (index >= 0 && !IdentityKit.instance[index].is_model_valid()) {
                        return true;
                    }
                }

                update = false;
                Model meshes[] = new Model[7];
                int count = 0;
                for (int i = 0; i < 7; i++) {
                    int identity_kit = selected_identity_kits[i];
                    if (identity_kit >= 0) {
                        meshes[count++] = IdentityKit.instance[identity_kit].get_mesh();
                    }
                }

                Model mesh = new Model(count, meshes);
                for (int i = 0; i < 5; i++) {
                    if (selected_colors[i] != 0) {
                        mesh.set_color(DESIGN_COLOR[i][0], DESIGN_COLOR[i][selected_colors[i]]);
                        if (i == 1) {
                            mesh.set_color(TORSO_COLORS[0], TORSO_COLORS[selected_colors[i]]);
                        }
                    }
                }

                mesh.apply_vertex_weights();
                mesh.apply_sequence_frame(Sequence.instance[Game.self.stand_animation].frame_primary[0]);
                mesh.apply_lighting(64, 850, -30, -50, -30, true);
                widget.model_type_disabled = 5;
                widget.model_index_disabled = 0;
                Widget.set_mesh(5, 0, mesh);
            }
            return true;
        }

        if (type == 324) {
            if (button_disabled == null) {
                button_disabled = widget.image_disabled;
                button_enabled = widget.image_enabled;
            }
            if (male) {
                widget.image_disabled = button_enabled;
            } else {
                widget.image_disabled = button_disabled;
            }
            return true;
        }

        if (type == 325) {
            if (button_disabled == null) {
                button_disabled = widget.image_disabled;
                button_enabled = widget.image_enabled;
            }
            if (male) {
                widget.image_disabled = button_disabled;
            } else {
                widget.image_disabled = button_enabled;
            }
            return true;
        }
        return false;
    }

}
