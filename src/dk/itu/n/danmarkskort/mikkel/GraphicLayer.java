package dk.itu.n.danmarkskort.mikkel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class GraphicLayer {
	private Color color;
	private float strokeWidth;
	private String strokeType;

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
}
