package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import dk.itu.n.danmarkskort.gui.components.CustomDropdown;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DropdownAddressSearch extends CustomDropdown {
	private static final long serialVersionUID = -9057099852028144701L;
	private Style style;
	private int selectedIndex = -1;
	private Component widthComponent;
	
    /**
     * Create the panel.
     */
    public DropdownAddressSearch(Component widthComponent, Style style) {
    	this(style);
    	this.widthComponent = widthComponent; 
    }
    
    public DropdownAddressSearch(Style style) {
    	this.style = style;
    	 UIManager.put("MenuItem.background", style.dropdownItemBG());
         UIManager.put("MenuItem:foreground", style.dropdowItemTextColor());
         UIManager.put("MenuItem.selectionBackground", style.dropdownItemBGActive());
         UIManager.put("MenuItem.selectionForeground", style.dropdownItemTextColorActive());
    }

    /**
     * Adds an element to the dropdown
     */
	public void addElement(JTextField inputField, String text) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setPreferredSize(new Dimension(widthComponent.getSize().width - 2, 
				menuItem.getPreferredSize().height));
		menuItem.addActionListener(e -> {
			inputField.setText(menuItem.getText());
			setVisible(false);
		});
		menuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				int index = getComponentIndex(menuItem);
				setSelectedElement(index);
				
			}
		});
		menuItem.setBorderPainted(false);
		add(menuItem);
	}
	
	public void itemClicked() {
		JMenuItem selectedItem = (JMenuItem) getComponent(getSelectedIndex());
		selectedItem.doClick();
	}
	
	public boolean isEmpty() {
		return getComponents().length == 0;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void setSelectedElement(int index) {
		if(getSelectedIndex() > -1) {
			JMenuItem oldSelectedItem = (JMenuItem) getComponent(selectedIndex);
			oldSelectedItem.setSelected(false);
			oldSelectedItem.setBackground(style.dropdownItemBG());
			oldSelectedItem.setForeground(style.dropdowItemTextColor());
		}
		JMenuItem selectedItem = (JMenuItem) getComponent(index);
		selectedItem.setBackground(style.dropdownItemBGActive());
		selectedItem.setForeground(style.dropdownItemTextColorActive());
		selectedIndex = index;
	}
	
	public void setWidthComponent(Component widthComponent) {
		this.widthComponent = widthComponent;
	}

	/**
	 * Show the Dropdown-Menu at the specified Component.
	 * @param source The component under which this menu should be shown.
	 */
	public void showDropdown(Component source) {
		selectedIndex = -1;
		show(source, 0, source.getHeight());
	}

	@Override
	protected void onClick(String text) {
		
	}
}