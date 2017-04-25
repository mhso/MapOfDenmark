package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.Region;

import java.util.ArrayList;
import java.util.List;

public class KDTreeNode<T extends KDComparable> extends KDTree<T> {

	private static final long serialVersionUID = 242589004649413322L;
	private KDTree<T> leftChild;
    private KDTree<T> rightChild;
    private float leftSplit;
    private float rightSplit;
   // private int size;

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
        for(int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[i];
        }

        KDComparable[] rightArray = new KDComparable[array.length - leftArray.length];
        for(int i = 0; i < rightArray.length; i ++) {
            rightArray[i] = array[i + leftArray.length];
        }

        if(sortByLon) {
            leftSplit = median.getFirstNode().getLon();
            for(KDComparable item : leftArray) {
                ParsedNode[] nodes = item.getNodes();
                for(ParsedNode node : nodes) leftSplit = node.getLon() > leftSplit ? node.getLon() : leftSplit; // til højre er værdierne størst
            }
            rightSplit = median.getFirstNode().getLon();
            for(KDComparable item : rightArray) {
                ParsedNode[] nodes = item.getNodes();
                for(ParsedNode node : nodes) rightSplit = node.getLon() < rightSplit ? node.getLon(): rightSplit; // til højre er værdierne størst
            }
        } else {
            // top part
            leftSplit = median.getFirstNode().getLat();
            for(KDComparable item : leftArray) {
                ParsedNode[] nodes = item.getNodes();
                for(ParsedNode node : nodes) leftSplit = node.getLat() > leftSplit ? node.getLat() : leftSplit; // nederst er værdierne størst (stadig minus, men tættere på 0)
            }
            // bottom part
            rightSplit = median.getFirstNode().getLat();
            for(KDComparable item : rightArray) {
                ParsedNode[] nodes = item.getNodes();
                for(ParsedNode node : nodes) rightSplit = node.getLat() < rightSplit ? node.getLat() : rightSplit; // nederst er værdierne størst
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

    public List<KDComparable[]> getItems(Region reg) { return getItems(reg, true); }

    public List<KDComparable[]> getItems(Region reg, boolean sortByLon) {
        List<KDComparable[]> items = new ArrayList<>();
        if(sortByLon) {
            if(reg.x1 < leftSplit) items.addAll(leftChild.getItems(reg, !sortByLon));
            if(reg.x2 > rightSplit) items.addAll(rightChild.getItems(reg, !sortByLon));
        }
        else {
            if(reg.y1 < leftSplit) items.addAll(leftChild.getItems(reg, !sortByLon));
            if(reg.y2 > rightSplit) items.addAll(rightChild.getItems(reg, !sortByLon));
        }
        return items;
    }

    public List<KDComparable[]> getAllItems() {
        List<KDComparable[]> arrList = new ArrayList<>();
        arrList.addAll(leftChild.getAllItems());
        arrList.addAll(rightChild.getAllItems());
        return arrList;
    }
}