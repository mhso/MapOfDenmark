package dk.itu.n.danmarkskort.routeplanner;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class RouteEdge {
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
	public boolean isBackwardAllowed() {return routeEdgeMeta.isBackwardAllowed();	}
	public String getDescription(){ return description; }

	private double distance(){ return from.getDistance(to); }
	
	public double getWeightByDistance(){ return distance(); }
	public double getWeightBySpeed(){ return distance() / (double)routeEdgeMeta.getMaxSpeed(); }
	
	public double getWeight(WeightEnum weightEnum){
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
		return from.getId() + " [" + from.toString() + "] ->" + to.getId()+ " [" + to.toString() + "]\n";
	}
	
	public String toStringDesr() {
		return from.getId() + " -> " + to.getId() + " [ " + description + " ]";
	}
}
