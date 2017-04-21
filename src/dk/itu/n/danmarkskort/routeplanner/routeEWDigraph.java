package dk.itu.n.danmarkskort.routeplanner;

public class routeEWDigraph {
	private static final String NEWLINE = System.getProperty("line.separator");
	private final int numOfVertices;			// number of vertices in this digraph
	private int numOfEdges;						// number of edges in this digraph
	private Bag<RouteDEdge>[] adj;		// adj[v] = adjacency list for vertex v
	private int[] indegree;						// indegree[v] = indegree of vertex v
	
	/**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public routeEWDigraph(int numOfVertices) {
        if (numOfVertices < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.numOfVertices = numOfVertices;
        numOfEdges = 0;
        this.indegree = new int[numOfVertices];
        adj = (Bag<RouteDEdge>[]) new Bag[numOfVertices];
        for (int i= 0; i < numOfVertices; i++)
            adj[i] = new Bag<RouteDEdge>();
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int getNumOfVertices() { return numOfVertices; }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int getNumOfEdges() { return numOfEdges; }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= numOfVertices)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (numOfVertices-1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(RouteDEdge e) {
        int v = e.getFromId();
        int w = e.getToId();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        numOfEdges++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<RouteDEdge> adj(int v) {
        validateVertex(v);
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
        validateVertex(v);
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
        validateVertex(v);
        return indegree[v];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<RouteDEdge> edges() {
        Bag<RouteDEdge> list = new Bag<RouteDEdge>();
        for (int v = 0; v < numOfVertices; v++) {
            for (RouteDEdge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(numOfVertices + " " + numOfEdges + NEWLINE);
        for (int v = 0; v < numOfVertices; v++) {
            s.append(v + ": ");
            for (RouteDEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}