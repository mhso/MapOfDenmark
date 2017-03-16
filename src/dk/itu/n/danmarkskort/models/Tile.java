package dk.itu.n.danmarkskort.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import dk.itu.n.danmarkskort.Util;

public class Tile {

	private TileCoordinate coord;
	private BufferedImage image;
	private int zoom;
	private final int size = 16;
	public ArrayList<ParsedObject> parsedObjects = new ArrayList<ParsedObject>();
	
	public Tile(TileCoordinate coord, int zoom) {
		this.coord = coord;
		this.zoom = zoom;
		image = new BufferedImage(size, size, )
	}

	public TileCoordinate getCoord() {
		return coord;
	}
	
	public void load() {
		// Read file
		// Fill parsedObjects
		// Add reference to TileController
	}
	
	public void unload() {
		// Remove reference from TileController
	}
	
	public void write() {
		try {
			File file = getFile();
			if(!file.exists()) {
				file.createNewFile();	
			} else {
				file.delete();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addInformation(ParsedObject object) {
		parsedObjects.add(object);
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public String getInfo() {
		return coord.getX() + "-" + coord.getY() + "-" + getZoom();
	}
	
	public File getFile() {
		return new File(Util.getCurrentOSMFolderPath() + "/" + getInfo() + ".tile");
	}

}
