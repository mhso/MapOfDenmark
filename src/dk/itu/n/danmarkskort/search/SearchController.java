package dk.itu.n.danmarkskort.search;

import java.util.List;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.Address;

public class SearchController{
	
	/**
	 * Get search suggestions by input, text can return multiple results,
	 * while coordinates can return nearst address as suggestion.
	 * 
	 * @param inputStr address string or coordinates
	 * @return an list of results
	 */
	public static List<String> getSearchFieldSuggestions(String inputStr){
		long limitAmountOfResults = 10;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			return Main.addressController.getSearchSuggestions(Util.stringCordsToPointFloat(inputStr));
		} else {
			return Main.addressController.getSearchSuggestions(inputStr, limitAmountOfResults);
		}
	}
	
	/**
	 * If search return a single address, return match.
	 * 
	 * @param inputStr address string or coordinates
	 * @return an address object or null
	 */
	public static Address getSearchFieldAddressObj(String inputStr){
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(isCoordinates(inputStr)) {
			return Main.addressController.getSearchResultByCoords(Util.stringCordsToPointFloat(inputStr));
		} else {
			return Main.addressController.getSearchResult(inputStr);
		}
	}
	
	/**
	 * Checks if input is coordinates.
	 * @param inputStr a string
	 * @return true if is coordinates.
	 */
	private static boolean isCoordinates(String inputStr){
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,9}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		if(inputStr.matches(cordsRegex)) return true;
		return false;
	}
}