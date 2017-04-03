package dk.itu.n.danmarkskort.gui.map;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.DropdownRightClick;
public class MapMouseController extends MouseAdapter {

	MapCanvas canvas;
	Point2D lastMousePosition;

	public MapMouseController(MapCanvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		if(SwingUtilities.isRightMouseButton(e)) {
			DropdownRightClick drc = new DropdownRightClick();
			drc.show(canvas, e.getX(), e.getY());
		}
	}

	public void mousePressed(MouseEvent e) {
		lastMousePosition = e.getPoint();
	}

	public void mouseDragged(MouseEvent e) {
		Point2D currentMousePosition = e.getPoint();
		double dx = currentMousePosition.getX() - lastMousePosition.getX();
		double dy = currentMousePosition.getY() - lastMousePosition.getY();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int warp = Util.mouseWarp();
		if(warp == 0) {
			dx -= screenSize.getWidth();
		} else if(warp == 1) {
			dx += screenSize.getWidth();
		} else if(warp == 2) {
			dy -= screenSize.getHeight();
		} else if(warp == 3) {
			dy += screenSize.getHeight();
		}
		
		if(warp != -1) Main.log(warp + "");
		
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

