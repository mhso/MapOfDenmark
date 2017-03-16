package dk.itu.n.danmarkskort.models;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Arrays;

public class GraphicSpecLine extends WaytypeGraphicSpec {
	private float lineWidth;
	private float[] dashArr;

	public Graphics2D transformPrimary(Graphics2D graphics) {
		graphics = super.transformPrimary(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
					0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
		
		return graphics;
	}
	
	public Graphics2D transformOutline(Graphics2D graphics) {
		graphics = super.transformOutline(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth+0.0000001f);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
					0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
		
		return graphics;
	}
	
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	public void setDashArr(float[] dashArr) {
		this.dashArr = dashArr;
	}
	
	public String toString() {
		return super.toString() + ", type=line, lineWidth=" + lineWidth + 
				", dashArr=" + Arrays.toString(dashArr) + "]";
	}
}