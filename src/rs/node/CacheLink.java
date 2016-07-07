package rs.node;

public class CacheLink extends Link {

    public static final CacheLink EMPTY = new CacheLink();

    public CacheLink next_cache;
    public CacheLink previous_cache;

    public void uncache() {
        if (this.previous_cache != null) {
            this.previous_cache.next_cache = this.next_cache;
            this.next_cache.previous_cache = this.previous_cache;
            this.next_cache = null;
            this.previous_cache = null;
        }
    }

}
