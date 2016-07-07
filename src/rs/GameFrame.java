package rs;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private static final long serialVersionUID = 2650967876682368190L;
    public Canvas canvas;
    public Insets insets;
    public GameShell shell;

    public GameFrame(GameShell shell) {
        this.shell = shell;

        this.canvas = new Canvas() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                GameFrame.this.shell.paint(g);
            }

            @Override
            public void update(Graphics g) {
                GameFrame.this.shell.update(g);
            }
        };

        this.canvas.setPreferredSize(new Dimension(GameShell.WIDTH, GameShell.HEIGHT));
        this.setTitle("Jagex");
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(this.canvas);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.toFront();
        this.setAlwaysOnTop("1".equals(System.getProperty("rt317.alwaysontop")));

        this.insets = this.getInsets();
    }

    @Override
    public void dispose() {
        super.dispose();
        shell.stop();
    }

    @Override
    public Graphics getGraphics() {
        Graphics g = super.getGraphics();

        if (insets != null) {
            g.translate(insets.left, insets.top);
        }

        return g;
    }
}
