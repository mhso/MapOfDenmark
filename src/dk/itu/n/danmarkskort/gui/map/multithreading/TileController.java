package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Timer;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.multithreading.Queue;
import dk.itu.n.danmarkskort.multithreading.Task;
import dk.itu.n.danmarkskort.multithreading.TaskPriority;

public class TileController implements ActionListener {
	
	private Point tileSize = new Point(1024, 1024);
	private HashMap<String, Tile> tiles;
	private Point currentLeftTop;
	private Point2D zero;
	private AffineTransform imageTransform;
	private AffineTransform blurTransform;
	private boolean firstRender;
	private boolean blur;
	private final int blurTimeout;
	private Timer blurTimer;
	
	Queue tileQueue;
	
	public TileController() {
		tiles = new HashMap<>();
		tileQueue = new Queue();
		currentLeftTop = new Point(0,0);
		imageTransform = new AffineTransform();
		blurTransform = new AffineTransform();
		zero = new Point2D.Float(0, 0);
		firstRender = true;
		blur = false;
		blurTimeout = 1000; //ms
		blurTimer = new Timer(blurTimeout, this);
		blurTimer.setRepeats(false);
		Queue.run(tileQueue);
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
		update();
	}
	
	public void hotswap(String oldTileKey, Tile newTile) {
		if(tiles.containsKey(oldTileKey)) {
			tiles.remove(oldTileKey);
			tiles.put(newTile.getKey(), newTile);
		}
	}
	
	public List<String> getUselessTileKeys() {
		ArrayList<String> uselessTiles = new ArrayList<>(); 
		uselessTiles.addAll(tiles.keySet());
		
		for(int i = currentLeftTop.x - 1; i < currentLeftTop.x + 2; i++) {
			for(int j = currentLeftTop.y - 1; j < currentLeftTop.y + 2; j++) {
				Point current = new Point(i, j);
				String key = pointToKey(current);
				uselessTiles.remove(key);
			}
		}
		return uselessTiles;
	}
	
	public void hotswapUselessTile(Tile newTile) {
		if(tiles.size() < 9) {
			tiles.put(newTile.getKey(), newTile);
		} else {
			List<String> uselessKeys = getUselessTileKeys();
			if(!uselessKeys.isEmpty()) hotswap(getUselessTileKeys().get(0), newTile);			
		}
	}
	
	public void update() {
		if(isBlurred()) return;
		
		if(updateLeftTop() || firstRender) {
			Main.log("It changed to: " + currentLeftTop.toString());
			List<Tile> unrenderedTiles = getUnrenderedTiles();
			Main.log(unrenderedTiles.size() + " unrendered tiles.");
			for(Tile tile : unrenderedTiles) {
				Task task = new TileRenderTask(tile);
				task.setPriority(TaskPriority.LOWEST);
				tileQueue.addTask(task);
				hotswapUselessTile(tile);
			}
			Main.log(tiles.size() + " total tiles.");
			Main.log(tileQueue.size() + " tiles in queue.");
			firstRender = false;
		}
	}
	
	public int getTileWidth() {
		return tileSize.x;
	}
	
	public int getTileHeight() {
		return tileSize.y;
	}
	
	public void pan(double dx, double dy) {
		if(isBlurred()) {
			Util.pan(blurTransform, dx, dy);
		} else {
			Util.pan(imageTransform, dx, dy);
		}
		update();
	}
	
	private void blur() {
		if(firstRender) return;
		blur = true;
		
		// Remove useless tiles to save memory upon zoom
		List<String> uselessTileKeys = getUselessTileKeys();
		blurTransform = (AffineTransform) Main.map.getPixelTransform().clone();
		for(int i=0; i<uselessTileKeys.size(); i++) tiles.remove(uselessTileKeys);
		
		// Restart the blur timer
		blurTimer.restart();
	}
	
	public boolean isBlurred() {
		return blur;
	}
	
	public void zoom(double scale) {
		if(!isBlurred()) blur();
		Util.zoom(blurTransform, scale);
		Main.log("Bluring");
	}
	
	private void unblur() {
		blur = false;
		imageTransform = new AffineTransform();
		updateLeftTop();
		tiles.clear();
		for(int i = currentLeftTop.x - 1; i < currentLeftTop.x + 2; i++) {
			for(int j = currentLeftTop.y - 1; j < currentLeftTop.y + 2; j++) {
				Tile tile = new Tile(new Point(i, j));
				tile.render();
				tiles.put(tile.getKey(), tile);
			}
		}
		Main.log("Unbluring");
		Main.mainPanel.repaint();
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
		currentLeftTop = new Point(x, y);
		return outcome;
	}

	public AffineTransform getBlurTransform() {
		return blurTransform;
	}
	
	public Point2D getZero() {
		return zero;
	}
	
	public void draw(Graphics2D g2d) {
		for(Tile tile: tiles.values()) tile.draw(g2d);
	}
	
	public void repaint(Graphics2D g2d) {
		draw(g2d);
	}

	public void actionPerformed(ActionEvent arg0) {
		if(isBlurred()) unblur();
	}
	
}
