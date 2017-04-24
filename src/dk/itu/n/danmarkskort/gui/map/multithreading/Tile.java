package dk.itu.n.danmarkskort.gui.map.multithreading;

import java.awt.Point;
import java.awt.image.BufferedImage;

import dk.itu.n.danmarkskort.Main;

public class Tile {

	private BufferedImage image;
	private Point pos;
	private double scale;
	
	public Tile(Point pos) {
		this.pos = pos;
		this.image = new BufferedImage(Main.tileController.getTileWidth(), Main.tileController.getTileHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	public boolean render() {
		if(scale != 0) return false;
		
		return true;
	}
	
	public Point getPos() {
		return pos;
	}
	
	public String getKey() {
		return pos.x + "" + pos.y;
	}
	
	public void zoom(double scale) {
		this.scale *= scale;
	}
	
	public void getGeographicalRegion() {
		
	}
	
}
