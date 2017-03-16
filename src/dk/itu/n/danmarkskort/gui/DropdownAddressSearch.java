package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import java.awt.*;

public class DropdownAddressSearch extends CustomDropdown {

	private JTextField txtField;
	private TopPanel topPanel;

    /**
     * Create the panel.
     */
    public DropdownAddressSearch(TopPanel topPanel) {
        this.topPanel = topPanel;
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

	/**
	 * Show the Dropdown-Menu at the specified Component.
	 * @param source The component under which this menu should be shown.
	 */
	public void showDropdown(Component source) {
		Point loc = source.getLocation();
		show(source, loc.x, loc.y+source.getHeight());
	}
}