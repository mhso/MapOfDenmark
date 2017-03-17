package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedObject;

public interface OSMParserListener {
	
	 void onParsingStarted();
	 void onParsingGotObject(ParsedObject parsedObject);
	 void onLineCountHundred();
	 void onParsingFinished();
	
}	
