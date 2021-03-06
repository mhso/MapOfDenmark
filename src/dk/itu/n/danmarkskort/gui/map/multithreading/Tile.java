package dk.itu.n.danmarkskort.gui.map.multithreading;

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
	
	/** This is a tile.<br>
	A tile represents a map region as an image.<br>
	It is initialized with the tile-coordinate.<br>
	This is the position of the tile, not the map coordinate. */
	public Tile(Point pos) {
		this.pos = pos;
		isRendered = false;
		image = new BufferedImage(
				Main.tileController.getTileWidth(), 
				Main.tileController.getTileHeight(), 
				BufferedImage.TYPE_INT_ARGB
		);
	}
	
	/**This will return the key of the tile, if you were to store the tiles in a map*/
	public String getKey() {
		return pos.x + "" + pos.y;
	}
	
	/**This will return the geographical region of the tile*/
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
	
	/**This will return the transform of the tile, which is the region of the tile on the map*/
	public AffineTransform getTransform() {
		AffineTransform transform = new AffineTransform();
		Util.zoomToRegion(transform, getGeographicalRegion(), Main.tileController.getTileWidth());
		return transform;
	}
	
	/**This will return the image of this tile*/
	public BufferedImage getImage() {
		return image;
	}
	
	/**This will render the tile. This is usually used with a TileRenderTask and a Queue*/
	public void render() {
		Main.map.drawMapShapesForTile(this);
		isRendered = true;
	}
	
	/**This will return true if the tile is already rendered*/
	public boolean isRendered() {
		return isRendered;
	}
	
	/**This will return the graphics of the image of this tile*/
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	/**This will draw the tile on a Graphics2D instance*/
	public void draw(Graphics2D g2d) {
		if(!isRendered()) return;
		
		Region pixelRegion = getGeographicalRegion().toPixelRegion();
		double scale = Main.tileController.getImageScale();
		double x = pixelRegion.x1;
		double y = pixelRegion.y1 + pixelRegion.getHeight();
		double width = pixelRegion.getWidth() * scale;
		double height = pixelRegion.getHeight() * scale;
		
		g2d.setTransform(new AffineTransform());
		g2d.drawImage(
				image, 
				(int)x, 
				(int)y, 
				(int)width, 
				(int)height, 
				null
		);
		
	}
	
	/**This will check if the tile is too far away from the viewport to be of any use*/
	public boolean isUseless() {
		Point viewPoint = Main.tileController.getTilePos();
		viewPoint = new Point(viewPoint.x, viewPoint.y - 1);
		return (Math.abs(pos.x - viewPoint.x) > 1 || Math.abs(pos.y - viewPoint.y) > 1);
	}
	
}
