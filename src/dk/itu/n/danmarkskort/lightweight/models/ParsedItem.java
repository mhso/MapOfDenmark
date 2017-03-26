package dk.itu.n.danmarkskort.lightweight.models;

public abstract class ParsedItem {

    public int compareLon(ParsedItem item) {
        float a = getFirstLon();
        float b = item.getFirstLon();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public int compareLat(ParsedItem item) {
        float a = getFirstLat();
        float b = item.getFirstLat();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public abstract float getFirstLon();
    public abstract float getFirstLat();
}