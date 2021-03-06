package dk.itu.n.danmarkskort.kdtree;

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

    /**
     * Constructor that calls another constructor with different params.
     * This constructors param is changed from an ArrayList to an array before the actual call.
     *
     * @param list List to be converted into an array
     */
    public KDTreeLeaf(ArrayList<T> list) {
        this(list.toArray(new KDComparable[list.size()]));
    }

    /**
     * Sets an array as the data of the KDTreeLeaf.
     * Also examines all the coordinates of the elements in the array, to determine values for a bounding box.
     * @param array
     */
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
        if(overlaps(reg)) return getAllItems();
        else return Collections.emptyList();
    }

    @Override
    List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        return getItems(reg);
    }

    @Override
    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        arrList.add(data);
        return arrList;
    }

    public int nodeSize() { return 0; }

    public int leafSize() { return 1; }
    
    public int size() { return data.length; }

    public boolean isSortingByLon(int currentDepth, int depth) { return currentDepth%2 == 0; }
    
    protected int nodesAndLeafsAtDepth(int targetDepth, int currentDepth) {
    	currentDepth++;
    	if(targetDepth == currentDepth) return 1;
    	return 0;
    }

    private boolean overlaps(Region reg) {
        double minX = reg.x1 < reg.x2 ? reg.x1 : reg.x2;
        double minY = reg.y1 < reg.y2 ? reg.y1 : reg.y2;
        double width = Math.abs(reg.getWidth());
        double height = Math.abs(reg.getHeight());

        return minLon <= minX + width &&
                minLon + (maxLon - minLon) >= minX &&
                minLat <= minY + height &&
                minLat + (maxLat - minLat) >= minY;
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