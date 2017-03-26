package dk.itu.n.danmarkskort.mapdata;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.util.ArrayList;

public class KDTreeLeaf extends KDTree {

    private KDTree parent;
    private ArrayList<ParsedItem> data;

    public KDTreeLeaf(ArrayList<ParsedItem> list, KDTree parent) {
        data = list;
    }

    public ArrayList<ParsedItem> getData() { return data; }
    @Override
    public KDTree getParent() { return parent; }
}
