package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import java.awt.*;

public class SettingsPage extends JPanel {

    public SettingsPage() {

        setPreferredSize(new Dimension(100, 800));
        setOpaque(false);

        JLabel lblPageHeadline = new JLabel("Settings Page");
        add(lblPageHeadline);
    }

}
