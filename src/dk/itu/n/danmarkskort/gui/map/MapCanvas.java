package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
import dk.itu.n.danmarkskort.models.ParsedBounds;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.models.WayType;

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;

	private AffineTransform transform = new AffineTransform();
	private boolean antiAlias = true;
	public int shapesDrawn = 0;
	private final int MAX_ZOOM = 20;
	private boolean resetDrawing;

	private WayType currentType;
	private WaytypeGraphicSpec currentWTGSpec;
	private boolean outline;
	private Graphics2D currentGraphics;
	
	private BufferedMapManager imageManager = null;
	private Point2D zero;
	
	public MapCanvas() {
		new MapMouseController(this);
		setDoubleBuffered(true);
	}

	protected void paintComponent(Graphics _g) {
		if(!resetDrawing) drawMap((Graphics2D)_g);
	}

	public AffineTransform getTransform() {
		return transform;
	}
	
	public void drawMap(Graphics2D g2d) {
		if(!Main.lightweight) {
			drawMapLegacy(g2d);
			return;
		}
		
		if(Main.buffered) {
			if(imageManager != null) imageManager.draw(g2d);
		} else {
			drawMapShapes(g2d);
		}
		
	}
	
	public void drawMapShapes(Graphics2D g2d) {
		g2d.setTransform(transform);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawMapRegion(g2d);
		List<WaytypeGraphicSpec> wayTypesVisible = getOnScreenGraphicsForCurrentZoom();
		shapesDrawn = 0;
		currentGraphics = g2d;

		// drawing all the outlines, if the current WayTypeGraphicSpec has one
		for (WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
			currentWTGSpec = wayTypeGraphic;
			if(currentWTGSpec.getOuterColor() != null) {
				KDTree kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
				if (kdTree == null) {
					continue;
				}
				outline = true;
				kdTree.getShapes(getGeographicalRegion(), this);
			}
		}

		// draw or fill for all the different WaytypeGraphicsSpecs
		for(WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
			currentWTGSpec = wayTypeGraphic;
			KDTree kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
			if(kdTree == null) {
				continue;
			}
			outline = false;
			kdTree.getShapes(getGeographicalRegion(), this);
		}
	}

	public void drawShapes(Shape[] shapes) {
		if(outline) {
			for(Shape shape : shapes) {
				currentWTGSpec.transformOutline(currentGraphics);
				if (currentWTGSpec instanceof GraphicSpecLine) currentGraphics.draw(shape);
				else if (currentWTGSpec instanceof GraphicSpecArea) currentGraphics.fill(shape);
			}
		} else {
			for(Shape shape : shapes) {
				currentWTGSpec.transformPrimary(currentGraphics);
				if (currentWTGSpec instanceof GraphicSpecLine) currentGraphics.draw(shape);
				else if (currentWTGSpec instanceof GraphicSpecArea) currentGraphics.fill(shape);
				shapesDrawn++;
			}
		}
	}

	public List<WaytypeGraphicSpec> getOnScreenGraphicsForCurrentZoom() {
		List<WaytypeGraphicSpec> wayTypeSpecs = GraphicRepresentation.getGraphicSpecs((int)getZoom());
		if(wayTypeSpecs == null) return new ArrayList<WaytypeGraphicSpec>();
		return wayTypeSpecs;
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
		Region mapRegion = Main.model.getMapRegion();
		g2d.draw(new Line2D.Double(0, 0, mapRegion.x1, mapRegion.y1));
		g2d.draw(new Rectangle2D.Double(mapRegion.x1, mapRegion.y1, mapRegion.getWidth(), mapRegion.getHeight()));
	}
	
	public void eraseMap(boolean erase) {
		resetDrawing = erase;
		if(resetDrawing) {
			Graphics g = getGraphics();
			Color oldColor = g.getColor();
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(oldColor);
		}
	}
	
	public void pan(double dx, double dy) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		if(Main.buffered && imageManager != null) imageManager.pan(dx, dy);
		repaint();
	}
	
	public void purePan(double dx, double dy) { //Only pans map temporary without automatic repaint.
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
	}
	
	public void purePanToTile(Point tilePosition) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(-transform.getTranslateX(), -transform.getTranslateY()));
		transform.preConcatenate(AffineTransform.getTranslateInstance(zero.getX() + -tilePosition.getX() * getWidth(), zero.getY() + -tilePosition.getY() * getHeight()));
	}
	
	public void purePanToPosition(Point2D position) {
		transform.preConcatenate(AffineTransform.getTranslateInstance(-transform.getTranslateX(), -transform.getTranslateY()));
		transform.preConcatenate(AffineTransform.getTranslateInstance(position.getX(), position.getY()));
	}
	
	public Point2D getCurrentPan() {
		return new Point2D.Double(transform.getTranslateX(), transform.getTranslateY());
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
			Point2D topLeft = toModelCoords(new Point2D.Double(0, 0));
			Point2D bottomRight = toModelCoords(new Point2D.Double(getWidth(), getHeight()));
			return new Region(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY());
		}
	}
	
	public void zoom(double factor) {	
		double scaleBefore = getZoomRaw();
		transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
		double scaleAfter = getZoomRaw();
		if(getZoom() > MAX_ZOOM) {
			transform.preConcatenate(AffineTransform.getScaleInstance(scaleBefore/scaleAfter, scaleBefore/scaleAfter));
		}
		else if(getZoom() < 1) {
			transform.preConcatenate(AffineTransform.getScaleInstance(scaleBefore/scaleAfter, scaleBefore/scaleAfter));
		}
		
		if(Main.buffered && imageManager != null) {
			zero = new Point2D.Double(transform.getTranslateX(), transform.getTranslateY());
			imageManager.forceFullRepaint();
		}
		repaint();
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
		ParsedBounds denmark = Util.BOUNDS_DENMARK;
		double denmarkWidth = denmark.maxLong - denmark.minLong;
		Region view = getGeographicalRegion();
		double zoom = Math.floor(Math.log(denmarkWidth/view.getWidth())*2.5);
		return zoom;
	}
	
	public double getZoomRaw() {
		return transform.getScaleX();
	}

	public Point2D getZero() {
		return zero;
	}
	
	public void zoomToBounds() {
		Region mapRegion = Main.model.getMapRegion();
		purePan(-mapRegion.x1, -mapRegion.y2);
		zoom(getWidth() / (mapRegion.x2 - mapRegion.x1));
		if(Main.buffered) {
			zero = new Point2D.Double(transform.getTranslateX(), transform.getTranslateY());
			imageManager = new BufferedMapManager();	
		}
	}

}