package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressController{
	private Map<float[], Address> addresses;
	private int addressesNotAcceptedCount;
	private List<Address> searchList = new ArrayList<Address>();

	private static AddressController instance;

	private final static Lock lock = new ReentrantLock();
	
	private AddressController(){
		addresses =  new HashMap<float[], Address>();
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
	
	public Address getAddressFromNodeId(long nodeId){ return addresses.get(nodeId); }
	
	public List<String> getSearchSuggestions(String find, long limitAmountOfResults){ return searchSuggestionsTest(find, limitAmountOfResults); }
	
	public Address getSearchResult(String find){
		Address addrBuild = AddressSearchPredicates.addressEquals(addresses, AddressParser.parse(find, false));
		return addrBuild;
	}
	
	private List<String> searchSuggestions(String find, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		//System.out.println("Addr: looking for suggestions ");
		Address addrBuild = AddressParser.parse(find, true);
		List<String> result = new ArrayList<String>();
		List<String> resultLast = new ArrayList<String>();
		//System.out.println("searchSuggestions: "+addrBuild.toString());
		Address addr = AddressSearchPredicates.addressEquals(addresses, addrBuild);
		if(addr != null) {
			result.add(addr.toStringShort());
			return result;
		}
		
		//Rebuild search list
		if(addrBuild != null){
			searchList = AddressSearchPredicates.filterToAddress(addresses, 
							AddressSearchPredicates.toStringShortContainsAddrParts(addrBuild) , Long.MAX_VALUE);
//			System.out.println("addresses: " + addresses.size());
//			System.out.println("searchList: " + searchList.size());
		}
		
		// Find suggestion from street
		if(addrBuild.getStreet() != null){
			result.addAll(AddressSearchPredicates.filterToStringShort(searchList, 
							AddressSearchPredicates.streetStartsWith(addrBuild.getStreet()) , limitAmountOfResults));
		}

			// Find suggestion from street and housenumber
			if(addrBuild.getHousenumber() != null){
				result = AddressSearchPredicates.filterToStringShort(searchList, 
						AddressSearchPredicates.streetEqualsHousenumberStartsWith(addrBuild) , limitAmountOfResults);
				
				result = AddressSearchPredicates.filterToStringShort(searchList, 
							AddressSearchPredicates.streetHousenumberEquals(addrBuild) , limitAmountOfResults);
				result.addAll(resultLast);
			}
			
			// Find suggestion from postcode
			if(addrBuild.getPostcode() != null){
				result.addAll(AddressSearchPredicates.filterToStringShort(searchList, 
						AddressSearchPredicates.postcodeStartsWith(addrBuild) , limitAmountOfResults));
				
				result.addAll(AddressSearchPredicates.filterToStringShort(searchList, 
						AddressSearchPredicates.postcodeEquals(addrBuild) , limitAmountOfResults));
			}
			
			// If nothing was found, look for misspelling
			// Find suggestion from street and housenumber, accept 0-1 mistakes in street and housenumber input	
			// Find suggestion from street, accept 0-1 mistakes in street input
			if (result.size() == 0 && addrBuild != null && addrBuild.getStreet() != null) {
				result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
						AddressSearchPredicates.streetLevenshteinDistance(addrBuild.getStreet(),-1,2) , limitAmountOfResults));
				
				// Find suggestion from street, accept 0-3  mistakes in street input
				if (result.size() == 0) {
					result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
							AddressSearchPredicates.streetLevenshteinDistance(addrBuild.getStreet(),1,4) , limitAmountOfResults));
				}
			
				if (result.size() == 0) {
				result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
						AddressSearchPredicates.streetHousenumberLevenshteinDistance(addrBuild,-1,2,-1,2) , limitAmountOfResults));
				}
				
				// Find suggestion from street and housenumber, accept 0-3 mistakes in street and 0-1 mistakes in housenumber input
				if (result.size() == 0) {
					result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
							AddressSearchPredicates.streetHousenumberLevenshteinDistance(addrBuild,1,4,-1,2) , limitAmountOfResults));
				}
			}
		
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Addresse Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().collect(Collectors.toList());
	}
	
	private List<String> searchSuggestionsTest(String find, long limitAmountOfResults){
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.on();
		//System.out.println("Addr: looking for suggestions ");
		Address addrBuild = AddressParser.parse(find, true);
		List<String> result = new ArrayList<String>();
		//System.out.println("find: " + find);
		//System.out.println("addrBuild: " + addrBuild.toStringShort());
		
			List<Postcode> list; 
			list = AddressHolder.search(addrBuild,
					SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE, SearchEnum.STARTSWITH, SearchEnum.NOT_IN_USE);
//			System.out.println("Address result 1: " + AddressHolder.count(list));
//			if(list.size() < limitAmountOfResults) { list = AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH);
//			System.out.println("Address result 2: " + AddressHolder.count(list));
//			}
//			if(list.size() < limitAmountOfResults) { list = AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.NOT_IN_USE);
//			System.out.println("Address result 3: " + AddressHolder.count(list));
//			}
//			if(list.size() < limitAmountOfResults) { list = AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.STARTSWITH, SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE);
//			System.out.println("Address result 4: " + AddressHolder.count(list));
//			}
//			if(list.size() < limitAmountOfResults) { list = AddressHolder.search(addrBuild,
//					SearchEnum.EQUALS, SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE);
//			System.out.println("Address result 5: " + AddressHolder.count(list));
//			}
//			if(list.size() < limitAmountOfResults) { list = AddressHolder.search(addrBuild,
//					SearchEnum.STARTSWITH, SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE, SearchEnum.NOT_IN_USE);
//			System.out.println("Address result 6: " + AddressHolder.count(list));
//			}
//			for(Postcode pc : list){
//				for(Street st : pc.getStreets().values()){
//					for(Housenumber hn : st.getHousenumbers().values()){
//						//System.out.println(st.getStreet() + " " + hn.getHousenumber() + " " + pc.getPostcode() + " " + pc.getCity());
//						result.add(st.getStreet() + " " + hn.getHousenumber() + " " + pc.getPostcode() + " " + pc.getCity());
//					}
//				}
//			}
//		System.out.println("Address result count: " + AddressHolder.count(list));
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Addresse Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().limit(limitAmountOfResults).collect(Collectors.toList());
	}
	
	public Address createOsmAddress(float[] lonLat, Map<String, String> attributes){
		if(lonLat != null){
			Address addr = addresses.get(lonLat);
			if (addr == null) addr = new Address(lonLat);
			AddressOsmParser aop = new AddressOsmParser(addr);
			aop.parseKeyAddr(attributes);
			if(addr.getPostcode() != null && addr.getCity() != null){
				PostcodeCityCombination.getInstance().add(addr.getPostcode(), addr.getCity());
			}
			return addr;
		}
		return null;
	}
	
	public void addressParsed(dk.itu.n.danmarkskort.lightweight.models.ParsedAddress address) {
        
        if(address != null) {	
			float[] lonLat = new float[] {address.getFirstLon(), address.getFirstLat()};
			//Address addrParsed = ap.parse(address.getStreet() +" "+address.getHousenumber()+" "+address.getPostcode()+" "+address.getCity());
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
				postcode.addAddress(addrParsed.getStreet().toLowerCase(), addrParsed.getHousenumber(), lonLat);
				
				AddressHolder.postcodes.put(addrParsed.getPostcode(), postcode);
				addresses.put(lonLat, addrParsed);
				PostcodeCityCombination.getInstance().add(addrParsed.getPostcode(), addrParsed.getCity());
			} else {
				//addressesNotAccepted.put(lonLat, address.toStringShort());
				addressesNotAcceptedCount++;
			}
        }	
    }

	public void onLWParsingFinished() {
		PostcodeCityCombination.getInstance().compileBestMatches();
		//PostcodeCityCombination.getInstance().printBestMaches();
		PostcodeCityCombination.getInstance().clearCombinations();
		for(Entry<String, Postcode> entry : AddressHolder.postcodes.entrySet()){
			entry.getValue().setCity(PostcodeCityCombination.getInstance().getCity(entry.getKey()));
		}
		//System.out.println("PostcodeCityCombination:"+PostcodeCityCombination.getInstance().sizeBestMatches());
//		int i=0;
//		for(Address addr : addresses.values()){
//			if(addr.toStringShort().contains("'")) System.out.println(i+": "+addr.toString());
//			if(++i > 250) break;
//		}
//		System.out.println("Not Accepted Addresses:");
//		i=0;
//		for(String addr : addressesNotAccepted.values()){
//			//if(addr.contains("'")) 
//				System.out.println(i+": "+addr);
//			if(++i > 300) break;
//		}
		Main.log("Addresses (accepted): " + addresses.size());
		Main.log("Addresses (not accepted): " + addressesNotAcceptedCount);
	}
	
	public int getAddressSize() {
		return addresses.size();
	}
}