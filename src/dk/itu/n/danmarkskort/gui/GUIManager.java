package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{
	private static final long serialVersionUID = 8887124630631749354L;
	Style style;
	private TopPanel topPanel;
	private BottomPanel bottomPanel;

    /**
     *
     */
    public GUIManager() {
        style = Main.style;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(style.margin(), style.margin(), style.margin(), style.margin()));
        topPanel = new TopPanel();
        add(topPanel, BorderLayout.NORTH);
        bottomPanel = new BottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        setOpaque(false);
    }
    
    public TopPanel getTopPanel() {
    	return topPanel;
    }

    public void repaintGUI() {
        topPanel.repaintPanels();
        bottomPanel.repaintPanels();
    }
}
