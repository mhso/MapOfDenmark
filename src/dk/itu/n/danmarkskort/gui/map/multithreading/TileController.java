package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.Timer;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.multithreading.Queue;
import dk.itu.n.danmarkskort.multithreading.TaskPriority;

public class TileController implements ActionListener {

	private int tileWidth, tileHeight;
	private boolean isInitialized, blur;
	public boolean blockNextPan = false;
	public double imageScale, tileX, tileY;
	public Point tilePos;
	public Point2D zero;
	public AffineTransform tileTransform;
	public AffineTransform zoomTransform;
	public HashMap<String, Tile> tiles;
	public Queue tileQueue;
	public Timer blurTimer;
	public BufferedImage zoomImage;
	
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
		Tile tile = new Tile(new Point(0, -1));
		tiles.put(tile.getKey(), tile);
		queueTile(tile, TaskPriority.MEDIUM, true, false);
		tileTransform = new AffineTransform();
		zoomTransform = new AffineTransform();
		zoomImage = null;
		tileX = 0;
		tileY = 0;
		isInitialized = true;
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	public boolean isTileQueueEmpty() {
		return tileQueue.size() == 0;
	}
	
	public void updateZero() {
		zero = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
	}
	
	public void zoom(double scale) {
		if(!Main.buffered) return;
		imageScale *= scale;
		if(!isBlurred()) blur();
		else Util.zoom(zoomTransform, scale);
		blurTimer.restart();
	}
	
	public void prepareZoomImage() {
		zoomImage = Util.screenshotWithoutGUI();
		zoomTransform = new AffineTransform();
		Point mousePositionScreen = MouseInfo.getPointerInfo().getLocation();
		zoomTransform.translate(-mousePositionScreen.x + Main.map.getLocationOnScreen().x, -mousePositionScreen.y + Main.map.getLocationOnScreen().y);
	}
	
	public boolean updateTilePos() {
		boolean outcome;
		int x = (int) tileX;
		int y = (int) tileY;
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
	
	public void queueTile(Tile tile, TaskPriority priority, boolean repaintAfterRender, boolean swapWhenDone) {
		if(isBlurred()) return;
		TileRenderTask task = new TileRenderTask(tile);
		task.setRepaintWhenDone(repaintAfterRender);
		task.setPriority(priority);
		task.setSwapWhenDone(swapWhenDone);
		tileQueue.addTask(task);
	}
	
	public void setTileSize(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		fullRepaint();
	}
	
	public void fullRepaint() {
		zoom(1);
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
		if(isBlurred() && zoomImage != null) {
			g2d.setTransform(zoomTransform);
			g2d.drawImage(zoomImage, 0, 0, null);
		} else {			
			String[] tileKeys = tiles.keySet().toArray(new String[tiles.size()]);
			for(String key : tileKeys) {
				Tile tile = tiles.get(key);
				if(tile == null) continue;
				tile.draw(g2d);
			}
		}
	}
	
	public boolean isBlurred() {
		return blur;
	}
	
	public void blur() {
		if(!isInitialized()) return;
		prepareZoomImage();
		tiles.clear();
		blur = true;
	}
	
	public void unblur() {
		
		// Update transform.
		updateZero();
		updateTilePos();
		tiles.clear();
		zoomImage = null;
		imageScale = 1;
		blur = false;
		
		// Render the viewport tile.
		Tile tileView = new Tile(new Point(0, -1));
		tiles.put(tileView.getKey(), tileView);
		queueTile(tileView, TaskPriority.HIGH, true, false);
		
		// Render the surrounding tiles.
		for(int x=-1; x<2; x++) {
			for(int y=-2; y<1; y++) {
				if(x == 0 && y == -1) continue;
				Tile tile = new Tile(new Point(x, y));
				tiles.put(tile.getKey(), tile);
				queueTile(tile, TaskPriority.MEDIUM, true, false);
			}
		}
		
		
	}

	public void actionPerformed(ActionEvent e) {
		unblur();
	}
	
	public void swapTile(Tile tileOld, Tile tileNew) {
		tiles.remove(tileOld.getKey());
		tiles.put(tileNew.getKey(), tileNew);
	}
	
	public void swapTileWithUselessTile(Tile newTile) {
		String[] tileKeys = tiles.keySet().toArray(new String[tiles.size()]);
		for(String key : tileKeys) {
			Tile oldTile = tiles.get(key);
			if(oldTile == null) continue;
			if(oldTile.isUseless()) {
				swapTile(oldTile, newTile);
				return;
			}
		}
	}
	
	public void checkForNewTiles() {
		if(!Main.buffered) return;
		List<Tile> newTiles = getNewTiles();
		for(Tile tile : newTiles) queueTile(tile, TaskPriority.HIGHEST, true, true);
		
	}
	
	public List<Tile> getNewTiles() {
		List<Tile> newTiles = new ArrayList<Tile>();
		for(int x=tilePos.x-1; x<tilePos.x+2; x++) {
			for(int y=tilePos.y-2; y<tilePos.y+1; y++) {
				Tile tile = new Tile(new Point(x, y));
				if(!tiles.containsKey(tile.getKey())) newTiles.add(tile);
			}
		}
		return newTiles;
	}
	
	public void pan(double tx, double ty) {
		if(isBlurred()) Util.pan(zoomTransform, tx, ty);
		if(!Main.buffered) return;
		if(blockNextPan) {
			blockNextPan = false;
			return;
		}
		tileTransform.translate(tx, ty);
		tileX = -Util.roundByN(0.5, tileTransform.getTranslateX() / getTileWidth());
		tileY = -Util.roundByN(0.5, tileTransform.getTranslateY() / getTileHeight());
	}
	
	public void resetTileTransform() {
		tileTransform = new AffineTransform();
		blockNextPan = true;
	}
	
}
