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
		case "flats":				buildAddress.setFlats(value);
		case "conscriptionnumber":	buildAddress.setConscriptionnumber(value);
		case "street":				buildAddress.setStreet(value);
		case "place":				buildAddress.setPlace(value);
		case "postcode":			buildAddress.setPostcode(value);
		case "city":				buildAddress.setCity(value);
		case "country":				buildAddress.setCountry(value);
		case "full":				buildAddress.setFull(value);
		case "door":				buildAddress.setDoor(value);
		case "unit":				buildAddress.setUnit(value);
		case "floor":				buildAddress.setFloor(value);
		case "name":				buildAddress.setName(value);
		}
	}
}