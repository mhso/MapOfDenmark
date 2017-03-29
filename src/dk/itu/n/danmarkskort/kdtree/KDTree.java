package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.awt.*;
import java.util.ArrayList;

public abstract class KDTree {

    public static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        ParsedItem[] array = new ParsedItem[list.size()];
        for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        return array;
    }

    public abstract ArrayList<Shape> getShapes(float lon, float lat, float w, float h);
    public abstract ArrayList<Shape> getShapes(float lon, float lat, float w, float h, boolean sortByLon);

    public abstract KDTree getParent();
    public abstract int size();
}