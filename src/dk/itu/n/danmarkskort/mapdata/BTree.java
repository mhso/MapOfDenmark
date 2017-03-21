package dk.itu.n.danmarkskort.mapdata;

import java.util.HashSet;
import java.util.Set;

public class BTree {

    BTree left;
    BTree right;
    float key;
    Set<OSMWay> data;

    public BTree(float key) {
        this.key = key;
        left = null;
        right = null;
        data = new HashSet<>();
    }

    public void setLeft(BTree left) { this.left = left; }
    public void setRight(BTree right) { this.right = right; }

    public Set getData() { return data; }
    public BTree getRight() { return right; }
    public BTree getLeft() { return left; }
    public float getKey() { return key; }
}