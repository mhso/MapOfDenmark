package dk.itu.n.danmarkskort;

public class Main {

	public final static String APP_NAME = "FrankMaps";
	public final static String APP_VERSION = "0.1";
	
	public static void main(String[] args) {
		startup();
		main();
		shutdown();
	}
	
	
	public static void log(String text) {
		System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text);
	}
	
	public static void startup() {
		log("Application started.");
	}
	
	public static void main() {
		
	}
	
	public static void shutdown() {
		log("Application ended.");
	}

	
}
