package dk.itu.n.danmarkskort.models;

import java.util.HashMap;

import dk.itu.n.danmarkskort.Main;

public class ReuseStringObj {
	private static HashMap<String, String> map = new HashMap<String, String>(145000);
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
		Main.log("ReuseStringObj, keysTotal: " + keysTotal + " keysReused: " + keysReused + " keysCreated: " + keysCreated);
		map.clear();
	}
}
