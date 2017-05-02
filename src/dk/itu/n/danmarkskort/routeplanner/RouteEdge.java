package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;
import java.io.Serializable;

import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class RouteEdge implements KDComparable, Serializable {
	private static final long serialVersionUID = 7080914394280747088L;
	private final RouteVertex from, to;
	private RouteEdgeMeta routeEdgeMeta;
	private final String description;

	public RouteEdge(RouteVertex from, RouteVertex to, RouteEdgeMeta routeEdgeMeta, String description){
		if (from.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (to.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (routeEdgeMeta.getMaxSpeed() <= 0) throw new IllegalArgumentException("maxSpeed is 0, must be positive integer");
		this.from = from;
		this.to = to;
		this.routeEdgeMeta = routeEdgeMeta;
		this.description = ReuseStringObj.make(description);
	}
	
	public int getMaxSpeed() { return routeEdgeMeta.getMaxSpeed(); }
	public RouteVertex getFrom() { return from; }
	public RouteVertex getTo() { return to; }
	public int getFromId() { return from.getId(); }
	public int getToId() { return to.getId(); }
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
		//System.out.println("Result: " + result + ", Type: " + weightEnum.toString());
		return result;
	}
	
	public String toString() {
		return from.getId() + " [" + from.toString() + "] ->" + to.getId()+ " [" + to.toString() + "]\n";
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
