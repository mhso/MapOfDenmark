package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

import java.io.Serializable;

public class Housenumber implements Serializable {
	private static final long serialVersionUID = -8400619618776493401L;
	private float lon;
	private float lat;
	private String housenumber;
	private Street street;
	
	public Housenumber(Postcode postcode, Street street, String housenumber, float[] lonLat){
		this.street = street;
		this.housenumber = ReuseStringObj.make(housenumber);
		this.lon = lonLat[0];
		this.lat = lonLat[1];
	}

	public float[] getLonLat() { return new float[] {lon, lat}; }

	public Street getStreet() { return street; }
	
	public String getHousenumber() { return housenumber.toString(); }

	public Postcode getPostcode() { return street.getPostcode(); }
	
	public String toString(){
		return street.getStreet() + " " + getHousenumber() + ", " + getPostcode().getPostcode() + " " + getPostcode().getCity();
	}

	private ParsedNode coordsToNode() {
	    return new ParsedNode(lon, lat);
    }
}