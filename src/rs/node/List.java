package rs.node;

public class List {

    public Array array;
    public Deque deque;
    public int remaining;
    public int size;

    public List(int size) {
        this.size = size;
        this.remaining = size;
        this.deque = new Deque();
        this.array = new Array(1024);
    }

    public void clear() {
        do {
            CacheLink c = deque.pop();
            if (c != null) {
                c.detach();
                c.uncache();
            } else {
                remaining = size;
                return;
            }
        } while (true);
    }

    public CacheLink get(long index) {
        CacheLink node = (CacheLink) array.get(index);
        if (node != null) {
            deque.push(node);
        }
        return node;
    }

    public void insert(CacheLink c, long index) {
        if (remaining == 0) {
            CacheLink tail = deque.pop();
            tail.detach();
            tail.uncache();

            if (tail == CacheLink.EMPTY) {
                CacheLink nextNode = deque.pop();
                nextNode.detach();
                nextNode.uncache();
            }
        } else {
            remaining--;
        }
        array.insert(c, index);
        deque.push(c);
    }
}
