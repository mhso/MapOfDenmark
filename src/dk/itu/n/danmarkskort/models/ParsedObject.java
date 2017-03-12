package dk.itu.n.danmarkskort.models;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import dk.itu.n.danmarkskort.Util;

public class ParsedObject {
	public Map<String, String> attributes = new HashMap<String, String>();
	
	public void addAttribute(String key, String value) {
		attributes.put(key, value);
	}
	
	public void addAttributes(Attributes atts) {
		for(int i=0; i<atts.getLength(); i++) {
			addAttribute(atts.getQName(i), atts.getValue(i));
		}
	}
	
	public void printAttributes() {
		Util.printMap(attributes);
	}
	
}
