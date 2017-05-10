package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.models.ReuseRouteEdgeMetaObj;
import dk.itu.n.danmarkskort.models.RouteEnum;
import dk.itu.n.danmarkskort.models.RouteModel;

public class RouteController {
	private int vertexCount;
	private List<RouteEdge> routeEdges;
	private RouteGraph routeGraph;
	private Region routeRegion;
	private boolean debug = false;
	public boolean isDrawingDjikstra = false;
	private boolean useDjikstraWithAStar = true;
	KDTree<RouteEdge> edgeTree;
	private HashMap<Point2D.Float, RouteVertex> vertexMap;

	public RouteController(){
		vertexCount = 0;
		routeEdges = new ArrayList<RouteEdge>();
	}

	/**
	 * Creates a RouteVertex with a unique Id based on the position i the ArrayList
	 * @param point
	 * @return A Vertex object
	 */

	public RouteVertex makeVertex(Point2D.Float lonLat){
		RouteVertex vertex = new RouteVertex(vertexCount, lonLat);
		vertexCount++;
		return vertex;
	}
	
	public void addEdge(Point2D.Float fromVertex, Point2D.Float toVertex, short maxSpeed,
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
		Main.log("I am makeGraph");

		vertexMap = new HashMap<>();
		for(RouteEdge edge: routeEdges) {
			Point2D.Float from = edge.getFrom();
			Point2D.Float to = edge.getTo();

			vertexMap.putIfAbsent(from, new RouteVertex(vertexMap.size(), from));
			edge.setFrom(vertexMap.get(from));
			vertexMap.putIfAbsent(to, new RouteVertex(vertexMap.size(), to));
			edge.setTo(vertexMap.get(to));
		}
		routeGraph = new RouteGraph(vertexMap.size());
		vertexMap = null;

		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge);
		makeTree();	
		cleanUp();
	}

	private void makeTree() {
		edgeTree = new KDTreeNode<>(routeEdges, 2000);
	}
	
	public void cleanUp(){
		routeEdges = null;
		ReuseRouteEdgeMetaObj.clear();
	}
	
	public Iterable<RouteEdge> getRoute(RouteVertex from, RouteVertex to, WeightEnum weightEnum){
		useDjikstraWithAStar = Main.userPreferences.useDjikstraWithAStar();
		if(useDjikstraWithAStar) {
			RouteDijkstraAStar routeDijkstra = new RouteDijkstraAStar(routeGraph, from, to, weightEnum);
			return routeDijkstra.pathTo(to.getId());
		}else{
			RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), weightEnum);
			return routeDijkstra.pathTo(to.getId());
		}
	}

	public int getVertexCount() { return vertexCount; }
	
	public int getNumOfRouteEdges() { return routeEdges.size(); }

	public List<RouteEdge> getRouteEdges() { return routeEdges; }
	
	public KDTree<RouteEdge> getEdgeTree() { return edgeTree; }
	
	public Region getRouteRegion() { return routeRegion; }
	
	public RouteGraph getGraph() { return routeGraph; }
	
	public void setEdgeTree(KDTree<RouteEdge> edgeTree) {
		this.edgeTree = edgeTree;
	}
	
	public void setGraph(RouteGraph graph) {
		routeGraph = graph;
	}
	
	public String toString() {
		return "RouteController: vertexCount=" + vertexCount
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
		RouteVertex fromVertex = (RouteVertex) fromEdge.getFrom();
		RouteVertex toVertex = (RouteVertex) toEdge.getFrom();

		Iterable<RouteEdge> edges = getRoute(fromVertex, toVertex, weightEnum);
		
		if(fromEdge != null && toEdge != null && edges != null) {
			
			RouteEdge lastEdge = null;
			RouteModel lastModel = null;
			double distSum = 0;
			int sizeOfEdges = 0;
			double minRouteX = Math.min(from.getX(), to.getX());
			double maxRouteX = Math.max(from.getX(), to.getX());
			double minRouteY = Math.min(from.getY(), to.getY());
			double maxRouteY = Math.max(from.getY(), to.getY());
			double[] routeBounds = {minRouteX, minRouteY, maxRouteX, maxRouteY};
			
			for(RouteEdge edge : edges) sizeOfEdges++;
			
			if(Main.map.getRoute() != null && !isDrawingDjikstra) Main.map.setRoute(null);
			int count = 0;
			RouteEnum routeEnum = RouteEnum.CONTINUE_ON;
			for(RouteEdge edge : edges){
				count++;
				if(!isDrawingDjikstra) Main.map.addRouteEdge(edge);
				if(lastModel != null && edge.getDescription().equals(lastEdge.getDescription())) {
					distSum += edge.getDistance();
					lastModel.setDistance(distSum);
				}else{
					routeEnum = calcDirectionType(edge, lastEdge);
					
					distSum = 0;
					RouteModel routeModel = new RouteModel(routeEnum, edge.getDescription(), edge.getMaxSpeed(), edge.getDistance());
					lastModel = routeModel;
					routeModels.add(routeModel);
				}
				if(count == sizeOfEdges) lastModel.updateDirectionAndDescription(RouteEnum.AT_DESTINATION, lastEdge.getDescription());
				testEdgeBounds(routeBounds, edge);
				lastEdge = edge;
				
			}
			routeRegion = new Region(routeBounds[0], routeBounds[1], routeBounds[2], routeBounds[3]);
			return routeModels;
		}
		return Collections.emptyList();
	}
	
	private void testEdgeBounds(double[] currentRouteBounds, RouteEdge edge) {
		RouteVertex edgeFrom = (RouteVertex) edge.getFrom();
		RouteVertex edgeTo = (RouteVertex) edge.getTo();
		if(edgeFrom.getX() < currentRouteBounds[0]) currentRouteBounds[0] = edgeFrom.getX();
		if(edgeFrom.getY() < currentRouteBounds[1]) currentRouteBounds[1] = edgeFrom.getY();
		if(edgeTo.getX() > currentRouteBounds[2]) currentRouteBounds[2] = edgeTo.getX();
		if(edgeTo.getY() > currentRouteBounds[3]) currentRouteBounds[3] = edgeTo.getY();
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
			Main.log("v: " + v1v2AngleInt + " ( " + v2StrAngleStr + " )" + ", " + routeEnum.toString() + " til " + edge.getDescription());
		}
		return routeEnum;
	}
	
	private double angleBetween2Edges(Point2D.Float center, Point2D.Float current, Point2D.Float previous) {
		  return Math.toDegrees(Math.atan2(current.x - center.x,current.y - center.y) -
		                        Math.atan2(previous.x - center.x,previous.y - center.y));
	}
}
