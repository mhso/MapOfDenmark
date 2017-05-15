package dk.itu.n.danmarkskort.gui.menu;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.gui.DropdownAddressSearch;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.components.SearchField;
import dk.itu.n.danmarkskort.gui.map.PinPoint;
import dk.itu.n.danmarkskort.gui.routeplanner.RoutePlannerMain;
import dk.itu.n.danmarkskort.models.RouteModel;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;
import dk.itu.n.danmarkskort.search.SearchController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutePage extends JPanel {
	private static final long serialVersionUID = 6134418299293182669L;
	private Style style;
    private JTextField txtAddrFrom, txtAddrTo;
    private JLabel lblAddrFromConfirmed, lblAddrToConfirmed;
    private DropdownAddressSearch dropSuggestionsAddrFrom, dropSuggestionsAddrTo;
    private ImageIcon ADDR_ICON_VALID, ADDR_ICON_INVALID;
	private DropdownMenu menu;
	private JRadioButton rdbtnCar, rdbtnBike, rdbtnWalk, rdbtnFastest, rdbtnShortest;
	private List<String> dropSuggestionsList;
	
    public RoutePage(DropdownMenu menu, String txtAddreToSetField) {
    	this.menu = menu;
    	dropSuggestionsList = new ArrayList<>();
    	if(Main.production) {
    		try {
				ADDR_ICON_VALID = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/happiness.png")));
				ADDR_ICON_INVALID = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/resources/icons/sad_red.png")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
    	}
    	else {
    		ADDR_ICON_VALID = new ImageIcon("resources/icons/happiness.png");
    		ADDR_ICON_INVALID = new ImageIcon("resources/icons/sad_red.png");
    	}
    	style = Main.style;
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
        lblPageHeadline.setFont(style.defaultHeadline());
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
        GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCenter.setLayout(gbl_panelCenter);
        
        
        JLabel lblFrom = new JLabel("From:");
        GridBagConstraints gbc_lblFrom = new GridBagConstraints();
        gbc_lblFrom.anchor = GridBagConstraints.EAST;
        gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
        gbc_lblFrom.gridx = 0;
        gbc_lblFrom.gridy = 1;
        panelCenter.add(lblFrom, gbc_lblFrom);
        
        dropSuggestionsAddrFrom = new DropdownAddressSearch(style);
        
        txtAddrFrom = new SearchField(dropSuggestionsAddrFrom, dropSuggestionsList, null);
        GridBagConstraints gbc_txtAddrfrom = new GridBagConstraints();
        gbc_txtAddrfrom.gridwidth = 4;
        gbc_txtAddrfrom.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddrfrom.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddrfrom.gridx = 1;
        gbc_txtAddrfrom.gridy = 1;
        panelCenter.add(txtAddrFrom, gbc_txtAddrfrom);
        ((AbstractDocument) txtAddrFrom.getDocument()).setDocumentFilter(new SearchFilter(txtAddrFrom, dropSuggestionsAddrFrom));
        
        dropSuggestionsAddrFrom.setWidthComponent(txtAddrFrom);
        
        JButton btnSwapToFrom = style.arrowUpDownButton();
        btnSwapToFrom.addActionListener(e -> swapToFromFields());
        
        lblAddrFromConfirmed = new JLabel();
        lblAddrFromConfirmed.setIcon(ADDR_ICON_INVALID);
        GridBagConstraints gbc_lblAddrfromconfirmed = new GridBagConstraints();
        gbc_lblAddrfromconfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrfromconfirmed.gridx = 6;
        gbc_lblAddrfromconfirmed.gridy = 1;
        panelCenter.add(lblAddrFromConfirmed, gbc_lblAddrfromconfirmed);
        
        lblAddrToConfirmed = new JLabel();
        lblAddrToConfirmed.setIcon(ADDR_ICON_INVALID);
        GridBagConstraints gbc_lblAddrToConfirmed = new GridBagConstraints();
        gbc_lblAddrToConfirmed.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddrToConfirmed.gridx = 6;
        gbc_lblAddrToConfirmed.gridy = 2;
        panelCenter.add(lblAddrToConfirmed, gbc_lblAddrToConfirmed);
        
        GridBagConstraints gbc_btnSwap = new GridBagConstraints();
        gbc_btnSwap.fill = GridBagConstraints.BOTH;
        gbc_btnSwap.gridheight = 2;
        gbc_btnSwap.insets = new Insets(0, 0, 5, 5);
        gbc_btnSwap.gridx = 7;
        gbc_btnSwap.gridy = 1;
        panelCenter.add(btnSwapToFrom, gbc_btnSwap);
        
        JLabel lblTo = new JLabel("To:");
        GridBagConstraints gbc_lblTo = new GridBagConstraints();
        gbc_lblTo.anchor = GridBagConstraints.EAST;
        gbc_lblTo.insets = new Insets(0, 0, 5, 5);
        gbc_lblTo.gridx = 0;
        gbc_lblTo.gridy = 2;
        panelCenter.add(lblTo, gbc_lblTo);
        
        dropSuggestionsAddrTo = new DropdownAddressSearch(style);
        
        txtAddrTo = new SearchField(dropSuggestionsAddrTo, dropSuggestionsList, null);
        txtAddrTo.setText(txtAddreToSetField);
        GridBagConstraints gbc_txtAddreto = new GridBagConstraints();
        gbc_txtAddreto.gridwidth = 4;
        gbc_txtAddreto.insets = new Insets(0, 0, 5, 5);
        gbc_txtAddreto.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtAddreto.gridx = 1;
        gbc_txtAddreto.gridy = 2;
        panelCenter.add(txtAddrTo, gbc_txtAddreto);
        ((AbstractDocument) txtAddrTo.getDocument()).setDocumentFilter(new SearchFilter(txtAddrTo, dropSuggestionsAddrTo));
        
        dropSuggestionsAddrTo.setWidthComponent(txtAddrTo);
        
        rdbtnCar = new JRadioButton("Car");
        rdbtnCar.setBackground(style.menuContentBG());
        rdbtnCar.setSelected(true);
        rdbtnCar.addActionListener(e -> {
        	if(rdbtnCar.isSelected()) {
        		rdbtnFastest.setSelected(true);
        		rdbtnShortest.setEnabled(true);
        		rdbtnFastest.setEnabled(true);
        	}
        });
        GridBagConstraints gbc_rdbtnCar = new GridBagConstraints();
        gbc_rdbtnCar.anchor = GridBagConstraints.WEST;
        gbc_rdbtnCar.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnCar.gridx = 2;
        gbc_rdbtnCar.gridy = 3;
        panelCenter.add(rdbtnCar, gbc_rdbtnCar);
        
        rdbtnBike = new JRadioButton("Bike");
        rdbtnBike.setBackground(style.menuContentBG());
        rdbtnBike.addActionListener(e -> {
        	if(rdbtnBike.isSelected()) {
        		rdbtnShortest.setSelected(true);
        		rdbtnShortest.setEnabled(false);
        		rdbtnFastest.setEnabled(false);
        	}
        });
        
        GridBagConstraints gbc_rdbtnBike = new GridBagConstraints();
        gbc_rdbtnBike.anchor = GridBagConstraints.NORTHWEST;
        gbc_rdbtnBike.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnBike.gridx = 3;
        gbc_rdbtnBike.gridy = 3;
        panelCenter.add(rdbtnBike, gbc_rdbtnBike);
        
        rdbtnWalk = new JRadioButton("Walk");
        rdbtnWalk.setBackground(style.menuContentBG());
        rdbtnWalk.addActionListener(e -> {
        	if(rdbtnWalk.isSelected()) {
        		rdbtnShortest.setSelected(true);
        		rdbtnShortest.setEnabled(false);
        		rdbtnFastest.setEnabled(false);
        	}
        });
        GridBagConstraints gbc_rdbtnWalk = new GridBagConstraints();
        gbc_rdbtnWalk.anchor = GridBagConstraints.NORTHWEST;
        gbc_rdbtnWalk.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnWalk.gridx = 4;
        gbc_rdbtnWalk.gridy = 3;
        panelCenter.add(rdbtnWalk, gbc_rdbtnWalk);
        
        ButtonGroup radioButtonGroupMovementType = new ButtonGroup();
        radioButtonGroupMovementType.add(rdbtnCar);
        radioButtonGroupMovementType.add(rdbtnBike);
        radioButtonGroupMovementType.add(rdbtnWalk);
        
        rdbtnFastest = new JRadioButton("Fastest");
        rdbtnFastest.setBackground(style.menuContentBG());
        rdbtnFastest.setSelected(true);
        GridBagConstraints gbc_rdbtnFastest = new GridBagConstraints();
        gbc_rdbtnFastest.anchor = GridBagConstraints.WEST;
        gbc_rdbtnFastest.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnFastest.gridx = 2;
        gbc_rdbtnFastest.gridy = 4;
        panelCenter.add(rdbtnFastest, gbc_rdbtnFastest);
        
        rdbtnShortest = new JRadioButton("Shortest");
        rdbtnShortest.setBackground(style.menuContentBG());
        GridBagConstraints gbc_rdbtnShortest = new GridBagConstraints();
        gbc_rdbtnShortest.anchor = GridBagConstraints.NORTHWEST;
        gbc_rdbtnShortest.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnShortest.gridx = 3;
        gbc_rdbtnShortest.gridy = 4;
        panelCenter.add(rdbtnShortest, gbc_rdbtnShortest);
        
        ButtonGroup radioButtonGroupRouteType = new ButtonGroup();
        radioButtonGroupRouteType.add(rdbtnFastest);
        radioButtonGroupRouteType.add(rdbtnShortest);
        
        JButton btnFind = new JButton("Find Route");
        btnFind.addActionListener( e -> openFindRoute());
        
        GridBagConstraints gbc_btnFind = new GridBagConstraints();
        gbc_btnFind.anchor = GridBagConstraints.EAST;
        gbc_btnFind.insets = new Insets(0, 0, 0, 5);
        gbc_btnFind.gridx = 4;
        gbc_btnFind.gridy = 6;
        panelCenter.add(btnFind, gbc_btnFind);
        
        validateToFromFields();
    }
    
    public void setFromText(String from) {
    	txtAddrFrom.setText(from);
    }
    
    public void setToText(String to) {
    	txtAddrTo.setText(to);
    }
    
    public boolean isFromFieldEmpty() {
    	return txtAddrFrom.getText().isEmpty();
    }
    
    public boolean isToFieldEmpty() {
    	return txtAddrTo.getText().isEmpty();
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
    	if(from && to) return true;
    	return false;
	}
    
    private boolean updateValidInputAddrTo(JTextField field, JLabel labelName){
    	boolean valid = false;
    	Address addr = null;
    	if(!field.getText().isEmpty()) addr = SearchController.getSearchFieldAddressObj(field.getText());
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
    		Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					WeightEnum weightEnum =  findWeightType();
		    		Address addrFrom = SearchController.getSearchFieldAddressObj(txtAddrFrom.getText());
					Address addrTo = SearchController.getSearchFieldAddressObj(txtAddrTo.getText());
		    		BufferedImage bufferedImage = null;
		    		
		    		List<RouteModel> routemodels = Main.routeController.makeRoute(addrFrom.getLonLatAsPoint(), addrTo.getLonLatAsPoint(), weightEnum);
		    		if(!routemodels.isEmpty()){
		    			makePinPoint(addrFrom, addrTo);
		    			
		    			Main.map.zoomToRouteRegion(Main.routeController.getRouteRegion());
		    			bufferedImage = Main.map.getRoutePreviewImage();
						
						new RoutePlannerMain(bufferedImage, addrFrom.toStringShort(), addrTo.toStringShort(), weightEnum, routemodels);
					}else {
						menu.blockVisibility(true);
			    		JOptionPane.showMessageDialog(RoutePage.this, "Unable to find route", "No route found", JOptionPane.INFORMATION_MESSAGE);
					}
    			}
    		});
    		t.start();
    	} else if(txtAddrFrom.getText().trim().isEmpty() || txtAddrTo.getText().trim().isEmpty()) {
    		menu.blockVisibility(true);
    		JOptionPane.showMessageDialog(this, "To/From fields can't be empty.", "Missing information", JOptionPane.INFORMATION_MESSAGE);
    	} else {
    		menu.blockVisibility(true);
    		JOptionPane.showMessageDialog(this, "The address input not found,\n please refer to the smileys.", "Wrong information", JOptionPane.INFORMATION_MESSAGE);
    	}
    }

	private void makePinPoint(Address addrFrom, Address addrTo) {
		ArrayList<PinPoint> pinPoints = new ArrayList<PinPoint>();
		PinPoint pinPointFrom = new PinPoint(addrFrom.getLonLatAsPoint(), txtAddrFrom.getText());
		PinPoint pinPointTo = new PinPoint(addrTo.getLonLatAsPoint(), txtAddrTo.getText());
		pinPointFrom.setIconIndex(14);
		pinPointTo.setIconIndex(13);
		pinPoints.add(pinPointFrom);
		pinPoints.add(pinPointTo);
		Main.pinPointManager.setTemporaryPinPoints(pinPoints);
	}
    
    private WeightEnum findWeightType() {
		if(rdbtnCar.isSelected() && rdbtnFastest.isSelected()){
			return WeightEnum.SPEED_CAR;
		}else if(rdbtnCar.isSelected() && rdbtnShortest.isSelected()){
			return WeightEnum.DISTANCE_CAR;
		}else if(rdbtnBike.isSelected()) {
			return WeightEnum.DISTANCE_BIKE; 
		}else{
			//Walk
			return WeightEnum.DISTANCE_WALK; 
		}
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
            	dropSuggestionsList.removeAll(dropSuggestionsList);
              	dropSuggestionsList.addAll(SearchController.getSearchFieldSuggestions(text));
            	populateSuggestions(das, input, dropSuggestionsList);
            } else {
            	das.setVisible(false);
            	validateToFromFields();
            }
        }
    }
}
