package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.newmodels.Region;

public class Street {
	private StringObj streetObj;
	private Map<String, Housenumber> housenumbers;
	private Postcode postcode;
	private RegionFloat region;
	
	public Postcode getPostcode() { return postcode; }
	public void setPostcode(Postcode postcode) { this.postcode = postcode; }
	
	Street(Postcode postcode, String street){
		this.setPostcode(postcode);
		this.streetObj = StringHolder.getInstance().make(street);
		housenumbers = new HashMap<String, Housenumber>();
		region = null;
	}
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
			System.out.println("Compare isWithin: hnR = input: ");
			System.out.println("Larger: " + hnR.toString());
			System.out.println("Smaller: " + input.toString());
			if(hnR.isWithin(input)){
				regions.put(hnR, hn);
				System.out.println("Street -> searchRegionWithin: " + input.toString());
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

	public String getStreet() { return streetObj.toString(); }

	
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
