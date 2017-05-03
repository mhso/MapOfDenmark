package dk.itu.n.danmarkskort;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.OSMReader;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.WindowLauncher;
import dk.itu.n.danmarkskort.gui.WindowParsingLoadscreenNew;
import dk.itu.n.danmarkskort.gui.map.MapCanvas;
import dk.itu.n.danmarkskort.gui.map.PinPointManager;
import dk.itu.n.danmarkskort.gui.map.multithreading.TileController;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.ReuseStringObj;
import dk.itu.n.danmarkskort.models.UserPreferences;
import dk.itu.n.danmarkskort.routeplanner.RouteController;

public class Main {
	public final static String APP_NAME = "Yak Maps";
	public final static String APP_VERSION = "0.8";
	public final static boolean debug = true;
	public final static boolean debugExtra = false;
	public final static boolean production = false;
	public final static boolean buffered = false;
	public final static boolean saveParsedAddresses = true;
	public final static boolean nearest = true;

	public static boolean useLauncher = true;
	public static OSMReader osmReader;
	public static JFrame window;
	public static OSMParser model;
	public static AddressController addressController;
	public static MapCanvas map;
	public static MainCanvas mainPanel;
	public static PinPointManager pinPointManager;
	public static UserPreferences userPreferences;
	public static TileController tileController;
	public static RouteController routeController;
	public static Style style;
	
	public static void main(String[] args) {
		System.setProperty("awt.useSystemAAFontSettings","on");
		System.setProperty("swing.aatext", "true");
		userPreferences = (UserPreferences)Util.readObjectFromFile(DKConstants.USERPREF_PATH);
		if(userPreferences == null) {
			userPreferences = new UserPreferences();
			Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
		}
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
		
		routeController = new RouteController();
		style = new Style();
		if(args.length > 0) prepareParser(args);
		else prepareParser(new String[]{userPreferences.getDefaultMapFile()});
		if(userPreferences.getCurrentMapTheme() != null) {
			if(Main.production) GraphicRepresentation.parseData(
					Main.class.getResource("/resources/Theme" + userPreferences.getCurrentMapTheme() + ".XML").toString());
			else GraphicRepresentation.parseData("resources/Theme" + userPreferences.getCurrentMapTheme() + ".XML");
		}
		else {
			if(Main.production) GraphicRepresentation.parseData(
					Main.class.getResource("/resources/Theme" + userPreferences.getDefaultTheme() + ".XML").toString());
			else GraphicRepresentation.parseData("resources/Theme" + userPreferences.getDefaultTheme() + ".XML");
			userPreferences.setCurrentMapTheme(userPreferences.getDefaultTheme());
		}
	}

	public static void prepareParser(String[] args) {
		WindowParsingLoadscreenNew loadScreen = new WindowParsingLoadscreenNew(args[0]);
		osmReader.addOSMListener(loadScreen);
		osmReader.addInputListener(loadScreen);
		loadScreen.run();
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				osmReader.parseFile(args[0], model);
				ReuseStringObj.clear();
				if(window == null) main();
		        shutdown();
			}
		};
		new Thread(r, "ParseThread").start();
	}
	
	public static void main() {
		makeFrame();
	}

	public static void shutdown() {}

	public static void log(Object text) {
		if(debug) System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text.toString());
	}

	public static void logRamUsage() {
		float usage = Util.getRAMUsageInMB();
		log("RAM usage: " + usage + "MB");
	}

    public static void makeFrame() {
        window = new JFrame(APP_NAME);
        window.setIconImage(style.frameIcon());
        window.add(createFrameComponents());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
        if(userPreferences.isMaximizeOnStartup()) window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        window.addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {         
            	Main.windowResized(e);
            }public void componentHidden(ComponentEvent arg0) {}public void componentMoved(ComponentEvent arg0) {}public void componentShown(ComponentEvent arg0) {}
        });

        window.pack();
		window.setLocationRelativeTo(null);
        window.setVisible(true);
        map.setupDone();
    }
    
    public static void windowResized(ComponentEvent e) {
    	Main.tileController.setTileSize(window.getWidth(), window.getHeight());
    }
    
    public static JPanel createFrameComponents() {
    	JPanel overlay = new JPanel();
        overlay.setLayout(new OverlayLayout(overlay));
        overlay.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));

        map = new MapCanvas();
        tileController = new TileController();
        pinPointManager = PinPointManager.load(map);
        mainPanel = new MainCanvas();
        
        map.setPreferredSize(new Dimension(DKConstants.WINDOW_WIDTH, DKConstants.WINDOW_HEIGHT));
        overlay.add(mainPanel);
    	overlay.add(map);
    	return overlay;
    }
    
}
