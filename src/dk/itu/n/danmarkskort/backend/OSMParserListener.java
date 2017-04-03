package dk.itu.n.danmarkskort.backend;

public interface OSMParserListener {
	
	void onParsingStarted();
	void onParsingGotItem(Object parsedItem);
	void onLineCountHundred();
	void onParsingFinished();
}	
