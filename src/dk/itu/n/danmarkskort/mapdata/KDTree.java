package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;
import java.util.List;

public class KDTree {

    KDTree left;
    KDTree right;
    float leftsplit;
    float rightsplit;
    List<OSMWay> data;
    float key;


    public KDTree() {
        this(null);
    }

    public KDTree(List<OSMWay> list) {
        key = 1L;
        data = list;
        left = null;
        right = null;
    }

    public void setLeft(KDTree left) { this.left = left; }
    public void setRight(KDTree right) { this.right = right; }
    public void setData(ArrayList<OSMWay> list) { data = list; }
    public void setKey(float key) { this.key = key; }

    public List<OSMWay> getData() { return data; }
    public KDTree getRight() { return right; }
    public KDTree getLeft() { return left; }
    public float getKey() { return key; }
}