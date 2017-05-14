package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.Region;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public abstract class KDTree<T extends KDComparable> implements Serializable, Iterable<T> {

	private static final long serialVersionUID = 5138300688014828078L;

    /**
     * Makes sure that getItems(Region reg, boolean sortByLon) always start by looking at longitude values, as all KDTrees are ordered according to
     * longitude values at the highest level.
     *
     * @param reg Used to determine whether a KDTree leaf or node is worth looking at.
     * @return All the elements stored in leafs, that contains at least one element found inside the region.
     */
    public List<KDComparable[]> getItems(Region reg) {
        if(reg == null) throw new IllegalArgumentException("Can't find items by region if region is null");
        return getItems(reg, true);
    }

    /**
     * If KDTree object is an instance of KDTreeNode should be used to if each child is within the specified region's
     * longitude or latitude values (depending on the boolean sortByLon). If they are recursively call the same method
     * on them. The potentially returned lists from the children are merged and returned.
     *
     * If KDTree object is an instance KDTreeLeaf, check whether the bounding region, made of all the elements
     * coordinates, overlaps the region given as parameter.
     *
     * @param reg Used to determine whether a KDTree leaf or node is worth looking at.
     * @return All the elements stored in leafs, that contains at least one element found inside the region.
     */
    abstract List<KDComparable[]> getItems(Region reg, boolean sortByLon);

    /**
     * If a specific KDTree object is an instance of KDTreeNode, all children are asked to return all their elements,
     * as arrays inside ArrayLists. These lists are then merged.
     *
     * If a KDTree object is an instance of KDTreeLeaf, the element data is returned as an array inside an ArrayList.
     *
     * @return All the objects stored in a KDTree structure.
     */
    abstract List<KDComparable[]> getAllItems();

    public abstract int nodeSize();

    public int nodesAndLeafsAtDepth(int targetDepth) { return nodesAndLeafsAtDepth(targetDepth, 0); }
    
    abstract int nodesAndLeafsAtDepth(int targetDepth, int currentDepth);

    public abstract int size();

    public abstract int leafSize();

    /**
     * Returns an iterator with all the elements in the KDTree.
     *
     * Example of usage:
     *      for(KDComparable item: tree) { // some code here }
     *
     * @return An iterator of all the elements in the KDTree.
     */
    public Iterator<T> iterator() { return new KDTreeIterator(); }

    /**
     * Returns an iterator with all the elements in the KDTree's KDTreeLeafs, which have elements that are inside the
     * region given as input.
     *
     * Example of usage:
     *      for(Iterator<KDComparable> i = tree.iterator(region); i.hasNext(); ) {
     *          KDComparable item = i.next();
     *          // some code here
     *      }
     *
     * @param reg Used to to search down the KDTree's nodes, and also used by KDTreeLeafs to determine whether it has
     *            any elements inside the region.
     * @return An iterator of elements in the KDTree, which are inside the region (or grouped together in a KDTreeLeaf
     * with one that is).
     */
    public Iterator<T> iterator(Region reg) { return new KDTreeIterator(reg); }

    /**
     * Converts a list into an array of KDComparable objects, by creating a new array with length equal to the
     * input-lists' size. Then it iterates length-times and adds the list' references to the new array.
     *
     * @param list List of KDComparable objects. Can potentially be used with List of any types, but this will cause
     *             issues. Everywhere this method is called in the package, it is whith elements that implements
     *             KDComparable.
     * @return Array of KDComparable objects.
     */
    static KDComparable[] listToArray(List list) {
        KDComparable[] arr = new KDComparable[list.size()];
        for(int i = 0; i < arr.length; i++) arr[i] = (KDComparable) list.get(i);
        return arr;
    }

    /**
     * Inner iterator class that traversed down the KDTree with either the recursive method getAllItems og
     * getItems(Region reg), depending on which contructor is being used.
     */
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
            return i < arrList.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @SuppressWarnings("unchecked")
        @Override
        public T next() {
        
            T next = (T) arrList.get(i)[j];
            j++;
            if(j >= arrList.get(i).length) {
                j = 0;
                i++;
            }
            return next;
        }
    }

    /**
     * Calculates the distance between to points/coordinates
     * @param a The first coordinate.
     * @param b The second coordinate.
     * @return The distance between a and b.
     */
    public static double calcDistance(Point2D.Float a, Point2D.Float b) {
        double x = b.x - a.x;
        double y = b.y - a.y;
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * Calculates the shortest distance between a point and a line segment.
     *
     * Method logic is inspired by the code found here: http://stackoverflow.com/a/1501725
     *
     * @param a First point of the line.
     * @param b Second point of the line.
     * @param query The point, whose shortest distance to the line we want to find.
     * @return The shortest distance between the line and the query point.
     */
    public static double distancePointToLine(Point2D.Float a, Point2D.Float b, Point2D.Float query) {
        double lengthSquared = distanceSquared(a, b);
        // if point 'a' and 'b' are equal (length returns 0) we just need to return the distance between 'a' (or 'b')
        // and the query point
        if(lengthSquared == 0) return Math.sqrt(distanceSquared(a, query));
        double dot = ((query.x - a.x) * (b.x - a.x)) +
                ((query.y - a.y) * (b.y - a.y));
        double scale = Math.max(0, Math.min(1, dot / lengthSquared));
        Point2D.Float closestPoint = new Point2D.Float((float)(a.x + scale * (b.x - a.x)),
                (float)(a.y + scale * (b.y - a.y)));
        return Math.sqrt(distanceSquared(query, closestPoint));
    }

    /**
     * Squared distance between two points.
     * @param a The first point.
     * @param b The second point.
     * @return The distance.
     */
    private static double distanceSquared(Point2D.Float a, Point2D.Float b) {
        double x = b.x - a.x;
        double y = b.y - a.y;
        return (x * x) + (y * y);
    }

    /**
     * Returns the KDComparable items in a KDTree, closest to the query point.
     * @param query The point the KDTree's elements are matched against.
     * @return The nearest object.
     */
    public T nearest(Point2D.Float query) { return nearest(query, Double.POSITIVE_INFINITY, true); }


    /**
     * Returns the KDComparable items in a KDTree, closest to the query point.
     * @param query The point which the KDTree's objects are matched against.
     * @param currentShortest The currently shortest known distance between query and a line segment in the KDTree.
     * @param sortByLon Whether we compare by longitude (true) or latitude (false) at this depth.
     * @return The nearest object, or null of none in this part of the KDTree is closer than currentShortest.
     */
    abstract T nearest(Point2D.Float query, double currentShortest, boolean sortByLon);

    /**
     * Finds the shortest distance between a query point and an array floats, that represent a (potentially)
     * multisegmented line. All floats at even indices represent longitude values, whereas the uneven indices
     * represent latitude values.
     *
     * A loop determines what the shortest distance is between the query point and every line between two coordinates,
     * from the float array.
     *
     * @param query The query point.
     * @param coords The coordinates we match up against.
     * @return The shortest distance.
     */
    public static double shortestDistance(Point2D.Float query, float[] coords) {
        double shortestDistance = Double.POSITIVE_INFINITY;
        if(coords.length == 2) {
        	double distance = KDTree.calcDistance(query, new Point2D.Float(coords[0], coords[1]));
            if(distance < shortestDistance) shortestDistance = distance;
        } else {
	        for(int i = 0; i < coords.length - 2; i+=2) {
	        	double distance = KDTree.distancePointToLine(
	                    new Point2D.Float(coords[i], coords[i+1]),
	                    new Point2D.Float(coords[i+2], coords[i+3]),
	                    query);
	            if(distance < shortestDistance) shortestDistance = distance;
	        }
        }
        return shortestDistance;
    }
}