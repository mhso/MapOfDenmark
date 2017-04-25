package dk.itu.n.danmarkskort.routeplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.itu.n.danmarkskort.models.PointFloat;
import dk.itu.n.danmarkskort.models.ReuseRouteEdgeMetaObj;
import dk.itu.n.danmarkskort.models.WayType;

public class RouteController {
	private int vertexCount;
	private List<RouteEdge> routeEdges;
	private List<RouteVertex> vertices;
	private RouteGraph routeGraph;
	
	public RouteController(){
		vertexCount = 0;
		routeEdges = new ArrayList<RouteEdge>();
		vertices = new ArrayList<RouteVertex>();
		routeGraph = null;
	}
	
	/**
	 * Creates a RouteVertex with a unique Id based on the position i the ArrayList
	 * @param point
	 * @return A Vertex object
	 */
	public RouteVertex makeVertex(float lon, float lat){
		RouteVertex vertex = new RouteVertex(vertexCount, new PointFloat(lon, lat));
		vertices.add(vertex);
		vertexCount++;
		return vertex;
	}
	
	public void addEdge(RouteVertex fromVertex, RouteVertex toVertex, Integer maxSpeed,
			boolean forwardAllowed, boolean backwardAllowed, boolean carsAllowed, boolean bikesAllowed, String description){
		RouteEdgeMeta routeEdgeMeta = new RouteEdgeMeta(maxSpeed, forwardAllowed, backwardAllowed,
				carsAllowed, bikesAllowed);
		RouteEdgeMeta reuseRouteEdgeMeta = ReuseRouteEdgeMetaObj.make(routeEdgeMeta);
		RouteEdge edge = new RouteEdge(fromVertex, toVertex, reuseRouteEdgeMeta, description);
		
		routeEdges.add(edge);
	}
	
	public void makeGraph(){
		routeGraph = new RouteGraph(vertexCount);
		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge);
	}
	
	public void cleanUp(){
		routeEdges = null;
	}
	
	public Iterable<RouteEdge> getRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		if(routeGraph == null) makeGraph();
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), weightEnum);
		return routeDijkstra.pathTo(to.getId());
	}
}
