package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.itu.n.danmarkskort.Main;
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
	private boolean debug = true;
	KDTree<RouteEdge> edgeTree;
	
	public RouteController(){
		vertexCount = 0;
		routeEdges = new ArrayList<RouteEdge>();
		vertices = new ArrayList<RouteVertex>();
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
			boolean forwardAllowed, boolean backwardAllowed, boolean carsAllowed, boolean bikesAllowed, boolean walkAllowed, String description){
		RouteEdgeMeta routeEdgeMeta = new RouteEdgeMeta(maxSpeed, forwardAllowed, backwardAllowed,
				carsAllowed, bikesAllowed, walkAllowed);
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
		
		cleanUp();
	}
	
	public void cleanUp(){
		routeEdges = null;
		vertices = null;
	}
	
	public Iterable<RouteEdge> getRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), weightEnum);
		//if(debug) System.out.println("debug getRoute: " + routeDijkstra);
		return routeDijkstra.pathTo(to.getId());
	}
	
	public boolean hasRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), weightEnum);
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
	
	public KDTree<RouteEdge> getEdgeTree() {
		return edgeTree;
	}
	
	public RouteGraph getGraph() {
		return routeGraph;
	}
	
	public void setEdgeTree(KDTree<RouteEdge> edgeTree) {
		this.edgeTree = edgeTree;
	}
	
	public void setGraph(RouteGraph graph) {
		routeGraph = graph;
	}
	
	public String toString() {
		return "RouteController: vertexCount=" + vertexCount
				+ " getNumOfVertices=" + getNumOfVertices()
				+ " getNumOfRouteEdges=" + getNumOfRouteEdges();
	}
	
	public RouteEdge searchEdgesKDTree(Point2D.Float lonLat){
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
			//if(debug)System.out.println("debug makeRoute hasRoute!!!");
			Iterable<RouteEdge> edges = getRoute(fromEdge.getFrom(), toEdge.getFrom(), weightEnum);
			
			//if(debug && edges != null)System.out.println("debug makeRoute IterableEdges!!!");
			RouteEdge lastEdge = null;
			RouteModel lastModel = null;
			double distSum = 0;
			int sizeOfEdges = 0;
			for(RouteEdge edge : edges){
				//if(debug)System.out.println("debug makeRoute foreach: " + edge.toString());
				RouteEnum routeEnum = RouteEnum.CONTINUE_ON;
				//if(lastModel == null) System.out.println("lastModel is null..");
				if(lastModel != null && edge.getDescription().equals(lastEdge.getDescription())) {
					//System.out.println(edge.getDescription() + " = " + lastEdge.getDescription());
					distSum += edge.getDistance();
					lastModel.setDistance(distSum);
					//System.out.println("RouteController-> streetname is the same...");
				}else{
					distSum = 0;
					RouteModel routeModel = new RouteModel(routeEnum, edge.getDescription(), edge.getDistance());
					lastModel = routeModel;
					lastEdge = edge;
					routeModels.add(routeModel);
					//System.out.println("RouteController-> streetname is new...");
				}
				sizeOfEdges++;
			}
			RouteEdge[] edgeArr = new RouteEdge[sizeOfEdges];
			int i = 0;
			for(RouteEdge edge : edges){ edgeArr[i++] =  edge;}
			Main.map.setRoute(edgeArr);
			if(debug) {
				//System.out.println("debug makeRoute return found edges! size: " + routeModels.size());
			}
			return routeModels;
		}
		return Collections.emptyList();
	}
}
