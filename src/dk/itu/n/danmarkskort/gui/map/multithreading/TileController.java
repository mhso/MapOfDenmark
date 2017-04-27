package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashMap;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.multithreading.Queue;
import dk.itu.n.danmarkskort.multithreading.TaskPriority;

public class TileController {

	public double tileWidth, tileHeight;
	public Point2D zero;
	public boolean isInitialized;
	public Queue tileQueue;
	public HashMap<String, Tile> tiles;
	
	public TileController() {
		isInitialized = false;
	}
	
	public void initialize() {
		tiles = new HashMap<>();
		tileWidth = Main.map.getWidth();
		tileHeight = Main.map.getHeight();
		tileQueue = new Queue();
		Queue.run(tileQueue);
		updateZero();
	}
	
	public void updateZero() {
		zero = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
	}
	
	public Point2D getZero() {
		return zero;
	}
	
	public void queueTile(Tile tile, TaskPriority priority, boolean repaintAfterRender) {
		TileRenderTask task = new TileRenderTask(tile);
		task.setRepaintWhenDone(repaintAfterRender);
		task.setPriority(priority);
		tileQueue.addTask(task);
	}
	
	public void setTileSize(double tileWidth, double tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public double getTileWidth() {
		return tileWidth;
	}
	
	public double getTileHeight() {
		return tileHeight;
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
		for(Tile tile : tiles.values()) tile.draw(g2d);
	}
	
}
