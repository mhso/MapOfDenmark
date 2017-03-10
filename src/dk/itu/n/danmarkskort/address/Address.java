package dk.itu.n.danmarkskort.address;

public class Address{
	private int nodeId;
	private long lat;
	private long lon;
	
	// Relevant parts off OpenStreetMaps addr: tags
	private String street;
	private String housenumber;
	private String postcode;
	private String city;
	private String country;
	private String housename;
	
	// Not part off OpenStreetMaps addr: tags
	private String floor;
	private String doorSide;

	public Address(){
		this.nodeId = -1;
		this.lat = -1;
		this.lon = -1;
	 
	}
	
	public Address(int nodeId, long lat, long lon){
		this.nodeId = nodeId;
		this.lat = lat;
		this.lon = lon;
	 
	}
	
	public int getNodeId() { return nodeId; }
	public void setNodeId(int nodeId) { this.nodeId = nodeId; }
	public long getLat() { return lat; }
	public void setLat(long lat) { this.lat = lat; }
	public long getLon() { return lon; }
	public void setLon(long lon) { this.lon = lon; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
	
 	public String getFloor() { return floor; }
 	public void setFloor(String floor) { this.floor = floor; }
 	
 	public String getDoorSide() { return doorSide; }
 	public void setDoorSide(String doorSide) { this.doorSide = doorSide; }
	
 	public String getPostcode() { return postcode; }
 	public void setPostcode(String postcode) { this.postcode = postcode; }

 	public String getCity() { return city; }
 	public void setCity(String city) { this.city = city; }

	public String getHousename() { return housename; }
	public void setHousename(String housename) { this.housename = housename; }

 	public String getCountry() { return country; }
 	public void setCountry(String country) { this.country = country; }

	@Override
	public String toString() {
		return "Address [nodeId=" + nodeId + ", lat=" + lat + ", lon=" + lon + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + ", country=" + country + ", housename="
				+ housename + ", floor=" + floor + ", doorSide=" + doorSide + "]";
	}
}