package dk.itu.n.danmarkskort.routeplanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

public class RouteDijkstraAStar {
	private double[] distTo;         		// distTo[v] = distance  of shortest s->v path
    private RouteEdge[] edgeTo;    			// edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    		// priority queue of vertices
    private WeightEnum weightEnum;
    private boolean debug = false;
    private int source, target;
    private RouteVertex sourceVertex, targetVertex;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     * @param  graph the edge-weighted digraph
     * @param  source the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public RouteDijkstraAStar(RouteGraph graph, RouteVertex sourceVertex, RouteVertex targetVertex, WeightEnum weightEnum) {
    	this.sourceVertex =  sourceVertex;
    	this.targetVertex =  targetVertex;
    	this.source =  sourceVertex.getId();
    	this.target =  targetVertex.getId();
    	this.weightEnum = weightEnum;

        distTo = new double[graph.getNumOfVertices()];
        edgeTo = new RouteEdge[graph.getNumOfVertices()];
        List<RouteEdge> edgesInRoute = null;
        if(Main.routeController.isDrawingDjikstra) edgesInRoute = new ArrayList<>();
        
        validateVertex(source);

        for (int v = 0; v < graph.getNumOfVertices(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[source] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(graph.getNumOfVertices());
        pq.insert(source, distTo[source] + heuristics(sourceVertex, -1));
    	while (!pq.isEmpty()) {
            int v = pq.delMin();
            if(v == target) break;
            
        	for (RouteEdge edge : graph.adjacent(v)) {
            	if(edge.isTravelTypeAllowed(weightEnum)) {
            		if(Main.routeController.isDrawingDjikstra) edgesInRoute.add(edge);
            		relax(edge);
            	}
            }
        }
        if(Main.routeController.isDrawingDjikstra) {
        	Main.map.setRoute(edgesInRoute);
        	Main.map.repaint();
        }

        // check optimality conditions
        //assert check(graph, source);
    }
    
    private double heuristics(Point2D.Float sourceVertex, int maxSpeed){
    	Point2D source = Util.toRealCoords(sourceVertex);
    	Point2D target = Util.toRealCoords(targetVertex);
    	double distance = Util.distanceInMeters(source, target);
    	double speed = 1;
    	if(maxSpeed == -1) maxSpeed = 1;
    	switch(weightEnum){
		case SPEED_CAR: speed = maxSpeed;
			break;
		default: speed = 1;
			break;
    	}
    	return distance / speed;
    }

    // relax edge e and update pq if changed
    private void relax(RouteEdge edge) {
        int fromId = edge.getFromId(), toId = edge.getToId();
        
        if (distTo[toId] > distTo[fromId] + edge.getWeight(weightEnum)) {
            distTo[toId] = distTo[fromId] + edge.getWeight(weightEnum);
            edgeTo[toId] = edge;
            if (pq.contains(toId)) pq.decreaseKey(toId, distTo[toId] + heuristics(edge.getFrom(), edge.getMaxSpeed()));
            else                   pq.insert(toId, distTo[toId] + heuristics(edge.getFrom(), edge.getMaxSpeed()));
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  vertexId the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(int vertexId) {
        validateVertex(vertexId);
        return distTo[vertexId];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     * @param  vertexId the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int vertexId) {
        validateVertex(vertexId);
        return distTo[vertexId] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  vertexId the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<RouteEdge> pathTo(int vertexId) {
        validateVertex(vertexId);
        if (!hasPathTo(vertexId)) return null;
        Stack<RouteEdge> path = new Stack<RouteEdge>();
        for (RouteEdge edge = edgeTo[vertexId]; edge != null; edge = edgeTo[edge.getFromId()]) {
        	if(debug)System.out.println("debug pathTo: " + edge.toString());
            path.push(edge);
        }
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertexId) {
        int V = distTo.length;
        if (vertexId < 0 || vertexId >= V)
            throw new IllegalArgumentException("vertex " + vertexId + " is not between 0 and " + (V-1));
    }
}
