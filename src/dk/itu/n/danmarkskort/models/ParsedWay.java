package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTree;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

public class ParsedWay extends ParsedItem implements KDComparable, Serializable {

	private static final long serialVersionUID = -3077017431818142936L;
	private long id;
    Point2D.Float[] nodes;
    private float[] coords;
    private String name;

    public ParsedWay() {
        this(0);
    }

    public ParsedWay(long id) {
        this.id = id;
        nodes = null;
        name = null;
    }

    public void setName(String name) { this.name = ReuseStringObj.make(name); }
    public String getName() { return name; }

    public void addNode(Point2D.Float node) { addNodes(new Point2D.Float[]{node}); }

    public void addNodes(List<Point2D.Float> nodeList) {
        addNodes(nodeList.toArray(new Point2D.Float[nodeList.size()]));
    }

    @Override
    public float[] getCoords() {
        return coords;
    }

    public void addNodes(Point2D.Float[] nodeArray) {
        int oldLength = nodes != null ? nodes.length : 0;
        Point2D.Float[] tempNodes = new Point2D.Float[nodeArray.length + oldLength];
        int i = 0;
        if (nodes != null) for (Point2D.Float pn : nodes) tempNodes[i++] = pn;
        for (Point2D.Float pn : nodeArray) tempNodes[i++] = pn;
        nodes = tempNodes;
    }

    @Override
    public Point2D.Float[] getNodes() { return nodes; }

    public long getID() { return id; }

    Point2D.Float[] getReversedNodes() {
        Point2D.Float[] revNodes = new Point2D.Float[nodes.length];
        int i = revNodes.length;
        for(Point2D.Float pn: nodes) revNodes[--i] = pn;
        return revNodes;
    }

    public void deleteOldRefs() {
        nodes = null;
    }

    public Shape getShape() {
        Path2D path = new Path2D.Float();
        path.moveTo(coords[0], coords[1]);
        for (int i = 2; i < coords.length; ) {
            path.lineTo(coords[i++], coords[i++]);
        }
        return path;
    }

    public void nodesToCoords() {
        coords = new float[nodes.length * 2];
        int i = 0;
        for (Point2D.Float pn : nodes) {
            coords[i++] = pn.x;
            coords[i++] = pn.y;
        }
    }

    @Override
    public Point2D.Float getFirstNode() {
        if (nodes != null && nodes.length > 0) return nodes[0];
        return null;
    }

    public Point2D.Float getLastNode() {
        if (nodes != null && nodes.length > 0) return nodes[nodes.length - 1];
        return null;
    }

    public String toString() {
        return "ParsedWay [" + "id=" + id
                + ", firstLon=" + getFirstNode().getX()
                + ", firstLat=" + getFirstNode().getY()
                + ", nodeAmount=" + nodes.length + "]";
    }

    public void setID(long id) { this.id = id; }

    public int size() {
        if(nodes != null) return nodes.length;
        else if(coords != null) return coords.length;
        return 0;
    }
}