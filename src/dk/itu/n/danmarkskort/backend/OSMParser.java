package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.*;
import dk.itu.n.danmarkskort.kdtree.*;

import dk.itu.n.danmarkskort.routeplanner.RouteController;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.awt.geom.Point2D;
import java.util.*;

public class OSMParser extends SAXAdapter {
    private NodeMap nodeMap;
    private HashMap<Long, ParsedWay> temporaryWayReferences;
    private HashMap<Long, ParsedRelation> temporaryRelationReferences;
    private HashMap<Point2D.Float, ParsedWay> coastlineMap;

    private EnumMap<WayType, ArrayList<ParsedItem>> enumMap;
    private EnumMap<WayType, ArrayList<ParsedPlace>> places;

    private ParsedWay way;
    private ParsedRelation relation;
    private ParsedNode node;
    private ParsedAddress address;
    private ParsedPlace place;

    private WayType waytype;
    private ArrayList<Point2D.Float> currentNodes;

    private String name;
    private boolean isHighway;
    private boolean isArea;
    private boolean bicycle;
    private boolean forward;
    private boolean backward;
    private boolean motorvehicle;
    private boolean walk;
    private boolean toGraph;
    private short maxSpeed;
    private RouteController route;

    private boolean finished = false;
    private OSMReader reader;

    public OSMParser(OSMReader reader) {
    	this.reader = reader;
    }
    
