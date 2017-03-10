package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.GUI.GUIManager;

import javax.swing.*;
import java.awt.*;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 800, HEIGHT = 600;
    private final Color canvasBG = new Color(146, 171, 200);

    public MainCanvas() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        addGUI();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics g2d = (Graphics2D) g;
        System.out.println("JEG HADER SWING");
        g2d.setColor(canvasBG);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void addGUI() {
        GUIManager gui = new GUIManager(this);
        add(gui);
    }
}