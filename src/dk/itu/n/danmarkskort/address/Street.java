package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.newmodels.Region;

public class Street {
	private String street;
	private Map<String, Housenumber> housenumbers;
	private Postcode postcode;
	private Region region;
	
	public Postcode getPostcode() { return postcode; }
	public void setPostcode(Postcode postcode) { this.postcode = postcode; }
	
	Street(Postcode postcode, String street){
		this.setPostcode(postcode);
		this.street = street;
		housenumbers = new HashMap<String, Housenumber>();
		region = null;
	}
	public Region getRegion(){
		if(region == null) region = genRegion();
		return region;
	}
	
	private Region genRegion(){
		Region region =  new Region(0, 0, 0, 0);
		for(Housenumber hn : housenumbers.values()){
				float[] lonLat = hn.getLonLat();
				float lon = lonLat[0];
				float lat = lonLat[1];
				if(region.x1 < lon) region.x1 = lon;
				if(region.y1 < lat) region.y1 = lat;
				if(region.x2 > lon) region.x2 = lon;
				if(region.y2 > lat) region.y2 = lat;
		}
		System.out.println(region.toString());
		return region;
	}
	
	public Map<Region, Housenumber> searchRegion(Region input){
		Map<Region, Housenumber> regions = new HashMap<Region, Housenumber>();
		for(Housenumber hn : housenumbers.values()) {
			float[] lonLat = hn.getLonLat();
			float lon = lonLat[0];
			float lat = lonLat[1];
			Region region =  new Region(lon, lat, lon, lat);
			if(input.x1 >= lon && input.y1 >= lat && input.x1 >= lon && input.y1 >= lat){
				regions.put(region, hn);
			}
		}
		return regions;
	}
	
	public int count(){ return housenumbers.size(); }

	public Map<String, Housenumber> getHousenumbers() { return housenumbers; }
	
	public void addHousenumber(String housenumber, float[] latLon){
		if(housenumber != null && latLon != null) {
			Housenumber hn = getHousenumber(housenumber);
			if(hn == null) hn = new Housenumber(postcode, this, housenumber, latLon);
			housenumbers.put(housenumber.toLowerCase(), hn);
		}
	}
	
	public Housenumber getHousenumber(String housenumber) {
		if(housenumber == null) return null;
		return housenumbers.get(housenumber.toLowerCase());
	}

	public String getStreet() { return street; }

	
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
			case ANY:
				list = inputList;
				break;
			case STARTSWITH:
				list = housenumberStartsWith(inputList, addr.getHousenumber());
				break;
			case LEVENSHTEIN:
				list = housenumberLevenshteinDistance(inputList, addr.getHousenumber());
				break;
			default:
				break;
			
		}
		return list;
	}

}
