package dk.itu.n.danmarkskort.models;

public class ParsedNode {

    public static final long serialVersionUID = 20170322L;
    private ParsedNode next;
    private long key;
    private float lon, lat;

    // Total memory usage of a Node is 32 bytes.
    public ParsedNode(long key, ParsedNode next, float lon, float lat) {
        this.key = key;
        this.next = next;
        this.lon = lon;
        this.lat = lat;
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
