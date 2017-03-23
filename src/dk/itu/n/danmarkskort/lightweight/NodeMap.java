package dk.itu.n.danmarkskort.lightweight;

import java.awt.geom.Point2D;
import java.util.Map;

public class NodeMap {

    private int capacity;
    private Node[] nodes;
    private int size;

    public NodeMap() {
        this(22);
    }

    public NodeMap(int cap) {
        capacity = cap;
        nodes = new Node[1 << capacity]; // length = 2^cap
        size = 0;
    }

    public void put(long key, float lon, float lat) { // lon = x, lat = y
        int hash = getHash(key);
        nodes[hash] = new Node(key, nodes[hash], lon, lat);
        size++;
    }

    public Node putAndTake(long key, float lon, float lat) {
        put(key, lon,lat);
        return nodes[getHash(key)];
    }

    public Node get(long key) {
        for(Node node = nodes[getHash(key)]; node != null; node = node.getNext()) {
            if(node.getKey() == key) return node;
        }
        return null;
    }

    public int size() { return size; }

    // & initialCapacity makes sure we don't get arrayIndexOutOfBound later
    private int getHash(long key) {
        return Long.hashCode(key) & (capacity);
    }

    public static class Node extends Point2D.Float{
        public static final long serialVersionUID = 20170322L;
        private Node next;
        private long key;
        private float lon, lat;

        // Total memory usage of a Node is 32 bytes.
        public Node(long key, Node next, float lat, float lon) {
            this.key = key;
            this.next = next;
            this.lon = lon;
            this.lat = lat;
        }

        public Node getNext() { return next; }
        public long getKey() { return key; }
        public float getLon() { return x; }
        public float getLat() { return y; }
    }
}
