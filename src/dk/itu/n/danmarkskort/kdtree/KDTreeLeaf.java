package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.Util;
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

	@Override
	T nearest(ParsedNode query, boolean sortByLon, T currentBest) {
		double dist = Double.POSITIVE_INFINITY;
		KDComparable best = currentBest;
		ParsedNode bestNode = null;
		for(KDComparable item : data) {
			if(item.getNodes() == null) continue;
			for(ParsedNode node : item.getNodes()) {
				if(Util.calcDistance(node, query) < dist) {
					dist = Util.calcDistance(node, query);
					bestNode = node;
					best = item;
				}
			}
		}
		return (T)best;
	}
}