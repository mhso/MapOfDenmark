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
	private Map<float[], String> addressesNotAccepted;
	private int addressesNotAcceptedCount;
	private List<Address> searchList = new ArrayList<Address>();

	private static AddressController instance;
	private HashMap<String, HashMap> addressDatabase;

	private final static Lock lock = new ReentrantLock();
	private static final int TreeMap = 0;

	private int numAddresses;
	
	private AddressController(){
		addresses =  new HashMap<float[], Address>();
		addressesNotAccepted =  new HashMap<float[], String>();
		addressDatabase = new HashMap<>();
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
		System.out.println("find: " + find);
		System.out.println("addrBuild: " + addrBuild.toStringShort());
		
		// perfect input
//		Postcode postcode = AddressHolder.postcodes.get(addrBuild.getPostcode());
//		if(postcode != null){
//			System.out.println("postcode: OK");
//			String city = postcode.getCity();
//			Street street = postcode.getStreet(addrBuild.getStreet());
//			if(street != null){
//				System.out.println("street: OK");
//				Housenumber housenumber = street.getHousenumber(addrBuild.getHousenumber());
//				if(housenumber != null) {
//					System.out.println("housenumber: OK");
//					result.add(street.getStreet() + " " + housenumber.getHousenumber() + ", " + postcode.getPostcode() + " " + postcode.getCity());
//					return result;
//				}
//			}
//
//		} else {
			List<Postcode> list = AddressHolder.search(addrBuild, AddressHolder.Enums.STARTSWITH, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE);
			System.out.println("Not in use :"
					+ list.size() +
					" | "
			);
			
//			System.out.println("Street :"
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.STARTSWITH, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.CONTAINS, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "	
//			);
			
//			System.out.println("Street Housenumber :"
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.EQUALS, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.STARTSWITH, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.CONTAINS, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "	
//			);
//			
//			System.out.println("Street Housenumber postcode:"
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.EQUALS, AddressHolder.Enums.EQUALS, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.EQUALS, AddressHolder.Enums.STARTSWITH, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.EQUALS, AddressHolder.Enums.EQUALS, AddressHolder.Enums.CONTAINS, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "	
//			);
//			
//			System.out.println("Postcode :"
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.EQUALS, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.STARTSWITH, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.CONTAINS, AddressHolder.Enums.NOT_IN_USE).size() +
//					" | "	
//			);
//			
//			System.out.println("City :"
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.EQUALS).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.STARTSWITH).size() +
//					" | "
//					+ AddressHolder.search(addrBuild, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.NOT_IN_USE, AddressHolder.Enums.CONTAINS).size() +
//					" | "	
//			);
			
			for(Postcode ps : list){
				for(Street st : ps.getStreets().values()){
					for(Housenumber hn : st.getHousenumbers().values()){
						result.add(st.getStreet() + " " + hn.getHousenumber() + " " + ps.getPostcode() + " " + ps.getCity());
					}
				}
			}
			
//		}
		
		Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
		// Remove duplicates and return
		timerUtil.off();
		Main.log("Addresse Suggestion time: " + timerUtil.toString());
		return result.parallelStream().distinct().limit(10l).collect(Collectors.toList());
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
//        HashMap<String, HashMap<String, Float[]>> postcode;
//        HashMap<String, Float[]> street;

//        if(addressDatabase.containsKey(address.getPostcode())) {
//        	postcode = addressDatabase.get(address.getPostcode());
//		} else {
//            postcode = new HashMap<>();
//            addressDatabase.put(address.getPostcode(), postcode);
//        }
//
//        if(postcode.containsKey(address.getStreet())) {
//        	street = postcode.get(address.getStreet());
//		} else {
//            street = new HashMap<>();
//            postcode.put(address.getStreet(), street);
//        }
//
//        if(!street.containsKey(address.getHousenumber())) {
//        	Float[] coords = new Float[]{address.getFirstLon(), address.getFirstLat()};
//            street.put(address.getHousenumber(), coords);
//            numAddresses++;
//        }
        
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