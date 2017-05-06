package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.backend.SAXAdapter;
import dk.itu.n.danmarkskort.models.WayType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GraphicRepresentation {
	private static Color canvasBGColor;
	private static Color coastlineColor;
	
	private static ArrayList<WaytypeGraphicSpec>[] zoomLevelArr = new ArrayList[20];
	private static List<WaytypeGraphicSpec> overriddenSpecs = new ArrayList<>();
	private static EnumMap<WayType, Integer> zoomMap = new EnumMap<>(WayType.class);
	private static String currentTheme;
	
	/**
	 * Get a list of Graphic Specification objects matching the inputed zoom level. 
	 * The Graphic Specs returned are for all zoom levels less than and equal to the specified zoom level.
	 * Fx. input zoom level 10 returns all Graphic Specs for level 10 and below.
	 * 
	 * Furthermore, the Graphic Specs are sorted by their layer values, meaning lower layer objects appear first in the list and are thereby drawn first.
	 * 
	 * @param zoomLevel The current zoom level for which to draw elements.
	 * @return A list of WaytypeGraphicSpec objects, specifying what should be drawn at the specific
	 * zoom level and how they should be drawn.
	 */
	public static List<WaytypeGraphicSpec> getGraphicSpecs(int zoomLevel) {
		zoomLevel -= 1;
		List<WaytypeGraphicSpec> cummulativeList = new ArrayList<>();
		cummulativeList.addAll(overriddenSpecs);
		for(int i = zoomLevel; i >= 0; i--) {
			for(WaytypeGraphicSpec wgs : zoomLevelArr[i]) {
				if(!wgs.isFiltered() && !overriddenSpecs.contains(wgs)) cummulativeList.add(wgs);
			}
		}
		cummulativeList.sort(null);
		return cummulativeList;
	}
	
	/**
	 * This method acts like a filter where a List of different types of WaytypeGraphicSpecs can be inputed and a new List
	 * of all the GraphicSpec objects in the list, that inherit from the specified WaytypeGraphicSpec, will be returned.
	 * 
	 * @param allSpecs The List of mixed Graphic Specs.
	 * @param searchClass The type of GraphicSpec to return a separate list of.
	 * @return A new List of all the filtered WaytypeGraphicSpecs in the original list.
	 */
	public static List<WaytypeGraphicSpec> getSpecificSpec(List<WaytypeGraphicSpec> allSpecs, 
			Class<? extends WaytypeGraphicSpec> searchClass) {
		List<WaytypeGraphicSpec> result = new ArrayList<>();
		for(WaytypeGraphicSpec spec : allSpecs) {
			if(spec.getClass() == searchClass) result.add(spec);
		}
		return result;
	}
	
	/**
	 * Add a WayType to Overridden Graphic Specifications, meaning the Graphic Specification for this WayType will be drawn at any zoom level.
	 * 
	 * @param wayType The WayType that should always be drawn.
	 */
	public static void addToOverriddenSpecs(WayType wayType) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			for(int j = 0; j < zoomLevelArr[i].size(); j++) {
				if(zoomLevelArr[i].get(j).getWayType() == wayType) overriddenSpecs.add(zoomLevelArr[i].get(j));
			}
		}
	}
	
	/**
	 * Set the Graphic Specification, associated with the specified WayType, back to its default zoom level.
	 * 
	 * @param wayType The WayType that should be reset to its default zoom level.
	 */
	public static void setDefault(WayType wayType) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			for(int j = 0; j < zoomLevelArr[i].size(); j++) {
				if(zoomLevelArr[i].get(j).getWayType() == wayType) {
					overriddenSpecs.remove(zoomLevelArr[i].get(j));
					zoomLevelArr[i].get(j).setFiltered(false);
				}
			}
		}
	}
	
	/**
	 * Set all Graphic Specification back to their default zoom levels.
	 */
	public static void setAllDefault() {
		overriddenSpecs.removeAll(overriddenSpecs);
		for(int i = 0; i < zoomLevelArr.length; i++) {
			for(int j = 0; j < zoomLevelArr[i].size(); j++) {
				if(zoomLevelArr[i].get(j).isFiltered()) zoomLevelArr[i].get(j).setFiltered(false);
			}
		}
	}
	
	/**
	 * Set whether a WayType should be filtered from the drawing process.
	 * 
	 * @param wayType The WayType to set filtering for.
	 * @param filtered Whether the WayType should be filtered.
	 */
	public static void setFilteredElement(WayType wayType, boolean filtered) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			for(int j = 0; j < zoomLevelArr[i].size(); j++) {
				if(zoomLevelArr[i].get(j).getWayType() == wayType) zoomLevelArr[i].get(j).setFiltered(filtered);
			}
		}
	}
	
	/**
	 * Check whether the specified WayType is being filtered in the drawing process.
	 * 
	 * @param wayType The WayType to check for.
	 * @return Whether the WayType is being drawn.
	 */
	public static boolean isFiltered(WayType wayType) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			for(int j = 0; j < zoomLevelArr[i].size(); j++) {
				if(zoomLevelArr[i].get(j).getWayType() == wayType) return zoomLevelArr[i].get(j).isFiltered();
			}
		}
		return false;
	}
	
	/**
	 * Check whether the specified WayType is overriding the zoom hierarchy, I.E: always being drawn.
	 * 
	 * @param wayType The WayType to check for.
	 * @return Whether the WayType is always being drawn.
	 */
	public static boolean isOverridden(WayType wayType) {
		for(WaytypeGraphicSpec wgs : overriddenSpecs) {
			if(wgs.getWayType() == wayType) return true;
		}
		return false;
	}
	
	/**
	 * Get the size of the Graphic Representation Map contained in this GraphicRepresentation class.
	 * 
	 * @return The Graphic Representation Map contained in this GraphicRepresentation class, IE: The amount of
	 * WayTypes, that have Graphic Specifications associated with them.
	 */
	public static int size() {
		return zoomMap.size();
	}
	
	/**
	 * Parse an XML file containing information about the graphical representation of the map.
	 * This method first parses and saves data from the basic theme (ThemeBasic.XML). If the inputed theme is a different one, 
	 * the values that exist in that theme will override those of the basic theme. 
	 * 
	 * @param themeFile The filename of the XML file.
	 */
	public static void parseData(String themeFile) {
		for(int i = 0; i < zoomLevelArr.length; i++) {
			zoomLevelArr[i] = new ArrayList<WaytypeGraphicSpec>();
		}
		if(currentTheme == null) parseZoomValues();
		String parseFile = "";
		if(Main.production) parseFile = GraphicRepresentation.class.getResource("/resources/themes/ThemeBasic.XML").toString();
		else parseFile = "resources/themes/ThemeBasic.XML";
		parseTheme(parseFile);
		if(!themeFile.equals(parseFile)) parseTheme(themeFile);
		currentTheme = themeFile;
	}
	
	private static void parseZoomValues() {
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new ZoomHandler());
			if(Main.production) reader.parse(new InputSource(GraphicRepresentation.class.getResourceAsStream("/resources/themes/ZoomValues.XML")));
			else reader.parse(new InputSource("resources/themes/ZoomValues.XML"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseTheme(String file) {
		try {
			InputSource source = new InputSource(file);
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new GraphicsHandler());
			reader.parse(source);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCurrentTheme() {
		return currentTheme.substring(10, currentTheme.length()-4);
	}
	
	public static Color getCanvasBGColor() {
		return canvasBGColor;
	}
	
	public static Color getCoastlineColor() {
		return coastlineColor;
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
	
	private static class ZoomHandler extends SAXAdapter {
		private int currentZoomValue;
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
			switch(qName) {
				case "zoomlevel":
					currentZoomValue = Integer.parseInt(atts.getValue("level"));
				break;
				case "type":
					zoomMap.put(WayType.valueOf(atts.getValue("name")), currentZoomValue-1);
				break;
			}
		}
	}

	private static class GraphicsHandler extends SAXAdapter {
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
				case "type":
					mapElement = WayType.valueOf(atts.getValue("name"));
					gs.setWayType(mapElement);
				break;
				case "bgcolor":
					canvasBGColor = parseColor(atts.getValue("color"));
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
					if(mapElement == WayType.COASTLINE) coastlineColor = parseColor(atts.getValue("color"));
				break;
				case "outercolor":
					gs.setOuterColor(parseColor(atts.getValue("color")));
				break;
				case "layer":
					gs.setLayer(Integer.parseInt(atts.getValue("layer")));
				break;
				case "lineproperties":
					float lineWidth = (float)(Double.parseDouble(atts.getValue("linewidth")) * DKConstants.LINE_MAGNIFYING_VALUE);
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
					if(atts.getValue("lineend") != null) {
						String endType = atts.getValue("lineend");
						if(endType.equalsIgnoreCase("BUTT")) gsl.setLineEnd(BasicStroke.CAP_BUTT);
						else if(endType.equalsIgnoreCase("ROUND")) gsl.setLineEnd(BasicStroke.CAP_ROUND);
						else if(endType.equalsIgnoreCase("SQUARE")) gsl.setLineEnd(BasicStroke.CAP_SQUARE);
					}
					if(atts.getValue("linejoin") != null) {
						String joinType = atts.getValue("linejoin");
						if(joinType.equalsIgnoreCase("BEVEL")) gsl.setLineEnd(BasicStroke.JOIN_BEVEL);
						else if(joinType.equalsIgnoreCase("ROUND")) gsl.setLineEnd(BasicStroke.JOIN_ROUND);
						else if(joinType.equalsIgnoreCase("MITER")) gsl.setLineEnd(BasicStroke.JOIN_MITER);
					}
					if(atts.getValue("borderdash") != null) gsl.setBorderDashed(Boolean.parseBoolean(atts.getValue("borderdash")));
				break;
				case "fontsize":
					GraphicSpecLabel gsla = (GraphicSpecLabel) gs;
					gsla.setFontSize(Integer.parseInt(atts.getValue("fontsize")));
				break;
			}
		}
	}
}