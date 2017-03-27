package dk.itu.n.danmarkskort.mapdata;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.util.ArrayList;

public class KDTreeLeaf extends KDTree {

    private KDTree parent;
    private ParsedItem[] data;

    public KDTreeLeaf(ParsedItem[] array, KDTree parent) {
        data = array;
    }

    public KDTreeLeaf(ArrayList<ParsedItem> list, KDTree parent) {
        data = KDUtil.listToArray(list);
    }

    public ParsedItem[] getData() { return data; }
    @Override
    public KDTree getParent() { return parent; }
    @Override public int size() {
        return data.length;
    }
}
