package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.lightweight.models.*;
import dk.itu.n.danmarkskort.lightweight.models.ParsedAddress;
import dk.itu.n.danmarkskort.lightweight.models.ParsedWay;
import dk.itu.n.danmarkskort.models.WayType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;

public class LightWeightParser extends SAXAdapter {

    private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private NodeMap nodeMap;
    private HashMap<Long, ParsedWay> wayMap;
    private HashMap<Long, ParsedRelation> relationMap;

    private ParsedWay way;
    private ParsedRelation relation;
    private ArrayList<Float> coords;
    private NodeMap.Node node;
    private ParsedAddress address;
    private long id;

    private ArrayList<ParsedRelation> memberRelations;
    private ArrayList<NodeMap.Node> memberNodes;
    private ArrayList<ParsedWay> memberWays;


    private WayType waytype;

    public void startDocument() throws SAXException {
        nodeMap = new NodeMap();
        wayMap = new HashMap<>();
        relationMap = new HashMap<>();
        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");
        Main.log("Nodes: " + nodeMap.size() + ", Ways: " + wayMap.size() + ", Relations: " + relationMap.size());
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
                node = nodeMap.get(Long.parseLong(atts.getValue("id"));
                break;
            case "way":
                way = new ParsedWay(Long.parseLong(atts.getValue("id")));
                coords = new ArrayList<>();
                break;
            case "relation":
                relation = new ParsedRelation(Long.parseLong(atts.getValue("id")));
                memberWays = new ArrayList<>();
                memberNodes = new ArrayList<>();
                memberRelations = new ArrayList<>();
                break;
            case "nd":
                NodeMap.Node node = nodeMap.get(Long.parseLong(atts.getValue("ref")));
                coords.add(node.getLon());
                coords.add(node.getLat());
                break;
            case "member":
                id = Long.parseLong(atts.getValue("ref"));
                String type = atts.getValue("type");
                switch(type) {
                    case "node":
                        memberNodes.add(nodeMap.get(id));
                        break;
                    case "way":
                        memberWays.add(wayMap.get(id));
                        break;
                    case "relation":
                        memberRelations.add(relationMap.get(id));
                        break;
                }
                break;
            case "tag":
                waytype = null;
                String k = atts.getValue("k");
                String v = atts.getValue("v");
                switch (k) {
                    case "highway":
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
                                waytype = WayType.HIGHWAY_RESIDENTAL;
                                break;
                            case "unclassified":
                                waytype = WayType.HIGHWAY_ROAD;
                                break;
                            case "service":
                                waytype = WayType.HIGHWAY_SERVICE;
                                break;
                        }
                    case "addr:city":
                        if(address == null) address = new ParsedAddress();
                        address.setCity(v);
                        break;
                    case "addr:postcode":
                        if(address == null) address = new ParsedAddress();
                        address.setPostcode(v);
                        break;
                    case "addr:housenumber":
                        if(address == null) address = new ParsedAddress();
                        address.setHousenumber(v);
                        break;
                    case "addr:street":
                        if(address == null) address = new ParsedAddress();
                        address.setStreet(v);
                        break;
                }
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(qName) {
            case "node":
            case "way":
            case "relation":
                addCurrent();
                break;
        }
    }

    private void addCurrent() {
        if(way != null) {
            way.setCoords(coords);
            wayMap.put(way.getID(), way);
        }
        if(relation != null) {
            relation.setWays(memberWays);
            relationMap.put(relation.getID(), relation);
        }
        if(address != null) {
            if (node != null) address.setCoords(node);
            else if (way != null) address.setCoords(way.getCoords());
            else if (relation != null) {
                //Main.log(address.getCity() + " " + address.getStreet() + " "+ address.getHousenumber());
                if(!memberWays.isEmpty()) address.setCoords(memberWays.get(0).getCoords());
                else if(!memberNodes.isEmpty()) address.setCoords(memberNodes.get(0).getPoint());
                else if(!memberRelations.isEmpty())  address.setCoords(memberRelations.get(0).getWays()[0].getCoords());
                else { Main.log("I hope this doesnt happen");}
        }
            AddressController.getInstance().addressParsed(address);
        }
        cleanUp();
    }

    private void cleanUp() {
        coords = null;
        way = null;
        relation = null;
        memberNodes = null;
        memberWays = null;
        memberRelations = null;
        address = null;
        node = null;
        type = null;
    }

    private void finalClean() {
        nodeMap = null;
        // ideally all objects have been passed on, and this object would delete all references to anything. A really big clean up!
    }
}
