package dk.itu.n.danmarkskort.gui.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import dk.itu.n.danmarkskort.Main;

public class BufferedMapImage extends BufferedImage {

	public Point pos;
	private BufferedMapManager manager;
	
	public BufferedMapImage(BufferedMapManager manager, Point pos) {
		super(Main.map.getWidth(), Main.map.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.manager = manager;
		this.pos = pos;
	}
	
	public void render() {
		Point2D currentPan = Main.map.getCurrentPan();
		Main.map.purePanToTile(getPosition());
		Main.log(Main.map.getTransform().getTranslateX()/Main.map.getZoomRaw() - Main.map.getZero().getX());
		Main.log(Main.map.getTransform().getTranslateY()/Main.map.getZoomRaw() - Main.map.getZero().getY());
		Main.map.drawMapShapes((Graphics2D) getGraphics());
		Main.map.purePanToPosition(currentPan);
	}
	
	public void draw(Graphics2D g) {
		Point pos = getPixelPosition();
		g.setColor(Color.GREEN);
		g.drawImage(this, pos.x, pos.y, getWidth(), getHeight(), null);
		if(Main.debug) g.drawRect(pos.x, pos.y, Main.map.getWidth(), Main.map.getHeight());
	}
	
	public Point getPixelPosition() {
		return new Point(pos.x * Main.map.getWidth(), pos.y * Main.map.getHeight());
	}
	
	public Point getPosition() {
		return pos;
	}
	
}
