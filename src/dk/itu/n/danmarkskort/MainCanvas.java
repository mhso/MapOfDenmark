package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.GUI.GUIManager;

import javax.swing.*;
import java.awt.*;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 1000, HEIGHT = 800;
    private final Color canvasBG = new Color(163, 204, 255);

    public MainCanvas() {
        setPreferredSize(new Dimension(1000, 800));
        setLayout(new BorderLayout());
        addGUI();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(canvasBG);
        g2d.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);
        if(Main.debug) drawDebug(g2d);
    }
    
    public void drawDebug(Graphics2D g2d) {
    	g2d.drawString("X: " + 0 + "Y: " + 0, 5, 5);
    	g2d.drawString("Zoom: " + 1, x, y);
    }

    private void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }
}