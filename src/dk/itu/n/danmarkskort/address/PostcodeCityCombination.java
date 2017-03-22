package dk.itu.n.danmarkskort.address;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PostcodeCityCombination {
	private Map<String[], Integer> combinations;
	
	
	public PostcodeCityCombination(){
		combinations = new HashMap<String[], Integer>();
	}
	
	public void add(Integer postcode, String city){
		String[] key = new String[]{postcode+"", city};
		
		if(combinations.get(key) != null){
			//int count = combinations.get(key);
			int count = combinations.get(key);
			combinations.put(key, count++);
		} else {
			combinations.put(key, 1);
		}
	}
	
	public Map<Integer, String> getBestMatches(){
		Map<Integer, String> bestMatches = new HashMap<Integer, String>();
		for(Entry<String[], Integer> entry1 : combinations.entrySet()){
			for(Entry<String[], Integer> entry2 : combinations.entrySet()){
				
				Integer postcode1 = Integer.parseInt(entry1.getKey()[0]);
				Integer postcode2 = Integer.parseInt(entry2.getKey()[0]);
				String city1 = entry1.getKey()[1];
				String city2 = entry2.getKey()[1];
				int count1 = entry1.getValue();
				int count2 = entry2.getValue();
				
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
		for(Entry<String[], Integer> entry : combinations.entrySet()){
			System.out.println("Post nr.: "+entry.getKey()[0]+" City: "+entry.getKey()[1]+" Count:"+entry.getValue());
		}
	}
	
	public void printBestMaches(){
		for(Entry<Integer, String> entry : getBestMatches().entrySet()){
			System.out.println("Post nr.: "+entry.getKey()+" City: "+entry.getKey());
		}
	}
}
