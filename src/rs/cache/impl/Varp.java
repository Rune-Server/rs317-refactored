package rs.cache.impl;

import rs.cache.Archive;
import rs.io.Buffer;

public class Varp {

    public static int count;
    public static Varp[] instance;

    public static void unpack(Archive a) {
        Buffer b = new Buffer(a.get("varp.dat"));
        Varp.count = b.get_ushort();
        Varp.instance = new Varp[count];

        for (int i = 0; i < Varp.count; i++) {
            Varp.instance[i] = new Varp(b);
        }

        if (b.position != b.payload.length) {
            System.out.println("varptype load mismatch");
        }
    }

    public int index;

    public Varp(Buffer b) {
        while (true) {
            int opcode = b.get_ubyte();
            if (opcode == 0) {
                return;
            } else if (opcode == 5) {
                this.index = b.get_ushort();
            } else {
                System.out.println("Error unrecognised config code: " + opcode);
            }
        }
    }

}
