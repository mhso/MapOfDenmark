package dk.itu.n.danmarkskort.mapdata;

public class OSMWay {

    private long[] path;

    //placeholders
    private float lat;
    private float lon;

    public OSMWay() {
        path = new long[10];
    }

    //placeholder, vi skal have adgang til pathens nodes
    public float getLat() { return lat; }
    public float getLon() { return lon; }
}
