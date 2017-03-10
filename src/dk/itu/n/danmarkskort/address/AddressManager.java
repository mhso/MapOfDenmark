package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;

public class AddressManager {
	private Map<Long, Address> addresses;
	
	
	public AddressManager(){
		addresses =  new HashMap<Long, Address>();
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
	
	public void addOsmAddress(long nodeId, long lat, long lon, String k, String v){
		Address adr;
		if (addresses.containsKey(nodeId)) {
			adr = addresses.get(nodeId);
		} else {
			adr = new Address(nodeId, lon, lon);
		}
		OsmAddressParser oap = new OsmAddressParser(adr);
		adr = oap.parseKeyAddr(nodeId, lat, lon, k, v);
		addresses.put(adr.getNodeId(), adr);
	}
}
