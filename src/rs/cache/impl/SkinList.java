package rs.cache.impl;

import rs.io.Buffer;

public class SkinList {

    public int count;
    public int[] opcodes;
    public int[][] vertices;

    public SkinList(Buffer s) {
        this.count = s.get_ubyte();
        this.opcodes = new int[count];
        this.vertices = new int[count][];

        for (int i = 0; i < count; i++) {
            this.opcodes[i] = s.get_ubyte();
        }

        for (int i = 0; i < count; i++) {
            int size = s.get_ubyte();
            this.vertices[i] = new int[size];
            for (int j = 0; j < size; j++) {
                this.vertices[i][j] = s.get_ubyte();
            }
        }

    }
}
