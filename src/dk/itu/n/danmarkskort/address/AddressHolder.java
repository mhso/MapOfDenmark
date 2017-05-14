package dk.itu.n.danmarkskort.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * This class holds a map of postcodes and gives access searching etc.
 * @author Group N
 *
 */
public class AddressHolder implements Serializable {
	private static final long serialVersionUID = 4946666884688610616L;
	public HashMap<String, Postcode> postcodes;
	
	public AddressHolder(){
		postcodes = new HashMap<String, Postcode>(1100); // Initializes a KDTree with leaf size of 1000 elements.
	}
	
	/**
	 * 
	 * @param postcode as string.
	 * @return a specific postcode object or null.
	 */
	public Postcode getPostcode(String postcode) {
		if(postcode == null) return null;
		return postcodes.get(postcode.toLowerCase());
	}
	
	/**
	 * Method runs through all postcodes, their streets and returns their Housenumbers.
	 * 
	 * @return a list of all Housenumber objects.
	 */
	public List<Housenumber> getHousenumbers(){
		List<Housenumber> result = new ArrayList<Housenumber>();
		for(Postcode postcode : postcodes.values()){
			for(Street street : postcode.getStreets().values()){
				result.addAll(street.getHousenumbers().values());
			}
		}
		return result;
	}
	
	/**
	 * Return a count of all housenumbers with in all postcodes.
	 * 
	 * @return count of all housenumbers.
	 */
	public int count(){
		int size = 0;
		for(Postcode pc : postcodes.values()) size += pc.count();
		return size;
	}
	
	/**
	 * Narrows down a map input by checking if postcode key contains find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> postcodeContains(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(find == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey().contains(find.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if postcode key starts with find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> postcodeStartsWith(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(find == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey().startsWith(find.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if postcode key is within LevenshteinDistance 1-2 of the find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> postcodeLevenshteinDistance(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(find == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey() != null && StringUtils.getLevenshteinDistance(entry.getKey(), find.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), find.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if city key contains the find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> cityContains(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().contains(find.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if city key starts with find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> cityStartsWith(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().startsWith(find.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if city key is within LevenshteinDistance 1-2 of the find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> cityLevenshteinDistance(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey() != null && find != null && StringUtils.getLevenshteinDistance(entry.getKey(), find.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), find.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Narrows down a map input by checking if city key is equal to the find value.
	 * 
	 * @param input map of postcodes to search in.
	 * @param find search string
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> cityEquals(Map<String, Postcode> input, String find){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().equalsIgnoreCase(find)) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * Method for searching through postcodes, by criteria.
	 * 
	 * @param input map of postcodes to search in.
	 * @param addr, address object with seach values.
	 * @param postcodeType, searchType for postcode.
	 * @param cityType, searchType for city.
	 * @return  a map with postcodes matching.
	 */
	private Map<String, Postcode> searchPostcode(Map<String, Postcode> input, Address addr, SearchEnum postcodeType, SearchEnum cityType){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(addr == null) return result;
			switch(postcodeType){
			case CONTAINS:
				result = postcodeContains(input, addr.getPostcode());
				break;
			case EQUALS:
				if(getPostcode(addr.getPostcode()) != null){
					Postcode pc = getPostcode(addr.getPostcode());
					result.put(pc.getPostcode(), pc);
				}
				break;
			case ANY:
				result = input;
				break;
			case STARTSWITH:
				result = postcodeStartsWith(input, addr.getPostcode());
				break;
			case LEVENSHTEIN:
				result = postcodeLevenshteinDistance(input, addr.getPostcode());
				break;
			default:
				break;
			}
			result.putAll(searchCity(result, addr, cityType));
		return result;
	}
	
	/**
	 * Method for searching through postcode citynames, by criteria.
	 * 
	 * @param input map of postcodes to search in.
	 * @param addr, address object with seach values.
	 * @param cityType, searchType for city.
	 * @return a map with postcodes matching.
	 */
	private Map<String, Postcode> searchCity(Map<String, Postcode> input, Address addr, SearchEnum cityType){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
			switch(cityType){
			case CONTAINS:
				result = cityContains(input, addr.getCity());
				break;
			case EQUALS:
				result = cityEquals(input, addr.getCity());
				break;
			case ANY:
				result = input;
				break;
			case STARTSWITH:
				result = cityStartsWith(input, addr.getCity());
				break;
			case LEVENSHTEIN:
				result = cityLevenshteinDistance(input, addr.getCity());
				break;
			default:
				break;
		}
		return result;
	}
	
	
	/**
	 * Method is used to make the search for a complete address, by criteria for all parts.
	 * 
	 * @param addr, address object with search values.
	 * @param streetType
	 * @param housenumberType
	 * @param postcodeType
	 * @param cityType
	 * @return a map with postcodes matching.
	 */
	public Map<String, Postcode> search(Address addr,
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
			Map<String, Postcode> result = new HashMap<String, Postcode>();
			for(Postcode pc : searchPostcode(postcodes, addr, postcodeType, cityType).values()){
				for(Street st : pc.search(pc.getStreets(), addr, streetType).values()){
					for(Housenumber hn : st.search(st.getHousenumbers(), addr, housenumberType).values()) {
						searchToMap(result, hn);
					}
				}
			}
		return result;
	}
	
	/**
	 * Converts the search results of housenumber to map of Postcodes results.
	 * 
	 * @param input, result map (working collection)
	 * @param hn, housenumber object
	 */
	private void searchToMap(Map<String, Postcode> input, Housenumber hn){
		Postcode p = input.get(hn.getPostcode().getPostcode());
		if(p == null) p = new Postcode(hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
		p.addAddress(hn.getStreet().getStreet(), hn.getHousenumber(), hn.getLonLat());
		input.putIfAbsent(p.getPostcode(), p);
	}
}