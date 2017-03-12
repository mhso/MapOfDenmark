package dk.itu.n.danmarkskort.address;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.ZipInputStream;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

public class DEMOAddressParser {
	long id;
	float lat;
	float lon;
	
	public DEMOAddressParser(String filename) {
		load(filename);
	}
	
	public void loadModel(String filename) {
		load(filename);
	}

	public void load(String filename) {
		System.out.println(filename);
		if (filename.endsWith(".osm")) {
			loadOSM(new InputSource(filename));
		} else if (filename.endsWith(".zip")) {
			try {
				ZipInputStream zip = new ZipInputStream(new BufferedInputStream(DEMOResource.loadStream(filename)));
				zip.getNextEntry();
				loadOSM(new InputSource(zip));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(DEMOResource.loadStream(filename)))) {
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadOSM(InputSource source) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new OSMHandler());
			reader.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class OSMHandler implements ContentHandler {
		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startPrefixMapping(String prefix, String uri) throws SAXException {
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			AddressManager addressManager = AddressManager.getInstance();
			switch(qName) {
				case "node":
					id = Long.parseLong(atts.getValue("id"));
					lat = Float.parseFloat(atts.getValue("lat"));
					lon = Float.parseFloat(atts.getValue("lon"));
					addressManager.addOsmAddress(id, lat, lon, null, null);
					break;
				case "tag":
					String k = atts.getValue("k");
					String v = atts.getValue("v");
					addressManager.addOsmAddress(id, lat, lon, k, v);
					break;
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data) throws SAXException {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}
	}
}
