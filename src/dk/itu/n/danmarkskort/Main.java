package dk.itu.n.danmarkskort;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import org.xml.sax.InputSource;

import dk.itu.n.danmarkskort.backend.LightWeightParser;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreenNew;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.gui.map.PinPointManager;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;

public class Main {

	public final static String APP_NAME = "Map";
	public final static String APP_VERSION = "0.5";
	public final static boolean debug = true;
	public final static boolean production = false;
	public final static boolean buffered = true;
	
	public static OSMParser osmParser;
	public static JFrame window;
	public static LightWeightParser model;
	public static MapCanvas map;
	public static MainCanvas mainPanel;
	public static PinPointManager pinPointManager;
	
	public static void main(String[] args) {
        startup(args);
        main();
        shutdown();
	}

	public static void startup(String[] args) {
		if(window != null) window.getContentPane().removeAll();
		osmParser = new OSMParser();
		model = new LightWeightParser(osmParser);
		GraphicRepresentation.parseData(new InputSource("resources/ThemeBasic.XML"));
		prepareParser(args);
	}

	public static void prepareParser(String[] args) {
		WindowParsingLoadscreenNew loadScreen = new WindowParsingLoadscreenNew();
		LoadScreenThread loadScreenThread = new LoadScreenThread(loadScreen);
		osmParser.addListener(loadScreen);
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
        
        window.add(createFrameComponents());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        map.zoomToBounds();
        
    }
    
    public static JPanel createFrameComponents() {
    	JPanel overlay = new JPanel();
        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
        
    	map = new MapCanvas();
        map.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
        pinPointManager = PinPointManager.load(map);
        mainPanel = new MainCanvas();
        overlay.add(mainPanel);
    	overlay.add(map);
    	return overlay;
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
