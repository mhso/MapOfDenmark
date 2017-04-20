package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KDTreeLeaf<T extends KDComparable> extends KDTree<T> {

    private static final long serialVersionUID = 1522369879614832796L;
    private KDComparable[] data;
    //private int size;

    public KDTreeLeaf(ArrayList<KDComparable> list) {
        this(list.toArray(new KDComparable[list.size()]));
    }

    KDTreeLeaf(KDComparable[] array) {
        data = array;
        //size = data.length;
    }

    @Override
    public List<KDComparable[]> getItems(Region reg) {
        return getItems(reg, true);
    }

    @Override
    List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        List<KDComparable[]> arr = new ArrayList<KDComparable[]>();
        arr.add(data);
        return arr;
    }
/*
    @Override
    public int size() { return size; }
*/
}
