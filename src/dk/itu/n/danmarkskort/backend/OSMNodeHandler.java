package dk.itu.n.danmarkskort.backend;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.*;

// This class is used as content handler when reading XML (or OSM) in OSMParser.
public class OSMNodeHandler implements ContentHandler {

	private XMLReader xmlReader;
	private String fileName;
	private ArrayList<String> keyList = new ArrayList<String>();
	private ParsedObject currentParsedObject = null;
	private ArrayList<ParsedObject> parsedObjects = new ArrayList<ParsedObject>();		
	
	public OSMNodeHandler(XMLReader xmlReader, String fileName) {
		this.xmlReader = xmlReader;
		this.fileName = fileName;
	}
	
	public void setDocumentLocator(Locator locator) {}

	public void startDocument() throws SAXException {
		createOSMDirectory();
		Main.log("Parsing started.");
	}

	public void endDocument() throws SAXException {
		Main.log("Parsing finished with " + parsedObjects.size() + " objects.");
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {}

	public void endPrefixMapping(String prefix) throws SAXException {}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		
		switch(qName) {
		
		case "osm":
			addParsedObject(new ParsedOSM(), atts);
			break;
		case "note":
			addParsedObject(new ParsedNote(), atts);
			break;
		case "meta":
			addParsedObject(new ParsedMeta(), atts);
			break;
		case "bounds":
			addParsedObject(new ParsedBounds(), atts);
			break;
		case "node":
			addParsedObject(new ParsedNode(), atts);
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

	public void addParsedObject(ParsedObject parsedObject, Attributes atts) {
		parsedObject.addAttributes(atts);
		currentParsedObject = parsedObject;
		parsedObjects.add(parsedObject);
	}
	
	public void addTagToParsedObject(Attributes atts) {
		String key = atts.getValue("k");
		String value = atts.getValue("v");
		if(key != null && value != null) {
			currentParsedObject.addAttribute(key, value);
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {}

	public void characters(char[] ch, int start, int length) throws SAXException {
		if(currentParsedObject != null) {
			String text = new String(ch, start, length);
			if(text.trim().length() > 0) currentParsedObject.addAttribute("text", text);
		}
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
