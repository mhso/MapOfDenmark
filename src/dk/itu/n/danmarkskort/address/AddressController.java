package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.models.ParsedAddress;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AddressController{
	private int addressesNotAcceptedCount;
	private TimerUtil timerUtilA = new TimerUtil();
	private TimerUtil timerUtilB = new TimerUtil();
	private PostcodeCityBestMatch postcodeCityBestMatch;
	private List<ParsedAddress> parsedAddresses = new ArrayList<ParsedAddress>();
	private String lastSearchInput = null;
	private Address lastSearchAddress = null;
	
	public AddressController(){
		postcodeCityBestMatch = new PostcodeCityBestMatch();
	}
	int count = 0; 
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ return searchSuggestions(find, limitAmountOfResults); }
	
	public Address getSearchResult(String find){
		
		Address addrBuild = AddressParser.parse(find, false);
		Map<String, Postcode> list;
		list = AddressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
		if(list.size() < 1) { list = AddressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY);
		}
		if(list.size() < 1) { list = AddressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
		}
		if(list.size() < 1) { list = AddressHolder.search(addrBuild,
				SearchEnum.LEVENSHTEIN, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
		}
		if(list.size() < 1) { list = AddressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.LEVENSHTEIN);
		}
		
		if(list.size() == 1) {
			for(Postcode pc : list.values()){
				for(Street st : pc.getStreets().values()){
					for(Housenumber hn : st.getHousenumbers().values()){
						Address addr = AddressParser.parse(hn.toString(), false);
						addr.setLonLat(hn.getLonLat());
						return addr;
					}
				}
			}
		}
		return null;
	}
	
	private void continueSearchSuggestions(String find){
		if(lastSearchInput != null && find != null){
			String part;
			try {
				part = find.substring(0, lastSearchInput.length());
				
				if(part.equals(lastSearchInput)){
					System.out.println("Lets continue the search., lock this part: " + part);
					//return lastSearchAddress;
				} else {
					//lastSearchInput = null;
					//lastSearchAddress = null;
					System.out.println("lets start af new search.");
					//return new Address();
				}
				
			} catch (StringIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		
	}
	
	private List<String> analyzeUserinput(String find, long limitAmountOfResults){
		Address addr = new Address();
		
		continueSearchSuggestions(find);
		
		String streetNum = find;
		String street = null;
		String postcode = null;
		String housenumber = null;
		String city = null;
		List<String> result = new ArrayList<String>();
		
		postcode = AddressValidator.findPostcode(find);
		
		if(postcode != null){
			System.out.println("findPostcode: " + postcode);
			String[] strArr = find.split(postcode);
			if(strArr.length>0) streetNum = strArr[0].trim();
			if(strArr.length>1) city = strArr[1].trim();
			addr.setPostcode(postcode);
			addr.setCity(AddressHolder.getPostcode(postcode).getCity());
		}
		
		//Find street
		for(int i=streetNum.length(); i>0; i--){
			String part = streetNum.substring(0, i).trim();
			
			addr.setStreet(part);
			if(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY).size() > 0) {
				street = part;
				System.out.println("Analysed S, found: " + part);
				streetNum = streetNum.substring(part.length());
				break;
			} else {
				addr.setStreet(null);
			}
		}
		//Find number
		if(addr.getStreet() != null){
			for(int i=streetNum.length(); i>0; i--){
				String part = streetNum.substring(0, i).trim();
				addr.setHousenumber(part);
				if(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY).size() > 0){
					housenumber = part;
					System.out.println("Analysed N, found: " + part);
					break;
				} else {
					addr.setHousenumber(null);
				}
			}
		}
		//Find city
		
//		System.out.println("Analyzed street: " + street);
//		System.out.println("Analyzed housenumber: " + housenumber);
//		System.out.println("Analyzed postcode: " + postcode);
//		System.out.println("Analyzed city: " + city);
		System.out.println("Analyzed addr: " + addr.toString());
		
		lastSearchInput = find;
		lastSearchAddress = addr;
		
		result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS), 5, 3, 10l));
		
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 1, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 2, list size: " + result.size());
		}
		if(result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 3, list size: " + result.size());
		}
		if(result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 4, list size: " + result.size());
		}
		if(result.size() < 1 && result.size() < limitAmountOfResults) { 
			result.addAll(resizeSearcResult(AddressHolder.search(addr,SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 5, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(AddressHolder.search(addr,SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 6, list size: " + result.size());
		}
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
			result.addAll(resizeSearcResult(
				AddressHolder.search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS), 5, 3, 10l));
			System.out.println("Search 7, list size: " + result.size());
		}
		
		
		
		Address tempAddr = new Address();
		tempAddr.setStreet(find);
		if(result.size() != 1 && result.size() < limitAmountOfResults) {
		result.addAll(resizeSearcResult(AddressHolder.search(tempAddr, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
		System.out.println("Search 10, list size: " + result.size());
		}
		
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY), 5, 3, 10l));
			System.out.println("Search 8, list size: " + result.size());
		}
		if(result.size() < 1) {
			result.addAll(resizeSearcResult(AddressHolder.search(addr, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN), 5, 3, 10l));
		System.out.println("Search 9, list size: " + result.size());
		}
		
//		for(Postcode pc : list.values()){
//			for(Street st : pc.getStreets().values()){
//				for(Housenumber hn : st.getHousenumbers().values()){
//					result.add(hn.toString());
//				}
//			}
//		}
		
		//Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
//		// Remove duplicates and return
//		//Main.log("Addresse Suggestion time: " + timerUtil.toString());
		result = result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
		
		for(String str : result){
			System.out.println(str);
		}
		System.out.println("Count Results: " + result.size());
		return result;
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
	
	private List<String> searchSuggestions(String find, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		Map<String, Postcode> list = new HashMap<String, Postcode>();
		List<String> result = new ArrayList<String>();
		
		//parseUserInput(find);
		return analyzeUserinput(find, limitAmountOfResults);
		
		//Address addrBuild = AddressParser.parse(find, true);
		
		//System.out.println(find);
		//System.out.println(addrBuild.toString());
//			list = AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY));
//			}
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY));
//			}
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY));
//			}
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY));
//			}
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS, SearchEnum.ANY));
//			}
//			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS));
//			}
//			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
//			}
//			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
//			}
//			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
//					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN));
//			}
			//System.out.println("Address results: " + AddressHolder.count(list));
