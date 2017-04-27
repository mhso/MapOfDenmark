package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
		blurTimer = new Timer(500, this);
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
		blurTimer.restart();
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
		blur = true;
	}
	
	public void unblur() {
		updateZero();
		updateTilePos();
		tiles.clear();
		imageScale = 1;
		
		Tile tile = new Tile(new Point(0, -1));
		tiles.put(tile.getKey(), tile);
		queueTile(tile, TaskPriority.HIGHEST, true);
		
		blur = false;
	}

	public void actionPerformed(ActionEvent e) {
		unblur();
	}
	
	public void swapTile(Tile tileOld, Tile tileNew) {
		tiles.remove(tileOld.getKey());
		tiles.put(tileNew.getKey(), tileNew);
	}
	
	public void swapTileWithUselessTile(Tile tile) {
		
	}
	
	public void checkForNewTiles() {
		List<Tile> newTiles = getNewTiles();
	}
	
	public List<Tile> getNewTiles() {
		List<Tile> newTiles = new ArrayList<Tile>();
		for(int x=-1; x<2; x++) {
			for(int y=-2; y<1; y++) {
				Tile tile = new Tile(new Point(x, y));
				if(!tiles.containsKey(tile.getKey())) newTiles.add(tile);
			}
		}
		return newTiles;
	}
	
}
