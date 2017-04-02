package dk.itu.n.danmarkskort.gui.map;

import java.io.Serializable;
import java.util.HashMap;

import dk.itu.n.danmarkskort.Util;

public class PinPointer implements Serializable {

	private static final long serialVersionUID = 8260486034670292677L;
	private HashMap<String, PinPoint> pinPoints = new HashMap<String, PinPoint>();
	private MapCanvas canvas;
	
	public PinPointer(MapCanvas canvas) {
		this.canvas = canvas;
	}
	
	public boolean addPinPoint(String name, PinPoint pinPoint) {
		if(pinPoints.containsKey(name)) return false;
		pinPoints.put(name, pinPoint);
		return true;
	}
	
	public PinPoint getPinPoint(String name) {
		return pinPoints.get(name);
	}
	
	public void panToLocation(PinPoint pinPoint) {
		canvas.purePanToPosition(pinPoint.getLocation());
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
	
	public static PinPointer load() {
		return (PinPointer) Util.readObjectFromFile("pinpoints.bin");
	}
	
	
}
