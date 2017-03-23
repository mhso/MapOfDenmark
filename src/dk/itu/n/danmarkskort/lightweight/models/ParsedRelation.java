package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private Shape shape;
    private long id;

    public ParsedRelation(long id) {
        this.id = id;
    }

    public void waysToShape(ArrayList<ParsedWay> ways) {
        shape = new Path2D.Float();
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        for(ParsedWay way : ways) {
            if(way != null) path.append(way.getShape(), false);
        }
        shape = path;
    }

    public Shape getShape() { return shape != null ? shape : null; }
    public long getID() { return id; }
}
