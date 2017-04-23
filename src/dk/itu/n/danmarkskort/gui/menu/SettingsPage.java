package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.CustomToggleButton;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SettingsPage extends JPanel {
	private static final long serialVersionUID = 4642167284178775315L;
	private Style style;
	private JPanel panelCenter;
    
    public SettingsPage() {
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
        
        JLabel lblPageHeadline = new JLabel("Settings");
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
        
        panelCenter = new JPanel();
        panelCenter.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        panelCenter.setLayout(new CardLayout());
        
        JPanel defaultCenterPanel = new JPanel();
        defaultCenterPanel.setOpaque(false);
        defaultCenterPanel.setLayout(new GridLayout(0, 1, 0, 10));
        panelCenter.add(defaultCenterPanel, "name_default");
        
        JButton buttonChangeMapTheme = new JButton("Change Map Theme");
        buttonChangeMapTheme.setBackground(new Color(224, 255, 255));
        buttonChangeMapTheme.setBorder(new LineBorder(new Color(0, 153, 204), 2, true));
        buttonChangeMapTheme.addActionListener(e -> showMapThemePanel());
        defaultCenterPanel.add(buttonChangeMapTheme);
        
        JButton buttonChangeInterfaceTheme = new JButton("Change Interface Theme");
        buttonChangeInterfaceTheme.setBackground(new Color(224, 255, 255));
        buttonChangeInterfaceTheme.setBorder(new LineBorder(new Color(0, 153, 204), 2, true));
        buttonChangeInterfaceTheme.addActionListener(e -> showInterfaceThemePanel());
        defaultCenterPanel.add(buttonChangeInterfaceTheme);
        
        JButton buttonChangePreferences = new JButton("Change Preferences");
        buttonChangePreferences.setBackground(new Color(224, 255, 255));
        buttonChangePreferences.setBorder(new LineBorder(new Color(0, 153, 204), 2, true));
        buttonChangePreferences.addActionListener(e -> showPreferencesPanel());
        defaultCenterPanel.add(buttonChangePreferences);
        
        createMapThemePanel();
        
        createInterfaceThemePanel();
        
        createPreferencesPanel();
    }
    
    private void createMapThemePanel() {
    	final int SUBPANEL_WIDTH = 420;
    	final int SUBPANEL_HEIGHT = 50;
    	
    	JPanel panelMapTheme = new JPanel();
        panelMapTheme.setOpaque(false);
        panelCenter.add(panelMapTheme, "name_maptheme");
        panelMapTheme.setLayout(new BorderLayout(0, 5));
        
        JLabel labelChangeTheme = new JLabel("Change Map Theme");
        labelChangeTheme.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelChangeTheme.setHorizontalAlignment(SwingConstants.CENTER);
        panelMapTheme.add(labelChangeTheme, BorderLayout.NORTH);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panelMapTheme.add(panel, BorderLayout.CENTER);
        
        JPanel panelBasicTheme = new JPanel();
        panelBasicTheme.setOpaque(false);
        panelBasicTheme.setMaximumSize(new Dimension(SUBPANEL_WIDTH, SUBPANEL_HEIGHT));        
        panelBasicTheme.setLayout(new BorderLayout(0, 0));
        panel.add(panelBasicTheme);
        
        JLabel labelBasic = new JLabel("Basic");
        panelBasicTheme.add(labelBasic, BorderLayout.WEST);
        
        JLabel labelBasicIcon = new JLabel(style.basicThemePreview());
        panelBasicTheme.add(labelBasicIcon, BorderLayout.CENTER);
        
        CustomToggleButton buttonBasicToggle = style.toggleButton();
        buttonBasicToggle.addActionListener(e -> {
        	if(!buttonBasicToggle.isOn()) {
        		Main.userPreferences.setCurrentMapTheme("Basic");
            	changeCurrentTheme();
        	}
        });
        buttonBasicToggle.setOn(false);
        panelBasicTheme.add(buttonBasicToggle, BorderLayout.EAST);
        
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        panel.add(rigidArea);
        
        JPanel panelColorblindTheme = new JPanel();
        panelColorblindTheme.setOpaque(false);
        panelColorblindTheme.setMaximumSize(new Dimension(SUBPANEL_WIDTH, SUBPANEL_HEIGHT));
        panel.add(panelColorblindTheme);
        panelColorblindTheme.setLayout(new BorderLayout(0, 0));
        
        JLabel labelColorblind = new JLabel("Colorblind");
        panelColorblindTheme.add(labelColorblind, BorderLayout.WEST);
        
        JLabel labelColorblindIcon = new JLabel();
        labelColorblindIcon.setHorizontalAlignment(SwingConstants.LEFT);
        panelColorblindTheme.add(labelColorblindIcon, BorderLayout.CENTER);
        
        CustomToggleButton buttonColorblindToggle = style.toggleButton();
        buttonColorblindToggle.addActionListener(e -> {
        	if(!buttonColorblindToggle.isOn()) {
        		Main.userPreferences.setCurrentMapTheme("Colorblind");
        		changeCurrentTheme();
        	}
        });
        buttonColorblindToggle.setOn(false);
        panelColorblindTheme.add(buttonColorblindToggle, BorderLayout.EAST);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        panelMapTheme.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(new BorderLayout(0, 0));
        
        JButton buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> showDefaultPanel());
        buttonBack.setFont(new Font("Tahoma", Font.PLAIN, 13));
        panelSouth.add(buttonBack, BorderLayout.WEST);
        
        CustomToggleButton[] buttons = {buttonBasicToggle, buttonColorblindToggle};
        for(CustomToggleButton button : buttons) {
        	button.addActionListener(new ButtonSelecter(button, buttons));
        }
        
        if(Main.userPreferences.getCurrentMapTheme().equals("Basic")) buttonBasicToggle.setOn(true);
        else if(Main.userPreferences.getCurrentMapTheme().equals("Colorblind")) buttonColorblindToggle.setOn(true);
    }
    
    private void changeCurrentTheme() {
    	GraphicRepresentation.parseData("resources/Theme" + Main.userPreferences.getCurrentMapTheme() + ".XML");
    	Main.map.forceRepaint();
    }
    
    private void createInterfaceThemePanel() {
    	JPanel panelInterfaceTheme = new JPanel();
    	panelInterfaceTheme.setLayout(new BorderLayout(0, 5));
        panelInterfaceTheme.setOpaque(false);
        panelCenter.add(panelInterfaceTheme, "name_interfacetheme");
        
        JLabel labelChangeTheme = new JLabel("Change Interface Theme");
        labelChangeTheme.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelChangeTheme.setHorizontalAlignment(SwingConstants.CENTER);
        panelInterfaceTheme.add(labelChangeTheme, BorderLayout.NORTH);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        panelInterfaceTheme.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(new BorderLayout(0, 0));
        
        JButton buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> showDefaultPanel());
        buttonBack.setFont(new Font("Tahoma", Font.PLAIN, 13));
        panelSouth.add(buttonBack, BorderLayout.WEST);
    }
    
    private void createPreferencesPanel() {
    	JPanel panelDefaultValues = new JPanel();
    	panelDefaultValues.setLayout(new BorderLayout(0, 5));
        panelDefaultValues.setOpaque(false);
        panelCenter.add(panelDefaultValues, "name_preferences");
        
        JLabel labelPreferences = new JLabel("Change Preferences");
        labelPreferences.setFont(new Font("Tahoma", Font.PLAIN, 20));
        labelPreferences.setHorizontalAlignment(SwingConstants.CENTER);
        panelDefaultValues.add(labelPreferences, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        panelDefaultValues.add(centerPanel, BorderLayout.CENTER);
        
        JPanel defaultMapFilePanel = new JPanel();
        defaultMapFilePanel.setOpaque(false);
        defaultMapFilePanel.setLayout(new BorderLayout());
        centerPanel.add(defaultMapFilePanel);
        
        JLabel defaultMapFileLabel = new JLabel("Default Map File:");
        defaultMapFilePanel.add(defaultMapFileLabel, BorderLayout.WEST);
        
        JButton defaultMapFileButton = new JButton();
        defaultMapFilePanel.add(defaultMapFileButton, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        
        JPanel defaultMapThemePanel = new JPanel();
        defaultMapThemePanel.setOpaque(false);
        defaultMapThemePanel.setLayout(new BorderLayout());
        centerPanel.add(defaultMapThemePanel);
        
        JLabel defaultMapThemeLabel = new JLabel("Default Map Theme:");
        defaultMapThemePanel.add(defaultMapThemeLabel, BorderLayout.WEST);
        
        JComboBox<String> defaultMapThemeBox = new JComboBox<>(new String[]{"Basic", "Colorblind"});
        defaultMapThemeBox.setSelectedItem(Main.userPreferences.getDefaultTheme());
        defaultMapThemePanel.add(defaultMapThemeBox);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        
        JPanel maximizeOnStartupPanel = new JPanel();
        maximizeOnStartupPanel.setOpaque(false);
        maximizeOnStartupPanel.setLayout(new BorderLayout());
        centerPanel.add(maximizeOnStartupPanel);
        
        JLabel maximizeLabel = new JLabel("Maximize On Startup: ");
        maximizeOnStartupPanel.add(maximizeLabel, BorderLayout.WEST);
        
        JRadioButton maximizeButton = new JRadioButton();
        maximizeButton.setBackground(panelCenter.getBackground());
        maximizeButton.setSelected(Main.userPreferences.isMaximizeOnStartup());
        maximizeOnStartupPanel.add(maximizeButton, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        
        JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        panelDefaultValues.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(new BorderLayout(0, 0));
        
        JButton buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> showDefaultPanel());
        buttonBack.setFont(new Font("Tahoma", Font.PLAIN, 13));
        panelSouth.add(buttonBack, BorderLayout.WEST);
    }
    
    private void showDefaultPanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_default");
    }
    
    private void showMapThemePanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_maptheme");
    }
    
    private void showInterfaceThemePanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_interfacetheme");
    }
    
    private void showPreferencesPanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_preferences");
    }

	private void initContentPanel(JPanel panel){
    	
    }

	private class ButtonSelecter implements ActionListener {
		private CustomToggleButton sourceButton;
		private CustomToggleButton[] buttons;
		
		public ButtonSelecter(CustomToggleButton sourceButton, CustomToggleButton[] buttons) {
			this.sourceButton = sourceButton;
			this.buttons = buttons;		
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			for(CustomToggleButton button : buttons) {
				if(button != sourceButton) button.setOn(false);
			}
		}
	}
}
