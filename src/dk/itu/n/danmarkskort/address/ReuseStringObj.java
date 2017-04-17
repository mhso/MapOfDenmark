package dk.itu.n.danmarkskort.address;

import java.util.HashMap;

public class ReuseStringObj {
	private static HashMap<String, String> map = new HashMap<String, String>();
	
	public static String make(String key){
		if(key == null) return "";
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
				String strObj = new String(key);
				map.put(key, strObj);
				return strObj;
		}
	}
}
