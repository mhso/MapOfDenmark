package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;

public class AddressManager {
	private Map<Long, Address> addresses;
	private Map<String, Postcode> postcodes;
	private Map<String, Postcode> streets;
	
	public AddressManager(){
		addresses =  new HashMap<Long, Address>();
		postcodes = new HashMap<String, Postcode>();
		streets = new HashMap<String, Postcode>();
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
	
	public Postcode getPostcodesFromStreet(String street){
		return streets.get(street);
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
		
		// Adding to the address path
		updateAddressPathMapping(addr);
	}
	
	private void updateAddressPathMapping(Address addr){
		if(addr != null){
			// Adding postcode to mapping
			if(addr.getPostcode() != null){
				Postcode postcode = postcodes.get(addr.getPostcode());
				if(postcode == null) postcode = new Postcode(addr.getCity());
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
						if(!housenumber.contains(addr)) housenumber.add(addr);
					}
				}
			}
		}
	}
	
}