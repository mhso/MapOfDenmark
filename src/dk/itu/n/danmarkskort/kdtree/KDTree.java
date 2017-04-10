package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class KDTree implements Serializable {

	private static final long serialVersionUID = 5138300688014828078L;

	static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        return list.toArray(new ParsedItem[list.size()]);
    }

    public abstract ArrayList<Shape> getShapes(Region reg);
    abstract ArrayList<Shape> getShapes(Region reg, boolean sortByLon);
    public abstract int size();
    public abstract void makeShapes();
    public abstract void deleteOldRefs();
}