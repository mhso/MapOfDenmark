package dk.itu.n.danmarkskort.gui.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;
import dk.itu.n.danmarkskort.gui.menu.PinPointPage;

public class PinPointManager implements Serializable {

	private static final long serialVersionUID = 8260486034670292677L;
	private HashMap<String, PinPoint> pinPoints = new HashMap<String, PinPoint>();
	private HashMap<String, PinPoint> systemPinPoints = new HashMap<String, PinPoint>();
	private transient ArrayList<BufferedImage> systemIcons = new ArrayList<BufferedImage>();
	private transient BufferedImage icon;
	private transient MapCanvas canvas;
	
	/**
	 * PinPointManager manages point of intersts for a MapCanvas.<br>
	 * This method creates a new PinPointManager for a MapCanvas. 
	 * @param canvas The <strong style="color:blue">MapCanvas</strong> to use.
	 */
	public PinPointManager(MapCanvas canvas) {
		this.canvas = canvas;
		loadIcon();
	}
	
	
	/**
	 * Updates the MapCanvas to use with the PinPointManager<br>
	 * @param canvas The <strong style="color:blue">MapCanvas</strong> to use.
	 */
	public void setMap(MapCanvas canvas) {
		this.canvas = canvas;
	}
	
	/**
	 * Outputs the first PinPoint added.<br>
	 * @return The first <strong style="color:blue">PinPoint</strong> in the list.
	 */
	public PinPoint getFirstPinPoint() {
		return pinPoints.get(pinPoints.keySet().toArray(new String[pinPoints.size()])[0]);
	}
	
	
	/**
	 * Loads icons to be used with the PinPointManager.<br>
	 * <strong>Note: </strong>Only to be used during initializtion.
	 */
	public void loadIcon() {
		try {
			if(systemIcons == null) systemIcons = new ArrayList<BufferedImage>();
			icon = ImageIO.read(new File("resources/icons/pin.png"));
			File[] fileList = new File("resources/icons/default_pins").listFiles();
			if(fileList == null) {
				Main.log("No system pin images were found.");
				return;
			}
			for(File file : fileList) {
				BufferedImage image = ImageIO.read(file);
				if(image == null) Main.log("(PinPoint) Could not load " + file.toString());
				else systemIcons.add(image);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Outputs the default icon which PinPoints draw if their index is invalid.<br>
	 * @return The default <strong style="color:blue">BufferedImage</strong> icon.
	 */
	public BufferedImage getIcon() {
		return icon;
	}
	
	
	/**
	 * @return The <strong style="color:blue">BufferedImage</strong> icon for the given <strong style="color:blue">index</strong><br>
	 * @param index The <strong style="color:blue">index</strong> of the icon. <br>
	 * Icons are added at <strong style="color:brown">resources/icons/default_pinpoints/...</strong>
	 */
	public BufferedImage getSystemIcon(int index) {
		if(index < 0 || index > systemIcons.size() - 1) return getIcon();
		else return systemIcons.get(index);
	}

	/**
	 * Adds a PinPoint. 
	 * @param name The <strong style="color:blue">name</strong> of the PinPoint.
	 * @param pinPoint The actual <strong style="color:blue">PinPoint</strong>. 
	 * @return If the pinpoint successfully was added. 
	 */
	public boolean addPinPoint(String name, PinPoint pinPoint) {
		if(pinPoints.containsKey(name)) return false;
		pinPoints.put(name, pinPoint);
		Main.mainPanel.repaint();
		save();
		PinPointPage.update();
		return true;
	}
	
	
	/**
	 * <strong>Designed in Denmark for use with Frank Andersen</strong><br>
	 * <image src="https://scontent.xx.fbcdn.net/v/t1.0-1/p50x50/14141640_1089731647742374_5173933318183000699_n.jpg?oh=c01c01e697dd5c0bf66b2276d8024bc8&oe=5993E800" /><br>
	 * Sets the temporary PinPoints to be shown on the map.<br><br>
	 * Create them using:<br>
	 * <span style="font-family: monospace;"> PinPoint p = new PinPoint("text", coordinates);</span><br><br>
	 * And set the icon using:<br>
	 * <span style="font-family: monospace;"> p.setIconIndex(an integer);</span><br>
	 * @param pinPoints List of PinPoints to set as temporary visible. 
	 */
	public void setTemporaryPinPoints(List<PinPoint> pinPoints) {
		systemPinPoints.clear();
		for(int i=0; i<pinPoints.size(); i++) systemPinPoints.put(i+"", pinPoints.get(i));
	}
	
	/**
	 * Removes a PinPoint. 
	 * @param pinPoint <strong style="color:blue">PinPoint</strong> to remove. 
	 * @return If the PinPoint successfully was removed. 
	 */
	public boolean removePinPoint(PinPoint pinPoint) {
		if(pinPoints.containsValue(pinPoint)) {
			pinPoints.remove(pinPoint.getName());
			return true;
		} else return false;
	}
	
	/**
	 * Adds a PinPoint. A system PinPoint <strong>cannot</strong> be edited or removed by the user.<br>
	 * @param name The <strong style="color:blue">name</strong> of the PinPoint.
	 * @param pinPoint The actual <strong style="color:blue">PinPoint</strong>. 
	 * @return If the pinpoint successfully was added. 
	 */
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
