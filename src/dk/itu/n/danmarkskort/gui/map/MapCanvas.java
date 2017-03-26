package dk.itu.n.danmarkskort.gui.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Observable;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;
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

	public void drawMap(Graphics2D g2d) {
		if(!Main.tileController.hasBounds()) return;
		g2d.setTransform(transform);

		List<WaytypeGraphicSpec> graphicSpecs =
				GraphicRepresentation.getGraphicSpecs((int)getZoom());

		for(WaytypeGraphicSpec wgs : graphicSpecs) {
			List<ParsedWay> ways = Main.tileController.getWaysOfType(wgs.getMapElement());
			if(wgs.getMapElement() == null) continue;
			for(ParsedWay way : ways) {
				Shape shape = way.getShape();

				wgs.transformOutline(g2d);
				if(wgs instanceof GraphicSpecLine) g2d.draw(shape);
				else if(wgs instanceof GraphicSpecArea) g2d.fill(shape);

				wgs.transformPrimary(g2d);

				if(wgs instanceof GraphicSpecLine) g2d.draw(shape);
				else if(wgs instanceof GraphicSpecArea) g2d.fill(shape);
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