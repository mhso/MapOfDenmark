package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;

public interface OSMParserListener {
	
	void onParsingStarted();
	void onParsingGotItem(Object parsedItem);
	void onLineCountHundred();
	void onParsingFinished();
	void onWayLinked(ParsedWay way);
}	
