package dk.itu.n.danmarkskort.models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class GraphicLayer {
	private Color color;
	private float strokeWidth;
	private String strokeType;

	/**
	 * Create a GraphicLayer containing information about how a Map Element's layer should be drawn.
	 * 
	 * @param color The color that this layer should be drawn with.
	 * @param strokeWidth The width of the stroke being used to draw this layer.
	 * @param dashType The stroke dash type to be used, f.x. "dotted" or "standard".
	 */
	public GraphicLayer(Color color, float strokeWidth, String dashType) {
		this.color = color;
		this.strokeWidth = strokeWidth;
		this.strokeType = dashType;
	}
	
	public Color getColor() {
		return color;
	}
	
	public float getStrokeWidth() {
		return strokeWidth;
	}
	
	public String getStrokeType() {
		return strokeType;
	}
	
	/**
	 * Apply the graphic elements of this GraphicLayer to the inputed Graphics2D object.
	 * 
	 * @param graphic The Graphics2D object to be changed to the values of this GraphicLayer.
	 * @return The Graphics2D object with updated values.
	 */
	public Graphics2D transform(Graphics2D graphic) {
		graphic.setColor(color);
		BasicStroke stroke = null;
		if(strokeType.equals("standard")) {
			stroke = new BasicStroke(strokeWidth);
		}
		else if(strokeType.equals("dotted")) {
			stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
					0.00008f, new float[]{0.00002f}, 0.0f);
		}
		graphic.setStroke(stroke);
		return graphic;
	}

	@Override
	public String toString() {
		return "GraphicLayer [color=" + color + ", strokeWidth=" + strokeWidth + ", strokeType=" + strokeType + "]";
	}
}
