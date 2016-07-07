package rs;

import rs.input.Keyboard;
import rs.input.Mouse;
import rs.net.Jaggrab;
import rs.util.FrameCounter;
import rs.util.JString;
import rs.util.NanoTimer;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.Socket;

public abstract class GameShell extends Applet implements Runnable {

    public static FrameCounter counter;
    public static final int WIDTH = 765;
    public static final int HEIGHT = 503;

    private static final long serialVersionUID = 8263129451912051795L;

    public Color color;
    public boolean focused = true;
    public Font font;
    public GameFrame frame;
    public Graphics graphics;
    public int idle_cycle;
    public Keyboard keyboard;
    public Mouse mouse;
    public boolean redraw = true;
    public NanoTimer timer;

    public GameShell init(boolean frame) {
        if (frame) {
            this.frame = new GameFrame(this);
        }
        this.graphics = getComponent().getGraphics();
        this.start_thread(this, 1);
        this.font = new Font(JString.HELVETICA, Font.BOLD, 13);
        this.color = new Color(140, 17, 17);
        return this;
    }

    @Override
    public void start() {
    }

    public abstract void startup();

    public abstract void process();

    public abstract void draw();

    public abstract void redraw();

    @Override
    public void run() {
        this.keyboard = new Keyboard(this);
        this.mouse = new Mouse(this);

        this.getComponent().addMouseListener(this.mouse);
        this.getComponent().addMouseMotionListener(this.mouse);
        this.getComponent().addMouseWheelListener(this.mouse);
        this.getComponent().addKeyListener(this.keyboard);

        this.getComponent().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusevent) {
                focused = true;
                redraw = true;
                redraw();
            }

            @Override
            public void focusLost(FocusEvent focusevent) {
                focused = false;
                Keyboard.clear();
            }
        });

        Jaggrab.set_shell(this);

        this.draw_progress(JString.LOADING, 0); //$NON-NLS-1$
        this.startup();

        GameShell.counter = new FrameCounter();

        this.timer = new NanoTimer();

        while (!Game.stopping) {
            int count = this.timer.sleep(1, 20);

            for (int i = 0; i < count; i++) {
                Mouse.process();
                this.process();
                Keyboard.process();
            }

            counter.tick();
            this.draw();

        }

        this.destroy();
    }

    public abstract void shutdown();

    @Override
    public void stop() {
        super.stop();
        Game.stopping = true;
    }

    @Override
    public void destroy() {
        try {
            Thread.sleep(1000L);
            Signlink.error = true;
            this.shutdown();
            Thread.sleep(1000);
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public void paint(Graphics g) {
        if (this.graphics == null) {
            this.graphics = g;
        }
        this.redraw = true;
        this.redraw();
    }

    public void update(Graphics g) {
        if (this.graphics == null) {
            this.graphics = g;
        }
        this.redraw = true;
        this.redraw();
    }

    public void draw_progress(String s, int i) {
        while (this.graphics == null) {
            this.graphics = getComponent().getGraphics();
            try {
                getComponent().repaint();
            } catch (Exception _ex) {
            }
            try {
                Thread.sleep(100L);
            } catch (Exception _ex) {
            }
        }

        if (this.redraw) {
            this.graphics.setColor(Color.BLACK);
            this.graphics.fillRect(0, 0, WIDTH, HEIGHT);
            this.redraw = false;
        }

        int x = WIDTH / 2;
        int y = HEIGHT / 2 - 18;

        this.graphics.setColor(Color.BLACK);
        this.graphics.fillRect(x - 152, y, 304, 33);

        this.graphics.setColor(this.color);
        this.graphics.drawRect(x - 152, y, 304, 33);
        this.graphics.fillRect(x - 150, y + 2, i * 3, 30);

        this.graphics.setFont(this.font);
        this.graphics.setColor(Color.WHITE);
        this.graphics.drawString(s, (WIDTH - this.graphics.getFontMetrics().stringWidth(s)) / 2, y + 22);
    }

    public void start_thread(Runnable runnable, int priority) {
        Thread t = new Thread(runnable);
        t.start();
        t.setPriority(priority);
        t.setName(runnable.getClass().getSimpleName());
    }

    public abstract Socket get_socket(int port) throws IOException;

    public Component getComponent() {
        if (this.frame != null) {
            return this.frame.canvas;
        }
        return this;
    }
}
