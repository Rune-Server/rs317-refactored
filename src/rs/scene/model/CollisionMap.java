package rs.scene.model;

public class CollisionMap {

    public int flags[][];
    public int width;
    public int height;

    public CollisionMap(int size_x, int size_y) {
        this.width = size_x;
        this.height = size_y;
        this.flags = new int[this.width][this.height];
        this.defaults();
    }

    public void add(int x, int y, int flag) {
        flags[x][y] |= flag;
    }

    public void add_loc(int loc_x, int loc_y, int size_x, int size_y, int rotation, boolean blocks_projectiles) {
        int flag = 256;

        if (blocks_projectiles) {
            flag += 0x20000;
        }

        if (rotation == 1 || rotation == 3) {
            int size_x2 = size_x;
            size_x = size_y;
            size_y = size_x2;
        }

        for (int x = loc_x; x < loc_x + size_x; x++) {
            if (x >= 0 && x < this.width) {
                for (int y = loc_y; y < loc_y + size_y; y++) {
                    if (y >= 0 && y < this.height) {
                        this.add(x, y, flag);
                    }
                }
            }
        }
    }

    public void add_wall(int x, int y, int type, int rotation, boolean blocks_projectiles) {
        if (type == 0) {
            if (rotation == 0) {
                add(x, y, 128);
                add(x - 1, y, 8);
            }
            if (rotation == 1) {
                add(x, y, 2);
                add(x, y + 1, 32);
            }
            if (rotation == 2) {
                add(x, y, 8);
                add(x + 1, y, 128);
            }
            if (rotation == 3) {
                add(x, y, 32);
                add(x, y - 1, 2);
            }
        }
        if (type == 1 || type == 3) {
            if (rotation == 0) {
                add(x, y, 1);
                add(x - 1, y + 1, 16);
            }
            if (rotation == 1) {
                add(x, y, 4);
                add(x + 1, y + 1, 64);
            }
            if (rotation == 2) {
                add(x, y, 16);
                add(x + 1, y - 1, 1);
            }
            if (rotation == 3) {
                add(x, y, 64);
                add(x - 1, y - 1, 4);
            }
        }
        if (type == 2) {
            if (rotation == 0) {
                add(x, y, 130);
                add(x - 1, y, 8);
                add(x, y + 1, 32);
            }
            if (rotation == 1) {
                add(x, y, 10);
                add(x, y + 1, 32);
                add(x + 1, y, 128);
            }
            if (rotation == 2) {
                add(x, y, 40);
                add(x + 1, y, 128);
                add(x, y - 1, 2);
            }
            if (rotation == 3) {
                add(x, y, 160);
                add(x, y - 1, 2);
                add(x - 1, y, 8);
            }
        }
        if (blocks_projectiles) {
            if (type == 0) {
                if (rotation == 0) {
                    add(x, y, 0x10000);
                    add(x - 1, y, 4096);
                }
                if (rotation == 1) {
                    add(x, y, 1024);
                    add(x, y + 1, 16384);
                }
                if (rotation == 2) {
                    add(x, y, 4096);
                    add(x + 1, y, 0x10000);
                }
                if (rotation == 3) {
                    add(x, y, 16384);
                    add(x, y - 1, 1024);
                }
            }
            if (type == 1 || type == 3) {
                if (rotation == 0) {
                    add(x, y, 512);
                    add(x - 1, y + 1, 8192);
                }
                if (rotation == 1) {
                    add(x, y, 2048);
                    add(x + 1, y + 1, 32768);
                }
                if (rotation == 2) {
                    add(x, y, 8192);
                    add(x + 1, y - 1, 512);
                }
                if (rotation == 3) {
                    add(x, y, 32768);
                    add(x - 1, y - 1, 2048);
                }
            }
            if (type == 2) {
                if (rotation == 0) {
                    add(x, y, 0x10400);
                    add(x - 1, y, 4096);
                    add(x, y + 1, 16384);
                }
                if (rotation == 1) {
                    add(x, y, 5120);
                    add(x, y + 1, 16384);
                    add(x + 1, y, 0x10000);
                }
                if (rotation == 2) {
                    add(x, y, 20480);
                    add(x + 1, y, 0x10000);
                    add(x, y - 1, 1024);
                }
                if (rotation == 3) {
                    add(x, y, 0x14000);
                    add(x, y - 1, 1024);
                    add(x - 1, y, 4096);
                }
            }
        }
    }

