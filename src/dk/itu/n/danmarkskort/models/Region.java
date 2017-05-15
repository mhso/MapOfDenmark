package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

public class Region implements Serializable {

	public double x1, x2, y1, y2;

	/** A region is initialized with two pairs of coordinates.<br>
	x1, y1 are the top left coordinates.<br>
	x2, y2 are the bottom right coordinates.**/
	public Region(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**  This will return the middle point of the region.<br>
	The middle point is the point exactly in the middle of the region.*/
	public Point2D getMiddlePoint() {
		return new Point2D.Double(((x1 + x2) / 2), ((y1 + y2) / 2));
	}

	/** This returns the first coordinate pair aka the left top coordinates.*/
	public Point2D getPointFrom() {
		return new Point2D.Double(x1, y1);
	}

	/** This returns the second coordinate pair aka the bottom right coordinates.*/
	public Point2D getPointTo() {
		return new Point2D.Double(x2, y2);
	}

	/** This return the width of the region.*/
	public double getWidth() {
		return x2 - x1;
	}

	/** This returns the height of the region.*/
	public double getHeight() {
		return y2 - y1;
	}

	/** This gives an insight for the console about the state of the instance.*/
	public String toString() {
		return "Region [x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + "]";
	}

	/** This will return true if the specified point is inside the region.*/
	public boolean containsPoint(Point2D point) {
		return (point.getX() >= x1 && point.getX() <= x2 && point.getY() >= y1 && point.getY() <= y2);
	}
	
	/** This will return a rectangle shape of the region.*/
	public Rectangle2D getRect() {
		return new Rectangle2D.Double(x1, y1, x1 + getWidth(), y1 + getHeight());
	}
	
	/** This will return true if this region overlaps the specified region.*/
	public boolean overlapsRegion(Region other) {
		if(Util.valueIsBetween(x1, other.x1, other.x2) && Util.valueIsBetween(y1, other.y1, other.y2)) return true;
		if(Util.valueIsBetween(x2, other.x1, other.x2) && Util.valueIsBetween(y1, other.y1, other.y2)) return true;
		if(Util.valueIsBetween(x1, other.x1, other.x2) && Util.valueIsBetween(y2, other.y1, other.y2)) return true;
		if(Util.valueIsBetween(x2, other.x1, other.x2) && Util.valueIsBetween(y2, other.y1, other.y2)) return true;
		if(Util.valueIsBetween(other.x1, x1, x2) && Util.valueIsBetween(other.y1, y1, y2)) return true;
		if(Util.valueIsBetween(other.x2, x1, x2) && Util.valueIsBetween(other.y1, y1, y2)) return true;
		if(Util.valueIsBetween(other.x1, x1, x2) && Util.valueIsBetween(other.y2, y1, y2)) return true;
		if(Util.valueIsBetween(other.x2, x1, x2) && Util.valueIsBetween(other.y2, y1, y2)) return true;
		
		return false;
	}
	
	/** If this region are made of geographical coordinates then this method will
	create a new region that consists of the screen-coordinates.*/
	public Region toPixelRegion() {
		Point2D p1 = Main.map.toActualScreenCoords(new Point2D.Double(x1, y1));
		Point2D p2 = Main.map.toActualScreenCoords(new Point2D.Double(x2, y2));
		return new Region(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	/** This will return a shape from the region.<br>
	This is exactly the same as getRectangle(), but developers like this naming convention.*/
	public Shape getShape() {
		return getRect();
	}
	
}
