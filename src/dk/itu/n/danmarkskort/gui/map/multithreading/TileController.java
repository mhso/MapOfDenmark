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
	
	/**This is the tile contoller<br>
	 * It is used to control Tile instances.
	 * It clears out the useless tiles, and renders the new when the user makes changes on the map.
	 */
	public TileController() {
		isInitialized = false;
	}
	
	/**This will initialize this TileController*/
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
	
	/**This will return true if this TileController is initialized*/
	public boolean isInitialized() {
		return isInitialized;
	}
	
	/**This will return true if the tile queue of this TileController is empty<br>
	 * That means if there are any tiles in the queue waiting to be rendered, or is currently being rendered.*/
	public boolean isTileQueueEmpty() {
		return tileQueue.size() == 0;
	}
	
	/**This will update the zero point.<br>
	 * The zero point is the geographical coordinate where the tile coordinate is (0, 0)
	 */
	public void updateZero() {
		zero = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
	}
	
	/**This will zoom the current rendered tiles, and prepare the new tiles for rendering if necessary*/
	public void zoom(double scale) {
		if(!Main.buffered) return;
		imageScale *= scale;
		if(!isBlurred()) blur();
		else Util.zoom(zoomTransform, scale);
		if(blurTimer != null) blurTimer.restart();
	}
	
	/**This will prepare the image used for zooming, which is basically a screenshot of the window.*/
	public void prepareZoomImage() {
		zoomImage = Util.screenshotWithoutGUI();
		zoomTransform = new AffineTransform();
		Point mousePositionScreen = MouseInfo.getPointerInfo().getLocation();
		zoomTransform.translate(-mousePositionScreen.x + Main.map.getLocationOnScreen().x, -mousePositionScreen.y + Main.map.getLocationOnScreen().y);
	}
	
	/**This will update the tile coordinate which is viewport is currenty on.*/
	public boolean updateTilePos() {
		boolean outcome;
		int x = (int) tileX;
		int y = (int) tileY;
		outcome = (x != tilePos.x || y != tilePos.y);
		tilePos = new Point(x, y);
		return outcome;
	}
	
	/**This will return the geographical zero point*/
	public Point2D getZero() {
		return zero;
	}
	
	/**This will return the tile coordinate which the viewport is currently on*/
	public Point getTilePos() {
		return tilePos;
	}
	
	/**This will add a tile to the queue. <br>
	 * It will render the tile, when it is the tile's turn in the queue.<br>
	 * @param tile The tile to render.
	 * @param priority The priority of the tile in the queue.
	 * @param repaintAfterRender If the tile should repaint the map after it is rendered.
	 * @param swapWhenDone If the tile should swap with a useless tile after it is rendered.
	 */
	public void queueTile(Tile tile, TaskPriority priority, boolean repaintAfterRender, boolean swapWhenDone) {
		if(isBlurred()) return;
		TileRenderTask task = new TileRenderTask(tile);
		task.setRepaintWhenDone(repaintAfterRender);
		task.setPriority(priority);
		task.setSwapWhenDone(swapWhenDone);
		tileQueue.addTask(task);
	}
	
	/**This will set the size of the tiles<br>
	 * This is usually used when the window is resized.
	 * @param tileWidth The width you want the tile to have
	 * @param tileHeight The height you want the tile to have
	 */
	public void setTileSize(int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		fullRepaint();
	}
	
	/**This will force the tiles to re-render completely*/
	public void fullRepaint() {
		zoom(1);
	}
	
	/**This will return the width of the tiles*/
	public int getTileWidth() {
		return tileWidth;
	}
	
	/**This will return the height of the tiles*/
	public int getTileHeight() {
		return tileHeight;
	}
	
	/**This will return the scale of the zoom image, if it is zoomed in.*/
	public double getImageScale() {
		return imageScale;
	}
	
	/**This will return the geographical width of the tiles*/
	public double getGeographicalTileWidth() {
		Point2D p1 = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
		Point2D p2 = Main.map.toActualModelCoords(new Point2D.Double(tileWidth, tileHeight));
		return p2.getX() - p1.getX();
	}

	/**This will return the geographical height of the tiles*/
	public double getGeographicalTileHeight() {
		Point2D p1 = Main.map.toActualModelCoords(new Point2D.Double(0, 0));
		Point2D p2 = Main.map.toActualModelCoords(new Point2D.Double(tileWidth, tileHeight));
		return p2.getY() - p1.getY();
	}
	
	/**This will draw all the active tiles on a Graphics2D instance*/
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
	
	/**This will return true if the map is currently blurred.<br>
	 * Being blurred means the map is currently being zoomed by the user.*/
	public boolean isBlurred() {
		return blur;
	}
	
	/**This will blur the tiles. Usually used by zoom()*/
	public void blur() {
		if(!isInitialized()) return;
		prepareZoomImage();
		tiles.clear();
		blur = true;
	}
	
	/**This will unblur the tiles, and render new tiles*/
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

	/**This will unblur the tiles automaticly after the timer is done*/
	public void actionPerformed(ActionEvent e) {
		unblur();
	}
	
	/**This will swap an old tile with a new one*/
	public void swapTile(Tile tileOld, Tile tileNew) {
		tiles.remove(tileOld.getKey());
		tiles.put(tileNew.getKey(), tileNew);
	}
	
	/**This will find a useless tile and swap a new tile with the useless one*/
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
	
	/**This will queue new tiles to be rendered.*/
	public void checkForNewTiles() {
		if(!Main.buffered) return;
		List<Tile> newTiles = getNewTiles();
		for(Tile tile : newTiles) queueTile(tile, TaskPriority.HIGHEST, true, true);
	}
	
	/**This will check if there are any new tiles that should be rendered*/
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
	
	/**This will pan the transform of which the tiles are drawn*/
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
	
	/**This will reset the transform of the tiles to a new AffineTransform*/
	public void resetTileTransform() {
		tileTransform = new AffineTransform();
		blockNextPan = true;
	}
	
}
