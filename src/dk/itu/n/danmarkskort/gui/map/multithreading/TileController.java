package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.multithreading.Queue;

public class TileController {
	
	private Point tileSize = new Point(1024, 1024);
	private HashMap<String, Tile> tiles;
	private Point currentLeftTop;
	private Point2D zero;
	private AffineTransform imageTransform;
	
	// Every tile should have and zoom with its own transform
	// Unrendered tiles should be panned and zoomed aswell
	// 
	
	Queue tileQueue;
	
	public TileController() {
		tiles = new HashMap<>();
		tileQueue = new Queue();
		currentLeftTop = new Point(0,0);
		imageTransform = new AffineTransform();
		zero = new Point2D.Float(0, 0);
	}
	
	public void setTileSize(int width, int height) {
		tileSize = new Point(width, height);
		fullRepaint();
	}
	
	public void fullRepaint() {
		// Start rendering of all current tiles, replacing the ones that are present.
	}
	
	public void updateZero() {
		zero = new Point2D.Double(imageTransform.getTranslateX(), imageTransform.getTranslateY());
		fullRepaint();
	}
	
	public void hotswap(String oldTileKey, Tile newTile) {
		if(tiles.containsKey(oldTileKey)) {
			tiles.remove(oldTileKey);
			tiles.put(newTile.getKey(), newTile);
		}
	}
	
	public void hotswapUselesstile(Tile newTile) {
		for(Tile tile : tiles.values()) {
			if(tile.getPos().x < currentLeftTop.x - 1 || tile.getPos().x > currentLeftTop.x + 1 ||
			   tile.getPos().x < currentLeftTop.x - 1 || tile.getPos().x > currentLeftTop.x + 1) {
				hotswap(tile.getKey(), newTile);
				return;
			}
		}
	}
	
	public void update() {
		createTilesIfNessasary();
		if(updateLeftTop()) {
			Main.log("It changed to: " + currentLeftTop.toString());
			List<Tile> unrenderedTiles = getUnrenderedTiles();
			// Start making new tiles.
			// When done, hotswap the old ones. 
		}
	}
	
	public int getTileWidth() {
		return tileSize.x;
	}
	
	public int getTileHeight() {
		return tileSize.y;
	}
	
	public void pan(double dx, double dy) {
		imageTransform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		update();
	}
	
	public void zoom(double scale) {
		
	}
	
	public List<Tile> getUnrenderedTiles() {
		ArrayList<Tile> unrenderedTiles = new ArrayList<>();
		for(int i = currentLeftTop.x - 1; i < currentLeftTop.x + 2; i++) {
			for(int j = currentLeftTop.y - 1; j < currentLeftTop.y + 2; j++) {
				Point current = new Point(i, j);
				String key = pointToKey(current);
				if(!tiles.containsKey(key)) unrenderedTiles.add(new Tile(current));
			}
		}
		Main.log(unrenderedTiles.size() + " unrendered tiles.");
		return unrenderedTiles;
	}
	
	private String pointToKey(Point pos) {
		return pos.x + "" + pos.y;
	}
	
	// Returns true if value is changed
	private boolean updateLeftTop() {
		int x = -Util.roundByN(getTileWidth(), imageTransform.getTranslateX()) / getTileWidth();
		int y = -Util.roundByN(getTileHeight(), imageTransform.getTranslateY()) / getTileHeight();
		boolean outcome = (x != currentLeftTop.x || y != currentLeftTop.getY());
		currentLeftTop = new Point(x, y);;
		return outcome;
	}
	
	private void createTilesIfNessasary() {
		if(tiles.size() == 0) {
			for(int i = -1; i < 2; i++) {
				for(int j = -1; j < 2; j++) {
					Tile tile = new Tile(new Point(i, j));
					tiles.put(i + "" + j, tile);
				}
			}
			Main.log("Created initial " + tiles.size() + " tiles.");
		}
		
	}
	
}
