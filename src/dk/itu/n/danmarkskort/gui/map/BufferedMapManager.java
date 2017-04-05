package dk.itu.n.danmarkskort.gui.map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.lang.Thread.State;
import java.util.HashMap;

import dk.itu.n.danmarkskort.Main;

public class BufferedMapManager {

	private BufferedMapImage[] images = new BufferedMapImage[4];
	private BufferedMapImage[] oldImages = new BufferedMapImage[4];
	private BufferedMapImage[] zoomImages = new BufferedMapImage[4];
	private AffineTransform transform = new AffineTransform();
	private AffineTransform oldTransform = new AffineTransform();
	private Thread mapWorkerThread;
	private MapWorker mapWorker;
	
	public BufferedMapManager() {
		prepareWorker();
	}
	
	public boolean isRepainting() {
		return mapWorker.isRunning();
	}
	
	public void storeZoomImages() {
		Main.log("Storing zoom images");
		for(int i=0; i<4; i++) {
			zoomImages[i] = new BufferedMapImage(this, images[i].getPosition());
			zoomImages[i].setData(images[i].getData());
		}
		Main.log("Done storing data");
	}
	
	public void prepareWorker() {
		mapWorker = new MapWorker();
		mapWorkerThread = new Thread(mapWorker);
	}
	
	public void checkForUpdates() {
		if(Main.map.scaleCurrentLayer) return;
		Point viewportTile = getViewportTile();
		Point p1 = new Point(viewportTile.x, viewportTile.y);
		Point p2 = new Point(viewportTile.x+1, viewportTile.y);
		Point p3 = new Point(viewportTile.x, viewportTile.y+1);
		Point p4 = new Point(viewportTile.x+1, viewportTile.y+1);
		boolean startThread = false;
		if(!hasTile(p1)) {
			createTile(0, p1);
			startThread = true;
		}
		if(!hasTile(p2)) {
			createTile(1, p2);
			startThread = true;
		}
		if(!hasTile(p3)) {
			createTile(2, p3);
			startThread = true;
		}
		if(!hasTile(p4)) {
			createTile(3, p4);
			startThread = true;
		}
		if(startThread) {
			if(!mapWorker.isRunning()) {
				mapWorkerThread = new Thread(mapWorker);
				mapWorkerThread.start();
			}
		}
	}
	
	public void forceFullRepaint() {
		for(int i=0; i<images.length; i++) images[i] = null;
		oldTransform = transform;
		transform = new AffineTransform();
		mapWorker.clearQueue();
		checkForUpdates();
	}
	
	public void zoom(double factor) {
		transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		oldTransform = transform;
	}
	
	public boolean hasTile(Point point) {
		for(BufferedMapImage image : images) {
			if(image == null) continue;
			if(image.getPosition().x == point.x && image.getPosition().y == point.y) return true;
		}
		return false;
	}
	
	public Point getViewportTile() {
		int x = -(int)Math.floor((transform.getTranslateX() / Main.map.getWidth())) - 1;
		int y = -(int)Math.floor((transform.getTranslateY() / Main.map.getHeight())) - 1;
		return new Point(x, y);
	}
	
	public BufferedMapImage createTile(int index, Point point) {
		BufferedMapImage image = new BufferedMapImage(this, point);
	    mapWorker.addToQueue(image);
	    if(images[index] != null && !Main.map.scaleCurrentLayer) oldImages[index] = images[index];
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
	
	public void drawOldImages(Graphics2D g) {
		g.setTransform(oldTransform);
		for(BufferedMapImage image : oldImages) {
			if(image != null) image.draw(g);
		}
	}
	
	public void drawZoomImages(Graphics2D g) {
		g.setTransform(transform);
		for(BufferedMapImage image : zoomImages) {
			if(image != null) image.draw(g);
		}
	}
	
	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		oldTransform = transform;
		checkForUpdates();
	}
	
}
