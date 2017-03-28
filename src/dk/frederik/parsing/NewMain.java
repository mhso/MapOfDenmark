package dk.frederik.parsing;

import java.io.FileInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import dk.itu.n.danmarkskort.TimerUtil;
import dk.itu.n.danmarkskort.Util;

public class NewMain {

	public static void main(String args[]) {
		TimerUtil timer = new TimerUtil();
		timer.on();
		parseOSM(args[0]);
		log("RAM usage: " + Util.getRAMUsageInMB() + "MB");
		timer.off();
	}
	
	private static void parseOSM(String fileName) {
		try {
			InputSource inputStream = new InputSource(new FileInputStream(fileName));
			XMLReader reader = XMLReaderFactory.createXMLReader();
			OSMHandler handler = new OSMHandler();
			reader.setContentHandler(handler);
			reader.parse(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void log(Object obj) {
		System.out.println(obj);
	}
	
}
