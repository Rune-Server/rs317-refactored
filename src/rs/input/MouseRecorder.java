package rs.input;

public class MouseRecorder implements Runnable {

    public boolean active;
    public int cycle;
    public int last_x, last_y;
    public int off;
    public Object synchronize;
    public int[] x, y;

    public MouseRecorder() {
        this.active = true;
        this.synchronize = new Object();
        this.x = new int[500];
        this.y = new int[500];
    }

    public void run() {
        while (active) {
            synchronized (synchronize) {
                if (off < 500) {
                    x[off] = Mouse.last_x;
                    y[off] = Mouse.last_y;
                    off++;
                }
            }
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }
    }
}
