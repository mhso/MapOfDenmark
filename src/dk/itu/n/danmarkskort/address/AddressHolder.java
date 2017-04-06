package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.newmodels.Region;

public class AddressHolder {
	public static HashMap<String, Postcode> postcodes = new HashMap<String, Postcode>();
	
	public AddressHolder(){
	
	}
	
	public static Postcode getPostcode(String postcode) {
		if(postcode == null) return null;
		return postcodes.get(postcode.toLowerCase());
	}
	
	public static int count(){
		int size = 0;
		for(Postcode pc : postcodes.values()){
			size += pc.count();
		}
		return size;
	}
	
	public static int count(Map<String, Postcode> list){
		int size = 0;
		for(Postcode pc : list.values()){
			size += pc.count();
		}
		return size;
	}
	
	private static Map<String, Postcode> postcodeContains(Map<String, Postcode> inputList, String postcode){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getKey().contains(postcode.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> postcodeStartsWith(Map<String, Postcode> inputList, String postcode){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getKey().startsWith(postcode.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> postcodeLevenshteinDistance(Map<String, Postcode> inputList, String postcode){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(postcode == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), postcode.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), postcode.toLowerCase()) < maxValue) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> cityContains(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().contains(city.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> cityStartsWith(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().startsWith(city.toLowerCase())) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> cityLevenshteinDistance(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		int minValue = 0, maxValue = 3;
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), city.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), city.toLowerCase()) < maxValue) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> cityEquals(Map<String, Postcode> inputList, String city){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		if(inputList == null) return list;
		for(Entry<String, Postcode> entry : inputList.entrySet()){
			if(entry.getValue().getCity() != null && entry.getValue().getCity().equalsIgnoreCase(city)) {
				list.put(entry.getKey(), entry.getValue());
			}
		}
		return list;
	}
	
	private static Map<String, Postcode> searchPostcode(Map<String, Postcode> inputList, Address addr, SearchEnum streetType, SearchEnum cityType){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
			switch(streetType){
			case CONTAINS:
				list = postcodeContains(inputList, addr.getPostcode());
				break;
			case EQUALS:
				if(getPostcode(addr.getPostcode()) != null){
					Postcode pc = getPostcode(addr.getPostcode());
					list.put(pc.getPostcode(), pc);
				}
				break;
			case ANY:
				list = inputList;
				break;
			case STARTSWITH:
				list = postcodeStartsWith(inputList, addr.getPostcode());
				break;
			case LEVENSHTEIN:
				list = postcodeLevenshteinDistance(inputList, addr.getPostcode());
				break;
			default:
				break;
			}
			
			list = searchCity(list, addr, cityType);
			
		return list;
	}
	
	private static Map<String, Postcode> searchCity(Map<String, Postcode> inputList, Address addr, SearchEnum cityType){
		Map<String, Postcode> list = new HashMap<String, Postcode>();
			switch(cityType){
			case CONTAINS:
				list = cityContains(inputList, addr.getPostcode());
				break;
			case EQUALS:
				list = cityEquals(inputList, addr.getPostcode());
				break;
			case ANY:
				list = inputList;
				break;
			case STARTSWITH:
				list = cityStartsWith(inputList, addr.getPostcode());
				break;
			case LEVENSHTEIN:
				list = cityLevenshteinDistance(inputList, addr.getPostcode());
				break;
			default:
				break;
		}
		return list;
	}
	
	public static Map<String, Postcode> search(Address addr,
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
			Map<String, Postcode> list = new HashMap<String, Postcode>();
			for(Postcode pc : searchPostcode(postcodes, addr, postcodeType, cityType).values()){
				for(Street st : pc.search(pc.getStreets(), addr, streetType).values()){
					for(Housenumber hn : st.search(st.getHousenumbers(), addr, housenumberType).values()) {
						//System.out.println(hn.toString());
						searchToMap(list, hn);
					}
				}
			}
		return list;
	}
	
	private static void searchToMap(Map<String, Postcode> inputList, Housenumber hn){
		Postcode p = inputList.get(hn.getPostcode().getPostcode());
		if(p == null) p = new Postcode(hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
			p.addAddress(hn.getStreet().getStreet(), hn.getHousenumber(), hn.getLonLat());
			inputList.putIfAbsent(p.getPostcode(), p);
	}
	
	public static Map<RegionFloat, Postcode> getRegions(){
		Map<RegionFloat, Postcode> regions = new HashMap<RegionFloat, Postcode>();
		for(Postcode pc : postcodes.values()) regions.put(pc.getRegion(), pc);
		return regions;
	}
	
	public static Map<RegionFloat, Postcode> searchRegionWithin(RegionFloat input){
		Map<RegionFloat, Postcode> regions = new HashMap<RegionFloat, Postcode>();
		for(Postcode pc : postcodes.values()) {
			RegionFloat pcR = pc.getRegion();
			if(pcR.isWithin(input)){
				regions.put(pc.getRegion(), pc);
				System.out.println("MATCH: ADD: " + pc.getPostcode());
			}
		}
		return regions;
	}
	
	public static Map<RegionFloat, Housenumber> searchRegionHousenumbers(RegionFloat input){	
		Map<RegionFloat, Housenumber> regions = new HashMap<RegionFloat, Housenumber>();
		for(Postcode pc : searchRegionWithin(input).values()) {
			float expanVal = 0.0f;
			for(int i=0; i<100; i++) {
				RegionFloat r = new RegionFloat(input.x1 - expanVal, input.y1 - expanVal, input.x2 + expanVal, input.y2 + expanVal);
				for(Street st : pc.searchRegionWithin(r).values()){
					regions.putAll(st.searchRegionWithin(r));
				}
				expanVal = expanVal + 0.0000100f;
//				System.out.println("searchRegionHousenumbers: " + r.toString() 
//				+ " Size: " + pc.searchRegionWithin(r).values().size()
//				+ " regions Size: " + regions.size());
			}
		}
		return regions;
	}
	
	public static Housenumber searchHousenumber(float[] lonLat){
		RegionFloat input = new RegionFloat(lonLat[0], lonLat[1], lonLat[0], lonLat[1]);
		for(Postcode pc : searchRegionWithin(input).values()) {
			for(Street st : pc.searchRegionWithin(input).values()){
				for(Housenumber hn : st.searchRegionWithin(input).values()){
					float[] hnLonLat = hn.getLonLat();
					if(hnLonLat[0] == lonLat[0] && hnLonLat[0] == lonLat[0]) {
						return hn;
					}
				}
			}
		}
		return null;
	}
}
