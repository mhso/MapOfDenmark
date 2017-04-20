package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{
	private static final long serialVersionUID = 8887124630631749354L;
	Style style;
	private TopPanel topPanel;

    /**
     *
     */
    public GUIManager() {
        style = new Style();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(style.margin(), style.margin(), style.margin(), style.margin()));
        topPanel = new TopPanel(style);
        add(topPanel, BorderLayout.NORTH);
        add(new BottomPanel(style), BorderLayout.SOUTH);
        setOpaque(false);
    }
    
    public TopPanel getTopPanel() {
    	return topPanel;
    }
}
