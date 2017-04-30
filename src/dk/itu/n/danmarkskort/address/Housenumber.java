package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class Housenumber implements Serializable {
	private static final long serialVersionUID = -8400619618776493401L;
	private float lon, lat;
	private String housenumber;
	private Street street;
	
	public Housenumber(Postcode postcode, Street street, String housenumber, Point2D.Float lonLat){
		this.street = street;
		this.housenumber = ReuseStringObj.make(housenumber);
		this.lon = lonLat.x;
		this.lat = lonLat.y;
	}
	
	public Point2D.Float getLonLat(){ return new Point2D.Float(lon, lat); }
	public float getLon(){ return lon; }
	public float getLat(){ return lat; }
	public Street getStreet() { return street; }
	public String getHousenumber() { return housenumber.toString(); }
	public Postcode getPostcode() { return street.getPostcode(); }
	
	public String toString(){
		return street.getStreet() + " " + getHousenumber() + ", " + getPostcode().getPostcode() + " " + getPostcode().getCity();
	}
}