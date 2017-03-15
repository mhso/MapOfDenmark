package dk.itu.n.danmarkskort.address;

import java.util.TreeMap;

public class Street {
	private String street;
	private TreeMap<String, Address> housenumbers;
	
	public Street(String street){
		this.setStreet(street);
		housenumbers = new TreeMap<String, Address>(String.CASE_INSENSITIVE_ORDER);
	}

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public TreeMap<String, Address> getHousenumbers() {
		return housenumbers;
	}
}
