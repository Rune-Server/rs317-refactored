package rs.node;

public class Deque {

    public CacheLink current;
    public CacheLink first;

    public Deque() {
        this.first = new CacheLink();
        this.first.next_cache = this.first;
        this.first.previous_cache = this.first;
    }

    public int count() {
        int i = 0;
        for (CacheLink n = first.next_cache; n != first; n = n.next_cache) {
            i++;
        }
        return i;
    }

    public CacheLink next() {
        CacheLink c = current;
        if (c == first) {
            current = null;
            return null;
        }
        current = c.next_cache;
        return c;
    }

    public CacheLink pop() {
        CacheLink c = first.next_cache;
        if (c == first) {
            return null;
        }
        c.uncache();
        return c;
    }

    public void push(CacheLink c) {
        c.uncache();
        c.previous_cache = this.first.previous_cache;
        c.next_cache = this.first;
        c.previous_cache.next_cache = c;
        c.next_cache.previous_cache = c;
    }

    public CacheLink top() {
        CacheLink c = first.next_cache;
        if (c == first) {
            current = null;
            return null;
        }
        current = c.next_cache;
        return c;
    }
}
