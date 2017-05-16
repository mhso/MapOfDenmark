package dk.itu.n.danmarkskort.backend;

import java.io.Serializable;

import dk.itu.n.danmarkskort.address.AddressHolder;
import dk.itu.n.danmarkskort.address.Housenumber;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.models.ParsedData;
import dk.itu.n.danmarkskort.routeplanner.RouteEdge;
import dk.itu.n.danmarkskort.routeplanner.RouteGraph;

public class BinaryWrapper implements Serializable {
	private static final long serialVersionUID = -7588730983362489371L;
	
	private ParsedData model;
	private AddressHolder addressHolder;
	private RouteGraph routeGraph;
	private KDTree<RouteEdge> edgeTree;
	private KDTree<Housenumber> housenumberTree;
	
	public void setModel(ParsedData model) {
		this.model = model;
	}
	
	public ParsedData getModel() {
		return model;
	}
	
	public void setAddressHolder(AddressHolder addressHolder){
		this.addressHolder = addressHolder;
	}
	
	public AddressHolder getAddressHolder(){
		return addressHolder;
	}
	
	public void setRouteGraph(RouteGraph graph) {
		routeGraph = graph;
	}
	
	public RouteGraph getRouteGraph() {
		return routeGraph;
	}
	
	public void setEdgeTree(KDTree<RouteEdge> edgeTree) {
		this.edgeTree = edgeTree;
	}
	
	public KDTree<RouteEdge> getEdgeTree() {
		return edgeTree;
	}
	
	public void setHousenumberTree(KDTree<Housenumber> housenumberTree) {
		this.housenumberTree = housenumberTree;
	}
	
	public KDTree<Housenumber> getHousenumberTree() {
		return housenumberTree;
	}
}
