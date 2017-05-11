package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;

public class Region {

	public double x1, x2, y1, y2;

	public Region(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	public Point2D getMiddlePoint() {
		return new Point2D.Double(((x1 + x2) / 2), ((y1 + y2) / 2));
	}

	public Point2D getPointFrom() {
		return new Point2D.Double(x1, y1);
	}

	public Point2D getPointTo() {
		return new Point2D.Double(x2, y2);
	}

	public double getWidth() {
		return Math.abs(x2 - x1);
	}

	public double getHeight() {
		return Math.abs(y2 - y1);
	}

	public String toString() {
		return "Region [x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + "]";
	}

	public boolean containsPoint(Point2D point) {
		return (point.getX() >= x1 && point.getX() <= x2 && point.getY() >= y1 && point.getY() <= y2);
	}
	
	public Rectangle2D getRect() {
		return new Rectangle2D.Double(x1, y1, x1 + getWidth(), y1 + getHeight());
	}
	
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
	
	public Region toPixelRegion() {
		Point2D p1 = Main.map.toActualScreenCoords(new Point2D.Double(x1, y1));
		Point2D p2 = Main.map.toActualScreenCoords(new Point2D.Double(x2, y2));
		return new Region(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public Shape getShape() {
		return getRect();
	}
	
}
