package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;
import java.io.Serializable;

// This class is a simple representation for latitude longtitude coordinates. 
public class Coordinate implements Serializable {

	private static final long serialVersionUID = -6165470892260851020L;
	public float latitude, longtitude;
	
	public Coordinate(float latitude, float longtitude) {
		this.latitude = latitude;
		this.longtitude = longtitude;
	}
	
	public float getLat() {return this.latitude;}
	public float getLong() {return this.longtitude;}
	public void setLat(float latitude) {this.latitude = latitude;}
	public void setLong(float longtitude) {this.longtitude = longtitude;}
	public Point2D.Float toPoint() {
		return new Point2D.Float(latitude, longtitude);
	}
}
