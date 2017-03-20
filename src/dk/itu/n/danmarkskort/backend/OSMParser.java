package dk.itu.n.danmarkskort.backend;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.TimerUtil;

// This class can parse an OSM file, and turn it into tile files. 
public class OSMParser {
	
	public List<OSMParserListener> parserListeners = new ArrayList<OSMParserListener>();
	private String currentChecksum;
	
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
	
	public void addListener(OSMParserListener listener) {
		parserListeners.add(listener);
	}
	
	public void parseFile(String fileName) {
		TimerUtil parseTimer = new TimerUtil();
		parseTimer.on();
		if (fileName.endsWith(".osm")) {
			loadOSM(new InputSource(fileName), fileName);
		} else if (fileName.endsWith(".zip")) {
			try {
				ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(fileName)));
				loadOSM(new InputSource(zip), fileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		parseTimer.off();
		Main.log("Time spend parsing: "+parseTimer.toString());
		Main.logRamUsage();
	}
	
	private void loadOSM(InputSource source, String fileName) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new OSMNodeHandler(this, fileName));
			reader.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setChecksum(String checksum) {
		this.currentChecksum = checksum;
	}
	
	public String getChecksum() {
		return currentChecksum;
	}
	
}
