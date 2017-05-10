package dk.itu.n.danmarkskort.models;

import java.util.HashMap;

public class ReuseStringObj {
	private static HashMap<String, String> map = new HashMap<String, String>();
	private static int keysTotal, keysReused, keysCreated;
	
	public static String make(String key){
		keysTotal++;
		if(key == null) key = "";
		if(map.containsKey(key)) {
			keysReused++;
			return map.get(key);
		} else {
			keysCreated++;
			String strObj = new String(key);
			map.put(key, strObj);
			return strObj;
		}
	}
	
	public static void clear(){
		System.out.println("keysTotal: " + keysTotal + " keysReused: " + keysReused + " keysCreated: " + keysCreated);
		map.clear();
	}
}
