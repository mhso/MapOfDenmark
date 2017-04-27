package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

public class ParsedAddress {

    private String city, street, housenumber, postcode;
    private float[] coords;
    private ParsedWay way;
    private ParsedRelation relation;

    public void setCity(String c) { city = c; }
    public void setPostcode(String p) { postcode = p; }
    public void setStreet(String s) { street = s; }
    public void setHousenumber(String h) { housenumber = h; }
    public void setWay(ParsedWay w) { way = w; }
    public void setRelation(ParsedRelation r) { relation = r; }
    public void setCoords(Point2D.Float point) {
        coords = new float[2];
        coords[0] = point.x;
        coords[1] = point.y;
    }

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHousenumber() { return housenumber; }
    public String getPostcode() { return postcode; }
    public float[] getCoords() { return coords; }
    public ParsedWay getWay() { return way; }
    public ParsedRelation getRelation() { return relation; }

    public float getFirstLon() {
        if(coords != null) return coords[0];
        else if (way != null) return (float) way.getFirstNode().x;
        else if(relation != null) return (float) relation.getFirstNode().getX();
        return -1;
    }

    public float getFirstLat() {
        if(coords != null) return coords[1];
        else if (way != null) return (float) way.getFirstNode().getY();
        else if(relation != null) return (float) relation.getFirstNode().getY();
        return -1;
    }
    
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
