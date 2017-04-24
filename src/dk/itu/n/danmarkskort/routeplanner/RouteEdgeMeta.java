package dk.itu.n.danmarkskort.routeplanner;

import dk.itu.n.danmarkskort.models.WayType;

public class RouteEdgeMeta {
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed, carsAllowed, bikesAllowed;
	private final WayType wayType;
	
	public RouteEdgeMeta(Integer maxSpeed, boolean forwardAllowed, boolean backwardAllowed,
			boolean carsAllowed, boolean bikesAllowed, WayType wayType){
         
        if(maxSpeed != null) {
        	this.maxSpeed = maxSpeed;
        } else {
        	maxSpeed = Integer.MAX_VALUE;
        }
        
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
		this.carsAllowed = carsAllowed;
		this.bikesAllowed = bikesAllowed;
		this.wayType = wayType;
	}

	public int getMaxSpeed() { return maxSpeed; }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() { return backwardAllowed; }
	public boolean isCarsAllowed() { return carsAllowed; }
	public boolean isBikesAllowed() { return bikesAllowed; }
	public WayType getWayType() { return wayType; }
	
	
}
