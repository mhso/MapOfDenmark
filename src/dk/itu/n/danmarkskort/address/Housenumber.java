package dk.itu.n.danmarkskort.address;

public class Housenumber {
	private float[] lonLat;
	//private String housenumber;
	private StringObj housenumberObj;
	private Postcode postcode;
	private Street street;
	
	public Housenumber(Postcode postcode, Street street, String housenumber, float[] lonLat){
		this.postcode = postcode;
		this.street = street;
		this.housenumberObj = StringHolder.make(housenumber);
		this.lonLat = lonLat;
	}

	public float[] getLonLat() { return lonLat; }

	public String getHousenumber() { return housenumberObj.toString(); }

	public Postcode getPostcode() { return postcode; }

	public Street getStreet() { return street; }
	
	public String toString(){
		return street.getStreet() + " " + getHousenumber() + ", " + postcode.getPostcode() + " " + postcode.getCity();
	}
}