package dk.itu.n.danmarkskort.routeplanner;

public class RouteEdgeMeta {
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed, carsAllowed, bikesAllowed;
	
	public RouteEdgeMeta(Integer maxSpeed, boolean forwardAllowed, boolean backwardAllowed,
		boolean carsAllowed, boolean bikesAllowed){
         
        this.maxSpeed = maxSpeed;
        
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
		this.carsAllowed = carsAllowed;
		this.bikesAllowed = bikesAllowed;
	}

	public int getMaxSpeed() { return maxSpeed; }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() { return backwardAllowed; }
	public boolean isCarsAllowed() { return carsAllowed; }
	public boolean isBikesAllowed() { return bikesAllowed; }
	
	public String getKey(){
		return maxSpeed + "_" + forwardAllowed + "_" + backwardAllowed + "_" + carsAllowed + "_" + bikesAllowed;
	}
	
}
