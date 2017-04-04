package dk.itu.n.danmarkskort.newmodels;

public class ParsedBounds {

	public double minLat, minLong, maxLat, maxLong;
	public final double CONST_X = 0.01;
	public final double CONST_Y = 0.0055;
	
	public ParsedBounds() {}
	public ParsedBounds(double minLat, double minLong, double maxLat, double maxLong) {
		this.minLat = minLat;
		this.minLong = minLong;
		this.maxLat = maxLat;
		this.maxLong = maxLong;
	}

	public String toString() {
		return "x: " + minLong + ", y: " + minLat + ", width: " + getWidth() + ", height: " + getHeight();
	}

	public double getWidth() {
		return maxLong - minLong;
	}
	
	public double getHeight() {
		return maxLat - minLat;
	}
	
}
