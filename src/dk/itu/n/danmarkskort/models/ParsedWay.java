package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

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

    /**
     * Constructor without a param. Used to create instances of ParsedWays that are not directly from OSM data.
     */
    public ParsedWay() { this(0); }

    /**
     * Contructor.
     *
     * @param id ID parsed from OSM data.
     */
    public ParsedWay(long id) {
        this.id = id;
        nodes = null;
        name = null;
    }

    public void setName(String name) { this.name = ReuseStringObj.make(name); }
    public String getName() { return name; }

    /**
     * Adds a node (coordinate) to the Point2D.Float array of nodes (coordinate points).
     * @param node The point to be added.
     */
    public void addNode(Point2D.Float node) { addNodes(new Point2D.Float[]{node}); }

    /**
     * Adds a List fo Point2D.Floats to the collection of nodes (coordinate points) held.
     * @param nodeList
     */
    public void addNodes(List<Point2D.Float> nodeList) {
        addNodes(nodeList.toArray(new Point2D.Float[nodeList.size()]));
    }

    /**
     *
     * Creates a new array, and adds the old one's elements (if it exists) and the ones given in the param.
     *
     * @param nodeArray array of nodes (coordinate points) to be added to the collection held.
     */
    public void addNodes(Point2D.Float[] nodeArray) {
        int oldLength = nodes != null ? nodes.length : 0;
        Point2D.Float[] tempNodes = new Point2D.Float[nodeArray.length + oldLength];
        int i = 0;
        if (nodes != null) for (Point2D.Float pn : nodes) tempNodes[i++] = pn;
        for (Point2D.Float pn : nodeArray) tempNodes[i++] = pn;
        nodes = tempNodes;
    }

    @Override
    public float[] getCoords() { return coords; }

    @Override
    public Point2D.Float[] getNodes() { return nodes; }

    public long getID() { return id; }

    /**
     * Creates and returns an array that has all the nodes (coordinate points) held, in reversed order.
     * @return New array containing all the nodes (coordinate points) in reversed order.
     */
    public Point2D.Float[] getReversedNodes() {
        Point2D.Float[] revNodes = new Point2D.Float[nodes.length];
        int i = revNodes.length;
        for(Point2D.Float pn: nodes) revNodes[--i] = pn;
        return revNodes;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteOldRefs() {
        nodes = null;
    }

    /**
     * {@inheritDoc}
     */
    public Shape getShape() {
        Path2D path = new Path2D.Float();
        path.moveTo(coords[0], coords[1]);
        for (int i = 2; i < coords.length; ) {
            path.lineTo(coords[i++], coords[i++]);
        }
        return path;
    }

    /**
     * {@inheritDoc}
     */
    public void nodesToCoords() {
        coords = new float[nodes.length * 2];
        int i = 0;
        for (Point2D.Float pn : nodes) {
            coords[i++] = pn.x;
            coords[i++] = pn.y;
        }
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Point2D.Float getFirstNode() {
        if (nodes != null && nodes.length > 0) return nodes[0];
        return null;
    }

    /**
     * @return The last element in the nodeArray (if it is not null)
     */
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

    /**
     * {@inheritDoc}
     */
    public int size() {
        if(nodes != null) return nodes.length;
        else if(coords != null) return coords.length;
        return 0;
    }
}