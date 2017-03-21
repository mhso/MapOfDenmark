package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;

public class BTreeController {

    private transient final int maxData = 1000;
    private boolean sortValue;

    public BTreeController() {
        sortValue = false; // true = longitude, false = latitude
    }

    public BTree createBTree(ArrayList<OSMWay> list, boolean sortValue) {
        BTree tree = new BTree();

        if (list.size() <= maxData) {
            tree.setData(list);
            return tree;
        } else {
            OSMWay median = QuickSelect.quickSelect(list, list.size() / 2, sortValue);

            ArrayList<OSMWay> left = new ArrayList<>(maxData / 2);
            ArrayList<OSMWay> right = new ArrayList<>(maxData / 2);

            for (OSMWay o : list) {
                if(less(o, median, sortValue)) left.add(o);
                else right.add(o);
            }

            if(sortValue) tree.setKey(median.getLon());
            else tree.setKey(median.getLat());

            tree.setLeft(createBTree(left, !sortValue));
            tree.setRight(createBTree(right, !sortValue));

            return tree;
        }
    }

    private boolean less(OSMWay a, OSMWay b, boolean sortValue) {
        if(sortValue) return a.getLon() < b.getLon();
        return a.getLat() < b.getLat();
    }

}