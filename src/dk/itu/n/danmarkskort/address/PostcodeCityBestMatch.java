package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class PostcodeCityBestMatch {
	private List<Combine> combinations;
	private Map<String, Combine> objMatches;
	
	public PostcodeCityBestMatch(){
		init();
	}
	
	private void init(){
		combinations = new ArrayList<Combine>(1200); // Initialize with expected combinations for Denmark
		objMatches = new HashMap<String, Combine>(1200); // Initialize with expected combinations for Denmark
	}
	
	/**
	 * Add a new combinations or increments if combination already exists.
	 * 
	 * @param postcode
	 * @param city
	 */
	public void add(String postcode, String city){
		if(combinations == null  || objMatches == null) init(); // If not Initialized, reInitialize.
		
		if(isValidPostcode(postcode) && isValidCity(city)){
			if(!incrementCountIfExists(postcode, city)){
				combinations.add(new Combine(postcode, city));
			}
		}
	}
	
	private boolean isValidPostcode(String postcode){
		if(postcode != null && !postcode.trim().isEmpty()) return true;
		return false;
	}
	
	private boolean isValidCity(String city){
		if(city != null && !city.trim().isEmpty())	return true;
		return false;
	}
	
	private boolean incrementCountIfExists(String postcode, String city){
		for(Combine c : combinations){
			if(c.getPostcode().equals(postcode) && c.getCity().equals(city)){
				c.increment();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generate the best matches
	 */
	private void genMatches(){
		for(Combine c : combinations) objMatches.putIfAbsent(c.getPostcode(), c);
		for(Combine c : combinations)
			if(c.getCount() > objMatches.get(c.getPostcode()).getCount()) objMatches.put(c.getPostcode(), c);
	}
	
	/**
	 * Return the bestmatch stored for the postcode.
	 * 
	 * @param postcode
	 * @return best match for the postcode
	 */
	public String getMatch(String postcode){
		if(objMatches.isEmpty()) genMatches();
		if(objMatches.containsKey(postcode)) return objMatches.get(postcode).getCity();
		return null;
	}
	
	/**
	 * Freeup memory.
	 */
	public void cleanUp(){
		combinations = null;
		objMatches = null;
		
	}
	
	/**
	 * Represent a postcode, city relationship, count stores repetitions of the combination.
	 * 
	 * @author Group N
	 *
	 */
	private class Combine{
		private String postcode, city;
		private int count;
		
		public Combine(String postcode, String city){
			this.postcode = ReuseStringObj.make(postcode);
			this.city = ReuseStringObj.make(city);
			count = 1;
		}
		
		public String getPostcode(){ return postcode.toString(); }
		public String getCity(){ return city.toString(); }
		public int getCount(){ return count; }
	
		public void increment(){ count++; }
		
		public String toString(){
			return getPostcode() + " " + getCity() + " Count: " + getCount();
		}
	}
}
