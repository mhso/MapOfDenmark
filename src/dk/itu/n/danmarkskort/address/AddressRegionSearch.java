package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.PointFloat;
import dk.itu.n.danmarkskort.models.RegionFloat;

public class AddressRegionSearch {
//	public Address getSearchResult(float[] lonLat){
//		Housenumber hn = Main.addressController.getAddressHolder().searchHousenumber(lonLat);
//		return new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
//	}
	
	public Address getNearstSearchResult(PointFloat input){
//		Address addr =  getSearchResult(new float[] {input.x, input.y});
//		if (addr == null) {
			RegionFloat region = new RegionFloat(input.x, input.y, input.x, input.y);
			Collection<Housenumber> hns = Main.addressController.getAddressHolder().searchRegionHousenumbers(region).values();
			double distMin = Double.POSITIVE_INFINITY;
			Housenumber hnNearst = null;
			System.out.println("getNearstSearchResult, checking distance for: " + hns.size() + " elements.");
			for(Housenumber hn : hns){
				double distTemp = input.getDistance(hn.getPointFloat());
				if(distTemp < distMin) {
					distMin = distTemp; 
					hnNearst = hn;
					System.out.println("getNearstSearchResult: " + hn.toString());
				}
			}
			Address addr = new Address(hnNearst.getLonLat(), hnNearst.getStreet().getStreet(), 
					hnNearst.getHousenumber(), hnNearst.getPostcode().getPostcode(), hnNearst.getPostcode().getCity());
//		}
		return addr;
	}
	
	public List<String> searchSuggestions(PointFloat input, long limitAmountOfResults){
		List<String> result = new ArrayList<String>();
		RegionFloat region = new RegionFloat(input.x, input.y, input.x, input.y);
		Collection<Housenumber> hns = Main.addressController.getAddressHolder().searchRegionHousenumbers(region).values();
		for(Housenumber hn : hns){
			result.add(hn.toString());
		}
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
}
