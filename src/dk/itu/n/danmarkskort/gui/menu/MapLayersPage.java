package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.WayType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class MapLayersPage extends JPanel {
	private static final long serialVersionUID = -6963692316700756734L;
	private Style style;
    private DropdownMenu menu;
    
    public MapLayersPage(DropdownMenu menu) {
    	style = new Style();
    	this.menu = menu;
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelHeadline.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelHeadline.setBackground(style.menuContentBG());
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        JLabel lblPageHeadline = new JLabel("Map Layers");
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
        panelCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        panelCenter.setLayout(new GridLayout(4, 0, 0, 0));
        
        JPanel panelDefault = new JPanel();
        panelDefault.setBorder(new EmptyBorder(0, 0, 0, 20));
        panelCenter.add(panelDefault);
        panelDefault.setOpaque(false);
        panelDefault.setLayout(new BorderLayout(30, 0));
        
        JLabel labelDefaultPreset = new JLabel("Default Preset");
        labelDefaultPreset.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelDefault.add(labelDefaultPreset, BorderLayout.CENTER);
        
        JRadioButton buttonDefault = new JRadioButton("");
        buttonDefault.addActionListener(e -> {
        	GraphicRepresentation.setAllDefault();
        	Main.map.forceRepaint();
        });
        buttonDefault.setSelected(true);
        buttonDefault.setOpaque(false);
        panelDefault.add(buttonDefault, BorderLayout.EAST);
        
        JPanel panelBike = new JPanel();
        panelBike.setBorder(new EmptyBorder(0, 0, 0, 20));
        panelBike.setOpaque(false);
        panelCenter.add(panelBike);
        panelBike.setLayout(new BorderLayout(30, 0));
        
        JLabel labelBikePreset = new JLabel("Biking/Walking Preset");
        labelBikePreset.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelBike.add(labelBikePreset, BorderLayout.CENTER);
        
        JRadioButton buttonBike = new JRadioButton("");
        buttonBike.addActionListener(e -> {
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_FOOTWAY);
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_CYCLEWAY);
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_TRACK);
        	Main.map.forceRepaint();
        });
        buttonBike.setOpaque(false);
        panelBike.add(buttonBike, BorderLayout.EAST);
        
        JPanel panelCar = new JPanel();
        panelCar.setBorder(new EmptyBorder(0, 0, 0, 20));
        panelCar.setOpaque(false);
        panelCenter.add(panelCar);
        panelCar.setLayout(new BorderLayout(30, 0));
        
        JLabel labelCarPreset = new JLabel("Car Preset");
        labelCarPreset.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelCar.add(labelCarPreset, BorderLayout.CENTER);
        
        JRadioButton buttonCar = new JRadioButton("");
        buttonCar.addActionListener(e -> {
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_MOTORWAY);
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_PRIMARY);
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_TRUNK);
        	GraphicRepresentation.addToOverriddenSpecs(WayType.HIGHWAY_SECONDARY);
        	Main.map.forceRepaint();
        });
        buttonCar.setOpaque(false);
        panelCar.add(buttonCar, BorderLayout.EAST);
        
        JPanel panelCustomize = new JPanel();
        panelCustomize.setBorder(new EmptyBorder(0, 0, 0, 20));
        panelCustomize.setOpaque(false);
        panelCenter.add(panelCustomize);
        panelCustomize.setLayout(new BorderLayout(30, 0));
        
        JLabel labelCustomize = new JLabel("Custom Preset");
        labelCustomize.setFont(new Font("Tahoma", Font.PLAIN, 12));
        panelCustomize.add(labelCustomize, BorderLayout.CENTER);
        
        JRadioButton buttonCustom = new JRadioButton("");
        buttonCustom.addActionListener(e -> openCustomPresetDialog());
        buttonCustom.setOpaque(false);
        panelCustomize.add(buttonCustom, BorderLayout.EAST);
        
        JRadioButton[] buttons = {buttonDefault, buttonBike, buttonCar, buttonCustom};
        for(JRadioButton button : buttons) {
        	button.addActionListener(new ButtonSelecter(button, buttons));
        }
    }
    
    private void openCustomPresetDialog() {
    	menu.blockVisibility(true);
    	JDialog dialog = new JDialog(Main.window);
		dialog.setModal(true);
		dialog.setTitle("Custom Preset");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		JPanel dialogContentPane = new JPanel();
		dialogContentPane.setOpaque(false);
		dialogContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		dialogContentPane.setLayout(new GridLayout(1, 1));
		dialog.setContentPane(dialogContentPane);
		
		JPanel listPanel = new JPanel();
		listPanel.setOpaque(false);
		listPanel.setLayout(new GridLayout(0, 1, 0, 20));
		dialogContentPane.add(listPanel);
		
		WayType[] wayTypes = WayType.values();
		for(int i = 0; i < wayTypes.length; i++) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			Border dashBorder = BorderFactory.createDashedBorder(Color.BLACK, 1, 5, 3, true);
			Border emptyBorder = new EmptyBorder(5, 5, 5, 5);
			panel.setBorder(new CompoundBorder(dashBorder, emptyBorder));
			panel.setLayout(new BorderLayout(30, 0));
			
			JLabel label = new JLabel(wayTypes[i].toString());
			panel.add(label, BorderLayout.CENTER);
			
			JPanel rightPanel = new JPanel();
			rightPanel.setOpaque(false);
			rightPanel.setLayout(new GridLayout(3, 1));
			panel.add(rightPanel, BorderLayout.EAST);
			
			JRadioButton chckDefault = new JRadioButton("Default Zoom Level");
			chckDefault.addActionListener(new WayTypeCustomize(wayTypes[i], "default"));
			chckDefault.setOpaque(false);
			rightPanel.add(chckDefault);
			
			JRadioButton chckRemove = new JRadioButton("Remove Map Element");
			chckRemove.addActionListener(new WayTypeCustomize(wayTypes[i], "filter"));
			chckRemove.setOpaque(false);
			rightPanel.add(chckRemove);
			
			JRadioButton chckAllLevels = new JRadioButton("Always Show Map Element");
			chckAllLevels.addActionListener(new WayTypeCustomize(wayTypes[i], "override"));
			chckAllLevels.setOpaque(false);
			rightPanel.add(chckAllLevels);
			
			if(GraphicRepresentation.isFiltered(wayTypes[i])) chckRemove.setSelected(true);
			else if(GraphicRepresentation.isOverridden(wayTypes[i])) chckAllLevels.setSelected(true);
			else chckDefault.setSelected(true);
			
			JRadioButton[] checkArr = {chckDefault, chckRemove, chckAllLevels};
			for(JRadioButton cb : checkArr) {
				cb.addActionListener(new ButtonSelecter(cb, checkArr));
			}
			
			listPanel.add(panel);
		}
		
		JScrollPane scrollPane = new JScrollPane(listPanel);
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dialogContentPane.add(scrollPane);
		
		dialog.setPreferredSize(new Dimension(450, 500));
		dialog.pack();
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				menu.blockVisibility(false);
				dialog.dispose();
			}	
		});
		
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
    }

	private void initContentPanel(JPanel panel) {}
	
	private class WayTypeCustomize implements ActionListener {
		private WayType wayType;
		private String action;
		
		public WayTypeCustomize(WayType wayType, String action) {
			this.wayType = wayType;
			this.action = action;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(action.equals("default")) GraphicRepresentation.setDefault(wayType);
			else if(action.equals("override")) GraphicRepresentation.addToOverriddenSpecs(wayType);
			else if(action.equals("filter")) GraphicRepresentation.setFilteredElement(wayType, true);
			Main.map.forceRepaint();
		}	
	}
	
	private class ButtonSelecter implements ActionListener {
		private JToggleButton sourceButton;
		private JToggleButton[] buttons;
		
		public ButtonSelecter(JToggleButton sourceButton, JToggleButton[] buttons) {
			this.sourceButton = sourceButton;
			this.buttons = buttons;		
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for(JToggleButton button : buttons) {
				if(button != sourceButton) button.setSelected(false);
			}
			sourceButton.setSelected(true);
		}
	}
}
