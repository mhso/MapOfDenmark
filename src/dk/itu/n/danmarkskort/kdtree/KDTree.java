package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.util.ArrayList;

public abstract class KDTree {

    public static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        ParsedItem[] array = new ParsedItem[list.size()];
        for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        return array;
    }

    public abstract void getShapes(Region reg, MapCanvas map);
    public abstract void getShapes(Region reg, MapCanvas map, boolean sortByLon);

    public abstract KDTree getParent();

    public abstract void makeShapes();
    
    public KDTree getRoot() {
    	KDTree parent = getParent();
    	while(parent != null) parent = parent.getParent();
    	return parent;
    }
    
    public abstract int size();
}