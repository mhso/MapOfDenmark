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
	private Map<Long, Address> addresses;

	private static AddressController instance;
	private final static Lock lock = new ReentrantLock();
	
	private AddressController(){
		addresses =  new HashMap<Long, Address>();
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
		// Remove dublicates and return
		return result.stream().distinct().collect(Collectors.toList());
	}
	
	public Address createOsmAddress(Long nodeId, float lat, float lon, Map<String, String> attributes){
		if(nodeId != null){
				Address addr = addresses.get(nodeId);
				if (addr == null) addr = new Address(nodeId, lon, lon);
					AddressOsmParser aop = new AddressOsmParser(addr);
					aop.parseKeyAddr(attributes);
					if(addr.getCity() != null){
						PostcodeCityCombination.getInstance().add(addr.getPostcode(), addr.getCity());
					}
					return addr;
		}
		return null;
	}
	
	@Override
	public void onParsingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParsingGotObject(ParsedObject parsedObject) {
		if(parsedObject instanceof ParsedAddress) {
			ParsedAddress omsAddr = (ParsedAddress) parsedObject;
			if(omsAddr.getAttributes().get("id") != null) {
				long nodeId = Long.parseLong(omsAddr.getAttributes().get("id"));
				float lat = Float.parseFloat(omsAddr.getAttributes().get("lat"));
				float lon = Float.parseFloat(omsAddr.getAttributes().get("lon"));
				
				Address addr = createOsmAddress(nodeId, lat, lon, omsAddr.attributes);
				if(addr != null) addresses.put(addr.getNodeId(), addr);
			}
		}
	}
	
	@Override
	public void onParsingFinished() {
		// TODO Auto-generated method stub
		PostcodeCityCombination.getInstance().bestMatches();
		PostcodeCityCombination.getInstance().clearCombinations();
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