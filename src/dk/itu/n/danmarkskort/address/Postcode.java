package dk.itu.n.danmarkskort.address;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

/**
 * Class represent a postcode, with cityname, and a list/map of streets
 * includes search methods for streets.
 * @author Group N
 *
 */
public class Postcode  extends HashMap<String,Street> implements Serializable {
	private static final long serialVersionUID = 2217407726502936291L;
	private String postcode, city;
	
	Postcode(String postcode, String city){
		super(200);
		this.postcode = ReuseStringObj.make(postcode); // Reuse string object, for space saving
		setCity(city);
	}

	public String getCity() { return city.toString(); }
	public void setCity(String city) { this.city = ReuseStringObj.make(city); }
	
	/**
	 * Return a count of all housenumbers with in all streets.
	 * @return count of all housenumbers.
	 */
	public int count(){
		int size = 0;
		for(Street st : this.values()) size += st.count();
		return size;
	}
	
	/**
	 * Used for adding a new street, to the datastructur.
	 * 
	 * @param street
	 */
	public void addStreet(String street){
		Street newStreet = getStreet(street);
		if(newStreet == null) {
			newStreet = new Street(this, street);
			this.put(ReuseStringObj.make(street.toLowerCase()), newStreet);
		}
	}
	
	/**
	 * Used for adding a street, housenumber and location to the datastructur.
	 * If street exists, add housenumber to existing street object.
	 * 
	 * @param street
	 * @param housenumber
	 * @param latLon
	 */
	public void addAddress(String street, String housenumber, Point2D.Float latLon){
		if(street != null && housenumber != null && latLon != null){
			Street st = getStreet(street);
			if(st == null) st = new Street(this, street);
			st.addHousenumber(housenumber, latLon);
			this.put(ReuseStringObj.make(street.toLowerCase()), st);
		}
	}

	public Map<String, Street> getStreets() { return this; }
	
	public Street getStreet(String street) {
		if(street == null) return null;
		return this.get(street.toLowerCase());
	}

	public String getPostcode() { return postcode.toString(); }
	
	/**
	 * Narrows down a map input by checking if streets key contains the find value.
	 * 
	 * @param input map of streets to search in.
	 * @param find search string
	 * @return a map with streets matching.
	 */
	private Map<String, Street> streetContains(Map<String, Street> input, String street){
		Map<String, Street> result = new HashMap<String, Street>();
		if(street == null) return result;
		for(Entry<String, Street> entry : input.entrySet()){
			if(entry.getKey().contains(street.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if streets key starts with find value.
	 * 
	 * @param input map of streets to search in.
	 * @param find search string
	 * @return a map with streets matching.
	 */
	private Map<String, Street> streetStartsWith(Map<String, Street> input, String find){
		Map<String, Street> result = new HashMap<String, Street>();
		if(find == null) return result;
		for(Entry<String, Street> entry : input.entrySet()){
			if(entry.getKey().startsWith(find.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if street key is within LevenshteinDistance 1-2 of the find value.
	 * 
	 * @param input map of streets to search in.
	 * @param find search string
	 * @return a map with streets matching.
	 */
	private Map<String, Street> streetLevenshteinDistance(Map<String, Street> input, String find){
		Map<String, Street> result = new HashMap<String, Street>();
		int minValue = 0, maxValue = 3;
		if(find == null) return result;
		for(Entry<String, Street> entry : input.entrySet()){
			try {
				String matchKey = entry.getKey().substring(0, find.length());
				if(StringUtils.getLevenshteinDistance(matchKey, find.toLowerCase()) > minValue &&
						StringUtils.getLevenshteinDistance(matchKey, find.toLowerCase()) < maxValue) {
					result.put(entry.getKey(), entry.getValue());
				}
			} catch (StringIndexOutOfBoundsException e) {

			}
		}
		return result;
	}
	
	/**
	 * Method is used to make the search for streets, by criteria for all parts.
	 * 
	 * @param input map of streets to search in.
	 * @param addr, address object with search values.
	 * @param streetType
	 * @return a map with streets matching.
	 */
	public Map<String, Street> search(Map<String, Street> input, Address addr, SearchEnum streetType){
		Map<String, Street> result = new HashMap<String, Street>();
			switch(streetType){
			case CONTAINS:
				result = streetContains(input, addr.getStreet());
				break;
			case EQUALS:
				if(getStreet(addr.getStreet()) != null){
					Street st = getStreet(addr.getStreet());
					result.put(st.getStreet(), st);
				}
				break;
			case ANY:
				result = input;
				break;
			case STARTSWITH:
				result = streetStartsWith(input, addr.getStreet());
				break;
			case LEVENSHTEIN:
				result = streetLevenshteinDistance(input, addr.getStreet());
				break;
			default:
				break;
		}
		return result;
	}
}