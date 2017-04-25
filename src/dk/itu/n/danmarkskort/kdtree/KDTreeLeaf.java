package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.Region;

import java.util.ArrayList;
import java.util.List;

public class KDTreeLeaf<T extends KDComparable> extends KDTree<T> {

    private static final long serialVersionUID = 1522369879614832796L;
    private KDComparable[] data;

    public KDTreeLeaf(ArrayList<T> list) {
        this(list.toArray(new KDComparable[list.size()]));
    }

    KDTreeLeaf(KDComparable[] array) {
        data = array;
    }

    @Override
    public List<KDComparable[]> getItems(Region reg) {
        return getAllItems();
    }

    @Override
    List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        return getAllItems();
    }

    @Override
    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        arrList.add(data);
        return arrList;
    }
}
