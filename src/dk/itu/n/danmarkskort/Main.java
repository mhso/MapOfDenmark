package dk.itu.n.danmarkskort;

import javax.swing.*;

import dk.itu.n.danmarkskort.GUI.WindowParsingLoadscreen;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.TileController;

public class Main {

	public final static String APP_NAME = "Map";
	public final static String APP_VERSION = "0.2";
	public final static boolean debug = true;
	public final static boolean production = false;
	public static OSMParser osmParser;
	public static TileController tileController;

	public static void main(String[] args) {
        startup(args);
        main();
        shutdown();
	}

	public static void startup(String[] args) {
		osmParser = new OSMParser();
		tileController = new TileController();
		prepareParser(args);
	}

	public static void prepareParser(String[] args) {
		WindowParsingLoadscreen loadScreen = new WindowParsingLoadscreen();
		LoadScreenThread r = new LoadScreenThread(loadScreen);
		
		// Add your listeners for the parser here, if you are going to use data. 
		osmParser.addListener(AddressController.getInstance());
		osmParser.addListener(loadScreen);
		osmParser.addListener(tileController);
		
		if(args.length == 1) {
			r.setFilenameAndRun(args[0]);
			osmParser.parseFile(args[0]);
		}
	}
	
	public static void main() {
		makeFrame();
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

    public static void makeFrame() {
            JFrame window = new JFrame(APP_NAME);
            window.add(new MainCanvas());
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
    }
    
    private static class LoadScreenThread implements Runnable {
    	private WindowParsingLoadscreen loadScreen;
    	private String fileName;
    	
    	public LoadScreenThread(WindowParsingLoadscreen loadScreen) {
    		this.loadScreen = loadScreen;
    	}
    	
    	public void setFilenameAndRun(String fileName) {
    		this.fileName = fileName;
    		run();
    	}
    	
		@Override
		public void run() {
			loadScreen.initialize(fileName);
		}
    	
    }
}
