package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class KDTreeNode<T extends KDComparable> extends KDTree<T> {

	private static final long serialVersionUID = 242589004649413322L;
	private KDTree<T> leftChild;
    private KDTree<T> rightChild;
    private float leftSplit;
    private float rightSplit;

    public KDTreeNode(List<T> list) {
        this(KDTree.listToArray(list), true, DKConstants.KD_SIZE);
    }
    
    public KDTreeNode(List<T> list, int leafSize) {
        this(KDTree.listToArray(list), true, leafSize);
    }
    
    private KDTreeNode(KDComparable[] arr, boolean sortByLon, int kd_size) {
        makeStructure(arr, sortByLon, kd_size);
    }

    /**
     * Recursive method that creates a 2-dimensional KDTree (with the dimensions being Longitude and latitude values).
     *
     * QuickSelect is used to find the median, and order the array according to it. The reordered array is then split
     * into to new arrays, by the median, and then either used to make a new KDTreeNode (if larger than leafSize) or
     * used to create an KDTreeLeaf
     *
     * @param array Array of KDComparable elements.
     * @param sortByLon Whether sorting by longitude (true) og latitude (false) values.
     * @param leafSize The maximum size of the element data contained in a KDTreeLeaf.
     */
    private void makeStructure(KDComparable[] array, boolean sortByLon, int leafSize) {
        if(array.length <= leafSize) {
            leftChild = new KDTreeLeaf<>(array);
            rightChild = null;
            leftSplit = splitValue(array, sortByLon, true);
            rightSplit = Float.MAX_VALUE;
            return;
        }

        KDComparable median = QuickSelect.quickSelect(array, (array.length + 1) / 2, sortByLon);

        KDComparable[] leftArray = new KDComparable[(array.length + 1) / 2];
        for(int i = 0; i < leftArray.length; i++) leftArray[i] = array[i];

        KDComparable[] rightArray = new KDComparable[array.length - leftArray.length];
        for(int i = 0; i < rightArray.length; i ++) rightArray[i] = array[i + leftArray.length];

        leftSplit = splitValue(leftArray, sortByLon, true);
        rightSplit = splitValue(rightArray, sortByLon, false);

        if(leftArray.length > leafSize) leftChild = new KDTreeNode<>(leftArray, !sortByLon, leafSize);
        else leftChild = new KDTreeLeaf<>(leftArray);

        if(rightArray.length > leafSize) rightChild = new KDTreeNode<>(rightArray, !sortByLon, leafSize);
        else rightChild = new KDTreeLeaf<>(rightArray);
    }

    /**
     *  Finds the value among an array's elements that diverges the most from the rest.
     *  In the order that left-top contains the lowest values, and thus we look for the highest value among them,
     *  and with right-bottom we look for the lowest value among them.
     *
     * @param arr Array to be looked at.
     * @param sortByLon Whether we check for longitude (true) or latitude (false) values.
     * @param left Whether its the left-top side of the array, or right-bottom.
     * @return The value that, among all the coordinates of all the KDComparable objects in the array, diverges the most.
     */
    private float splitValue(KDComparable[] arr, boolean sortByLon, boolean left) {
        float value;
        if(left) value = Float.MIN_VALUE; // left-top has lowest values
        else value = Float.MAX_VALUE; // top-left has highest values

        if(sortByLon) {
            if(left) {
                for (KDComparable item : arr)
                    for (Point2D.Float node : item.getNodes())
                        value = node.x > value ? node.x : value; // left side has lowest values
            } else {
                for(KDComparable item : arr)
                    for(Point2D.Float node : item.getNodes())
                        value = node.x < value ? node.x: value; // right side has highest values
            }
        } else {
            if(left) {
                for (KDComparable item : arr)
                    for (Point2D.Float node : item.getNodes())
                        value = node.y > value ? node.y : value; // top has lowest values
            } else {
                for (KDComparable item : arr) {
                    for (Point2D.Float node : item.getNodes())
                        value = node.y < value ? node.y : value; // bottom part has highest values
                }
            }
        }
        return value;
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }

    /**
     * Recursive method that checks whether any of the children of the KDTreeNode is within either the given Region's
     * longitude or latitude values. If they are, this method is called on them.
     *
     * @param reg Used to determine whether a KDTree leaf or node is worth looking at.
     * @param sortByLon Whether we check by longitude or latitude values.
     * @return All the items returned by the children, merged together in an ArrayList.
     */
    public List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        List<KDComparable[]> items = new ArrayList<>();
        double minX = reg.x1 < reg.x2 ? reg.x1 : reg.x2;
        double maxX = reg.x1 < reg.x2 ? reg.x2 : reg.x1;
        double minY = reg.x1 < reg.x2 ? reg.y1 : reg.y2;
        double maxY = reg.x1 < reg.x2 ? reg.y2 : reg.y1;

        if(sortByLon) {
            if(minX <= leftSplit && leftChild != null) items.addAll(leftChild.getItems(reg, false));
            if(maxX >= rightSplit && rightChild != null) items.addAll(rightChild.getItems(reg, false));
        }
        else {
            if(minY <= leftSplit && leftChild != null) items.addAll(leftChild.getItems(reg, true));
            if(maxY >= rightSplit && rightChild != null) items.addAll(rightChild.getItems(reg, true));
        }
        return items;
    }

    /**
     * Asks both children to hand over all their elements, without any conditions.
     *
     * @return A List of arrays of childrens elements, gotten through the same method recursively.
     */
    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        if(leftChild != null) arrList.addAll(leftChild.getAllItems());
        if(rightChild != null) arrList.addAll(rightChild.getAllItems());
        return arrList;
    }

    /**
     * Finds the element, among all the objects stored in the KDTree, that has a line segment which has the shortest
     * distance to the query point.
     *
     * @param query The query point used to match against.
     * @param currentShortest The shortest known distance (from a different part of the KDTree).
     * @param sortByLon Whether this depth is ordered according to longitude (true) or latitude (false) values.
     * @return Null if no element in this part of the tree can possibly be closer to the query point, than the already
     * known shortestDistance. Else returns the element that is closest to the query point.
     */
    protected T nearest(Point2D.Float query, double currentShortest, boolean sortByLon) {
        if(query == null) return null;

        double nearestPossibleLT, nearestPossibleRB;
        double shortest = currentShortest;
        T candidate = null;

        // calculate the nearest possible candidate from either side
        if(sortByLon) {
            nearestPossibleLT = (query.x < leftSplit) ? 0 : calcDistance(query, new Point2D.Float(leftSplit, query.y));
            nearestPossibleRB = (query.x > rightSplit) ? 0 : calcDistance(query, new Point2D.Float(rightSplit, query.y));
        } else {
            nearestPossibleLT = (query.y < leftSplit) ? 0 : calcDistance(query, new Point2D.Float(query.x, leftSplit));
            nearestPossibleRB = (query.y > rightSplit) ? 0 : calcDistance(query, new Point2D.Float(query.x, rightSplit));
        }

        // if no possible candidate has shorter path than det one already known, abort the operation
        if(shortest < nearestPossibleLT && shortest < nearestPossibleRB) return null;

        // if query point is inside the left/top
        if(nearestPossibleLT == 0 && leftChild != null) {
            T leftCandidate = leftChild.nearest(query, shortest, !sortByLon);
            if(leftCandidate != null) {
                shortest = KDTree.shortestDistance(query, leftCandidate.getCoords());
                if(nearestPossibleRB > shortest) return leftCandidate;
                else candidate = leftCandidate;
            }
        }
        // if query point is inside the right/bottom
        if(nearestPossibleRB == 0 && rightChild != null) {
            T rightCandidate = rightChild.nearest(query, shortest, !sortByLon);
            if(rightCandidate != null) {
                shortest = KDTree.shortestDistance(query, rightCandidate.getCoords());
                if(nearestPossibleLT > shortest) return rightCandidate;
                else candidate = rightCandidate;
            }
        }

        // query point outside of left/top
        if(nearestPossibleLT > 0 && leftChild != null) {
            T leftCandidate = leftChild.nearest(query, shortest, !sortByLon);
            if(leftCandidate != null) {
                shortest = KDTree.shortestDistance(query, leftCandidate.getCoords());
                if(nearestPossibleRB > shortest) return leftCandidate;
                else candidate = leftCandidate;
            }
        }

        // query point outside of right/bottom
        if(nearestPossibleRB > 0 && rightChild != null) {
            T rightCandidate = rightChild.nearest(query, shortest, !sortByLon);
            if(rightCandidate != null) return rightCandidate;
        }

        // maybe nothing of interest found (it could be null, but it could also be amazing)
        return candidate;
    }

    public int nodeSize() {
    	int count = 0;
    	if(leftChild != null) count += leftChild.nodeSize();
    	if(rightChild != null) count += rightChild.nodeSize();
    	return ++count;
    }
    
    protected int nodesAndLeafsAtDepth(int targetDepth, int currentDepth) {
    	currentDepth++;
    	int size = 0;
    	if(targetDepth == currentDepth) return 1;
    	else {
    		if(leftChild != null) size += leftChild.nodesAndLeafsAtDepth(targetDepth, currentDepth);
    		if(rightChild != null) size += rightChild.nodesAndLeafsAtDepth(targetDepth, currentDepth);
    	}
    	return size;
    }

    public int leafSize() {
        int count = 0;
    	if(leftChild != null) count += leftChild.leafSize();
    	if(rightChild != null) count += rightChild.leafSize();
    	return count;
    }
    
    public int size() {
        int size = 0;
    	if(leftChild != null) size += leftChild.size();
    	if(rightChild != null) size += rightChild.size();
    	return size;
    }
    
    public String toString() {
    	return "KDTreeNode [leftSplit=" + leftSplit + ", rightSplit=" + rightSplit + ", leftChild=\n" +
    			leftChild.toString() + ", rightChild=\n" + rightChild.toString() + "]";
    }
}