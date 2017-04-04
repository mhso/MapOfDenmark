package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;

import java.util.ArrayList;
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
		//System.out.println("find: " + find);
		//System.out.println("addrBuild: " + addrBuild.toStringShort());
		
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
					SearchEnum.EQUALS, SearchEnum.ANY, SearchEnum.ANY, SearchEnum.ANY));
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
	
	public void addressParsed(dk.itu.n.danmarkskort.newmodels.ParsedAddress address) {
        
        if(address != null) {	
			float[] lonLat = new float[] {address.getFirstLon(), address.getFirstLat()};
			Address addrParsed = AddressParser.parse(address.toStringShort(), false);
			if(addrParsed != null 
					&& addrParsed.getStreet() != null
					&& addrParsed.getHousenumber() != null
					&& addrParsed.getPostcode() != null) {
				addrParsed.setLonLat(lonLat);
				//System.out.println("OSM: "+address.toString());
				//System.out.println("ADC: "+addrParsed.toStringShort());
//				if(!address.toStringShort().equals(addrParsed.toStringShort())) {
//					System.out.println("--- ALARM NOT MATCHING ALARM ---\n --> OSM: "+address.toStringShort()
//						+"\n --> ADC: "+addrParsed.toStringShort()
//						+"\n --> ADC: "+addrParsed.toString());
//				}			
				Postcode postcode = AddressHolder.postcodes.get(addrParsed.getPostcode());
				if(postcode == null) postcode = new Postcode(addrParsed.getPostcode(), addrParsed.getCity());
				postcode.addAddress(addrParsed.getStreet(), addrParsed.getHousenumber(), lonLat);
				
				AddressHolder.postcodes.put(addrParsed.getPostcode(), postcode);
				PostcodeCityCombination.getInstance().add(addrParsed.getPostcode(), addrParsed.getCity());
			} else {
				addressesNotAcceptedCount++;
			}
        }	
    }

	public void onLWParsingFinished() {
		PostcodeCityCombination.getInstance().compileBestMatches();
		PostcodeCityCombination.getInstance().clearCombinations();
		for(Entry<String, Postcode> entry : AddressHolder.postcodes.entrySet()){
			entry.getValue().setCity(PostcodeCityCombination.getInstance().getCity(entry.getKey()));
		}
		PostcodeCityCombination.getInstance().clearBestMatches();
		Main.log("Addresses (accepted): " + getAddressSize());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);
	}
	
	public int getAddressSize() {
		return AddressHolder.count();
	}
}