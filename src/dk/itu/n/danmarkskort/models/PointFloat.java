package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.DKConstants;

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
		return calcRealCords(this);
	}
	
	public PointFloat calcRealCords(PointFloat input){
		float realLon = input.x / DKConstants.FACTOR_LON_DENMARK;
		float realLat = -(input.y);
		return new PointFloat(realLon, realLat);
	}
	
	public PointFloat calcFakeCords(PointFloat input){
		float realLon = input.x * DKConstants.FACTOR_LON_DENMARK;
		float realLat = -(input.y);
		return new PointFloat(realLon, realLat);
	}
	
	
}
