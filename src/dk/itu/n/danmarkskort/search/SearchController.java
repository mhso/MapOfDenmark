package dk.itu.n.danmarkskort.search;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.address.AddressController;

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
		return AddressController.getInstance().getSearchSuggestions(inputStr, limitAmountOfResults);
	}
	
	public Address getSearchFieldAddressObj(String inputStr){
		return AddressController.getInstance().getSearchResult(inputStr);
	} 
}
