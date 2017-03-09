package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.TileController;

public class Main {

	public final static String APP_NAME = "FrankMaps";
	public final static String APP_VERSION = "0.1";
	public final static boolean debug = true;
	public final static boolean production = false;
	public static OSMParser osmParser;
	public static TileController tileController;
	
	public static void main(String[] args) {
		startup(args);
		Main.log("Application succesfully started.");
		main();
		shutdown();
		Main.log("Application ended.");
	}
		
	public static void startup(String[] args) {
		if(args.length == 1) osmParser = new OSMParser(args[0]);
		else osmParser = new OSMParser();
		tileController = new TileController();
	}
	
	public static void main() {
		
	}
	
	public static void shutdown() {
		
	}

	public static void log(String text) {
		if(debug) System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text);
	}
	
	public static void logRamUsage() {
		float usage = Util.getRAMUsageInMB();
		log("RAM usage: " + usage + "MB");
	}
	
}
