package dk.itu.n.danmarkskort.datastructure;

import java.awt.geom.Point2D;

public class PointMap {

    private final int initialCapacity;
    public Node[] nodes;

    public PointMap() {
        this(22);
    }

    public PointMap(int cap) {
        nodes = new Node[1 << cap]; // length = 2^cap
        initialCapacity = nodes.length - 1;
    }

    public void put(long key, float lat, float lon) { // lat = x, lon = y
        int hash = getHash(key);
        nodes[hash] = new Node(key, nodes[hash], lon, lat);
    }

    public Point2D get(long key) {
        for(Node node = nodes[getHash(key)]; node != null; node = node.getNext()) {
            if(node.getKey() == key) return node;
        }
        return null;
    }

    // & initialCapacity makes sure we don't get arrayIndexOutOfBound later
    private int getHash(long key) { return Long.hashCode(key) & initialCapacity; }

    public static class Node extends Point2D.Float{
        private Node next;
        private long key;

        public Node(long key, Node next, float lat, float lon) {
            super(lon, lat); // x, y
            this.next = next;
            this.key = key;
        }

        public Node getNext() { return next; }
        public long getKey() { return key; }
    }
}
