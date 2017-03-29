package dk.itu.n.danmarkskort;

import javax.swing.*;

import dk.itu.n.danmarkskort.gui.GUIManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 1000, HEIGHT = 800;
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
    	g2d.fillRect(5, 10, 110, 128);
    	g2d.setColor(Color.BLACK);
    	g2d.drawRect(5, 10, 110, 128);
    	g2d.drawString("x1: " + Main.map.getGeographicalRegion().x1 + ", y1: " + Main.map.getGeographicalRegion().y1, 10, 30);
    	g2d.drawString("x2: " + Main.map.getGeographicalRegion().x2 + ", y2: " + Main.map.getGeographicalRegion().y2, 10, 45);
    	g2d.drawString("Zoom: " + String.format("%.01f", Main.map.getZoom()).replace(",", "."), 10, 60);
    	g2d.drawString("Shapes drawn: " + Main.map.shapesDrawn, 10, 75);
    }

    private void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }
    
}