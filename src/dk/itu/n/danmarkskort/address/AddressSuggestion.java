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
	private boolean debug = false;
	
	AddressSuggestion(){
		
	}
	
	public List<String> searchSuggestions(String find, long limitAmountOfResults){
		return analyzeUserinput(find, limitAmountOfResults);
	}
	
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
	
	
	private List<String> analyzeUserinput(String find, long limitAmountOfResults){
		Address addr = continueSearchSuggestions(find);
		List<String> result = new ArrayList<String>();
		String streetNum = find;
		String postcode = addr.getPostcode();
		postcode = AddressValidator.findPostcode(find);
		
		if(postcode != null){
			if(debug) System.out.println("findPostcode: " + postcode);
			String[] strArr = find.split(postcode);
			if(strArr.length>0) streetNum = strArr[0].trim();
			
			if(Main.addressController.getAddressHolder().getPostcode(postcode) != null){
				addr.setPostcode(postcode);
				addr.setCity(Main.addressController.getAddressHolder().getPostcode(postcode).getCity());
			}
		}
		
		// Confirm/search street
		if(addr.getStreet() == null){
			for(int i=streetNum.length(); i>0; i--){
				String part = streetNum.substring(0, i).trim();
				addr.setStreet(part);
				if(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY).size() > 0) {
					if(debug) System.out.println("Analysed S, found: " + part);
					streetNum = streetNum.substring(part.length());
					break;
				} else {
					addr.setStreet(null);
				}
			}
		}
		
		// Confirm/search housenumber
		if(addr.getStreet() != null){
			streetNum = streetNum.replaceAll(addr.getStreet(), "");
			for(int i=streetNum.length(); i>0; i--){
				String part = streetNum.substring(0, i).trim();
				addr.setHousenumber(part);
				if(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY).size() > 0){
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
					if(Main.addressController.getAddressHolder().search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS).size() > 0){
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
		
		searchSuggestionsPart1(result, addr, 10l);
		searchSuggestionsPart2(result, addr, 10l, find);
		
		result = result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
		return result;
	}
	
	private void searchSuggestionsPart1(List<String> result, Address addr, long limitAmountOfResults) {
		result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS), 5, 3, 10l));
		
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 1, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
		if(debug) System.out.println("Search 2, list size: " + result.size());
		}
		if(result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 3, list size: " + result.size());
		}
		if(result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 4, list size: " + result.size());
		}
		if(result.size() < 1 && result.size() < limitAmountOfResults) { 
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr,SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 5, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr,SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 6, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(
					Main.addressController.getAddressHolder().search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS), 5, 3, 10l));
			if(debug) System.out.println("Search 7, list size: " + result.size());
		}
	}

	private void searchSuggestionsPart2(List<String> result, Address addr, long limitAmountOfResults, String find){
		Address tempAddr = new Address();
		tempAddr.setStreet(find);
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(tempAddr, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 10, list size: " + result.size());
		}
		
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 8, list size: " + result.size());
		}
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(tempAddr, SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			if(debug) System.out.println("Search 8, list size: " + result.size());
		}
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN), 5, 3, 10l));
			if(debug) System.out.println("Search 9, list size: " + result.size());
		}
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(Main.addressController.getAddressHolder().search(tempAddr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN), 5, 3, 10l));
			if(debug) System.out.println("Search 9, list size: " + result.size());
		}
	}

	private List<String> resizeSearcResult(Map<String, Postcode> list, int cutOfToStreet, int limitHousenumbers, long limitAmountOfResults){
		List<String> result = new ArrayList<String>();
		
		for(Postcode pc : list.values()){
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
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		result = result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
		return result;
	}
}