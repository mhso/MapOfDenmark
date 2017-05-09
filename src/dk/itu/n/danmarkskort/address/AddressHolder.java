package dk.itu.n.danmarkskort.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.models.RegionFloat;

public class AddressHolder implements Serializable {
	private static final long serialVersionUID = 4946666884688610616L;
	public HashMap<String, Postcode> postcodes = new HashMap<String, Postcode>(2000);
	
	public AddressHolder(){ }
	
	public Postcode getPostcode(String postcode) {
		if(postcode == null) return null;
		return postcodes.get(postcode.toLowerCase());
	}
	
	public List<Housenumber> getHousenumbers(){
		List<Housenumber> result = new ArrayList<Housenumber>();
		for(Postcode postcode : postcodes.values()){
			for(Street street : postcode.getStreets().values()){
				result.addAll(street.getHousenumbers().values());
			}
		}
		return result;
	}
	
	public int count(){
		int size = 0;
		for(Postcode pc : postcodes.values()) size += pc.count();
		return size;
	}
	
	public int count(Map<String, Postcode> list){
		int size = 0;
		for(Postcode pc : list.values()) size += pc.count();
		return size;
	}
	
	private Map<String, Postcode> postcodeContains(Map<String, Postcode> input, String postcode){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(postcode == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey().contains(postcode.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> postcodeStartsWith(Map<String, Postcode> input, String postcode){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(postcode == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey().startsWith(postcode.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> postcodeLevenshteinDistance(Map<String, Postcode> input, String postcode){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(postcode == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey() != null && StringUtils.getLevenshteinDistance(entry.getKey(), postcode.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), postcode.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> cityContains(Map<String, Postcode> input, String city){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().contains(city.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> cityStartsWith(Map<String, Postcode> input, String city){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().startsWith(city.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> cityLevenshteinDistance(Map<String, Postcode> input, String city){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getKey() != null && city != null && StringUtils.getLevenshteinDistance(entry.getKey(), city.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), city.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Postcode> cityEquals(Map<String, Postcode> input, String city){
		Map<String, Postcode> result = new HashMap<String, Postcode>();
		if(input == null) return result;
		for(Entry<String, Postcode> entry : input.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().equalsIgnoreCase(city)) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
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
			result = searchCity(result, addr, cityType);
		return result;
	}
	
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
	
	public Map<String, Postcode> search(Address addr,
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
			Map<String, Postcode> result = new HashMap<String, Postcode>();
			for(Postcode pc : searchPostcode(postcodes, addr, postcodeType, cityType).values()){
				for(Street st : pc.search(pc.getStreets(), addr, streetType).values()){
					for(Housenumber hn : st.search(st.getHousenumbers(), addr, housenumberType).values()) {
						//System.out.println(hn.toString());
						searchToMap(result, hn);
					}
				}
			}
		return result;
	}
	
	private void searchToMap(Map<String, Postcode> input, Housenumber hn){
		Postcode p = input.get(hn.getPostcode().getPostcode());
		if(p == null) p = new Postcode(hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
			p.addAddress(hn.getStreet().getStreet(), hn.getHousenumber(), hn.getLonLat());
			input.putIfAbsent(p.getPostcode(), p);
	}
	
	public Map<RegionFloat, Postcode> getRegions(){
		Map<RegionFloat, Postcode> regions = new HashMap<RegionFloat, Postcode>();
		for(Postcode pc : postcodes.values()) regions.put(pc.getRegion(), pc);
		return regions;
	}
}