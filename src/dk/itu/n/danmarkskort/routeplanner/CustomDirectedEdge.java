package dk.itu.n.danmarkskort.routeplanner;

public class CustomDirectedEdge {
	private final CustomVertex from, to;
	private int maxSpeed;
	private final boolean forwardAllowed, backwardAllowed;

	public CustomDirectedEdge(CustomVertex from, CustomVertex to, int maxSpeed, boolean forwardAllowed, boolean backwardAllowed){
		this.from = from;
		this.to= to;
		this.maxSpeed = maxSpeed;
		this.forwardAllowed = forwardAllowed;
		this.backwardAllowed = backwardAllowed;
	}
	
	public int getMaxSpeed() { return maxSpeed; }

	public CustomVertex getFrom() { return from; }
	public CustomVertex getTo() { return to; }
	public boolean isForwardAllowed() { return forwardAllowed; }
	public boolean isBackwardAllowed() {return backwardAllowed;	}

	private double distance(){
		return Math.sqrt(Math.pow((to.getX() - from.getY()), 2) + Math.pow((to.getY() - from.getY()), 2));
	}
	
	public double weightDistance(){ return distance(); }
	
	public double weightBySpeed(){ return distance() / (double)maxSpeed; }
}
