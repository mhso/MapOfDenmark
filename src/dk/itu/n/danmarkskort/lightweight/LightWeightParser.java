package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.lightweight.models.*;
import dk.itu.n.danmarkskort.lightweight.models.ParsedAddress;
import dk.itu.n.danmarkskort.lightweight.models.ParsedWay;
import dk.itu.n.danmarkskort.mapdata.KDTree;
import dk.itu.n.danmarkskort.mapdata.KDTreeLeaf;
import dk.itu.n.danmarkskort.mapdata.KDTreeNode;
import dk.itu.n.danmarkskort.models.WayType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

public class LightWeightParser extends SAXAdapter {

    private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private NodeMap nodeMap;
    private HashMap<Long, ParsedWay> wayMap;
    private HashMap<Long, ParsedRelation> relationMap;

    // bør efter endt OSM indlæsning omdanne alle ArrayLists til KD-træer
    public EnumMap<WayType, ArrayList<ParsedItem>> enumMap;
    public EnumMap<WayType, KDTree> enumMapKD;

    private ParsedWay way;
    private ParsedRelation relation;
    private NodeMap.Node node;
    private ParsedAddress address;

    private ArrayList<Float> currentNodes;
    private ArrayList<ParsedWay> currentWays;
    private ArrayList<ParsedRelation> currentRelations;

    private WayType waytype;
    private long id;

    public void startDocument() throws SAXException {
        nodeMap = new NodeMap();
        wayMap = new HashMap<>();
        relationMap = new HashMap<>();

        enumMap = new EnumMap<>(WayType.class);
        for(WayType waytype : WayType.values()) enumMap.put(waytype, new ArrayList<>());

        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");
        Main.log("Max lon: " + maxLonBoundary + ", " + "Min lon: " + minLonBoundary);

        int count = 0;
        for(WayType wt : WayType.values()) count += enumMap.get(wt).size();
        Main.log("Ways and Relations saved: " + count);

        Main.log("Converting WayType ArrayLists to KDTrees");

        enumMapKD = new EnumMap<>(WayType.class);
        for(WayType wt : WayType.values()) {
            Main.log(wt);
            ArrayList<ParsedItem> current = enumMap.get(wt);
            KDTree tree;
            if(current.isEmpty()) tree = null;
            else if(current.size() < 1000) tree = new KDTreeLeaf(current, null);
            else tree = new KDTreeNode(current);
            enumMap.remove(wt);
            enumMapKD.put(wt, tree);
        }

        AddressController.getInstance().onLWParsingFinished();
        finalClean();
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
                break;
            case "node":
                id = Long.parseLong(atts.getValue("id"));
                float lon = Float.parseFloat(atts.getValue("lon"));
                float lat = Float.parseFloat(atts.getValue("lat"));
                nodeMap.put(id, lon * lonFactor, -lat);
                node = nodeMap.get(Long.parseLong(atts.getValue("id")));
                break;
            case "way":
                way = new ParsedWay(Long.parseLong(atts.getValue("id")));
                // no longer needed? wayMap.put(way.getID(), way);
                break;
            case "relation":
                relation = new ParsedRelation(Long.parseLong(atts.getValue("id")));
                // no longer needed? relationMap.put(relation.getID(), relation);
                break;
            case "nd":
                NodeMap.Node node = nodeMap.get(Long.parseLong(atts.getValue("ref")));
                currentNodes.add(node.getLon());
                currentNodes.add(node.getLat());
                break;
            case "member":
                long ref = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                switch(type) {
                    case "node":
                        if(nodeMap.get(ref) != null) {
                            currentNodes.add(nodeMap.get(ref).getLon());
                            currentNodes.add(nodeMap.get(ref).getLat());
                        }
                        break;
                    case "way":
                        if(wayMap.containsKey(ref)) currentWays.add(wayMap.get(ref));
                        break;
                    case "relation":
                        if(relationMap.containsKey(ref)) currentRelations.add(relationMap.get(ref));
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
                waytype = WayTypeUtil.tagToType(k, v);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(qName) {
            case "relation":
                if(!currentRelations.isEmpty()) relation.addRelations(currentRelations);
                if(!currentWays.isEmpty()) relation.addWays(currentWays);
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
        if (waytype != null) {
            if(relation != null) enumMap.get(waytype).add(relation);
            if(way != null) enumMap.get(waytype).add(way);
            if(node != null) ;// do something eventually;
        }
        if(address != null) {
            if(node != null) address.setCoords(node.getPoint());
            else if (way != null) address.setWay(way);
            else if (relation != null) address.setRelation(relation);
            AddressController.getInstance().addressParsed(address);
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
        currentWays = new ArrayList<>();
        currentRelations = new ArrayList<>();
    }

    private void finalClean() {
        nodeMap = null;
        // ideally all objects have been passed on, and this object would delete all references to anything. A really big clean up!
    }
}
