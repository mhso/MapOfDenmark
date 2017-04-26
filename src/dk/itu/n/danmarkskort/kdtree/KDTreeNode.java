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
        //  finds the median of the given list, either by lon or lat values
        KDComparable median = QuickSelect.quickSelect(array, (array.length + 1) / 2, sortByLon);

        KDComparable[] leftArray = new KDComparable[(array.length + 1) / 2];
        for(int i = 0; i < leftArray.length; i++) leftArray[i] = array[i];

        KDComparable[] rightArray = new KDComparable[array.length - leftArray.length];
        for(int i = 0; i < rightArray.length; i ++) rightArray[i] = array[i + leftArray.length];

        if(sortByLon) {
            // left side
            leftSplit = median.getFirstNode().getLon();
            for(KDComparable item : leftArray) {
                for(ParsedNode node : item.getNodes()) leftSplit = node.getLon() > leftSplit ? node.getLon() : leftSplit; // til højre er værdierne størst
            }
            // right side
            rightSplit = median.getFirstNode().getLon();
            for(KDComparable item : rightArray) {
                for(ParsedNode node : item.getNodes()) rightSplit = node.getLon() < rightSplit ? node.getLon(): rightSplit; // til højre er værdierne størst
            }
        } else {
            // top part
            leftSplit = median.getFirstNode().getLat();
            for(KDComparable item : leftArray) {
                for(ParsedNode node : item.getNodes()) leftSplit = node.getLat() > leftSplit ? node.getLat() : leftSplit; // nederst er værdierne størst (stadig minus, men tættere på 0)
            }
            // bottom part
            rightSplit = median.getFirstNode().getLat();
            for(KDComparable item : rightArray) {
                for(ParsedNode node : item.getNodes()) rightSplit = node.getLat() < rightSplit ? node.getLat() : rightSplit; // nederst er værdierne størst
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
            if(reg.x1 < leftSplit) items.addAll(leftChild.getItems(reg, false));
            if(reg.x2 > rightSplit) items.addAll(rightChild.getItems(reg, false));
        }
        else {
            if(reg.y1 < leftSplit) items.addAll(leftChild.getItems(reg, true));
            if(reg.y2 > rightSplit) items.addAll(rightChild.getItems(reg, true));
        }
        return items;
    }

    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        arrList.addAll(leftChild.getAllItems());
        arrList.addAll(rightChild.getAllItems());
        return arrList;
    }

    protected T nearest(ParsedNode query, double currentShortest, boolean sortByLon) {
        double nearestPossibleLT, nearestPossibleRB;
        double shortest = currentShortest;
        T candidateLT = null, candidateRB = null;

        if(sortByLon) {
            if(query.getLon() < leftSplit) nearestPossibleLT = 0;
            else nearestPossibleLT = calcDistance(query, new ParsedNode(leftSplit, query.getLat()));

            if(query.getLon() > rightSplit) nearestPossibleRB = 0;
            else nearestPossibleRB = calcDistance(query, new ParsedNode(rightSplit, query.getLat()));
        } else {
            if(query.getLon() < leftSplit) nearestPossibleLT = 0;
            else nearestPossibleLT = calcDistance(query, new ParsedNode(query.getLon(), leftSplit));

            if(query.getLat() > rightSplit) nearestPossibleRB = 0;
            else nearestPossibleRB = calcDistance(query, new ParsedNode(query.getLon(), rightSplit));
        }

        if(shortest < nearestPossibleLT && shortest < nearestPossibleRB) {
            return null;
        }

        if(nearestPossibleLT == 0) {
            candidateLT = leftChild.nearest(query, shortest, !sortByLon);
            shortest = candidateLT.shortestDistance(query) < shortest ? candidateLT : shortest;
        }
        if(candidateLT != null && nearestPossibleRB > candidateLT.shortestDistance(query)) return candidateLT;

        if(nearestPossibleRB == 0) {
            candidateRB = rightChild.nearest(query, shortest, !sortByLon);
        }
        if(candidateRB != null && nearestPossibleLT > candidateRB.shortestDistance(query)) return candidateRB;

        if(nearestPossibleLT > 0) {
            candidateLT = leftChild.nearest(query, shortest, !sortByLon);
        }
        if(candidateLT != null && nearestPossibleRB > candidateLT.shortestDistance(query)) return candidateLT;

        if(nearestPossibleRB > 0) {
            candidateRB = rightChild.nearest(query, shortest, !sortByLon);
        }
        if(candidateRB != null && nearestPossibleLT > candidateRB.shortestDistance(query)) return candidateRB;

        if(candidateLT != null && candidateRB != null) {
            return candidateLT.shortestDistance(query) < candidateRB.shortestDistance(query) ? candidateLT : candidateRB;
        }

        Main.log("This shouldnt happen, right?");
        return null;
    }
}