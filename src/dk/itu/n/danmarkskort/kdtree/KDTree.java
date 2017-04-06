package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import java.util.ArrayList;

public abstract class KDTree {

    static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        return list.toArray(new ParsedItem[list.size()]);
    }

    public abstract ArrayList<Shape> getShapes(Region reg);
    abstract ArrayList<Shape> getShapes(Region reg, boolean sortByLon);
    public abstract int size();
    public abstract void makeShapes();
    public abstract void deleteOldRefs();
}