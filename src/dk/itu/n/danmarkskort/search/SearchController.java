package dk.itu.n.danmarkskort.search;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.models.RegionFloat;

public class SearchController{
	
	private SearchController(){
		
	}
	
	public static List<String> getSearchFieldSuggestions(String inputStr){
		long limitAmountOfResults = 10;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			String[] strArr = inputStr.split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return Main.addressController.searchSuggestions(
					new RegionFloat(cord[0], cord[1], cord[0], cord[1]), limitAmountOfResults);
		} else {
			return Main.addressController.getSearchSuggestions(inputStr, limitAmountOfResults);
		}
	}
	
	public static Address getSearchFieldAddressObj(String inputStr){
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			String[] strArr = inputStr.split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return Main.addressController.getSearchResult(cord);
		} else {
			return Main.addressController.getSearchResult(inputStr);
		}
	}
	
	private static boolean isCoordinates(String inputStr){
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,7}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		if(inputStr.matches(cordsRegex)) return true;
		return false;
		
	}
}
