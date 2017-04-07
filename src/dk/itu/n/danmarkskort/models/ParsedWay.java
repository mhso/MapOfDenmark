package dk.itu.n.danmarkskort.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedWay extends ParsedItem{

    private long id;
    ArrayList<ParsedNode> nodes;
    Shape shape;

    public ParsedWay() {
        nodes = new ArrayList<>();
    }

    public ParsedWay(long id) {
        this.id = id;
        nodes = new ArrayList<>();
    }

    public void addNode(ParsedNode node) { nodes.add(node); }

    public void setFirstNode(ParsedNode node) { nodes.set(0, node); }

    public void setLastNode(ParsedNode node) { if(nodes.size() > 0) nodes.set(nodes.size() - 1, node); }

    void addNodes(ArrayList<ParsedNode> nodes) { this.nodes.addAll(nodes); }

    public ArrayList<ParsedNode> getNodes() { return nodes; }

    public long getID() { return id; }

    public Path2D getPath() {
        return getPath(true);
    }

    public Path2D getReversedPath() {
        return getPath(false);
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

    private Path2D getPath(boolean rightWay) {
        Path2D path = new Path2D.Float();
        if(rightWay) {
            path.moveTo(nodes.get(0).getLon(), nodes.get(0).getLat());
            for (int i = 1; i < nodes.size(); i++) {
                path.lineTo(nodes.get(i).getLon(), nodes.get(i).getLat());
            }
        }
        else {
            path.moveTo(nodes.get(nodes.size() - 1).getLon(), nodes.get(nodes.size() - 1).getLat());
            for (int i = nodes.size() - 2; i >= 0; i--) {
                path.lineTo(nodes.get(i).getLon(), nodes.get(i).getLat());
            }
        }
        return path;
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
    
    public String toString() {
    	return "ParsedWay [" + "id=" + id + ", firstLon=" + getFirstNode().getLon() + ", firstLat=" + getFirstNode().getLat() + ", nodeAmount=" + nodes.size() + "]";
    }
}
