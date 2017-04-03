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
	
	public int count(){
		int size = 0;
		for(Street st : streets.values()){
			size += st.count();
		}
		return size;
	}
	
	public void addStreet(String street){
		Street newStreet = getStreet(street);
		if(newStreet == null) newStreet = new Street(this, street);
		streets.put(street.toLowerCase(), newStreet);
	}
	
	public void addAddress(String street, String housenumber, float[] latLon){
		if(street != null && housenumber != null && latLon != null){
			Street newStreet = getStreet(street);
			if(newStreet == null) newStreet = new Street(this, street);
			newStreet.addHousenumber(housenumber, latLon);
			streets.put(street.toLowerCase(), newStreet);
		}
	}
	
	public void addStreet(Street st){
		if(st.getStreet() != null){
			Street newStreet = getStreet(st.getStreet());
			if(newStreet == null) newStreet = new Street(this, st.getStreet());
			streets.put(st.getStreet().toLowerCase(), newStreet);
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
		if(street == null) return list;
		for(Entry<String, Street> entry : streets.entrySet()){
			if(entry.getKey().contains(street.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public Map<String, Street> streetContains(Map<String, Street> inputList, String street){
		Map<String, Street> list = new HashMap<String, Street>();
		if(street == null) return list;
		for(Entry<String, Street> entry : inputList.entrySet()){
			if(entry.getKey().contains(street.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	public List<Street> streetStartsWith(String street){
		List<Street> list = new ArrayList<Street>();
		if(street == null) return list;
		for(Entry<String, Street> entry : streets.entrySet()){
			if(entry.getKey().startsWith(street.toLowerCase())) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public Map<String, Street> streetStartsWith(Map<String, Street> inputList, String street){
		Map<String, Street> list = new HashMap<String, Street>();
		if(street == null) return list;
		for(Entry<String, Street> entry : inputList.entrySet()){
			if(entry.getKey().startsWith(street.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	public Map<String, Street> search(Map<String, Street> inputList, Address addr, SearchEnum streetType){
		Map<String, Street> list = new HashMap<String, Street>();
			switch(streetType){
			case CONTAINS:
				list = streetContains(inputList, addr.getStreet());
				break;
			case EQUALS:
				if(getStreet(addr.getStreet()) != null){
					Street st = getStreet(addr.getStreet());
					list.put(st.getStreet(), st);
				}
				break;
			case NOT_IN_USE:
				list = inputList;
				break;
			case STARTSWITH:
				list = streetStartsWith(inputList, addr.getStreet());
				break;
			default:
				break;
		}
		return list;
	}
}