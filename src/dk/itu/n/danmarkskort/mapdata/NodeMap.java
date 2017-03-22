package dk.itu.n.danmarkskort.mapdata;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.address.AddressOsmParser;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;

import java.awt.geom.Point2D;

public class NodeMap implements OSMParserListener {

    private static NodeMap instance = new NodeMap();
    private final static int CAPACITY = 22; // actually 2^22
    private int capacity;
    private Node[] nodes;
    private int size;

    private NodeMap() {
        nodes = new Node[1 << CAPACITY]; // length = 2^cap
        size = 0;
    }

    public static NodeMap getInstance() {
        if(instance == null) return instance = new NodeMap();
        else return instance;
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

    @Override
    public void onParsingGotObject(ParsedObject parsedObject) {
        if(parsedObject instanceof ParsedNode) {
            ParsedNode node = (ParsedNode) parsedObject;
            //Main.log(omsAddr.getAttributes().get("id"));
            if(node.getAttributes().get("id") != null) {
                long key = Long.parseLong(node.getAttributes().get("id"));
                float lat = Float.parseFloat(node.getAttributes().get("lat"));
                float lon = Float.parseFloat(node.getAttributes().get("lon"));

                put(key, lon, lat); // creates a new node
            }
        }
    }

    @Override
    public void onParsingFinished() {
        Main.log("NodeMap: " + size() + " nodes found");
    }

    @Override
    public void onParsingStarted() {}

    @Override
    public void onLineCountHundred() {}

    @Override
    public void onWayLinked(ParsedWay way) {}

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
