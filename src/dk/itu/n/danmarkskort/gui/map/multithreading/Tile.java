package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;

public class Tile {

	private BufferedImage image;
	private Point pos;
	private double scale;
	private boolean isRendered;
	
	public Tile(Point pos) {
		this.pos = pos;
		this.image = new BufferedImage(Main.tileController.getTileWidth(), Main.tileController.getTileHeight(), BufferedImage.TYPE_INT_ARGB);
		this.isRendered = false;
	}
	
	public boolean render() {
		if(scale != 0) return false;
		
		Main.map.drawMapShapesForTile(this);
		setIsRendered(true);
		Main.mainPanel.repaint();
		return true;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public String getKey() {
		return pos.x + "" + pos.y;
	}
	
	public void zoom(double scale) {
		this.scale *= scale;
	}
	
	public Region getGeographicalRegion() {
		Point2D zero = Main.tileController.getZero();
		Point2D p1 = Main.map.toActualScreenCoords(zero);
		Point2D p2 = Main.map.toModelCoords(new Point2D.Double(p1.getX() + Main.tileController.getTileWidth(), p1.getY() + Main.tileController.getTileHeight()));
		
		Point2D position =  Main.map.toModelCoords(new Point2D.Double(p1.getX() + Main.tileController.getTileWidth() * -pos.x, p1.getY() + Main.tileController.getTileHeight() * -pos.y));
		Point2D size = new Point2D.Double(p2.getX() - zero.getX(), p2.getY() - zero.getY());
		Region region = new Region(-position.getX(), -position.getY(), -(position.getX() + -size.getX()), -(position.getY() + -size.getY()));
		return region;
	}
	
	public boolean isRendered() {
		return isRendered;
	}
	
	public void setIsRendered(boolean isRendered) {
		this.isRendered = isRendered;
		Main.log("Finished");
	}
	
	public boolean isVisibleToViewport() {
		Region r = getGeographicalRegion();
		Region viewport = Main.map.getActualGeographicalRegion();
		return r.overlapsRegion(viewport);
	}
	
	public boolean draw(Graphics2D g2d) {
		if(isVisibleToViewport()) {
			if(isRendered()) {
				g2d.setTransform(Main.map.getPixelTransform());
				Point2D screenPos = Main.map.toActualScreenCoords(getGeographicalRegion().getPointFrom());
				g2d.drawImage(image, (int)screenPos.getX(), (int)screenPos.getY(), null);
				Region r = getGeographicalRegion();
				g2d.setColor(Color.GREEN);
				g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
				g2d.draw(new Rectangle2D.Double(r.x1, r.y1, r.getWidth(), r.getHeight()));
			} else {
				g2d.setTransform(Main.map.getActualTransform());
				Region r = getGeographicalRegion();
				g2d.setColor(Color.DARK_GRAY);
				g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
				g2d.fill(new Rectangle2D.Double(r.x1, r.y1, r.getWidth(), r.getHeight()));
			}
			return true;
		} else return false;
	}
	
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	public AffineTransform getTransform() {
		AffineTransform transform = new AffineTransform();
		Region region = getGeographicalRegion();
		Util.pan(transform, -region.x1, -region.y2);
		Util.zoom(transform, image.getWidth() / (region.x2 - region.x1));
		return transform;
	}
	
	public boolean repaint(Graphics2D g2d) {
		return draw(g2d);
	}
	
}
