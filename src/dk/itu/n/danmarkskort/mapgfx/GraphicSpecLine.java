package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Arrays;

public class GraphicSpecLine extends WaytypeGraphicSpec {
	private float lineWidth;
	private float[] dashArr;

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
					0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
		
	}
	
	public void transformOutline(Graphics2D graphics) {
		super.transformOutline(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth+OUTLINE_WIDTH);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
					0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
	}
	
	public float getLineWidth() {
		return lineWidth;
	}
	
	public float[] getDashArr() {
		return dashArr;
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