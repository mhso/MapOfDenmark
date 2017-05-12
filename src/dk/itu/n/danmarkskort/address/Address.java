package dk.itu.n.danmarkskort.address;

import java.awt.geom.Point2D;

/**
 * Class is used for representing a complete or partial address used for handling information between data-structures.
 */
public class Address extends Point2D.Float{
	private String street, housenumber, city, postcode;

	public Address(){
		super(-1f, -1f);
	}
	
	public Address(Point2D.Float lonLat){
		this.setLocation(lonLat);
	}
	
	public Address(String street, String housenumber, String city, String postcode) {
		this.housenumber = housenumber;
		this.city = city;
		this.postcode = postcode;
	}
	
	public Address(Point2D.Float lonLat, String street, String housenumber, String city, String postcode) {
		this(street, housenumber, city, postcode);
		this.setLocation(lonLat);
	}

	public Point2D.Float getLonLatAsPoint() { return this; }
	public void setLonLat(Point2D.Float lonLat) { this.setLocation(lonLat); }
	
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
 			if(housenumber != null) sb.append(housenumber + ", ");
 			if(postcode != null) sb.append(postcode + " ");
 			if(getCity() != null) sb.append(getCity());
 		return sb.toString().trim();
 	}

	@Override
	public String toString() {
		return "Address [lon=" + x + ", lat=" + y + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + "]";
	}
}