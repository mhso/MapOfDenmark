package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;

public interface OSMParserListener {
	
	public abstract void onParsingStarted();
	public abstract void onParsingGotObject(ParsedObject parsedObject);
	public abstract void onLineCountHundred();
	public abstract void onParsingFinished();
	public abstract void onWayLinked(ParsedWay way);
}	
