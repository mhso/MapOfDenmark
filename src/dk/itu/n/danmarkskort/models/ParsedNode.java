package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

public class ParsedNode extends Point2D.Float{

    public static final long serialVersionUID = 20170322L;
    private ParsedNode next;
    private long key;

    public ParsedNode(long key, ParsedNode next, float lon, float lat) {
        super(lon, lat);
        this.key = key;
        this.next = next;
    }

    // used to create a Node on-the-go. Specifically so a parsedAdress can use a KDTree
    public ParsedNode(float lon, float lat) {
        super(lon, lat);
    }

    public ParsedNode(Point2D point) {
        super((float)point.getX(), (float)point.getY());
    }

    public ParsedNode getNext() { return next; }
    public long getKey() { return key; }
    public float getLon() { return x; }
    public float getLat() { return y; }
    //public float[] getPoint() { return new float[]{lon, lat}; }

    public String toString() {
    	return "ParsedNode [" + "key=" + key + ", lon=" + x + ", lat=" + y + "]";
    }
}
