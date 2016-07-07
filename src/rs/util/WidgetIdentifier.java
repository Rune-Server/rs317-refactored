package rs.util;

import rs.cache.impl.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WidgetIdentifier {

    public static ArrayList<Integer> list = new ArrayList<Integer>();

    public static int get_parent(int widget) {
        return get_parent(widget, -1);
    }

    public static int get_parent(int current, int last) {
        if (current != last) {
            Widget widget = Widget.get(current);
            if (widget != null) {
                if (widget.parent != -1) {
                    return get_parent(widget.parent, current);
                } else {
                    return widget.index;
                }
            } else {
                if (last != -1) {
                    return last;
                } else {
                    return -1;
                }
            }
        } else {
            return last;
        }
    }

    public static void print_all() {
        list.clear();

        for (int i = 0; i < Widget.instance.length; i++) {
            int parent = get_parent(i);
            if (parent != -1 && !list.contains(parent)) {
                list.add(parent);
            }
        }

        Collections.sort(list);
        System.out.println(Arrays.toString(list.toArray(new Integer[list.size()])));
    }

}
