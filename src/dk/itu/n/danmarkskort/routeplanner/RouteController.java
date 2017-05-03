package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
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
			boolean forwardAllowed, boolean backwardAllowed, boolean carsAllowed,
			boolean bikesAllowed, boolean walkAllowed, String description){
		
		RouteEdgeMeta routeEdgeMeta = new RouteEdgeMeta(maxSpeed, forwardAllowed, backwardAllowed,
				carsAllowed, bikesAllowed, walkAllowed);
		RouteEdgeMeta reuseRouteEdgeMeta = ReuseRouteEdgeMetaObj.make(routeEdgeMeta);
		if(forwardAllowed){
			RouteEdge edge = new RouteEdge(fromVertex, toVertex, reuseRouteEdgeMeta, description);
			routeEdges.add(edge);
		}
		if(backwardAllowed){
			RouteEdge edge = new RouteEdge(toVertex, fromVertex, reuseRouteEdgeMeta, description);
			routeEdges.add(edge);
		}
	
	}
	
	public void makeGraph(){
		routeGraph = new RouteGraph(vertexCount);
		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge);
		makeTree();	
		cleanUp();
	}

	private void makeTree() {
		edgeTree = new KDTreeNode<>(routeEdges);
	}
	
	public void cleanUp(){
		routeEdges = null;
		vertices = null;
		ReuseRouteEdgeMetaObj.clear();
	}
	
	public Iterable<RouteEdge> getRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), weightEnum);
		return routeDijkstra.pathTo(to.getId());
	}
	
	public boolean hasRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), weightEnum);
		return routeDijkstra.hasPathTo(to.getId());
	}

	public int getVertexCount() { return vertexCount; }
	
	public int getNumOfRouteEdges() { return routeEdges.size(); }

	public int getNumOfVertices() { return vertices.size(); }

	public List<RouteEdge> getRouteEdges() { return routeEdges; }

	public List<RouteVertex> getVertices() { return vertices; }
	
	public KDTree<RouteEdge> getEdgeTree() { return edgeTree; }
	
	public RouteGraph getGraph() { return routeGraph; }
	
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
		if(lonLat != null) {
			RouteEdge edge = edgeTree.nearest(lonLat);
			return edge;
		}
		return null;
	}

	public List<RouteModel> makeRoute(Point2D.Float from, Point2D.Float to, WeightEnum weightEnum){
		List<RouteModel> routeModels = new ArrayList<RouteModel>();
		RouteEdge fromEdge = searchEdgesKDTree(from);
		RouteEdge toEdge = searchEdgesKDTree(to);
		
		if(fromEdge != null && toEdge != null && hasRoute(fromEdge.getFrom(), toEdge.getFrom(), weightEnum)) {
			Iterable<RouteEdge> edges = getRoute(fromEdge.getFrom(), toEdge.getFrom(), weightEnum);
			
			RouteEdge lastEdge = null;
			RouteModel lastModel = null;
			double distSum = 0;
			int sizeOfEdges = 0;
			for(RouteEdge edge : edges){
				if(lastModel != null && edge.getDescription().equals(lastEdge.getDescription())) {
					distSum += edge.getDistance();
					lastModel.setDistance(distSum);
				}else{
					RouteEnum routeEnum = calcDirectionType(edge, lastEdge);
					distSum = 0;
					RouteModel routeModel = new RouteModel(routeEnum, edge.getDescription(), edge.getDistance());
					lastModel = routeModel;
					routeModels.add(routeModel);
				}
				lastEdge = edge;
				sizeOfEdges++;
			}
			RouteEdge[] edgeArr = new RouteEdge[sizeOfEdges];
			int i = 0;
			for(RouteEdge edge : edges){ edgeArr[i++] =  edge;}
			Main.map.setRoute(edgeArr);
			return routeModels;
		}
		return Collections.emptyList();
	}

	private RouteEnum calcDirectionType(RouteEdge edge, RouteEdge lastEdge) {
		RouteEnum routeEnum = RouteEnum.CONTINUE_ON;
		if(lastEdge == null){
			return RouteEnum.START_AT;
		}
		if(lastEdge != null && edge != null){
			double v1v2Angle = angleBetween2Edges(lastEdge.getFrom(), lastEdge.getTo(), edge.getTo());
			int v1v2AngleInt = (int)v1v2Angle;
			String v2StrAngleStr = String.format("%.1f", v1v2Angle); 
			
			if(v1v2AngleInt < 0) v1v2AngleInt = 360 + v1v2AngleInt;
			
			if(v1v2AngleInt > 45 && v1v2AngleInt < 180){
				routeEnum = RouteEnum.TURN_RIGHT;
			} else if(v1v2AngleInt > 180){
				routeEnum = RouteEnum.TURN_LEFT;
			} else {
				routeEnum = RouteEnum.CONTINUE_ON;
			}
			System.out.println("v: " + v1v2AngleInt + " ( " + v2StrAngleStr + " )" + ", " + routeEnum.toString() + " til " + edge.getDescription());
		}
		return routeEnum;
	}
	
	private double angleBetween2Edges(Point2D.Float center, Point2D.Float current, Point2D.Float previous) {
		  return Math.toDegrees(Math.atan2(current.x - center.x,current.y - center.y) -
		                        Math.atan2(previous.x - center.x,previous.y - center.y));
	}
}
