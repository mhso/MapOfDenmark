package dk.itu.n.danmarkskort.search;

import java.util.List;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.Address;

public class SearchController{
	private SearchController(){ }
	
	public static List<String> getSearchFieldSuggestions(String inputStr){
		long limitAmountOfResults = 10;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			return Main.addressController.getSearchSuggestions(Util.stringCordsToPointFloat(inputStr));
		} else {
			return Main.addressController.getSearchSuggestions(inputStr, limitAmountOfResults);
		}
	}
	
	public static Address getSearchFieldAddressObj(String inputStr){
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			return Main.addressController.getSearchResult(Util.stringCordsToPointFloat(inputStr));
		} else {
			return Main.addressController.getSearchResult(inputStr);
		}
	}
	
	private static boolean isCoordinates(String inputStr){
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,9}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		if(inputStr.matches(cordsRegex)) return true;
		return false;
	}
}