package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;

public interface OSMParserListener {
	
	void onParsingStarted();
	void onParsingGotObject(ParsedObject parsedObject);
	void onLineCountHundred();
	void onParsingFinished();
	void onWayLinked(ParsedWay way);
}	
