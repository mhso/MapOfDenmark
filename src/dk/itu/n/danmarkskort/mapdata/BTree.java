package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;
import java.util.List;

public class BTree {

    BTree left;
    BTree right;
    float key;
    List<OSMWay> data;

    public BTree() {
        this(null);
    }

    public BTree(List list) {
        data = list;
        key = 0f;
        left = null;
        right = null;
    }

    public void setLeft(BTree left) { this.left = left; }
    public void setRight(BTree right) { this.right = right; }
    public void setData(ArrayList list) { data = list; }
    public void setKey(float key) { this.key = key; }

    public List getData() { return data; }
    public BTree getRight() { return right; }
    public BTree getLeft() { return left; }
    public float getKey() { return key; }
}