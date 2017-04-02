package dk.itu.n.danmarkskort.gui.map;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class PinPoint implements Serializable {

	private static final long serialVersionUID = -3229202388000112060L;
	private Point2D location;
	private int icon = 0;
	
	public PinPoint(Point2D location) {
		this.location = location;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	public void draw() {
		// If mouse is over, draw the icon with the name below, else just draw the icon. 
	}
	
}
