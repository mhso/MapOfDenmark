package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.*;

public abstract class CustomDropdown extends JPopupMenu {
	
	private static final long serialVersionUID = 6967771850567290975L;
	private Point2D geographical;
	
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
        if(b) geographical = Main.map.getGeographicalMousePosition();
    }
    
    public void addItem(String text) {
    	add(new CustomDropdownMenuItem(this, text));
    }
    
    public Point2D getGeographical() {
    	return geographical;
    }
    
    // Fired when the user clicks a string in the popup menu. 
    protected abstract void onClick(String text);
    
    private class CustomDropdownMenuItem extends JMenuItem {
		private static final long serialVersionUID = -5771460361957758585L;

		public CustomDropdownMenuItem(CustomDropdown dropdown, String text) {
    		super(text);
    		CustomDropdownMenuItem self = this;
			ActionListener menuListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dropdown.onClick(self.getText());
				}
			};
			this.addActionListener(menuListener);
    	}
    }
    
}
