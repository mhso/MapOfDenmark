package dk.itu.danmarkskort.gui.map;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import dk.itu.n.danmarkskort.Main;
public class MapMouseController extends MouseAdapter {

	MapCanvas canvas;
	Point2D lastMousePosition;

	public MapMouseController(MapCanvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
	}

	public void mousePressed(MouseEvent e) {
		lastMousePosition = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		Point2D currentMousePosition = e.getPoint();
		double dx = currentMousePosition.getX() - lastMousePosition.getX();
		double dy = currentMousePosition.getY() - lastMousePosition.getY();
		canvas.pan(dx, dy);
		lastMousePosition = currentMousePosition;
		Main.window.repaint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		double factor = Math.pow(0.9, e.getWheelRotation());
		Point2D currentMousePosition = e.getPoint();
		double dx = currentMousePosition.getX();
		double dy = currentMousePosition.getY();
		canvas.pan(-dx, -dy);
		canvas.zoom(factor);
		canvas.pan(dx, dy);
		Main.window.repaint();
	}
}

