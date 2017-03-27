package dk.itu.n.danmarkskort.models;

public class ParsedOSM extends ParsedObject{
	
	private String version, generator;

	public String getVersion() {
		return version;
	}

	public String getGenerator() {
		return generator;
	}
	
	public void parseAttributes() {
		version = attributes.get("version");
		generator = attributes.get("generator");
	}
	
}
