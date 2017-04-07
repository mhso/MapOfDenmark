package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PostcodeCityCombination {
	private static Map<String, Integer> combinations = new HashMap<String, Integer>();
	private static Map<String, String> bestMatches = new HashMap<String, String>();
	private final static String SPLIT_VALUE = "<%>";
	
	private PostcodeCityCombination(){
	}
	
	public static void add(String postcode, String city){
		String key = postcode+SPLIT_VALUE+city;
		if(city != null) {
			if(combinations.containsKey(key)){
				int count = combinations.get(key);
				combinations.put(key, ++count);
			} else {
				combinations.put(key, 1);
			}
		}
	}
	
	public static void compileBestMatches(){
		for(Map.Entry<String, Integer> entry1: combinations.entrySet()) {
			String[] arrPostCity1 = entry1.getKey().split(SPLIT_VALUE);
			   for(Map.Entry<String, Integer> entry2: combinations.entrySet()) {
				   
				   // Dont compare twice
			       if (System.identityHashCode(entry1.getKey()) > System.identityHashCode(entry2.getKey())) continue;
			       
			       String[] arrPostCity2 = entry2.getKey().split(SPLIT_VALUE);
			       // compare value1 and value2;
			       if(arrPostCity1[0].equalsIgnoreCase(arrPostCity2[0])
			    		   && arrPostCity1[1].equalsIgnoreCase(arrPostCity2[1])
			    		   && entry1.getValue() >= entry2.getValue()){
						bestMatches.put(arrPostCity1[0], arrPostCity1[1]);
					} else {
						bestMatches.put(arrPostCity2[0], arrPostCity2[1]);
					}      
			   }
		}
		clearCombinations();
	}
	
	public static int sizeBestMatches(){
		return bestMatches.size();
	}
	
	public static void clearCombinations(){
		if(bestMatches.size() > 0) combinations.clear();
	}
	
	public static void clearBestMatches(){
		if(bestMatches.size() > 0) bestMatches.clear();
	}
	
	public static String getCity(String postcode){
		if(combinations != null) compileBestMatches();
		if(postcode == null) return null;
		return bestMatches.get(postcode);
	}
	
	public static void printCombinationMap(){
		for(String entry : combinations.keySet()){
			String[] entrys = entry.split(SPLIT_VALUE);
			System.out.println("Post nr.: "+entrys[0]+" City: "+entrys[1]+" Count:"+combinations.get(entry));
		}
	}
	
	public static void printBestMaches(){
		for(Entry<String, String> entry : bestMatches.entrySet()){
			System.out.println("Post nr.: "+entry.getKey()+" City: "+entry.getValue());
		}
	}
}