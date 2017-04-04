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
		Region region =  new Region(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MAX_VALUE);
		for(Housenumber hn : housenumbers.values()){
				float[] lonLat = hn.getLonLat();
				float lon = lonLat[0];
				float lat = lonLat[1];
				Region hnR = new Region(lon, lat, lon, lat);
				if(hnR.x1 > region.x1) region.x1 = hnR.x1;
				if(hnR.y1 > region.y1) region.y1 = hnR.y1;
				if(hnR.x2 < region.x2) region.x2 = hnR.x2;
				if(hnR.y2 < region.y2) region.y2 = hnR.y2;
		}
		return region;
	}
	
	public Map<Region, Housenumber> searchRegions(Region input){
		Map<Region, Housenumber> regions = new HashMap<Region, Housenumber>();
		for(Housenumber hn : housenumbers.values()) {
			float[] lonLat = hn.getLonLat();
			float lon = lonLat[0];
			float lat = lonLat[1];
			Region region =  new Region(lon, lat, lon, lat);
			if(input.x1 >= lon && input.y1 >= lat && input.x2 >= lon && input.y2 >= lat){
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
