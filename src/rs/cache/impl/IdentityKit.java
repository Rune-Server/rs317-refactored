package rs.cache.impl;

import rs.cache.Archive;
import rs.io.Buffer;
import rs.scene.model.Model;

public class IdentityKit {

    public static int count;
    public static IdentityKit[] instance;

    public static void unpack(Archive a) {
        Buffer s = new Buffer(a.get("idk.dat"));
        IdentityKit.count = s.get_ushort();
        IdentityKit.instance = new IdentityKit[IdentityKit.count];

        for (int i = 0; i < IdentityKit.count; i++) {
            IdentityKit.instance[i] = new IdentityKit(s);
        }
    }

    public int anInt657;
    public short[] dialog_model_index = {-1, -1, -1, -1, -1};
    public short[] model_index;
    public int new_color[];
    public int old_color[];
    public boolean unselectable;

    public IdentityKit() {
        this.defaults();
    }

    public IdentityKit(Buffer b) {
        this.defaults();
        do {
            int opcode = b.get_ubyte();
            if (opcode == 0) {
                return;
            }
            if (opcode == 1) {
                this.anInt657 = b.get_ubyte();
            } else if (opcode == 2) {
                this.model_index = new short[b.get_ubyte()];
                for (int k = 0; k < this.model_index.length; k++) {
                    this.model_index[k] = (short) b.get_ushort();
                }
            } else if (opcode == 3) {
                this.unselectable = true;
            } else if (opcode >= 40 && opcode < 50) {
                this.old_color[opcode - 40] = b.get_ushort();
            } else if (opcode >= 50 && opcode < 60) {
                this.new_color[opcode - 50] = b.get_ushort();
            } else if (opcode >= 60 && opcode < 70) {
                this.dialog_model_index[opcode - 60] = (short) b.get_ushort();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }

    public void defaults() {
        anInt657 = -1;
        old_color = new int[6];
        new_color = new int[6];
        unselectable = false;
    }

    public Model get_dialog_model() {
        Model models[] = new Model[5];

        int count = 0;
        for (int i : this.dialog_model_index) {
            if (i != -1) {
                models[count++] = Model.get(i);
            }
        }

        Model m = new Model(count, models);
        for (int i = 0; i < 6; i++) {
            if (old_color[i] == 0) {
                break;
            }
            m.set_color(old_color[i], new_color[i]);
        }

        return m;
    }

    public Model get_mesh() {
        if (model_index == null) {
            return null;
        }

        Model models[] = new Model[model_index.length];

        for (int i = 0; i < model_index.length; i++) {
            models[i] = Model.get(model_index[i]);
        }

        Model m;

        if (models.length == 1) {
            m = models[0];
        } else {
            m = new Model(models.length, models);
        }

        for (int i = 0; i < 6; i++) {
            if (old_color[i] == 0) {
                break;
            }
            m.set_color(old_color[i], new_color[i]);
        }

        return m;
    }

    public boolean is_dialog_model_valid() {
        boolean valid = true;
        for (int i = 0; i < 5; i++) {
            if (dialog_model_index[i] != -1 && !Model.is_valid(dialog_model_index[i])) {
                valid = false;
            }
        }
        return valid;
    }

    public boolean is_model_valid() {
        if (model_index == null) {
            return true;
        }
        for (int i : this.model_index) {
            if (!Model.is_valid(i)) {
                return false;
            }
        }
        return true;
    }
}
