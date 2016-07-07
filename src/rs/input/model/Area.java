package rs.input.model;

import rs.input.Mouse;

public enum Area {
    CHAT(17, 357, 479, 96), TAB(553, 205, 190, 261), VIEWPORT(4, 4, 512, 334);

    public int x, y, width, height;

    Area(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public boolean contains_mouse() {
        return contains(Mouse.last_x, Mouse.last_y);
    }
}
