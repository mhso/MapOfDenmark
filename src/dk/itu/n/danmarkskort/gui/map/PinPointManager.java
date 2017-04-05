package dk.itu.n.danmarkskort.gui.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import javax.imageio.ImageIO;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.parsedmodels.Region;

public class PinPointManager implements Serializable {

	private static final long serialVersionUID = 8260486034670292677L;
	private HashMap<String, PinPoint> pinPoints = new HashMap<String, PinPoint>();
	private HashMap<String, PinPoint> systemPinPoints = new HashMap<String, PinPoint>();
	private transient BufferedImage icon;
	private transient MapCanvas canvas;
	
	public PinPointManager(MapCanvas canvas) {
		this.canvas = canvas;
		loadIcon();
	}
	
	public void setMap(MapCanvas canvas) {
		this.canvas = canvas;
	}
	
	public PinPoint getFirstPinPoint() {
		return pinPoints.get(pinPoints.keySet().toArray(new String[pinPoints.size()])[0]);
	}
	
	public void loadIcon() {
		try {
			icon = ImageIO.read(new File("resources/icons/pin.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getIcon() {
		return icon;
	}
	
	public boolean addPinPoint(String name, PinPoint pinPoint) {
		if(pinPoints.containsKey(name)) return false;
		pinPoints.put(name, pinPoint);
		Main.mainPanel.repaint();
		save();
		return true;
	}
	
	public boolean removePinPoint(PinPoint pinPoint) {
		if(pinPoints.containsValue(pinPoint)) {
			pinPoints.remove(pinPoint.getName());
			return true;
		} else return false;
	}
	
	public boolean addSystemPinPoint(String name, PinPoint pinPoint) {
		if(systemPinPoints.containsKey(name)) return false;
		systemPinPoints.put(name, pinPoint);
		Main.mainPanel.repaint();
		save();
		return true;
	}
	
	public boolean removeSystemPinPoint(PinPoint pinPoint) {
		if(systemPinPoints.containsValue(pinPoint)) {
			systemPinPoints.remove(pinPoint.getName());
			return true;
		} else return false;
	}
	
	public PinPoint getPinPoint(String name) {
		return pinPoints.get(name);
	}
	
	public PinPoint getSystemPinPoint(String name) {
		return pinPoints.get(name);
	}
	
	public void checkHover() {
		Region mapRegion = canvas.getGeographicalRegion();
		
		for(PinPoint pinPoint : pinPoints.values()) {
			if(pinPoint.isInRegion(mapRegion)) {
				if(pinPoint.checkHover()) {
					Main.mainPanel.repaint();
					return;
				}
			}
		}
	}
	
	public void panToLocation(PinPoint pinPoint) {
		canvas.panToPosition(pinPoint.getLocation());
	}
	
	public boolean panToLocation(String name) {
		PinPoint pinPoint = pinPoints.get(name);
		if(pinPoint == null) return false;
		panToLocation(pinPoint);
		return true;
	}
	
	public void save() {
		Util.writeObjectToFile(this, "pinpoints.bin");
	}
	
	public void drawPinPoints(Graphics2D g) {
		Region mapRegion = canvas.getGeographicalRegion();
		
		for(PinPoint pinPoint : pinPoints.values()) {
			if(pinPoint.isInRegion(mapRegion)) pinPoint.draw(g);
		}
	}
	
	public void drawSystemPinPoints(Graphics2D g) {
		Region mapRegion = canvas.getGeographicalRegion();
		
		for(PinPoint pinPoint : systemPinPoints.values()) {
			if(pinPoint.isInRegion(mapRegion)) pinPoint.draw(g);
		}
	}
	
	public int size() {
		return pinPoints.size();
	}
	
	public int systemSize() {
		return systemPinPoints.size();
	}
	
	public static PinPointManager load(MapCanvas canvas) {
		PinPointManager manager = (PinPointManager) Util.readObjectFromFile("pinpoints.bin");
		if(manager != null) {
			manager.setMap(canvas);
			manager.loadIcon();
			Main.log("Loaded " + manager.size() + " pinpoints.");
			return manager;
		}
		return new PinPointManager(canvas);
	}
	
	public PinPoint[] getPinPoints() {
		return this.pinPoints.values().toArray(new PinPoint[pinPoints.values().size()]);
	}
	
	public PinPoint[] getSystemPinPoints() {
		return this.systemPinPoints.values().toArray(new PinPoint[systemPinPoints.values().size()]);
	}
	
}
