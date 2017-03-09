package dk.itu.n.danmarkskort.address;

public class Address{
	private int nodeId;
	private long lat;
	private long lon;
	private String housenumber;
	private String housename;
	private String flats;
	private String conscriptionnumber;
	private String street;
	private String place;
	private String postcode;
	private String city;
	private String country;
	private String full;
	private String door;
	private String unit;
	private String floor;
	private String name;

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

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }

	public String getHousename() { return housename; }
	public void setHousename(String housename) { this.housename = housename; }

	public String getFlats() { return flats; }
	public void setFlats(String flats) { this.flats = flats; }

	public String getConscriptionnumber() { return conscriptionnumber; }
	public void setConscriptionnumber(String conscriptionnumber) { this.conscriptionnumber = conscriptionnumber; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getPlace() { return place; }
 	public void setPlace(String place) { this.place = place; }

 	public String getPostcode() { return postcode; }
 	public void setPostcode(String postcode) { this.postcode = postcode; }

 	public String getCity() { return city; }
 	public void setCity(String city) { this.city = city; }

 	public String getCountry() { return country; }
 	public void setCountry(String country) { this.country = country; }

 	public String getFull() { return full; }
 	public void setFull(String full) { this.full = full; }

 	public String getDoor() { return door; }
 	public void setDoor(String door) { this.door = door; }

 	public String getUnit() { return unit; }
 	public void setUnit(String unit) { this.unit = unit; }

 	public String getFloor() { return floor; }
 	public void setFloor(String floor) { this.floor = floor; }
 
 	public String getName() { return name; }
 	public void setName(String name) { this.name = name; }

@Override
public String toString() {
	return "Address [housenumber=" + housenumber + ", housename=" + housename + ", flats=" + flats
			+ ", conscriptionnumber=" + conscriptionnumber + ", street=" + street + ", place=" + place + ", postcode="
			+ postcode + ", city=" + city + ", country=" + country + ", full=" + full + ", door=" + door + ", unit="
			+ unit + ", floor=" + floor + "]";
}
}