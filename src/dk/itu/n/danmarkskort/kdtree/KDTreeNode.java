package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import java.util.ArrayList;

public class KDTreeNode extends KDTree {

	private static final long serialVersionUID = 242589004649413322L;
	private KDTree leftChild;
    private KDTree rightChild;
    private float leftSplit;
    private float rightSplit;
    private int size;

    public KDTreeNode(ArrayList<ParsedItem> list) {
        this(listToArray(list), true);
    }

    private KDTreeNode(ParsedItem[] array, boolean sortByLon) {
        createStructure(array, sortByLon);
        size = array.length;
    }

    private void createStructure(ParsedItem[] array, boolean sortByLon) {
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
        else leftChild = new KDTreeLeaf(leftArray);

        if(rightArray.length > DKConstants.KD_SIZE) rightChild = new KDTreeNode(rightArray, !sortByLon);
        else rightChild = new KDTreeLeaf(rightArray);
    }

    public KDTree getRightChild() { return rightChild; }
    public KDTree getLeftChild() { return leftChild; }
    public float getLeftSplit() { return leftSplit; }
    public float getRightSplit() { return rightSplit; }

    @Override
    public ArrayList<Shape> getShapes(Region reg) { return getShapes(reg, true); }
    @Override
    public ArrayList<Shape> getShapes(Region reg,  boolean sortByLon) {
        ArrayList<Shape> shapes = new ArrayList<>(size);
        if(sortByLon) {
            if(reg.x1 < leftSplit) shapes.addAll(leftChild.getShapes(reg, !sortByLon));
            if(reg.x2 > rightSplit) shapes.addAll(rightChild.getShapes(reg, !sortByLon));
        }
        else {
            if(reg.y1 < leftSplit) shapes.addAll(leftChild.getShapes(reg, !sortByLon));
            if(reg.y2 > rightSplit) shapes.addAll(rightChild.getShapes(reg, !sortByLon));
        }
        return shapes;
    }

    @Override
    public void makeShapes() {
        leftChild.makeShapes();
        rightChild.makeShapes();
    }

    @Override
    public void deleteOldRefs() {
        leftChild.deleteOldRefs();
        rightChild.deleteOldRefs();
    }

    @Override
    public int size() { return size; }

    /*
    @Override
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
    }*/

}