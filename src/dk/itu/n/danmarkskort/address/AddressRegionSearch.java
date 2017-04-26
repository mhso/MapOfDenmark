package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.models.PointFloat;
import dk.itu.n.danmarkskort.models.RegionFloat;

public class AddressRegionSearch {
	public Address getSearchResult(float[] lonLat){
		Housenumber hn = Main.addressController.getAddressHolder().searchHousenumber(lonLat);
		return new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
	}
	
	public Address getNearstSearchResult(PointFloat input){
		Address addr =  getSearchResult(new float[] {input.x, input.y});
		if (addr == null) {
			RegionFloat region = new RegionFloat(input.x, input.y, input.x, input.y);
			Collection<Housenumber> hns = Main.addressController.getAddressHolder().searchRegionHousenumbers(region).values();
			Housenumber hn = regionRecursiveLookup(region);
			addr = new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
		}
		return addr;
	}
	
	private Housenumber regionRecursiveLookup(RegionFloat input){
		Map<RegionFloat, Housenumber> hns = Main.addressController.getAddressHolder().searchRegionHousenumbers(input);
		Housenumber house = null;
		if(hns.size() == 1){ 
			for(Housenumber hn : hns.values()) house = hn;
		} else {
			RegionFloat r = new RegionFloat(input.x1 - 0.000001f, input.y1 + 0.000001f, input.x2 + 0.000001f, input.y2 - 0.000001f);
			house = regionRecursiveLookup(r);
		}
		return house;
	}
	
	public List<String> searchSuggestions(PointFloat input, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		List<String> result = new ArrayList<String>();
		RegionFloat region = new RegionFloat(input.x, input.y, input.x, input.y);
		Collection<Housenumber> hns = Main.addressController.getAddressHolder().searchRegionHousenumbers(region).values();
		for(Housenumber hn : hns){
			result.add(hn.toString());
		}
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Lon/lat Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
}
