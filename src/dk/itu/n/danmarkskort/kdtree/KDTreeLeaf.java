package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.awt.*;
import java.util.ArrayList;

public class KDTreeLeaf extends KDTree {

    private KDTree parent;
    private ParsedItem[] data;

    public KDTreeLeaf(ParsedItem[] array, KDTree parent) {
        data = array;
    }

    public KDTreeLeaf(ArrayList<ParsedItem> list, KDTree parent) {
        data = listToArray(list);
    }

    @Override
    public ArrayList<Shape> getShapes(float lon, float lat, float w, float h) {
        ArrayList<Shape> shapes = new ArrayList<>();
        for(ParsedItem item : data) shapes.add(item.getPath());
        return shapes;
    }
    @Override
    public ArrayList<Shape> getShapes(float lon, float lat, float w, float h, boolean sortByLon) {
        return getShapes(lon, lat, w, h);
    }
    public ParsedItem[] getData() { return data; }
    @Override
    public KDTree getParent() { return parent; }
    @Override public int size() {
        return data.length;
    }
}
