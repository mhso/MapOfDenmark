package dk.itu.n.danmarkskort.models;

import java.util.ArrayList;

public class ParsedWay extends ParsedObject {

	private ArrayList<ParsedNode> nodes = new ArrayList<ParsedNode>();
	private ArrayList<Long> nodeIds = new ArrayList<Long>();
	
	public void addNode(ParsedNode node) {
		nodes.add(node);
	}

	public ParsedNode[] getNodes() {
		return nodes.toArray(new ParsedNode[nodes.size()]);
	}
	
	public ArrayList<Long> getNodeIds() {
		return nodeIds;
	}
	
	public void parseAttributes() {}
	
}
