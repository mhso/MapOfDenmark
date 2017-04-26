package dk.itu.n.danmarkskort.address;

import java.awt.geom.Point2D;

public class Address{
	private float lon, lat;
	private String street, housenumber, city, postcode;

	public Address(){
		this.lon = -1f;
		this.lat = -1f;
	}
	
	public Address(float[] lonLat){
		this.lon = lonLat[0];
		this.lat = lonLat[1];
	}
	
	public Address(float[] lonLat, String street, String housenumber, String city, String postcode) {
		this(lonLat);
		this.street = street;
		this.housenumber = housenumber;
		this.city = city;
		this.postcode = postcode;
	}
	
	public Address(String street, String housenumber, String city, String postcode) {
		this.housenumber = housenumber;
		this.city = city;
		this.postcode = postcode;
	}

	public float[] getLonLat() { return new float[] {lon, lat}; }
	public Point2D getLonLatAsPoint() {return new Point2D.Float(lon, lat);}
	public void setLonLat(float[] lonLat) {
		this.lon = lonLat[0];
		this.lat = lonLat[1];
		}
	
	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
	
 	public String getPostcode() { return postcode; }
 	public void setPostcode(String postcode) { this.postcode = postcode; }

 	public void setCity(String city) { this.city = city; }
 	public String getCity() { return city; }
 	
 	public String toStringShort(){
 		StringBuilder sb = new StringBuilder();
 			if(street != null) sb.append(street +" ");
 			if(housenumber != null) sb.append(housenumber + " ");
 			if(postcode != null) sb.append(postcode + " ");
 			if(getCity() != null) sb.append(getCity());
 		return sb.toString().trim();
 	}

	@Override
	public String toString() {
		return "Address [lon=" + lon + ", lat=" + lat + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + "]";
	}
}