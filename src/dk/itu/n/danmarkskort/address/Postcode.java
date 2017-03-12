package dk.itu.n.danmarkskort.address;

import java.util.HashMap;

public class Postcode extends HashMap<String, Street>{
	private String city;
	
	public Postcode(String city){
		this.city = city;
	}
}
