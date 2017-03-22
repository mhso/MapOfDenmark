package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeListener;

public class CustomDropdown extends JPopupMenu {
    public CustomDropdown() {
        setFocusable(false);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder());
    }
    
    /**
     * Hide the Dropdown-Menu.
     */
    public void hideDropdown() {
        setVisible(false);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        Main.window.revalidate();
        Main.window.repaint();
    }
}
