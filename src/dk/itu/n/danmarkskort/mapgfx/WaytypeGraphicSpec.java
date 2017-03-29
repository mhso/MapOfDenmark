package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Graphics2D;

import dk.itu.n.danmarkskort.models.WayType;

/**
 * This class is used for storing information about the visual representation of a map element 
 * (A.K.A: A WayType).
 */
public abstract class WaytypeGraphicSpec {
	protected static final float OUTLINE_WIDTH = 0.00001f;
	
	private WayType mapElement;
	private Color innerColor;
	private Color outerColor;
	
	/**
	 * Apply the inner line/area attributes of this WaytypeGraphicSpec to a given Graphics2D object.
	 * 
	 * @param graphics The Graphics2D object that should have its values updated.
	 */
	public void transformPrimary(Graphics2D graphics) {
		graphics.setColor(innerColor);
	}
	
	/**
	 * Apply the outer line/area attributes of this WaytypeGraphicSpec to a given Graphics2D object.
	 * 
	 * @param graphics The Graphics2D object that should have its values updated.
	 */
	public void transformOutline(Graphics2D graphics) {
		graphics.setColor(outerColor);
	}
	
	/*
	 * Return the WayType that this GraphicSpec object refers to.
	 */
	public WayType getWayType() {
		return mapElement;
	}
	
	public Color getInnerColor() {
		return innerColor;
	}
	
	public Color getOuterColor() {
		return outerColor;
	}
	
	public void setMapElement(WayType mapElement) {
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