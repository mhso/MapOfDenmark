package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

public class ParsedNode {

    public static final long serialVersionUID = 20170322L;
    private ParsedNode next;
    private long key;
    private float lon, lat;

    public ParsedNode(long key, ParsedNode next, float lon, float lat) {
        this.key = key;
        this.next = next;
        this.lon = lon;
        this.lat = lat;
    }

    // used to create a Node on-the-go. Specifically so a parsedAdress can use a KDTree
    public ParsedNode(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public ParsedNode(Point2D point) {
        this.lon = (float) point.getX();
        this.lat = (float) point.getY();
    }

    public ParsedNode getNext() { return next; }
    public long getKey() { return key; }
    public float getLon() { return lon; }
    public float getLat() { return lat; }
    public float[] getPoint() { return new float[]{lon, lat}; }

    public String toString() {
    	return "ParsedNode [" + "key=" + key + ", lon=" + lon + ", lat=" + lat + "]";
    }
}
