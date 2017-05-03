package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.Region;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public abstract class KDTree<T extends KDComparable> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = 5138300688014828078L;

    abstract List<KDComparable[]> getItems(Region reg);

    abstract List<KDComparable[]> getItems(Region reg, boolean sortByLon);

    abstract List<KDComparable[]> getAllItems();
    
    public int nodeSize() {
    	return nodeSize(0);
    }
    
    protected int nodeSize(int currentSize) {
    	return currentSize;
    }
    
    public int nodeSizeAt(int depth) {
    	return nodeSizeAt(depth, 0, 0);
    }
    
    protected abstract int nodeSizeAt(int depth, int currentDepth, int currentSize);
    
    public int leafSize() {
    	return leafSize(0);
    }
    
    public int size() {
    	return size(0);
    }
    
    protected int size(int currentSize) {
    	return currentSize;
    }
    
    protected abstract int leafSize(int currentSize);

    public Iterator<T> iterator() { return new KDTreeIterator(); }

    public Iterator<T> iterator(Region reg) { return new KDTreeIterator(reg); }

    static KDComparable[] listToArray(List list) {
        KDComparable[] arr = new KDComparable[list.size()];
        for(int i = 0; i < arr.length; i++) arr[i] = (KDComparable) list.get(i);
        return arr;
    }

    private class KDTreeIterator implements Iterator<T> {
        List<KDComparable[]> arrList;
        int i = 0;
        int j = 0;

        KDTreeIterator() { arrList = getAllItems(); }

        KDTreeIterator(Region reg) { arrList = getItems(reg); }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() { 
        	while(i < arrList.size() && arrList.get(i) == null) {
        		i++;
        	}
        	return i < arrList.size(); }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
        
            T next = (T) arrList.get(i)[j++];
            if(j > arrList.get(i).length - 1) {
                j = 0;
                i++;
            }
            return next;
        }
    }

    public static double calcDistance(Point2D.Float a, Point2D.Float b) {
        double x = b.x - a.x;
        double y = b.y - a.y;
        return Math.sqrt((x * x) + (y * y));
    }

    /*
    *   Finds the shortest distance between a query point and a line segment
    *
    *   Method logic inspired by: http://stackoverflow.com/a/1501725
     */
    private static double distancePointToLine(Point2D.Float a, Point2D.Float b, Point2D.Float query) {
        double lengthSquared = distanceSquared(a, b);
        // if point 'a' and 'b' are equal (length returns 0) we just need to return the distance between 'a' (or 'b')
        // and the query point
        if(lengthSquared == 0) return Math.sqrt(distanceSquared(a, query));
        // the dot (scalar) product. The angle between
        double dot = ((query.x - a.x) * (b.x - a.x)) +
                ((query.y - a.y) * (b.y - a.y));
        double scale = Math.max(0, Math.min(1, dot / lengthSquared));
        Point2D.Float closestPoint = new Point2D.Float((float)(a.x + scale * (b.x - a.x)),
                (float)(a.y + scale * (b.y - a.y)));
        return Math.sqrt(distanceSquared(query, closestPoint));
    }

    private static double distanceSquared(Point2D.Float a, Point2D.Float b) {
        double x = b.x - a.x;
        double y = b.y - a.y;
        return (x * x) + (y * y);
    }

    public T nearest(Point2D.Float query) { return nearest(query, Double.POSITIVE_INFINITY, true); }

    abstract T nearest(Point2D.Float query, double currentShortest, boolean sortByLon);

    public static double shortestDistance(Point2D.Float query, float[] coords) {
        double shortestDistance = Double.POSITIVE_INFINITY;
        for(int i = 0; i < coords.length - 2; i+=2) {
            double distance = KDTree.distancePointToLine(
                    new Point2D.Float(coords[i], coords[i+1]),
                    new Point2D.Float(coords[i+2], coords[i+3]),
                    query);
            if(distance < shortestDistance) shortestDistance = distance;
        }
        return shortestDistance;
    }
}