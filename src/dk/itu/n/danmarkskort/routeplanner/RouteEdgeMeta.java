package dk.itu.n.danmarkskort.routeplanner;

import java.io.Serializable;

public class RouteEdgeMeta implements Serializable {
	private static final long serialVersionUID = 4626171199056902618L;
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed, carsAllowed, bikesAllowed, walkAllowed;
	
	public RouteEdgeMeta(Integer maxSpeed, boolean forwardAllowed, boolean backwardAllowed,
		boolean carsAllowed, boolean bikesAllowed, boolean walkAllowed){
        
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
		this.carsAllowed = carsAllowed;
		this.bikesAllowed = bikesAllowed;
		this.walkAllowed = walkAllowed;
	}

	public int getMaxSpeed() { return maxSpeed; }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() { return backwardAllowed; }
	public boolean isCarsAllowed() { return carsAllowed; }
	public boolean isBikesAllowed() { return bikesAllowed; }
	public boolean isWalkAllowed() { return walkAllowed; }
	
	public String getKey(){
		return maxSpeed + "_" + forwardAllowed + "_" + backwardAllowed + "_" + carsAllowed + "_" + bikesAllowed + "_" + walkAllowed;
	}
}
