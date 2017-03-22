package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;
import java.util.List;

public class KDTree {

    KDTree left;
    KDTree right;
    float key;
    List<OSMWay> data;

    public KDTree() {
        this(null);
    }

    public KDTree(List list) {
        data = list;
        key = 0f;
        left = null;
        right = null;
    }

    public void setLeft(KDTree left) { this.left = left; }
    public void setRight(KDTree right) { this.right = right; }
    public void setData(ArrayList list) { data = list; }
    public void setKey(float key) { this.key = key; }

    public List getData() { return data; }
    public KDTree getRight() { return right; }
    public KDTree getLeft() { return left; }
    public float getKey() { return key; }
}