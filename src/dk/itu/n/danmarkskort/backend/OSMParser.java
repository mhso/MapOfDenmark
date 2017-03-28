package dk.itu.n.danmarkskort.backend;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.lightweight.LightWeightParser;

// This class can parse an OSM file, and turn it into tile files. 
public class OSMParser {
	
	public List<OSMParserListener> parserListeners = new ArrayList<OSMParserListener>();
	private String currentChecksum;
	private InputStream inputStream;
	
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
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public void parseFile(String fileName) {
		TimerUtil parseTimer = new TimerUtil();
		MemoryUtil parseMemory = new MemoryUtil();
		parseTimer.on();
		parseMemory.on();
		
		if (fileName.endsWith(".osm")) {
			try {
				inputStream = new FileInputStream(fileName);
				loadOSM(new InputSource(inputStream), fileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		} else if (fileName.endsWith(".zip")) {
			try {
				inputStream = new FileInputStream(fileName);
				ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputStream));
				zip.getNextEntry();
				loadOSM(new InputSource(zip), fileName);
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
	
	private void loadOSM(InputSource source, String fileName) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			if(Main.lightweight) {
				reader.setContentHandler(Main.model);
				reader.parse(source);
			}
			else {
				reader.setContentHandler(new OSMNodeHandler(this, fileName));
				reader.parse(source);
			}
		} catch (SAXException | IOException e) {
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
