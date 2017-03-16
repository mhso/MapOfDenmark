package dk.itu.n.danmarkskort.backend;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.*;

// This class is used as content handler when reading XML (or OSM) in OSMParser.
public class OSMNodeHandler implements ContentHandler {

	private OSMParser parser;
	private String fileName;
	private int lineCount;
	private Locator locator;
	private List<ParsedObject> currentParsedObjects = new ArrayList<ParsedObject>();
	private Map<Long, ParsedNode> nodeMap = new HashMap<Long, ParsedNode>();
	
	
	public OSMNodeHandler(OSMParser parser, String fileName) {
		this.fileName = fileName;
		this.parser = parser;
	}
	
	private void incrementLineCount() {
		int currentCount = locator.getLineNumber();
		if(lineCount == currentCount) return;
		lineCount = currentCount;
		if(lineCount % 100 == 0) {
			for(OSMParserListener listener : parser.parserListeners) listener.onLineCountHundred();
		}
	}
	
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void startDocument() throws SAXException {
		Main.log("Parsing started.");
		createOSMDirectory();
		for(OSMParserListener listener : parser.parserListeners) listener.onParsingStarted();
	}

	public void endDocument() throws SAXException {
		for(OSMParserListener listener : parser.parserListeners) listener.onParsingFinished();
		nodeMap.clear();
		Main.log("Parsing finished.");
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

	public void endPrefixMapping(String prefix) throws SAXException {}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		incrementLineCount();
		
		switch(qName) {
		
		case "osm":
			addParsedObject(new ParsedOSM(), atts, qName);
			break;
		case "note":
			addParsedObject(new ParsedNote(), atts, qName);
			break;
		case "meta":
			addParsedObject(new ParsedMeta(), atts, qName);
			break;
		case "bounds":
			addParsedObject(new ParsedBounds(), atts, qName);
			break;
		case "node":
			addParsedNode(new ParsedNode(), atts, qName);
			break;
		case "tag":
			addTagToParsedObject(atts);
			break;
		case "way":
			addParsedWay(new ParsedWay(), atts, qName);
			break;
		case "nd":
			break;
		case "relation":
			break;
		case "member":
			break;

		}
	}

	public void addParsedObject(ParsedObject parsedObject, Attributes atts, String qName) {
		parsedObject.addAttributes(atts);
		parsedObject.setQName(qName);
		currentParsedObjects.add(parsedObject);
	}
	
	public void addTagToParsedObject(Attributes atts) {
		String key = atts.getValue("k");
		ParsedObject lastParsedObject = getLastParsedObject();
		
		if(key.contains("addr:")) {
			currentParsedObjects.remove(lastParsedObject);
			lastParsedObject = new ParsedAddress(lastParsedObject);
			currentParsedObjects.add(lastParsedObject);
		}
		
		String value = atts.getValue("v");
		if(key != null && value != null) {
			 lastParsedObject.addAttribute(key, value);
		}
	}
	
	public void addParsedNode(ParsedObject parsedObject, Attributes atts, String qName) {
		parsedObject.addAttributes(atts);
		parsedObject.setQName(qName);
		currentParsedObjects.add(parsedObject);
	}

	public void addParsedWay(ParsedObject parsedObject, Attributes atts, String qName) {
		parsedObject.addAttributes(atts);
		parsedObject.setQName(qName);
		currentParsedObjects.add(parsedObject);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		incrementLineCount();
		ParsedObject lastParsedObject = getLastParsedObject();
		if(qName.equals(lastParsedObject.getQName())) {
			lastParsedObject.parseAttributes();
			
			//Add nodes to nodemap
			if(lastParsedObject instanceof ParsedNode) {
				ParsedNode node = (ParsedNode) lastParsedObject;
				nodeMap.put(node.getId(), node);
			}
			
			//Map nodes to way
			if(lastParsedObject instanceof ParsedWay) {
				ParsedWay way = (ParsedWay) lastParsedObject;
				
				if(nodeMap.containsKey())
			}
			
			for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotObject(lastParsedObject);
			currentParsedObjects.remove(lastParsedObject);
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(getLastParsedObject() != null) {
			String text = new String(ch, start, length);
			if(text.trim().length() > 0) getLastParsedObject().addAttribute("innerText", text);
		}
	}

	private ParsedObject getLastParsedObject() {
		if(currentParsedObjects.size() == 0) return null;
		return currentParsedObjects.get(currentParsedObjects.size()-1);
	}
	
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

	public void processingInstruction(String target, String data) throws SAXException {}

	public void skippedEntity(String name) throws SAXException {}
	
	public String getDirectoryPath() {
		Main.log("Fetching checksum");
		String fileHash = fileName;
		try {
			fileHash = Util.getFileChecksumMD5(new File(fileName));
			Main.log("Got checksum: " + fileHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		parser.setChecksum(fileHash);
		return Util.getCurrentDirectoryPath() + "/parsedOSMFiles/" + fileHash;
	}
	
	public void createOSMDirectory() {
		File file = new File(getDirectoryPath());
		if(!file.exists()) {
			Main.log("Created directory.");
			file.mkdirs();
		} else {
			Main.log("Using existing directory.");
		}
	}
}
