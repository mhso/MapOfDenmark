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
		
		return true;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public String getKey() {
		return pos.x + "" + pos.y;
	}
	
	public void zoom(double scale) {
		//this.scale *= scale;
	}
	
	public Region getGeographicalRegion() {
		
		double scale = Main.tileController.getImageTransform().getScaleX();
		
		Point2D zero = Main.tileController.getZero();
		Point2D p1 = Main.map.toActualScreenCoords(zero);
		Point2D p2 = Main.map.toModelCoords(new Point2D.Double(p1.getX() + Main.tileController.getTileWidth() * scale, p1.getY() + Main.tileController.getTileHeight() * scale));
		
		Point2D position = Main.map.toModelCoords(new Point2D.Double(p1.getX() + (Main.tileController.getTileWidth()) * scale * -pos.x, p1.getY() + (Main.tileController.getTileHeight() * scale) * -(pos.y-1)));
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
		//if(isVisibleToViewport()) {
			if(isRendered()) {
				Region r = getGeographicalRegion();
				Point2D pos1 = Main.map.toActualScreenCoords(new Point2D.Double(r.x1,  r.y1 + r.getHeight()));
				Point2D pos2 = Main.map.toActualScreenCoords(new Point2D.Double(r.x1 + r.getWidth(), r.y1 + r.getHeight()*2));
				int width  = (int)(pos2.getX() - pos1.getX());
				int height = (int)(pos2.getY() - pos1.getY());
				Main.log("Width: " + width);
				Main.log("Height: " + height);
				Main.log("X: " + pos1.getX());
				Main.log("Y: " + pos1.getY());
				g2d.setTransform(Util.zeroTransform);
				g2d.drawImage(image, (int)pos1.getX(), (int)pos1.getY(), width, height, null);
				
				g2d.setTransform(Main.map.getActualTransform());
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
				g2d.draw(new Rectangle2D.Double(r.x1, r.y1 + r.getHeight(), r.getWidth(), r.getHeight()*2));
			} else {
				g2d.setTransform(Main.map.getActualTransform());
				Region r = getGeographicalRegion();
				g2d.setColor(Color.DARK_GRAY);
				g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
				g2d.fill(new Rectangle2D.Double(r.x1,  r.y1 + r.getHeight(), r.getWidth(), r.getHeight()*2));
			}
			return true;
		//} else return false;
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
