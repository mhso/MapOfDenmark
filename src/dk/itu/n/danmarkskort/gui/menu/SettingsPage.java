package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.components.CustomToggleButton;
import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;

import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SettingsPage extends JPanel {
	private static final long serialVersionUID = 4642167284178775315L;
	private Style style;
	private JPanel panelCenter;
	private DropdownMenu menu;
    
    public SettingsPage(DropdownMenu menu) {
    	style = Main.style;
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
        
        JLabel lblPageHeadline = new JLabel("Settings");
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
        
        panelCenter = new JPanel();
        panelCenter.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new CardLayout());
        
        JPanel defaultCenterPanel = new JPanel();
        defaultCenterPanel.setOpaque(false);
        defaultCenterPanel.setLayout(new GridLayout(0, 1, 0, 10));
        panelCenter.add(defaultCenterPanel, "name_default");
        
        JButton buttonChangeMapTheme = style.themeMenuButton();
        buttonChangeMapTheme.addActionListener(e -> showMapThemePanel());
        defaultCenterPanel.add(buttonChangeMapTheme);
        
        JButton buttonChangePreferences = new JButton("Change Preferences");
        buttonChangePreferences.setBackground(new Color(224, 255, 255));
        buttonChangePreferences.setBorder(new LineBorder(new Color(0, 153, 204), 2, true));
        buttonChangePreferences.addActionListener(e -> showPreferencesPanel());
        defaultCenterPanel.add(buttonChangePreferences);
        
        // Hack! Couldn't get ScrollPane to work properly with CardLayout, so inserting a RigidArea here to push setting buttons to the top.
        defaultCenterPanel.add(Box.createRigidArea(new Dimension(20, 30)));
        
        createMapThemePanel();
        
        createPreferencesPanel();
    }
    
    private void createMapThemePanel() {
    	final int SUBPANEL_WIDTH = 420;
    	final int SUBPANEL_HEIGHT = 40;
    	
    	JPanel panelMapTheme = new JPanel();
        panelMapTheme.setOpaque(false);
        panelCenter.add(panelMapTheme, "name_maptheme");
        panelMapTheme.setLayout(new BorderLayout(0, 0));
        
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
        	if(!buttonBasicToggle.isSelected()) {
        		Main.userPreferences.setCurrentMapTheme("Basic");
            	changeCurrentTheme();
        	}
        });
        buttonBasicToggle.setSelected(false);
        panelBasicTheme.add(buttonBasicToggle, BorderLayout.EAST);
        
        Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
        panel.add(rigidArea);
        
        JPanel panelNightTheme = new JPanel();
        panelNightTheme.setOpaque(false);
        panelNightTheme.setMaximumSize(new Dimension(SUBPANEL_WIDTH, SUBPANEL_HEIGHT));
        panel.add(panelNightTheme);
        panelNightTheme.setLayout(new BorderLayout(0, 0));
        
        JLabel labelNight = new JLabel("Night");
        panelNightTheme.add(labelNight, BorderLayout.WEST);
        
        JLabel labelNightIcon = new JLabel(style.nightThemePreview());
        panelNightTheme.add(labelNightIcon, BorderLayout.CENTER);
        
        CustomToggleButton buttonNightToggle = style.toggleButton();
        buttonNightToggle.addActionListener(e -> {
        	if(!buttonNightToggle.isSelected()) {
        		Main.userPreferences.setCurrentMapTheme("Night");
        		changeCurrentTheme();
        	}
        });
        buttonNightToggle.setSelected(false);
        panelNightTheme.add(buttonNightToggle, BorderLayout.EAST);
        
        rigidArea = Box.createRigidArea(new Dimension(20, 20));
        panel.add(rigidArea);
        
        JPanel panelBWTheme = new JPanel();
        panelBWTheme.setOpaque(false);
        panelBWTheme.setMaximumSize(new Dimension(SUBPANEL_WIDTH, SUBPANEL_HEIGHT));
        panel.add(panelBWTheme);
        panelBWTheme.setLayout(new BorderLayout(0, 0));
        
        JLabel labelBW = new JLabel("<html><body>Gray-<br>scale</body></html>");
        panelBWTheme.add(labelBW, BorderLayout.WEST);
        
        JLabel labelBWIcon = new JLabel(style.bwThemePreview());
        panelBWTheme.add(labelBWIcon, BorderLayout.CENTER);
        
        CustomToggleButton buttonBWToggle = style.toggleButton();
        buttonBWToggle.addActionListener(e -> {
        	if(!buttonBWToggle.isSelected()) {
        		Main.userPreferences.setCurrentMapTheme("BW");
        		changeCurrentTheme();
        	}
        });
        buttonBWToggle.setSelected(false);
        panelBWTheme.add(buttonBWToggle, BorderLayout.EAST);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        panelMapTheme.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(new BorderLayout(0, 0));
        
        JButton buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> showDefaultPanel());
        panelSouth.add(buttonBack, BorderLayout.WEST);
        
        CustomToggleButton[] buttons = {buttonBasicToggle, buttonNightToggle, buttonBWToggle};
        for(CustomToggleButton button : buttons) {
        	button.addActionListener(new ButtonSelecter(button, buttons));
        }
        
        if(Main.userPreferences.getCurrentMapTheme().equals("Basic")) buttonBasicToggle.setSelected(true);
        else if(Main.userPreferences.getCurrentMapTheme().equals("Night")) buttonBWToggle.setSelected(true);
        else if(Main.userPreferences.getCurrentMapTheme().equals("BW")) buttonBWToggle.setSelected(true);
    }
    
    private void changeCurrentTheme() {
    	if(Main.production) GraphicRepresentation.parseData(
    			getClass().getResource("/resources/themes/Theme" + Main.userPreferences.getCurrentMapTheme() + ".XML").toString());
    	else GraphicRepresentation.parseData("resources/themes/Theme" + Main.userPreferences.getCurrentMapTheme() + ".XML");
    	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
    	Main.map.forceRepaint();
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
        defaultMapFilePanel.setLayout(new BorderLayout(15, 0));
        centerPanel.add(defaultMapFilePanel);
        
        JLabel defaultMapFileLabel = new JLabel("Default Map File:");
        defaultMapFilePanel.add(defaultMapFileLabel, BorderLayout.WEST);
        
        JComboBox<String> defaultMapFileBox = new JComboBox<>(getParsedFiles());
        defaultMapFileBox.addActionListener(e -> {
        	Main.userPreferences.setDefaultMapFile(defaultMapFileBox.getSelectedItem().toString());
        	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
        });
        defaultMapFileBox.addMouseListener(new MouseAdapter() {
        	@Override
			public void mousePressed(MouseEvent e) {
				menu.blockVisibility(true);
			}
        	
			@Override
			public void mouseEntered(MouseEvent e) {
				menu.blockVisibility(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				menu.blockVisibility(false);
			}
        });
        defaultMapFileBox.setSelectedItem(Main.userPreferences.getDefaultMapFile());
        defaultMapFilePanel.add(defaultMapFileBox, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        
        JPanel defaultMapThemePanel = new JPanel();
        defaultMapThemePanel.setOpaque(false);
        defaultMapThemePanel.setLayout(new BorderLayout(15, 0));
        centerPanel.add(defaultMapThemePanel);
        
        JLabel defaultMapThemeLabel = new JLabel("Default Map Theme:");
        defaultMapThemePanel.add(defaultMapThemeLabel, BorderLayout.WEST);
        
        JComboBox<String> defaultMapThemeBox = new JComboBox<>(new String[]{"Basic", "Night", ""});
        defaultMapThemeBox.addActionListener(e -> {
        	Main.userPreferences.setDefaultTheme(defaultMapThemeBox.getSelectedItem().toString());
        	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
        });
        defaultMapThemeBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				menu.blockVisibility(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				menu.blockVisibility(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				menu.blockVisibility(false);
			}
        });
        defaultMapThemeBox.setSelectedItem(Main.userPreferences.getDefaultTheme());
        defaultMapThemeBox.setPreferredSize(new Dimension(defaultMapFileBox.getPreferredSize()));
        defaultMapThemePanel.add(defaultMapThemeBox, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        
        JPanel maximizeOnStartupPanel = new JPanel();
        maximizeOnStartupPanel.setOpaque(false);
        maximizeOnStartupPanel.setLayout(new BorderLayout(15, 0));
        centerPanel.add(maximizeOnStartupPanel);
        
        JLabel maximizeLabel = new JLabel("Maximize On Startup: ");
        maximizeOnStartupPanel.add(maximizeLabel, BorderLayout.WEST);
        
        JCheckBox maximizeButton = new JCheckBox();
        maximizeButton.addActionListener(e -> {
        	Main.userPreferences.setMaximizeOnStartup(maximizeButton.isSelected());
        	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
        });
        maximizeButton.setBackground(panelCenter.getBackground());
        maximizeButton.setSelected(Main.userPreferences.isMaximizeOnStartup());
        maximizeOnStartupPanel.add(maximizeButton, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        
        JPanel highlightRoadPanel = new JPanel();
        highlightRoadPanel.setOpaque(false);
        centerPanel.add(highlightRoadPanel);
        highlightRoadPanel.setLayout(new BorderLayout(15, 0));
        
        JLabel highlightLabel = new JLabel("Hightlight Nearest Road");
        highlightRoadPanel.add(highlightLabel, BorderLayout.WEST);
        
        JCheckBox highlightButton = new JCheckBox();
        highlightButton.addActionListener(e -> {
        	Main.userPreferences.setHighlightNearestRoad(highlightButton.isSelected());
        	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
        });
        highlightButton.setBackground(panelCenter.getBackground());
        highlightButton.setSelected(Main.userPreferences.isHighlightingNearestRoad());
        highlightRoadPanel.add(highlightButton, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        
        JPanel useDjikstraWithAStarPanel = new JPanel();
        useDjikstraWithAStarPanel.setOpaque(false);
        centerPanel.add(useDjikstraWithAStarPanel);
        useDjikstraWithAStarPanel.setLayout(new BorderLayout(15, 0));
        
        JLabel useDjikstraWithAStarLabel = new JLabel("Use Djikstra with A* as search algorithm: ");
        useDjikstraWithAStarPanel.add(useDjikstraWithAStarLabel, BorderLayout.WEST);
        
        JCheckBox useDjikstraWithAStarCheckBox = new JCheckBox();
        useDjikstraWithAStarCheckBox.addActionListener(e ->
        		Main.userPreferences.setUseDjikstraWithAStar(useDjikstraWithAStarCheckBox.isSelected()));
        useDjikstraWithAStarCheckBox.setBackground(panelCenter.getBackground());
        useDjikstraWithAStarCheckBox.setSelected(Main.userPreferences.useDjikstraWithAStar());
        useDjikstraWithAStarPanel.add(useDjikstraWithAStarCheckBox, BorderLayout.EAST);
        
        centerPanel.add(Box.createRigidArea(new Dimension(20, 10)));
        
        JPanel drawDjikstraPanel = new JPanel();
        drawDjikstraPanel.setOpaque(false);
        centerPanel.add(drawDjikstraPanel);
        drawDjikstraPanel.setLayout(new BorderLayout(15, 0));
        
        JLabel drawDjikstraLabel = new JLabel("DEBUG: Draw Djikstra/A*");
        drawDjikstraPanel.add(drawDjikstraLabel, BorderLayout.WEST);
        
        JCheckBox drawDjikstraCheckBox = new JCheckBox();
        drawDjikstraCheckBox.addActionListener(e -> Main.routeController.isDrawingDjikstra = drawDjikstraCheckBox.isSelected());
        drawDjikstraCheckBox.setBackground(panelCenter.getBackground());
        drawDjikstraCheckBox.setSelected(Main.userPreferences.isHighlightingNearestRoad());
        drawDjikstraPanel.add(drawDjikstraCheckBox, BorderLayout.EAST);
        
        JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        panelDefaultValues.add(panelSouth, BorderLayout.SOUTH);
        panelSouth.setLayout(new BorderLayout(0, 0));
        
        JButton buttonBack = new JButton("Back");
        buttonBack.addActionListener(e -> showDefaultPanel());
        panelSouth.add(buttonBack, BorderLayout.WEST);
        
        // Hack! Couldn't get ScrollPane to work properly with CardLayout, so inserting a RigidArea here to push preference buttons to the top.
        panelSouth.add(Box.createRigidArea(new Dimension(20, 60)), BorderLayout.SOUTH);
    }
    
    private String[] getParsedFiles() {
		List<Path> binFiles = new ArrayList<>();
		try {
			for(Path entry : Files.newDirectoryStream(Paths.get("parsedOSMFiles"))) {
				for(Path binFile : Files.newDirectoryStream(entry)) {
					if(!binFile.toFile().getName().equals("pinpoints.bin")) {
						binFiles.add(binFile);
					}
				}
			}
		}
		catch (IOException e) {
			if(Main.debug) e.printStackTrace();
		}
		String[] results = new String[binFiles.size()];
		for(int i = 0; i < binFiles.size(); i++) {
			results[i] = binFiles.get(i).getFileName().toString();
		}
		return results;
	}
    
    private void showDefaultPanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_default");
    }
    
    private void showMapThemePanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_maptheme");
    }
    
    private void showPreferencesPanel() {
    	CardLayout cl = (CardLayout)panelCenter.getLayout();
		cl.show(panelCenter, "name_preferences");
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
				if(button != sourceButton) button.setSelected(false);
			}
		}
	}
}
