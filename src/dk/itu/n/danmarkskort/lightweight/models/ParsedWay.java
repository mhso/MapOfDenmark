package dk.itu.n.danmarkskort.lightweight.models;

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
    }

    public void addNodes(ArrayList<ParsedNode> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<ParsedNode> getNodes() {
        return nodes;
    }

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
        return nodes.get(0);
    }

    @Override
    public ParsedNode getLastNode() {
        return nodes.get(nodes.size() - 1);
    }

    @Override
    public float getFirstLon() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLon();
        return -1;
    }
    @Override
    public float getFirstLat() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLat();
        return -1;
    }
}
