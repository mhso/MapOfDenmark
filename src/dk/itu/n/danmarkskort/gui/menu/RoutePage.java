package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.gui.DropdownAddressSearch;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.TopPanel;
import dk.itu.n.danmarkskort.routeplanner.RoutePlannerMain;
import dk.itu.n.danmarkskort.search.SearchController;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RoutePage extends JPanel {

    Style style;
    private JTextField txtAddrFrom;
    private JTextField txtAddrTo;
    JLabel lblAddrFromConfirmed, lblAddrToConfirmed;
    private DropdownAddressSearch dropSuggestionsAddrFrom;
    private DropdownAddressSearch dropSuggestionsAddrTo;
    private final ImageIcon ADDR_ICON_VALID = new ImageIcon("resources/icons/happiness.png");	
	private final ImageIcon ADDR_ICON_INVALID = new ImageIcon("resources/icons/sad_red.png");
	private DropdownMenu menu;
	
    public RoutePage(DropdownMenu menu, String txtAddreToSetField) {
    	this.menu = menu;
    	style = new Style();
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelHeadline.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelHeadline.setBackground(style.menuContentBG());
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        JLabel lblPageHeadline = new JLabel("Find Route");
        lblPageHeadline.setFont(new Font("Tahoma", Font.BOLD, 18));
        panelHeadline.add(lblPageHeadline);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setBackground(style.menuContentBG());
        panelPage.add(panelSouth, BorderLayout.SOUTH);
        
        JPanel panelWest = new JPanel();
        panelWest.setBackground(style.menuContentBG());
        panelPage.add(panelWest, BorderLayout.WEST);
        
        JPanel panelEast = new JPanel();
        panelEast.setBackground(style.menuContentBG());
        panelPage.add(panelEast, BorderLayout.EAST);
        
        JPanel panelCenter = new JPanel();
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCenter.setLayout(gbl_panelCenter);
        
        
        JLabel lblFrom = new JLabel("From:");
        GridBagConstraints gbc_lblFrom = new GridBagConstraints();
        gbc_lblFrom.anchor = GridBagConstraints.EAST;
        gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
        gbc_lblFrom.gridx = 0;
        gbc_lblFrom.gridy = 1;
        panelCenter.add(lblFrom, gbc_lblFrom);
        
        txtAddrFrom = new JTextField();
        GridBagConstraints gbc_txtAddrfrom = new GridBagConstraints();
        gbc_txtAddrfrom.gridwidth = 3;
        gbc_txtAddrfrom.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddrfrom.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddrfrom.gridx = 1;
        gbc_txtAddrfrom.gridy = 1;
        panelCenter.add(txtAddrFrom, gbc_txtAddrfrom);
        dropSuggestionsAddrFrom = new DropdownAddressSearch(txtAddrFrom, style);
        ((AbstractDocument) txtAddrFrom.getDocument()).setDocumentFilter(new SearchFilter(txtAddrFrom, dropSuggestionsAddrFrom));
        txtAddrFrom.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!dropSuggestionsAddrFrom.isEmpty()) {
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(dropSuggestionsAddrFrom.getSelectedIndex() < dropSuggestionsAddrFrom.getComponents().length-1) {	
							dropSuggestionsAddrFrom.setSelectedElement(dropSuggestionsAddrFrom.getSelectedIndex()+1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP) {
						if(dropSuggestionsAddrFrom.getSelectedIndex() > 0) {
							dropSuggestionsAddrFrom.setSelectedElement(dropSuggestionsAddrFrom.getSelectedIndex()-1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(dropSuggestionsAddrFrom.getSelectedIndex() > 0) {
							dropSuggestionsAddrFrom.itemClicked();
						}
					}
				}
			}
        	
        });
        
        
        JButton btnS = new JButton();
        btnS.addActionListener(e -> swapToFromFields());
        
        lblAddrFromConfirmed = new JLabel();
        lblAddrFromConfirmed.setIcon(ADDR_ICON_INVALID);
        GridBagConstraints gbc_lblAddrfromconfirmed = new GridBagConstraints();
        gbc_lblAddrfromconfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrfromconfirmed.gridx = 4;
        gbc_lblAddrfromconfirmed.gridy = 1;
        panelCenter.add(lblAddrFromConfirmed, gbc_lblAddrfromconfirmed);
        
        lblAddrToConfirmed = new JLabel();
        lblAddrToConfirmed.setIcon(ADDR_ICON_INVALID);
        GridBagConstraints gbc_lblAddrToConfirmed = new GridBagConstraints();
        gbc_lblAddrToConfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrToConfirmed.gridx = 4;
        gbc_lblAddrToConfirmed.gridy = 2;
        panelCenter.add(lblAddrToConfirmed, gbc_lblAddrToConfirmed);
        
        btnS.setIcon(style.arrowUpDownButton());
        GridBagConstraints gbc_btnS = new GridBagConstraints();
        gbc_btnS.fill = GridBagConstraints.BOTH;
        gbc_btnS.gridheight = 2;
        gbc_btnS.insets = new Insets(0, 0, 5, 5);
        gbc_btnS.gridx = 5;
        gbc_btnS.gridy = 1;
        panelCenter.add(btnS, gbc_btnS);
        
        JLabel lblTo = new JLabel("To:");
        GridBagConstraints gbc_lblTo = new GridBagConstraints();
        gbc_lblTo.anchor = GridBagConstraints.EAST;
        gbc_lblTo.insets = new Insets(0, 0, 5, 5);
        gbc_lblTo.gridx = 0;
        gbc_lblTo.gridy = 2;
        panelCenter.add(lblTo, gbc_lblTo);
        
        txtAddrTo = new JTextField();
        txtAddrTo.setText(txtAddreToSetField);
        GridBagConstraints gbc_txtAddreto = new GridBagConstraints();
        gbc_txtAddreto.gridwidth = 3;
        gbc_txtAddreto.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddreto.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddreto.gridx = 1;
        gbc_txtAddreto.gridy = 2;
        panelCenter.add(txtAddrTo, gbc_txtAddreto);
        dropSuggestionsAddrTo = new DropdownAddressSearch(txtAddrTo, style);
        ((AbstractDocument) txtAddrTo.getDocument()).setDocumentFilter(new SearchFilter(txtAddrTo, dropSuggestionsAddrTo));
        txtAddrTo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(!dropSuggestionsAddrTo.isEmpty()) {
					if(e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(dropSuggestionsAddrTo.getSelectedIndex() < dropSuggestionsAddrTo.getComponents().length-1) {	
							dropSuggestionsAddrTo.setSelectedElement(dropSuggestionsAddrTo.getSelectedIndex()+1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_UP) {
						if(dropSuggestionsAddrTo.getSelectedIndex() > 0) {
							dropSuggestionsAddrTo.setSelectedElement(dropSuggestionsAddrTo.getSelectedIndex()-1);
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
						if(dropSuggestionsAddrTo.getSelectedIndex() > 0) {
							dropSuggestionsAddrTo.itemClicked();
						}
					}
				}
			}
        	
        });
        
        JButton btnFind = new JButton("Find Route");
        btnFind.addActionListener( e -> openFindRoute());
        
        JRadioButton rdbtnCar = new JRadioButton("Car");
        rdbtnCar.setBackground(style.menuContentBG());
        rdbtnCar.setSelected(true);
        GridBagConstraints gbc_rdbtnCar = new GridBagConstraints();
        gbc_rdbtnCar.anchor = GridBagConstraints.WEST;
        gbc_rdbtnCar.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnCar.gridx = 2;
        gbc_rdbtnCar.gridy = 3;
        panelCenter.add(rdbtnCar, gbc_rdbtnCar);
        
        JRadioButton rdbtnBike = new JRadioButton("Bike");
        rdbtnBike.setBackground(style.menuContentBG());
        GridBagConstraints gbc_rdbtnBike = new GridBagConstraints();
        gbc_rdbtnBike.anchor = GridBagConstraints.WEST;
        gbc_rdbtnBike.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnBike.gridx = 3;
        gbc_rdbtnBike.gridy = 3;
        panelCenter.add(rdbtnBike, gbc_rdbtnBike);
        
        ButtonGroup radioButtonGroupMovementType = new ButtonGroup();
        radioButtonGroupMovementType.add(rdbtnCar);
        radioButtonGroupMovementType.add(rdbtnBike);
        
        JRadioButton rdbtnFastest = new JRadioButton("Fastest");
        rdbtnFastest.setBackground(style.menuContentBG());
        rdbtnFastest.setSelected(true);
        GridBagConstraints gbc_rdbtnFastest = new GridBagConstraints();
        gbc_rdbtnFastest.anchor = GridBagConstraints.WEST;
        gbc_rdbtnFastest.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnFastest.gridx = 2;
        gbc_rdbtnFastest.gridy = 4;
        panelCenter.add(rdbtnFastest, gbc_rdbtnFastest);
        
        JRadioButton rdbtnShortest = new JRadioButton("Shortest");
        rdbtnShortest.setBackground(style.menuContentBG());
        GridBagConstraints gbc_rdbtnShortest = new GridBagConstraints();
        gbc_rdbtnShortest.anchor = GridBagConstraints.WEST;
        gbc_rdbtnShortest.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnShortest.gridx = 3;
        gbc_rdbtnShortest.gridy = 4;
        panelCenter.add(rdbtnShortest, gbc_rdbtnShortest);
        
        ButtonGroup radioButtonGroupRouteType = new ButtonGroup();
        radioButtonGroupRouteType.add(rdbtnFastest);
        radioButtonGroupRouteType.add(rdbtnShortest);
        
        GridBagConstraints gbc_btnFind = new GridBagConstraints();
        gbc_btnFind.anchor = GridBagConstraints.EAST;
        gbc_btnFind.insets = new Insets(0, 0, 0, 5);
        gbc_btnFind.gridx = 3;
        gbc_btnFind.gridy = 6;
        panelCenter.add(btnFind, gbc_btnFind);

        validateToFromFields();
    }
    
    private void swapToFromFields() {
    	String addrFromTemp = txtAddrFrom.getText();
    	txtAddrFrom.setText(txtAddrTo.getText());
    	txtAddrTo.setText(addrFromTemp);
    	validateToFromFields();
	}
    
    private boolean validateToFromFields() {
    	boolean from = updateValidInputAddrTo(txtAddrFrom, lblAddrFromConfirmed);
    	boolean to = updateValidInputAddrTo(txtAddrTo, lblAddrToConfirmed);
    	if(from == true && to == true) return true;
    	return false;
	}
    
    private boolean updateValidInputAddrTo(JTextField field, JLabel labelName){
    	boolean valid = false;
    	Address addr = null;
    	if(!field.getText().isEmpty()) addr = SearchController.getInstance().getSearchFieldAddressObj(field.getText());
    	if(addr != null) valid = true;
    	changeValidAddrIcon(labelName, valid);
    	return valid;
    }
    
    private void changeValidAddrIcon(JLabel labelName, boolean valid){
    	ImageIcon imageToShow = ADDR_ICON_INVALID;
    	if (valid)  imageToShow = ADDR_ICON_VALID;
    	labelName.setIcon(imageToShow);
    }
    
    private void openFindRoute(){
    	if(validateToFromFields()){
    		RoutePlannerMain routePlannerMain =  new RoutePlannerMain(txtAddrFrom.getText(), txtAddrTo.getText());
    	} else if(!txtAddrFrom.getText().trim().isEmpty() && !txtAddrTo.getText().trim().isEmpty()) {
    		menu.blockVisibility(true);
    		JOptionPane.showMessageDialog(this, "To/From fields can't be empty.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
    	} else {
    		menu.blockVisibility(true);
    		JOptionPane.showMessageDialog(this, "The address input not found,\n please refere to the smilyes.", "Wrong information", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

	private void initContentPanel(JPanel panel){
		
    }
	
	public void populateSuggestions(DropdownAddressSearch das, JTextField textField, List<String> list) {
		menu.blockVisibility(true);
		das.setVisible(false);
		das.removeAll();
        for(String st : list){
        	das.addElement(textField, st);
        }
        das.showDropdown(textField);
        menu.blockVisibility(false);
    }
	
	private class SearchFilter extends DocumentFilter {
		JTextField input;
		DropdownAddressSearch das;
		
		public SearchFilter(JTextField input, DropdownAddressSearch das){
			this.input = input;
			this.das = das;
		}
		
		
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
            validateToFromFields();
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String newText,
                            AttributeSet attr) throws BadLocationException {

            super.replace(fb, offset, length, newText, attr);
            dropdownSuggestions(offset, input.getText());
            validateToFromFields();
        }
        
        public void dropdownSuggestions(int offset, String text) {
            if(offset > 1) {
                populateSuggestions(das, input, SearchController.getInstance().getSearchFieldSuggestions(text));
                revalidate();
                repaint();
            } else {
            	das.setVisible(false);
            	validateToFromFields();
            }
        }
    }
}
