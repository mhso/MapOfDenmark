package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import java.awt.*;

public class GUIManager extends JPanel{
	private static final long serialVersionUID = 8887124630631749354L;
	Style style;

    /**
     *
     */
    public GUIManager() {
        style = new Style();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(style.margin(), style.margin(), style.margin(), style.margin()));
        add(new TopPanel(style), BorderLayout.NORTH);
        add(new BottomPanel(style), BorderLayout.SOUTH);
        setOpaque(false);
    }
}
