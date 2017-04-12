package dk.itu.n.danmarkskort.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParsedWay extends ParsedItem implements Serializable{

    private long id;
    ArrayList<ParsedNode> nodes;
    Shape shape;

    public ParsedWay() {
        this(0);
    }

    public ParsedWay(long id) {
        this.id = id;
        nodes = new ArrayList<>();
    }

    public void addNode(ParsedNode node) { nodes.add(node); }

    public void addNodes(List<ParsedNode> nodeList) { nodes.addAll(nodeList); }

    public void setFirstNode(ParsedNode node) { nodes.set(0, node); }

    public void setLastNode(ParsedNode node) { if(nodes.size() > 0) nodes.set(nodes.size() - 1, node); }

    public ArrayList<ParsedNode> getNodes() { return nodes; }

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
        nodes = null;
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
                path.moveTo(nodes.get(0).getLon(), nodes.get(0).getLat());
                for (int i = 1; i < nodes.size(); i++) {
                    path.lineTo(nodes.get(i).getLon(), nodes.get(i).getLat());
                }
            } else {
                path.moveTo(nodes.get(nodes.size() - 1).getLon(), nodes.get(nodes.size() - 1).getLat());
                for (int i = nodes.size() - 2; i >= 0; i--) {
                    path.lineTo(nodes.get(i).getLon(), nodes.get(i).getLat());
                }
            }
            shape = path;
            return shape;
        }
    }

    @Override
    public ParsedNode getFirstNode() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0);
        return null;
    }

    public ParsedNode getLastNode() {
        if(nodes.size() > 0) return nodes.get(nodes.size() - 1);
        return null;
    }

    public ParsedNode getSecondNode() {
        if(nodes != null && nodes.size() > 3) return nodes.get(1);
        return null;
    }

    public ParsedNode getSecondToLastNode() {
        if(nodes != null && nodes.size() > 3) return nodes.get(nodes.size() - 2);
        return null;
    }
    
    public String toString() {
    	return "ParsedWay [" + "id=" + id + ", firstLon=" + getFirstNode().getLon() + ", firstLat=" + getFirstNode().getLat() + ", nodeAmount=" + nodes.size() + "]";
    }

    public void setID(long id) {
        this.id = id;
    }
}
