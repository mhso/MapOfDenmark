package dk.itu.n.danmarkskort.backend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.mapdata.NodeMap;
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
	private int byteCount;
	private Locator locator;
	private List<ParsedObject> currentParsedObjects = new ArrayList<ParsedObject>();
	private NodeMap nodes;
	private InputStream inputStream;
	private long fileSize;
	
	public OSMNodeHandler(OSMParser parser, String fileName) {
		this.fileName = fileName;
		fileSize = Util.getFileSize(new File(fileName));
		this.parser = parser;
		nodes = new NodeMap(21);
		inputStream = parser.getInputStream();
	}
	
	private void incrementLineCount() {
		if(locator.getLineNumber() % 1000 != 0) return;
		int currentCount = 0;
		try {
			currentCount = (int)((((double)fileSize-(double)inputStream.available())/(double)fileSize)*100);
			// System.out.println(currentCount);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(currentCount == byteCount) return;
		byteCount = currentCount;
		
		for(OSMParserListener listener : parser.parserListeners) listener.onLineCountHundred();
	}
	
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void startDocument() throws SAXException {
		createOSMDirectory();
		Main.log("Parsing started.");
		for(OSMParserListener listener : parser.parserListeners) listener.onParsingStarted();
	}

	public void endDocument() throws SAXException {
		Main.log("Parsing finished.");
		for(OSMParserListener listener : parser.parserListeners) listener.onParsingFinished();

		Main.log("Found " + nodes.size() + " nodes");
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

	public void endPrefixMapping(String prefix) throws SAXException {}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		incrementLineCount();
		int i = 0;
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
			addNode(atts);
			addParsedObject(new ParsedNode(), atts, qName);
			break;
		case "tag":
			addTagToParsedObject(atts);
			break;
		case "way":
			break;
		case "nd":
			break;
		case "relation":
			break;
		case "member":
			break;
		}
	}

	public void addNode(Attributes atts) {
		long key = Long.parseLong(atts.getValue("id"));
        float lon = Float.parseFloat(atts.getValue("lon"));
        float lat = Float.parseFloat(atts.getValue("lat"));
        nodes.put(key, lon, lat);
    }
    public NodeMap getNodeMap() { return nodes; }

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
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		incrementLineCount();
		ParsedObject lastParsedObject = getLastParsedObject();
		if(qName.equals(lastParsedObject.getQName())) {
			lastParsedObject.parseAttributes();
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
