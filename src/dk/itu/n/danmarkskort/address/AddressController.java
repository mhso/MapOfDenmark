package dk.itu.n.danmarkskort.address;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.ParsedObject;

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
	
	public Set<String> getSearchSuggestions(String find){ return searchSuggestions(find); }
	
	public Address getSearchResult(String find){
		AddressParser ap = new AddressParser();
		Address addrBuild = preciseMatch(ap.parse(find));
		if(addrBuild != null) System.out.println("getSearchResult: "+addrBuild.toString());
		return addrBuild;
	}
	
	private Set<String> searchSuggestions(String find){
		AddressParser ap = new AddressParser();
		Address addrBuild = ap.parse(find);
		Set<String> resultSet = new TreeSet<String>();
		Set<String> setLevens = new TreeSet<String>();
		if(addrBuild.getStreet() != null && !confirmStreetExist(addrBuild.getStreet())) {
			setLevens = streetSearchLevenshteinDistance(addrBuild.getStreet());
			resultSet.addAll(setLevens);
		} else {
			System.out.println("Addr: accepted "+addrBuild.getStreet());
		}
		System.out.println("searchSuggestions: "+addrBuild.toString());
		if(resultSet.size() == 0){
			Set<String>set = shortAddresses.keySet()
	                .stream()
	                .filter(s -> s.toLowerCase().contains(addrBuild.getStreet().toLowerCase()))
	                .collect(Collectors.toSet());
			resultSet.addAll(set);
		}
		if(addrBuild.getHousenumber() != null){
			Set<String>set2 = shortAddresses.keySet()
	                .stream()
	                .filter(s -> 
	                s.toLowerCase().contains(addrBuild.getStreet().toLowerCase())
	            			&& s.toLowerCase().contains(addrBuild.getHousenumber().toLowerCase()))
			 .collect(Collectors.toSet());
			if(set2 != null) resultSet = set2;
		}
		if(addrBuild.getHousenumber() != null && addrBuild.getPostcode() == -1){
			Set<String>set3 = shortAddresses.keySet()
	                .stream()
	                .filter(s -> 
	                s.toLowerCase().contains(addrBuild.getStreet().toLowerCase())
	            			&& s.toLowerCase().contains(addrBuild.getHousenumber().toLowerCase())
	            			&& s.toLowerCase().contains(Integer.toString(addrBuild.getPostcode()))
	                ).collect(Collectors.toSet());
			if(set3 != null) resultSet = set3;
		}	
		return resultSet;
	}
	
	private Set<String> streetSearchLevenshteinDistance(String inputStr){
		Set<String> set = new TreeSet<String>(); 
		if(inputStr != null){
			for(Postcode postcode : postcodes.values()){
				for(String str : postcode.getStreets().keySet()){
					if(StringUtils.getLevenshteinDistance(str.toLowerCase(), inputStr.toLowerCase()) == 1) { set.add(str);
						System.out.println(str+" "+StringUtils.getLevenshteinDistance(str.toLowerCase(), inputStr.toLowerCase()));
					}
				}
			}
		}
		return set;
	}
	
	private Address preciseMatch(Address addr){
		Address result = addresses.values().stream()
				.filter(x -> addr.toStringShort().equalsIgnoreCase(x.toStringShort())														
				).findFirst()
				.orElse(null);
		if (result != null) return result;
		return addr;
	}
	
	private boolean confirmStreetExist(String inputStr){
		Address result = addresses.values().stream()
				.filter((x) -> inputStr.equalsIgnoreCase(x.getStreet()))
				.findAny()
				.orElse(null);
		return (result != null);
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