package dk.itu.n.danmarkskort.models;

public class ParsedAddress extends ParsedObject {

	private String city, houseNumber, postCode;
	
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
		city = attributes.get("addr:city");
		houseNumber = attributes.get("addr:housenumber");
		postCode = attributes.get("addr:postcode");
	}
	
}
