package dk.itu.n.danmarkskort.gui.map;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.HashMap;

import dk.itu.n.danmarkskort.Main;

public class BufferedMapManager {

	private BufferedMapImage[] images = new BufferedMapImage[4];
	private AffineTransform transform = new AffineTransform();
	private HashMap<String, Thread> workers = new HashMap<String, Thread>();
	
	public void checkForUpdates() {
		Point viewportTile = getViewportTile();
		Point p1 = new Point(viewportTile.x, viewportTile.y);
		Point p2 = new Point(viewportTile.x+1, viewportTile.y);
		Point p3 = new Point(viewportTile.x, viewportTile.y+1);
		Point p4 = new Point(viewportTile.x+1, viewportTile.y+1);
		if(!hasTile(p1)) createTile(0, p1);
		if(!hasTile(p2)) createTile(1, p2);
		if(!hasTile(p3)) createTile(2, p3);
		if(!hasTile(p4)) createTile(3, p4);
	}
	
	public void forceFullRepaint() {
		for(int i=0; i<images.length; i++) images[i] = null;
		transform = new AffineTransform();
		checkForUpdates();
	}
	
	public boolean hasTile(Point point) {
		for(BufferedMapImage image : images) {
			if(image == null) continue;
			if(image.getPosition().x == point.x && image.getPosition().y == point.y) return true;
		}
		return false;
	}
	
	public void spawnWorker(int index, Point point) {
		String key = point.x + "" + point.y;
		if(workers.containsKey(key)) return;
		MapWorker worker = new MapWorker(this, index, point);
		Thread thread = new Thread(worker);
		workers.put(key, thread);
		thread.start();
	}
	
	public void onWorkerFinished(MapWorker worker, int index, BufferedMapImage image) {
		images[index] = image;
		Thread thread = workers.get(image.getKey());
		thread.interrupt();
		workers.remove(image.getKey());
	}
	
	public Point getViewportTile() {
		int x = -(int)Math.floor((transform.getTranslateX() / Main.map.getWidth())) - 1;
		int y = -(int)Math.floor((transform.getTranslateY() / Main.map.getHeight())) - 1;
		return new Point(x, y);
	}
	
	public BufferedMapImage createTile(int index, Point point) {
		BufferedMapImage image = new BufferedMapImage(this, point);
	    		image.render();

		images[index] = image;
		return image;
		
	}
	
	public boolean didRenderTiles() {
		return images[0] != null;
	}
	
	public void draw(Graphics2D g) {
		if(!didRenderTiles()) checkForUpdates();
		g.setTransform(transform);
		for(BufferedMapImage image : images) {
			if(image != null) image.draw(g);
		}
	}
	
	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		checkForUpdates();
	}
	
}
