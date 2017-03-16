package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import java.awt.*;

public class MapLayersPage extends JPanel {

    // should the class name be filters?
    public MapLayersPage() {
        setPreferredSize(new Dimension(80, 800));
        setOpaque(false);

        JLabel nothing = new JLabel("Map Layers Page");
        add(nothing);
    }


}
