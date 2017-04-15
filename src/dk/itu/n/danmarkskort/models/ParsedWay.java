package dk.itu.n.danmarkskort.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParsedWay extends ParsedItem implements Serializable{

    private long id;
    //ArrayList<ParsedNode> nodes;
    Shape shape;

    public ParsedWay() {
        this(0);
    }

    public ParsedWay(long id) {
        this.id = id;
        //nodes = new ArrayList<>();
    }

    public void addNode(ParsedNode node) { add(node); }

    public void addNodes(List<ParsedNode> nodeList) { addAll(nodeList); }

    public void setFirstNode(ParsedNode node) { set(0, node); }

    public void setLastNode(ParsedNode node) { if(size() > 0) set(size() - 1, node); }

    public ArrayList<ParsedNode> getNodes() { return this; }

    public long getID() { return id; }

    public Shape getPath() {
        return getPath(true);
    }

    public Shape getReversedPath() {
        return getPath(true);
        //return getPath(false);
    }

    @Override
    public void deleteOldRefs() {
        clear();
    }

    @Override
    public void makeShape() {
        shape = getPath();

    }

    @Override
    public Shape getShape() {
        return shape;
    }

    private Shape getPath(boolean rightWay) {
        if(shape != null) return shape;
        else {
            Path2D path = new Path2D.Float();
            if (rightWay) {
                path.moveTo(get(0).getLon(), get(0).getLat());
                for (int i = 1; i < size(); i++) {
                    path.lineTo(get(i).getLon(), get(i).getLat());
                }
            } else {
                path.moveTo(get(size() - 1).getLon(), get(size() - 1).getLat());
                for (int i = size() - 2; i >= 0; i--) {
                    path.lineTo(get(i).getLon(), get(i).getLat());
                }
            }
            shape = path;
            return shape;
        }
    }

    @Override
    public ParsedNode getFirstNode() {
        if(size() > 0) return get(0);
        return null;
    }

    public ParsedNode getLastNode() {
        if(size() > 0) return get(size() - 1);
        return null;
    }

    public ParsedNode getSecondNode() {
        if(size() > 3) return get(1);
        return null;
    }

    public ParsedNode getSecondToLastNode() {
        if(size() > 3) return get(size() - 2);
        return null;
    }
    
    public String toString() {
    	return "ParsedWay [" + "id=" + id + ", firstLon=" + getFirstNode().getLon() + ", firstLat=" + getFirstNode().getLat() + ", nodeAmount=" + size() + "]";
    }

    public void setID(long id) {
        this.id = id;
    }
}
