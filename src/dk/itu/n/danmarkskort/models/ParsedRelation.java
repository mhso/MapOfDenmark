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
        nodes = null;
        inners = null;
        outers = null;
    }

    public void makeShape() {
        shape = getPath();
    }

    public ArrayList<ParsedWay> getInners() { return inners; }
    public ArrayList<ParsedWay> getOuters() { return outers; }

    public ParsedNode[] getNodes() {
        ArrayList<ParsedNode> arrList = new ArrayList<>();
        if(inners.size() > 0 && outers.size() > 0) {
            for(ParsedItem outer: outers) arrList.addAll(Arrays.asList(outer.getNodes()));
            for(ParsedItem inner: inners) arrList.addAll(Arrays.asList(inner.getNodes()));
        }
        return arrList.toArray(new ParsedNode[arrList.size()]);
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(inners.size() > 0) {
            for(ParsedWay item : inners) path.append(item.getPath(), false);
        }
        if(outers.size() > 0) path.append(connectItems(outers), false);
        return path;
    }

    /*
    * Connects outer ways into a coherent polygon
    * Some ways has to have their nodelist reversed
     */
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
        if(nodes != null && nodes.length > 0) return nodes[0];
        else if(outers.size() > 0) return outers.get(0).getFirstNode();
        else if(inners.size() > 0) return inners.get(0).getFirstNode();
        return null;
    }
    
    public String toString() {
    	int nodeAmount = 0;
    	if(nodes != null && nodes.length > 0) nodeAmount = nodes.length;

    	return "ParsedRelation [" + "id=" + getID()
                //+ ", firstLon=" + getFirstNode().getLon()
                //+ ", firstLat=" + getFirstNode().getLat()
                + ", nodeAmount=" + nodeAmount
                + ", itemAmount=" + (inners.size() + outers.size()) + "]";
    }
}
