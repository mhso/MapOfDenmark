package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

import dk.itu.n.danmarkskort.Util;

public class PointFloat {
	public float x;
	public float y;
	
	public PointFloat(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public double getDistance(PointFloat to){
		return Math.sqrt(Math.pow((to.x - this.x), 2) + Math.pow((to.y - this.y), 2));
	}
	
	public float[] getMiddlePoint(PointFloat to){
		return new float[] {((this.x + to.x) / 2), ((this.y + to.y) / 2)};
	}
	
	public String toString(){
		return "[" + x + ", " + y + "]";
	}
	
	public boolean isEqualPoint(PointFloat to){
		if(x == to.x && y == to.y) return true;
		return false;
	}
	
	public PointFloat getRealCords(){
		Point2D result = Util.toRealCoords(new Point2D.Float(x, y));
		return new PointFloat((float)result.getX(), (float)result.getY());
	}
	
	public PointFloat getFakeCords(){
		Point2D result = Util.toFakeCoords(new Point2D.Float(x, y));
		return new PointFloat((float)result.getX(), (float)result.getY());
	}
	
}
