package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

public class ParsedWay extends ParsedObject {

	private ArrayList<ParsedNode> nodes = new ArrayList<ParsedNode>();
	private ArrayList<Long> nodeIds = new ArrayList<Long>();
	private Shape shape = null;
	private boolean closedShape = false;
	public WayType type = WayType.DEFAULT;
	
	public ParsedNode[] getNodes() {
		return nodes.toArray(new ParsedNode[nodes.size()]);
	}
	
	public ArrayList<Long> getNodeIds() {
		return nodeIds;
	}
	
	public void addNodeId(long nodeId) {
		nodeIds.add(nodeId);
	}
	
	public void addNode(ParsedNode node) {
		nodes.add(node);
		long id = node.getId();
		if(nodeIds.contains(id)) {
			nodeIds.remove(id);
			if(isCompletelyLinked() && shape == null) createShape();
		}
	}
	
	public void createShape() {
		Path2D path = new Path2D.Float();
		ParsedNode first = get(0);
		Point2D firstPost = Util.coordinateToScreen(first.getPosition());
		path.moveTo(firstPost.getX(), firstPost.getY());
		
		for(int i=1; i<nodes.size(); i++) {
			ParsedNode node = get(i);
			Point2D post = Util.coordinateToScreen(node.getPosition());
			path.lineTo(post.getX(), post.getY());
		}
		
		determineType();
		if(closedShape) path.closePath();
		shape = path;
	}
	
	public void determineType() {
		if(attributes.containsKey("building")) {
			type = WayType.BUILDING;
			closedShape = true;
		} else if("coastline".equals(attributes.get("natural"))) {
			type = WayType.COASTLINE;
		} else if(attributes.containsKey("highway")) {
			type = WayType.HIGHWAY;
		} else if("ferry".equals(attributes.get("route")) || "navigation_line".equals(attributes.get("seamark:type"))) {
			type = WayType.ROUTE_FERRY;
		}
	}
	
	public boolean isCompletelyLinked() {
		return (nodeIds.size() == 0);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public ParsedNode get(int index) {
		return nodes.get(index);
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public void parseAttributes() {}
	
}
