package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressController{
	private int addressesNotAcceptedCount;
	private static AddressController instance;
	private final static Lock lock = new ReentrantLock();
	TimerUtil timerUtilA = new TimerUtil();
	TimerUtil timerUtilB = new TimerUtil();
	private AddressController(){
//		addresses =  new HashMap<float[], Address>();
	}
	int count = 0; 

	public static AddressController getInstance(){
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                	AddressController tmpInstance = new AddressController();
                    instance = tmpInstance;
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }
	
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
	
	private List<String> searchSuggestions(String find, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		Address addrBuild = AddressParser.parse(find, true);
		List<String> result = new ArrayList<String>();
		
			Map<String, Postcode> list; 
			list = AddressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS);
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY));
			}
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY));
			}
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY));
			}
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY));
			}
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS, SearchEnum.ANY));
			}
			if(list.size() != 1 && list.size() < limitAmountOfResults) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.EQUALS));
			}
			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.STARTSWITH, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
			}
			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.LEVENSHTEIN, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
			}
			if(list.size() < 1) { list.putAll(AddressHolder.search(addrBuild,
					SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.LEVENSHTEIN));
			}
			System.out.println("Address results: " + AddressHolder.count(list));
			for(Postcode pc : list.values()){
				for(Street st : pc.getStreets().values()){
					for(Housenumber hn : st.getHousenumbers().values()){
						result.add(hn.toString());
					}
				}
			}

		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Addresse Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
	
	public Address getSearchResult(float[] lonLat){
		Housenumber hn = AddressHolder.searchHousenumber(lonLat);
		return new Address(hn.getLonLat(), hn.getStreet().getStreet(), hn.getHousenumber(), hn.getPostcode().getPostcode(), hn.getPostcode().getCity());
	}
	
	public Address getNearstSearchResult(RegionFloat input){
		Address addr =  getSearchResult(input.centerPoint());
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
		pc.addAddress(
				street,
				housenumber, 
				lonLat);
		
		AddressHolder.postcodes.put(postcode, pc);
		PostcodeCityCombination.add(postcode, city);
	}
	
	private int acceptLvl1 = 0, acceptLvl2 = 0, acceptLvl3 = 0, acceptNot = 0;
	public void addressParsed(dk.itu.n.danmarkskort.models.ParsedAddress address) {
		
		timerUtilA.on();
        if(address != null) {
			float[] lonLat = new float[] {address.getFirstLon(), address.getFirstLat()};
			
        	if(AddressValidator.isAddressComplete(address.getStreet(),
        			address.getHousenumber(),
        			address.getPostcode(),
        			address.getCity())) {
        		//System.out.println("Addr: OK: " + address.toString());
        		addAddress(lonLat, address.getStreet(), address.getHousenumber(), address.getPostcode(), address.getCity());
        		acceptLvl1++;
        	}else if(AddressValidator.isAddressMinimum(address.getStreet(), address.getHousenumber(), address.getPostcode())){
        	addAddress(lonLat, address.getStreet(), address.getHousenumber(), address.getPostcode(), null);
        	acceptLvl2++;
        }else if(AddressValidator.isAddressMinimum(
        				AddressValidator.prepStreetname(address.getStreet()),
        				AddressValidator.prepHousenumber(address.getHousenumber()),
        				AddressValidator.prepPostcode(address.getPostcode())
        				)) {
        			addAddress(lonLat, address.getStreet(), address.getHousenumber(), address.getPostcode(), "");
        			acceptLvl3++;
            		//System.out.println("Addr: OK: " + address.toString());
         } else {
            			acceptNot++;
//		        		System.out.println("Addr: NOT OK: " + address.toString());
//		        		System.out.println(" ---> " + address.getStreet() + " | "
//		        		+ address.getHousenumber() + " | "
//		        		+ address.getPostcode() + " | "
//		        		+ address.getCity());
//		        		
//		        		System.out.println(" ------> " + AddressValidator.prepStreetname(address.getStreet()) + " | "
//		        		+ AddressValidator.prepHousenumber(address.getHousenumber()) + " | "
//        				+ AddressValidator.prepPostcode(address.getPostcode()));
         }
//			Address addrParsed = AddressParser.parse(address.toStringShort(), false);
//			if(addrParsed != null 
//					&& addrParsed.getStreet() != null && addrParsed.getStreet().length() > 0
//					&& addrParsed.getHousenumber() != null && addrParsed.getHousenumber().length() > 0
//					&& addrParsed.getPostcode() != null && addrParsed.getPostcode().length() == 4) {
//				addrParsed.setLonLat(lonLat);
//				Postcode postcode = AddressHolder.postcodes.get(addrParsed.getPostcode());
//				if(postcode == null) postcode = new Postcode(addrParsed.getPostcode(), addrParsed.getCity());
//				postcode.addAddress(addrParsed.getStreet(), addrParsed.getHousenumber(), lonLat);
//				
//				AddressHolder.postcodes.put(addrParsed.getPostcode(), postcode);
//				PostcodeCityCombination.add(addrParsed.getPostcode(), addrParsed.getCity());
//			} else {
//				addressesNotAcceptedCount++;
//			}
//			lonLat = null;
//			addrParsed = null;
        }
    }

	public void onLWParsingFinished() {
		timerUtilA.off();
		System.out.print("acceptLvl1: " + acceptLvl1 + ", acceptLvl2: " + acceptLvl2 + ", acceptLvl3: " + acceptLvl3 + ", acceptNot: " + acceptNot);
		Main.log("Addresses parse first to last time: " + timerUtilA.toString());
		timerUtilB.on();
		for(Entry<String, Postcode> entry : AddressHolder.postcodes.entrySet()){
			entry.getValue().setCity(PostcodeCityCombination.getCity(entry.getKey()));
		}
		PostcodeCityCombination.clearBestMatches();
		timerUtilB.off();
		Main.log("Addresses PostcodeCityCombination time: " + timerUtilB.toString());
		
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);		
	}
	
	public int getAddressSize() {
		return AddressHolder.count();
	}
}