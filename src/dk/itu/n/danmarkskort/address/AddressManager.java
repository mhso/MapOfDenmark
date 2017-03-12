package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;

public class AddressManager {
	private Map<Long, Address> addresses;
	private Map<String, Postcode> countryToPostcodes;
	private Map<String, Street> postcodeToStreet;
	private Map<String, Housenumber> streetToHousenumber;
	private Map<String, Address> housenumberToAddress;
	
	public AddressManager(){
		addresses =  new HashMap<Long, Address>();
		countryToPostcodes = new HashMap<String, Postcode>();
		postcodeToStreet = new HashMap<String, Street>();
		streetToHousenumber = new HashMap<String, Housenumber>();
		housenumberToAddress = new HashMap<String, Address>();
	}
	
	public Map<Long, Address> getAddresses(){
		return addresses;
	}
	
	public Address search(String indputStr){
		return null;
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
		OsmAddressParser oap = new OsmAddressParser(addr);
		addr = oap.parseKeyAddr(addr, nodeId, lat, lon, k, v);
		addresses.put(addr.getNodeId(), addr);
		countryToPostcodes.put(addr.getCountry(), new Postcode(addr.getPostcode()));
		postcodeToStreet.put(addr.getPostcode(), new Street(addr.getCity()));
		streetToHousenumber.put(addr.getStreet(), new Housenumber(addr.getHousenumber()));
		housenumberToAddress.put(addr.getHousenumber(), addr);
	}
}