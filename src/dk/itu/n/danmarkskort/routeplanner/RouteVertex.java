package dk.itu.n.danmarkskort.routeplanner;

import java.awt.geom.Point2D;

public class RouteVertex extends Point2D.Float {
	private final int id;
	
	RouteVertex(int id, Point2D.Float point){
		super(point.x, point.y);
		this.id = id;
	}

	public int getId() { return id; }
	
	public String toString(){
		return "Id: " + id + ", " + "Point[" + x + ", " + y + "]";
	}
}
