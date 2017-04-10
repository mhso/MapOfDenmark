package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedNode;

import java.awt.*;
import java.util.ArrayList;

public class Housenumber extends ParsedItem {
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

	private ParsedNode coordsToNode() {
	    return new ParsedNode(lonLat[0], lonLat[1]);
    }

	@Override
	public ParsedNode getFirstNode() {
	    return coordsToNode();
	}

	@Override
	public ArrayList<ParsedNode> getNodes() {
		ArrayList<ParsedNode> nodes = new ArrayList<>();
		nodes.add(coordsToNode());
		return nodes;
	}

	@Override
	public void makeShape() {}

	@Override
	public Shape getShape() { return null;}

	@Override
	public void deleteOldRefs() {}
}