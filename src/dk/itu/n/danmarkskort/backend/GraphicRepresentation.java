package dk.itu.n.danmarkskort.backend;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.models.GraphicLayer;
import dk.itu.n.danmarkskort.models.GraphicSpecification;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class GraphicRepresentation {
	private static Map<Object, GraphicSpecification> graphicsMap = new HashMap<>();
	
	public GraphicSpecification getGraphics(Object mapElement) {
		return graphicsMap.get(mapElement);
	}
	
	/**
	 * Test main method.
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		if(args.length > 0) parseData(new InputSource(args[0]));
		for(GraphicSpecification gs : graphicsMap.values()) {
			System.out.println(gs);
		}
	}
	
	/**
	 * Parse an XML file containing information about the graphical representation of the map.
	 * 
	 * @param source The InputSource of the XML file.
	 */
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
	
	/**
	 * Parse a color from Hex to RGB.
	 * 
	 * @param hex A Hexadecimal String representation of a color.
	 * @return A Java RGB Color matching the inputed Hex color.
	 */
	private static Color parseColor(String hex) {
		if(hex.charAt(0) == '#') hex = hex.substring(1);
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return new Color(r, g, b);
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
				gs = new GraphicSpecification(mapElement);
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
