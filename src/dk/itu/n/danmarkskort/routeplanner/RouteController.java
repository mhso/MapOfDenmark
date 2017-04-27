package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.models.ReuseRouteEdgeMetaObj;

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
		RouteVertex vertex = new RouteVertex(vertexCount, new Point2D.Float(lon, lat));
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
		//System.out.println("add Edge: " + edge.toString());
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
	
	public boolean isRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		if(routeGraph == null) makeGraph();
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, 
				from.getId(), 
				weightEnum);
		return routeDijkstra.hasPathTo(to.getId());
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getNumOfRouteEdges() {
		return routeEdges.size();
	}

	public int getNumOfVertices() {
		return vertices.size();
	}

	public List<RouteEdge> getRouteEdges() {
		return routeEdges;
	}

	public List<RouteVertex> getVertices() {
		return vertices;
	}
	
	public String toString() {
		return "RouteController: vertexCount=" + vertexCount
				+ " getNumOfVertices=" + getNumOfVertices()
				+ " getNumOfRouteEdges=" + getNumOfRouteEdges();
	}
	
	public RouteVertex demoFindVertex(float[] lonLat){
		for(RouteVertex rv : vertices){
			System.out.println("demoFindVertex, compare: x" + lonLat[0] + " = " + rv.x +", " + lonLat[1] + " = " + rv.y);
			if(rv.isEqualPoint(new Point2D.Float(lonLat[0], lonLat[1]))) return rv;
		}
		return null;
	}
}
