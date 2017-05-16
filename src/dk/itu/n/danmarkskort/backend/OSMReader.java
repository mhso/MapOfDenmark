package dk.itu.n.danmarkskort.backend;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.xml.sax.ContentHandler;
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
	private List<ProgressListener> inputListeners = new ArrayList<ProgressListener>();
	private String currentChecksum;
	private String fileName;
	
	public OSMReader() {
		initialize();
	}
	
	public OSMReader(String fileName, ContentHandler contentHandler) {
		this();
		parseFile(fileName, contentHandler);
	}
	
	public void initialize() {
		Main.log("Initialzed OSM Parser");
	}
	
	public void addOSMListener(OSMParserListener listener) {
		parserListeners.add(listener);
	}
	
	public void addInputListener(ProgressListener listener) {
		inputListeners.add(listener);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void parseFile(String fileName, ContentHandler contentHandler) {
		this.fileName = fileName;
		
		TimerUtil parseTimer = new TimerUtil();
		MemoryUtil parseMemory = new MemoryUtil();
		parseTimer.on();
		parseMemory.on();

		if(fileName.endsWith(".bin")) {
			try {
				for(Path checkSumDir : Files.newDirectoryStream(Paths.get("parsedOSMFiles"))) {
					for(Path parsedFile : Files.newDirectoryStream(checkSumDir)) {
						if(parsedFile.toString().equals(fileName)) currentChecksum = checkSumDir.toFile().getName();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				BinaryWrapper binary = (BinaryWrapper) Util.readObjectFromFile(fileName, inputListeners);
				Util.extractAllFromBinary(binary);
				for(ProgressListener listener : inputListeners) listener.onSetupDone();
			}
			catch (ClassCastException e) {
				Main.handleError("Invalid binary file.", true);
			}
			
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
	                for (ProgressListener listener : inputListeners) listener.onSetupDone();
			}  
            else {
            	if (fileName.endsWith(".osm")) {
                    try {
                    	FileInputStream inputStream = new FileInputStream(fileName);
                        ProgressMonitor monitor = new ProgressMonitor(inputStream);
                        for (ProgressListener inListener : inputListeners) monitor.addListener(inListener);
                        loadOSM(new InputSource(monitor), contentHandler);
                    } catch (FileNotFoundException e) {
                        Main.handleError("File not found.", true);
                    } catch (IOException e) {
                        Main.handleError("Error occured when loading file.", true);
                    } catch(SAXException e) {
                    	Main.handleError("Could not parse OSM data.", true);
                    }

                } else if (fileName.endsWith(".zip")) {
                    try {
                    	FileInputStream inputStream = new FileInputStream(fileName);
                        ProgressMonitor monitor = new ProgressMonitor(inputStream);
                        ZipInputStream zip = new ZipInputStream(new BufferedInputStream(monitor));
                        zip.getNextEntry();
                        for (ProgressListener inListener : inputListeners) monitor.addListener(inListener);
                        loadOSM(new InputSource(zip), contentHandler);
                    } catch (FileNotFoundException e) {
                        Main.handleError("File not found.", true);
                    } catch (IOException e) {
                        Main.handleError("Error occured when loading file.", true);
                    } catch(SAXException e) {
                    	Main.handleError("Could not parse OSM data.", true);
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

            for (ProgressListener listener : inputListeners) listener.onSetupDone();
        }
		
		parseTimer.off();
		parseMemory.off();
		Main.log(parseMemory.humanReadableByteCount(true));
		Main.log("Time spend parsing: "+parseTimer.toString());
		Main.logRamUsage();
	}

	private void loadOSM(InputSource source, ContentHandler contentHandler) throws SAXException, IOException {
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(contentHandler);
		reader.parse(source);
	}
	
	private boolean checkSumExists(String checkSum) {
		return Files.exists(Paths.get("parsedOSMFiles/"+checkSum));
	}
	
	public void setChecksum(String checksum) {
		this.currentChecksum = checksum;
	}
	
	public String getChecksum() {
		return currentChecksum;
	}
	
}
