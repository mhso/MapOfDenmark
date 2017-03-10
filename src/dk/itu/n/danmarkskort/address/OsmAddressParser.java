package dk.itu.n.danmarkskort.address;

public class OsmAddressParser {
	private Address buildAddress;
	
	public OsmAddressParser(Address buildAddress){
		this.buildAddress = buildAddress;
	}
	
	public Address parseKeyAddr(int nodeId, long lat, long lon, String k, String v){
		if(isAddress(k)){
			String cleanKey = cleanKey(k);
			buildAddress.setNodeId(nodeId);
			buildAddress.setLat(lat);
			buildAddress.setLon(lon);
			setKeyFromValue(cleanKey, v);
			return buildAddress;
		}
		return null;
	}
	
	private boolean isAddress(String key){
		return key.length() < cleanKey(key).length();
	}
	
	private String cleanKey(String key){
		return key.replaceAll("addr:", "").trim();
	}
	
	private void setKeyFromValue(String key, String value){
		switch(key){
		case "housenumber":			buildAddress.setHousenumber(value);
		case "housename":			buildAddress.setHousename(value);
		case "street":				buildAddress.setStreet(value);
		case "postcode":			buildAddress.setPostcode(value);
		case "city":				buildAddress.setCity(value);
		case "country":				buildAddress.setCountry(value);
		case "floor":				buildAddress.setFloor(value);
		}
	}
}