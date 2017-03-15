package dk.itu.n.danmarkskort.address;

import java.util.TreeMap;

public class Postcode {
	private int postcode;
	private String city;
	private TreeMap<String, Street> streetsMap;
	
	public Postcode(int postcode, String city){
		this.setPostcode(postcode);
		this.setCity(city);
		streetsMap = new TreeMap<String, Street>();
	}

	public int getPostcode() { return postcode; }
	public void setPostcode(int postcode) { this.postcode = postcode; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	
	public TreeMap<String, Street> getStreets(){
		return streetsMap;
	}
}
