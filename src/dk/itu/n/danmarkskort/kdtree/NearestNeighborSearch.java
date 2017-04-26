package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.ParsedNode;

public class NearestNeighborSearch<T extends KDComparable> {
	private T nearestItem;
	private ParsedNode nearestNode;
	
	public NearestNeighborSearch(KDTree<T> tree, ParsedNode query) {
		nearestItem = tree.nearest(query);
		double dist = Double.POSITIVE_INFINITY;
		for(ParsedNode node : nearestItem.getNodes()) {
			if(Util.calcDistance(node, query) < dist) {
				dist = Util.calcDistance(node, query);
				nearestNode = node;
			}
		}
	}
	
	public T getNearestItem() {
		return nearestItem;
	}
	
	public ParsedNode getNearestNode() {
		return nearestNode;
	}
}
