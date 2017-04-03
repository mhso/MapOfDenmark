package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.lightweight.models.*;
import dk.itu.n.danmarkskort.newmodels.ParsedAddress;
import dk.itu.n.danmarkskort.newmodels.ParsedItem;
import dk.itu.n.danmarkskort.newmodels.ParsedNode;
import dk.itu.n.danmarkskort.newmodels.ParsedRelation;
import dk.itu.n.danmarkskort.newmodels.ParsedWay;
import dk.itu.n.danmarkskort.newmodels.Region;
import dk.itu.n.danmarkskort.newmodels.WayType;
import dk.itu.n.danmarkskort.kdtree.*;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class LightWeightParser extends SAXAdapter {

    public float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    public NodeMap nodeMap;
    public HashMap<Long, ParsedWay> wayMap;
    public HashMap<Long, ParsedRelation> relationMap;
    public HashMap<Long, ParsedItem> temporaryWayReferences;
    public HashMap<Long, ParsedItem> temporaryRelationReferences;

    public EnumMap<WayType, ArrayList<ParsedItem>> enumMap;
    public EnumMap<WayType, KDTree> enumMapKD;

    private ParsedWay way;
    private ParsedRelation relation;
    private ParsedNode node;
    private ParsedAddress address;

    private ArrayList<ParsedNode> currentNodes;
    private ArrayList<ParsedItem> currentItems;

    private WayType waytype;
    private long id;
    private boolean finished = false;
    private long fileSize;
    private int byteCount;
    private OSMParser parser;
    private InputStream inputStream;
    private Locator locator;
    
    public LightWeightParser(OSMParser parser) {
    	this.parser = parser;
    }
    
    private void incrementLineCount() {
		if(locator.getLineNumber() % 1000 != 0) return;
		int currentCount = 0;
		try {
			currentCount = (int)((((double)fileSize-(double)inputStream.available())/(double)fileSize)*100);
		} catch (IOException e) {
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
    	fileSize = Util.getFileSize(new File(parser.getFileName()));
    	inputStream = parser.getInputStream();
        nodeMap = new NodeMap();
        wayMap = new HashMap<>();
        relationMap = new HashMap<>();
        temporaryWayReferences = new HashMap<>();
        temporaryRelationReferences = new HashMap<>();

        enumMap = new EnumMap<>(WayType.class);
        for(WayType waytype : WayType.values()) enumMap.put(waytype, new ArrayList<>());

        finished = false;
        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");
        Main.log("Max lon: " + maxLonBoundary + ", " + "Min lon: " + minLonBoundary);

        int count = 0;
        for(WayType wt : WayType.values()) count += enumMap.get(wt).size();
        Main.log("Ways and Relations saved: " + count);

        Main.log("Splitting data into KDTrees");

        enumMapKD = new EnumMap<>(WayType.class);
        for(WayType wt : WayType.values()) {
            ArrayList<ParsedItem> current = enumMap.get(wt);
            KDTree tree;
            if(current.isEmpty()) tree = null;
            else if(current.size() < DKConstants.KD_SIZE) tree = new KDTreeLeaf(current, null);
            else tree = new KDTreeNode(current);
            if(tree != null) tree.makeShapes();
            enumMap.remove(wt);
            enumMapKD.put(wt, tree);
        }

        for(OSMParserListener listener : parser.parserListeners) listener.onParsingFinished();
        AddressController.getInstance().onLWParsingFinished();
        finalClean();
        finished = true;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        incrementLineCount();
    	switch(qName) {
            case "bounds":
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
                Main.log("Updated bounds.");
                break;
            case "node":
                id = Long.parseLong(atts.getValue("id"));
                float lon = Float.parseFloat(atts.getValue("lon"));
                float lat = Float.parseFloat(atts.getValue("lat"));
                nodeMap.put(id, lon * lonFactor, -lat);
                node = nodeMap.get(id);
                break;
            case "way":
                way = new ParsedWay(Long.parseLong(atts.getValue("id")));
                break;
            case "relation":
                relation = new ParsedRelation(Long.parseLong(atts.getValue("id")));
                break;
            case "nd":
                currentNodes.add(nodeMap.get(Long.parseLong(atts.getValue("ref"))));
                break;
            case "member":
                long ref = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                switch(type) {
                    case "node":
                        if(nodeMap.get(ref) != null) currentNodes.add(nodeMap.get(ref));
                        break;
                    case "way":
                        if(temporaryWayReferences.containsKey(ref)) currentItems.add(temporaryWayReferences.get(ref));
                        break;
                    case "relation":
                        if(temporaryRelationReferences.containsKey(ref)) currentItems.add(temporaryRelationReferences.get(ref));
                        break;
                }
                break;
            case "tag":
                String k = atts.getValue("k");
                String v = atts.getValue("v");
                switch(k) {
                    case "addr:city":
                        if (address == null) address = new ParsedAddress();
                        address.setCity(v);
                        break;
                    case "addr:postcode":
                        if (address == null) address = new ParsedAddress();
                        address.setPostcode(v);
                        break;
                    case "addr:housenumber":
                        if (address == null) address = new ParsedAddress();
                        address.setHousenumber(v);
                        break;
                    case "addr:street":
                        if (address == null) address = new ParsedAddress();
                        address.setStreet(v);
                        break;
                }
                waytype = WayTypeUtil.tagToType(k, v, waytype);
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        incrementLineCount();
    	switch(qName) {
            case "relation":
                if(!currentItems.isEmpty()) relation.addItems(currentItems);
                if(!currentNodes.isEmpty()) relation.addNodes(currentNodes);
                addCurrent();
                break;
            case "way":
                if(!currentNodes.isEmpty()) way.addNodes(currentNodes);
                addCurrent();
                break;
            case "node":
                addCurrent();
                break;
        }
    }

    private void addCurrent() {
        if(waytype != null) {
            if(way != null) {
            	enumMap.get(waytype).add(way);
            	for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(way);
            }
            else if(relation != null) {
                enumMap.get(waytype).add(relation);
                for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(relation);
            }
            else if(node != null) {
            	for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(node);
            } 
        }

        if(way != null) {
        	temporaryWayReferences.put(way.getID(), way);
        	for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(way);
        }
        else if(relation != null) {
        	temporaryRelationReferences.put(relation.getID(), relation);
        	for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(relation);
        }

        if(address != null) {
            if(node != null) address.setCoords(node.getPoint());
            else if (way != null) address.setWay(way);
            else if (relation != null) address.setRelation(relation);
            AddressController.getInstance().addressParsed(address);
            for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(address);
        }
        cleanUp();
    }

    private void cleanUp() {
        way = null;
        relation = null;
        address = null;
        node = null;
        waytype = null;
        currentNodes = new ArrayList<>();
        currentItems = new ArrayList<>();
    }

    private void finalClean() {
        nodeMap = null;
        temporaryWayReferences = null;
        temporaryRelationReferences = null;
        System.gc();
    }
    
    public boolean isFinished() {
    	return finished;
    }
    
    public float getMinLon() {
    	return this.minLonBoundary;
    }
    
    public float getMaxLon() {
    	return this.maxLonBoundary;
    }
    
    public float getMinLat() {
    	return this.minLatBoundary;
    }
    
    public float getMaxLat() {
    	return this.maxLatBoundary;
    }
    
    public Region getMapRegion() {
    	float x1 = getMinLon();
    	float y1 = getMinLat();
    	float x2 = getMaxLon();
    	float y2 = getMaxLat();
    	Region reg = new Region(x1, y1, x2, y2);
    	return reg;
    }
}
