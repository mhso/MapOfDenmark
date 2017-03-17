package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import java.awt.*;

public class DropdownAddressSearch extends CustomDropdown {

	private JTextField txtField;
	private TopPanel topPanel;
	private Style style;

    /**
     * Create the panel.
     */
    public DropdownAddressSearch(TopPanel topPanel, Style style) {
        this.topPanel = topPanel;
        this.style = style;

        UIManager.put("MenuItem.background", style.dropdownItemBG());
        UIManager.put("MenuItem:foreground", style.dropdowItemTextColor());
        UIManager.put("MenuItem.selectionBackground", style.dropdownItemBGActive());
        UIManager.put("MenuItem.selectionForeground", style.dropdownItemTextColorActive());
    }

    /**
     * Adds an element to the dropdown
     */
	public void addElement(String text) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setPreferredSize(new Dimension(topPanel.getInputFieldWidth() - 2, menuItem.getPreferredSize().height));
		menuItem.addActionListener(e -> {
			topPanel.getInputField().setText(menuItem.getText());
			setVisible(false);
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
		int selectedIndex = -1;
		int i = 0;
		for(Component c : getComponents()) {
			JMenuItem item = (JMenuItem) c;
			if(item.getBackground() == style.dropdownItemBGActive()) {
				selectedIndex = i;
				break;
			}
			i++;
		}
		return selectedIndex;
	}
	
	public void setSelectedElement(int index) {
		if(getSelectedIndex() > -1) {
			JMenuItem oldSelectedItem = (JMenuItem) getComponent(getSelectedIndex());
			oldSelectedItem.setBackground(style.dropdownItemBG());
			oldSelectedItem.setForeground(style.dropdowItemTextColor());
		}
		JMenuItem selectedItem = (JMenuItem) getComponent(index);
		selectedItem.setBackground(style.dropdownItemBGActive());
		selectedItem.setForeground(style.dropdownItemTextColorActive());
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