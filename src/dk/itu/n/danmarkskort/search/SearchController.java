package dk.itu.n.danmarkskort.search;

import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedObject;

public class SearchController{
	private static SearchController instance;
	private final static Lock lock = new ReentrantLock();
	
	public SearchController(){
		
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
	
	public Set<String> getSearchFieldSuggestions(String inputStr){
		return AddressController.getInstance().getSearchSuggestions(inputStr);
	}
	
	public Address getSearchFieldAddressObj(String inputStr){
		return AddressController.getInstance().getSearchResult(inputStr);
	} 
}
