package rs.input;

import rs.GameShell;
import rs.input.model.Key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Keyboard implements KeyListener {

    public static byte[] action = new byte[128];
    public static GameShell applet;
    public static int[] buffer = new int[128];
    public static int position;
    public static Keyboard instance;
    public static ArrayList<Key> keys = new ArrayList<Key>();
    public static int last_position;

    static {
        Key.init();
    }

    public static void add(Key key) {
        Keyboard.keys.add(key);
    }

    public static void clear() {
        for (int i = 0; i < Keyboard.action.length; i++) {
            Keyboard.action[i] = 0;
        }
    }

    public static boolean is_down(int action) {
        return Keyboard.action[action] == 1;
    }

    public static int next() {
        int i = -1;
        if (Keyboard.last_position != Keyboard.position) {
            i = Keyboard.buffer[Keyboard.position];
            Keyboard.position = Keyboard.position + 1 & 0x7F;
        }
        return i;
    }

    public static void process() {
        Keyboard.position = Keyboard.last_position;
    }

    public Keyboard(GameShell applet) {
        Keyboard.applet = applet;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keyboard.applet.idle_cycle = 0;
        int i = e.getKeyCode();
        int c = e.getKeyChar();

        if (c < 30) {
            c = 0;
        }

        boolean isAction = false;
        for (Key k : Keyboard.keys) {
            if (k.index == i) {
                c = k.action;
                isAction = true;
                break;
            }
        }

        if (!isAction) {
            if (i >= 112 && i <= 123) {
                c = (1008 + i) - 112;
            } else if (i == KeyEvent.VK_HOME) {
                c = 1000;
            } else if (i == KeyEvent.VK_END) {
                c = 1001;
            } else if (i == KeyEvent.VK_PAGE_UP) {
                c = 1002;
            } else if (i == KeyEvent.VK_PAGE_DOWN) {
                c = 1003;
            }
        }

        if (c > 0 && c < 128) {
            Keyboard.action[c] = 1;
        }

        if (c > 4) {
            Keyboard.buffer[Keyboard.last_position] = c;
            Keyboard.last_position = Keyboard.last_position + 1 & 0x7F;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keyboard.applet.idle_cycle = 0;
        int i = e.getKeyCode();
        char c = e.getKeyChar();

        if (c < '\036') {
            c = '\0';
        } else if (i == 37) {
            c = '\001';
        } else if (i == 39) {
            c = '\002';
        } else if (i == 38) {
            c = '\003';
        } else if (i == 40) {
            c = '\004';
        } else if (i == 17) {
            c = '\005';
        } else if (i == 8) {
            c = '\b';
        } else if (i == 127) {
            c = '\b';
        } else if (i == 9) {
            c = '\t';
        } else if (i == 10) {
            c = '\n';
        }

        if (c > 0 && c < 128) {
            Keyboard.action[c] = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
