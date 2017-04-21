package dk.itu.n.danmarkskort.routeplanner;

public class CustomDiGraph {
	private final int numOfVertices;			// number of vertices in this digraph
	private int numOfEdges;						// number of edges in this digraph
	private Bag<CustomDirectedEdge>[] adj;		// adj[v] = adjacency list for vertex v
	private int[] indegree;						// indegree[v] = indegree of vertex v
	
	/**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public CustomDiGraph(int numOfVertices) {
        if (numOfVertices < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.numOfVertices = numOfVertices;
        numOfEdges = 0;
        this.indegree = new int[numOfVertices];
        adj = (Bag<CustomDirectedEdge>[]) new Bag[numOfVertices];
        for (int i= 0; i < numOfVertices; i++)
            adj[i] = new Bag<CustomDirectedEdge>();
    }
    
    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(CustomDirectedEdge e) {
        CustomVertex from = e.getFrom();
        CustomVertex to = e.to();
        adj[v].add(e);
        indegree[e.to()]++;
        numOfEdges++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<CustomDirectedEdge> adj(int v) {
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        return adj[v].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(int v) {
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<CustomDirectedEdge> edges() {
        Bag<CustomDirectedEdge> list = new Bag<CustomDirectedEdge>();
        for (int v = 0; v < numOfVertices; v++) {
            for (DirectedEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    }
    
    
}

