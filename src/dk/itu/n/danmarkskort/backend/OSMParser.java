package dk.itu.n.danmarkskort.backend;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.Main;

// This class can parse an OSM file, and turn it into tile files. 
public class OSMParser {

	public OSMParser() {
		initialize();
	}
	
	public OSMParser(String fileName) {
		initialize();
		parseFile(fileName);
	}
	
	public void initialize() {
		Main.log("Initialzed OSM Parser");
	}
	
	public void parseFile(String fileName) {
		
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new OSMNodeHandler(reader, fileName)); // Handles the actual XML with the OSMNodeHandler
			reader.parse(new InputSource(fileName));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Main.logRamUsage();
	}
	
}
