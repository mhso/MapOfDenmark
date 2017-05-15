package dk.itu.n.danmarkskort.address;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class Street extends HashMap<String,Housenumber> implements Serializable {
	private static final long serialVersionUID = -2943891636428003556L;
	private String street;
	private Postcode postcode;
	
	Street(Postcode postcode, String street){
		super(50); // Initialize housenumber map with a average size per streets.
		this.setPostcode(postcode);
		this.street = ReuseStringObj.make(street);
	}
	
	public Postcode getPostcode() { return postcode; }
	public void setPostcode(Postcode postcode) { this.postcode = postcode; }
	
	public int count(){ return this.size(); }
	
	public String getStreet() { return street.toString(); }

	public Map<String, Housenumber> getHousenumbers() { return this; }
	
	/**
	 * Used for adding a housenumber and location to the datastructur.
	 * If housenumber exists, add location to existing housenumber object.
	 * 
	 * @param street
	 * @param housenumber
	 * @param latLon
	 */
	public void addHousenumber(String housenumber, Point2D.Float lonLat){
		if(housenumber != null && lonLat != null) {
			Housenumber hn = getHousenumber(housenumber);
			if(hn == null) hn = new Housenumber(postcode, this, housenumber, lonLat);
			this.put(ReuseStringObj.make(housenumber.toLowerCase()), hn);
		}
	}
	
	public Housenumber getHousenumber(String housenumber) {
		if(housenumber == null) return null;
		return this.get(housenumber.toLowerCase());
	}

	/**
	 * Narrows down a map input by checking if housenumbers key contains the find value.
	 * 
	 * @param input map of housenumbers to search in.
	 * @param find search string
	 * @return a map with housenumbers matching.
	 */
	private Map<String, Housenumber> housenumberContains(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(entry.getKey().contains(housenumber.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if housenumbers key starts with find value.
	 * 
	 * @param input map of housenumbers to search in.
	 * @param find search string
	 * @return a map with housenumbers matching.
	 */
	private Map<String, Housenumber> housenumberStartsWith(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(entry.getKey().startsWith(housenumber.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if housenumber key is within LevenshteinDistance 1-2 of the find value.
	 * 
	 * @param input map of housenumbers to search in.
	 * @param find search string
	 * @return a map with housenumbers matching.
	 */
	private Map<String, Housenumber> housenumberLevenshteinDistance(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		int minValue = 0, maxValue = 3;
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Method is used to make the search for housenumbers, by criteria for all parts.
	 * 
	 * @param input map of housenumbers to search in.
	 * @param addr, address object with search values.
	 * @param streetType
	 * @return a map with housenumbers matching.
	 */
	public Map<String, Housenumber> search(Map<String, Housenumber> input, Address addr, SearchEnum housenumberType){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
			switch(housenumberType){
			case CONTAINS:
				result = housenumberContains(input, addr.getHousenumber());
				break;
			case EQUALS:
				if(getHousenumber(addr.getHousenumber()) != null){
					Housenumber hn = getHousenumber(addr.getHousenumber());
					result.put(hn.getHousenumber(), hn);
				}
				break;
			case ANY:
				result = input;
				break;
			case STARTSWITH:
				result = housenumberStartsWith(input, addr.getHousenumber());
				break;
			case LEVENSHTEIN:
				result = housenumberLevenshteinDistance(input, addr.getHousenumber());
				break;
			default:
				break;
		}
		return result;
	}
}