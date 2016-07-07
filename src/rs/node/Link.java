package rs.node;

public class Link {

    public static final Link EMPTY = new Link();

    public long identity;
    public Link next;
    public Link previous;

    public void detach() {
        if (this.previous != null) {
            this.previous.next = this.next;
            this.next.previous = this.previous;
            this.next = null;
            this.previous = null;
        }
    }

}
