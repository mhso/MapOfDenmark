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
		combinations = new ArrayList<Combine>(5000);
		objMatches = new HashMap<String, Combine>(5000);
	}
	
	public void add(String postcode, String city){
		
		if(combinations == null  || objMatches == null) init();
		
		if(isValidPostcode(postcode) && isValidCity(city)){
			if(!incrementCountIfExists(postcode, city)){
				addIfNew(postcode, city);
			}
		}
	}
	
	private boolean isValidPostcode(String postcode){
		if(postcode != null && !postcode.isEmpty()) return true;
		return false;
	}
	
	private boolean isValidCity(String city){
		if(city != null && !city.isEmpty())	return true;
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
	
	private void addIfNew(String postcode, String city){
		Combine cNew = new Combine(postcode, city);
		combinations.add(cNew);
	}
	
	private void genMatches(){
		for(Combine c : combinations) objMatches.putIfAbsent(c.getPostcode(), c);
		for(Combine c : combinations) if(c.getCount() > objMatches.get(c.getPostcode()).getCount()) objMatches.put(c.getPostcode(), c);
	}
	
	public String getMatch(String postcode){
		if(objMatches.isEmpty()) genMatches();
		if(objMatches.containsKey(postcode)) return objMatches.get(postcode).getCity();
		return null;
	}
	
	public void cleanup(){
		combinations = null;
		objMatches = null;
		
	}
	
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
