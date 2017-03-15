package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.GUI.GUIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 1000, HEIGHT = 800;
    private final Color canvasBG = new Color(163, 204, 255);
    private final Color debugBackgroundColor = new Color(255, 240, 224);
    
    AffineTransform transform = new AffineTransform();
	boolean antiAlias;
	
    public MainCanvas() {
        setLayout(new BorderLayout());
        setOpaque(false);
        addGUI();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(Main.debug) drawDebug(g2d);   
    }
    
    public void drawDebug(Graphics2D g2d) {
    	g2d.setColor(debugBackgroundColor);
    	g2d.fillRect(5, 10, 64, 50);
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect(5, 10, 64, 50);
    	g2d.drawString("X: " + 0 + ", Y: " + 0, 10, 30);
    	g2d.drawString("Zoom: " + 1, 10, 45);
    }

    private void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }
    
}