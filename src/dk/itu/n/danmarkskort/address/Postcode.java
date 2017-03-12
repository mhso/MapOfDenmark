package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;

public class Postcode extends ArrayList<Street>{
	private String postcode;
	
	public Postcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}
