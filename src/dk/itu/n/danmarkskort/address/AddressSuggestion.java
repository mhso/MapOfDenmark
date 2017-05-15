package dk.itu.n.danmarkskort.address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dk.itu.n.danmarkskort.Main;

public class AddressSuggestion {
	private String lastSearchInput = null;
	private Address lastSearchAddress = null;
	private AddressController addressController;
	private boolean debug = false;
	
	AddressSuggestion(AddressController addressController){
		this.addressController = addressController;
	}
	
	/**
	 * Outside accesses to the analyze method.
	 * 
	 * @param find a search string.
	 * @param limitAmountOfResults, maximum returned result.
	 * @return a string list of addresses in short format.
	 */
	public List<String> searchSuggestions(String find, long limitAmountOfResults){
		return analyzeUserinput(find, limitAmountOfResults);
	}
	
	/**
	 * I found that it an ongoing search, we keep keeps status, else we create a new address object.
	 * 
	 * @param find search string.
	 * @return an address object.
	 */
	private Address continueSearchSuggestions(String find){
		if(lastSearchInput != null && find != null && lastSearchInput.length() <= find.length()){	
			String part = find.substring(0, lastSearchInput.length());
			if(part.equals(lastSearchInput)){
				if(debug) System.out.println("Lets continue the search., lock this part: " + part);
				return lastSearchAddress;
			} 
		}
			return new Address();
	}
	
	/**
	 * Method analyzes input, and finds patterns that can be "locked" down as valid search parts.
	 * 
	 * @param find a search string.
	 * @param limitAmountOfResults, maximum returned result.
	 * @return list of search results.
	 */
	private List<String> analyzeUserinput(String find, long limitAmountOfResults){
		Address addr = continueSearchSuggestions(find); // check continue or new search.
		List<String> result = new ArrayList<String>();
		String streetAndNumber = find;
		String postcode = addr.getPostcode();
		postcode = AddressValidator.findPostcode(find); //Extracts postcode if a valid is found.
		
		if(postcode != null){
			if(debug) System.out.println("findPostcode: " + postcode);
			String[] strArr = find.split(postcode);
			if(strArr.length>0) streetAndNumber = strArr[0].trim();
			
			if(addressController.getAddressHolder().getPostcode(postcode) != null){
				addr.setPostcode(postcode);
				addr.setCity(addressController.getAddressHolder().getPostcode(postcode).getCity());
			}
		}
		
		// Confirm/search street
		if(addr.getStreet() == null){
			for(int i=streetAndNumber.length(); i>0; i--){
				String part = streetAndNumber.substring(0, i).trim();
				addr.setStreet(part);
				if(addressController.getAddressHolder()
						.search(addr, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY).size() > 0) {
					if(debug) System.out.println("Analysed S, found: " + part);
					streetAndNumber = streetAndNumber.substring(part.length());
					break;
				} else {
					addr.setStreet(null);
				}
			}
		}
		
		// Confirm/search housenumber
		if(addr.getStreet() != null){
			streetAndNumber = streetAndNumber.replaceAll(addr.getStreet(), "");
			for(int i=streetAndNumber.length(); i>0; i--){
				String part = streetAndNumber.substring(0, i).trim();
				addr.setHousenumber(part);
				if(addressController.getAddressHolder()
						.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY).size() > 0){
					if(debug) System.out.println("Analysed N, found: " + part);
					break;
				} else {
					addr.setHousenumber(null);
				}
			}
		}
		
