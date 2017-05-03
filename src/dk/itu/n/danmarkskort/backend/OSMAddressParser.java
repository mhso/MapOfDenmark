package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.Serializable;

public class OSMAddressParser extends SAXAdapter implements Serializable {
	private static final long serialVersionUID = 8120338349077111532L;

	private float minLatBoundary, minLonBoundary, maxLatBoundary, maxLonBoundary, lonFactor;

    private transient NodeMap nodeMap;
    private transient ParsedNode node;
    private transient ParsedAddress address;

    private transient WayType waytype;
    private transient String name;
    
    private transient boolean finished = false;
    private transient OSMReader reader;

    public OSMAddressParser(OSMReader reader) {
    	this.reader = reader;
    }
    
    public void startDocument() throws SAXException {
    	
        nodeMap = new NodeMap();

        finished = false;
        Main.log("OSMAddressParsing started.");

        resetValues();
    }

    public void endDocument() throws SAXException {
        Main.log("OSMAddressParsing finished.");

        temporaryClean();
        
        Main.addressController.onLWParsingFinished();
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
            case "tag":
                String k = atts.getValue("k").trim();
                String v = atts.getValue("v").trim();
                parseTagInformation(k, v);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
    	switch(qName) {
            case "node":
                addCurrent();
                break;
        }
    }

    private void addCurrent() {
        if(waytype != null) {
            if(node != null) {
            	for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(node);
            } 
        }

        if(address != null && Main.saveParsedAddresses) {
            if(node != null) address.setCoords(node);
            Main.addressController.addressParsed(address);
            for(OSMParserListener listener : reader.parserListeners) listener.onParsingGotItem(address);
        }
        resetValues();
    }

    private void resetValues() {
        address = null;
        node = null;

        waytype = null;
    }

    private void temporaryClean() {
        nodeMap = null;
    }

    private void finalClean() {
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
            }
        }
    }
}