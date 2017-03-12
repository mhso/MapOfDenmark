package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;

public class AddressLookup {
	private Map<String, Country> countries;
	
	public AddressLookup(String country){
		countries = new HashMap<String, Country>();
	}
	
	public Map<String, Country> getCountries(){
		return countries;
	}
}
