package dk.itu.n.danmarkskort.address;

public class Housenumber {
	private float[] lonLat;
	private StringObj housenumberObj;
	private Street street;
	
	public Housenumber(Postcode postcode, Street street, String housenumber, float[] lonLat){
		this.street = street;
		this.housenumberObj = StringHolder.make(housenumber);
		this.lonLat = lonLat;
	}

	public float[] getLonLat() { return lonLat; }

	public Street getStreet() { return street; }
	
	public String getHousenumber() { return housenumberObj.toString(); }

	public Postcode getPostcode() { return street.getPostcode(); }
	
	public String toString(){
		return street.getStreet() + " " + getHousenumber() + ", " + getPostcode().getPostcode() + " " + getPostcode().getCity();
	}
}