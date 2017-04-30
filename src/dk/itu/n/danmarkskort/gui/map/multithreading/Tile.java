package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;

public class Tile {

	Point pos;
	BufferedImage image;
	boolean isRendered;
	
	public Tile(Point pos) {
		this.pos = pos;
		isRendered = false;
		image = new BufferedImage(Main.tileController.getTileWidth(), Main.tileController.getTileHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	public String getKey() {
		return pos.x + "" + pos.y;
	}
	
	public Region getGeographicalRegion() {
		Point2D zero = Main.tileController.getZero();
		double tileWidth = Main.tileController.getGeographicalTileWidth();
		double tileHeight = Main.tileController.getGeographicalTileHeight();
		
		double x1 = zero.getX() + pos.x * tileWidth;
		double y1 = zero.getY() + pos.y * tileHeight;
		double x2 = x1 + tileWidth;
		double y2 = y1 + tileHeight;
		return new Region(x1, y1, x2, y2);
	}
	
	public AffineTransform getTransform() {
		AffineTransform transform = new AffineTransform();
		Util.zoomToRegion(transform, getGeographicalRegion(), Main.tileController.getTileWidth());
		return transform;
	}
	
	public void render() {
		Main.map.drawMapShapesForTile(this);
		isRendered = true;
	}
	
	public boolean isRendered() {
		return isRendered;
	}
	
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	public void draw(Graphics2D g2d) {
		if(!isRendered()) return;
		Region pixelRegion = getGeographicalRegion().toPixelRegion();
		double scale = Main.tileController.getImageScale();
		g2d.setTransform(new AffineTransform());
		g2d.drawImage(image, (int)pixelRegion.x1, (int)(pixelRegion.y1+pixelRegion.getHeight()), (int)(pixelRegion.getWidth() * scale), (int)(pixelRegion.getHeight() * scale), null);
		
		// Debug
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
		g2d.drawRect((int)pixelRegion.x1, (int)(pixelRegion.y1+pixelRegion.getHeight()), (int)(pixelRegion.getWidth() * scale), (int)(pixelRegion.getHeight() * scale));
	}
	
	public boolean isUseless() {
		Point viewPoint = Main.tileController.getTilePos();
		viewPoint = new Point(viewPoint.x, viewPoint.y - 1);	
		return false;
	}
	
}