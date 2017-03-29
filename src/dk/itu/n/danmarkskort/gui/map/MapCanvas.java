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
import java.awt.geom.Rectangle2D;
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
	private final int MAX_ZOOM = 200000;

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
		
//		RenderingHints hints    = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);    
//
//		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		hints.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//		hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//
//		g2d.setRenderingHints(hints);
//		
		if(!Main.lightweight && !Main.tileController.hasBounds()) return;
		g2d.setTransform(transform);
		
		if(!Main.lightweight) {
			ParsedBounds bounds = Main.tileController.getBounds();
			
			g2d.setColor(Color.RED);
			g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
			
			Point2D minPoint = Util.coordinateToScreen(bounds.minLong, bounds.minLat);
			Point2D maxPoint = Util.coordinateToScreen(bounds.maxLong, bounds.maxLat);
			double width = maxPoint.getX() - minPoint.getX();
			double height = Math.abs(maxPoint.getY() - minPoint.getY());
			
			Main.log(minPoint);
			Main.log(maxPoint);
			Main.log(width + ", " + height);
			
			g2d.draw(new Rectangle2D.Double(minPoint.getX(), minPoint.getY(), width, height));
			
			List<WaytypeGraphicSpec> graphicSpecs =
					GraphicRepresentation.getGraphicSpecs(20);
	
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
	}

	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		repaint();
	}

	public void update(Observable o, Object arg) {
		repaint();
	}

	public Region getGeographicalRegion() {
		
		ParsedBounds denmark = Util.BOUNDS_DENMARK;
		
		double x1 = denmark.minLong + (-transform.getTranslateX()/getZoomRaw() / (640) * denmark.getWidth());
		double y1 = denmark.minLat +  (transform.getTranslateY()/getZoomRaw() / (480) * denmark.getHeight());
		double x2 = x1 +  (getWidth()/getZoomRaw() / (640) * denmark.getWidth());
		double y2 = y1 +  (-getHeight()/getZoomRaw() / (480) * denmark.getHeight());
		
		
		return new Region(x1, y1, x2, y2);
		
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

	public void zoomToBounds(ParsedBounds bounds) {
		
	}
	
	public Region getDisplayedRegion() {
		return getGeographicalRegion();
	}

}