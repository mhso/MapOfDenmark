package dk.itu.n.danmarkskort;

public class Main {

	public static String appName = "";
	public static String appVersion = "";
	
	public static void main(String[] args) {
		startup();
		main();
		shutdown();
	}
	
	
	public static void log(String text) {
		System.out.println("[" + appName + " " + appVersion + "] " + text);
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
