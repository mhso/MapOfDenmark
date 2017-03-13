package dk.itu.n.danmarkskort.models;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import dk.itu.n.danmarkskort.Util;

public abstract class ParsedObject {
	public Map<String, String> attributes = new HashMap<String, String>();
	private String qName;
	
	public ParsedObject() {}
	public ParsedObject(ParsedObject object) {
		attributes = new HashMap<String, String>(object.getAttributes());
		qName = object.qName;
	}
	
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	public void addAttributes(Attributes atts) {
		for(int i=0; i<atts.getLength(); i++) {
			addAttribute(atts.getQName(i), atts.getValue(i));
		}
	}
	
	public abstract void parseAttributes();
	
	public void setQName(String qName) {
		this.qName = qName;
	}
	
	public String getQName() {
		return this.qName;
	}
	
	public void printAttributes() {
		Util.printMap(attributes);
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
