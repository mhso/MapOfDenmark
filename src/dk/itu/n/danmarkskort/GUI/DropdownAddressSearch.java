package dk.itu.n.danmarkskort.GUI;

import javax.swing.*;

import java.awt.*;

public class DropdownAddressSearch extends JPopupMenu {

	private JTextField txtField;
	private TopPanel topPanel;

    /**
     * Create the panel.
     */
    public DropdownAddressSearch(TopPanel topPanel) {
        setFocusable(false);
        this.topPanel = topPanel;
        setBorderPainted(false);
    }

    /**
     * Adds an element to the dropdown
     */
	public void addElement(String text) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setPreferredSize(new Dimension(topPanel.getInputFieldWidth() - 2, (int) menuItem.getPreferredSize().getHeight()));
        menuItem.addActionListener(e -> {
            topPanel.getInputField().setText(menuItem.getText());
            hideDropdown();
        });
        menuItem.setBorderPainted(false);
        add(menuItem);

        /*
		JButton buttonResult = new JButton(text);
        buttonResult.setPreferredSize(new Dimension(topPanel.getInputFieldWidth(), buttonResult.getPreferredSize().height));
        setSharedButtonAttributes(buttonResult);
		add(buttonResult);*/
	}

    /**
     * Clear the Dropdown-Menu.
     */
	public void clearElements() {
		removeAll();
	}

	/*
	private void setSharedButtonAttributes(JButton dropdownButton) {
		dropdownButton.addActionListener(e -> {
			topPanel.getInputField().setText(dropdownButton.getText());
			hideDropdown();
		});
		dropdownButton.setFocusPainted(false);
		dropdownButton.setBorderPainted(false);
		dropdownButton.setBackground(new Color(0, 0, 0, 0));
	}*/
	
	/**
	 * Hide the Dropdown-Menu.
	 */
	public void hideDropdown() {
		setVisible(false);
	}

	/**
	 * Show the Dropdown-Menu at the specified Component.
	 * @param source The component under which this menu should be shown.
	 */
	public void showDropdown(Component source) {
		Point loc = source.getLocation();
		show(source, loc.x, loc.y+source.getHeight());
	}

	// is it safe to remove the overridden paintComponent?
	/*
	@Override
	public void paintComponent(Graphics g) {
		// We have to override this method and add a 'repaint()' to it, otherwise transparent
		// JButtons cause trouble.
		super.paintComponent(g);
		//repaint();
	}*/

	/* Not used in this class anymore
	private class SearchFilter extends DocumentFilter {
		@Override
		public void replace(FilterBypass fb, int offset, int length, String newText, 
				AttributeSet attr) throws BadLocationException {
			
			super.replace(fb, offset, length, newText, attr);
			
			showDropdown(txtField);
		}
	}*/
}