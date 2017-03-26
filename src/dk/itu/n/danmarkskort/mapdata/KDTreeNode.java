package dk.itu.n.danmarkskort.mapdata;

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
        this(list, null, true);
    }

    public KDTreeNode(ArrayList<ParsedItem> list, KDTree parent, boolean sortByLon) {
        this.parent = parent;
        createStructure(list, sortByLon);
    }

    public void createStructure(ArrayList<ParsedItem> list, boolean sortByLon) {
        //  finds the median of the given list, either by lon or lat values
        ParsedItem median = QuickSelect.quickSelect(list, list.size() / 2, sortByLon);

        ArrayList<ParsedItem> leftList = new ArrayList<>((list.size() + 1) / 2);
        for(int i = 0; i < list.size(); i++) leftList.add(list.get(i));

        ArrayList<ParsedItem> rightList = new ArrayList<>((list.size() + 1) / 2);
        for(int i = list.size() / 2; i < list.size(); i ++) rightList.add(list.get(i));

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

        if(leftList.size() > 1000) leftChild = new KDTreeNode(leftList, this, !sortByLon);
        else leftChild = new KDTreeLeaf(leftList, this);

        if(rightList.size() > 1000) rightChild = new KDTreeNode(rightList, this, !sortByLon);
        else rightChild = new KDTreeLeaf(rightList, this);
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }
    public KDTree getParent() { return parent; }

}