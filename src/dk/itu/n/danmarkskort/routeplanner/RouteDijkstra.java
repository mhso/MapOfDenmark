package dk.itu.n.danmarkskort.routeplanner;

public class RouteDijkstra {
	private double[] distTo;         		// distTo[v] = distance  of shortest s->v path
    private RouteEdge[] edgeTo;    			// edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    		// priority queue of vertices
    private WeightEnum weightEnum;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     * @param  graph the edge-weighted digraph
     * @param  sourceVetex the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public RouteDijkstra(RouteGraph graph, int sourceVetex, WeightEnum weightEnum) {
    	this.weightEnum = weightEnum;
        for (RouteEdge edge : graph.edges()) {
            if (edge.getWeight(weightEnum) < 0)
                throw new IllegalArgumentException("edge " + edge + " has negative weight");
        }

        distTo = new double[graph.getNumOfVertices()];
        edgeTo = new RouteEdge[graph.getNumOfVertices()];

        validateVertex(sourceVetex);

        for (int v = 0; v < graph.getNumOfVertices(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[sourceVetex] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(graph.getNumOfVertices());
        pq.insert(sourceVetex, distTo[sourceVetex]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (RouteEdge edge : graph.adjacent(v))
                relax(edge);
        }

        // check optimality conditions
        assert check(graph, sourceVetex);
    }

    // relax edge e and update pq if changed
    private void relax(RouteEdge edge) {
        int fromId = edge.getFromId(), toId = edge.getToId();
        
        if (distTo[toId] > distTo[fromId] + edge.getWeight(weightEnum)) {
            distTo[toId] = distTo[fromId] + edge.getWeight(weightEnum);
            edgeTo[toId] = edge;
            if (pq.contains(toId)) pq.decreaseKey(toId, distTo[toId]);
            else                pq.insert(toId, distTo[toId]);
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
            path.push(edge);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(RouteGraph graph, int s) {

        // check that edge weights are nonnegative
        for (RouteEdge edge : graph.edges()) {
            if (edge.getWeight(weightEnum) < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < graph.getNumOfVertices(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < graph.getNumOfVertices(); v++) {
            for (RouteEdge edge : graph.adjacent(v)) {
                int w = edge.getToId();
                if (distTo[v] + edge.getWeight(weightEnum) < distTo[w]) {
                    System.err.println("edge " + edge + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < graph.getNumOfVertices(); w++) {
            if (edgeTo[w] == null) continue;
            RouteEdge edge = edgeTo[w];
            int v = edge.getFromId();
            if (w != edge.getToId()) return false;
            if (distTo[v] + edge.getWeight(weightEnum) != distTo[w]) {
                System.err.println("edge " + edge + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int vertexId) {
        int V = distTo.length;
        if (vertexId < 0 || vertexId >= V)
            throw new IllegalArgumentException("vertex " + vertexId + " is not between 0 and " + (V-1));
    }
}
