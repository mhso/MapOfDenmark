package dk.itu.n.danmarkskort;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public static final int WIDTH = 800, HEIGHT = 600;

    public MainPanel() {

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        addGUI();
        addCanvas();
    }

    public void addCanvas() {
        // this is a placeholder, until we have a canvas to fill with
        JPanel background = new JPanel();
        background.setBackground(new Color(98, 146, 196));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        add(background);
    }

    public void addGUI() {
        GUIManager gui = new GUIManager();
        add(gui);
    }

}
