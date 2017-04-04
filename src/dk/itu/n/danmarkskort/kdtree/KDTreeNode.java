package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.newmodels.ParsedItem;
import dk.itu.n.danmarkskort.newmodels.ParsedNode;
import dk.itu.n.danmarkskort.newmodels.Region;

import java.util.ArrayList;

public class KDTreeNode extends KDTree {

    private KDTree leftChild;
    private KDTree rightChild;
    private float leftSplit;
    private float rightSplit;

    public KDTreeNode(ArrayList<ParsedItem> list) {
        this(listToArray(list), true);
    }

    public KDTreeNode(ParsedItem[] array, boolean sortByLon) {
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
            leftSplit = median.getFirstNode().getLon();
            for(ParsedItem item : leftArray) {
                ArrayList<ParsedNode> nodes = item.getNodes();
                for(ParsedNode node : nodes) leftSplit = node.getLon() > leftSplit ? node.getLon() : leftSplit; // til højre er værdierne størst
            }
            rightSplit = median.getFirstNode().getLon();
            for(ParsedItem item : rightArray) {
                ArrayList<ParsedNode> nodes = item.getNodes();
                for(ParsedNode node : nodes) rightSplit = node.getLon() < rightSplit ? node.getLon(): rightSplit; // til højre er værdierne størst
            }
        } else {
            leftSplit = median.getFirstNode().getLat();
            for(ParsedItem item : leftArray) {
                ArrayList<ParsedNode> nodes = item.getNodes();
                for(ParsedNode node : nodes) leftSplit = node.getLat() > leftSplit ? node.getLat() : leftSplit; // nederst er værdierne størst
            }
            rightSplit = median.getFirstNode().getLat();
            for(ParsedItem item : rightArray) {
                ArrayList<ParsedNode> nodes = item.getNodes();
                for(ParsedNode node : nodes) rightSplit = node.getLat() < rightSplit ? node.getLat() : rightSplit; // nederst er værdierne størst
            }
        }
        if(leftArray.length > DKConstants.KD_SIZE) leftChild = new KDTreeNode(leftArray, !sortByLon);
        else leftChild = new KDTreeLeaf(leftArray, this);

        if(rightArray.length > DKConstants.KD_SIZE) rightChild = new KDTreeNode(rightArray, !sortByLon);
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
    public int size() { return leftChild.size() + rightChild.size(); }
    
    public int size(Region reg) {
    	return size(reg, true);
    }
    public int size(Region reg, boolean sortByLon) {
    	int size = 0;
    	if(sortByLon) {
            if(reg.x1 < leftSplit) size += leftChild.size(reg, !sortByLon);
            if(reg.x2 > rightSplit) size += rightChild.size(reg, !sortByLon);
        }
        else {
            if(reg.y1 < leftSplit) size += leftChild.size(reg, !sortByLon);
            if(reg.y2 > rightSplit) size += rightChild.size(reg, !sortByLon);
        }
    	return size;
    }

}