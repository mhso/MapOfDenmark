package dk.itu.n.danmarkskort.gui;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.gui.map.PinPoint;
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
    private JPanel searchInputWrapper, topParent, top;
    private JButton menu;
    private List<String> dropSuggestionsList;

    public TopPanel() {
    	dropSuggestionsList = new ArrayList<>();
        this.style = Main.style;
        setOpaque(false);

        topParent = new JPanel(new BorderLayout());
        topParent.setBorder(BorderFactory.createLineBorder(style.panelBG(), style.topPanelBorderWidth(), false));

        top = new JPanel(new GridBagLayout());
        top.setBackground(style.panelBG());
        top.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 3, 0, 3);
        gbc.weighty = 1;

        menu = style.menuButton();
        menu.setToolTipText("Menu");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(menu, gbc);

        JButton search = style.searchButton();
        search.setToolTipText("Search");
        
        searchInputWrapper = new JPanel(new GridBagLayout());
        searchInputWrapper.setBackground(style.inputFieldBG());
        GridBagConstraints searchInputGBC = new GridBagConstraints();
        
        dropSuggestions = new DropdownAddressSearch(searchInputWrapper, style);
        
        input = new SearchField(dropSuggestions, dropSuggestionsList, search);
        input.setColumns(30);
        input.setBorder(BorderFactory.createEmptyBorder(style.smallMargin(), style.smallMargin(), style.smallMargin(), style.smallMargin()));
        input.setFont(new Font("sans serif", Font.PLAIN, 14));
        input.setForeground(style.panelTextColor());
        input.setOpaque(false);
        input.setBackground(new Color(0,0,0,0));
        input.setCaretColor(style.panelTextColor());

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

        search.addActionListener(e -> {
        	Main.log(input.getText());
        	searchForAddress(input.getText());
        });

        // adding drop down functionality
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new SearchFilter());
    }

    public void repaintPanels() {
        topParent.repaint();
        dropMenu.repaint();
        dropSuggestions.repaint();
    }

    public void searchForAddress(String address) {
    	if(!address.isEmpty()) {
    		Address addr = SearchController.getSearchFieldAddressObj(address);
        	if(addr != null ) { 
        		System.out.println("Toppanel->searchForAddress: "+addr.toString());
        		panZoomToCoordinates(addr.getLonLatAsPoint());
        	} else {
        		JOptionPane.showMessageDialog(null, "No match found.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
        	}
    	} else {
    		JOptionPane.showMessageDialog(null, "Search field can't be empty.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

    private void panZoomToCoordinates(Point2D.Float input) {
    	Main.log("Toppanel->panZoomToCoordinats (lon, lat): " + input.toString() + "\n -->, real (lon, lat): " + Util.toRealCoords(input));
    	String pinPointName = "SearchLocation - " + input.toString();
    	makePinPoint(input, pinPointName);
    	Main.map.panToPosition(new Point2D.Float(input.x, input.y));
    	Main.mainPanel.revalidate();
    	Main.mainPanel.repaint();
	}
    
    private void makePinPoint(Point2D inputScreenCords, String pinPointName) {
		ArrayList<PinPoint> pinPoints = new ArrayList<PinPoint>();
		PinPoint pinPoint = new PinPoint(inputScreenCords, pinPointName);
		pinPoint.setIconIndex(5);
		pinPoints.add(pinPoint);
		Main.pinPointManager.setTemporaryPinPoints(pinPoints);
	}

	public void populateSuggestions(List<String> list) {
        dropSuggestions.removeAll();
        for(String str : list){
            dropSuggestions.addElement(input, str);
        }
        dropSuggestions.showDropdown(input);
    }

	public DropdownMenu getDropMenu() {
		return dropMenu;
	}
	
	public JButton getMenuButton() {
		return menu;
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
            	dropSuggestionsList.removeAll(dropSuggestionsList);
              	dropSuggestionsList.addAll(SearchController.getSearchFieldSuggestions(text));
            	populateSuggestions(dropSuggestionsList);
            } else {
                dropSuggestions.setVisible(false);
            }
        }
    }
}
