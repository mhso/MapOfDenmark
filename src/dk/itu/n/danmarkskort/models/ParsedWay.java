package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.kdtree.KDComparable;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsedWay extends ParsedItem implements KDComparable, Serializable {

    private long id;
    Shape shape;
    ParsedNode[] nodes;
    private float[] coords;

    public ParsedWay() {
        this(0);
    }

    public ParsedWay(long id) {
        this.id = id;
        nodes = null;
    }

    public void addNode(ParsedNode node) {
        addNodes(new ParsedNode[]{node});
    }

    public void addNodes(List<ParsedNode> nodeList) {
        addNodes(nodeList.toArray(new ParsedNode[nodeList.size()]));
    }

    public void addNodes(ParsedNode[] nodeArray) {
        int oldLength = nodes != null ? nodes.length : 0;
        ParsedNode[] tempNodes = new ParsedNode[nodeArray.length + oldLength];
        int i = 0;
        if (nodes != null) for (ParsedNode pn : nodes) tempNodes[i++] = pn;
        for (ParsedNode pn : nodeArray) tempNodes[i++] = pn;
        nodes = tempNodes;
    }

    public ParsedNode[] getNodes() {
        return nodes;
    }

    public long getID() {
        return id;
    }

    ParsedNode[] getReversedNodes() {
        ParsedNode[] revNodes = new ParsedNode[nodes.length];
        int i = revNodes.length;
        for(ParsedNode pn: nodes) revNodes[--i] = pn;
        return revNodes;
    }

    @Override
    public void deleteOldRefs() {
        nodes = null;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    private Shape getPath() {
        Path2D path = new Path2D.Float();
        path.moveTo(nodes[0].getLon(), nodes[0].getLat());
        for (int i = 1; i < nodes.length; i++) {
            path.lineTo(nodes[i].getLon(), nodes[i].getLat());
        }
        shape = path;
        return shape;
    }

    public void nodesToCoords() {
        coords = new float[nodes.length * 2];
        int i = 0;
        for (ParsedNode pn : nodes) {
            coords[i++] = pn.getLon();
            coords[i++] = pn.getLat();
        }
        nodes = null;
    }

    public ParsedNode getFirstNode() {
        if (nodes != null && nodes.length > 0) return nodes[0];
        return null;
    }

    public ParsedNode getLastNode() {
        if (nodes != null && nodes.length > 0) return nodes[nodes.length - 1];
        return null;
    }

    public String toString() {
        return "ParsedWay [" + "id=" + id
                + ", firstLon=" + getFirstNode().getLon()
                + ", firstLat=" + getFirstNode().getLat()
                + ", nodeAmount=" + nodes.length + "]";
    }

    public void setID(long id) { this.id = id; }
}