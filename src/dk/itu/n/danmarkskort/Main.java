package dk.itu.n.danmarkskort;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.OSMReader;
import dk.itu.n.danmarkskort.gui.WindowLauncher;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreenNew;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.gui.map.PinPointManager;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.UserPreferences;

public class Main {

	public final static String APP_NAME = "M&F's Magic Maps";
	public final static String APP_VERSION = "0.6";
	public final static boolean debug = true;
	public final static boolean production = false;
	public final static boolean buffered = true;
	public final static boolean saveParsedAddresses = true;
	public final static boolean useLauncher = true;
	public static final boolean binaryfile = true;
	
	public static boolean forceParsing;
	public static OSMReader osmReader;
	public static JFrame window;
	public static OSMParser model;
	public static AddressController addressController;
	public static MapCanvas map;
	public static MainCanvas mainPanel;
	public static PinPointManager pinPointManager;
	public static UserPreferences userPreferences;
	
	public static void main(String[] args) {
		userPreferences = (UserPreferences)Util.readObjectFromFile(DKConstants.USERPREF_PATH);
		if(userPreferences == null) {
			userPreferences = new UserPreferences();
			Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
		}
		forceParsing = userPreferences.isForcingParsing();
        if(useLauncher || args.length < 1) new WindowLauncher();
        else launch(args);
	}
	
	public static void launch(String[] args) {
		startup(args);
		
	}

	public static void startup(String[] args) {
		if(window != null) window.getContentPane().removeAll();
		addressController  =  new AddressController();
		osmReader = new OSMReader();
		model = new OSMParser(osmReader);
		if(args.length > 0) prepareParser(args);
		else prepareParser(new String[]{userPreferences.getDefaultMapFile()});
		if(userPreferences.getCurrentMapTheme() != null) {
			GraphicRepresentation.parseData("resources/Theme" + userPreferences.getCurrentMapTheme() + ".XML");
		}
		else {
			GraphicRepresentation.parseData("resources/Theme" + userPreferences.getDefaultTheme() + ".XML");
			userPreferences.setCurrentMapTheme(userPreferences.getDefaultTheme());
		}
	}

	public static void prepareParser(String[] args) {
		WindowParsingLoadscreenNew loadScreen = new WindowParsingLoadscreenNew(args[0]);
		osmReader.addOSMListener(loadScreen);
		osmReader.addInputListener(loadScreen);
		loadScreen.run();
		
		System.out.println(Thread.currentThread());
		Runnable r = new Runnable() {
			@Override
			public void run() {
				osmReader.parseFile(args[0]);
				main();
		        shutdown();
			}
		};
		new Thread(r, "lol").start();
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
        window.setBackground(new Color(110, 192, 255));
        window.add(createFrameComponents());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));  
        
        window.pack();
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        map.setupDone();
    }
    
    public static JPanel createFrameComponents() {
    	JPanel overlay = new JPanel();
        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));

        map = new MapCanvas();
        pinPointManager = PinPointManager.load(map);
        mainPanel = new MainCanvas();
        
        map.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
        mainPanel = new MainCanvas();
        overlay.add(mainPanel);
    	overlay.add(map);
    	return overlay;
    }
}