    public boolean at_decoration(int x, int y, int dest_x, int dest_y, int type, int rotation) {
        if (x == dest_x && y == dest_y) {
            return true;
        }
        if (type == 6 || type == 7) {
            if (type == 7) {
                rotation = rotation + 2 & 3;
            }
            if (rotation == 0) {
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x80) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 2) == 0) {
                    return true;
                }
            } else if (rotation == 1) {
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 8) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 2) == 0) {
                    return true;
                }
            } else if (rotation == 2) {
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 8) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x20) == 0) {
                    return true;
                }
            } else if (rotation == 3) {
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x80) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x20) == 0) {
                    return true;
                }
            }
        }
        if (type == 8) {
            if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x20) == 0) {
                return true;
            }
            if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 2) == 0) {
                return true;
            }
            if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 8) == 0) {
                return true;
            }
            if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x80) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean at_object(int x, int y, int dest_x, int dest_y, int size_x, int size_y, int face) {
        int max_x = (dest_x + size_x) - 1;
        int max_y = (dest_y + size_y) - 1;

        if (x >= dest_x && x <= max_x && y >= dest_y && y <= max_y) {
            return true;
        }

        if (x == dest_x - 1 && y >= dest_y && y <= max_y && (flags[x][y] & 0x8) == 0 && (face & 0x8) == 0) {
            return true;
        }

        if (x == max_x + 1 && y >= dest_y && y <= max_y && (flags[x][y] & 0x80) == 0 && (face & 0x2) == 0) {
            return true;
        }

        if (y == dest_y - 1 && x >= dest_x && x <= max_x && (flags[x][y] & 0x2) == 0 && (face & 0x4) == 0) {
            return true;
        }

        return y == max_y + 1 && x >= dest_x && x <= max_x && (flags[x][y] & 0x20) == 0 && (face & 0x1) == 0;
    }

    public boolean at_wall(int x, int y, int dest_x, int dest_y, int type, int rotation) {
        if (x == dest_x && y == dest_y) {
            return true;
        }
        if (type == 0) {
            if (rotation == 0) {
                if (x == dest_x - 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x1280120) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 0x1280102) == 0) {
                    return true;
                }
            } else if (rotation == 1) {
                if (x == dest_x && y == dest_y + 1) {
                    return true;
                }
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 0x1280108) == 0) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x1280180) == 0) {
                    return true;
                }
            } else if (rotation == 2) {
                if (x == dest_x + 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x1280120) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 0x1280102) == 0) {
                    return true;
                }
            } else if (rotation == 3) {
                if (x == dest_x && y == dest_y - 1) {
                    return true;
                }
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 0x1280108) == 0) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x1280180) == 0) {
                    return true;
                }
            }
        }
        if (type == 2) {
            if (rotation == 0) {
                if (x == dest_x - 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x1280180) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 0x1280102) == 0) {
                    return true;
                }
            } else if (rotation == 1) {
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 0x1280108) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 0x1280102) == 0) {
                    return true;
                }
            } else if (rotation == 2) {
                if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 0x1280108) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x1280120) == 0) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1) {
                    return true;
                }
            } else if (rotation == 3) {
                if (x == dest_x - 1 && y == dest_y) {
                    return true;
                }
                if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x1280120) == 0) {
                    return true;
                }
                if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x1280180) == 0) {
                    return true;
                }
                if (x == dest_x && y == dest_y - 1) {
                    return true;
                }
            }
        }
        if (type == 9) {
            if (x == dest_x && y == dest_y + 1 && (flags[x][y] & 0x20) == 0) {
                return true;
            }
            if (x == dest_x && y == dest_y - 1 && (flags[x][y] & 2) == 0) {
                return true;
            }
            if (x == dest_x - 1 && y == dest_y && (flags[x][y] & 8) == 0) {
                return true;
            }
            if (x == dest_x + 1 && y == dest_y && (flags[x][y] & 0x80) == 0) {
                return true;
            }
        }
        return false;
    }

    public void defaults() {
        int border_x = this.width - 1;
        int border_y = this.height - 1;

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (x == 0 || y == 0 || x == border_x || y == border_y) {
                    flags[x][y] = 0xFFFFFF;
                } else {
                    flags[x][y] = 0x1000000;
                }
            }
        }
    }

    public void method218(int x, int y) {
        this.flags[x][y] &= 0xDFFFFF;
    }

    public void remove(int x, int y, int flags) {
        this.flags[x][y] &= 0xFFFFFF - flags;
    }

    public void remove_loc(int x, int y, int size_x, int size_y, int rotation, boolean blocks_projectiles) {
        int flags = 256;

        if (blocks_projectiles) {
            flags += 0x20000;
        }

        if (rotation == 1 || rotation == 3) {
            int size_x2 = size_x;
            size_x = size_y;
            size_y = size_x2;
        }

        for (int i = x; i < x + size_x; i++) {
            if (i >= 0 && i < this.width) {
                for (int j = y; j < y + size_y; j++) {
                    if (j >= 0 && j < this.height) {
                        remove(i, j, flags);
                    }
                }
            }
        }
    }

    public void remove_wall(int x, int y, int type, int rotation, boolean blocks_projectiles) {
        if (type == 0) {
            if (rotation == 0) {
                remove(x, y, 128);
                remove(x - 1, y, 8);
            }
            if (rotation == 1) {
                remove(x, y, 2);
                remove(x, y + 1, 32);
            }
            if (rotation == 2) {
                remove(x, y, 8);
                remove(x + 1, y, 128);
            }
            if (rotation == 3) {
                remove(x, y, 32);
                remove(x, y - 1, 2);
            }
        }
        if (type == 1 || type == 3) {
            if (rotation == 0) {
                remove(x, y, 1);
                remove(x - 1, y + 1, 16);
            }
            if (rotation == 1) {
                remove(x, y, 4);
                remove(x + 1, y + 1, 64);
            }
            if (rotation == 2) {
                remove(x, y, 16);
                remove(x + 1, y - 1, 1);
            }
            if (rotation == 3) {
                remove(x, y, 64);
                remove(x - 1, y - 1, 4);
            }
        }
        if (type == 2) {
            if (rotation == 0) {
                remove(x, y, 130);
                remove(x - 1, y, 8);
                remove(x, y + 1, 32);
            }
            if (rotation == 1) {
                remove(x, y, 10);
                remove(x, y + 1, 32);
                remove(x + 1, y, 128);
            }
            if (rotation == 2) {
                remove(x, y, 40);
                remove(x + 1, y, 128);
                remove(x, y - 1, 2);
            }
            if (rotation == 3) {
                remove(x, y, 160);
                remove(x, y - 1, 2);
                remove(x - 1, y, 8);
            }
        }
        if (blocks_projectiles) {
            if (type == 0) {
                if (rotation == 0) {
                    remove(x, y, 0x10000);
                    remove(x - 1, y, 4096);
                }
                if (rotation == 1) {
                    remove(x, y, 1024);
                    remove(x, y + 1, 16384);
                }
                if (rotation == 2) {
                    remove(x, y, 4096);
                    remove(x + 1, y, 0x10000);
                }
                if (rotation == 3) {
                    remove(x, y, 16384);
                    remove(x, y - 1, 1024);
                }
            }
            if (type == 1 || type == 3) {
                if (rotation == 0) {
                    remove(x, y, 512);
                    remove(x - 1, y + 1, 8192);
                }
                if (rotation == 1) {
                    remove(x, y, 2048);
                    remove(x + 1, y + 1, 32768);
                }
                if (rotation == 2) {
                    remove(x, y, 8192);
                    remove(x + 1, y - 1, 512);
                }
                if (rotation == 3) {
                    remove(x, y, 32768);
                    remove(x - 1, y - 1, 2048);
                }
            }
            if (type == 2) {
                if (rotation == 0) {
                    remove(x, y, 0x10400);
                    remove(x - 1, y, 4096);
                    remove(x, y + 1, 16384);
                }
                if (rotation == 1) {
                    remove(x, y, 5120);
                    remove(x, y + 1, 16384);
                    remove(x + 1, y, 0x10000);
                }
                if (rotation == 2) {
                    remove(x, y, 20480);
                    remove(x + 1, y, 0x10000);
                    remove(x, y - 1, 1024);
                }
                if (rotation == 3) {
                    remove(x, y, 0x14000);
                    remove(x, y - 1, 1024);
                    remove(x - 1, y, 4096);
                }
            }
        }
    }

    public void set_solid(int x, int y) {
        flags[x][y] |= 0x200000;
    }
}
