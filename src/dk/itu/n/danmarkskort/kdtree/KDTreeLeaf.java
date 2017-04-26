package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KDTreeLeaf<T extends KDComparable> extends KDTree<T> {

    private static final long serialVersionUID = 1522369879614832796L;
    private KDComparable[] data;
    private float minLon = Float.POSITIVE_INFINITY,
            maxLon = Float.NEGATIVE_INFINITY,
            minLat = Float.POSITIVE_INFINITY,
            maxLat = Float.NEGATIVE_INFINITY;

    public KDTreeLeaf(ArrayList<T> list) {
        this(list.toArray(new KDComparable[list.size()]));
    }

    KDTreeLeaf(KDComparable[] array) {
        data = array;
        for(KDComparable item: data) {
            for(ParsedNode node: item.getNodes()) {
                if(minLon > node.getLon()) minLon = node.getLon();
                if(maxLon < node.getLon()) maxLon = node.getLon();
                if(minLat > node.getLat()) minLat = node.getLat();
                if(maxLat < node.getLat()) maxLat = node.getLat();
            }
        }
    }

    @Override
    public List<KDComparable[]> getItems(Region reg) {
        return getAllItems();
    }

    @Override
    List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        if(overlaps(reg)) return getAllItems();
        else return Collections.emptyList();
    }

    @Override
    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        arrList.add(data);
        return arrList;
    }

    private boolean overlaps(Region reg) {
        return minLon < reg.x1 + reg.getWidth() &&
                minLon + (maxLon - minLon) > reg.x1 &&
                minLat < reg.y1 + reg.getHeight() &&
                minLat + (maxLat - minLat) > reg.y1;
    }

    @SuppressWarnings("unchecked")
    public T nearest(ParsedNode query, double shortest, boolean sortByLon) {
        KDComparable candidate = null;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for(KDComparable item: data) {
            if(item.shortestDistance(query) < shortestDistance) {
                shortestDistance = item.shortestDistance(query);
                candidate = item;
            }
        }
        return (T) candidate;
    }
}
