package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.models.Coordinate;
import dk.itu.n.danmarkskort.models.ParsedObject;
import dk.itu.n.danmarkskort.models.Tile;
import dk.itu.n.danmarkskort.models.TileCoordinate;

// This class is used to extract exact information from tile files. 
public class TileController implements OSMParserListener {

	public void requestTiles(Coordinate from, Coordinate to) {
		
	}

	public void onParsingStarted() {
	}

	public void onLineCountHundred() {
	}

	public void onParsingFinished() {
		Tile tile = new Tile(new TileCoordinate(10,10), 3);
		tile.write();
	}

	public void onParsingGotObject(ParsedObject parsedObject) {
	}
}
