package dk.itu.n.danmarkskort.mapgfx;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.Arrays;

import dk.itu.n.danmarkskort.DKConstants;

public class GraphicSpecLine extends WaytypeGraphicSpec {
	private float lineWidth;
	private float[] dashArr;
	private boolean borderDash;
	private int lineEnd = BasicStroke.CAP_ROUND;
	private int lineJoin = BasicStroke.JOIN_ROUND;

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		BasicStroke stroke = null;
		if(dashArr == null) {
			stroke = new BasicStroke(lineWidth, lineEnd, lineJoin);
		}
		else {
			stroke = new BasicStroke(lineWidth, lineEnd, lineJoin, 0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
	}
	
	public void transformOutline(Graphics2D graphics) {
		super.transformOutline(graphics);
		BasicStroke stroke = null;
		if(!borderDash) {
			stroke = new BasicStroke(lineWidth+DKConstants.LINE_OUTLINE_WIDTH, lineEnd, lineJoin);
		}
		else {
			stroke = new BasicStroke(lineWidth+DKConstants.LINE_OUTLINE_WIDTH, lineEnd, lineJoin, 0.00008f, dashArr, 0.0f);
		}
		graphics.setStroke(stroke);
	}
	
	public float getLineWidth() {
		return lineWidth;
	}
	
	public float[] getDashArr() {
		return dashArr;
	}
	
	public boolean isBorderDashed() {
		return borderDash;
	}
	
	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
	
	public void setDashArr(float[] dashArr) {
		this.dashArr = dashArr;
		if(this.dashArr != null) {
		    for(int i = 0; i < this.dashArr.length; i++) {
                this.dashArr[i] *= DKConstants.LINE_OUTLINE_WIDTH;
            }
        }
	}
	
	public void setBorderDashed(boolean dashed) {
		borderDash = dashed;
	}
	
	public int getLineEnd() {
		return lineEnd;
	}

	public void setLineEnd(int lineEnd) {
		this.lineEnd = lineEnd;
	}

	public int getLineJoin() {
		return lineJoin;
	}

	public void setLineJoin(int lineJoin) {
		this.lineJoin = lineJoin;
	}
	
	public String toString() {
		return super.toString() + ", type=line, lineWidth=" + lineWidth + 
				", dashArr=" + Arrays.toString(dashArr) + "]";
	}
}