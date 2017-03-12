package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;

public class Country extends ArrayList<Postcode> {
	private String country;
	
	public Country(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
