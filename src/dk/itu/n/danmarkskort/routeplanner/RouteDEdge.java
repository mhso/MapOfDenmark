package dk.itu.n.danmarkskort.routeplanner;

import dk.itu.n.danmarkskort.models.ReuseStringObj;
import dk.itu.n.danmarkskort.models.WayType;

public class RouteDEdge {
	private final RouteVertex from, to;
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed;
	private final String description;
	private final WayType wayType;

	public RouteDEdge(RouteVertex from, RouteVertex to, int maxSpeed, boolean forwardAllowed, boolean backwardAllowed, String description, WayType wayType){
		if (from.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (to.getId() < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (maxSpeed <= 0) throw new IllegalArgumentException("maxSpeed is 0, must be nonnegative integer");
		
		this.from = from;
		this.to = to;
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
		this.description = ReuseStringObj.make(description);
		this.wayType = wayType;
	}
	
	public int getMaxSpeed() { return maxSpeed; }
	public RouteVertex getFrom() { return from; }
	public RouteVertex getTo() { return to; }
	public int getFromId() { return from.getId(); }
	public int getToId() { return to.getId(); }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() {return backwardAllowed;	}
	public String getDescription(){ return description; }
	public WayType getWayType(){ return wayType; }

	private double distance(){
		return Math.sqrt(Math.pow((to.getX() - from.getX()), 2) + Math.pow((to.getY() - from.getY()), 2));
	}
	
	public double getWeightByDistance(){ return distance(); }
	public double getWeightBySpeed(){ return distance() / (double)maxSpeed; }
	
	public double getWeight(DEWeightEnum weightEnum){
    	double result = 0;
    	switch(weightEnum) {
		case DISTANCE: result = getWeightByDistance();
			break;
		case SPEED: result = getWeightBySpeed();
			break;
		default:
			break;
    	}
    	return result;
    }
	
	public String toString() {
		return from.getId() + "->" + to.getId();
	}
}
