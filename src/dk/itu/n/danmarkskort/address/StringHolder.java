package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StringHolder {
	private static StringHolder instance;
	private final static Lock lock = new ReentrantLock();
	private HashMap<String, StringObj> map =  new HashMap<String, StringObj>();
	
	public static StringHolder getInstance(){
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                	StringHolder tmpInstance = new StringHolder();
                    instance = tmpInstance;
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }	
	
	public StringObj make(String key){
		if(map.containsKey(key)) {
			return map.get(key);
		} else {
			StringObj strObj = new StringObj(key);
			map.put(key, strObj);
			return strObj;
		}
	}
}
