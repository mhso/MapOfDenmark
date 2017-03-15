package dk.itu.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;
	
	private AffineTransform transform = new AffineTransform();
	private boolean antiAlias;
	private int tileCount = 0;
	private final int MAX_ZOOM = 20;
	
	public MapCanvas() {
		new MapMouseController(this);
	}
	
	protected void paintComponent(Graphics _g) {
		Graphics2D g = (Graphics2D) _g;
		g.setTransform(transform);
		g.setStroke(new BasicStroke(Float.MIN_VALUE));
		if (antiAlias) g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawMap(g);
	}
	
	public void drawMap(Graphics2D g2d) {
		
		int boxSize = 16;
		
		Region mapRegion = getDisplayedRegion();
		double x1 = Math.max(0, mapRegion.getPointFrom().getX());
		double y1 = Math.max(0, mapRegion.getPointFrom().getY());
		double x2 = Math.min(640, mapRegion.getPointTo().getX());
		double y2 = Math.min(480, mapRegion.getPointTo().getY());
		
		tileCount = 0;
		for(double x=x1; x<x2; x+=boxSize) {
			for(double y=y1; y<y2; y+=boxSize) {
				int r = (int)(Util.roundByN(50, 200 / getZoom()));
				int g = (int)(200);
				int b = (int)(Util.roundByN(50, 200 / getZoom()));
				g2d.setColor(new Color(r, g, b));
				g2d.fillRect((int)(x - x1 % 16), (int)(y - y1 % 16), boxSize, boxSize);
				g2d.setColor(Color.WHITE);
				g2d.drawRect((int)(x - x1 % 16), (int)(y - y1 % 16), boxSize, boxSize);
				
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
