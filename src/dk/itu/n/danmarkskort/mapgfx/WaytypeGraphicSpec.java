package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Graphics2D;

import dk.itu.n.danmarkskort.models.WayType;

/**
 * This class is used for storing information about the visual representation of a map element 
 * (A.K.A: A WayType).
 */
public abstract class WaytypeGraphicSpec implements Comparable<WaytypeGraphicSpec> {
	private WayType wayType;
	private Color innerColor;
	private Color outerColor;
	private int layer;
	private boolean isFiltered;
	
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
		return wayType;
	}
	
	public Color getInnerColor() {
		return innerColor;
	}
	
	public Color getOuterColor() {
		return outerColor;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setWayType(WayType wayType) {
		this.wayType = wayType;
	}
	
	public void setInnerColor(Color innerColor) {
		this.innerColor = innerColor;
	}
	
	public void setOuterColor(Color outerColor) {
		this.outerColor = outerColor;
	}
	
	public boolean isFiltered() {
		return isFiltered;
	}
	
	public void setFiltered(boolean filtered) {
		isFiltered = filtered;
	}
	
	public int compareTo(WaytypeGraphicSpec otherSpec) {
		if(layer < otherSpec.getLayer()) return -1;
		else if(layer > otherSpec.getLayer()) return 1;
		return 0;
	}
	
	public String toString() {
		return "Graphic Specification [" + "wayType=" + wayType + ", innerColor="+innerColor
				+", outerColor="+outerColor+", layer=" + layer;
	}
}