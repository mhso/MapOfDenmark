package dk.itu.n.danmarkskort.address;

import java.util.TreeMap;

public class Postcode extends TreeMap<String, Street>{
	private String postcode;
	private String city;
	
	public Postcode(String postcode, String city){
		this.setPostcode(postcode);
		this.setCity(city);
	}

	public String getPostcode() { return postcode; }
	public void setPostcode(String postcode) { this.postcode = postcode; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
}
