package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
import dk.itu.n.danmarkskort.models.Coordinate;
import dk.itu.n.danmarkskort.models.ParsedBounds;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Region;

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;

	private AffineTransform transform = new AffineTransform();
	private boolean antiAlias = true;
	private int tileCount = 0;
	private final int MAX_ZOOM = 20;

	public MapCanvas() {
		new MapMouseController(this);
	}

	protected void paintComponent(Graphics _g) {
		drawMap((Graphics2D)_g);
	}

	public AffineTransform getTransform() {
		return transform;
	}
	
	public void drawMap(Graphics2D g2d) {
		if(!Main.lightweight) {
			drawMapLegacy(g2d);
			return;
		}
		
		g2d.setTransform(transform);
		g2d.draw(new Line2D.Double(0, 0, Util.BOUNDS_DENMARK.minLong, Util.BOUNDS_DENMARK.minLat));
		drawMapRegion(g2d);
		
	}

	public List<Shape> getShapesInsideScreen() {
		List<WaytypeGraphicSpec> wayTypeSpecs = GraphicRepresentation.getGraphicSpecs(Math.min(20, (int)getZoom()));
		if(wayTypeSpecs == null) return new ArrayList<Shape>();
		return null;
	}
	
	public void drawMapLegacy(Graphics2D g2d) {
		g2d.setTransform(transform);
		
		List<WaytypeGraphicSpec> graphicSpecs = GraphicRepresentation.getGraphicSpecs(20);
		for(WaytypeGraphicSpec wgs : graphicSpecs) {
	
			List<ParsedWay> ways = Main.tileController.getWaysOfType(wgs.getWayType());
			if(wgs.getWayType() == null) continue;
			
			for(ParsedWay way : ways) {
				Shape shape = way.getShape();
				wgs.transformOutline(g2d);
				if(wgs instanceof GraphicSpecLine) g2d.draw(shape);
				else if(wgs instanceof GraphicSpecArea) g2d.fill(shape);
			}
			
			for(ParsedWay way : ways) {
				Shape shape = way.getShape();	
				wgs.transformPrimary(g2d);
				if(wgs instanceof GraphicSpecLine) g2d.draw(shape);
				else if(wgs instanceof GraphicSpecArea) g2d.fill(shape);
			}
		}
	}
	
	public void drawMapRegion(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
		Main.log(Util.BOUNDS_DENMARK);
		Shape mapRegion = new Rectangle2D.Double(Util.BOUNDS_DENMARK.minLong, Util.BOUNDS_DENMARK.minLat, Util.BOUNDS_DENMARK.getWidth(), -Util.BOUNDS_DENMARK.getHeight());
		g2d.draw(mapRegion);
	}
	
	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		repaint();
	}

	public void update(Observable o, Object arg) {
		repaint();
	}

	public Region getGeographicalRegion() {
		if(!Main.lightweight) {
			ParsedBounds denmark = Util.BOUNDS_DENMARK;
			double x1 = denmark.minLong + (-transform.getTranslateX()/getZoomRaw() / (640) * denmark.getWidth());
			double y1 = denmark.minLat +  (transform.getTranslateY()/getZoomRaw() / (480) * denmark.getHeight());
			double x2 = x1 +  (getWidth()/getZoomRaw() / (640) * denmark.getWidth());
			double y2 = y1 +  (-getHeight()/getZoomRaw() / (480) * denmark.getHeight());
			return new Region(x1, y1, x2, y2);
		} else {
			Point2D topLeft = toModelCoords(new Point2D.Double(-transform.getTranslateX(), -transform.getTranslateY()));
			Point2D bottomRight = toModelCoords(new Point2D.Double(-transform.getTranslateX() + getWidth(), -transform.getTranslateY() - getHeight()));
			return new Region(topLeft.getX(), -topLeft.getY(), bottomRight.getX(), -bottomRight.getY());
		}
	}
	
	public void zoom(double factor) {
		transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		//lockZoom();
		repaint();
	}

	public void lockZoom() {
		if(transform.getScaleX() > MAX_ZOOM) {
			double factor = (20 / getZoom());
			transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		} else if(transform.getScaleX() < 1) {
			double factor = (1 / getZoom());
			transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		}
	}
	
	public Point2D toModelCoords(Point2D screenPosition) {
		try {
			return transform.inverseTransform(screenPosition, null);
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
	
	public double getZoomRaw() {
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

	public void zoomToBounds() {
		pan(-Main.model.getMinLon(), -Main.model.getMaxLat());
		zoom(getWidth() / (Main.model.getMaxLon() - Main.model.getMinLon()));
	}
	
	public Region getDisplayedRegion() {
		return getGeographicalRegion();
	}

}