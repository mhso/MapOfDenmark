package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

/**
 * This class is almost entirely inspired by the DijkstraSP, from 
 * Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne.
 * The class is extended with a heuristic method, that adds the A* addition to the Dijkstra algorithm. 
 * Names have been changed to be more readable.
 *
 * @author Group N
 *
 * 
 *  The {@code DijkstraSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
 *  This implementation uses Dijkstra's algorithm with a binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code distTo()} and {@code hasPathTo()} methods take
 *  constant time and the {@code pathTo()} method takes time proportional to the
 *  number of edges in the shortest path returned.
 *  <p>
 *  For additional documentation,    
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class RouteDijkstraAStar {
	private double[] distTo;         		// distTo[v] = distance  of shortest s->v path
    private RouteEdge[] edgeTo;    			// edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    		// priority queue of vertices
    private WeightEnum weightEnum;
    private boolean debug = false;
    private int source, target;
    private RouteVertex targetVertex;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     * @param  graph the edge-weighted digraph
     * @param  source the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public RouteDijkstraAStar(RouteGraph graph, RouteVertex sourceVertex, RouteVertex targetVertex, WeightEnum weightEnum) {
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
    }
    
    /**
     * The method calculates a heuristic weight guess, based on the actual source vertex a the target destination.
     * The travel speed will be include in the calculations if relevant.
     * 
     * @param sourceVertex actual vertex.
     * @param maxSpeed maximum allowed to take into account.
     * @return a heuristic weight guess from source to target.
     */
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
