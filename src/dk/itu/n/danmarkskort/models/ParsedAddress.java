package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.newmodels.ParsedObject;

public class ParsedAddress extends ParsedObject {

	private String city, houseNumber, postCode, address;
	
	public ParsedAddress() {}
	public ParsedAddress(ParsedObject object) {
		super(object);
	}
	
	public String getCity() {
		return city;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public String getPostCode() {
		return postCode;
	}

	public void parseAttributes() {
		address = attributes.get("addr:street");
		city = attributes.get("addr:city");
		houseNumber = attributes.get("addr:housenumber");
		postCode = attributes.get("addr:postcode");
	}
	
	public String toString() {
		return address + ", " + houseNumber + ", " + postCode + ", " + city;
	}
	
}
