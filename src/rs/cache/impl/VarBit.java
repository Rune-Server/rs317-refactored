package rs.cache.impl;

import rs.cache.Archive;
import rs.io.Buffer;

public class VarBit {

    public static int count;
    public static VarBit[] instance;

    public static void unpack(Archive a) {
        Buffer s = new Buffer(a.get("varbit.dat", null));

        VarBit.count = s.get_ushort();
        VarBit.instance = new VarBit[VarBit.count];

        for (int i = 0; i < VarBit.count; i++) {
            VarBit.instance[i] = new VarBit(s);
        }

        if (s.position != s.payload.length) {
            System.out.println("varbit load mismatch");
        }
    }

    public int offset;
    public int setting;
    public int shift;

    public VarBit(Buffer s) {
        do {
            int opcode = s.get_ubyte();

            if (opcode == 0) {
                return;
            }

            if (opcode == 1) {
                setting = s.get_ushort();
                offset = s.get_ubyte();
                shift = s.get_ubyte();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        } while (true);
    }
}
