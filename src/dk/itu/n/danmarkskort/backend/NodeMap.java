package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedNode;

public class NodeMap {

    private ParsedNode[] nodes;
    private int size;
    private int allowedRange;

    public NodeMap() {
        this(22);
    }

    public NodeMap(int cap) {
        nodes = new ParsedNode[1 << cap]; // length = 2^cap
        size = 0;
        allowedRange = nodes.length - 1;
    }

    public void put(long key, float lon, float lat) { // lon = x, lat = y
        int hash = getHash(key);
        nodes[hash] = new ParsedNode(key, nodes[hash], lon, lat);
        size++;
    }

    public ParsedNode get(long key) {
        for(ParsedNode node = nodes[getHash(key)]; node != null; node = node.getNext()) {
            if(node.getKey() == key) return node;
        }
        return null;
    }

    public int size() { return size; }

    // "& allowedRange" makes sure we don't get arrayIndexOutOfBound later
    private int getHash(long key) {
        return Long.hashCode(key) & allowedRange;
    }

    public void killNextReferences() {
        for(int i = 0; i < nodes.length; i++) {
            if(nodes[i] != null) nodes[i].killNextReference();
            nodes[i] = null;
        }
    }
}
