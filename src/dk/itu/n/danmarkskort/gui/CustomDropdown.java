package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;

import javax.swing.*;

public class CustomDropdown extends JPopupMenu {
	private static final long serialVersionUID = 6967771850567290975L;

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
