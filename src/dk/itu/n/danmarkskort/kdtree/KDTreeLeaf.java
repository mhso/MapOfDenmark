package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class KDTreeLeaf extends KDTree {

    private ParsedItem[] data;
    private int size;

    public KDTreeLeaf(ArrayList<ParsedItem> list) {
        this(listToArray(list));
    }

    KDTreeLeaf(ParsedItem[] array) {
        data = array;
        size = data.length;
    }

    @Override
    public void deleteOldRefs() {
        for(ParsedItem item : data) item.deleteOldRefs();
    }

    @Override
    public void makeShapes() {
        for(ParsedItem item : data) item.makeShape();
    }

    @Override
    public ArrayList<Shape> getShapes(Region reg) {
        return getShapes(reg, true);
    }

    @Override
    ArrayList<Shape> getShapes(Region reg, boolean sortByLon) {
        ArrayList<Shape> shapes = new ArrayList<>(size);
        for(ParsedItem item : data) shapes.add(item.getShape());
        return shapes;
    }

    @Override
    public int size() { return size; }
}
