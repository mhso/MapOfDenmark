package dk.itu.n.danmarkskort.address;

public class Housenumber {
	private float[] lonLat;
	private String housenumber;
	private Postcode postcode;
	private Street street;
	
	public Housenumber(Postcode postcode, Street street, String housenumber, float[] lonLat){
		this.postcode = postcode;
		this.street = street;
		this.housenumber = housenumber;
		this.lonLat = lonLat;
	}

	public float[] getLonLat() { return lonLat; }

	public String getHousenumber() { return housenumber; }

	public Postcode getPostcode() { return postcode; }

	public Street getStreet() { return street; }
	
	public String toString(){
		return street.getStreet() + " " + this.housenumber + ", " + postcode.getPostcode() + " " + postcode.getCity();
	}
}