package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedWay extends ParsedItem {

    private Shape shape;
    private long id;
    private String type;

    public ParsedWay(long id) {
        this.id = id;
    }

    public void coordsToShape(ArrayList<Float> coords) {
        shape = new Path2D.Float();
        Path2D path = new Path2D.Float();
        path.moveTo(coords.get(0), coords.get(1));
        for(int i = 2; i < coords.size();) {
            path.lineTo(coords.get(i++), coords.get(i++));
        }
        shape = path;
    }

    public void setType(String type) { this.type = type; }

    public Shape getShape() { return shape != null ? shape : null; }
    public long getID() { return id; }
}
