package dk.itu.n.danmarkskort.address;

import java.util.HashMap;

public class Postcode extends HashMap<String, Street>{
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
