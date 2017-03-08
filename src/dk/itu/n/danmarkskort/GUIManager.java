package dk.itu.n.danmarkskort;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{

    public final static int
            TOP_PANEL_WIDTH = 400,
            TOP_PANEL_HEIGHT = 50,
            STANDARD_MARGIN = 40,
            SMALL_MARGIN = 10;

    public GUIManager() {
        setBounds(0, 0, MainPanel.WIDTH, MainPanel.HEIGHT);
        addTopPanel();
        setOpaque(false);
    }

    public void addTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(255, 255, 255, 70));
        topPanel.setPreferredSize(new Dimension(TOP_PANEL_WIDTH, TOP_PANEL_HEIGHT));
        topPanel.setBounds((MainPanel.WIDTH / 2) - (TOP_PANEL_WIDTH / 2),
                STANDARD_MARGIN,
                TOP_PANEL_WIDTH,
                TOP_PANEL_HEIGHT);
        add(topPanel);
    }


}
