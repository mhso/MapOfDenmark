package dk.itu.n.danmarkskort;

import java.awt.Dimension;

import javax.swing.*;

import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.TileController;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreen;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreenNew;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;

public class Main {

	public final static String APP_NAME = "Map";
	public final static String APP_VERSION = "0.2";
	public final static boolean debug = true;
	public final static boolean production = false;
	public final static int WIDTH = 1000, HEIGHT = 800;
	public static OSMParser osmParser;
	public static TileController tileController;
	public static JFrame window;
	public static MapCanvas map;
	public static MainCanvas mainPanel;
	
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
		WindowParsingLoadscreenNew loadScreen = new WindowParsingLoadscreenNew();
		LoadScreenThread loadScreenThread = new LoadScreenThread(loadScreen);
		
		// Add your listeners for the parser here, if you are going to use data. 
		osmParser.addListener(AddressController.getInstance());
		osmParser.addListener(loadScreen);
		osmParser.addListener(tileController);
		
		if(args.length == 1) {
			loadScreenThread.setFilenameAndRun(args[0]);
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
            window = new JFrame(APP_NAME);
            JPanel overlay = new JPanel();
            overlay.setLayout(new OverlayLayout(overlay));
            overlay.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            mainPanel = new MainCanvas();
            map = new MapCanvas();
            map.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            overlay.add(mainPanel);
            
            overlay.add(map);

            window.add(overlay);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
    }
    
    private static class LoadScreenThread implements Runnable {
    	private WindowParsingLoadscreenNew loadScreen;
    	private String fileName;
    	
    	public LoadScreenThread(WindowParsingLoadscreenNew loadScreen) {
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
