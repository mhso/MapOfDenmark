package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;

import java.awt.*;
import java.awt.geom.Path2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
        ArrayList<ParsedNode> arrList = new ArrayList<>();
        if(inners.size() > 0 && outers.size() > 0) {
            for(ParsedItem outer: outers) arrList.addAll(Arrays.asList(outer.getNodes()));
            for(ParsedItem inner: inners) arrList.addAll(Arrays.asList(inner.getNodes()));
        }
        return arrList.toArray(new ParsedNode[arrList.size()]);
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

        ArrayList<ParsedWay> corrected = new ArrayList<>();
        ParsedWay tempWay = null;
        ParsedWay current = outers.get(0);
        current.getLastNode();
        if(outers.size() > 1) {
            for(int i = 1; i < outers.size(); i++) {
                for(int j = i; j < outers.size(); j++ ) {
                    ParsedWay candidate = outers.get(j);
                    // checks if candidate is a match, with a correct path direction
                    if(current.getLastNode() == candidate.getFirstNode()) {
                        if(tempWay == null) {
                            tempWay = new ParsedWay();
                            tempWay.addNodes(current.getNodes());
                        }
                        tempWay.addNodes(candidate.getNodes());
                        current = candidate;
                        Collections.swap(outers, i, j);
                        break;
                    }
                    // checks if candiate is a match, with an incorrect direction
                    else if(current.getLastNode() == candidate.getLastNode()) {
                        if(tempWay == null) {
                            tempWay = new ParsedWay();
                            tempWay.addNodes(current.getNodes());
                        }
                        tempWay.addNodes(candidate.getReversedNodes());
                        current = candidate;
                        Collections.swap(outers, i, j);
                        break;
                    }
                    // if no candidates are matches, append the first candidate without connecting,
                    // and set it's lastNode to check new candidates against
                    else if(j == outers.size() - 1) {
                        if(tempWay == null) corrected.add(current);
                        else {
                            corrected.add(tempWay);
                            tempWay = null;
                            current = outers.get(i);
                        }
                    }
                }
            }
        }
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
}
