package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PostcodeCityCombination {
	private static PostcodeCityCombination instance;
	private final static Lock lock = new ReentrantLock();
	private Map<String, Integer> combinations;
	private Map<String, String> bestMatches;
	
	private PostcodeCityCombination(){
		combinations = new HashMap<String, Integer>();
		bestMatches = new TreeMap<String, String>();
	}
	
	public static PostcodeCityCombination getInstance(){
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                	PostcodeCityCombination tmpInstance = new PostcodeCityCombination();
                    instance = tmpInstance;
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }
	
	public void add(String postcode, String city){
		String key = postcode+"%"+city;
		
		if(combinations.get(key) != null){
			int count = combinations.get(key);
			count++;
			combinations.put(key, count);
		} else {
			combinations.put(key, 1);
		}
	}
	
	public void bestMatches(){
		for(String entry1 : combinations.keySet()){
			String[] entrys1 = entry1.split("%");
			String postcode1 = entrys1[0];
			String city1 = entrys1[1];
			int count1 = combinations.get(entry1);
				for(String entry2 : combinations.keySet()){	
					String[] entrys2 = entry2.split("%");
					String postcode2 = entrys2[0];
					String city2 = entrys2[1];
					int count2 = combinations.get(entry2);
						if(postcode1 == postcode2 && city1.equalsIgnoreCase(city2) && count1 >= count2){
							bestMatches.put(postcode1, city1);
						} else {
							bestMatches.put(postcode2, city2);
						}
				}
		}
	}
	
	public void clearCombinations(){
		if(bestMatches.size() > 0) combinations = null;
	}
	
	public String getCity(String postcode){
		if(postcode == null) return null;
		return bestMatches.get(postcode);
	}
	
	public void printCombinationMap(){
		for(String entry : combinations.keySet()){
			String[] entrys = entry.split("%");
			System.out.println("Post nr.: "+entrys[0]+" City: "+entrys[1]+" Count:"+combinations.get(entry));
		}
	}
	
	public void printBestMaches(){
		for(Entry<String, String> entry : bestMatches.entrySet()){
			System.out.println("Post nr.: "+entry.getKey()+" City: "+entry.getValue());
		}
	}
}