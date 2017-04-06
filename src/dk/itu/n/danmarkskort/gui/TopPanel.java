package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.gui.menu.DropdownMenu;
import dk.itu.n.danmarkskort.gui.menu.RoutePage;
import dk.itu.n.danmarkskort.search.SearchController;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class TopPanel extends JPanel {
	private static final long serialVersionUID = -3413495967270668324L;
	private Style style;
    private DropdownAddressSearch dropSuggestions;
    private DropdownMenu dropMenu;
    private JTextField input;
    private JPanel searchInputWrapper, topParent;
    private JButton menu;
    private List<String> dropSuggestionsList;

    public TopPanel(Style style) {
    	dropSuggestionsList = new ArrayList<String>();
        this.style = style;
        setOpaque(false);

        topParent = new JPanel(new BorderLayout());
        topParent.setBorder(BorderFactory.createLineBorder(style.panelBG(), style.topPanelBorderWidth(), false));
        topParent.setOpaque(false);

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(style.panelBG());
        top.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.weighty = 1;

        menu = style.menuButton();
        menu.setToolTipText("Menu");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        input = new JTextField(30);
        input.setBorder(BorderFactory.createEmptyBorder(style.smallMargin(), style.smallMargin(), style.smallMargin(), style.smallMargin()));
        input.setFont(new Font("sans serif", Font.PLAIN, 14));
        input.setForeground(style.panelTextColor());
        input.setOpaque(false);
        input.setBackground(new Color(0,0,0,0));
        input.setCaretColor(style.panelTextColor());
        input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!dropSuggestions.isEmpty()) {
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(dropSuggestions.getSelectedIndex() < dropSuggestions.getComponents().length-1) {	
							dropSuggestions.setSelectedElement(dropSuggestions.getSelectedIndex()+1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP) {
						if(dropSuggestions.getSelectedIndex() > 0) {
							dropSuggestions.setSelectedElement(dropSuggestions.getSelectedIndex()-1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(dropSuggestions.getSelectedIndex() > 0) {
							dropSuggestions.itemClicked();
						}
					}
				}
			}
        	
        });

        JButton search = style.searchButton();
        search.setToolTipText("Search");

        searchInputWrapper = new JPanel(new GridBagLayout());
        searchInputWrapper.setBackground(style.inputFieldBG());
        GridBagConstraints searchInputGBC = new GridBagConstraints();

        searchInputWrapper.add(input, searchInputGBC);
        searchInputGBC.insets = new Insets(0, 0, 0, 10);
        searchInputWrapper.add(search, searchInputGBC);

        gbc.insets = new Insets(0, 6, 0, 6);
        gbc.weightx = 1;
        gbc.gridx = 1;
        top.add(searchInputWrapper, gbc);

        JButton route = style.routeButton();
        route.setToolTipText("Directions");

        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.gridx = 2;
        top.add(route, gbc);

        topParent.add(top);
        add(topParent);
        
        dropSuggestions = new DropdownAddressSearch(searchInputWrapper, style);
        dropMenu = new DropdownMenu(this, style);
        
        menu.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!dropMenu.isVisible()) {
					dropMenu.blockVisibility(false);
					dropMenu.showDropdown(menu);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dropMenu.blockVisibility(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(dropMenu.isVisible()) dropMenu.blockVisibility(false);
			}
        });
        menu.addActionListener(e -> {
        	if(dropMenu.isVisible() && dropMenu.isPopUpBlocked()) {
				dropMenu.blockVisibility(false);
				dropMenu.setVisible(false);
        	}
        });
        
        route.addActionListener(e -> {
            dropMenu.addToContentPane(new RoutePage(dropMenu, input.getText()));
            dropMenu.showDropdown(menu);
        });

        search.addActionListener(e -> searchForAddress(input.getText()));

        // adding drop down functionality
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new TopPanel.SearchFilter());
    }

    // Skal nok flyttes senere, for at overholde MVC
    public void searchForAddress(String address) {
    	if(!address.trim().isEmpty()) {
    		Address addr = SearchController.getInstance().getSearchFieldAddressObj(address);
        	if(addr != null ) { 
        		System.out.println("Toppanel->searchForAddress: "+addr.toString());
        		panZoomToCoordinates(addr.getLonLat());
        	} else {
        		JOptionPane.showMessageDialog(null, "No match found.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
        	}
    	} else {
    		JOptionPane.showMessageDialog(null, "Search field can't be empty.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

    private void panZoomToCoordinates(float[] lonLat) {
		// TODO Auto-generated method stub
    	System.out.println("Toppanel->panZoomToCoordinats (lon, lat): (" + lonLat[0] + ", " + lonLat[1] + ")");
    	Main.map.panToPosition(new Point2D.Float(lonLat[0], lonLat[1]));
    	Main.mainPanel.repaint();
	}

	public void populateSuggestions(List<String> list) {
        dropSuggestions.setVisible(false);
        dropSuggestions.removeAll();
        for(String str : list){
            dropSuggestions.addElement(input, str);
        }
        dropSuggestions.showDropdown(input);
    }

    public int getMenuWidth() {
        return menu.getPreferredSize().width + style.topPanelBorderWidth() + 12;
    } // 6 is from insets

    public Dimension getTopPanelDimension() {
        return topParent.getPreferredSize();
    }

    private class SearchFilter extends DocumentFilter {
        /**
         * Invoked prior to removal of the specified region in the
         * specified Document. Subclasses that want to conditionally allow
         * removal should override this and only call supers implementation as
         * necessary, or call directly into the <code>FilterBypass</code> as
         * necessary.
         *
         * @param fb     FilterBypass that can be used to mutate Document
         * @param offset the offset from the beginning &gt;= 0
         * @param length the number of characters to remove &gt;= 0
         * @throws BadLocationException some portion of the removal range
         *                              was not a valid part of the document.  The location in the exception
         *                              is the first bad position encountered.
         */
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
            dropdownSuggestions(offset - 1, input.getText());
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String newText,
                            AttributeSet attr) throws BadLocationException {

            super.replace(fb, offset, length, newText, attr);
            dropdownSuggestions(offset, input.getText());
        }

        public void dropdownSuggestions(int offset, String text) {
        	
            if(offset > 1 && text.length() > 1) {
            	
//            	char charAtEnd = text.charAt(text.length()-1);
//            	int countSameCharAtPos = 0;
//            	for(String str : dropSuggestionsList) {
//            		if(str.charAt(text.length()-1) == charAtEnd) countSameCharAtPos++;
////            		System.out.println("Char compare: " + str.charAt(text.length()-1) + " = " + charAtEnd);
//            	}
//            	
//            	
//            	if(countSameCharAtPos == 0) {
////            		dropSuggestionsList = SearchController.getInstance().getSearchFieldSuggestions(text);
////            		System.out.println("refresh list");
////            	}
//            	
//                populateSuggestions(dropSuggestionsList);
//            	dropSuggestionsList = SearchController.getInstance().getSearchFieldSuggestions(text);
//            	populateSuggestions(dropSuggestionsList);
                
            		swingSearch(text);
            } else {
                dropSuggestions.setVisible(false);
            }
        }
    }
    
    private void swingSearch(String text){
	    SwingWorker worker = new SwingWorker<List<String>, Void>() {
	        @Override
	        public List<String> doInBackground() {
	            return dropSuggestionsList = SearchController.getInstance().getSearchFieldSuggestions(text);
	        }
	
	        @Override
	        public void done() {
	        	populateSuggestions(dropSuggestionsList);
	        }
	    };
	    worker.execute();
    }
}
