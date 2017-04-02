package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Arrays;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;

public class GraphicSpecLine extends WaytypeGraphicSpec {
	private float lineWidth;
	private float[] dashArr;

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
		
	}
	
	public void transformOutline(Graphics2D graphics) {
		super.transformOutline(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth+DKConstants.LINE_OUTLINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
		else {
			stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.00008f, dashArr, 0.0f);
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
		if(this.dashArr != null) {
		    for(int i = 0; i < this.dashArr.length; i++) {
                this.dashArr[i] *= 0.000006f;
            }
        }
	}
	
	public String toString() {
		return super.toString() + ", type=line, lineWidth=" + lineWidth + 
				", dashArr=" + Arrays.toString(dashArr) + "]";
	}
}