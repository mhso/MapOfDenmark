package dk.itu.n.danmarkskort.backend;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.MemoryUtil;
import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.Util;

/**
 * This class recieves a map file (osm, zip or bin) and either loads from binary, from a Zip Inputstream or
 * from an Open Street Map XML file. This class also manages Inputstream Listeners, that send events to anyone
 * listening on the parsing.
 * 
 * @author Team N @ ITU
 */
public class OSMReader {
	
	public List<OSMParserListener> parserListeners = new ArrayList<OSMParserListener>();
	private List<InputStreamListener> inputListeners = new ArrayList<InputStreamListener>();
	private String currentChecksum;
	private InputStream inputStream;
	private String fileName;
	
	public OSMReader() {
		initialize();
	}
	
	public OSMReader(String fileName) {
		initialize();
		parseFile(fileName);
	}
	
	public void initialize() {
		Main.log("Initialzed OSM Parser");
	}
	
	public void addOSMListener(OSMParserListener listener) {
		parserListeners.add(listener);
	}
	
	public void addInputListener(InputStreamListener listener) {
		inputListeners.add(listener);
	}
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void parseFile(String fileName) {
		this.fileName = fileName;
		
		TimerUtil parseTimer = new TimerUtil();
		MemoryUtil parseMemory = new MemoryUtil();
		parseTimer.on();
		parseMemory.on();

		if(fileName.endsWith(".bin")) {
			if(Main.production && new File(fileName).getName().equals("default.bin")) {
				fileName = getClass().getResource(fileName).toString();
			}
			else {
				try {
					for(Path checkSumDir : Files.newDirectoryStream(Paths.get("parsedOSMFiles"))) {
						for(Path parsedFile : Files.newDirectoryStream(checkSumDir)) {
							if(parsedFile.toString().equals(fileName)) currentChecksum = checkSumDir.toFile().getName();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BinaryWrapper binary = (BinaryWrapper) Util.readObjectFromFile(fileName, inputListeners);
			Util.extractAllFromBinary(binary);
			for(InputStreamListener listener : inputListeners) listener.onSetupDone();
		}
		else {
			try {
                currentChecksum = Util.getFileChecksumMD5(fileName);
            } catch (NoSuchAlgorithmException | IOException e1) {
                e1.printStackTrace();
            }
			if (!Main.userPreferences.isForcingParsing() && !Main.userPreferences.isSavingToBinary() && checkSumExists(currentChecksum)) {
	                fileName = Util.getBinaryFilePath();
	                BinaryWrapper binary = (BinaryWrapper) Util.readObjectFromFile(fileName, inputListeners);
	                Util.extractAllFromBinary(binary);
	                for (InputStreamListener listener : inputListeners) listener.onSetupDone();
			}  
            else {
            	if (fileName.endsWith(".osm")) {
                    try {
                    	if(Main.production) inputStream = new FileInputStream(getClass().getResource(fileName).toString());
                    	else inputStream = new FileInputStream(fileName);
                        InputMonitor monitor = new InputMonitor(inputStream, fileName);
                        for (InputStreamListener inListener : inputListeners) monitor.addListener(inListener);
                        loadOSM(new InputSource(monitor), fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else if (fileName.endsWith(".zip")) {
                    try {
                    	if(Main.production) inputStream = new FileInputStream(getClass().getResource(fileName).toString());
                    	else inputStream = new FileInputStream(fileName);
                        InputMonitor monitor = new InputMonitor(inputStream, fileName);
                        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(monitor));
                        zip.getNextEntry();
                        for (InputStreamListener inListener : inputListeners) monitor.addListener(inListener);
                        loadOSM(new InputSource(zip), fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            	
                if(Main.userPreferences.isSavingToBinary()) {
                    String path = "parsedOSMFiles/" + currentChecksum + "/";
                    try {
                    	Path filePath = Paths.get(path);
                        if(!Files.exists(filePath)) Files.createDirectory(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BinaryWrapper binary = new BinaryWrapper();
                    Util.addAllToBinary(binary);
                    Util.writeObjectToFile(binary, Util.getBinaryFilePath());
                }
            }

            for (InputStreamListener listener : inputListeners) listener.onSetupDone();
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
			reader.setContentHandler(Main.model);
			reader.parse(source);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkSumExists(String checkSum) {
		if(Main.production) return getClass().getResource("/parsedOSMFiles/"+checkSum) != null;
		return Files.exists(Paths.get("parsedOSMFiles/"+checkSum));
	}
	
	public void setChecksum(String checksum) {
		this.currentChecksum = checksum;
	}
	
	public String getChecksum() {
		return currentChecksum;
	}
	
}
