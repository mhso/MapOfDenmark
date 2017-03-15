package dk.itu.n.danmarkskort.address;

public class Address{
	private long nodeId;
	private float lat, lon;
	
	// Relevant parts off OpenStreetMaps addr: tags
	private String street, housenumber, city, housename;
	private int postcode;

	public Address(){
		this.nodeId = -1l;
		this.lat = -1f;
		this.lon = -1f;
		this.postcode = -1;
	}
	
	public Address(long nodeId, float lat, float lon){
		this.nodeId = nodeId;
		this.lat = lat;
		this.lon = lon;
		this.postcode = -1;
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
	
 	public int getPostcode() { return postcode; }
 	public void setPostcode(int postcode) { this.postcode = postcode; }

 	public String getCity() { return city; }
 	public void setCity(String city) { this.city = city; }

	public String getHousename() { return housename; }
	public void setHousename(String housename) { this.housename = housename; }
 	
 	public String toStringShort(){
 		StringBuilder sb = new StringBuilder();
 			if(street != null) sb.append(street +" ");
 			if(housenumber != null) sb.append(housenumber + " ");
 			if(postcode != -1) sb.append(postcode + " ");
 			if(city != null) sb.append(city);
 		return sb.toString().trim();
 	}

	@Override
	public String toString() {
		return "Address [nodeId=" + nodeId + ", lat=" + lat + ", lon=" + lon + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + ", housename="+ housename + "]";
	}
}