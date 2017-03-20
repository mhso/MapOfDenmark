package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import java.awt.*;

public class RoutePage extends JPanel {

    public RoutePage() {
        setPreferredSize(new Dimension(100, 800));
        setOpaque(false);

        JLabel lblPageHeadline = new JLabel("Find Route Page");
        add(lblPageHeadline);
    }

}
