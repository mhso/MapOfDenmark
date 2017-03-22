package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PostcodeCityCombination {
	private Map<String, Integer> combinations;
	private static PostcodeCityCombination instance;
	private final static Lock lock = new ReentrantLock();
	
	private PostcodeCityCombination(){
		combinations = new HashMap<String, Integer>();
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
	
	public void add(Integer postcode, String city){
		String key = postcode+"%"+city;
		
		if(combinations.get(key) != null){
			//int count = combinations.get(key);
			int count = combinations.get(key);
			count++;
			combinations.put(key, count);
		} else {
			combinations.put(key, 1);
		}
	}
	
	public Map<Integer, String> getBestMatches(){
		Map<Integer, String> bestMatches = new HashMap<Integer, String>();
		for(String entry1 : combinations.keySet()){
			String[] entrys1 = entry1.split("%");
			Integer postcode1 = Integer.parseInt(entrys1[0]);
			String city1 = entrys1[1];
			int count1 = combinations.get(entry1);
			
			for(String entry2 : combinations.keySet()){	
				String[] entrys2 = entry2.split("%");
				Integer postcode2 = Integer.parseInt(entrys2[0]);
				String city2 = entrys2[1];
				int count2 = combinations.get(entry2);
				
				if(postcode1 == postcode2 && city1.equalsIgnoreCase(city2) && count1 >= count2){
					bestMatches.put(postcode1, city1);
				} else {
					bestMatches.put(postcode2, city2);
				}
			}
		}		
		return bestMatches;
	}
	
	public void printCombinationMap(){
		for(String entry : combinations.keySet()){
			String[] entrys = entry.split("%");
			System.out.println("Post nr.: "+entrys[0]+" City: "+entrys[1]+" Count:"+combinations.get(entry));
		}
	}
	
	public void printBestMaches(){
		for(Entry<Integer, String> entry : getBestMatches().entrySet()){
			System.out.println("Post nr.: "+entry.getKey()+" City: "+entry.getValue());
		}
	}
}
