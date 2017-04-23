package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.RegionFloat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AddressController {
	private AddressHolder addressHolder = new AddressHolder();
	private int addressesNotAcceptedCount;
	private TimerUtil timerUtilA = new TimerUtil();
	private TimerUtil timerUtilB = new TimerUtil();
	private PostcodeCityBestMatch postcodeCityBestMatch;
	private AddressSuggestion addressSuggestion;
	private AddressRegionSearch addressRegionSearch;
	private boolean debug = false;
	
	public AddressController(){
		postcodeCityBestMatch = new PostcodeCityBestMatch();
		addressSuggestion = new AddressSuggestion();
		addressRegionSearch = new AddressRegionSearch();
	}
	
	public AddressHolder getAddressHolder() {
		return addressHolder;
	}
	
	public void setAddressHolder(AddressHolder holder) {
		addressHolder = holder;
	}
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ return addressSuggestion.searchSuggestions(find, limitAmountOfResults); }
	
	public Address getSearchResult(String find){
		
		Address addrBuild = AddressParser.parse(find, false);
		Map<String, Postcode> list;
		list = addressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
		if(list.size() < 1) { list = addressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY);
		}
		if(list.size() < 1) { list = addressHolder.search(addrBuild,
				SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
		}
		if(list.size() < 1) { list = addressHolder.search(addrBuild,
				SearchEnum.LEVENSHTEIN, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.EQUALS);
		}
		if(list.size() < 1) { list = addressHolder.search(addrBuild,
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
		Postcode pc = addressHolder.postcodes.get(postcode);
		if(pc == null) pc = new Postcode(postcode, city);
		pc.addAddress(street, housenumber, lonLat);
		addressHolder.postcodes.put(postcode, pc);
		postcodeCityBestMatch.add(postcode,  city);
	}
	
	private int acceptLvl1 = 0, acceptLvl2 = 0, acceptNot = 0;
	public void addressParsed(ParsedAddress addr) {
		timerUtilA.on();
        if(addr != null) {
			float[] lonLat = new float[] {addr.getFirstLon(), addr.getFirstLat()};
			
			//if(debug) if(addr.getPostcode() != null && !addr.getPostcode().matches("(^[0-9]{4}$)")) System.out.println("Postcode sucks: " + addr.toStringParted());
			
			if(AddressValidator.isAddressMinimum(addr.getStreet(), addr.getHousenumber(), addr.getPostcode())){
				if(AddressValidator.isCityname(addr.getCity())) {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity());
				} else {
					addAddress(lonLat, addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), null);
				}
				//if(debug) if(addr.getStreet().matches(".*[0-9].*")) System.out.println("lvl1 " + addr.toStringParted());
				acceptLvl1++;
			}else if(AddressValidator.isAddressMinimum(
        				AddressValidator.prepStreetname(addr.getStreet()),
        				AddressValidator.prepHousenumber(addr.getHousenumber()),
        				AddressValidator.prepPostcode(addr.getPostcode())
        				)) {
        	addAddress(lonLat, AddressValidator.prepStreetname(addr.getStreet()),
    				AddressValidator.prepHousenumber(addr.getHousenumber()),
    				AddressValidator.prepPostcode(addr.getPostcode()), null);
        	acceptLvl2++;
			} else {
					acceptNot++;
					addressesNotAcceptedCount++;
         }        	
			lonLat = null;
        }
    }

	public void onLWParsingFinished() {
		timerUtilA.off();
		if(debug) System.out.print("acceptLvl1: " + acceptLvl1 + ", acceptLvl2: " + acceptLvl2 + ", acceptNot: " + acceptNot);
		Main.log("Addresses parse first to last time: " + timerUtilA.toString());
		timerUtilB.on();
		for(Entry<String, Postcode> entry : addressHolder.postcodes.entrySet()){
			entry.getValue().setCity(postcodeCityBestMatch.getMatch(entry.getKey()));
		}
		postcodeCityBestMatch.cleanup();
		timerUtilB.off();
		Main.log("Addresses PostcodeCityCombination time: " + timerUtilB.toString());
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);		
	}
	
	public int getAddressSize() {
		return addressHolder.count();
	}
}