		// Confirm/search city
		if(addr.getPostcode() == null){
			String str = find;
			if(addr.getStreet() != null) str = str.replaceAll(addr.getStreet(), "");
			if(addr.getHousenumber() != null) str = str.replaceAll(addr.getHousenumber(), "");
			
			if(!str.isEmpty()){
				for(int i=str.length(); i>0; i--){
					String part = str.substring(0, i).trim();
					addr.setCity(part);
					if(addressController.getAddressHolder()
							.search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS).size() > 0){
						if(debug) System.out.println("Analysed C, found: " + part);
						break;
					} else {
						addr.setCity(null);
					}
				}
			}
		}
		if(debug) System.out.println("Analyzed addr: " + addr.toString());
		
		lastSearchInput = find;
		lastSearchAddress = addr;
		
		// Perform search with locked input
		searchSuggestionsPart1(result, addr, 10l);
		searchSuggestionsPart2(result, addr, 10l, find);
		
		// Limit and convert to list.
		result = result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
		return result;
	}
	
	/**
	 * Suggestion part 1, try to find best results, if nothing is found less precise search methods is called.
	 * 
	 * @param result, working result list.
	 * @param addr, parsedAddress.
	 * @param limitAmountOfResults, result limitation.
	 */
	private void searchSuggestionsPart1(List<String> result, Address addr, long limitAmountOfResults) {
		
		result.addAll(resizeSearcResult(addressController.getAddressHolder()
				.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS), 5, 3, limitAmountOfResults));
			
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, addr, 
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY);
			
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, addr, 
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY);
			
		ifListLessThanLimit(result, limitAmountOfResults, addr, 
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY);
			
		ifListLessThanLimit(result, limitAmountOfResults, addr, 
					SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY);
			
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, addr,
					SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY);
		
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, addr,
					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS, SearchEnum.ANY);
			
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, addr, 
					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS);
	}

	/**
	 * Suggestion part 2, try to find best results, if nothing is found less precise search methods is called.
	 * @param result
	 * @param addr
	 * @param limitAmountOfResults
	 * @param find
	 */
	private void searchSuggestionsPart2(List<String> result, Address addr, long limitAmountOfResults, String find){
		Address tempAddr = new Address();
		tempAddr.setStreet(find);
		ifListNotOneAndListLessThanLimit(result, limitAmountOfResults, tempAddr, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY);
		ifListEmptySearchAndAddAll(result, addr, SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY);
		ifListEmptySearchAndAddAll(result, tempAddr, SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY);
		ifListEmptySearchAndAddAll(result, addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN);
		ifListEmptySearchAndAddAll(result, tempAddr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN);
	}
	
	/**
	 * If the incoming result list is not exactly 1 and the list is not greater than limitAmountOfResults, then
	 * the method searches the data-structure for addresses based on search criteria, and
	 * add them to the working result list.
	 * 
	 * @param result, working result list
	 * @param limitAmountOfResults, , maximum returned result.
	 * @param addr, search object
	 * @param streetType
	 * @param housenumberType
	 * @param postcodeType
	 * @param cityType
	 */
	private void ifListNotOneAndListLessThanLimit(List<String> result, long limitAmountOfResults, Address addr,  
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		boolean isRelevant = isRelevantSearch(addr, streetType, housenumberType, postcodeType, cityType);
		if(isRelevant && result.size() != 1 && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(addressController.getAddressHolder()
					.search(addr, streetType, housenumberType, postcodeType, cityType), 5, 3, 10l));
		}
	}
	
	/**
	 * If the incoming result list is not greater than limitAmountOfResults, then
	 * the method searches the data-structure for addresses based on search criteria, and
	 * add them to the working result list.
	 * 
	 * @param result, working result list
	 * @param limitAmountOfResults, , maximum returned result.
	 * @param addr, search object
	 * @param streetType
	 * @param housenumberType
	 * @param postcodeType
	 * @param cityType
	 */
	private void ifListLessThanLimit(List<String> result, long limitAmountOfResults, Address addr,  
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		boolean isRelevant = isRelevantSearch(addr, streetType, housenumberType, postcodeType, cityType);
		if(isRelevant && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(addressController.getAddressHolder()
					.search(addr, streetType, housenumberType, postcodeType, cityType), 5, 3, 10l));
		}
	}
	
	/**
	 * If the incoming result list is empty, then
	 * the method searches the data-structure for addresses based on search criteria, and
	 * add them to the working result list.
	 * 
	 * @param result, working result list
	 * @param addr, search object
	 * @param streetType
	 * @param housenumberType
	 * @param postcodeType
	 * @param cityType
	 */
	private void ifListEmptySearchAndAddAll(List<String> result, Address addr, 
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		boolean isRelevant = isRelevantSearch(addr, streetType, housenumberType, postcodeType, cityType);
		if(isRelevant && result.isEmpty()) {
			result.addAll(resizeSearcResult(addressController.getAddressHolder()
					.search(addr, streetType, housenumberType, postcodeType, cityType), 5, 3, 10l));
		}
	}

	/**
	 * Method runs through the input, and returns a smaller list of strings.
	 * This is to be able to present the user for different result instead of return only one street.
	 * The method first returns the streetname and then a limited list of houses on that street.
	 * 
	 * @param input result input
	 * @param cutOfToStreet, amount of result allowed before we cut of, and only show streetname
	 * @param limitHousenumbers, maximum housenumbers to show from the same street.
	 * @param limitAmountOfResults, maximum returned result.
	 * @return list of address suggestions.
	 */
	private List<String> resizeSearcResult(Map<String, Postcode> input, int cutOfToStreet, int limitHousenumbers, long limitAmountOfResults){
		List<String> result = new ArrayList<String>();
		
		for(Postcode pc : input.values()){
			for(Street st : pc.getStreets().values()){
				result.add(st.getStreet());
				if(pc.getStreets().size() <= cutOfToStreet){
					int i = 0;
					for(Housenumber hn : st.getHousenumbers().values()){
						result.add(hn.toString());
						if(++i > limitHousenumbers) break;
					}
				}
			}
		}
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER); // Sort list
		result = result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
		return result;
	}
	
	private boolean isRelevantSearch(Address addr, 
			SearchEnum streetType, SearchEnum housenumberType, SearchEnum postcodeType, SearchEnum cityType){
		if(streetType != SearchEnum.ANY && addr.getStreet() == null) return false;
		if(housenumberType != SearchEnum.ANY && addr.getHousenumber() == null) return false;
		if(postcodeType != SearchEnum.ANY && addr.getPostcode() == null) return false;
		if(cityType != SearchEnum.ANY && addr.getCity() == null) return false;
		return true;
	}
}