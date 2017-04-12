package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.models.ParsedAddress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	AddressSuggestion addressSuggestion = new AddressSuggestion();
	AddressRegionSearch addressRegionSearch = new AddressRegionSearch();
	
	public AddressController(){
		postcodeCityBestMatch = new PostcodeCityBestMatch();
	}
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ return addressSuggestion.searchSuggestions(find, limitAmountOfResults); }
	
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
	
	public Address getSearchResult(float[] lonLat){
		return addressRegionSearch.getSearchResult(lonLat);
	}
	
	public Address getNearstSearchResult(RegionFloat input){
		return addressRegionSearch.getNearstSearchResult(input);
	}
	
	public List<String> searchSuggestions(RegionFloat input, long limitAmountOfResults){
		 return addressRegionSearch.searchSuggestions(input, limitAmountOfResults);
	}
	
	public void addAddress(float[] lonLat, String street, String housenumber, String postcode, String city){
		Postcode pc = AddressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		AddressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode,  city);
	}
	
	private int acceptLvl1 = 0, acceptLvl2 = 0, acceptLvl3 = 0, acceptNot = 0;
	public void addressParsed(ParsedAddress addr) {
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
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);		
	}
	
	public int getAddressSize() {
		return AddressHolder.count();
	}
}