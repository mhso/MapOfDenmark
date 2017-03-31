package dk.itu.n.danmarkskort;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.TileController;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreenNew;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.lightweight.LightWeightParser;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;

public class Main {

	public final static String APP_NAME = "Map";
	public final static String APP_VERSION = "0.4";
	public final static boolean debug = true;
	public final static boolean production = false;
	public static OSMParser osmParser;
	public static TileController tileController;
	public static JFrame window;
	public static LightWeightParser model;
	public static MapCanvas map;
	public static MainCanvas mainPanel;

	public final static boolean lightweight = true;
	
	public static void main(String[] args) {
        startup(args);
        main();
        shutdown();
	}

	public static void startup(String[] args) {
		if(lightweight) {
			osmParser = new OSMParser();
			model = new LightWeightParser(osmParser);
			GraphicRepresentation.main(new String[]{"resources/ThemeBasic.XML"});
			prepareParser(args);
		} else {
			osmParser = new OSMParser();
			tileController = new TileController();
			prepareParser(args);
		}

	}

	public static void prepareParser(String[] args) {
		WindowParsingLoadscreenNew loadScreen = new WindowParsingLoadscreenNew();
		LoadScreenThread loadScreenThread = new LoadScreenThread(loadScreen);
		//osmParser.addListener(AddressController.getInstance());
		osmParser.addListener(loadScreen);
		if(!lightweight) {
			osmParser.addListener(tileController);
		}
		loadScreenThread.setFilenameAndRun(args[0]);
		osmParser.parseFile(args[0]);
	}
	
	public static void main() {
		makeFrame();
	}

	public static void shutdown() {

	}

	public static void log(Object text) {
		if(debug) System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text.toString());
	}

	public static void logRamUsage() {
		float usage = Util.getRAMUsageInMB();
		log("RAM usage: " + usage + "MB");
	}

    public static void makeFrame() {
            window = new JFrame(APP_NAME);
            window.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
            JPanel overlay = new JPanel();
            overlay.setLayout(new OverlayLayout(overlay));
            overlay.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
            mainPanel = new MainCanvas();
            map = new MapCanvas();
            map.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
            
            overlay.add(mainPanel);
            overlay.add(map);

            window.add(overlay);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            map.zoomToBounds();
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
