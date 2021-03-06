package dk.itu.n.danmarkskort.gui.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import javax.swing.*;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.map.multithreading.Tile;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLabel;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedPlace;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.models.WayType;
import dk.itu.n.danmarkskort.routeplanner.RouteEdge;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;

public class MapCanvas extends JPanel {

	private AffineTransform transform = new AffineTransform();
	private AffineTransform actualTransform = new AffineTransform();
	private boolean antiAlias = true;
	public int shapesDrawn = 0;
	public static final int MAX_ZOOM = 20;

	private WaytypeGraphicSpec currentWTGSpec;
	private boolean zoomChanged = true;
	
	private List<CanvasListener> listeners = new ArrayList<>();
	private List<WaytypeGraphicSpec> wayTypesVisible;
	private List<WaytypeGraphicSpec> areasVisible;
	private List<WaytypeGraphicSpec> linesVisible;
	private List<WaytypeGraphicSpec> labelsVisible;
	
	public boolean scaleCurrentLayer = false;
	private boolean setupDone;

	private Shape currentHighlighedShape;
	private WayType currentHighlighedWaytype;
	private List<RouteEdge> currentRoute;
	
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
	
	/**
	 * Add a Listener in order to receive different events from the MapCanvas.
	 * 
	 * @param l The CanvasListener.
	 */
	public void addCanvasListener(CanvasListener l) {
		listeners.add(l);
	}
	
	public AffineTransform getTransform() {
		return transform;
	}
	
	public AffineTransform getActualTransform() {
		return actualTransform;
	}
	
	/**
	 * The main method for the different drawing methods.
	 * 
	 * @param g2d The Graphics2D object to be drawn with, derived from the overriden paintComponent method.
	 */
	public void drawMap(Graphics2D g2d) {
		if(Main.buffered) {
			Main.tileController.draw(g2d);
		} else {
			drawMapShapes(g2d, getGeographicalRegion(), transform);
			g2d.setStroke(new BasicStroke(Float.MIN_VALUE));
		}
		if(Main.pinPointManager != null) {
			Main.pinPointManager.drawPinPoints(g2d);
			Main.pinPointManager.drawSystemPinPoints(g2d);
		}
		if(currentRoute != null) {
			drawRouteEdges(g2d, currentRoute);
		}
	}

	public void drawImage(Graphics2D g2d) {
        drawMapShapes(g2d, getGeographicalRegion(), transform);
        if(currentRoute != null) drawRouteEdges(g2d, currentRoute);
    }

	public void repaintPinPoints() {
		repaint();
	}

	/**
	 * This method is where all the shapes received from the KD-trees are drawn onto the Canvas.
	 * The method receives a list of WaytypeGraphicSpec objects that correspond to the current zoom level
	 * and draws them according to their visual attributes.
	 * 
	 * @param g2d The Graphics2D object to be drawn with, derived from the overriden paintComponent method.
	 * @param region The currently visible Geographical Region of the map.
	 * @param at The AffineTransform that should be used when drawing.
	 */
	@SuppressWarnings("unused")
	public void drawMapShapes(Graphics2D g2d, Region region, AffineTransform at) {
		Region currentRegion = region;
		drawBackground(g2d);
		g2d.setTransform(at);
		if(antiAlias) g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		if(zoomChanged) {
			wayTypesVisible = getOnScreenGraphicsForCurrentZoom();
			if(wayTypesVisible == null) return;
			areasVisible = GraphicRepresentation.getSpecificSpec(wayTypesVisible, GraphicSpecArea.class);
	        linesVisible = GraphicRepresentation.getSpecificSpec(wayTypesVisible, GraphicSpecLine.class);
	        labelsVisible = GraphicRepresentation.getSpecificSpec(wayTypesVisible, GraphicSpecLabel.class);
		}
		shapesDrawn = 0;

        // drawing all areas
        for (WaytypeGraphicSpec wayTypeArea : areasVisible) {
            currentWTGSpec = wayTypeArea;
            KDTree<ParsedItem> kdTree = Main.model.getEnumMapKD().get(wayTypeArea.getWayType());
            if (kdTree == null) continue;

            // first paint the inner color for areas
            currentWTGSpec.transformPrimary(g2d);
            for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                ParsedItem item = i.next();
                g2d.fill(item.getShape());
                shapesDrawn++;
            }
        }
        
