package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DropdownAddressSearch extends CustomDropdown {

	private JTextField txtField;
	private Style style;
	private int selectedIndex = -1;
	private Component widthComponent;
	
    /**
     * Create the panel.
     */
    public DropdownAddressSearch(Component widthComponent, Style style) {
        this.style = style;
        this.widthComponent = widthComponent;
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
		menuItem.setPreferredSize(new Dimension(widthComponent.getPreferredSize().width - 2, 
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

	/**
	 * Show the Dropdown-Menu at the specified Component.
	 * @param source The component under which this menu should be shown.
	 */
	public void showDropdown(Component source) {
		Point loc = source.getLocation();
		show(source, loc.x, loc.y+source.getHeight());
	}
}