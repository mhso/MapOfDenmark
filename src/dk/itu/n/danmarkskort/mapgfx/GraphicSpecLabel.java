package dk.itu.n.danmarkskort.mapgfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GraphicSpecLabel extends WaytypeGraphicSpec {
	private int fontSize;
	private Font font;
	
	public GraphicSpecLabel(int fontSize, Color fontColor) {
		this.fontSize = fontSize;
		font = new Font("Tahoma", Font.PLAIN, (int)fontSize);
		//font = font.deriveFont(fontSize*0.00001f);
	}

	public void transformPrimary(Graphics2D graphics) {
		super.transformPrimary(graphics);
		
		graphics.setFont(new Font(graphics.getFont().getName(), Font.PLAIN, fontSize));
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}