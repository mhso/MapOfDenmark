package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.geom.Point2D;
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
            for(Point2D.Float point: item.getNodes()) {
                if(minLon > point.getX()) minLon = point.x;
                if(maxLon < point.getX()) maxLon = point.x;
                if(minLat > point.getY()) minLat = point.y;
                if(maxLat < point.getY()) maxLat = point.y;
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
    
    protected int leafSize(int currentSize) {
    	return ++currentSize;
    }
    
    protected int size(int currentSize) {
    	return currentSize += data.length;
    }

    protected int nodeSizeAt(int depth, int currentDepth, int currentSize) {
    	currentDepth++;
    	if(depth == currentDepth) {
    		currentSize++;
    	}
    	return currentSize;
    }
    
    private boolean overlaps(Region reg) {
        return minLon < reg.x1 + reg.getWidth() &&
                minLon + (maxLon - minLon) > reg.x1 &&
                minLat < reg.y1 + reg.getHeight() &&
                minLat + (maxLat - minLat) > reg.y1;
    }

    @SuppressWarnings("unchecked")
    public T nearest(Point2D.Float query, double currentShortest, boolean sortByLon) {
        T candidate = null;
        double shortest = currentShortest;
        for(KDComparable item: data) {
            double distance = KDTree.shortestDistance(query, item.getCoords());
            if(distance < shortest) {
                shortest = distance;
                candidate = (T) item;
            }
        }
        return candidate;
    }
    
    public String toString() {
    	return "KDTreeLeaf [data=" + data.toString() + ", dataSize=" + data.length + ", minLon=" + minLon +
    			", minLat=" + minLat + ", maxLon=" + maxLon + ", maxLat=" + maxLat + "]";
    }
}