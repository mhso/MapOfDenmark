package dk.itu.n.danmarkskort;

import javax.swing.*;

public class Main {

	public final static String APP_NAME = "Ceci n'est pas une carte";
	public final static String APP_VERSION = "0.1";
	
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
	}
	
	public static void main() {
		
	}
	
	public static void shutdown() {
		log("Application ended.");
	}

	
}
