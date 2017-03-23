package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;

public class KDTreeLeaf extends KDTree {

    private ArrayList<OSMWay> data;

    public KDTreeLeaf(ArrayList<OSMWay> list) {
        data = list;
    }

    public ArrayList<OSMWay> getData() { return data; }
}
