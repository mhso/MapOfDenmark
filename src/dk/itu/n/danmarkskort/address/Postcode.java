package dk.itu.n.danmarkskort.address;

import java.util.TreeMap;

public class Postcode extends TreeMap<String, Street>{
	private String city;
	
	public Postcode(String city){
		this.setCity(city);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
