package dk.itu.n.danmarkskort.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JLabel;

import dk.itu.n.danmarkskort.DKConstants;

public class ScaleLabel extends JLabel {
	public ScaleLabel(String text) {
		super(text);
		setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH/12, 20));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawScale((Graphics2D) g);
	}
	
	public void drawScale(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		Point l = getLocation();
		g2d.drawLine(l.x, l.y+6, l.x, l.y+getHeight()-1);
		g2d.drawLine(l.x, l.y+getHeight()-1, l.x+getWidth(), l.y+getHeight()-1);
		g2d.drawLine(l.x+getWidth()-1, l.y+6, l.x+getWidth()-1, l.y+getHeight()-1);
	}
}
