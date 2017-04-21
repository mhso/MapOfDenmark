package dk.itu.n.danmarkskort.routeplanner;

import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.models.WayType;

public class RouteController {
	private List<CustomVertex> customVertices;
	private List<CustomDirectedEdge> customDirectedEdges;
	
	public RouteController(){
		customVertices = new ArrayList<>();
		
	}
	
	public void addVertex(CustomVertex v){
		customVertices.add(v);
	}
	
	public void addEdge(CustomVertex from, CustomVertex to, int maxSpeed, boolean forwardAllowed, boolean backwardAllowed, String description, WayType wayType){
		CustomDirectedEdge customDirectedEdge = new CustomDirectedEdge(from, to, maxSpeed, forwardAllowed, backwardAllowed, description, wayType);
		customDirectedEdges.add(customDirectedEdge);
	}
}
