package dk.itu.n.danmarkskort.address;

public class Housenumber {
	private float[] lonLat;
	private String housenumber;
	
	public Housenumber(String housenumber, float[] lonLat){
		this.housenumber = housenumber;
		this.lonLat = lonLat;
	}

	public float[] getLonLat() {
		return lonLat;
	}

	public String getHousenumber() {
		return housenumber;
	}
}
