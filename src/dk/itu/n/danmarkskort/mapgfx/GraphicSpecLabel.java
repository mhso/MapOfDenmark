package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import dk.itu.n.danmarkskort.DKConstants;

public class GraphicSpecLabel extends WaytypeGraphicSpec {
	private int fontSize;
	private Font font;
	
	public GraphicSpecLabel(int fontSize, Color fontColor) {
		this.fontSize = fontSize;
		setInnerColor(fontColor);
		font = new Font("Tahoma", Font.BOLD, fontSize);
		font = font.deriveFont(fontSize*DKConstants.FONTSIZE_MAGNIFYING_VALUE);
	}

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		graphics.setFont(font);
	}
	
	public void transformOutline(Graphics2D graphics) {
		super.transformOutline(graphics);
		graphics.setFont(font.deriveFont((fontSize*DKConstants.FONTSIZE_MAGNIFYING_VALUE)+DKConstants.FONT_OUTLINE_WIDTH));
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public String toString() {
		return super.toString() + ", type=label, fontSize=" + fontSize;
	}
}