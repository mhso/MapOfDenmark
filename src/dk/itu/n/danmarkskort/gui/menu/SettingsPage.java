package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import dk.itu.n.danmarkskort.gui.Style;

import java.awt.*;
import javax.swing.border.EmptyBorder;

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
        panelCenter.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        panelCenter.setLayout(new CardLayout());
        
        JPanel defaultCenterPanel = new JPanel();
        panelCenter.add(defaultCenterPanel, BorderLayout.CENTER);
        defaultCenterPanel.setLayout(new GridLayout(0, 1, 0, 10));
        
        JButton buttonChangeMapTheme = new JButton("Change Map Theme");
        buttonChangeMapTheme.addActionListener(e -> showMapThemePanel());
        panelCenter.add(buttonChangeMapTheme);
        
        JButton buttonChangeInterfaceTheme = new JButton("Change Interface Theme");
        panelCenter.add(buttonChangeInterfaceTheme);
        
        JButton buttonDefaultValues = new JButton("Change Default Values");
        panelCenter.add(buttonDefaultValues);
    }
    
    private void showMapThemePanel() {
    	
    }

	private void initContentPanel(JPanel panel){
    	
    }

}
