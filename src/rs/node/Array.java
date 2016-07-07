package rs.node;

public class Array {

    public Link link[];
    public int size;

    public Array(int size) {
        this.size = size;
        this.link = new Link[size];

        for (int i = 0; i < size; i++) {
            Link n = link[i] = new Link();
            n.next = n;
            n.previous = n;
        }

    }

    public Link get(long l) {
        Link node = link[(int) (l & (long) (size - 1))];
        for (Link n = node.next; n != node; n = n.next) {
            if (n.identity == l) {
                return n;
            }
        }
        return null;
    }

    public void insert(Link node, long index) {
        node.detach();
        Link n = link[(int) (index & (long) (size - 1))];
        node.previous = n.previous;
        node.next = n;
        node.previous.next = node;
        node.next.previous = node;
        node.identity = index;
    }
}
