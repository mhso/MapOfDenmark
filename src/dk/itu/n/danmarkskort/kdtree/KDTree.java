package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;
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

    public static double distancePointToLine(Point2D.Float a, Point2D.Float b, Point2D.Float query) {
        double segmentLength = calcDistance(a, b);
        double distance = (
                ((query.x - a.y) * (b.y - a.y))
                - ((query.y - a.y) * (b.x - a.x)));
        distance = Math.abs(distance) / segmentLength;
        return distance;
    }

    public T nearest(Point2D.Float query) {
        return nearest(query, Double.POSITIVE_INFINITY, true);
    }

    abstract T nearest(Point2D.Float query, double currentShortest, boolean sortByLon);

    public static double shortestDistance(Point2D.Float query, float[] coords) {
        double shortestDistance = Double.POSITIVE_INFINITY;
        for(int i = 0; i < coords.length - 1; i++) {
            double distance = KDTree.calcDistance(query, new Point2D.Float(coords[i], coords[i+1]));
            /*double distance = KDTree.distancePointToLine(
                    new ParsedNode(coords[i], coords[i+1]),
                    new ParsedNode(coords[i+2], coords[i+3]),
                    query);*/
            if(distance < shortestDistance) shortestDistance = distance;
        }
        return shortestDistance;
    }
}