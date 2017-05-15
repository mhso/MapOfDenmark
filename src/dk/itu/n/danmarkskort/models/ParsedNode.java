package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

public class ParsedNode extends Point2D.Float{
    public static final long serialVersionUID = 20170322L;
    private ParsedNode next;
    private long key;

    /**
     * A ParsedNode object is used to store information, during parsing of an OSM dataset, about a point and its
     * reference.
     *
     * In order to save memory during parsing, it also contains a reference to another ParsedNode object (which
     * gets the same hashCode from the NodeMap class), so we are able to use a custom version of a, sort of, hashmap.
     *
     * @param key The identifier found in the OSM data.
     * @param next Reference to another ParsedNode.
     * @param lon Longitude value.
     * @param lat Latitude value.
     */
    public ParsedNode(long key, ParsedNode next, float lon, float lat) {
        super(lon, lat);
        this.key = key;
        this.next = next;
    }

    /**
     * Used to create a ParsedNode not directly from OSM data, for whatever purpose.
     * @param lon Longitude value.
     * @param lat Latitude value.
     */
    public ParsedNode(float lon, float lat) {
        super(lon, lat);
    }

    /**
     * Used to creaate a ParsedNode not directly from OSM data, for whatever purpose.
     * @param point Point2D point used to get an x and y value.
     */
    public ParsedNode(Point2D point) {
        super((float)point.getX(), (float)point.getY());
    }

    public ParsedNode getNext() { return next; }
    public long getKey() { return key; }
    public float getLon() { return x; }
    public float getLat() { return y; }

    public String toString() {
    	return "ParsedNode [" + "key=" + key + ", lon=" + x + ", lat=" + y + "]";
    }

    /**
     * Used to recursively null all next references in a chain of next references between ParsedNodes.
     */
    public void killNextReference() {
        if(next == null) return;
        next.killNextReference();
        next = null;
    }
}
