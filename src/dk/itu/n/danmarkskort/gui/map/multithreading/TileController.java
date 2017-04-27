package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.swing.Timer;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.multithreading.Queue;
import dk.itu.n.danmarkskort.multithreading.TaskPriority;

public class TileController implements ActionListener {

	public int tileWidth, tileHeight;
	public Point2D zero;
	public Point tilePos;
	public boolean isInitialized, blur;
	public Queue tileQueue;
	public HashMap<String, Tile> tiles;
	public double imageScale;
	public Timer blurTimer;
	
	public TileController() {
		isInitialized = false;
	}
	
	public void initialize() {
		tiles = new HashMap<>();
		tileWidth = Main.map.getWidth();
		tileHeight = Main.map.getHeight();
		tileQueue = new Queue();
		Queue.run(tileQueue);
		tilePos = new Point(0, 0);
		updateZero();
		blur = false;
		imageScale = 1;
		blurTimer = new Timer(1000, this);
		blurTimer.setRepeats(false);
		
		// Debug tiles start
		Tile tile = new Tile(new Point(0, -1));
		tiles.put(tile.getKey(), tile);
		queueTile(tile, TaskPriority.MEDIUM, true);
		// Debug tiles end
		
		isInitialized = true;
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	public void updateZero() {
		zero = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
	}
	
	public void zoom(double scale) {
		imageScale *= scale;
		if(!isBlurred()) blur();
	}
	
	public boolean updateTilePos() {
		boolean outcome;
		
		Region view = Main.map.getActualGeographicalRegion();
		int x = (int) (Util.roundByN(getGeographicalTileWidth(), view.x1 - zero.getX()) / getGeographicalTileWidth());
		int y = (int) (Util.roundByN(getGeographicalTileHeight(), view.y1 - zero.getY()) / getGeographicalTileHeight());
		outcome = (x != tilePos.x || y != tilePos.y);
		tilePos = new Point(x, y);
		
		return outcome;
	}
	
	public Point2D getZero() {
		return zero;
	}
	
	public Point getTilePos() {
		return tilePos;
	}
	
	public void queueTile(Tile tile, TaskPriority priority, boolean repaintAfterRender) {
		TileRenderTask task = new TileRenderTask(tile);
		task.setRepaintWhenDone(repaintAfterRender);
		task.setPriority(priority);
		tileQueue.addTask(task);
	}
	
	public void setTileSize(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public double getImageScale() {
		return imageScale;
	}
	
	public double getGeographicalTileWidth() {
		Point2D p1 = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
		Point2D p2 = Main.map.toActualModelCoords(new Point2D.Double(tileWidth, tileHeight));
		return p2.getX() - p1.getX();
	}

	public double getGeographicalTileHeight() {
		Point2D p1 = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
		Point2D p2 = Main.map.toActualModelCoords(new Point2D.Double(tileWidth, tileHeight));
		return p2.getY() - p1.getY();
	}
	
	public void draw(Graphics2D g2d) {
		if(!isInitialized()) return;
		for(Tile tile : tiles.values()) tile.draw(g2d);
	}
	
	public boolean isBlurred() {
		return blur;
	}
	
	public void blur() {
		if(!isInitialized()) return;
		blurTimer.restart();
		Main.log("Blurring");
		blur = true;
	}
	
	public void unblur() {
		updateZero();
		updateTilePos();
		tiles.clear();
		imageScale = 1;
		Main.log("Unblurring");
		Main.log("Tile is now: " + getTilePos());
		
		Tile tile = new Tile(new Point(0, -1));
		tiles.put(tile.getKey(), tile);
		queueTile(tile, TaskPriority.HIGHEST, true);
		Tile tile2 = new Tile(new Point(0, 0));
		tiles.put(tile2.getKey(), tile2);
		queueTile(tile2, TaskPriority.MEDIUM, true);
		Tile tile3 = new Tile(new Point(-1, 0));
		tiles.put(tile3.getKey(), tile3);
		queueTile(tile3, TaskPriority.MEDIUM, true);
		Tile tile4 = new Tile(new Point(-1, -1));
		tiles.put(tile4.getKey(), tile4);
		queueTile(tile4, TaskPriority.MEDIUM, true);
		
		blur = false;
	}

	public void actionPerformed(ActionEvent e) {
		unblur();
	}
	
}
