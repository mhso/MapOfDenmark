package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

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
		return x2 - x1;
	}

	public double getHeight() {
		return y2 - y1;
	}

	public String toString() {
		return "Region [x1=" + x1 + ", x2=" + x2 + ", y1=" + y1 + ", y2=" + y2 + "]";
	}

	public boolean containsPoint(Point2D point) {
		return (point.getX() >= x1 && point.getX() <= x2 && point.getY() >= y1 && point.getY() <= y2);
	}

}
