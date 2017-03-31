package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;

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
        if(sortByLon) {
            leftSplit = median.getFirstLon();
            for(ParsedItem item : leftArray) {
                ArrayList<Float> lons = item.getLons();
                for(Float coord : lons) leftSplit = coord > leftSplit ? coord : leftSplit; // til højre er værdierne størst
            }
            rightSplit = median.getFirstLon();
            for(ParsedItem item : rightArray) {
                ArrayList<Float> lons = item.getLons();
                for(Float coord : lons) rightSplit = coord < rightSplit ? coord : rightSplit; // til højre er værdierne størst
            }
        } else {
            leftSplit = median.getFirstLat();
            for(ParsedItem item : leftArray) {
                ArrayList<Float> lats = item.getLats();
                for(Float coord : lats) leftSplit = coord > leftSplit ? coord : leftSplit; // nederst er værdierne størst
            }
            rightSplit = median.getFirstLat();
            for(ParsedItem item : rightArray) {
                ArrayList<Float> lats = item.getLats();
                for(Float coord : lats) rightSplit = coord < rightSplit ? coord : rightSplit; // nederst er værdierne størst
            }
        }
        if(leftArray.length > DKConstants.KD_SIZE) leftChild = new KDTreeNode(leftArray, this, !sortByLon);
        else leftChild = new KDTreeLeaf(leftArray, this);

        if(rightArray.length > DKConstants.KD_SIZE) rightChild = new KDTreeNode(rightArray, this, !sortByLon);
        else rightChild = new KDTreeLeaf(rightArray, this);
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }

    @Override
    public void getShapes(Region reg, MapCanvas map) { getShapes(reg, map, true); }
    @Override
    public void getShapes(Region reg, MapCanvas map,  boolean sortByLon) {
        if(sortByLon) {
            if(reg.x1 < leftSplit) leftChild.getShapes(reg, map, !sortByLon);
            if(reg.x2 > rightSplit) rightChild.getShapes(reg, map, !sortByLon);
        }
        else {
            if(reg.y1 < leftSplit) leftChild.getShapes(reg, map, !sortByLon);
            if(reg.y2 > rightSplit) rightChild.getShapes(reg, map, !sortByLon);
        }
    }
    @Override
    public void makeShapes() {
        leftChild.makeShapes();
        rightChild.makeShapes();
    }
    @Override
    public KDTree getParent() { return parent; }
    @Override
    public int size() { return leftChild.size() + rightChild.size(); }

}