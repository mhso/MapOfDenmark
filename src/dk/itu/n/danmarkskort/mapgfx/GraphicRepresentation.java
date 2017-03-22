package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.SAXAdapter;
import dk.itu.n.danmarkskort.models.WayType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GraphicRepresentation {
	private static ArrayList<WaytypeGraphicSpec>[] zoomLevelArr 
		= new ArrayList[20];
	private static EnumMap<WayType, Integer> zoomMap = new EnumMap<>(WayType.class);
	
	/**
	 * Get the Graphic Specification matching the inputed Map Element.
	 * 
	 * @param mapElement The Map Element (a WayType or Node f.x.) to get the graphics for.
	 * @return A Graphic Specification object representing how a Map Element should be drawn.
	 */
	public static List<WaytypeGraphicSpec> getGraphicSpecs(int zoomLevel) {
		zoomLevel -= 1;
		List<WaytypeGraphicSpec> cummulativeList = new ArrayList<>();
		for(int i = zoomLevel; i >= 0; i--) {
			cummulativeList.addAll(zoomLevelArr[i]);
		}
		return cummulativeList;
	}
	
	/**
	 * Test main method.
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		if(args.length > 0) parseData(new InputSource(args[0]));
	}
	
	/**
	 * Get the size of the Graphic Representation Map contained in this GraphicRepresentation class.
	 * 
	 * @return The Graphic Representation Map contained in this GraphicRepresentation class.
	 */
	public static int size() {
		return zoomMap.size();
	}
	
	/**
	 * Parse an XML file containing information about the graphical representation of the map.
	 * 
	 * @param source The InputSource of the XML file.
	 */
	public static void parseData(InputSource source) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			zoomLevelArr[i] = new ArrayList<WaytypeGraphicSpec>();
		}
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ZoomHandler());
			reader.parse(new InputSource("resources/ZoomValues.XML"));
			reader = XMLReaderFactory.createXMLReader();
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
	
	private static WayType stringToEnum(String waytype) {
		return WayType.valueOf(waytype);
	}
	
	private static class ZoomHandler extends SAXAdapter {
		private int currentZoomValue;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			switch(qName) {
				case "zoomlevel":
					currentZoomValue = Integer.parseInt(atts.getValue("level"));
				break;
				case "tag":
					zoomMap.put(stringToEnum(atts.getValue("v")), currentZoomValue-1);
				break;
			}
		}
	}
	
	private static class GraphicsHandler extends SAXAdapter {
		private static final float LINE_MAGNIFYING_VALUE = 0.001f;
		
		private static WayType mapElement;
		private static WaytypeGraphicSpec gs;
		private static int defaultFontSize;
		private static Color defaultFontColor;

		@Override
		public void endElement(String uri, String localname, String qName) throws SAXException {
			switch(qName) {
				case "line":
					zoomLevelArr[zoomMap.get(mapElement)].add(gs);
				break;
				case "area":
					zoomLevelArr[zoomMap.get(mapElement)].add(gs);
				break;
				case "label":
					zoomLevelArr[zoomMap.get(mapElement)].add(gs);
				break;
				case "icon":
					zoomLevelArr[zoomMap.get(mapElement)].add(gs);
				break;
			}		
		}

		@Override
		public void startElement(String uri, String localname, String qName, Attributes atts) 
				throws SAXException {
			switch(qName) {
				case "tag":
					mapElement = stringToEnum(atts.getValue("v"));
					gs.setMapElement(mapElement);
				break;
				case "defaultfont": 
					defaultFontSize = Integer.parseInt(atts.getValue("fontsize"));
					defaultFontColor = parseColor(atts.getValue("fontcolor"));
				break;
				case "line":
					gs = new GraphicSpecLine();
				break;
				case "area":
					gs = new GraphicSpecArea();
				break;
				case "label":
					gs = new GraphicSpecLabel(defaultFontSize, defaultFontColor);
				break;
				case "icon":
					gs = new GraphicSpecIcon();
				break;
				case "innercolor":
					gs.setInnerColor(parseColor(atts.getValue("color")));
				break;
				case "outercolor":
					gs.setOuterColor(parseColor(atts.getValue("color")));
				break;
				case "lineproperties":
					float lineWidth = (float)(Double.parseDouble(atts.getValue("linewidth")) * LINE_MAGNIFYING_VALUE);
					float[] dashArr = null;
					if(atts.getValue("linedash") != null) {
						String[] splitArr = atts.getValue("linedash").split(",");
						dashArr = new float[splitArr.length];
						for(int i = 0; i < splitArr.length; i++) {
							dashArr[i] = Integer.parseInt(splitArr[i].trim());
						}
					}
					GraphicSpecLine gsl = (GraphicSpecLine) gs;
					gsl.setLineWidth(lineWidth);
					gsl.setDashArr(dashArr);
				break;
				case "fontcolor":
					GraphicSpecLabel gsla = (GraphicSpecLabel) gs;
					gsla.setFontColor(parseColor(atts.getValue("fontcolor")));
				break;
				case "fontsize":
					gsla = (GraphicSpecLabel) gs;
					gsla.setFontSize(Integer.parseInt(atts.getValue("fontsize")));
				break;
			}
		}
	}
}