package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.GUI.GUIManager;

import javax.swing.*;
import java.awt.*;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 1000, HEIGHT = 800;
    private final Color canvasBG = new Color(73, 146, 94);

    public MainCanvas() {
        setPreferredSize(new Dimension(1000, 800));
        setLayout(new BorderLayout());
        addGUI();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics g2d = g;
        g2d.setColor(canvasBG);
        g2d.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);
    }

    private void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }
}