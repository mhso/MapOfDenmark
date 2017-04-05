package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.parsedmodels.ParsedItem;
import dk.itu.n.danmarkskort.parsedmodels.Region;

import java.util.ArrayList;

public abstract class KDTree {

    public static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        return list.toArray(new ParsedItem[list.size()]);
    }

    public abstract void getShapes(Region reg, MapCanvas map);
    public abstract void getShapes(Region reg, MapCanvas map, boolean sortByLon);

    public abstract void makeShapes();
    
    public abstract int size();
    public abstract int size(Region r);
    public abstract int size(Region r, boolean b);
}