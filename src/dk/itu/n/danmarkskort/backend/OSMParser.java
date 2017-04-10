package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.models.*;
import dk.itu.n.danmarkskort.kdtree.*;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

public class OSMParser extends SAXAdapter implements Serializable {
	private static final long serialVersionUID = 8120338349077111532L;

	private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private transient NodeMap nodeMap;
    private transient HashMap<Long, ParsedWay> temporaryWayReferences;
    private transient HashMap<Long, ParsedRelation> temporaryRelationReferences;
    private transient IdentityHashMap<ParsedNode, ParsedWay> coastlineMap;

    private transient EnumMap<WayType, ArrayList<ParsedItem>> enumMap;
    public EnumMap<WayType, KDTree> enumMapKD;

    private transient ParsedWay way;
    private transient ParsedRelation relation;
    private transient ParsedNode node;
    private transient ParsedAddress address;

    private transient WayType waytype;
    private transient String name;
    private transient Integer maxSpeed;
    private transient boolean oneWay;

    private transient boolean finished = false;
    private transient OSMReader parser;
    
    public OSMParser(OSMReader parser) {
    	this.parser = parser;
    }
    
    public void startDocument() throws SAXException {
        nodeMap = new NodeMap();
        temporaryWayReferences = new HashMap<>();
        temporaryRelationReferences = new HashMap<>();
        coastlineMap = new IdentityHashMap<>();
        enumMap = new EnumMap<>(WayType.class);
        for(WayType waytype : WayType.values()) enumMap.put(waytype, new ArrayList<>());

        finished = false;
        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");

        int numItemsSaved = 0;
        for(WayType wt : WayType.values()) numItemsSaved += enumMap.get(wt).size();
        Main.log("Ways and Relations saved: " + numItemsSaved);

        Main.log("Splitting data into KDTrees");

        temporaryClean();
        enumMapKD = new EnumMap<>(WayType.class);

        for(OSMParserListener listener : parser.parserListeners) listener.onParsingFinished();
        
        for(WayType wt : WayType.values()) {
            KDTree tree;
            if(wt == WayType.COASTLINE) tree = getCoastlines();
            else {
                ArrayList<ParsedItem> current = enumMap.get(wt);
                if (current.isEmpty()) tree = null;
                else if (current.size() < DKConstants.KD_SIZE) tree = new KDTreeLeaf(current);
                else tree = new KDTreeNode(current);
            }
            enumMap.remove(wt);
            if(tree != null) tree.makeShapes();
            enumMapKD.put(wt, tree);
        }

        for(Map.Entry<WayType, KDTree> entry : enumMapKD.entrySet()) {
            KDTree current = entry.getValue();
            if(current != null) current.deleteOldRefs();
        }

        for(OSMParserListener listener : parser.parserListeners) listener.onParsingFinished();
        AddressController.getInstance().onLWParsingFinished();
        finalClean();
        finished = true;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
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
                long id = Long.parseLong(atts.getValue("id"));
                float lon = Float.parseFloat(atts.getValue("lon"));
                float lat = Float.parseFloat(atts.getValue("lat"));
                nodeMap.put(id, lon * lonFactor, -lat);
                node = nodeMap.get(id);
                break;
            case "way":
                way = new ParsedWay(Long.parseLong(atts.getValue("id")));
                temporaryWayReferences.put(way.getID(), way);
                break;
            case "relation":
                relation = new ParsedRelation(Long.parseLong(atts.getValue("id")));
                temporaryRelationReferences.put(relation.getID(), relation);
                break;
            case "nd":
                way.addNode(nodeMap.get(Long.parseLong(atts.getValue("ref"))));
                break;
            case "member":
                long ref = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                String role = atts.getValue("role");
                switch(type) {
                    case "node":
                        if(nodeMap.get(ref) != null) relation.addNode(nodeMap.get(ref));
                        break;
                    case "way":
                        if(temporaryWayReferences.containsKey(ref)) relation.addMember(temporaryWayReferences.get(ref), role);
                        break;
                    case "relation":
                        if(temporaryRelationReferences.containsKey(ref)) relation.addMember(temporaryRelationReferences.get(ref), role);
                        break;
                }
                break;
            case "tag":
                String k = atts.getValue("k");
                String v = atts.getValue("v").trim();
                if(node != null) {
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
                    break;
                }
                else {
                    waytype = WayTypeUtil.tagToType(k, v, waytype);
                    switch (k) {
                        case "name":
                            name = v;
                            break;
                        case "maxspeed":
                            try{
                                maxSpeed = Integer.parseInt(v);
                            }
                            catch (NumberFormatException e) {
                                // do nothing
                            }
                            break;
                        case "oneway":
                            if(v.equals("yes")) oneWay = true;
                            break;
                    }
                }
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    	switch(qName) {
            case "relation":
            case "way":
            case "node":
                addCurrent();
                break;
        }
    }

    private void addCurrent() {
        if(waytype != null) {
            if(waytype == WayType.COASTLINE) {
                if(way != null) CoastlineUtil.connectCoastline(coastlineMap, way);
                if(relation != null) {
                    Main.log("does this really happen????");
                    Main.log(relation.getID());
                    CoastlineUtil.connectCoastline(coastlineMap, relation);
                }
            }
            else if(way != null) {
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

        if(address != null) {
            if(node != null) address.setCoords(node.getPoint());
            else if (way != null) address.setWay(way);
            else if (relation != null) address.setRelation(relation);
            //AddressController.getInstance().addressParsed(address);
            for(OSMParserListener listener : parser.parserListeners) listener.onParsingGotItem(address);
        }
        cleanUp();
    }

    private KDTree getCoastlines() {
        KDTree tree = null;
        HashSet<ParsedWay> connected = new HashSet<>();
        HashSet<ParsedWay> unconnected = new HashSet<>();
        ArrayList<ParsedItem> combined = new ArrayList<>();

        // I'm not entirely sure, but I think that coastlineMap (and thus also connected)
        // contains 2 of each way
        if(coastlineMap.size() > 0) {
            coastlineMap.forEach((firstNode, item) -> {
                //if(item.getLastNode() != item.getFirstNode()) item.addNode(item.getFirstNode());
                //combined.add(item);
                if(item.getFirstNode() == item.getLastNode()) connected.add(item);
                else if(item.getFirstNode() != item.getLastNode()) unconnected.add(item);
                else Main.log("what is happening!!!");
            });

            HashSet<ParsedWay> fixed = CoastlineUtil.fixUnconnectedCoastlines(unconnected);
            combined.addAll(connected);

            combined.addAll(fixed);
            tree = new KDTreeLeaf(combined);
        }
        else tree = null;

        return tree;
    }

    private void cleanUp() {
        way = null;
        relation = null;
        address = null;
        node = null;

        waytype = null;
        name = null;
        oneWay = false;
        maxSpeed = null;
    }

    private void temporaryClean() {
        temporaryWayReferences = null;
        temporaryRelationReferences = null;
    }

    private void finalClean() {
        nodeMap = null;
        coastlineMap = null;
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
    	return new Region(x1, y1, x2, y2);
    }

    public float getLonFactor() {
        return lonFactor;
    }
}