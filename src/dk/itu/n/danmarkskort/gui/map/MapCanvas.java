package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
import dk.itu.n.danmarkskort.parsedmodels.ParsedBounds;
import dk.itu.n.danmarkskort.parsedmodels.Region;

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;

	private AffineTransform transform = new AffineTransform();
	private boolean antiAlias = true;
	public int shapesDrawn = 0;
	private final int MAX_ZOOM = 20;

	private WaytypeGraphicSpec currentWTGSpec;
	private boolean outline;
	private boolean zoomChanged;
	private Graphics2D currentGraphics;
	
	private BufferedMapManager imageManager = null;
	private Point2D zero;

	private List<CanvasListener> listeners = new ArrayList<>();
	private List<WaytypeGraphicSpec> wayTypesVisible;
	private boolean repaintPinPointsOnly = false;
	
	public MapCanvas() {
		new MapMouseController(this);
		setDoubleBuffered(true);
	}

	protected void paintComponent(Graphics _g) {
		_g.clearRect(0, 0, getWidth(), getHeight());
		drawMap((Graphics2D)_g);
	}
	
	public void addCanvasListener(CanvasListener l) {
		listeners.add(l);
	}
	
	public void forceRepaint() {
		zoomChanged = true;
		repaint();
	}

	public AffineTransform getTransform() {
		return transform;
	}
	
	public void drawMap(Graphics2D g2d) {
		if(repaintPinPointsOnly) {
			if(Main.pinPointManager != null) Main.pinPointManager.drawPinPoints(g2d);
			repaintPinPointsOnly = false;
			return;
		}
		if(Main.buffered) {
			if(imageManager != null) imageManager.draw(g2d);
		} else {
			drawMapShapes(g2d);
		}
		if(Main.pinPointManager != null) {
			Main.pinPointManager.drawPinPoints(g2d);
			Main.pinPointManager.drawSystemPinPoints(g2d);
		}
	}
	
	public void repaintPinPoints() {
		repaintPinPointsOnly = true;
		repaint();
	}
	
	public void drawMapShapes(Graphics2D g2d) {
		g2d.setTransform(transform);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawMapRegion(g2d);
		if(zoomChanged) wayTypesVisible = getOnScreenGraphicsForCurrentZoom();
		shapesDrawn = 0;
		currentGraphics = g2d;

		if(wayTypesVisible == null) return;

        //drawBackground(g2d);

        // drawing all the outlines, if the current WayTypeGraphicSpec has one
		for (WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
			currentWTGSpec = wayTypeGraphic;
			if (currentWTGSpec.getOuterColor() != null) {
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
/*
	private void drawBackground(Graphics2D g2d) {
        Region region = Main.model.getMapRegion();
        Path2D background = new Path2D.Double();
        background.moveTo(region.x1, region.y1);
        background.lineTo(region.x2, region.y1);
        background.lineTo(region.x2, region.y2);
        background.lineTo(region.x1, region.y2);
        background.lineTo(region.x1, region.y1);

        // backgroundcolor for the map. If there's a coastline use the water innercolor, otherwise use the coastline innercolor
        if(Main.model.enumMapKD.containsKey(WayType.COASTLINE) && Main.model.enumMapKD.get(WayType.COASTLINE).size() > 0) {
            g2d.setColor(new Color(110, 192, 255));
        }
        else g2d.setColor(new Color(240, 240, 230));

        g2d.fill(background);
    }*/

	public List<WaytypeGraphicSpec> getOnScreenGraphicsForCurrentZoom() {
		List<WaytypeGraphicSpec> wayTypeSpecs = GraphicRepresentation.getGraphicSpecs((int)getZoom());
		zoomChanged = false;
		if(wayTypeSpecs == null) return new ArrayList<WaytypeGraphicSpec>();
		return wayTypeSpecs;
	}
	
	public void drawMapRegion(Graphics2D g2d) {
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
		Region mapRegion = Main.model.getMapRegion();
		g2d.draw(new Line2D.Double(0, 0, mapRegion.x1, mapRegion.y1));
		g2d.draw(new Rectangle2D.Double(mapRegion.x1, mapRegion.y1, mapRegion.getWidth(), mapRegion.getHeight()));
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
	
	public void panToPosition(Point2D position) {
		Point2D middleGeo = this.getGeographicalMiddleOfView();
		Point2D screenGeo = this.toScreenCoords(middleGeo);
		Point2D screenTarget = this.toScreenCoords(position);
		pan(screenGeo.getX() - screenTarget.getX(), screenGeo.getY() - screenTarget.getY());
		repaint();
	}
	
	public Point2D getGeographicalMiddleOfView() {
		Region region = this.getGeographicalRegion();
		return new Point2D.Double(region.x1 + region.getWidth() / 2, region.y1 + region.getHeight() / 2);
	}
	
	public Point2D getCurrentPan() {
		return new Point2D.Double(transform.getTranslateX(), transform.getTranslateY());
	}

	public void update(Observable o, Object arg) {
		repaint();
	}

	public Region getGeographicalRegion() {
		Point2D topLeft = toModelCoords(new Point2D.Double(0, 0));
		Point2D bottomRight = toModelCoords(new Point2D.Double(getWidth(), getHeight()));
		return new Region(topLeft.getX(), topLeft.getY(), bottomRight.getX(), bottomRight.getY());
	}
	
	public Point2D getRelativeMousePosition() {
		Point mousePositionScreen = MouseInfo.getPointerInfo().getLocation();
		Point mapCanvasPosition = getLocationOnScreen();
		return new Point(mousePositionScreen.x - mapCanvasPosition.x, mousePositionScreen.y - mapCanvasPosition.y);
	}
	
	public Point2D getGeographicalMousePosition() {
		return toModelCoords(getRelativeMousePosition());
	}
	
	public void zoom(double factor) {
		double zoomBefore = getZoom();
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
		for(CanvasListener listener : listeners) listener.onZoom();
		if(zoomBefore != getZoom()) {
			zoomChanged = true;
			for(CanvasListener listener : listeners) listener.onZoomLevelChanged();
		}
		repaint();
	}
	
	public Point2D toModelCoords(Point2D relativeToMapCanvasPosition) {
		try {
			return transform.inverseTransform(relativeToMapCanvasPosition, null);
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Point2D toScreenCoords(Point2D coordinates) {
		return transform.transform(coordinates, null);
	}
	
	public void toggleAA() {
		antiAlias = !antiAlias;
		repaint();
	}

	public double getZoom() {
		ParsedBounds denmark = DKConstants.BOUNDS_DENMARK;
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