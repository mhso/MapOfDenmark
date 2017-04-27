package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.kdtree.KDTree;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedRelation extends ParsedWay {

    private ArrayList<ParsedWay> inners;
    private ArrayList<ParsedWay> outers;

    public ParsedRelation(long id) {
        super(id);
        inners = new ArrayList<>();
        outers = new ArrayList<>();
    }

    public void addMember(ParsedWay item, String role) {
        if(role.equals("outer")) {
            outers.add(item);
        }
        else inners.add(item);
    }

    @Override
    public void deleteOldRefs() {
        super.deleteOldRefs();
        for(ParsedItem inner: inners) inner.deleteOldRefs();
        for(ParsedItem outer: outers) outer.deleteOldRefs();
    }

    @Override
    public ParsedNode[] getNodes() {
        int size = getNodeAmount();
        ParsedNode[] nodeArr = new ParsedNode[size];
        int i = 0;
        for(ParsedItem inner : inners) {
            for(int j = 0; j < inner.getNodes().length; j++, i++) nodeArr[i] = inner.getNodes()[j];
        }
        for(ParsedItem outer : outers) {
            for(int k = 0; k < outer.getNodes().length; k++, i++) nodeArr[i] = outer.getNodes()[k];
        }
        return nodeArr;
    }

    @Override
    public Shape getShape() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(inners.size() > 0) for(ParsedWay inner: inners) path.append(inner.getShape(), false);
        if(outers.size() > 0) for(ParsedWay outer: outers) path.append(outer.getShape(), false);
        return path;
    }

    /*
    * Connects outer ways into a coherent polygon.
    * Some ways has to have their coords reversed.
    * These reversed paths have to be created as a new instance of
    * way, as they would otherwise mess with themselves or other
    * relations, where they don't have to be reversed.
     */

    // FIXME: check if there should both be a lastNode and firstNode to check on (ie. 5 ifs in the double for loop)
    // FIXME: also, not sure if inners and nothingerners should also be connected like this?
    public void correctOuters() {
        if(outers.size() == 0) return;
        HashMap<ParsedNode, ParsedWay> tempWayMap = new HashMap<>();
        ArrayList<ParsedWay> corrected = new ArrayList<>();
        for(ParsedWay outer: outers) {
            ParsedWay candidateBefore = tempWayMap.remove(outer.getFirstNode());
            ParsedWay candidateAfter = tempWayMap.remove(outer.getLastNode());
            if(candidateBefore != null || candidateAfter != null) {
                ParsedWay tempWay = new ParsedWay();
                if(candidateBefore != null) {
                    if (outer.getFirstNode() == candidateBefore.getLastNode()) { // direction is correct
                        tempWayMap.remove(candidateBefore.getFirstNode());
                        tempWay.addNodes(candidateBefore.getNodes());
                    } else {    // direction is incorrect
                        tempWayMap.remove(candidateBefore.getLastNode());
                        tempWay.addNodes(candidateBefore.getReversedNodes());
                    }
                }
                tempWay.addNodes(outer.getNodes());
                if(candidateAfter != null && candidateAfter != candidateBefore) {
                    if (outer.getLastNode() == candidateAfter.getFirstNode()) { // direction is correct
                        tempWayMap.remove(candidateAfter.getLastNode());
                        tempWay.addNodes(candidateAfter.getNodes());
                    } else {    // direction is incorrect
                        tempWayMap.remove(candidateAfter.getFirstNode());
                        tempWay.addNodes(candidateAfter.getReversedNodes());
                    }
                }
                tempWayMap.put(tempWay.getFirstNode(), tempWay);
                tempWayMap.put(tempWay.getLastNode(), tempWay);
            }
            else {
                tempWayMap.put(outer.getFirstNode(), outer);
                tempWayMap.put(outer.getLastNode(), outer);
            }
        }
        tempWayMap.forEach((key, current) -> {
            if(key == current.getFirstNode()) corrected.add(current);
        });
        outers = corrected;
    }

    @Override
    public void nodesToCoords() {
        if(nodes != null) super.nodesToCoords();
        for(ParsedItem inner: inners) inner.nodesToCoords();
        for(ParsedItem outer: outers) outer.nodesToCoords();
    }

    @Override
    public ParsedNode getFirstNode() {
        if(nodes != null && nodes.length > 0) return nodes[0];
        else if(outers.size() > 0) return outers.get(0).getFirstNode();
        else if(inners.size() > 0) return inners.get(0).getFirstNode();
        return null;
    }

    @Override
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null && nodes.length > 0) nodeAmount = nodes.length;

    	return "ParsedRelation [" + "id=" + getID()
                + ", firstLon=" + getFirstNode().getLon()
                + ", firstLat=" + getFirstNode().getLat()
                + ", nodeAmount=" + nodeAmount
                + ", itemAmount=" + (inners.size() + outers.size()) + "]";
    }

    public int getNodeAmount() {
        int size = 0;
        for(ParsedItem inner: inners) size += inner.getNodeAmount();
        for(ParsedItem outer: outers) size += outer.getNodeAmount();
        return size;
    }

    @Override
    public double shortestDistance(ParsedNode query) {
        double shortest = Double.POSITIVE_INFINITY;
        for(ParsedItem inner: inners) {
            double distance = inner.shortestDistance(query);
            if(distance < shortest) shortest = distance;
        }
        for(ParsedItem outer: outers) {
            double distance = outer.shortestDistance(query);
            if(distance < shortest) shortest = distance;
        }
        return shortest;
    }
}
