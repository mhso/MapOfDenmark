package dk.frederik.parsing;

import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import dk.itu.n.danmarkskort.SAXAdapter;

public class OSMHandler extends SAXAdapter {

	HashMap<Long, float[]> nodeMap;
	HashMap<Long, ParsedWay> wayMap;
	HashMap<Long, ParsedRelation> relationMap;
	
	ParsedWay currentParsedWay;
	ArrayList<Float> currentCoords;
	
	public void startDocument() throws SAXException {
		NewMain.log("Start");
		nodeMap = new HashMap<>(18818991);
		wayMap = new HashMap<>();
		relationMap = new HashMap<>();
		currentCoords = new ArrayList<>();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		if(qName.equals("node")) {
			long id = Long.parseLong(atts.getValue("id"));
			nodeMap.put(id, new float[] {Float.parseFloat(atts.getValue("lon")), Float.parseFloat(atts.getValue("lat"))});
		} else if(qName.equals("way")) {
			long id = Long.parseLong(atts.getValue("id"));
			currentParsedWay = new ParsedWay(id);
			wayMap.put(id, currentParsedWay);
		} else if(qName.equals("nd")) {
			long id = Long.parseLong(atts.getValue("ref"));
			float[] coords = nodeMap.get(id);
			currentCoords.add(coords[0]);
			currentCoords.add(coords[1]);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("way")) {
			currentParsedWay.addCoords(currentCoords);
			currentParsedWay = null;
			currentCoords.clear();
		}
	}
	
	public void endDocument() throws SAXException {
		NewMain.log("Stop");
		NewMain.log("Node count: " + nodeMap.size());
		NewMain.log("Way count: " + wayMap.size());
	}
}
