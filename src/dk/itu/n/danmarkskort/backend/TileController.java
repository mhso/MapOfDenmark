package dk.itu.n.danmarkskort.backend;

import java.util.ArrayList;
import java.util.EnumMap;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.ParsedBounds;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Tile;
import dk.itu.n.danmarkskort.models.TileCoordinate;
import dk.itu.n.danmarkskort.models.WayType;

// This class is used to extract exact information from tile files. 
public class TileController implements OSMParserListener {

	private ParsedBounds mapBounds = null;
	private final int staticZoomLevel = 1;
	public final int MAX_ZOOM_LEVEL = 20;
	public EnumMap<WayType, ArrayList<ParsedWay>> ways = new EnumMap<>(WayType.class);

	public Tile requestTile(int x, int y, int zoom) {
		Tile tile = new Tile(new TileCoordinate(x, y), zoom);
		tile.load();
		return tile;
	}

	public void onParsingStarted() {
		prepareWays();
	}
	public void onLineCountHundred() {}

	public void onParsingFinished() {
		GraphicRepresentation.main(new String[]{"resources/ThemeBasic.XML"});
	}

	public void onParsingGotObject(ParsedObject parsedObject) {
		if(parsedObject instanceof ParsedBounds) {
			mapBounds = (ParsedBounds) parsedObject;
			Main.log("Horizontal tiles: " + mapBounds.getHorizontalTileCount() + ", vertical tiles: " + mapBounds.getVerticalTileCount());
		}
	}
	
	public void prepareTileFiles() {
		int hor = getBounds().getHorizontalTileCount();
		int ver = getBounds().getVerticalTileCount();
		
		for(int x=0; x<hor; x++) {
			for(int y=0; y<ver; y++) {
				Tile tile = new Tile(new TileCoordinate(x, y), staticZoomLevel);
				tile.write();
			}
		}
	}
	
	public void renderTiles() {
		int hor = getBounds().getHorizontalTileCount();
		int ver = getBounds().getVerticalTileCount();
		for(int zoom=1; zoom<MAX_ZOOM_LEVEL; zoom++) {
			int w = hor * zoom;
			int h = ver * zoom;
			for(int x=0; x<w; x++) {for(int y=0; y<h; y++) {
				
			}}
			
		}
	}
	
	public boolean hasBounds() {
		return (this.mapBounds != null);
	}
	
	public ParsedBounds getBounds() {
		return mapBounds;
	}

	public void prepareWays() {
		for(WayType wayType : WayType.values()) ways.put(wayType, new ArrayList<>());
	}
	
	public void onWayLinked(ParsedWay way) {
		ways.get(way.type).add(way);
	}
	
	public ArrayList<ParsedWay> getWaysOfType(WayType type) {
		return ways.get(type);
	}
}
