package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

public class ParsedAddress extends Point2D.Float {

    private String city, street, housenumber, postcode;
    
    public void setCity(String c) { city = ReuseStringObj.make(c); }
    public void setPostcode(String p) { postcode = ReuseStringObj.make(p); }
    public void setStreet(String s) { street = ReuseStringObj.make(s); }
    public void setHousenumber(String h) { housenumber = ReuseStringObj.make(h); }
    public void setCoords(Point2D.Float point) {
        x = point.x;
        y = point.y;
    }

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHousenumber() { return housenumber; }
    public String getPostcode() { return postcode; }

    public float getLon() { return x; }
    public float getLat() { return y; }
    
    public String toStringShort(){
 		StringBuilder sb = new StringBuilder();
 			if(street != null) sb.append(street +" ");
 			if(housenumber != null) sb.append(housenumber + " ");
 			if(postcode != null) sb.append(postcode + " ");
 			if(city != null) sb.append(city + " ");
 		return sb.toString().trim();
 	}
    
    public String toString(){
    	return street +" " + housenumber + ", " + postcode + " "+city;
    }
    
    public String toStringParted(){
    	return street + " | " + housenumber + " | "+ postcode + " | " + city;
    }
}
