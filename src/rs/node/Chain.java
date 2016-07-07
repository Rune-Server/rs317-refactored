package rs.node;

public class Chain {

    public Link current;
    public Link first;

    public Chain() {
        first = new Link();
        first.next = first;
        first.previous = first;
    }

    public Link bottom() {
        Link n = first.previous;
        if (n == first) {
            current = null;
            return null;
        }
        current = n.previous;
        return n;
    }

    public void clear() {
        if (first.next == first) {
            return;
        }
        do {
            Link n = first.next;
            if (n == first) {
                return;
            }
            n.detach();
        } while (true);
    }

    public Link next() {
        Link n = current;
        if (n == first) {
            current = null;
            return null;
        }
        current = n.next;
        return n;
    }

    public Link pop() {
        Link n = first.next;
        if (n == first) {
            return null;
        }
        n.detach();
        return n;
    }

    public Link previous() {
        Link n = current;
        if (n == first) {
            current = null;
            return null;
        }
        current = n.previous;
        return n;
    }

    public void push(Link n) {
        n.detach();
        n.previous = first;
        n.next = first.next;
        n.previous.next = n;
        n.next.previous = n;
    }

    public void push_back(Link n) {
        n.detach();
        n.previous = first.previous;
        n.next = first;
        n.previous.next = n;
        n.next.previous = n;
    }

    public Link top() {
        Link n = first.next;
        if (n == first) {
            current = null;
            return null;
        }
        current = n.next;
        return n;
    }
}
