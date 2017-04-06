package dk.itu.n.danmarkskort.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public abstract class ParsedItem {

    public int compareLon(ParsedItem item) {
        float a = getFirstNode().getLon();
        float b = item.getFirstNode().getLon();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public int compareLat(ParsedItem item) {
        float a = getFirstNode().getLat();
        float b = item.getFirstNode().getLat();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public abstract ParsedNode getFirstNode();
    public abstract ArrayList<ParsedNode> getNodes();
    public abstract void makeShape();
    public abstract Shape getShape();

    public abstract void deleteOldRefs();
}
