package dk.itu.n.danmarkskort.mikkel;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class GraphicRepresentation {
	private static Map<Object, GraphicSpecification> graphicsMap = new HashMap<>();
	
	public GraphicSpecification getGraphics(Object mapElement) {
		return graphicsMap.get(mapElement);
	}
	
	public static void parseData(InputSource source) {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new GraphicsHandler());
			reader.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Color parseColor(String hex) {
		return null;
	}
	
	private static class GraphicsHandler implements ContentHandler {
		private static Object mapElement;
		private static GraphicSpecification gs;
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endElement(String uri, String localname, String qName) throws SAXException {
			switch(qName) {
			case "element":
				graphicsMap.put(mapElement, gs);
				break;
			}
		}

		@Override
		public void endPrefixMapping(String arg0) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void processingInstruction(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setDocumentLocator(Locator arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void skippedEntity(String arg0) throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void startElement(String uri, String localname, String qName, Attributes attr) 
				throws SAXException {
			switch(qName) {
			case  "tag":
				String k = attr.getValue("k");
				String v = attr.getValue("v");
				mapElement = v;
				break;
			case "layer":
				Color color = parseColor(attr.getValue("color"));
				float lineWidth = Float.parseFloat(attr.getValue("lineWidth"));
				String lineType = attr.getValue("lineType");
				gs.addLayer(new GraphicLayer(color, lineWidth, lineType));
				break;
			}
		}

		@Override
		public void startPrefixMapping(String arg0, String arg1) throws SAXException {
			// TODO Auto-generated method stub
			
		}
		
	}
}
