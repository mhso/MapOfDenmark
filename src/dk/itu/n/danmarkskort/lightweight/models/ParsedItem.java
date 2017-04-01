package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.geom.Path2D;
import java.util.ArrayList;

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

    public abstract ParsedNode getFirstNode();
    public abstract ParsedNode getLastNode();
    public abstract ArrayList<ParsedNode> getNodes();
    public abstract Path2D getPath();
    public abstract float getFirstLon();
    public abstract float getFirstLat();
}
