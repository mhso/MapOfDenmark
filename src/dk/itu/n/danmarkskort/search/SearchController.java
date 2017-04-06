package dk.itu.n.danmarkskort.search;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.address.Housenumber;
import dk.itu.n.danmarkskort.address.RegionFloat;
import dk.itu.n.danmarkskort.newmodels.Region;

public class SearchController{
	private static SearchController instance;
	private final static Lock lock = new ReentrantLock();
	
	private SearchController(){
		
	}
		
	public static SearchController getInstance(){
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                	SearchController tmpInstance = new SearchController();
                    instance = tmpInstance;
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }
	
	public List<String> getSearchFieldSuggestions(String inputStr){
		long limitAmountOfResults = 5;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,7}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		
		if(inputStr == null || inputStr.isEmpty()) return null;
		
		if(inputStr.matches(cordsRegex +"\\; "+ cordsRegex)){
			String[] strArr = inputStr.replaceAll(";", ",").split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return AddressController.getInstance().searchSuggestions(
					new RegionFloat(cord[0], cord[1], cord[2], cord[3]), limitAmountOfResults);
		}else if(inputStr.matches(cordsRegex)) {
			String[] strArr = inputStr.split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return AddressController.getInstance().searchSuggestions(
					new RegionFloat(cord[0], cord[1], cord[0], cord[1]), limitAmountOfResults);
		} else {
			return AddressController.getInstance().getSearchSuggestions(inputStr, limitAmountOfResults);
		}
	}
	
	public Address getSearchFieldAddressObj(String inputStr){
		if(inputStr == null || inputStr.isEmpty()) return null;
		String cordRegex = "((\\-{0,1})([0-9]{1,3})(\\.)(\\-{0,1})([0-9]{5,7}))";
		String cordsRegex = cordRegex + "(\\,\\s)" + cordRegex;
		
		if(inputStr.matches(cordsRegex +"\\; "+ cordsRegex)){
			String[] strArr = inputStr.replaceAll(";", ",").split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return AddressController.getInstance().getNearstSearchResult(new RegionFloat(cord[0], cord[1], cord[2], cord[3]));
		}else if(inputStr.matches(cordsRegex)) {
			String[] strArr = inputStr.split(", ");
			float[] cord = new float[strArr.length];
			for(int i=0; i<cord.length; i++) cord[i] = Float.parseFloat(strArr[i]);
			return AddressController.getInstance().getSearchResult(cord);
		} else {
			return AddressController.getInstance().getSearchResult(inputStr);
		}
	} 
}
