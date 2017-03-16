package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

public class ParsedWay extends ParsedObject {

	private ArrayList<ParsedNode> nodes = new ArrayList<ParsedNode>();
	private ArrayList<Long> nodeIds = new ArrayList<Long>();
	private Shape shape = null;
	
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
		
		shape = path;
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
