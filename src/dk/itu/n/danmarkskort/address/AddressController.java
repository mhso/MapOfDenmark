package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressController implements OSMParserListener{
	private Map<float[], Address> addresses;

	private static AddressController instance;
	private HashMap<String, HashMap> addressDatabase;

	private final static Lock lock = new ReentrantLock();

	private int numAddresses;
	
	private AddressController(){
		addresses =  new HashMap<float[], Address>();
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
	
	public List<String> getSearchSuggestions(String find){ return searchSuggestions(find); }
	
	public Address getSearchResult(String find){
		AddressParser ap = new AddressParser();
		Address addrBuild = AddressSearchPredicates.addressEquals(addresses, ap.parse(find));
		return addrBuild;
	}
	
	private List<String> searchSuggestions(String find){
		//System.out.println("Addr: looking for suggestions ");
		AddressParser ap = new AddressParser();
		Address addrBuild = ap.parse(find);
		List<String> result = new ArrayList<String>();
		//System.out.println("searchSuggestions: "+addrBuild.toString());

		// Find suggestion from street
		if(addrBuild.getStreet() != null){
			result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
							AddressSearchPredicates.streetStartsWith(addrBuild.getStreet()) , 5l));
			// Find suggestion from street, accept 0-1 mistakes in street input
			if (result.size() == 0) {
				result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
						AddressSearchPredicates.streetLevenshteinDistance(addrBuild.getStreet(),-1,2) , 5l));
				// Find suggestion from street, accept 0-3  mistakes in street input
				if (result.size() == 0) {
					result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
							AddressSearchPredicates.streetLevenshteinDistance(addrBuild.getStreet(),-1,4) , 5l));
				}
			}
		}
		
		// Find suggestion from street and housenumber
		if(addrBuild.getStreet() != null && addrBuild.getHousenumber() != null){
			result = (AddressSearchPredicates.filterToStringShort(addresses, 
					AddressSearchPredicates.streetEqualsHousenumberStartsWith(addrBuild) , 5l));
			// Find suggestion from street and housenumber, accept 0-1 mistakes in street and housenumber input
			if (result.size() == 0) {
				result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
						AddressSearchPredicates.streetHousenumberLevenshteinDistance(addrBuild,-1,2,-1,2) , 5l));
				// Find suggestion from street and housenumber, accept 0-3 mistakes in street
				// and 0-1 mistakes in housenumber input
				if (result.size() == 0) {
					result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
							AddressSearchPredicates.streetHousenumberLevenshteinDistance(addrBuild,-1,4,-1,2) , 5l));
				}
			}
		}
		
		if(result.size() == 0 && addrBuild.getPostcode() != null){
			result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
					AddressSearchPredicates.postcodeEquals(addrBuild.getPostcode()) , 5l));
		}
		// Remove duplicates and return
		return result.stream().distinct().collect(Collectors.toList());
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
        HashMap<String, HashMap<String, Float[]>> postcode;
        HashMap<String, Float[]> street;

        if(addressDatabase.containsKey(address.getPostcode())) {
        	postcode = addressDatabase.get(address.getPostcode());
		} else {
            postcode = new HashMap<>();
            addressDatabase.put(address.getPostcode(), postcode);
        }

        if(postcode.containsKey(address.getStreet())) {
        	street = postcode.get(address.getStreet());
		} else {
            street = new HashMap<>();
            postcode.put(address.getStreet(), street);
        }

        if(!street.containsKey(address.getHousenumber())) {
        	Float[] coords = new Float[]{address.getFirstLon(), address.getFirstLat()};
            street.put(address.getHousenumber(), coords);
            numAddresses++;
        }
        
        if(address != null) {
	        float lon = address.getFirstLon();
			float lat = address.getFirstLat();			
			float[] lonLat = new float[] {lon,lat};
			AddressParser ap = new AddressParser();
			//Address addrParsed = ap.parse(address.getStreet() +" "+address.getHousenumber()+" "+address.getPostcode()+" "+address.getCity());
			Address addrParsed = ap.parse(address.toStringShort());
			if(addrParsed != null) {
				addrParsed.setLonLat(lonLat);
				//System.out.println("OSM: "+address.toString());
				//System.out.println("ADC: "+addrParsed.toStringShort());
				if(!address.toStringShort().equals(addrParsed.toStringShort())) {
					System.out.println("--- ALARM NOT MATCHING ALARM ---\n --> OSM: "+address.toStringShort()
						+"\n --> ADC: "+addrParsed.toStringShort()
						+"\n --> ADC: "+addrParsed.toString());
				}
				addresses.put(addrParsed.getLonLat(), addrParsed);
				PostcodeCityCombination.getInstance().add(addrParsed.getPostcode(), addrParsed.getCity());
			}
        }	
    }

	public void onLWParsingFinished() {
		PostcodeCityCombination.getInstance().printBestMaches();
		Main.log("Addresses: " + numAddresses);
	}
	
	@Override
	public void onParsingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParsingGotObject(ParsedObject parsedObject) {
		if(parsedObject instanceof ParsedAddress) {
			ParsedAddress omsAddr = (ParsedAddress) parsedObject;
			float lon = Float.parseFloat(omsAddr.getAttributes().get("lon"));
			float lat = Float.parseFloat(omsAddr.getAttributes().get("lat"));
			float[] lonLat = new float[] {lon,lat};
			Address addr = createOsmAddress(lonLat, omsAddr.attributes);
			if(addr != null) addresses.put(addr.getLonLat(), addr);
		}
	}
	
	@Override
	public void onParsingFinished() {
		// TODO Auto-generated method stub
		PostcodeCityCombination.getInstance().bestMatches();
		PostcodeCityCombination.getInstance().clearCombinations();
		PostcodeCityCombination.getInstance().printBestMaches();
		Main.log("AdresseController found: "+addresses.size()+" adresses");
	}

	@Override
	public void onLineCountHundred() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWayLinked(ParsedWay way) {
		// TODO Auto-generated method stub
		
	}
}