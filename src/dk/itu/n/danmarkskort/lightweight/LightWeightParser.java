package dk.itu.n.danmarkskort.lightweight;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.MemoryUtil;
import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.lightweight.models.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;

public class LightWeightParser extends SAXAdapter {

    private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private NodeMap nodeMap;
    private HashMap<Long, ParsedWay> wayMap;
    private ArrayList<ParsedRelation> relationMap;

    private NodeMap.Node currentParsedNode;
    private ParsedWay currentParsedWay;
    private ParsedRelation currentParsedRelation;
    private ArrayList<ParsedWay> currentMembers = new ArrayList<>();
    private ArrayList<Float> currentCoords = new ArrayList<>();

    private MemoryUtil memUtil;

    public void startDocument() throws SAXException {
        nodeMap = new NodeMap();
        wayMap = new HashMap<>();
        relationMap = new ArrayList<>();
        Main.log("Parsing started.");
    }

    public void endDocument() throws SAXException {
        Main.log("Parsing finished.");
        Main.log("Nodes: " + nodeMap.size() + ", Ways: " + wayMap.size() + ", Relations: " + relationMap.size());
        cleanUp();
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        switch(qName) {
            case "node":
                newNode(atts);
                break;
            case "tag":
                newTag(atts);
                break;
            case "nd":
                newNodeRef(atts);
                break;
            case "way":
                newWay(atts);
                break;
            case "member":
                newWayRef(atts);
                break;
            case "relation":
                newRelation(atts);
                break;
            case "bounds":
                newBounds(atts);
                break;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch(qName) {
            case "node":
                break;
            case "way":
                currentParsedWay.nodesToCoords(currentCoords);
                wayMap.put(currentParsedWay.getID(), currentParsedWay);
                break;
            case "relation":
                currentParsedRelation.addWays(currentMembers);
                relationMap.add(currentParsedRelation);
                break;
        }
    }

    private void newNode(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        float lon = Float.parseFloat(atts.getValue("lon"));
        float lat = Float.parseFloat(atts.getValue("lat"));
        currentParsedNode = nodeMap.putAndTake(id, lon * lonFactor, -lat);
    }

    private void newNodeRef(Attributes atts) {
        long ref = Long.parseLong(atts.getValue("ref"));
        NodeMap.Node node = nodeMap.get(ref);
        currentCoords.add(node.getLon());
        currentCoords.add(node.getLat());
    }

    private void newWay(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        currentParsedWay = new ParsedWay(id);
        currentCoords = new ArrayList<>();
    }

    private void newWayRef(Attributes atts) {
        long ref = Long.parseLong(atts.getValue("ref"));
        if(wayMap.containsKey(ref)) currentMembers.add(wayMap.get(ref));
    }

    private void newRelation(Attributes atts) {
        long id = Long.parseLong(atts.getValue("id"));
        currentParsedRelation = new ParsedRelation(id);
        currentMembers = new ArrayList<>();
    }

    private void newTag(Attributes atts) {
        // nothing yet
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

    private void cleanUp() {
        nodeMap = null;
    }
}
