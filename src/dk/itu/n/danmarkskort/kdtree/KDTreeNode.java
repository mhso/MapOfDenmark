package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.ParsedNode;
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
        this(KDTree.listToArray(list), true);
    }

    private KDTreeNode(KDComparable[] arr, boolean sortByLon) {
        createStructure(arr, sortByLon);
    }

    private void createStructure(KDComparable[] array, boolean sortByLon) {

        if(array.length < 2) {
            leftChild = new KDTreeLeaf<T>(array);
            rightChild = null;
            return;
        }

        //  finds the median of the given list, either by lon or lat values
        KDComparable median = QuickSelect.quickSelect(array, (array.length + 1) / 2, sortByLon);

        KDComparable[] leftArray = new KDComparable[(array.length + 1) / 2];
        for(int i = 0; i < leftArray.length; i++) leftArray[i] = array[i];

        KDComparable[] rightArray = new KDComparable[array.length - leftArray.length];
        for(int i = 0; i < rightArray.length; i ++) rightArray[i] = array[i + leftArray.length];

        if(sortByLon) {
            // left side
            leftSplit = median.getFirstNode().x;
            for(KDComparable item : leftArray) {
                for(Point2D.Float node : item.getNodes()) leftSplit = node.x > leftSplit ? node.x : leftSplit; // til højre er værdierne størst
            }
            // right side
            rightSplit = median.getFirstNode().x;
            for(KDComparable item : rightArray) {
                for(Point2D.Float node : item.getNodes()) rightSplit = node.x < rightSplit ? node.x: rightSplit; // til højre er værdierne størst
            }
        } else {
            // top part
            leftSplit = median.getFirstNode().y;
            for(KDComparable item : leftArray) {
                for(Point2D.Float node : item.getNodes()) leftSplit = node.y > leftSplit ? node.y : leftSplit; // nederst er værdierne størst (stadig minus, men tættere på 0)
            }
            // bottom part
            rightSplit = median.getFirstNode().y;
            for(KDComparable item : rightArray) {
                for(Point2D.Float node : item.getNodes()) rightSplit = node.y < rightSplit ? node.y : rightSplit; // nederst er værdierne størst
            }
        }
        if(leftArray.length > DKConstants.KD_SIZE) leftChild = new KDTreeNode<>(leftArray, !sortByLon);
        else leftChild = new KDTreeLeaf<>(leftArray);

        if(rightArray.length > DKConstants.KD_SIZE) rightChild = new KDTreeNode<>(rightArray, !sortByLon);
        else rightChild = new KDTreeLeaf<>(rightArray);
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }

    public List<KDComparable[]> getItems(Region reg) {
        return getItems(reg, true);
    }

    public List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        List<KDComparable[]> items = new ArrayList<>();
        if(sortByLon) {
            if(reg.x1 < leftSplit && leftChild != null) items.addAll(leftChild.getItems(reg, false));
            if(reg.x2 > rightSplit && rightChild != null) items.addAll(rightChild.getItems(reg, false));
        }
        else {
            if(reg.y1 < leftSplit && leftChild != null) items.addAll(leftChild.getItems(reg, true));
            if(reg.y2 > rightSplit && rightChild != null) items.addAll(rightChild.getItems(reg, true));
        }
        return items;
    }

    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        if(leftChild != null) arrList.addAll(leftChild.getAllItems());
        if(rightChild != null) arrList.addAll(rightChild.getAllItems());
        return arrList;
    }

    protected T nearest(Point2D.Float query, double currentShortest, boolean sortByLon) {
        double nearestPossibleLT, nearestPossibleRB;
        double shortest = currentShortest;
        T candidate = null;

        // calculate the nearest possible candidate from either side
        if(sortByLon) {
            nearestPossibleLT = (query.x < leftSplit) ? 0 : calcDistance(query, new Point2D.Float(leftSplit, query.y));
            nearestPossibleRB = (query.x > rightSplit) ? 0 : calcDistance(query, new Point2D.Float(rightSplit, query.y));
        } else { // FIXME: is query.x correct?
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
}