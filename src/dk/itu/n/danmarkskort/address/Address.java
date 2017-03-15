package dk.itu.n.danmarkskort.address;

import java.awt.geom.Point2D;

public class Address{
	private long nodeId;
	private float lat;
	private float lon;
	
	// Relevant parts off OpenStreetMaps addr: tags
	private String street;
	private String housenumber;
	private String postcode;
	private String city;
	private String country;
	private String housename;

	public Address(){
		this.nodeId = -1l;
		this.lat = -1f;
		this.lon = -1f;
	}
	
	public Address(long nodeId, float lat, float lon){
		this.nodeId = nodeId;
		this.lat = lat;
		this.lon = lon;
	}
	
	public long getNodeId() { return nodeId; }
	public void setNodeId(long nodeId2) { this.nodeId = nodeId2; }
	
	public double getLat() { return lat; }
	public void setLat(float lat) { this.lat = lat; }
	
	public double getLon() { return lon; }
	public void setLon(float lon) { this.lon = lon; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
	
 	public String getPostcode() { return postcode; }
 	public void setPostcode(String postcode) { this.postcode = postcode; }

 	public String getCity() { return city; }
 	public void setCity(String city) { this.city = city; }

	public String getHousename() { return housename; }
	public void setHousename(String housename) { this.housename = housename; }

 	public String getCountry() { return country; }
 	public void setCountry(String country) { this.country = country; }
 	
 	public String toStringShort(){
 		StringBuilder sb = new StringBuilder();
 			if(street != null) sb.append(street +" ");
 			if(housenumber != null) sb.append(housenumber + " ");
 			if(postcode != null) sb.append(postcode + " ");
 			if(city != null) sb.append(city);
 		return sb.toString().trim();
 	}

	@Override
	public String toString() {
		return "Address [nodeId=" + nodeId + ", lat=" + lat + ", lon=" + lon + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + ", country=" + country + ", housename="
				+ housename + "]";
	}
}