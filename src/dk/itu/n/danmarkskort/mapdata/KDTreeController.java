package dk.itu.n.danmarkskort.mapdata;

import java.util.ArrayList;

public class KDTreeController {

    private transient final int maxData = 1000;
    private boolean sortValue;

    public KDTreeController() {
        sortValue = false; // true = longitude, false = latitude
    }

    public KDTree createKDTree(ArrayList<OSMWay> list, boolean sortByLon) {
        KDTree tree = new KDTree();

        if (list.size() <= maxData) {
            tree.setData(list);
            return tree;
        } else {
            OSMWay median = QuickSelect.quickSelect(list, list.size() / 2, sortByLon);

            ArrayList<OSMWay> left = new ArrayList<>((list.size() + 1) / 2);
            ArrayList<OSMWay> right = new ArrayList<>((list.size() + 1) / 2);

            for (OSMWay o : list) {
                if(less(o, median, sortByLon)) left.add(o);
                else right.add(o);
            }

            if(sortByLon) tree.setKey(median.getLon());
            else tree.setKey(median.getLat());

            tree.setData(null);

            tree.setLeft(createKDTree(left, !sortByLon));
            tree.setRight(createKDTree(right, !sortByLon));

            return tree;
        }
    }

    private boolean less(OSMWay a, OSMWay b, boolean sortValue) {
        if(sortValue) return a.getLon() < b.getLon();
        return a.getLat() < b.getLat();
    }
}