package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;

public class Street extends ArrayList<Housenumber>{
	private String street;

	public Street(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}
