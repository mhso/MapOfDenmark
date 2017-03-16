package dk.itu.n.danmarkskort.models;

public class ParsedNode extends ParsedObject{

	private long id;
	
	public void parseAttributes() {
		id = Long.parseLong(attributes.get("id"));
	}
	
	public long getId() {
		return id;
	}
	
}
