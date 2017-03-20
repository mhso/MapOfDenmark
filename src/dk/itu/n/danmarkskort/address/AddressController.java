package dk.itu.n.danmarkskort.address;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.ParsedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class AddressController implements OSMParserListener{
	private Map<Long, Address> addresses;
	private Map<Integer, Postcode> postcodes;
	private Map<String, Address> shortAddresses;
	private static AddressController instance;
	private final static Lock lock = new ReentrantLock();
	
	private AddressController(){
		addresses =  new TreeMap<Long, Address>();
		postcodes = new TreeMap<Integer, Postcode>();
		shortAddresses = new TreeMap<String, Address>();
	}
	
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
		if(addrBuild != null) System.out.println("getSearchResult: "+addrBuild.toString());
		return addrBuild;
	}
	
	private List<String> searchSuggestions(String find){
		//System.out.println("Addr: looking for suggestions ");
		AddressParser ap = new AddressParser();
		Address addrBuild = ap.parse(find);
		List<String> result = new ArrayList<String>();
		System.out.println("searchSuggestions: "+addrBuild.toString());

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
		
		if(result.size() == 0 && addrBuild.getPostcode() != -1){
			result.addAll(AddressSearchPredicates.filterToStringShort(addresses, 
					AddressSearchPredicates.postcodeEquals(Integer.toString(addrBuild.getPostcode())) , 5l));
		}
		// Remove dublicates
		result = result.stream().distinct().collect(Collectors.toList());
		System.out.println("Before sort");
		for(String str : result){
			System.out.println(str);
		}
		System.out.println("After sort");
		result.stream().sorted();
		for(String str : result){
			System.out.println(str);
		}
		return result;
	}

	public void addOsmAddress(long nodeId, float lat, float lon, String k, String v){
		Address addr;
		if (addresses.containsKey(nodeId)) {
			addr = addresses.get(nodeId);
		} else {
			addr = new Address(nodeId, lon, lon);
		}
		AddressOsmParser oap = new AddressOsmParser(addr);
		addr = oap.parseKeyAddr(nodeId, lat, lon, k, v);
		if(addr != null) addresses.put(addr.getNodeId(), addr);
	}
	
	public Address createOsmAddress(Long nodeId, float lat, float lon){
		if(nodeId != null && lat != -1f && lon != -1f){
			Address addr;
			if (addresses.containsKey(nodeId)) {
				addr = addresses.get(nodeId);
			} else {
				addr = new Address(nodeId, lon, lon);
			}
			addresses.put(addr.getNodeId(), addr);
			return addr;
			}
		return null;
	}
	
	private void updateAllAddressPathMapping(){
		for(Address addr : addresses.values()){
			updateAddressPathMapping(addr);
			shortAddresses.put(addr.toStringShort(), addr);
		}
	}
	
	private void updateAddressPathMapping(Address addr){
		if(addr != null){
			// Adding postcode to mapping
			if(addr.getPostcode() != -1){
				Postcode postcode = postcodes.get(addr.getPostcode());
				
				if(postcode == null) postcode = new Postcode(addr.getPostcode(), addr.getCity());
				postcodes.put(addr.getPostcode(), postcode);
				
				// Adding street to mapping
				if(addr.getStreet() != null){
					Street street = postcode.getStreets().get(addr.getStreet());
					if(street == null) street = new Street(addr.getStreet());
					postcode.getStreets().put(addr.getStreet(), street);
					
					// Adding housenumber to map
					if(addr.getHousenumber() != null){
						street.getHousenumbers().put(addr.getHousenumber(), addr);
					}
				}
			}
		}
	}
	
	@Override
	public void onParsingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParsingGotObject(ParsedObject parsedObject) {
		if(parsedObject instanceof ParsedAddress) {
			ParsedAddress omsAddr = (ParsedAddress) parsedObject;
			//Main.log(omsAddr.getAttributes().get("id"));
			if(omsAddr.getAttributes().get("id") != null) {
				long nodeId = Long.parseLong(omsAddr.getAttributes().get("id"));
				float lat = Float.parseFloat(omsAddr.getAttributes().get("lat"));
				float lon = Float.parseFloat(omsAddr.getAttributes().get("lon"));
				
				Address addr = createOsmAddress(nodeId, lat, lon);
				AddressOsmParser aop = new AddressOsmParser(addr);
				aop.parseKeyAddr(omsAddr.attributes);				
			}
		}
	}
	@Override
	public void onParsingFinished() {
		// TODO Auto-generated method stub
		
		// Adding to the address path
		updateAllAddressPathMapping();
		
		Main.log("AdresseController found: "+addresses.size()+" adresses");
	}

	@Override
	public void onLineCountHundred() {
		// TODO Auto-generated method stub
	}
}