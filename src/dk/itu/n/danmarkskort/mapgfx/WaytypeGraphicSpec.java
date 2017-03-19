package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for storing information about the visual representation of a map element (a WayType f.x.).
 */
public abstract class WaytypeGraphicSpec {
	private Object mapElement;
	private Color innerColor;
	private Color outerColor;
	
	public Graphics2D transformPrimary(Graphics2D graphics) {
		graphics.setColor(innerColor);
		return graphics;
	}
	
	public Graphics2D transformOutline(Graphics2D graphics) {
		graphics.setColor(outerColor);
		return graphics;
	}
	
	public Object getMapElement() {
		return mapElement;
	}
	
	public Color getInnerColor() {
		return innerColor;
	}
	
	public Color getOuterColor() {
		return outerColor;
	}
	
	public void setMapElement(Object mapElement) {
		this.mapElement = mapElement;
	}
	
	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}
	
	public void setOuterColor(Color outerColor) {
		this.outerColor = outerColor;
	}
	
	public String toString() {
		return "Graphic Specification [" + "mapElement=" + mapElement + ", innerColor="+innerColor
				+", outerColor="+outerColor;
	}
}