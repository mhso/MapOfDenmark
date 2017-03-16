package dk.itu.n.danmarkskort.models;

public class ParsedBounds extends ParsedObject {

	public double minLat, minLong, maxLat, maxLong;
	public final double CONST_X = 0.01;
	public final double CONST_Y = 0.005;
	
	public void parseAttributes() {
		minLat = Double.parseDouble(attributes.get("minlat"));
		minLong = Double.parseDouble(attributes.get("minlon"));
		maxLat = Double.parseDouble(attributes.get("maxlat"));
		maxLong = Double.parseDouble(attributes.get("maxlon"));
	}

	public String toString() {
		return "minLat=" + minLat + ", minLong=" + minLong + ", maxLat=" + maxLat + ", maxLong=" + maxLong;
	}

	public double getWidth() {
		return maxLong - minLong;
	}
	
	public double getHeight() {
		return maxLat - minLat;
	}
	
	public int getHorizontalTileCount() {
		return (int)(getWidth() / CONST_X);
	}
	
	public int getVerticalTileCount() {
		return (int)(getHeight() / CONST_Y);
	}
	
}
