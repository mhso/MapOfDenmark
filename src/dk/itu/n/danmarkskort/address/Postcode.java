package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Postcode {
	private String postcode;
	private String city;
	private Map<String, Street> streets;
	
	Postcode(String postcode, String city){
		this.postcode = postcode;
		this.city = city;
		streets = new HashMap<String, Street>();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public void addStreet(String street){
		Street newStreet = getStreet(street);
		if(newStreet == null) newStreet = new Street(street);
		streets.put(street.toLowerCase(), newStreet);
	}
	
	public void addAddress(String street, String housenumber, float[] latLon){
		if(street != null && housenumber != null && latLon != null){
			Street newStreet = getStreet(street);
			if(newStreet == null) newStreet = new Street(street);
			newStreet.addHousenumber(housenumber, latLon);
			streets.put(street.toLowerCase(), newStreet);
		}
	}

	public Map<String, Street> getStreets() {
		return streets;
	}
	
	public Street getStreet(String street) {
		if(street == null) return null;
		return streets.get(street.toLowerCase());
	}

	public String getPostcode() {
		return postcode;
	}
	
	public List<Street> streetContains(String street){
		List<Street> list = new ArrayList<Street>();
		for(Entry<String, Street> entry : streets.entrySet()){
			if(entry.getKey().contains(street.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public List<Street> streetStartsWith(String street){
		List<Street> list = new ArrayList<Street>();
		for(Entry<String, Street> entry : streets.entrySet()){
			if(entry.getKey().startsWith(street.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
}
