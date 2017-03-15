package dk.itu.n.danmarkskort.address;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedAddress;
import dk.itu.n.danmarkskort.models.ParsedObject;
import jdk.nashorn.internal.ir.SetSplitState;

public class AddressController implements OSMParserListener{
	private Map<Long, Address> addresses;
	private Map<String, Postcode> postcodes;
	private Map<String, Postcode> streets;
	private static AddressController instance;
	private final static Lock lock = new ReentrantLock();
	
	private AddressController(){
		addresses =  new TreeMap<Long, Address>();
		postcodes = new TreeMap<String, Postcode>();
		streets = new TreeMap<String, Postcode>();
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
	
	
	public Map<Long, Address> getAddresses(){
		return addresses;
	}
	
	public Address getAddressFromNodeId(long nodeId){
		return addresses.get(nodeId);
	}
	
	public Map<String, Postcode> getPostcodes(){
		return postcodes;
	}
	
	public Postcode getPostcode(String postcode){
		return postcodes.get(postcode);
	}
	
	public Map<String, Postcode> getStreets(){
		return streets;
	}
	
	public Postcode getPostcodeFromStreet(String street){
		return streets.get(street);
	}
	
	public Set<String> getAddrSearchResults(String find){
		return streetSearch(find);
	}
	
	private Set<String> streetSearch(String find){
		AddressParser ap = new AddressParser();
		Address addrBuild = ap.findMatch(find);
		String parsedFind = addrBuild.toStringShort();
		System.out.println(addrBuild.toString());
		Set<String> set = addresses.values()
                .stream()
                .filter(s -> s.toStringShort().toLowerCase().startsWith(addrBuild.getStreet().toLowerCase()))
                .map(Address::toStringShort)
                .collect(Collectors.toSet());
		return set;
	}
	
	private Set<String> getCityPostcodeFromStreet(Set<String> input){
		Set<String> set = new TreeSet<String>();
		for(String street : input){
			Postcode postcode = getPostcodeFromStreet(street);
			Street streetb = postcode.get(street);
			
			for (String housenumber : streetb.keySet()) {
				set.add(street+" "+housenumber+", "+postcode.getPostcode()+" "+postcode.getCity());
			}
		}
		return set;
	}
	
	public void add(Address address){
		addresses.put(address.getNodeId(), address);
	}
	
	public void addOsmAddress(long nodeId, double lat, double lon, String k, String v){
		Address addr;
		if (addresses.containsKey(nodeId)) {
			addr = addresses.get(nodeId);
		} else {
			addr = new Address(nodeId, lon, lon);
		}
		AddressOsmParser oap = new AddressOsmParser(addr);
		addr = oap.parseKeyAddr(addr, nodeId, lat, lon, k, v);
		if(addr != null) addresses.put(addr.getNodeId(), addr);
		
		// Adding to the address path
		updateAddressPathMapping(addr);
	}
	
	public Address createOsmAddress(Long nodeId, Double lat, Double lon){
		if(nodeId != null && lat != null && lon != null){
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
	
	private void updateAddressPathMapping(Address addr){
		if(addr != null){
			// Adding postcode to mapping
			if(addr.getPostcode() != null){
				Postcode postcode = postcodes.get(addr.getPostcode());
				
				if(postcode == null) postcode = new Postcode(addr.getPostcode(), addr.getCity());
				postcodes.put(addr.getPostcode(), postcode);
				
				// Adding street to mapping
				if(addr.getStreet() != null){
					Street street = postcode.get(addr.getStreet());
					if(street == null) street = new Street();
					postcode.put(addr.getStreet(), street);
					
					streets.put(addr.getStreet(), postcode);
					
					// Adding housenumber to map
					if(addr.getHousenumber() != null){
						Housenumber housenumber = street.get(addr.getHousenumber());
						if(housenumber == null) housenumber = new Housenumber();
						street.put(addr.getHousenumber(), housenumber);
						
						// Adding address to the housenumber list
						housenumber.put(addr.getHousenumber(), addr);
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
				double lat = Double.parseDouble(omsAddr.getAttributes().get("lat"));
				double lon = Double.parseDouble(omsAddr.getAttributes().get("lon"));
				Address addr = createOsmAddress(nodeId, lat, lon);
				
				AddressOsmParser aop = new AddressOsmParser(addr);
				aop.parseKeyAddr(addr, omsAddr.attributes);				
				// Adding to the address path
				updateAddressPathMapping(addr);
			}
		}
	}

	@Override
	public void onParsingFinished() {
		// TODO Auto-generated method stub
		Main.log("AdresseController found: "+addresses.size()+" adresses");
	}
}