        for (WaytypeGraphicSpec wayTypeArea : areasVisible) {
            currentWTGSpec = wayTypeArea;
            KDTree<ParsedItem> kdTree = Main.model.getEnumMapKD().get(wayTypeArea.getWayType());
            
            if (kdTree == null) continue;
        	// second, paint outlines for areas
            if (currentWTGSpec.getOuterColor() != null) {
                currentWTGSpec.transformOutline(g2d);
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            }
        }
        
        // Drawing all line outlines
        for (WaytypeGraphicSpec wayTypeLine : linesVisible) {
        	if(wayTypeLine.getOuterColor() == null) continue;
            currentWTGSpec = wayTypeLine;
            KDTree<ParsedItem> kdTree = Main.model.getEnumMapKD().get(wayTypeLine.getWayType());
            if (kdTree == null) continue;
            currentWTGSpec.transformOutline(g2d);
            for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                ParsedItem item = i.next();
                g2d.draw(item.getShape());
                shapesDrawn++;
            }
        }

        // draw inner part of all lines.
        for(WaytypeGraphicSpec wayTypeLine : linesVisible) {
            currentWTGSpec = wayTypeLine;
            if (currentWTGSpec instanceof GraphicSpecLine) {
                currentWTGSpec.transformPrimary(g2d);
                KDTree<ParsedItem> kdTree = Main.model.getEnumMapKD().get(wayTypeLine.getWayType());
                if (kdTree == null) continue;
                for (Iterator<ParsedItem> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
                    ParsedItem item = i.next();
                    g2d.draw(item.getShape());
                    shapesDrawn++;
                }
            }
        }
        
        // draw all labels        
        if(getZoom() < 14) {
        	for(WaytypeGraphicSpec wayTypeLabel : labelsVisible) {
            	currentWTGSpec = wayTypeLabel;
            	KDTree<ParsedPlace> kdTree = Main.model.getEnumMapPlacesKD().get(wayTypeLabel.getWayType());
            	if(kdTree == null) continue;
                currentWTGSpec.transformPrimary(g2d);
				for(Iterator<ParsedPlace> i = kdTree.iterator(currentRegion); i.hasNext(); ) {
					ParsedPlace place = i.next();
                    g2d.drawString(place.getName(), place.x, place.y);
                    shapesDrawn++;
                }
            }
        }

        // Draw the KD-tree debug rectangle.
        if(Main.debugExtra && !Main.buffered) {
            g2d.setStroke(new BasicStroke(0.0001f));
            g2d.setColor(Color.BLACK);
            Path2D box = new Path2D.Float();
            box.moveTo(currentRegion.x1, currentRegion.y1);
            box.lineTo(currentRegion.x1, currentRegion.y2);
            box.lineTo(currentRegion.x2, currentRegion.y2);
            box.lineTo(currentRegion.x2, currentRegion.y1);
            box.lineTo(currentRegion.x1, currentRegion.y1);
            g2d.draw(box);
        }
    }
	
	/**
	 * Draws all the shapes, but for a single Tile, this is used when rendering the BufferedImages.
	 * 
	 * @param tile The Tile to draw on.
	 */
	public void drawMapShapesForTile(Tile tile) {
		if(tile.isRendered()) return;
		Graphics2D g2d = (Graphics2D)tile.getGraphics();
		Region tileRegion = tile.getGeographicalRegion();
		Region currentRegion = new Region(tileRegion.x1, tileRegion.y1 + tileRegion.getHeight(), tileRegion.x2, tileRegion.y2 + tileRegion.getHeight());
		AffineTransform at = tile.getTransform();
		drawMapShapes(g2d, currentRegion, at);
    }

	/**
	 * Draw the Canvas background. If no Coastlines are present in the current map file, the background is painted according to the current Theme's
	 * background color, else the background is painted according to Coastlines' color.
	 * 
	 * @param g2d The Graphics2D object to draw with.
	 */
	private void drawBackground(Graphics2D g2d) {
        Region region = Main.model.getMapRegion();
        Path2D background = new Path2D.Double();
        background.moveTo(region.x1, region.y1);
        background.lineTo(region.x2, region.y1);
        background.lineTo(region.x2, region.y2);
        background.lineTo(region.x1, region.y2);
        background.lineTo(region.x1, region.y1);
        
        if(Main.model.getEnumMapKD().containsKey(WayType.COASTLINE) ){
        	g2d.setColor(GraphicRepresentation.getCanvasBGColor());
        }
        else g2d.setColor(GraphicRepresentation.getCoastlineColor());
		AffineTransform af = new AffineTransform();
		g2d.setTransform(af);
        g2d.fillRect(-100, -100, getWidth() + 200, getHeight() + 200);
    }
	
	private float getZoomSizeFactor(float base) {
		return (float)((base*Math.pow(2, (20-getZoom())/2))/5);
	}

	/**
	 * Return a list of the WaytypeGraphicSpecs for all the elements that should be drawn at the current zoom level.
	 * 
	 * @return A list of the WaytypeGraphicSpecs for all the elements that should be drawn at the current zoom level.
	 */
	public List<WaytypeGraphicSpec> getOnScreenGraphicsForCurrentZoom() {
		List<WaytypeGraphicSpec> wayTypeSpecs = GraphicRepresentation.getGraphicSpecs((int)getZoom());
		zoomChanged = false;
		if(wayTypeSpecs == null) return new ArrayList<>();
		return wayTypeSpecs;
	}
	
	public void highlightWay(WayType wayType, Shape shape) {
		if(zoomChanged) {
			boolean zoomLegal = false;
			for(WaytypeGraphicSpec spec : wayTypesVisible) {
				if(spec.getWayType() == wayType) {
					zoomLegal = true;
					break;
				}
			}
			if(!zoomLegal) return;
		}
		
		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.setTransform(transform);
		if(antiAlias) g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		List<WaytypeGraphicSpec> wgs = getOnScreenGraphicsForCurrentZoom();
		for(WaytypeGraphicSpec spec : wgs) {
			if(currentHighlighedShape != null && spec.getWayType() == currentHighlighedWaytype) {
				spec.transformPrimary(g2d);

				if(spec instanceof GraphicSpecLine) g2d.draw(currentHighlighedShape);
				else g2d.fill(currentHighlighedShape);
			}
			if(spec.getWayType() == wayType) {
				spec.transformPrimary(g2d);
				try {
					float lineWidth = ((GraphicSpecLine) spec).getLineWidth() * 0.9f;
					g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				} catch (ClassCastException e) {
					// do nothing
				}
				g2d.setColor(Color.ORANGE);
				if(spec instanceof GraphicSpecLine) g2d.draw(shape);
				else g2d.fill(shape);
			}
			Main.mainPanel.getGuiManager().repaintGUI();
		}
		currentHighlighedShape = shape;
		currentHighlighedWaytype = wayType;
	}
	
	public void drawRouteEdge(RouteEdge edge) {
		drawRouteEdge((Graphics2D)getGraphics(), edge);
	}
	
	private void drawRouteEdges(Graphics2D g2d, List<RouteEdge> edges) {
		currentRoute = edges;
		for(RouteEdge edge : edges) drawRouteEdge(g2d, edge);
	}
	
	private void drawRouteEdge(Graphics2D g2d, RouteEdge edge) {
		Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
		path.moveTo(edge.getFrom().getX(), edge.getFrom().getY());
		path.lineTo(edge.getTo().getX(), edge.getTo().getY());
		g2d.setTransform(transform);
		if(antiAlias) g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		else g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setStroke(new BasicStroke(getZoomSizeFactor(0.00005f)));
		g2d.setColor(Color.BLUE);
		
		if(Main.debug) {
			if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR)  && 
					edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE)
					&& edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK));
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR)  && 
					edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE)) g2d.setColor(Color.MAGENTA);
			
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR)  && 
					edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK)) g2d.setColor(Color.GREEN);
			
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK)  && 
					edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE)) g2d.setColor(Color.PINK);
			
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE)) g2d.setColor(Color.ORANGE);
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR)) g2d.setColor(Color.RED);
			else if(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK)) g2d.setColor(Color.YELLOW);
		}
		
		g2d.draw(path);
	}
	
	public void addRouteEdge(RouteEdge edge) {
		if(currentRoute == null) currentRoute = new ArrayList<>();
		currentRoute.add(edge);
	}
	
	public void setRoute(List<RouteEdge> edges) {
		currentRoute = edges;
	}
	
	public List<RouteEdge> getRoute() {
		return currentRoute;
	}
	
	public Shape getHighlightedShape() {
		return currentHighlighedShape;
	}
	
	public void zoomToRouteRegion(Region routeRegion) {
		final double MARGIN = 1.60;
		
		double distX = (routeRegion.x2-routeRegion.x1)*MARGIN;
		double distY = (routeRegion.y2-routeRegion.y1)*MARGIN;
		double x1 = routeRegion.x2 - distX;
		double x2 = routeRegion.x1 + distX;
		double y1 = routeRegion.y2 - distY;
		double y2 = routeRegion.y1 + distY;
		Region region = new Region(x1, y1, x2, y2);
		
		zoomToRegion(region);
	}
	
	public BufferedImage getRoutePreviewImage() {
		return Util.screenshot(new Rectangle(Main.window.getLocation().x+75, Main.window.getLocation().y+105, getWidth()-150, getHeight()-130), 1500);
	}
	
	public void pan(double dx, double dy) {
		repair();
		transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
		actualTransform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        if (Main.tileController.isInitialized()) {
            Main.tileController.pan(dx, dy);
            if (Main.tileController.updateTilePos()) {
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
	
	public boolean isSetupDone() {
		return setupDone;
	}
	
	public void setupInProgress() {
		setupDone = false;
	}
	
	public void mouseMoved() {
		if(setupDone) for(CanvasListener listener : listeners) listener.onMouseMoved();
	}

	public void zoom(double _factor) {
	    double factor = _factor;

		double zoomBefore = getZoom();
		double scaleBefore = getZoomRaw();

		if(scaleBefore * factor > DKConstants.MAX_SCALE) factor = DKConstants.MAX_SCALE / scaleBefore;
		else if(scaleBefore * factor < DKConstants.MIN_SCALE) factor = DKConstants.MIN_SCALE / scaleBefore;
		if(factor == 1) return;
		
		Util.zoom(transform, factor);
		Util.zoom(actualTransform, factor);
		if(Main.tileController.isInitialized()) Main.tileController.zoom(factor);
		double scaleAfter = getZoomRaw();

		scaleAfter = getZoomRaw();
		
		for(CanvasListener listener : listeners) listener.onZoom();
		
		if(scaleBefore != scaleAfter) {
			if(Main.tileController.isInitialized()) Main.tileController.resetTileTransform();
		}
		
		if(zoomBefore != getZoom()) {
			zoomChanged = true;
			for(CanvasListener listener : listeners) listener.onZoomLevelChanged();
		}

		repaint();
	}
	
	public void snapToZoom(int zoomValue) {
		double factor;
		int amount;
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
	    return (double)(int)((Math.log(transform.getScaleX() / DKConstants.MIN_SCALE) / Math.log(DKConstants.ZOOM_LOG_BASE)) + 1);
    }
	
	public double getZoomRaw() {
		return transform.getScaleX();
	}
	
	public void zoomToBounds() {
		Region mapRegion = Main.model.getMapRegion();
		pan(-mapRegion.x1, -mapRegion.y2);
		zoom(getWidth() / (mapRegion.x2 - mapRegion.x1));
	}
	
	public void zoomToRegion(Region region) {
		AffineTransform newTransform = new AffineTransform();
		if(region.y2-region.y1 < region.x2-region.x1) Util.zoomToRegion(newTransform, region, getWidth());
		else Util.zoomToRegionY(newTransform, region, getHeight());
		
		transform.setTransform(newTransform);
		actualTransform.setTransform(newTransform);
		panToPosition(region.getMiddlePoint());
		
		forceRepaint();
	}
	
	public void setupDone() {
		zoomToBounds();
		
		if(!Main.tileController.isInitialized()) Main.tileController.initialize();
		setupDone = true;
		for(CanvasListener listener : listeners) listener.onSetupDone();
	}
	
	public void forceRepaint() {
		zoomChanged = true;
		if(Main.buffered) {
			Main.tileController.fullRepaint();
		} else {
			repaint();
		}
	}
}