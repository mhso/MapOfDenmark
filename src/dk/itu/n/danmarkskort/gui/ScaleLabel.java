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
	private static final long serialVersionUID = 7223963683459327164L;

	public ScaleLabel(String text) {
		super(text);
		setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH/12, 20));
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawScale((Graphics2D) g);
	}
	
	public void drawScale(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke(1));
		Point l = getLocation();
		g2d.drawLine(2, 8, 2, getHeight()-3);
		g2d.drawLine(2, getHeight()-3, l.x+getWidth()-2, getHeight()-3);
		g2d.drawLine(getWidth()-3, 8, getWidth()-3, getHeight()-3);
	}
}
