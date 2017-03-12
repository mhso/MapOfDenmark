package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;

public class Housenumber extends ArrayList<Address>{
	private String housenumber;

	public Housenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	public String getHousenumber() {
		return housenumber;
	}

	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}
	
	

}
