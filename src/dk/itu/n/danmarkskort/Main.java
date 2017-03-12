package dk.itu.n.danmarkskort;

<<<<<<< HEAD
import javax.swing.*;
=======
import dk.itu.n.danmarkskort.backend.OSMParser;
import dk.itu.n.danmarkskort.backend.TileController;
>>>>>>> 35f502f61c718bfe8d364ff1ae03bcb8d4034159

public class Main {

	public final static String APP_NAME = "Ceci n'est pas une carte";
	public final static String APP_VERSION = "0.1";
	public final static boolean debug = true;
	public final static boolean production = false;
	public static OSMParser osmParser;
	public static TileController tileController;

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            startup();
            main();
            makeFrame();
        });
	}

    public static void makeFrame() {
        JFrame window = new JFrame("Ceci n'est pas une carte");
        window.add(new MainCanvas());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
    }

	public static void log(String text) {
		System.out.println("[" + APP_NAME + " " + APP_VERSION + "] " + text);
	}

	public static void startup() {
		log("Application started.");
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
