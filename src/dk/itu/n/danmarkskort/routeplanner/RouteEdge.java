package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.io.Serializable;

import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.models.ReuseRouteEdgeMetaObj;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

/**
 * Used with routeGraph, edges represent a road stretch from point to point, with addtional meta data information. 
 * @author Group N
 */
public class RouteEdge implements KDComparable, Serializable {
	private static final long serialVersionUID = 7080914394280747088L;
	private Point2D.Float from, to;
	private RouteEdgeMeta routeEdgeMeta;
	private final String description;

	/**
	 * Class constructor, initilize the edge.
	 * @param from, location coordinates.
	 * @param to, location coordinate.
	 * @param routeEdgeMeta, extended meta for the edge.
	 * @param description, a streetname or place description.
	 */
	public RouteEdge(Point2D.Float from, Point2D.Float to, RouteEdgeMeta routeEdgeMeta, String description){
        if (routeEdgeMeta.getMaxSpeed() <= 0) { throw new IllegalArgumentException("maxSpeed less than or equal to 0, must be positive integer"); }
		this.from = from;
		this.to = to;
		this.routeEdgeMeta = ReuseRouteEdgeMetaObj.make(routeEdgeMeta);
		if(description == null || description.trim().isEmpty()) description = "Unnamed road"; // "Ukendt vej";
		this.description = ReuseStringObj.make(description);
	}
	
	public int getMaxSpeed() { return routeEdgeMeta.getMaxSpeed(); }
	public Point2D.Float getFrom() { return from; }
	public Point2D.Float getTo() { return to; }
	public void setFrom(Point2D.Float from) { this.from = from; }
	public void setTo(Point2D.Float to) { this.to = to; }
	
	
	public int getFromId() {
		if(from instanceof RouteVertex) return ((RouteVertex) from).getId();
		return -1;
	}
	public int getToId() {
		if(to instanceof RouteVertex) return ((RouteVertex) to).getId();
		return -1;
	}
	
	/**
	 * Real world distance between from/to location coordinats in metres.
	 * @return distance in metres.
	 */
	private double distance(){ return Util.distanceInMeters(from, to); }
	
	public String getDescription(){ return description; }
	public double getDistance(){ return distance(); }
	
	/**
	 * Calculate the weight dependend of the maximum allowed speed of the road stretch. 
	 * @return distance devided by speed.
	 */
	public double getWeightBySpeed(){ return distance() / (double)routeEdgeMeta.getMaxSpeed(); }
	
	public boolean isForwardAllowed() { return routeEdgeMeta.isForwardAllowed(); }
	public boolean isBackwardAllowed() {return routeEdgeMeta.isBackwardAllowed(); }
	
	/**
	 * Decide edge weight based on travel perferences (car, bike etc.).
	 * @param weightEnum
	 * @return the edge "weight" base on travel type.
	 */
	public double getWeight(WeightEnum weightEnum){
    	double result = 0;
    	switch(weightEnum) {
		case DISTANCE_CAR: result = getDistance();
			break;
		case SPEED_CAR: result = getWeightBySpeed();
			break;
		case DISTANCE_BIKE: result = getDistance();
			break;
		case DISTANCE_WALK: result = getDistance();
			break;
		default:
			break;
    	}
    	return result;
    }
	
	/**
	 * Tells whether edge allows a traveltype.
	 * @param weightEnum
	 * @return true if edge allows the traveltype, based on weight
	 */
	public boolean isTravelTypeAllowed(WeightEnum weightEnum){
		boolean result = false;
		switch(weightEnum){
		case DISTANCE_BIKE: if(routeEdgeMeta.isBikesAllowed()) { result = true; }
			break;
		case DISTANCE_CAR: if(routeEdgeMeta.isCarsAllowed()) { result = true; }
			break;
		case DISTANCE_WALK: if(routeEdgeMeta.isWalkAllowed()) { result = true; }
			break;
		case SPEED_CAR: if(routeEdgeMeta.isCarsAllowed()) { result = true; }
			break;
		default:
			break;
		}
		return result;
	}

	public String toStringDesr() { return "[ " + description + " ]"; }
	
	@Override
	public String toString() { return "from " + from.toString() + " -> " + to.toString() + "]\n"; }

	@Override
	public Point2D.Float getFirstNode() { return from; }

	@Override
	public Point2D.Float[] getNodes() { return new Point2D.Float[] {from, to}; }

	@Override
	public float[] getCoords() { return new float[] {from.x, from.y, to.x, to.y}; }
}
