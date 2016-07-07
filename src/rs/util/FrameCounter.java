package rs.util;

public class FrameCounter {

    private long[] cache;
    private int fps;
    private int position;

    public FrameCounter() {
        this.position = 0;
        this.cache = new long[0x20];
    }

    public int get() {
        return this.fps;
    }

    public void tick() {
        long current = System.currentTimeMillis();
        long previous = this.cache[this.position];

        this.cache[this.position] = current;
        this.position = this.position + 1 & 0x1f;

        if (previous != 0L && previous < current) {
            int difference = (int) (current - previous);
            this.fps = ((difference >> 1) + 32000) / difference;
        }
    }

}
