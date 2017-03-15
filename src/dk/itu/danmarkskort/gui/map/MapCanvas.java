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

public class MapCanvas extends JPanel {

	private static final long serialVersionUID = -4476997375977002964L;
	
	AffineTransform transform = new AffineTransform();
	boolean antiAlias;
	
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
		g2d.setColor(Color.DARK_GRAY);
		int boxSize = 16;
		for(int x=0; x<this.getWidth(); x+=boxSize) {
			for(int y=0; y<this.getWidth(); y+=boxSize) {
				g2d.drawRect(x, y, boxSize, boxSize);
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

}
