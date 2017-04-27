package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.Timer;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.map.multithreading.Tile;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.models.WayType;

public class MapCanvas extends JPanel {

	private AffineTransform transform = new AffineTransform();
	private AffineTransform actualTransform = new AffineTransform();
	private boolean antiAlias = true;
	public int shapesDrawn = 0;
	private final int MAX_ZOOM = 20;

	private WaytypeGraphicSpec currentWTGSpec;
	private boolean zoomChanged;
	private boolean isInitialized = false;
	
	private List<CanvasListener> listeners = new ArrayList<>();
	private List<WaytypeGraphicSpec> wayTypesVisible;
	
	public boolean scaleCurrentLayer = false;
	
	public MapCanvas() {
		new MapMouseController(this);
		setDoubleBuffered(true);
	}

	public void repair() {
		transform = (AffineTransform) actualTransform.clone();
	}
	
	protected void paintComponent(Graphics _g) {
		_g.clearRect(0, 0, getWidth(), getHeight());
		drawMap((Graphics2D)_g);
	}
	
	public void addCanvasListener(CanvasListener l) {
		listeners.add(l);
	}

	public AffineTransform getTransform() {
		return transform;
	}
	
	public AffineTransform getActualTransform() {
		return actualTransform;
	}
	
	public void drawMap(Graphics2D g2d) {
		if(Main.buffered) {
			Main.tileController.draw(g2d);
		} else {
			drawMapShapes(g2d);
		}
		if(Main.pinPointManager != null) {
			Main.pinPointManager.drawPinPoints(g2d);
			Main.pinPointManager.drawSystemPinPoints(g2d);
		}
	}
	
	public void repaintPinPoints() {
		repaint();
	}
	
	public void drawMapShapes(Graphics2D g2d) {
		g2d.setTransform(transform);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawMapRegion(g2d);
		if(zoomChanged) wayTypesVisible = getOnScreenGraphicsForCurrentZoom();
		shapesDrawn = 0;

		if(wayTypesVisible == null) return;
        //drawBackground(g2d);

        Region currentRegion = getGeographicalRegion();

        // drawing all the outlines, if the current WayTypeGraphicSpec has one
        for (WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
            currentWTGSpec = wayTypeGraphic;
            KDTree<ParsedItem> kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
            if (kdTree == null) continue;
            //if (currentWTGSpec.getOuterColor() != null) {
            if (currentWTGSpec instanceof GraphicSpecLine) {
                currentWTGSpec.transformOutline(g2d);
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            } else if (currentWTGSpec instanceof GraphicSpecArea) {
                currentWTGSpec.transformPrimary(g2d);
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.fill(item.getShape());
                    shapesDrawn++;
                }
            }
        }

