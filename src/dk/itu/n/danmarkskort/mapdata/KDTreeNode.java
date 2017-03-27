package dk.itu.n.danmarkskort.mapdata;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.util.ArrayList;

public class KDTreeNode extends KDTree {

    private transient final int maxData = 1000;

    private KDTree leftChild;
    private KDTree rightChild;
    private float leftSplit;
    private float rightSplit;
    private KDTree parent; // should this just be deleted? At least now we can move both up and down

    public KDTreeNode(ArrayList<ParsedItem> list) {
        this(KDUtil.listToArray(list), null, true);
    }

    public KDTreeNode(ParsedItem[] array, KDTree parent, boolean sortByLon) {
        this.parent = parent;
        createStructure(array, sortByLon);
    }
 int count = 0;
    public void createStructure(ParsedItem[] array, boolean sortByLon) {
        //  finds the median of the given list, either by lon or lat values
        ParsedItem median = QuickSelect.quickSelect(array, array.length / 2, sortByLon);

        ParsedItem[] leftArray = new ParsedItem[(array.length + 1) / 2];
        for(int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[i];
        }
        ParsedItem[] rightArray = new ParsedItem[(array.length + 1) / 2];
        for(int i = 0; i < rightArray.length; i ++) {
            rightArray[i] = array[i + rightArray.length - 1];
            //Main.log(count++ + " " + i + " " + rightArray.length + " " + (i + rightArray.length));
        }

        // We need to know by what either lat or lon value the data set has been split
        if(sortByLon) {
            leftSplit = median.getFirstLon();
            rightSplit = leftSplit;
        } else {
            leftSplit = median.getFirstLat();
            rightSplit = leftSplit;
        }
        // ^^^ Here we should loop through both lists' ways' coordinates, and check whether they are "outside" of split point
        // and then save the max_value of the one farthest away
        // this will, however, slow down the creation of KDTree
        // but, it will ensure that we get all the elements we want to paint when we are drawing!

        if(leftArray.length > 1000) leftChild = new KDTreeNode(leftArray, this, !sortByLon);
        else leftChild = new KDTreeLeaf(leftArray, this);

        if(rightArray.length > 1000) rightChild = new KDTreeNode(rightArray, this, !sortByLon);
        else rightChild = new KDTreeLeaf(rightArray, this);
        Main.log("finished a KD run");
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }
    public KDTree getParent() { return parent; }

}