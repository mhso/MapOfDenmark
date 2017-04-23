package dk.itu.n.danmarkskort.routeplanner;

import dk.itu.n.danmarkskort.models.PointFloat;

public class RouteVertex extends PointFloat {
	private final int id;
	
	RouteVertex(int id, PointFloat point){
		super(point.x, point.y);
		this.id = id;
	}

	public int getId() { return id; }
	public float getX() { return x; }
	public float getY(){ return y; }
	
	public String toString(){
		return "Id: " + id + ", " + "Point[" + x + ", " + y + "]";
	}
}
