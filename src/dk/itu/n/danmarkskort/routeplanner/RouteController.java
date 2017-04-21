package dk.itu.n.danmarkskort.routeplanner;

import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.models.PointFloat;
import dk.itu.n.danmarkskort.models.WayType;

public class RouteController {
	private int vertexCount;
	private List<RouteEdge> routeEdges;
	
	public RouteController(){
		vertexCount = 0;
		routeEdges = new ArrayList<RouteEdge>();
	}
	
	/**
	 * Creates a RouteVertex with a uniq Id based on the position i the ArrayList
	 * @param point
	 * @return A Vertex object
	 */
	private RouteVertex makeVertex(PointFloat point){
		RouteVertex vertex = new RouteVertex(vertexCount, point);
		vertexCount++;
		return vertex;
	}
	
	public void addEdge(PointFloat fromVertex, PointFloat toVertex, int maxSpeed, 
			boolean forwardAllowed, boolean backwardAllowed, String description, WayType wayType){
		
		RouteEdge edge = new RouteEdge(makeVertex(fromVertex), makeVertex(toVertex), maxSpeed, 
				forwardAllowed, backwardAllowed, description, wayType);
		
		routeEdges.add(edge);
	}
	
	public void makeGraph(){
		RouteGraph routeGraph =  new RouteGraph(vertexCount);
		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge);
	}
	
	public void cleanUp(){
		vertexCount = 0;
		routeEdges = null;
	}
}
