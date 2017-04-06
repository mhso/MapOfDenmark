package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import java.util.ArrayList;

public class KDTreeLeaf extends KDTree {

    private ParsedItem[] data;
    private Shape[] shapes;

    public KDTreeLeaf(ParsedItem[] array) {
        data = array;
    }

    public KDTreeLeaf(ArrayList<ParsedItem> list) {
        data = listToArray(list);
    }

    @Override
    public void makeShapes() {
        shapes = new Shape[data.length];
        for(int i = 0; i < shapes.length; i++) {
            shapes[i] = data[i].getPath();
        }
        data = null;
    }
    @Override
    public void getShapes(Region reg, MapCanvas map) {
        getShapes(reg, map, true);
    }
    @Override
    public void getShapes(Region reg, MapCanvas map, boolean sortByLon) {
        map.drawShapes(shapes);
    }
    @Override public int size() {
    	if(data == null) {
    		if(shapes == null) return 0;
    		return shapes.length;
    	}
    	return data.length;
    }

    public int size(Region reg) { return size(); }
    public int size(Region reg, boolean sortByLon) { return size(); }
}
