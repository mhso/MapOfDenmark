package dk.itu.n.danmarkskort.address;

public class Address{
	private float[] lonLat;
	private String street, housenumber, city, postcode;

	public Address(){
		this.lonLat = new float[]{-1f,-1f};
	}
	
	public Address(float[] lonlat){
		this.lonLat = lonlat;
	}
	
	public float[] getLonLat() { return lonLat; }
	public void setLonLat(float[] latLon) { this.lonLat = latLon; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getHousenumber() { return housenumber; }
	public void setHousenumber(String housenumber) { this.housenumber = housenumber; }
	
 	public String getPostcode() { return postcode; }
 	public void setPostcode(String postcode) { this.postcode = postcode; }

 	public void setCity(String city) { this.city = city; }
 	public String getCity() {
 		if(city != null) { 
 			return city;
 		};
 		return PostcodeCityCombination.getInstance().getCity(postcode);
 	}
 	
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
		return "Address [lon=" + lonLat[0] + ", lat=" + lonLat[1] + ", street=" + street + ", housenumber="
				+ housenumber + ", postcode=" + postcode + ", city=" + getCity() + "]";
	}
}