//			for(Postcode pc : list.values()){
//				for(Street st : pc.getStreets().values()){
//					for(Housenumber hn : st.getHousenumbers().values()){
//						result.add(hn.toString());
//					}
//				}
//			}
//
//		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
//		// Remove duplicates and return
//		timerUtil.off();
//		//Main.log("Addresse Suggestion time: " + timerUtil.toString());
//		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
	
	public List<String> searchSuggestionsStreet(String find, long limitAmountOfResults){
		Address addrBuild = AddressParser.parse(find, true);
		
		List<String> result = new ArrayList<String>();
		
			Map<String, Postcode> list; 
			list = AddressHolder.search(addrBuild,
					SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY);
			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
			}
			System.out.println("Address results: " + AddressHolder.count(list));
			for(Postcode pc : list.values()){
				for(Street st : pc.getStreets().values()){
					result.add(st.getStreet());
				}
			}

		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
	
	public Address getSearchResult(float[] lonLat){
		Housenumber hn = AddressHolder.searchHousenumber(lonLat);
		return new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
	}
	
	public Address getNearstSearchResult(RegionFloat input){
		Address addr =  getSearchResult(input.getMiddlePoint());
		if (addr == null) {
			Collection<Housenumber> hns = AddressHolder.searchRegionHousenumbers(input).values();
			Housenumber hn = regionRecursiveLookup(input);
			addr = new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
		}
		return addr;
	}
	
	public Housenumber regionRecursiveLookup(RegionFloat input){
		Map<RegionFloat, Housenumber> hns = AddressHolder.searchRegionHousenumbers(input);
		Housenumber house = null;
		if(hns.size() == 1){ 
			for(Housenumber hn : hns.values()) house = hn;
		} else {
			RegionFloat r = new RegionFloat(input.x1 - 0.000001f, input.y1 + 0.000001f, input.x2 + 0.000001f, input.y2 - 0.000001f);
			house = regionRecursiveLookup(r);
		}
		return house;
	}
	
	public List<String> searchSuggestions(RegionFloat input, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		List<String> result = new ArrayList<String>();
		Collection<Housenumber> hns = AddressHolder.searchRegionHousenumbers(input).values();
		for(Housenumber hn : hns){
			result.add(hn.toString());
		}
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Lon/lat Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
	
	public void addAddress(float[] lonLat, String street, String housenumber, String postcode, String city){
		Postcode pc = AddressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		AddressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode,  city);
	}
	
	private int acceptLvl1 = 0, acceptLvl2 = 0, acceptLvl3 = 0, acceptNot = 0;
	public void addressParsed(dk.itu.n.danmarkskort.models.ParsedAddress addr) {
		timerUtilA.on();
        if(addr != null) {
        	parsedAddresses.add(addr);
			float[] lonLat = new float[] {addr.getFirstLon(), addr.getFirstLat()};
			
			//if(addr.getPostcode() != null && !addr.getPostcode().matches("(^[0-9]{4}$)")) System.out.println("Postcode sucks: " + addr.toString());
			
			if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
				if(AddressValidator.isCityname(addr.getCity())) {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity());
				} else {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
				}
				//if(addr.getStreet().matches(".*[0-9].*")) System.out.println("lvl1 " + addr.toStringShort());
				acceptLvl1++;
			}else if(AddressValidator.isAddressMinimum(
        				AddressValidator.prepStreetname(addr.getStreet()),
        				AddressValidator.prepHousenumber(addr.getHousenumber()),
        				AddressValidator.prepPostcode(addr.getPostcode())
        				)) {
        	addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
        	//System.out.println("lvl2: " + addr.toString());
        	acceptLvl2++;
			} else {
				Address addrParsed = AddressParser.parse(addr.toStringShort(), false);
				if(addrParsed != null 
						&& addrParsed.getStreet() != null && addrParsed.getStreet().length() > 0
						&& addrParsed.getHousenumber() != null && addrParsed.getHousenumber().length() > 0
						&& addrParsed.getPostcode() != null && addrParsed.getPostcode().length() == 4) {
					addrParsed.setLonLat(lonLat);
					
					if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
						if(AddressValidator.isCityname(addrParsed.getCity())) {
							addAddress(lonLat, addrParsed.getStreet(), addrParsed.getHousenumber(), addrParsed.getPostcode(), addrParsed.getCity());
						} else {
							addAddress(lonLat, addrParsed.getStreet(), addrParsed.getHousenumber(), addrParsed.getPostcode(), null);
						}
						acceptLvl3++;
					}
					if(addr.getStreet().matches("[0-9]+")) System.out.println("lvl3 " + addr.toStringShort());
					//System.out.println("lvl3: " + addr.toStringShort());
				} else {
					//System.out.println("Not: " + addr.toString());
					acceptNot++;
					addressesNotAcceptedCount++;
				}
         }        	
			lonLat = null;
        }
    }

	public void onLWParsingFinished() {
		timerUtilA.off();
		System.out.print("acceptLvl1: " + acceptLvl1 + ", acceptLvl2: " + acceptLvl2 + ", acceptLvl3: " + acceptLvl3 + ", acceptNot: " + acceptNot);
		Main.log("Addresses parse first to last time: " + timerUtilA.toString());
		timerUtilB.on();
		for(Entry<String, Postcode> entry : AddressHolder.postcodes.entrySet()){
			entry.getValue().setCity(postcodeCityBestMatch.getMatch(entry.getKey()));
		}
		timerUtilB.off();
		Main.log("Addresses PostcodeCityCombination time: " + timerUtilB.toString());
		saveInputToTextFile();
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);		
	}
	
	private void saveListToTextFile(){
		Path path = Paths.get("Debug_AddressController.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//			List<String> result = new ArrayList<String>();
//			Map<String, Postcode> list = new HashMap<String, Postcode>();
//			Address addr = new Address();
//			String strs = "0 1 2 3 4 5 6 7 8 9 , .";
//			String[] strArr = strs.split(" ");
//			for(String str : strArr){
//				addr.setStreet(str);
//				list.putAll(AddressHolder.search(addr,SearchEnum.CONTAINS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
//			}
//			addr.setStreet("");
//			list.putAll(AddressHolder.search(addr,SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
//			
//			for(Postcode pc : list.values()){
//				for(Street st : pc.getStreets().values()){
//					for(Housenumber hn : st.getHousenumbers().values()){
//						result.add(hn.toString());
//					}
//				}
//			}
			

//		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
//		result = result.parallelStream().distinct().collect(Collectors.toList());
//		//result = result.parallelStream().distinct().limit(500).collect(Collectors.toList());
		
//		for(String str : result){
//			 writer.write(str + "\n");
//		}
		
		for(ParsedAddress addr : parsedAddresses){
			 writer.write(addr.getStreet() + " | " + addr.getHousenumber() + " | " + addr.getPostcode() + " | " + addr.getCity() + "\n");
		}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveInputToTextFile(){
		Path pathAll = Paths.get("Debug_Addr_All.txt");
		Path pathStreet = Paths.get("Debug_Addr_All_Streets.txt");
		Path pathHousenumber = Paths.get("Debug_Addr_All_Housenumber.txt");
		Path pathPostcode = Paths.get("Debug_Addr_All_Postcode.txt");
		Path pathCity = Paths.get("Debug_Addr_All_City.txt");
		try {
			BufferedWriter writerAll = Files.newBufferedWriter(pathAll);
			BufferedWriter writerStreet = Files.newBufferedWriter(pathStreet);
			BufferedWriter writerHousenumber = Files.newBufferedWriter(pathHousenumber);
			BufferedWriter writerPostcode = Files.newBufferedWriter(pathPostcode);
			BufferedWriter writerCity = Files.newBufferedWriter(pathCity);
			
			for(ParsedAddress addr : parsedAddresses){
//				writerAll.write(addr.getStreet() + " | " + addr.getHousenumber() + " | " + addr.getPostcode() + " | " + addr.getCity() + "\n");
//				if(addr.getStreet() != null && addr.getStreet().matches("[^a-zA-ZæøåÆØÅ\\s]{1,}")) writerStreet.write(addr.getStreet() + "\n");
//				if(addr.getHousenumber() != null && addr.getHousenumber().matches("[^0-9]{1,}")) writerHousenumber.write(addr.getHousenumber() + "\n");
				if(!AddressValidator.isPostcode(addr.getPostcode())) writerPostcode.write(addr.getPostcode() + "\n");
//				if(!AddressValidator.isCityname(addr.getCity())) writerCity.write(addr.getCity() + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getAddressSize() {
		return AddressHolder.count();
	}
}