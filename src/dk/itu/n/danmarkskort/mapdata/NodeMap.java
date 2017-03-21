package dk.itu.n.danmarkskort.mapdata;

import java.awt.geom.Point2D;

public class NodeMap {

    private int capacity = 22; // actually 2^22
    private Node[] nodes;
    private int size;

    public NodeMap() {
        this(22);
    }

    public NodeMap(int cap) {
        capacity = cap;
        nodes = new Node[1 << cap]; // length = 2^cap
        size = 0;
    }

    public void put(long key, float lon, float lat) { // lon = x, lat = y
        int hash = getHash(key);
        nodes[hash] = new Node(key, nodes[hash], lon, lat);
        size++;
    }

    public Point2D get(long key) {
        for(Node node = nodes[getHash(key)]; node != null; node = node.getNext()) {
            if(node.getKey() == key) return node;
        }
        return null;
    }

    // & initialCapacity makes sure we don't get arrayIndexOutOfBound later
    private int getHash(long key) { return Long.hashCode(key) & (capacity); }
    public int size() { return size; }
    public int length() { return nodes.length; }

    public static class Node extends Point2D.Float{
        private Node next;
        private long key;

        // Total memory usage of a Node is 32 bytes.
        public Node(long key, Node next, float lat, float lon) {
            super(lon, lat); // x, y
            this.next = next;
            this.key = key;
        }

        public Node getNext() { return next; }
        public long getKey() { return key; }
    }
}
