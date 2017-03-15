package dk.itu.n.danmarkskort.search;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import dk.itu.n.danmarkskort.backend.OSMParserListener;
import dk.itu.n.danmarkskort.models.ParsedObject;

public class SearchController implements OSMParserListener{
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

	@Override
	public void onParsingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParsingGotObject(ParsedObject parsedObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLineCountHundred() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onParsingFinished() {
		// TODO Auto-generated method stub
		
	}
}