    public void startDocument() throws SAXException {
        nodeMap = new NodeMap();
        temporaryWayReferences = new HashMap<>();
        temporaryRelationReferences = new HashMap<>();
        places = new EnumMap<>(WayType.class);
        coastlineMap = new HashMap<>();
        currentNodes = new ArrayList<>();
        enumMap = new EnumMap<>(WayType.class);
        route = Main.routeController;

        for(WayType waytype : WayType.values()) {
        	if(waytype.toString().startsWith("PLACE_")) places.put(waytype, new ArrayList<>());
        	else enumMap.put(waytype, new ArrayList<>());
        }
        
        finished = false;
        Main.log("Parsing started.");

        resetValues();
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");

        int numItemsSaved = 0;
        for(WayType wt : WayType.values()) {
        	if(wt.toString().startsWith("PLACE_")) numItemsSaved += places.get(wt).size();
        	else numItemsSaved += enumMap.get(wt).size();
        }
       
        Main.log("Ways, Relations and Places saved: " + numItemsSaved);

        temporaryClean();
        Main.model.enumMapKD = new EnumMap<>(WayType.class);
        Main.model.enumMapPlacesKD = new EnumMap<>(WayType.class);
        Main.log("Splitting data into KDTrees");

        for(WayType wt : WayType.values()) {
            KDTree<ParsedItem> tree = null;
            if(wt == WayType.COASTLINE) {
                tree = getCoastlines();
                coastlineMap = null;
            }
            else if(wt.toString().startsWith("PLACE_")) {
            	if(!places.isEmpty()) {
                	KDTree<ParsedPlace> placeTree;
                	ArrayList<ParsedPlace> placeList = places.get(wt);
                	if(placeList.isEmpty()) placeTree = null;
                	else placeTree = new KDTreeNode<>(placeList);
                	
                	Main.model.enumMapPlacesKD.put(wt, placeTree);
                }
            }
            else {
                ArrayList<ParsedItem> current = enumMap.get(wt);
                if(current.isEmpty()) tree = null;
                else tree = new KDTreeNode<>(current);
            }
            
            enumMap.remove(wt);
            if(tree != null) Main.model.enumMapKD.put(wt, tree);
        }

        Main.log("Deleting nodes, adding float[] coords");
        for(Map.Entry<WayType, KDTree<ParsedItem>> entry : Main.model.enumMapKD.entrySet()) {
            KDTree<ParsedItem> current = entry.getValue();
            if(current != null) for(ParsedItem item : current) item.nodesToCoords();
        }

        Main.log("Deleting old references");
        for(Map.Entry<WayType, KDTree<ParsedItem>> entry : Main.model.enumMapKD.entrySet()) {
            KDTree<ParsedItem> current = entry.getValue();
            if(current != null) for (ParsedItem item : current) item.deleteOldRefs();
        }
        
        Main.log("Creating route graph");
        route.makeGraph();
        
        for(OSMParserListener listener : reader.parserListeners) listener.onParsingFinished();
        Main.addressController.onLWParsingFinished();
        finalClean();
        finished = true;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    	switch(qName) {
            case "bounds":
            	float minLatBoundary = Float.parseFloat(atts.getValue("minlat"));
            	float minLonBoundary = Float.parseFloat(atts.getValue("minlon"));
            	float maxLatBoundary = Float.parseFloat(atts.getValue("maxlat"));
                float maxLonBoundary = Float.parseFloat(atts.getValue("maxlon"));
                float avglat = minLatBoundary + (maxLatBoundary - minLatBoundary) / 2;
                float lonFactor = (float) Math.cos(avglat / 180 * Math.PI);
                minLonBoundary *= lonFactor;
                maxLonBoundary *= lonFactor;
                minLatBoundary = -minLatBoundary;
                maxLatBoundary = -maxLatBoundary;
                Main.model.setLonFactor(lonFactor);
                Main.model.setMinLat(minLatBoundary);
            	Main.model.setMinLon(minLonBoundary);
            	Main.model.setMaxLat(maxLatBoundary);
                Main.model.setMaxLon(maxLonBoundary);
                Main.log("Updated bounds.");
                break;
            case "node":
                long id = Long.parseLong(atts.getValue("id"));
                float lon = Float.parseFloat(atts.getValue("lon"));
                float lat = Float.parseFloat(atts.getValue("lat"));
                nodeMap.put(id, lon * Main.model.getLonFactor(), -lat);
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
                currentNodes.add(nodeMap.get(Long.parseLong(atts.getValue("ref"))));
                break;
            case "member":
                long ref = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                String role = atts.getValue("role");
                switch(type) {
                    case "node":
                        if(nodeMap.get(ref) != null) currentNodes.add(nodeMap.get(ref));
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
                String k = atts.getValue("k").trim();
                String v = atts.getValue("v").trim();
                parseTagInformation(k, v);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    	switch(qName) {
            case "relation":
            case "way":
            case "node":
                if(isHighway) addToGraph();
                addCurrent();
                break;
        }
    }

    private void addToGraph() {
        checkMaxSpeed();
        if(!toGraph) return;

        if(maxSpeed == 0) Main.log(waytype);

        for(int i = 0; i < currentNodes.size() - 1; i++) {
            Point2D.Float firstNode = currentNodes.get(i);
            Point2D.Float secondNode = currentNodes.get(i + 1);
            if(firstNode != null && secondNode != null) {
                if (maxSpeed > 0)
                    route.addEdge(firstNode, secondNode, maxSpeed, forward, backward, motorvehicle, bicycle, walk, name);
            }
        }
    }

    private void addCurrent() {
        if(way != null) way.addNodes(currentNodes);
        else if(relation != null && currentNodes.size() > 0) relation.addNodes(currentNodes);

        if(waytype != null) {
            if(way != null) {
                if(waytype == WayType.COASTLINE) ParserUtil.connectCoastline(coastlineMap, way);
                else enumMap.get(waytype).add(way);
                for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(way);
            }
            else if(relation != null) {
                enumMap.get(waytype).add(relation);
                relation.correctOuters();
                for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(relation);
            }
            else if(node != null) for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(node);
        }

        if(address != null && Main.saveParsedAddresses) {
            if(node != null) address.setCoords(node);
            Main.addressController.addressParsed(address);
            for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(address);
        }
        if(place != null) {
        	places.get(waytype).add(place);
        }
        resetValues();
    }

    private KDTree<ParsedItem> getCoastlines() {
        KDTree<ParsedItem> tree;
        HashSet<ParsedWay> connected = new HashSet<>();
        HashSet<ParsedWay> unconnected = new HashSet<>();
        ArrayList<ParsedItem> combined = new ArrayList<>();
        if(coastlineMap.size() > 0) {
            coastlineMap.forEach((firstNode, item) -> {
                if(item.getFirstNode() == item.getLastNode()) {
                    connected.add(item);
                }
                else if(item.getFirstNode() != item.getLastNode()) {
                    if(!connected.contains(item)) {
                        unconnected.add(item);
                    }
                }
            });
            combined.addAll(connected);
            HashSet<ParsedWay> fixed = ParserUtil.fixUnconnectedCoastlines(unconnected);
            combined.addAll(fixed);
            tree = new KDTreeNode<>(combined);
        }
        else tree = null;
        coastlineMap = null;
        return tree;
    }

    private void resetValues() {
        way = null;
        relation = null;
        address = null;
        node = null;
        place = null;

        waytype = null;
        name = null;
        maxSpeed = 0;

        forward = true;
        backward = true;
        bicycle = true;
        motorvehicle = true;
        isArea = false;
        isHighway = false;
        toGraph = false;
        walk = true;
        currentNodes = new ArrayList<>();
    }

    private void temporaryClean() {
        nodeMap.killNextReferences();
        nodeMap = null;
        temporaryWayReferences = null;
        temporaryRelationReferences = null;
    }

    private void finalClean() {
        System.gc();
    }
    
    public boolean isFinished() {
    	return finished;
    }

    public void parseTagInformation(String k, String v) {
        if(waytype == WayType.COASTLINE) return;
        if(node != null) {
            switch(k) {
                case "addr:city":
                    if (address == null) address = new ParsedAddress();
                    address.setCity(v);
                    return;
                case "addr:postcode":
                    if (address == null) address = new ParsedAddress();
                    address.setPostcode(v);
                    return;
                case "addr:housenumber":
                    if (address == null) address = new ParsedAddress();
                    address.setHousenumber(v);
                    return;
                case "addr:street":
                    if (address == null) address = new ParsedAddress();
                    address.setStreet(v);
                    return;
                case "name":
                	name = v;
                	break;
                case "place":
                	switch(v) {
                		case "city":
                			place = new ParsedPlace(name, node.x, node.y);
                			waytype = WayType.PLACE_CITY;
                			return;
                		case "town":
                			place = new ParsedPlace(name, node.x, node.y);
                			waytype = WayType.PLACE_TOWN;
                			return;
                		case "suburb":
                			place = new ParsedPlace(name, node.x, node.y);
                			waytype = WayType.PLACE_SUBURB;
                			return;
                		case "neighbourhood":
                			place = new ParsedPlace(name, node.x, node.y);
                			waytype = WayType.PLACE_NEIGHBOURHOOD;
                			return;
                        case "square":
                            isArea = true;
                            break;
                	}
            }
        }
        if(place != null) {
        	switch(k) {
	        	case "population":
	            	place.setPopulation(Integer.parseInt(v));
	            	break;
        	}
        }
        switch (k) {
            case "highway":
                if(!v.equals("pier")) {
                    isHighway = true;
                    toGraph = true;
                }
                switch(v) {
                    case "motorway_link":
                    case "motorway":
                        waytype = WayType.HIGHWAY_MOTORWAY;
                        break;
                    case "trunk_link":
                    case "trunk":
                        waytype = WayType.HIGHWAY_TRUNK;
                        break;
                    case "primary_link":
                    case "primary":
                        waytype = WayType.HIGHWAY_PRIMARY;
                        break;
                    case "secondary_link":
                    case "secondary":
                        waytype = WayType.HIGHWAY_SECONDARY;
                        break;
                    case "tertiary_link":
                    case "tertiary":
                        waytype = WayType.HIGHWAY_TERTIARY;
                        break;
                    case "residential":
                        waytype = WayType.HIGHWAY_RESIDENTIAL;
                        break;
                    case "pedestrian":
                        if(isArea) waytype = WayType.PEDESTRIAN_AREA;
                        else waytype = WayType.HIGHWAY_PEDESTRIAN;
                        break;
                    case "unclassified":
                        waytype = WayType.HIGHWAY_UNCLASSIFIED;
                        break;
                    case "service":
                        waytype = WayType.HIGHWAY_SERVICE;
                        break;
                    case "cycleway":
                        waytype = WayType.HIGHWAY_CYCLEWAY;
                        break;
                    case "traffic_signal":
                        break;
                    case "path":
                    case "footway":
                    case "bridleway":
                    case "track":
                    case "steps":
                        waytype = WayType.HIGHWAY_FOOTWAY;
                        break;
                    default:
                        waytype = WayType.HIGHWAY_UNDEFINED;
                        break;
                }
                break;
            case "building":
                switch (v) {
                    case "school":
                        waytype = WayType.BUILDING_SCHOOL;
                        break;
                    case "train_station":
                        waytype = WayType.TRAIN_STATION;
                        break;
                    case "roof":
                        waytype = WayType.ROOF;
                        break;
                    default:
                        waytype = WayType.BUILDING;
                        break;
                }
                break;
            case "landuse":
                switch(v) {
                    case "residential":
                        waytype = WayType.RESIDENTIAL;
                        break;
                    case "farmland":
                        waytype = WayType.FARMLAND;
                        break;
                    case "forest":
                        waytype = WayType.FOREST;
                        break;
                    case "meadow":
                    	waytype = WayType.MEADOW;
                    	break;
                    case "industrial":
                        waytype = WayType.INDUSTRIAL;
                        break;
                    case "recreation_ground":
                    case "grass":
                        waytype = WayType.GRASS;
                        break;
                    case "retail":
                        waytype = WayType.RETAIL;
                        break;
                    case "military":
                        waytype = WayType.MILITARY;
                        break;
                    case "cemetery":
                        waytype = WayType.CEMETERY;
                        break;
                    case "orchard":
                        waytype = WayType.ORCHARD;
                        break;
                    case "allotments":
                        waytype = WayType.ALLOTMENTS;
                        break;
                    case "construction":
                        waytype = WayType.CONSTRUCTION;
                        break;
                    case "railway":
                        waytype = WayType.RAILWAY;
                        break;
                }
                break;
            case "natural":
                switch(v) {
                    case "water":
                        waytype = WayType.WATER;
                        break;
                    case "coastline":
                        waytype = WayType.COASTLINE;
                        break;
                    case "scrub":
                        waytype = WayType.SCRUB;
                        break;
                    case "wood":
                        waytype = WayType.WOOD;
                        break;
                    case "wetland":
                        waytype = WayType.WETLAND;
                        break;
                    case "sand":
                        waytype = WayType.SAND;
                        break;
                }
                break;
            case "railway":
                switch(v) {
                	case "rail":
                		waytype = WayType.RAIL;
                		break;
                    case "light_rail":
                        waytype = WayType.LIGHT_RAIL;
                        break;
                    case "platform":
                        waytype = WayType.PLATFORM;
                        break;
                }
                break;
            case "man_made":
                switch(v) {
                    case "pier":
                        waytype = WayType.PIER;
                        break;
                    case "breakwater":
                    case "bridge":
                        waytype = WayType.BRIDGE;
                        break;
                    case "embankment":
                        waytype = WayType.EMBANKMENT;
                        break;
                }
                break;
            case "waterway":
                switch(v) {
                    case "stream":
                        waytype =  WayType.WATER_STREAM;
                        break;
                    case "river":
                        waytype =  WayType.WATER_RIVER;
                        break;
                }
                break;
            case "leisure":
                switch(v) {
                    case "stadium":
                        waytype = WayType.STADIUM;
                        break;
                    case "common":
                        waytype = WayType.MEADOW;
                        break;
                    case "garden":
                    case "park":
                        waytype = WayType.PARK;
                        break;
                    case "playground":
                        waytype = WayType.PLAYGROUND;
                        break;
                    case "track":
                    case "pitch":
                    case "golf_course":
                        waytype = WayType.SPORT;
                        break;
                }
                break;
            case "amenity":
                switch(v) {
                    case "parking":
                        waytype = WayType.PARKING;
                        break;
                }
                break;
            case "oneway":
                switch(v) {
                    case "yes":
                        backward = false;
                        break;
                    case "-1":
                        forward = false;
                        break;
                }
                break;
            case "name":
                name = v;
                if(way != null) way.setName(v);
                break;
            case "maxspeed":
                try{
                    maxSpeed = Short.parseShort(v);
                }
                catch (NumberFormatException e) {
                    maxSpeed = 0;
                }
                break;
            case "junction":
            	if(v.equals("roundabout")) backward = false;
            	break;
            case "bicycle":
                switch(v) {
                    case "no":
                        bicycle = false;
                        break;
                }
                break;
            case "area":
                isArea = true;
                break;
            case "foot":
                switch (v) {
                    case "yes":
                        walk = true;
                        break;
                    case "no":
                        walk = false;
                        break;
                }
                break;
            case "sidewalk":
                switch(v) {
                    case "none":
                        walk = false;
                        break;
                    case "both":
                    case "right":
                    case "left":
                        walk = true;
                        break;
                }
                break;
        }
    }

    private void checkMaxSpeed() {
        switch(waytype) {
            case HIGHWAY_SERVICE:
                toGraph = false;
                break;
            case HIGHWAY_FOOTWAY:
                bicycle = false;
            case PEDESTRIAN_AREA:
            case HIGHWAY_PEDESTRIAN:
                walk = true;
                if(maxSpeed == 0) maxSpeed = 6;
                motorvehicle = false;
                break;
            case HIGHWAY_CYCLEWAY:
                motorvehicle = false;
                if(maxSpeed == 0) maxSpeed = 30; // is not relevant, as we never use this when calculating bike routes
                break;
            case HIGHWAY_MOTORWAY:
                if(maxSpeed == 0) maxSpeed = 130;
                bicycle = false;
                walk = false;
                break;
            case HIGHWAY_TRUNK:
            case HIGHWAY_PRIMARY:
                bicycle = false;
                walk = false;
                if(maxSpeed == 0) maxSpeed = 90;
                break;
            case HIGHWAY_SECONDARY:
                if(maxSpeed == 0) maxSpeed = 80;
                break;
            case HIGHWAY_TERTIARY:
            case HIGHWAY_RESIDENTIAL:
            case HIGHWAY_UNCLASSIFIED:
            case HIGHWAY_UNDEFINED:
                if(maxSpeed == 0) maxSpeed = 50;
                break;
            default:
                toGraph = false;
        }
    }
}