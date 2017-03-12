package dk.itu.n.danmarkskort.address;

public class OsmAddressParser {
	private Address buildAddress;
	
	public OsmAddressParser(Address buildAddress){
		this.buildAddress = buildAddress;
	}
	
	public Address parseKeyAddr(Address buildAddress, long nodeId, double lat, double lon, String k, String v){
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
		return key.length() > cleanKey(key).length();
	}
	
	private String cleanKey(String key){
		return key.replaceAll("addr:", "").trim();
	}
	
	private void setKeyFromValue(String key, String value){
		switch(key){
		case "street":				buildAddress.setStreet(value);
		break;
		case "housenumber":			buildAddress.setHousenumber(value);
		break;
		case "postcode":			buildAddress.setPostcode(value);
		break;
		case "city":				buildAddress.setCity(value);
		break;
		case "country":				buildAddress.setCountry(value);
		break;
		case "floor":				buildAddress.setFloor(value);
		break;
		case "housename":			buildAddress.setHousename(value);
		break;
		}
	}
}