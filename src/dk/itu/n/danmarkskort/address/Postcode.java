package dk.itu.n.danmarkskort.address;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.models.RegionFloat;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class Postcode implements Serializable {
	private static final long serialVersionUID = 2217407726502936291L;
	private String postcode, city;
	private Map<String, Street> streets;
	private RegionFloat region;
	private boolean debug = false;
	
	Postcode(String postcode, String city){
		this.postcode = ReuseStringObj.make(postcode);
		setCity(city);
		streets = new HashMap<String, Street>();
	}

	public String getCity() { return city.toString(); }
	public void setCity(String city) { this.city = ReuseStringObj.make(city); }
	
	public RegionFloat getRegion(){
		if(region == null) region = genRegion();
		return region;
	}

	private RegionFloat genRegion(){
		RegionFloat region =  new RegionFloat(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.MAX_VALUE, Float.MAX_VALUE);
		for(Street st : streets.values()){
			RegionFloat stR = st.getRegion();
				if(stR.x1 > region.x1) region.x1 = stR.x1;
				if(stR.y1 > region.y1) region.y1 = stR.y1;
				if(stR.x2 < region.x2) region.x2 = stR.x2;
				if(stR.y2 < region.y2) region.y2 = stR.y2;
		}
		return region;
	}
	
	public Map<RegionFloat, Street> searchRegionWithin(RegionFloat input){
		Map<RegionFloat, Street> regions = new HashMap<RegionFloat, Street>();
		for(Street st : streets.values()) {
			RegionFloat stR = st.getRegion();
			if(stR.isWithin(input)){
				regions.put(st.getRegion(), st);
				if(debug) System.out.println("MATCH: ADD: " + st.getStreet());
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

	public String getPostcode() { return postcode.toString(); }
	
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
	
	private Map<String, Street> streetStartsWith(Map<String, Street> input, String street){
		Map<String, Street> result = new HashMap<String, Street>();
		if(street == null) return result;
		for(Entry<String, Street> entry : input.entrySet()){
			if(entry.getKey().startsWith(street.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Street> streetLevenshteinDistance(Map<String, Street> input, String street){
		Map<String, Street> result = new HashMap<String, Street>();
		int minValue = 0, maxValue = 3;
		if(street == null) return result;
		for(Entry<String, Street> entry : input.entrySet()){
			try {
				String matchKey = entry.getKey().substring(0, street.length());
				if(StringUtils.getLevenshteinDistance(matchKey, street.toLowerCase()) > minValue &&
						StringUtils.getLevenshteinDistance(matchKey, street.toLowerCase()) < maxValue) {
					result.put(entry.getKey(), entry.getValue());
				}
			} catch (StringIndexOutOfBoundsException e) {
				//e.printStackTrace();
			}
		}
		return result;
	}
	
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