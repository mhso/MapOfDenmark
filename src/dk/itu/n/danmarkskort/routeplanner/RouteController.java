package dk.itu.n.danmarkskort.routeplanner;

import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.models.WayType;

public class RouteController {
	private List<RouteVertex> customVertices;
	private List<RouteDEdge> customDirectedEdges;
	
	public RouteController(){
		customVertices = new ArrayList<>();
	}
	
	private RouteVertex makeVertex(PointFloat point){
		RouteVertex v = new RouteVertex(customVertices.size(), point);
		customVertices.add(v);
		return v;
	}
	
	public void addEdge(PointFloat fromVertex, PointFloat toVertex, int maxSpeed, boolean forwardAllowed, boolean backwardAllowed, String description, WayType wayType){
		RouteVertex from = makeVertex(fromVertex);
		RouteVertex to = makeVertex(toVertex);
		
		RouteDEdge customDirectedEdge = new RouteDEdge(from, to, maxSpeed, forwardAllowed, backwardAllowed, description, wayType);
		customDirectedEdges.add(customDirectedEdge);
	}
}
