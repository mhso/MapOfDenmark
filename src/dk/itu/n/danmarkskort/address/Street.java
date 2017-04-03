package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class Street {
	private String street;
	private Map<String, Housenumber> housenumbers;
	private Postcode postcode;
		
	Street(Postcode postcode, String street){
		this.setPostcode(postcode);
		this.street = street;
		housenumbers = new HashMap<String, Housenumber>();
	}
	
	public int count(){
		return housenumbers.size();
	}

	public Map<String, Housenumber> getHousenumbers() {
		return housenumbers;
	}
	
	public void addHousenumber(String housenumber, float[] latLon){
		if(housenumber != null && latLon != null) {
			Housenumber newHousenumber = getHousenumber(housenumber);
			if(newHousenumber == null) newHousenumber = new Housenumber(postcode, this, housenumber, latLon);
			housenumbers.put(housenumber.toLowerCase(), newHousenumber);
		}
	}
	
	public Housenumber getHousenumber(String housenumber) {
		if(housenumber == null) return null;
		return housenumbers.get(housenumber.toLowerCase());
	}

	public String getStreet() {
		return street;
	}

	
	private Map<String, Housenumber> housenumberContains(Map<String, Housenumber> inputList, String housenumber){
		Map<String, Housenumber> list = new HashMap<String, Housenumber>();
		if(housenumber == null) return list;
		for(Entry<String, Housenumber> entry : inputList.entrySet()){
			if(entry.getKey().contains(housenumber.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private Map<String, Housenumber> housenumberStartsWith(Map<String, Housenumber> inputList, String housenumber){
		Map<String, Housenumber> list = new HashMap<String, Housenumber>();
		if(housenumber == null) return list;
		for(Entry<String, Housenumber> entry : inputList.entrySet()){
			if(entry.getKey().startsWith(housenumber.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private Map<String, Housenumber> housenumberLevenshteinDistance(Map<String, Housenumber> inputList, String housenumber){
		Map<String, Housenumber> list = new HashMap<String, Housenumber>();
		int minValue = 0, maxValue = 3;
		if(housenumber == null) return list;
		for(Entry<String, Housenumber> entry : inputList.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) < maxValue) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	public Map<String, Housenumber> search(Map<String, Housenumber> inputList, Address addr, SearchEnum housenumberType){
		Map<String, Housenumber> list = new HashMap<String, Housenumber>();
			switch(housenumberType){
			case CONTAINS:
				list = housenumberContains(inputList, addr.getHousenumber());
				break;
			case EQUALS:
				if(getHousenumber(addr.getHousenumber()) != null){
					Housenumber hn = getHousenumber(addr.getHousenumber());
					list.put(hn.getHousenumber(), hn);
				}
				break;
			case NOT_IN_USE:
				list = inputList;
				break;
			case STARTSWITH:
				list = housenumberStartsWith(inputList, addr.getHousenumber());
				break;
			case LEVENSHTEINDISTANCE:
				list = housenumberLevenshteinDistance(inputList, addr.getHousenumber());
				break;
			default:
				break;
			
		}
		return list;
	}

	public Postcode getPostcode() {
		return postcode;
	}

	public void setPostcode(Postcode postcode) {
		this.postcode = postcode;
	}
}
