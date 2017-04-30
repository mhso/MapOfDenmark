package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ReuseRouteEdgeMetaObj;
import dk.itu.n.danmarkskort.models.RouteEnum;
import dk.itu.n.danmarkskort.models.RouteModel;

public class RouteController {
	private int vertexCount;
	private List<RouteEdge> routeEdges;
	private List<RouteVertex> vertices;
	private RouteGraph routeGraph;
<<<<<<< HEAD
	private boolean debug = false;
=======
	private boolean debug = true;
	KDTree<RouteEdge> edgeTree;
>>>>>>> branch 'develop' of https://github.itu.dk/fand/BFST17_DK_Kort
	
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
		if(forwardAllowed){
			if(debug && description != null && description.startsWith("Amagerbrogade"))System.out.println("debug addEdge forward: " + description);
			RouteEdge edge = new RouteEdge(fromVertex, toVertex, reuseRouteEdgeMeta, description);
			routeEdges.add(edge);
		}
		if(backwardAllowed){
			if(debug && description != null && description.startsWith("Amagerbrogade"))System.out.println("debug addEdge backward: " + description);
			RouteEdge edge = new RouteEdge(toVertex, fromVertex, reuseRouteEdgeMeta, description);
			routeEdges.add(edge);
		}
	
	}
	
	public void makeGraph(){
		routeGraph = new RouteGraph(vertexCount);
		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge);
		edgeTree = new KDTreeNode<>(routeEdges);
	}
	
	public void cleanUp(){
		routeEdges = null;
	}
	
	public Iterable<RouteEdge> getRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		if(routeGraph == null) makeGraph();
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), weightEnum);
		if(debug) System.out.println("debug getRoute: " + routeDijkstra);
		return routeDijkstra.pathTo(to.getId());
	}
	
	public boolean hasRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		if(routeGraph == null) makeGraph();
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), weightEnum);
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
	
	public RouteEdge searchEdgesKDTree(Point2D.Float lonLat){
		//KDTree<RouteEdge> kdTree = new KDTreeNode<>(routeEdges);
		
		if(debug) {
			System.out.println("debug searchEdgesKDTree: \n"
					+ "input para: " + lonLat
					);
		}
		
		if(lonLat != null) {
			RouteEdge edge = edgeTree.nearest(lonLat);
			if(edge != null) { System.out.println("RouteController found Edge: " + edge.getDescription()); }
			else { System.out.println("No edge found"); }
			return edge;
		}
		return null;
	}


	
	public List<RouteModel> makeRoute(Point2D.Float from, Point2D.Float to, WeightEnum weightEnum){
		List<RouteModel> routeModels = new ArrayList<RouteModel>();
		RouteEdge fromEdge = searchEdgesKDTree(from);
		RouteEdge toEdge = searchEdgesKDTree(to);
		
		if(debug) {
			System.out.println("debug makeRoute: \n"
					+ "from: " + from.toString() + ", fromEdge: " + fromEdge
					+ " to: " + to.toString() + ", toEdge: " + toEdge
					);
		}
		
		if(fromEdge != null && toEdge != null && hasRoute(fromEdge.getFrom(), toEdge.getFrom(), weightEnum)) {
			if(debug)System.out.println("debug makeRoute hasRoute!!!");
			Iterable<RouteEdge> edges = getRoute(fromEdge.getFrom(), toEdge.getFrom(), weightEnum);
			
			if(debug && edges != null)System.out.println("debug makeRoute IterableEdges!!!");
			RouteModel lastModel = null;
			double distSum = 0;
			for(RouteEdge edge : edges){
				if(debug)System.out.println("debug makeRoute foreach: " + edge.toString());
				RouteEnum routeEnum = RouteEnum.CONTINUE_ON;
				
				if(lastModel != null && edge.getDescription().equals(lastModel.getDescription())) {
					distSum += edge.getDistance();
					lastModel.setDistance(distSum);
				}else{
					distSum = 0;
					RouteModel routeModel = new RouteModel(routeEnum, edge.getDescription(), edge.getDistance());
					lastModel = routeModel;
					routeModels.add(routeModel);
				}
			}
			if(debug) {
				System.out.println("debug makeRoute return found edges! size: " + routeModels.size());
			}
			return routeModels;
		}
		return Collections.emptyList();
	}
}
