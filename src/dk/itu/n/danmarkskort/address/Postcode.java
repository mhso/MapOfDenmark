package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.newmodels.Region;

public class Postcode {
	private String postcode, city;
	private Map<String, Street> streets;
	private Region region;
	
	Postcode(String postcode, String city){
		this.postcode = postcode;
		this.city = city;
		streets = new HashMap<String, Street>();
	}

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }
	
	public Region getRegion(){
		if(region == null) region = genRegion();
		return region;
	}

	private Region genRegion(){
		Region region =  new Region(0, 0, 0, 0);
		for(Street st : streets.values()){
				Region stR = st.getRegion();
				if(region.x1 < stR.x1) region.x1 = stR.x1;
				if(region.y1 < stR.y1) region.y1 = stR.y1;
				if(region.x2 > stR.x2) region.x2 = stR.x2;
				if(region.y2 > stR.y2) region.y2 = stR.y2;
		}
		System.out.println(region.toString());
		return region;
	}
	
	public Map<Region, Street> searchRegion(Region input){
		Map<Region, Street> regions = new HashMap<Region, Street>();
		for(Street st : streets.values()) {
			Region stR = st.getRegion();
			if(input.x1 >= stR.x1 && input.y1 >= stR.y2 && input.x1 >= stR.x1 && input.y1 >= stR.y2){
				regions.put(st.getRegion(), st);
			}
		}
		return regions;
	}
	
	public int count(){
		int size = 0;
		for(Street st : streets.values()) size += st.count();
		return size;
	}
	
	public void addStreet(String street){
		Street newStreet = getStreet(street);
		if(newStreet == null) newStreet = new Street(this, street);
		streets.put(street.toLowerCase(), newStreet);
	}
	
	public void addAddress(String street, String housenumber, float[] latLon){
		if(street != null && housenumber != null && latLon != null){
			Street st = getStreet(street);
			if(st == null) st = new Street(this, street);
			st.addHousenumber(housenumber, latLon);
			streets.put(street.toLowerCase(), st);
		}
	}

	public Map<String, Street> getStreets() { return streets; }
	
	public Street getStreet(String street) {
		if(street == null) return null;
		return streets.get(street.toLowerCase());
	}

	public String getPostcode() { return postcode; }
	
	private Map<String, Street> streetContains(Map<String, Street> inputList, String street){
		Map<String, Street> list = new HashMap<String, Street>();
		if(street == null) return list;
		for(Entry<String, Street> entry : inputList.entrySet()){
			if(entry.getKey().contains(street.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private Map<String, Street> streetStartsWith(Map<String, Street> inputList, String street){
		Map<String, Street> list = new HashMap<String, Street>();
		if(street == null) return list;
		for(Entry<String, Street> entry : inputList.entrySet()){
			if(entry.getKey().startsWith(street.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private Map<String, Street> streetLevenshteinDistance(Map<String, Street> inputList, String street){
		Map<String, Street> list = new HashMap<String, Street>();
		int minValue = 0, maxValue = 3;
		if(street == null) return list;
		for(Entry<String, Street> entry : inputList.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), street.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), street.toLowerCase()) < maxValue) {
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
			case ANY:
				list = inputList;
				break;
			case STARTSWITH:
				list = streetStartsWith(inputList, addr.getStreet());
				break;
			case LEVENSHTEIN:
				list = streetLevenshteinDistance(inputList, addr.getStreet());
				break;
			default:
				break;
		}
		return list;
	}
}