        // draw or fill for all the different WaytypeGraphicsSpecs
        for(WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
            currentWTGSpec = wayTypeGraphic;
            if (currentWTGSpec instanceof GraphicSpecLine) {
                currentWTGSpec.transformPrimary(g2d);
                KDTree<ParsedItem> kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
                if (kdTree == null) continue;
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            }
        }
    }
	
	public void drawMapShapesForTile(Tile tile) {
		if(tile.isRendered()) return;
		Graphics2D g2d = (Graphics2D)tile.getGraphics();
		
		Region tileRegion = tile.getGeographicalRegion();
		Region currentRegion = new Region(tileRegion.x1, tileRegion.y1 + tileRegion.getHeight(), tileRegion.x2, tileRegion.y2 + tileRegion.getHeight());
		
		g2d.setTransform(tile.getTransform());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawMapRegion(g2d);
		if(zoomChanged) wayTypesVisible = getOnScreenGraphicsForCurrentZoom();
		shapesDrawn = 0;

		if(wayTypesVisible == null) return;
        drawBackground(g2d);

        // drawing all the outlines, if the current WayTypeGraphicSpec has one
        for (WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
            currentWTGSpec = wayTypeGraphic;
            KDTree<ParsedItem> kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
            if (kdTree == null) continue;
            //if (currentWTGSpec.getOuterColor() != null) {
            if (currentWTGSpec instanceof GraphicSpecLine) {
                currentWTGSpec.transformOutline(g2d);
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            } else if (currentWTGSpec instanceof GraphicSpecArea) {
                currentWTGSpec.transformPrimary(g2d);
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.fill(item.getShape());
                    shapesDrawn++;
                }
            }
        }

        // draw or fill for all the different WaytypeGraphicsSpecs
        for(WaytypeGraphicSpec wayTypeGraphic : wayTypesVisible) {
            currentWTGSpec = wayTypeGraphic;
            if (currentWTGSpec instanceof GraphicSpecLine) {
                currentWTGSpec.transformPrimary(g2d);
                KDTree<ParsedItem> kdTree = Main.model.enumMapKD.get(wayTypeGraphic.getWayType());
                if (kdTree == null) continue;
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            }
        }
    }

	// This method is probably not the best. We should "just" change the entire background for et mapcanvas instead, if possible
	private void drawBackground(Graphics2D g2d) {
        Region region = Main.model.getMapRegion();
        Path2D background = new Path2D.Double();
        background.moveTo(region.x1, region.y1);
        background.lineTo(region.x2, region.y1);
        background.lineTo(region.x2, region.y2);
        background.lineTo(region.x1, region.y2);
        background.lineTo(region.x1, region.y1);

        // backgroundcolor for the map. If there's a coastline use the water innercolor, otherwise use the coastline innercolor
        if(Main.model.enumMapKD.containsKey(WayType.COASTLINE) ){//&& Main.model.enumMapKD.get(WayType.COASTLINE).size() > 0) {
        	g2d.setColor(new Color(110, 192, 255));
        } else g2d.setColor(new Color(240, 240, 230));

        g2d.fill(background);
    }

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
		repair();
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		actualTransform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		if(Main.tileController.isInitialized()) {
			if(Main.tileController.updateTilePos()) {
				Main.tileController.checkForNewTiles();
			}
		}
		repaint();
	}
	
	public void purePan(double dx, double dy) { //Only pans map temporary without automatic repaint.
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
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
	
	public Region getActualGeographicalRegion() {
		Point2D topLeft = toActualModelCoords(new Point2D.Double(0, 0));
		Point2D bottomRight = toActualModelCoords(new Point2D.Double(getWidth(), getHeight()));
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
	
	public void mouseMoved() {
		for(CanvasListener listener : listeners) listener.onMouseMoved();
	}
	
	public void zoom(double factor) {
		
		double zoomBefore = getZoom();
		double scaleBefore = getZoomRaw();
		Util.zoom(transform, factor);
		Util.zoom(actualTransform, factor);
		if(Main.tileController.isInitialized()) Main.tileController.zoom(factor);
		double scaleAfter = getZoomRaw();
		if(getZoom() > MAX_ZOOM || getZoom() < 1) {
			Util.zoom(transform, scaleBefore/scaleAfter);
			Util.zoom(transform, scaleBefore/scaleAfter);		
			if(Main.tileController.isInitialized()) Main.tileController.zoom(scaleBefore/scaleAfter);
		}
		
		
		for(CanvasListener listener : listeners) listener.onZoom();
		
		if(zoomBefore != getZoom()) {
			
			zoomChanged = true;
			for(CanvasListener listener : listeners) listener.onZoomLevelChanged();
		}
		
		

		repaint();
	}
	
	public void snapToZoom(int zoomValue) {
		double factor = 0.0;
		int amount = 0;
		if(zoomValue > getZoom()) {
			factor = 1.5;
			amount = (int)(zoomValue-getZoom());
		}
		else {
			factor = 0.667;
			amount = (int)(getZoom()-zoomValue);
		}
		for(int i = 0; i < amount; i++) zoom(factor);
	}
	
	public Point2D toModelCoords(Point2D relativeToMapCanvasPosition) {
		try {
			return transform.inverseTransform(relativeToMapCanvasPosition, null);
		} catch (NoninvertibleTransformException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Point2D toActualModelCoords(Point2D relativeToMapCanvasPosition) {
		try {
			return actualTransform.inverseTransform(relativeToMapCanvasPosition, null);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Point2D toScreenCoords(Point2D coordinates) {
		return transform.transform(coordinates, null);
	}
	
	public Point2D toActualScreenCoords(Point2D coordinates) {
		return actualTransform.transform(coordinates, null);
	}
	
	public void toggleAA() {
		antiAlias = !antiAlias;
		repaint();
	}

	public double getZoom() {
		Region denmark = DKConstants.BOUNDS_DENMARK;
		double denmarkWidth = denmark.getWidth();
		Region view = getGeographicalRegion();
		return Math.floor(Math.log(denmarkWidth/view.getWidth())*2.5);
	}
	
	public double getZoomRaw() {
		return transform.getScaleX();
	}
	
	public void zoomToBounds() {
		Region mapRegion = Main.model.getMapRegion();
		pan(-mapRegion.x1, -mapRegion.y2);
		zoom(getWidth() / (mapRegion.x2 - mapRegion.x1));
		if(!Main.tileController.isInitialized) Main.tileController.initialize();
		isInitialized = true;
	}
	
	public void setupDone() {	
		zoomToBounds();
		for(CanvasListener listener : listeners) listener.onSetupDone();
	}
	
	public void forceRepaint() {
		
	}

}