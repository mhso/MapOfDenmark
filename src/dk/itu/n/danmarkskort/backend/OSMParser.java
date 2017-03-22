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
import dk.itu.n.danmarkskort.MemoryUtil;
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
		MemoryUtil parseMemory = new MemoryUtil();
		parseTimer.on();
		parseMemory.on();
		if (fileName.endsWith(".osm")) {
			try {
				FileInputStream fis2 = new FileInputStream(fileName);
				loadOSM(new InputSource(fis2), fileName, fis2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (fileName.endsWith(".zip")) {
			try {
				FileInputStream fis = new FileInputStream(fileName);
				ZipInputStream zip = new ZipInputStream(new BufferedInputStream(fis));
				ZipEntry bdfd = zip.getNextEntry();
				System.out.println("fis.available: "+(fis.available()/Math.pow(1024, 2))+" MB");
				loadOSM(new InputSource(zip), fileName, fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		parseTimer.off();
		parseMemory.off();
		Main.log(parseMemory.humanReadableByteCount(true));
		Main.log("Time spend parsing: "+parseTimer.toString());
		Main.logRamUsage();
	}
	
	private void loadOSM(InputSource source, String fileName, FileInputStream fis) {
		
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new OSMNodeHandler(this, fileName, fis));  // Handles the actual XML with the OSMNodeHandler
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
