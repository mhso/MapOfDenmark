package dk.itu.n.danmarkskort.backend;

import java.util.ArrayList;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.Coordinate;
import dk.itu.n.danmarkskort.models.ParsedBounds;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Tile;
import dk.itu.n.danmarkskort.models.TileCoordinate;

// This class is used to extract exact information from tile files. 
public class TileController implements OSMParserListener {

	private ParsedBounds mapBounds = null;
	private final int staticZoomLevel = 1;
	public ArrayList<ParsedWay> wayList = new ArrayList<ParsedWay>();
	
	public Tile requestTile(int x, int y, int zoom) {
		Tile tile = new Tile(new TileCoordinate(x, y), zoom);
		tile.load();
		return tile;
	}

	public void onParsingStarted() {}
	public void onLineCountHundred() {}

	public void onParsingFinished() {
		prepareTileFiles();
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
	
	public boolean hasBounds() {
		return (this.mapBounds != null);
	}
	
	public ParsedBounds getBounds() {
		return mapBounds;
	}

	public void onWayLinked(ParsedWay way) {
		wayList.add(way);
	}
}
