package dk.itu.n.danmarkskort.routeplanner;

import java.io.Serializable;

/**
 *  The {@code Graph} class represents an undirected graph of vertices
 *  named 0 through <em>V</em> – 1.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the vertices adjacent to a vertex. It also provides
 *  methods for returning the number of vertices <em>V</em> and the number
 *  of edges <em>E</em>. Parallel edges and self-loops are permitted.
 *  By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 *  adjacency list of <em>v</em> twice and contributes two to the degree
 *  of <em>v</em>.
 *  <p>
 *  This implementation uses an adjacency-lists representation, which 
 *  is a vertex-indexed array of {@link Bag} objects.
 *  All operations take constant time (in the worst case) except
 *  iterating over the vertices adjacent to a given vertex, which takes
 *  time proportional to the number of such vertices.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class RouteGraph implements Serializable {
	private static final long serialVersionUID = 7359475752602187631L;
	private static final String NEWLINE = System.getProperty("line.separator");
	private final int numOfVertices;			// number of vertices in this digraph
	private int numOfEdges;						// number of edges in this digraph
	private ResizingArrayBag<RouteEdge>[] adjacent;			// adj[v] = adjacency list for vertex v
	//private int[] indegree;						// indegree[v] = indegree of vertex v
	
	/**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     * @param  V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    @SuppressWarnings("unchecked")
	public RouteGraph(int numOfVertices) {
        if (numOfVertices < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.numOfVertices = numOfVertices;
        numOfEdges = 0;
        //this.indegree = new int[numOfVertices];
        adjacent = (ResizingArrayBag<RouteEdge>[]) new ResizingArrayBag[numOfVertices];
        
        for (int i= 0; i < numOfVertices; i++) adjacent[i] = new ResizingArrayBag<RouteEdge>();
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
    	ResizingArrayBag<RouteEdge> list = new ResizingArrayBag<RouteEdge>();
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

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/