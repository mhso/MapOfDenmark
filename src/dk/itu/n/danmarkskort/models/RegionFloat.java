package dk.itu.n.danmarkskort.models;

import java.io.Serializable;

public class RegionFloat implements Serializable {
	private static final long serialVersionUID = -2303775702516883239L;
	public float x1, x2, y1, y2;
	
	public RegionFloat(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public String toString() {
		return "Region [x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + "]";
	}
	
	public boolean isWithin(RegionFloat largerRegion){
		if(largerRegion.x1 <= x1
				&& largerRegion.y1 <= y1
				&& largerRegion.x2 >= x2
				&& largerRegion.y1 >= y2) return true;
		return false;
	}
	
	public boolean isSinglePoint(){
		if(x1 == x2 && y1 == y2) return true;
		return false;
	}
	
	public float[] getMiddlePoint(){
		if(isSinglePoint()) return new float[] {x1, y1};
		return new float[] {((x1 + x2) / 2), ((y1 + y2) / 2)};
	}
	
	private double distance(){
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}
