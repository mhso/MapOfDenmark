package dk.itu.n.danmarkskort.newmodels;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedWay extends ParsedItem{

    private long id;
    private ArrayList<ParsedNode> nodes;
    private Shape shape;

    public ParsedWay() {}

    public ParsedWay(long id) {
        this.id = id;
        nodes = new ArrayList<>();
    }

    public void addNode(ParsedNode node) { nodes.add(node); }

    public ArrayList<ParsedNode> getNodes() { return nodes; }

    public long getID() { return id; }

    public Path2D getPath() {
        Path2D path = new Path2D.Float();
        path.moveTo(nodes.get(0).getLon(), nodes.get(0).getLat());
        for(int i = 1; i < nodes.size(); i++) {
        	path.lineTo(nodes.get(i).getLon(), nodes.get(i).getLat());
        }
        return path;
    }

    @Override
    public ParsedNode getFirstNode() {
        if(nodes.size() > 0) return nodes.get(0);
        return null;
    }

    @Override
    public ParsedNode getLastNode() {
        if(nodes.size() > 0) return nodes.get(nodes.size() - 1);
        return null;
    }
    
    public String toString() {
    	return "ParsedWay [" + "id=" + id + ", firstLon=" + getFirstNode().getLon() + ", firstLat=" + getFirstNode().getLat() + ", nodeAmount=" + nodes.size() + "]";
    }
}
