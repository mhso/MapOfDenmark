package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.Main;

public class NodeMap {

    private Node[] nodes;
    private int size;
    private int allowedRange;

    public NodeMap() {
        this(22);
    }

    public NodeMap(int cap) {
        nodes = new Node[1 << cap]; // length = 2^cap
        size = 0;
        allowedRange = nodes.length - 1;
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
        int i = 0;
        for(Node node = nodes[getHash(key)]; node != null; node = node.getNext()) {
            if(node.getKey() == key) return node;
        }
        return null;
    }

    public int size() { return size; }

    // "& allowedRange" makes sure we don't get arrayIndexOutOfBound later
    private int getHash(long key) {
        return Long.hashCode(key) & allowedRange;
    }

    public static class Node{
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
        public float getLon() { return lon; }
        public float getLat() { return lat; }
    }
}
