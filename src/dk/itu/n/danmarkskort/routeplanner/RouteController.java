package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.*;

import com.github.davidmoten.guavamini.Lists;

import dk.itu.n.danmarkskort.Main;
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
	public boolean isDrawingDjikstra = false;
	private boolean useDjikstraWithAStar = true;
	KDTree<RouteEdge> edgeTree;
	private HashMap<Point2D.Float, RouteVertex> vertexMap;

	public RouteController(){
		vertexCount = 0;
		routeEdges = new ArrayList<RouteEdge>(1610400); // Initialize the array to match the amount of edges expected for Denmark
	}
	
	/**
	 * Method used for creating metadata and edges, depended on forward/backward values the edges
	 * are then added to the map, for later handling.
	 * 
	 * @param fromVertex
	 * @param toVertex
	 * @param maxSpeed
	 * @param forwardAllowed
	 * @param backwardAllowed
	 * @param carsAllowed
	 * @param bikesAllowed
	 * @param walkAllowed
	 * @param description, could be streetname, or names area.
	 */
	public void addEdge(Point2D.Float fromVertex, Point2D.Float toVertex, short maxSpeed,
			boolean forwardAllowed, boolean backwardAllowed, boolean carsAllowed,
			boolean bikesAllowed, boolean walkAllowed, String description){
		
		// EdgeMeta created, will be reused if one has equal values.
		RouteEdgeMeta routeEdgeMeta = new RouteEdgeMeta(maxSpeed, forwardAllowed, backwardAllowed,
				carsAllowed, bikesAllowed, walkAllowed);
		
		if(forwardAllowed) // Added forward going edge to the map.
			routeEdges.add(new RouteEdge(fromVertex, toVertex, routeEdgeMeta, description));
		if(backwardAllowed) // Added backward going edge to the ma.
			routeEdges.add(new RouteEdge(toVertex, fromVertex, routeEdgeMeta, description));
	}
	
	/**
	 * When all vertex and edges have established, this method will create the actual graph datastructure.
	 * Method will create uniq ids for every vertex used in the graph.
	 * Afterwards the makeTree methods i called for creating a KD-Tree of edges.
	 * At last the cleanUp method, removes unused references etc.
	 */
	public void makeGraph(){
		vertexMap = new HashMap<>();
		for(RouteEdge edge: routeEdges) {
			Point2D.Float from = edge.getFrom();
			Point2D.Float to = edge.getTo();

			vertexMap.putIfAbsent(from, new RouteVertex(vertexMap.size(), from));
			edge.setFrom(vertexMap.get(from));
			vertexMap.putIfAbsent(to, new RouteVertex(vertexMap.size(), to));
			edge.setTo(vertexMap.get(to));
		}
		vertexCount = vertexMap.size();
		routeGraph = new RouteGraph(vertexMap.size());
		vertexMap = null;

		for(RouteEdge edge : routeEdges) routeGraph.addEdge(edge) ;
		Main.log("vertexMap.size(): " + vertexCount);
		makeTree();	
		cleanUp();
	}

	/**
	 * Creates a KD-Tree of edges, for finding nearest neighbor, i the edge structure. 
	 */
	private void makeTree() {
		edgeTree = new KDTreeNode<>(routeEdges, 1000); // Initializes a KDTree with leaf size of 1000 elements.
	}
	
	public void cleanUp(){
		routeEdges = null;
		ReuseRouteEdgeMetaObj.clear();
	}
	
	/**
	 * Method make a call to Dijkstra or DijkstraAStar depended of userPreferences.
	 * If a route is found a collection of edges is returned.
	 * @param from, vertex
	 * @param to, vertex
	 * @param weightEnum, based on traveltype (speed, distance, car, bike etc..)
	 * @return an Iterator of RouteEdge
	 */
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

	public int getRelaxCount(RouteVertex from, RouteVertex to, WeightEnum type) {
        useDjikstraWithAStar = Main.userPreferences.useDjikstraWithAStar();
        if(useDjikstraWithAStar) {
            RouteDijkstraAStar routeDijkstra = new RouteDijkstraAStar(routeGraph, from, to, type);
            return routeDijkstra.getRelaxCount();
        }else{
            RouteDijkstra routeDijkstra = new RouteDijkstra(routeGraph, from.getId(), to.getId(), type);
            return routeDijkstra.getRelaxCount();
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
	
	@Override
	public String toString() {
		return "RouteController: vertexCount=" + vertexCount
				+ " getNumOfRouteEdges=" + getNumOfRouteEdges();
	}
	
	/**
	 * Method call nearest on the KD-Tree of edges and return a nearest neighbor.
	 * 
	 * @param lonLat coordinates to find a neighbor to.
	 * @return nearest edge to input coordinates.
	 */
	public RouteEdge searchEdgesKDTree(Point2D.Float lonLat){		
		if(lonLat != null) {
			RouteEdge edge = edgeTree.nearest(lonLat);
			return edge;
		}
		return null;
	}

	/**
	 * Method will if able return a route based in the from/to and weight of the traveltype.
	 * The created RouteModels are given a direction description, like left, right, at destination etc.
	 * based on the relationship between to edges.
	 * 
	 * @param from, coordinates
	 * @param to, coordinates
	 * @param weightEnum, based on travel type
	 * @return a list of RouteModels as a route, or empty collections if no route found.
	 */
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
			
			sizeOfEdges = Lists.newArrayList(edges).size();
			
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
					RouteModel routeModel = new RouteModel(routeEnum, 
							edge.getDescription(), edge.getMaxSpeed(), edge.getDistance());
					lastModel = routeModel;
					routeModels.add(routeModel);
				}
				if(count == sizeOfEdges)
					lastModel.updateDirectionAndDescription(RouteEnum.AT_DESTINATION, lastEdge.getDescription());
				updateEdgeBounds(routeBounds, edge);
				lastEdge = edge;
				
			}
			routeRegion = new Region(routeBounds[0], routeBounds[1], routeBounds[2], routeBounds[3]);
			return routeModels;
		}
		return Collections.emptyList();
	}
	
	/**
	 * Check for region to capture routeplanner image.
	 * 
	 * @param currentRouteBounds
	 * @param edge
	 */
	private void updateEdgeBounds(double[] currentRouteBounds, RouteEdge edge) {
		RouteVertex edgeFrom = (RouteVertex) edge.getFrom();
		RouteVertex edgeTo = (RouteVertex) edge.getTo();
		if(edgeFrom.getX() < currentRouteBounds[0]) currentRouteBounds[0] = edgeFrom.getX();
		if(edgeFrom.getY() < currentRouteBounds[1]) currentRouteBounds[1] = edgeFrom.getY();
		if(edgeTo.getX() > currentRouteBounds[2]) currentRouteBounds[2] = edgeTo.getX();
		if(edgeTo.getY() > currentRouteBounds[3]) currentRouteBounds[3] = edgeTo.getY();
	}

	/**
	 * Finds a direction between to edges.
	 * The heuristic calculation is based upon the angle between to edges.
	 * 
	 * @param edge, actual edge
	 * @param lastEdge, former edge
	 * @return a enum representing a direction.
	 */
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
			//Main.log("v: " + v1v2AngleInt + " ( " + v2StrAngleStr + " )" + ", " + routeEnum.toString() + " til " + edge.getDescription());
		}
		return routeEnum;
	}
	
	/**
	 * Calculations the angle between 2 coherent lines, represented by 3 vertex points.
	 * 
	 * @param center 
	 * @param current
	 * @param previous 
	 * @return a calculation of the angle from the 3 points.
	 */
	private double angleBetween2Edges(Point2D.Float center, Point2D.Float current, Point2D.Float previous) {
		  return Math.toDegrees(Math.atan2(current.x - center.x,current.y - center.y) -
		                        Math.atan2(previous.x - center.x,previous.y - center.y));
	}
}
