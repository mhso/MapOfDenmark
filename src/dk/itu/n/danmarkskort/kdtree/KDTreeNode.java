package dk.itu.n.danmarkskort.kdtree;

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
        this(listToArray(list), null, true);
    }

    public KDTreeNode(ParsedItem[] array, KDTree parent, boolean sortByLon) {
        this.parent = parent;
        createStructure(array, sortByLon);
    }

    public void createStructure(ParsedItem[] array, boolean sortByLon) {
        //  finds the median of the given list, either by lon or lat values
        ParsedItem median = QuickSelect.quickSelect(array, (array.length + 1) / 2, sortByLon);

        ParsedItem[] leftArray = new ParsedItem[(array.length + 1) / 2];
        for(int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[i];
        }

        ParsedItem[] rightArray = new ParsedItem[array.length - leftArray.length];
        for(int i = 0; i < rightArray.length; i ++) {
            rightArray[i] = array[i + leftArray.length];
        }

        // We need to know by what either lat or lon value the data set has been split
        if(sortByLon) {
            leftSplit = median.getFirstLon();
            for(ParsedItem item : leftArray) {
                ArrayList<Float> lons = item.getLons();
                for(Float f : lons) leftSplit = f > leftSplit ? f : leftSplit;
            }
            rightSplit = median.getFirstLon();
            for(ParsedItem item : rightArray) {
                ArrayList<Float> lons = item.getLons();
                for(Float f : lons) rightSplit = f > rightSplit ? f : rightSplit;
            }
        } else {
            leftSplit = median.getFirstLat();
            for(ParsedItem item : leftArray) {
                ArrayList<Float> lats = item.getLats();
                for(Float f : lats) leftSplit = f > leftSplit ? f : leftSplit;
            }
            rightSplit = median.getFirstLat();
            for(ParsedItem item : rightArray) {
                ArrayList<Float> lats = item.getLats();
                for(Float f : lats) rightSplit = f > rightSplit ? f : rightSplit;
            }
        }

        Main.log("left: " + leftSplit + " : right: " + rightSplit);

        if(leftArray.length > 1000) leftChild = new KDTreeNode(leftArray, this, !sortByLon);
        else leftChild = new KDTreeLeaf(leftArray, this);

        if(rightArray.length > 1000) rightChild = new KDTreeNode(rightArray, this, !sortByLon);
        else rightChild = new KDTreeLeaf(rightArray, this);
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }

    @Override
    public KDTree getParent() { return parent; }
    @Override
    public int size() { return leftChild.size() + rightChild.size(); }

}