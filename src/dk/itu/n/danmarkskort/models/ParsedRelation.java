package dk.itu.n.danmarkskort.models;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

public class ParsedRelation extends ParsedWay {

    private long id;
    private ArrayList<ParsedWay> inners;
    private ArrayList<ParsedWay> outers;

    public ParsedRelation(long id) {
        this.id = id;
        inners = new ArrayList<>();
        outers = new ArrayList<>();
    }

    public void addMember(ParsedWay item, String role) {
        if(role.equals("outer")) {
            outers.add(item);
        }
        else inners.add(item);
    }

    public void deleteOldRefs() {
        super.deleteOldRefs();
        inners = null;
        outers = null;
    }

    public void makeShape() {
        shape = getPath();
    }

    public long getID() { return id; }

    public ArrayList<ParsedWay> getInners() { return inners; }
    public ArrayList<ParsedWay> getOuters() { return outers; }

    public ArrayList<ParsedNode> getNodes() {
        ArrayList<ParsedNode> nodeList = new ArrayList<>();
        if(inners.size() > 0 || outers.size() > 0) {
            for(ParsedWay inner  : inners) nodeList.addAll(inner.getNodes());
            for(ParsedWay outer : outers) nodeList.addAll(outer.getNodes());
        } else if(nodes != null) {
            return nodes;
        }
        return nodeList;
    }

    @Override
    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(inners.size() > 0) {
            for(ParsedWay item : inners) path.append(item.getPath(), false);
        }
        if(outers.size() > 0) path.append(connectItems(outers), false);
        return path;
    }

    @Override
    public Path2D getReversedPath() {
        return getPath();
        // This is not correct, but I don't think its an issue.
        // relations in relations is only something we have with busroutes and the likes
        // and that's not something we are displaying at the moment, and probably never will
    }

    private Path2D connectItems(ArrayList<ParsedWay> list) {
        Path2D path = new Path2D.Float();
        path.append(list.get(0).getPath(), false);
        ParsedNode lastNode = list.get(0).getLastNode();
        if(list.size() > 1) {
            for(int i = 1; i < list.size(); i++) {
                for(int j = i; j < list.size(); j++ ) {
                    if(lastNode == list.get(j).getFirstNode()) {
                        path.append(list.get(j).getPath(), true);
                        lastNode = list.get(j).getLastNode();
                        Collections.swap(list, i, j);
                        break;
                    }
                    else if(lastNode == list.get(j).getLastNode()) {
                        path.append(list.get(j).getReversedPath(), true);
                        lastNode = list.get(j).getFirstNode();
                        Collections.swap(list, i, j);
                        break;
                    }
                    else if(j == list.size() - 1) {
                        path.append(list.get(i).getPath(), false);
                        lastNode = list.get(i).getLastNode();
                    }
                }
            }
        }
        return path;
    }

    @Override
    public ParsedNode getFirstNode() {
        if(nodes.size() > 0) return nodes.get(0);
        else if(outers.size() > 0) return outers.get(0).getFirstNode();
        else if(inners.size() > 0) return inners.get(0).getFirstNode();
        return null;
    }

    @Override
    public ParsedNode getLastNode() {
        if(nodes.size() > 0) return nodes.get(nodes.size() - 1);
        else if(outers.size() > 0) return outers.get(outers.size() - 1).getLastNode();
        else if(inners.size() > 0) return inners.get(inners.size() - 1).getLastNode();
        return null;
    }
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null) nodeAmount = nodes.size();

    	return "ParsedRelation [" + "id=" + id 	+ ", firstLon=" + getFirstNode().getLon() + ", firstLat="
                + getFirstNode().getLat() + ", nodeAmount=" + nodeAmount
                + ", itemAmount=" + (inners.size() + outers.size()) + "]";
    }
}
