package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GraphicSpecLabel extends WaytypeGraphicSpec {
	private int fontSize;
	private Color fontColor;
	
	public GraphicSpecLabel(int fontSize, Color fontColor) {
		this.fontSize = fontSize;
		this.fontColor = fontColor;
	}

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		graphics.setFont(new Font(graphics.getFont().getName(), Font.PLAIN, fontSize));
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
}