package dk.itu.n.danmarkskort.newmodels;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

public class ParsedRelation extends ParsedItem {

    private long id;
    private ArrayList<ParsedNode> nodes;
    private ArrayList<ParsedItem> items;
    private Shape shape;

    public ParsedRelation(long id) { this.id = id; }

    public void addNodes(ArrayList<ParsedNode> nodes) { this.nodes = nodes; }

    public void addItems(ArrayList<ParsedItem> items) { this.items = items; }

    public long getID() { return id; }

    public ArrayList<ParsedNode> getNodes() {
        ArrayList<ParsedNode> nodeList = new ArrayList<>();
        if(items != null) {
            for(ParsedItem item : items) nodeList.addAll(item.getNodes());
        } else if(nodes != null) {
            return nodes;
        }
        return nodeList;
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(items != null) {
            ParsedNode lastNode = items.get(0).getLastNode();
            path.append(items.get(0).getPath(), true);
            for(int i = 1; i < items.size(); i++) {
                ParsedItem current = items.get(i);
                if(lastNode.getKey() != current.getFirstNode().getKey()) {
                    Collections.reverse(current.getNodes());
                }
                if(lastNode.getKey() == current.getFirstNode().getKey()) {
                    path.append(current.getPath(), true);
                } else {
                    path.append(current.getPath(), false);
                }
                lastNode = current.getLastNode();
            }
        }
        return path;
    }

    @Override
    public ParsedNode getFirstNode() { return null;}

    @Override
    public ParsedNode getLastNode() { return null; }

    @Override
    public float getFirstLon() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLon();
        else if(items != null) return items.get(0).getFirstLon();
        return -1;
    }

    @Override
    public float getFirstLat() {
        if(nodes != null && nodes.size() > 0) return nodes.get(0).getLat();
        else if(items != null) return items.get(0).getFirstLat();
        return -1;
    }
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null) nodeAmount = nodes.size();
    	return "";
    }
}
