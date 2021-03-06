package dk.itu.n.danmarkskort.gui.components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.DropdownAddressSearch;

public class SearchField extends JTextField {
	private static final long serialVersionUID = 7828180981089742062L;	

	public SearchField(DropdownAddressSearch dropSuggestions, List<String> dropSuggestionsList, JButton searchButton) {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!dropSuggestions.isEmpty()) {
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(dropSuggestions.getSelectedIndex() < dropSuggestions.getComponents().length-1) {	
							dropSuggestions.setSelectedElement(dropSuggestions.getSelectedIndex()+1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP) {
						if(dropSuggestions.getSelectedIndex() > -1) {
							dropSuggestions.setSelectedElement(dropSuggestions.getSelectedIndex()-1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(dropSuggestions.getSelectedIndex() > -1 && dropSuggestions.isVisible()) dropSuggestions.itemClicked();
						else if(searchButton != null) searchButton.doClick();
					}
					else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
						if(dropSuggestions.getSelectedIndex() > -1 && dropSuggestions.isVisible()) {
							setText(dropSuggestionsList.get(dropSuggestions.getSelectedIndex()));
						}
					}
				}
			}
        });
	}
}
