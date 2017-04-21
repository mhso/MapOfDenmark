package dk.itu.n.danmarkskort.routeplanner;

import dk.itu.n.danmarkskort.models.ReuseStringObj;
import dk.itu.n.danmarkskort.models.WayType;

public class CustomDirectedEdge {
	private final CustomVertex from, to;
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed;
	private final String description;
	private final WayType wayType;

	public CustomDirectedEdge(CustomVertex from, CustomVertex to, int maxSpeed, boolean forwardAllowed, boolean backwardAllowed, String description, WayType wayType){
		this.from = from;
		this.to= to;
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
		this.description = ReuseStringObj.make(description);
		this.wayType = wayType;
	}
	
	public int getMaxSpeed() { return maxSpeed; }

	public CustomVertex getFrom() { return from; }
	public CustomVertex getTo() { return to; }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() {return backwardAllowed;	}
	public String getDescription(){ return description; }
	public WayType getWayType(){ return wayType; }

	private double distance(){
		return Math.sqrt(Math.pow((to.getX() - from.getY()), 2) + Math.pow((to.getY() - from.getY()), 2));
	}
	
	public double weightByDistance(){ return distance(); }
	public double weightBySpeed(){ return distance() / (double)maxSpeed; }
}
