package dk.itu.n.danmarkskort.models;

public class ParsedNode extends ParsedObject{

	private long id;
	private float lat, lon;
	
	public void parseAttributes() {
		id = Long.parseLong(attributes.get("id"));
		lat = Float.parseFloat(attributes.get("lat"));
		lon = Float.parseFloat(attributes.get("lon"));
	}
	
	public long getId() { return id; }
	
	public float getLat() { return lat; }
	
	public float getLon() { return lon;	}
	
	public Coordinate getPosition() { return new Coordinate(lat, lon); }
	
}
