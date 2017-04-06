package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StringHolder {
	private static HashMap<String, StringObj> map =  new HashMap<String, StringObj>();
	
	public static StringObj make(String key){
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
			StringObj strObj = new StringObj(key);
			map.put(key, strObj);
			return strObj;
		}
	}
}
