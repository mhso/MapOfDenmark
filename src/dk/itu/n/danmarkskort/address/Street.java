package dk.itu.n.danmarkskort.address;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.models.RegionFloat;
import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class Street implements Serializable {
	private static final long serialVersionUID = -2943891636428003556L;
	private String street;
	private Map<String, Housenumber> housenumbers;
	private Postcode postcode;
	private RegionFloat region;
	
	public Postcode getPostcode() { return postcode; }
	public void setPostcode(Postcode postcode) { this.postcode = postcode; }
	
	Street(Postcode postcode, String street){
		this.setPostcode(postcode);
		this.street = ReuseStringObj.make(street);
		housenumbers = new HashMap<String, Housenumber>();
		region = null;
	}
	
	public int count(){ return housenumbers.size(); }
	
	public String getStreet() { return street.toString(); }
	
	public RegionFloat getRegion(){
		if(region == null) region = genRegion();
		return region;
	}
	
	private RegionFloat genRegion(){
		RegionFloat region =  new RegionFloat(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		for(Housenumber hn : housenumbers.values()){
				float[] lonLat = hn.getLonLat();
				float lon = lonLat[0];
				float lat = lonLat[1];
				RegionFloat hnR = new RegionFloat(lon, lat, lon, lat);
				if(hnR.x1 > region.x1) region.x1 = hnR.x1;
				if(hnR.y1 > region.y1) region.y1 = hnR.y1;
				if(hnR.x2 < region.x2) region.x2 = hnR.x2;
				if(hnR.y2 < region.y2) region.y2 = hnR.y2;
		}
		return region;
	}
	
	public Map<RegionFloat, Housenumber> searchRegionWithin(RegionFloat input){
		Map<RegionFloat, Housenumber> regions = new HashMap<RegionFloat, Housenumber>();
		for(Housenumber hn : housenumbers.values()) {
			float[] lonLat = hn.getLonLat();
			float lon = lonLat[0];
			float lat = lonLat[1];
			RegionFloat hnR =  new RegionFloat(lon, lat, lon, lat);
			if(hnR.isWithin(input)){
				regions.put(hnR, hn);
				System.out.println("Street -> searchRegionWithin: " + input.toString());
				System.out.println("Street -> house: " + hn.toString());
			}
		}
		return regions;
	}

	public Map<String, Housenumber> getHousenumbers() { return housenumbers; }
	
	public void addHousenumber(String housenumber, float[] lonLat){
		if(housenumber != null && lonLat != null) {
			Housenumber hn = getHousenumber(housenumber);
			if(hn == null) hn = new Housenumber(postcode, this, housenumber, lonLat);
			housenumbers.put(housenumber.toLowerCase(), hn);
		}
	}
	
	public Housenumber getHousenumber(String housenumber) {
		if(housenumber == null) return null;
		return housenumbers.get(housenumber.toLowerCase());
	}

	
	private Map<String, Housenumber> housenumberContains(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(entry.getKey().contains(housenumber.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Housenumber> housenumberStartsWith(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(entry.getKey().startsWith(housenumber.toLowerCase())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private Map<String, Housenumber> housenumberLevenshteinDistance(Map<String, Housenumber> input, String housenumber){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
		int minValue = 0, maxValue = 3;
		if(housenumber == null) return result;
		for(Entry<String, Housenumber> entry : input.entrySet()){
			if(StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) > minValue &&
					StringUtils.getLevenshteinDistance(entry.getKey(), housenumber.toLowerCase()) < maxValue) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	public Map<String, Housenumber> search(Map<String, Housenumber> input, Address addr, SearchEnum housenumberType){
		Map<String, Housenumber> result = new HashMap<String, Housenumber>();
			switch(housenumberType){
			case CONTAINS:
				result = housenumberContains(input, addr.getHousenumber());
				break;
			case EQUALS:
				if(getHousenumber(addr.getHousenumber()) != null){
					Housenumber hn = getHousenumber(addr.getHousenumber());
					result.put(hn.getHousenumber(), hn);
				}
				break;
			case ANY:
				result = input;
				break;
			case STARTSWITH:
				result = housenumberStartsWith(input, addr.getHousenumber());
				break;
			case LEVENSHTEIN:
				result = housenumberLevenshteinDistance(input, addr.getHousenumber());
				break;
			default:
				break;
		}
		return result;
	}
}