package rs.input;

import rs.GameShell;
import rs.input.model.Area;

import java.awt.event.*;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    public static GameShell applet;
    public static int click_button;
    public static long click_time;
    public static int click_x;
    public static int click_y;
    public static int drag_button;
    public static int last_click_button;
    public static long last_click_time;
    public static int last_click_x;
    public static int last_click_y;
    public static int last_x;
    public static int last_y;
    public static int wheel_amount = 0;
    public static int x;
    public static int y;

    public static boolean clicked(int i) {
        return last_click_button == i;
    }

    public static boolean clicked(int x, int y, int width, int height) {
        return click_button != 0 && click_x >= x && click_y >= y && click_x <= x + width && click_y <= y + height;
    }

    public static boolean is_dragging() {
        return drag_button != 0;
    }

    public static boolean is_dragging(int button) {
        return drag_button == button;
    }

    public static void process() {
        Mouse.click_button = Mouse.last_click_button;
        Mouse.click_x = Mouse.last_click_x;
        Mouse.click_y = Mouse.last_click_y;
        Mouse.click_time = Mouse.last_click_time;
        Mouse.last_click_button = 0;
    }

    public static boolean within(Area area) {
        return area.contains(Mouse.last_x, Mouse.last_y);
    }

    public Mouse(GameShell applet) {
        Mouse.applet = applet;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Mouse.applet.idle_cycle = 0;
        Mouse.last_x = x;
        Mouse.last_y = y;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
        Mouse.applet.idle_cycle = 0;
        Mouse.last_x = -1;
        Mouse.last_y = -1;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Mouse.applet.idle_cycle = 0;
        Mouse.last_x = x;
        Mouse.last_y = y;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        Mouse.applet.idle_cycle = 0;
        Mouse.last_click_x = x;
        Mouse.last_click_y = y;
        Mouse.last_click_time = System.currentTimeMillis();

        if (e.isMetaDown()) {
            Mouse.last_click_button = 2;
            Mouse.drag_button = 2;
        } else {
            Mouse.last_click_button = 1;
            Mouse.drag_button = 1;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Mouse.applet.idle_cycle = 0;
        Mouse.drag_button = 0;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheel_amount = e.getWheelRotation();
    }

}
