package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.ParsedBounds;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.models.Tile;
import dk.itu.n.danmarkskort.models.TileCoordinate;
import dk.itu.n.danmarkskort.models.WayType;

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;
	
	private AffineTransform transform = new AffineTransform();
	private boolean antiAlias = true;
	private int tileCount = 0;
	private final int MAX_ZOOM = 20;
	
	private boolean did = false;
	
	public MapCanvas() {
		new MapMouseController(this);
	}
	
	protected void paintComponent(Graphics _g) {
		drawMap((Graphics2D)_g);
	}
	
	public void drawMap(Graphics2D g2d) {
		if(!Main.tileController.hasBounds()) return;
		g2d.setTransform(transform);
		g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
		
		for(ParsedWay way : Main.tileController.wayList) {
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
			Shape shape = way.getShape();
			if(way.type == WayType.DEFAULT) g2d.draw(shape);
			else if(way.type == WayType.BUILDING) g2d.fill(shape);
			else if(way.type == WayType.COASTLINE) {
				g2d.setStroke(new BasicStroke(0.2F));
				g2d.draw(shape);
			} else if(way.type == WayType.HIGHWAY) {
				g2d.setStroke(new BasicStroke(0.5F));
				g2d.draw(shape);
			}
		}
		
		for(ParsedWay way : Main.tileController.wayList) {
			Shape shape = way.getShape();
			if(way.type == WayType.HIGHWAY) {
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke(0.4F));
				g2d.draw(shape);
				g2d.setColor(Color.BLACK);
			}
		}
		
		int boxSize = 16;
		
		ParsedBounds bounds = Main.tileController.getBounds();
		int horTileLimit = bounds.getHorizontalTileCount() * boxSize;
		int verTileLimit = bounds.getVerticalTileCount() * boxSize;
		
		Region mapRegion = getDisplayedRegion();
		double x1 = Math.max(0, mapRegion.getPointFrom().getX());
		double y1 = Math.max(0, mapRegion.getPointFrom().getY());
		double x2 = Math.min(horTileLimit, mapRegion.getPointTo().getX());
		double y2 = Math.min(verTileLimit, mapRegion.getPointTo().getY());
		
		tileCount = 0;
		Tile tile = new Tile(new TileCoordinate(1, 1), 1);
		tile.render();
		
		for(double x=x1; x<x2; x+=boxSize) {
			for(double y=y1; y<y2; y+=boxSize) {
				int xPos = (int)(x - x1 % 16);
				int yPos = (int)(y - y1 % 16);
				g2d.drawImage(tile.getImage(), xPos, yPos, null);
				tileCount++;
			}
		}
	}

	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		repaint();
	}

	public void update(Observable o, Object arg) {
		repaint();
	}

	public void zoom(double factor) {
		transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		if(transform.getScaleX() > MAX_ZOOM) {
			factor = (20 / getZoom());
			transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		} else if(transform.getScaleX() < 1) {
			factor = (1 / getZoom());
			transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		}
		repaint();
	}

	public Point2D toModelCoords(Point2D lastMousePosition) {
		try {
			return transform.inverseTransform(lastMousePosition, null);
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException(e);
		}
	}

	public void toggleAA() {
		antiAlias = !antiAlias;
		repaint();
	}

	public double getZoom() {
		return transform.getScaleX();
	}
	
	public double getMapX() {
		return (transform.getTranslateX() / getZoom()) * -1;
	}
	
	public double getMapY() {
		return (transform.getTranslateY() / getZoom()) * -1;
	}
	
	public int getTileDrawnCount() {
		return tileCount;
	}
	
	public Region getDisplayedRegion() {
		double o = -200 / getZoom();
		double width = getPreferredSize().getWidth();
		double height = getPreferredSize().getHeight();
		Region r = new Region(getMapX() + o, getMapY() + o, getMapX() + width/getZoom() - o, getMapY() + height/getZoom() - o);
		return r;
	}
	
}
