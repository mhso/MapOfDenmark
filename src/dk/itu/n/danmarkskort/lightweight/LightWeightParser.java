package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.lightweight.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LightWeightParser implements ContentHandler {

    private String filename;
    private OSMParser parser;
    private InputStream inputStream;

    private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private NodeMap nodeMap = new NodeMap();
    //private EnumMap<LWWayType, ArrayList<ParsedItem>> ways;
    private HashMap<Long, ParsedWay> ways = new HashMap<>();
    private ArrayList<ParsedRelation> relations = new ArrayList<>();
    private ArrayList<ParsedAddress> addresses = new ArrayList<>();
    private ArrayList<ParsedSinglePointTag> singlePointTags = new ArrayList<>();

    private NodeMap.Node currentNode;
    private ParsedItem currentTag;
    private ParsedWay currentWay;
    private ParsedRelation currentRelation;
    private ParsedSinglePointTag currentSinglePointTag;
    private ParsedAddress currentAddress;
    private ArrayList<ParsedWay> currentMembers = new ArrayList<>();
    private ArrayList<Float> currentCoords = new ArrayList<>();

    private String currentType; // bør være en waytype senere
    private String currentName;
    private String currentStreet;
    private String currentPostCode;
    private String currentCity;
    private String currentHouseNumber;

    private int numWays;
    private int numRelations;
    private int numNodes;
    private int numAddresses;
    private int numSinglePointTags;

    public LightWeightParser(OSMParser parser, String filename) {
        this.parser = parser;
        this.filename = filename;
        this.inputStream = parser.getInputStream();
    }

    public void startDocument() throws SAXException {
        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Done reading the input.");
        Main.log("Found " + numNodes + " nodes");
        Main.log("Found " + numRelations + " relations");
        Main.log("Found " + numWays + " ways");
        Main.log("Found " + numAddresses + " addresses");
        Main.log("Found " + numSinglePointTags + " singePointTags");
        Main.log(nodeMap.size() + ", " + relations.size() + ", " + ways.size() + ", " + addresses.size() + ", " + singlePointTags.size());
        cleanUp();
    }

    private void cleanUp() {
        nodeMap = null;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch(qName) {
            case "osm":
                break;
            case "note":
                break;
            case "meta":
                break;
            case "bounds":
                newBounds(atts);
                break;
            case "node":
                newNode(atts);
                numNodes++;
                break;
            case "tag":
                newTag(atts);
                break;
            case "way":
                newWay(atts);
                numWays++;
                break;
            case "nd":
                newNodeRef(atts);
                break;
            case "relation":
                newRelation(atts);
                numRelations++;
                break;
            case "member":
                newWayRef(atts);
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    	Main.log(qName);
        switch(qName) {
            case "node":
                if(currentAddress != null) {
                    addresses.add(currentAddress);
                    currentAddress = null;
                }
                currentNode = null;
                break;
            case "way":
                if(currentAddress != null) {
                    addresses.add(currentAddress);
                    currentAddress = null;
                }
                currentWay.coordsToShape(currentCoords);
                ways.put(currentWay.getID(), currentWay);
                currentWay = null;
                currentCoords = new ArrayList<>();
                break;
            case "relation":
                currentRelation.waysToShape(currentMembers);
                relations.add(currentRelation);
                currentRelation = null;
                currentMembers = new ArrayList<>();
                break;
        }
    }

    int count = 0;
    
    private void newWayRef(Attributes atts) {
        long ref = Long.parseLong(atts.getValue("ref"));
        if(ways.containsKey(ref)) {        	
            currentMembers.add(ways.get(ref));
            ways.remove(ref);
        } else Main.log("This shouldn't happen");
    }

    private void newNodeRef(Attributes atts) {
        long ref = Long.parseLong(atts.getValue("ref"));
        NodeMap.Node node = nodeMap.get(ref);
        Main.log("count: " + ++count);
        currentCoords.add(node.getLon());
        currentCoords.add(node.getLat());
    }

    private void newWay(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        currentWay = new ParsedWay(id);
    }

    private void newRelation(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        currentRelation = new ParsedRelation(id);
    }

    public void newBounds(Attributes atts) {
        minLatBoundary = Float.parseFloat(atts.getValue("minlat"));
        minLonBoundary = Float.parseFloat(atts.getValue("minlon"));
        maxLatBoundary = Float.parseFloat(atts.getValue("maxlat"));
        maxLonBoundary = Float.parseFloat(atts.getValue("maxlon"));
        float avglat = minLatBoundary + (maxLatBoundary - minLatBoundary) / 2;
        lonFactor = (float) Math.cos(avglat / 180 * Math.PI);
        minLonBoundary *= lonFactor;
        maxLonBoundary *= lonFactor;
        minLatBoundary = -minLatBoundary;
        maxLatBoundary = -maxLatBoundary;
    }

    private void newNode(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        float lon = Float.parseFloat(atts.getValue("lon"));
        float lat = Float.parseFloat(atts.getValue("lat"));
        currentNode = nodeMap.putAndTake(id, lon * lonFactor, -lat);
    }

    private void newTag(Attributes atts) {
        String k = atts.getValue("k");
        String v = atts.getValue("v");
        if(k == null || v == null) return;
        switch(k) {
            case "addr:city":
                if(currentAddress == null) currentAddress = new ParsedAddress();
                currentAddress.setCity(v);
                if(currentNode != null) currentAddress.setPoint(currentNode.getLon(), currentNode.getLat());
                else currentAddress.setPoint(currentCoords.get(0), currentCoords.get(1));
                break;
            case "addr:housenumber":
            	if(currentAddress == null) currentAddress = new ParsedAddress();
                currentAddress.setHousenumber(v);
                break;
            case "addr:postcode":
            	if(currentAddress == null) currentAddress = new ParsedAddress();
                currentAddress.setPostcode(v);
                break;
            case "addr:street":
            	if(currentAddress == null) currentAddress = new ParsedAddress();
                currentAddress.setStreet(v);
                break;
            case "highway":
                currentType = v;
        }
    }

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		// TODO Auto-generated method stub
		
	}
}
