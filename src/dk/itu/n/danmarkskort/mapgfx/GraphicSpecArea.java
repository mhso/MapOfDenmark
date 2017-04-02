package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import dk.itu.n.danmarkskort.DKConstants;

public class GraphicSpecArea extends WaytypeGraphicSpec {
	
	public String toString() {
		return super.toString() + ", type=area]";
	}
	
	public void transformOutline(Graphics2D graphics) {
		super.transformOutline(graphics);
		BasicStroke stroke = new BasicStroke(DKConstants.LINE_OUTLINE_WIDTH);
		graphics.setStroke(stroke);
	}
}