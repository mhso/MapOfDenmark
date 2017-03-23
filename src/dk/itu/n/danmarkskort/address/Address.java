package dk.itu.n.danmarkskort.address;

public class Address{
	private long nodeId;
	private float[] lonLat;
	
	// Relevant parts off OpenStreetMaps addr: tags
	private String street, housenumber, city;

	private int postcode;

	public Address(){
		this.nodeId = -1L;
		this.lonLat = new float[]{-1f,-1f};
		this.postcode = -1;
	}
	
	public Address(long nodeId, float lat, float lon){
		this.nodeId = nodeId;
		this.lonLat = new float[]{lon, lat};
		this.postcode = -1;
	}
	
	public long getNodeId() { return nodeId; }
	public void setNodeId(long nodeId) { this.nodeId = nodeId; }
	
	public float[] getLonLat() { return lonLat; }
	public void setLonLat(float[] latLon) { this.lonLat = latLon; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
	
 	public int getPostcode() { return postcode; }
 	public void setPostcode(int postcode) { this.postcode = postcode; }

 	public void setCity(String city) { this.city = city; }
 	public String getCity() {
 		if(city != null) return city;
 		return PostcodeCityCombination.getInstance().getCity(postcode);
 	}
 	
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
		return "Address [nodeId=" + nodeId + ", lat=" + lonLat[0] + ", lon=" + lonLat[1] + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + city + "]";
	}
}