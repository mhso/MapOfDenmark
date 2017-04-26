package dk.itu.n.danmarkskort.search;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.models.PointFloat;
import dk.itu.n.danmarkskort.models.RegionFloat;

public class SearchController{
	
	private SearchController(){
		
	}
	
	public static List<String> getSearchFieldSuggestions(String inputStr){
		long limitAmountOfResults = 10;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			return Main.addressController.getSearchSuggestions(stringCordsToPointFloat(inputStr), limitAmountOfResults);
		} else {
			return Main.addressController.getSearchSuggestions(inputStr, limitAmountOfResults);
		}
	}
	
	public static Address getSearchFieldAddressObj(String inputStr){
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			System.out.println("Search for cords string");
			return Main.addressController.getNearstSearchResult(stringCordsToPointFloat(inputStr));
		} else {
			System.out.println("Search for address string");
			return Main.addressController.getSearchResult(inputStr);
		}
	}
	
	private static boolean isCoordinates(String inputStr){
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,7}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		if(inputStr.matches(cordsRegex)) return true;
		return false;
	}
	
	private static PointFloat stringCordsToPointFloat(String inputStr){
		String[] strArr = inputStr.split(", ");
		float lon = Float.parseFloat(strArr[0]);
		float lat = Float.parseFloat(strArr[1]);
		return new PointFloat(lon, lat);
	}
}
