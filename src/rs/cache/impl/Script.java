package rs.cache.impl;

import rs.Game;
import rs.cache.model.ObjConfig;
import rs.io.Buffer;
import rs.model.Skill;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Script {

    public static final int OPTYPE_ADD = 0;
    public static final int OPTYPE_DIV = 2;
    public static final int OPTYPE_MUL = 3;
    public static final int OPTYPE_SUB = 1;
    public static final String[] TRIGGER_TYPE = {"==", "==", "<", ">", "!="};

    public byte compare_type = 0;
    public int compare_value = -2;
    public final byte index;
    public final int[] intcode;
    public Widget widget;

    public Script(Widget widget, Buffer b, int index) {
        this.widget = widget;
        this.index = (byte) index;
        this.intcode = new int[b.get_ushort()];

        for (int i = 0; i < this.intcode.length; i++) {
            this.intcode[i] = b.get_ushort();
        }
    }

    public int execute() {
        try {
            int value = 0;
            int type = 0;
            int i = 0;
            int[] code = this.intcode;

            do {
                int opcode = code[i++];
                int register = 0;
                byte optype = 0;

                switch (opcode) {
                    case 0: {
                        return value;
                    }

                    case 1: {
                        register = Game.skill_level[code[i++]];
                        break;
                    }

                    case 2: {
                        register = Game.skill_real_level[code[i++]];
                        break;
                    }

                    case 3: {
                        register = Game.skill_experience[code[i++]];
                        break;
                    }

                    case 4: {
                        Widget w = Widget.instance[code[i++]];
                        int item_index = code[i++];

                        if (item_index >= 0 && item_index < ObjConfig.count && (!ObjConfig.get(item_index).is_members || Game.is_members)) {
                            for (int j = 0; j < w.item_index.length; j++) {
                                if (w.item_index[j] == item_index + 1) {
                                    register += w.item_count[j];
                                }
                            }
                        }
                        break;
                    }

                    case 5: {
                        register = Game.settings[code[i++]];
                        break;
                    }

                    case 6: {
                        register = Game.XP_TABLE[Game.skill_real_level[code[i++]] - 1];
                        break;
                    }

                    case 7: {
                        register = (Game.settings[code[i++]] * 100) / 46875;
                        break;
                    }

                    case 8: {
                        register = Game.self.combat_level;
                        break;
                    }

                    case 9: {
                        for (int j = 0; j < Skill.COUNT; j++) {
                            if (Skill.ENABLED[j]) {
                                register += Game.skill_real_level[j];
                            }
                        }
                        break;
                    }

                    // XXX: register = 999,999,999 if inventory doesn't contain item
                    // Which means that by default it would return 0 if it does.
                    case 10: {
                        Widget child = Widget.get(code[i++]);
                        if (child != null) {
                            int item_index = code[i++] + 1;
                            if (item_index >= 0 && item_index < ObjConfig.count && (!ObjConfig.get(item_index).is_members || Game.is_members)) {
                                for (int slot = 0; slot < child.item_index.length; slot++) {
                                    if (child.item_index[slot] != item_index) {
                                        continue;
                                    }
                                    register = 999999999;
                                    break;
                                }
                            }
                        }
                        break;
                    }

                    case 11:
                        register = Game.energy_left;
                        break;
                    case 12:
                        register = Game.weight_carried;
                        break;
                    case 13:
                        int setting = Game.settings[code[i++]];
                        int shift = code[i++];
                        register = (setting & 1 << shift) == 0 ? 0 : 1;
                        break;
                    case 14:
                        VarBit varbit = VarBit.instance[code[i++]];
                        int offset = varbit.offset;
                        int lsb = Game.LSB_BIT_MASK[varbit.shift - offset];
                        register = (Game.settings[varbit.setting] >> offset) & lsb;
                        break;
                    case 15:
                        optype = OPTYPE_SUB;
                        break;
                    case 16:
                        optype = OPTYPE_DIV;
                        break;
                    case 17:
                        optype = OPTYPE_MUL;
                        break;
                    case 18:
                        register = Game.self.get_tile_x();
                        break;
                    case 19:
                        register = Game.self.get_tile_y();
                        break;
                    case 20:
                        register = code[i++];
                        break;
                }

                if (optype == OPTYPE_ADD) {
                    switch (type) {
                        case OPTYPE_ADD: {
                            value += register;
                            break;
                        }

                        case OPTYPE_SUB: {
                            value -= register;
                            break;
                        }

                        case OPTYPE_DIV: {
                            if (register != 0) {
                                value /= register;
                            }
                            break;
                        }

                        case OPTYPE_MUL: {
                            value *= register;
                            break;
                        }
                    }
                    type = 0;
                } else {
                    type = optype;
                }
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void export(File f) {
        f.getParentFile().mkdirs();

        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(f));

            if (this.compare_type != -1) {
                out.write("ENABLED: VALUE ");
                out.write(TRIGGER_TYPE[this.compare_type]);
                out.write(" ");
                out.write(String.valueOf(this.compare_value));
                out.newLine();
            }

            int[] code = this.intcode;

            for (int i = 0; i < this.intcode.length; i++) {
                switch (code[i]) {
                    case 0:
                        out.write("RETURN VALUE");
                        break;
                    case 1:
                        out.write("REGISTER = SKILL_LEVEL[" + code[++i] + "]\n");
                        break;
                    case 2:
                        out.write("REGISTER = REAL_SKILL_LEVEL[" + code[++i] + "]\n");
                        break;
                    case 3:
                        out.write("REGISTER = SKILL_EXPERIENCE[" + code[++i] + "]\n");
                        break;
                    case 4:
                        int widget = code[++i];
                        int item = code[++i];
                        out.write("REGISTER += ITEM_COUNT[" + item + "] OF WIDGET[" + widget + "]\n");
                        break;
                    case 5:
                        out.write("REGISTER = GAME_SETTING[" + code[++i] + "]\n");
                        break;
                    case 6:
                        out.write("REGISTER = XP_TABLE[" + code[++i] + "]\n");
                        break;
                    case 7:
                        out.write("REGISTER = (GAME_SETTING[" + code[++i] + "] * 100) / 46875\n");
                        break;
                    case 8:
                        out.write("REGISTER = LOCAL_PLAYER.COMBAT_LEVEL\n");
                        break;
                    case 9:
                        out.write("REGISTER = TOTAL_LEVEL\n");
                        break;
                    case 10:
                        widget = code[++i];
                        item = code[++i];
                        out.write("REGISTER = 999999999 IF WIDGET[" + widget + "] HAS ITEM[" + item + "]\n");
                        break;
                    case 11:
                        out.write("REGISTER = ENERGY_LEFT\n");
                        break;
                    case 12:
                        out.write("REGISTER = CARRIED_WEIGHT\n");
                        break;
                    case 13:
                        int setting = code[++i];
                        int shift = code[++i];
                        out.write("REGISTER = GAME_SETTING[" + setting + "] BIT " + shift + " == 0 ? 0 : 1\n");
                        break;
                    case 14:
                        int varbit = code[++i];
                        VarBit v = VarBit.instance[varbit];
                        out.write("V = VARBIT[" + varbit + "]\n");
                        out.write("REGISTER = GAME_SETTINGS[");
                        out.write(String.valueOf(v.setting));
                        out.write("].BITS[");
                        out.write(String.valueOf(v.offset));
                        out.write("->");
                        out.write(String.valueOf(v.shift - v.offset));
                        out.write("]\n");
                        break;
                    case 15:
                        out.write("OP_TYPE = -\n");
                        break;
                    case 16:
                        out.write("OP_TYPE = /\n");
                        break;
                    case 17:
                        out.write("OP_TYPE = *\n");
                        break;
                    case 18:
                        out.write("REGISTER = LOCAL_PLAYER.TILE_X\n");
                        break;
                    case 19:
                        out.write("REGISTER = LOCAL_PLAYER.TILE_Y\n");
                        break;
                    case 20:
                        out.write("REGISTER = " + code[++i] + "\n");
                        break;
                    default: {
                        out.write("UNKNOWN CODE: " + code[i] + "\n");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
