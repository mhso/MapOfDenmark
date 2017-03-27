package dk.itu.n.danmarkskort.address;

import java.util.Map;

public class AddressOsmParser {
	private Address buildAddress;
	
	public AddressOsmParser(Address buildAddress){
		this.buildAddress = buildAddress;
	}
	
	public Address parseKeyAddr(Map<String, String> keysValues){
		for(String key : keysValues.keySet()){
			if(isAddress(key)) setKeyFromValue(cleanKey(key), keysValues.get(key));
		}
		return buildAddress;
	}
	
	private boolean isAddress(String key){
		if(key != null) return key.length() > cleanKey(key).length();
		return false;
	}
	
	private String cleanKey(String key){
		return key.toLowerCase().replaceAll("addr:", "").trim();
	}
	
	private void setKeyFromValue(String key, String value){
		value = value.trim();
		switch(key){
		case "street":			buildAddress.setStreet(value);
		break;
		case "housenumber":		buildAddress.setHousenumber(value);
		break;
		case "postcode":
			int postcodeTemp = -1;
			try{
				postcodeTemp = Integer.parseInt(value);
			} catch(NumberFormatException e){
				String str = value.replaceAll("[^0-9]", "");
				if(str.length() == 4) postcodeTemp = Integer.parseInt(str);
				//e.printStackTrace();
			} finally{
				buildAddress.setPostcode(postcodeTemp);
			}
		break;
//		case "city":			buildAddress.setCity(null);
//		break;
		}
	}
}