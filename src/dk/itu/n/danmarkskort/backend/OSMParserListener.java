package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.ParsedObject;

public interface OSMParserListener {
	
	public abstract void onParsingStarted();
	public abstract void onParsingGotObject(ParsedObject parsedObject);
	public abstract void onLineCountHundred();
	public abstract void onParsingFinished();
	
}	
