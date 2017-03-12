package dk.itu.n.danmarkskort;

import dk.itu.n.danmarkskort.GUI.GUIManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainCanvas extends JPanel {

    public static final int WIDTH = 1000, HEIGHT = 800;
    private final Color canvasBG = new Color(146, 171, 200);
    BufferedImage canvasImg;

    public MainCanvas() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        canvasImg = null;
        try {
            canvasImg = ImageIO.read(new File("resources/satellite.jpeg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        addGUI();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics g2d = g;
        if(canvasImg != null) {
            g2d.drawImage(canvasImg, 0, 0, this);
        } else {
            g2d.setColor(canvasBG);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
        }
    }

    private void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }
}