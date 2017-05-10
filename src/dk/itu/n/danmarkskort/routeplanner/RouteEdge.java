package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.io.Serializable;

import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class RouteEdge implements KDComparable, Serializable {
	private static final long serialVersionUID = 7080914394280747088L;
	private Point2D.Float from, to;
	private RouteEdgeMeta routeEdgeMeta;
	private final String description;

	public RouteEdge(Point2D.Float from, Point2D.Float to, RouteEdgeMeta routeEdgeMeta, String description){
		//if (from.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        //if (to.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (routeEdgeMeta.getMaxSpeed() <= 0) { throw new IllegalArgumentException("maxSpeed less than or equal to 0, must be positive integer"); }
		this.from = from;
		this.to = to;
		this.routeEdgeMeta = routeEdgeMeta;
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
	public boolean isForwardAllowed() { return routeEdgeMeta.isForwardAllowed(); }
	public boolean isBackwardAllowed() {return routeEdgeMeta.isBackwardAllowed(); }
	public String getDescription(){ return description; }

	private double distance(){ return Util.distanceInMeters(from, to); }
	
	public double getDistance(){ return distance(); }
	public double getWeightBySpeed(){ return distance() / (double)routeEdgeMeta.getMaxSpeed(); }
	
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
	
	public String toString() {
		return "from " + from.toString() + " -> " + to.toString() + "]\n";
	}
	
	public String toStringDesr() {
		return "[ " + description + " ]";
	}

	@Override
	public Point2D.Float getFirstNode() {
		return from;
	}

	@Override
	public Point2D.Float[] getNodes() {
		return new Point2D.Float[] {from, to};
	}

	@Override
	public float[] getCoords() {
		return new float[] {from.x, from.y, to.x, to.y};
	}
}
