package dk.itu.n.danmarkskort.routeplanner;

import java.io.Serializable;

public class RouteGraph implements Serializable {
	private static final long serialVersionUID = 7359475752602187631L;
	private static final String NEWLINE = System.getProperty("line.separator");
	private final int numOfVertices;			// number of vertices in this digraph
	private int numOfEdges;						// number of edges in this digraph
	private Bag<RouteEdge>[] adjacent;			// adj[v] = adjacency list for vertex v
	//private int[] indegree;						// indegree[v] = indegree of vertex v
	
	/**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public RouteGraph(int numOfVertices) {
        if (numOfVertices < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.numOfVertices = numOfVertices;
        numOfEdges = 0;
        //this.indegree = new int[numOfVertices];
        adjacent = (Bag<RouteEdge>[]) new Bag[numOfVertices];
        
        for (int i= 0; i < numOfVertices; i++) adjacent[i] = new Bag<RouteEdge>();
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     * @return the number of vertices in this edge-weighted digraph
     */
    public int getNumOfVertices() { return numOfVertices; }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     * @return the number of edges in this edge-weighted digraph
     */
    public int getNumOfEdges() { return numOfEdges; }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertexId) {
        if (vertexId < 0 || vertexId >= numOfVertices)
            throw new IllegalArgumentException("vertex " + vertexId + " is not between 0 and " + (numOfVertices-1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     * @param  edge the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(RouteEdge edge) {
        int fromId = edge.getFromId();
        int toId = edge.getToId();
        validateVertex(fromId);
        validateVertex(toId);
        adjacent[fromId].add(edge);
        //indegree[toId]++;
        numOfEdges++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     * @param  vertexId the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<RouteEdge> adjacent(int vertexId) {
        validateVertex(vertexId);
        return adjacent[vertexId];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     * @param  vertexId the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int vertexId) {
        validateVertex(vertexId);
        return adjacent[vertexId].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}
     * @param  vertexId the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
//    public int indegree(int vertexId) {
//        validateVertex(vertexId);
//        return indegree[vertexId];
//    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<RouteEdge> edges() {
    	Bag<RouteEdge> list = new Bag<RouteEdge>();
        for (int vertexId = 0; vertexId < numOfVertices; vertexId++) {
            for (RouteEdge edge : adjacent(vertexId)) {
                list.add(edge);
            }
        }
        return list;
    } 

    /**
     * Returns a string representation of this edge-weighted digraph.
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(numOfVertices + " " + numOfEdges + NEWLINE);
        for (int vertexId = 0; vertexId < numOfVertices; vertexId++) {
            sb.append(vertexId + ": ");
            for (RouteEdge edge : adjacent[vertexId]) {
                sb.append(edge + "  ");
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }
}