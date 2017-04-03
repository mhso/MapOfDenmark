package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Street {
	private String street;
	private Map<String, Housenumber> housenumbers;
		
	Street(String street){
		this.street = street;
		housenumbers = new HashMap<String, Housenumber>();
	}

	public Map<String, Housenumber> getHousenumbers() {
		return housenumbers;
	}
	
	public void addHousenumber(String housenumber, float[] latLon){
		if(housenumber != null && latLon != null) {
			Housenumber newHousenumber = getHousenumber(housenumber);
			if(newHousenumber == null) newHousenumber = new Housenumber(housenumber, latLon);
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
	
	public List<Housenumber> housenumberContains(String housenumber){
		List<Housenumber> list = new ArrayList<Housenumber>();
		for(Entry<String, Housenumber> entry : housenumbers.entrySet()){
			if(entry.getKey().contains(housenumber.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public List<Housenumber> housenumberStartsWith(String housenumber){
		List<Housenumber> list = new ArrayList<Housenumber>();
		for(Entry<String, Housenumber> entry : housenumbers.entrySet()){
			if(entry.getKey().startsWith(housenumber.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
}
