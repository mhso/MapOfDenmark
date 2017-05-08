package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;

import java.awt.*;
import javax.swing.border.TitledBorder;

public class AboutUsPage extends JPanel  {
	private static final long serialVersionUID = 7161959941791759055L;
	Style style;
    public AboutUsPage() {
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
        JLabel lblPageHeadline = new JLabel("About");
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
    }
    
    private void initContentPanel(JPanel panel){
    	GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panelCenter);
        
        JLabel lblName = new JLabel("Name:");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.WEST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 1;
        panel.add(lblName, gbc_lblName);
        
        JLabel lblAppname = new JLabel(Main.APP_NAME);
        GridBagConstraints gbc_lblAppname = new GridBagConstraints();
        gbc_lblAppname.anchor = GridBagConstraints.WEST;
        gbc_lblAppname.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppname.gridx = 1;
        gbc_lblAppname.gridy = 1;
        panel.add(lblAppname, gbc_lblAppname);
        
        JLabel lblVersion = new JLabel("Version:");
        GridBagConstraints gbc_lblVersion = new GridBagConstraints();
        gbc_lblVersion.anchor = GridBagConstraints.WEST;
        gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
        gbc_lblVersion.gridx = 0;
        gbc_lblVersion.gridy = 2;
        panel.add(lblVersion, gbc_lblVersion);
        
        JLabel lblAppversion = new JLabel(Main.APP_VERSION);
        GridBagConstraints gbc_lblAppversion = new GridBagConstraints();
        gbc_lblAppversion.anchor = GridBagConstraints.WEST;
        gbc_lblAppversion.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppversion.gridx = 1;
        gbc_lblAppversion.gridy = 2;
        panel.add(lblAppversion, gbc_lblAppversion);
        
        JLabel lblDate = new JLabel("Date:");
        GridBagConstraints gbc_lblDate = new GridBagConstraints();
        gbc_lblDate.anchor = GridBagConstraints.WEST;
        gbc_lblDate.insets = new Insets(0, 0, 5, 5);
        gbc_lblDate.gridx = 0;
        gbc_lblDate.gridy = 3;
        panel.add(lblDate, gbc_lblDate);
        
        JLabel lblAppdate = new JLabel("05.08.2017");
        GridBagConstraints gbc_lblAppdate = new GridBagConstraints();
        gbc_lblAppdate.anchor = GridBagConstraints.WEST;
        gbc_lblAppdate.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppdate.gridx = 1;
        gbc_lblAppdate.gridy = 3;
        panel.add(lblAppdate, gbc_lblAppdate);
        
        JLabel lblDescription = new JLabel("Description:");
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescription.gridx = 0;
        gbc_lblDescription.gridy = 4;
        panel.add(lblDescription, gbc_lblDescription);
        
        String appDescription = "<html>This application is made as part of our education"
        		+ "<br>as software developers, its includes a map of Denmark,"
        		+ "<br>Key features:"
        		+ "<br>Zoom/pan, search, routeplanning.";
        
        JLabel lblAppDescription = new JLabel(appDescription);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
        gbc_lblNewLabel.gridx = 1;
        gbc_lblNewLabel.gridy = 4;
        panel.add(lblAppDescription, gbc_lblNewLabel);
               
        JLabel lblTeamName = new JLabel("Team Name:");
        GridBagConstraints gbc_lblTeamName = new GridBagConstraints();
        gbc_lblTeamName.anchor = GridBagConstraints.WEST;
        gbc_lblTeamName.insets = new Insets(0, 0, 5, 5);
        gbc_lblTeamName.gridx = 0;
        gbc_lblTeamName.gridy = 6;
        panel.add(lblTeamName, gbc_lblTeamName);
        
        JLabel lblTeamAi = new JLabel("Group N");
        GridBagConstraints gbc_lblTeamAi = new GridBagConstraints();
        gbc_lblTeamAi.anchor = GridBagConstraints.WEST;
        gbc_lblTeamAi.insets = new Insets(0, 0, 5, 0);
        gbc_lblTeamAi.gridx = 1;
        gbc_lblTeamAi.gridy = 6;
        panel.add(lblTeamAi, gbc_lblTeamAi);
        
        JLabel lblDevelopers = new JLabel("Developers:");
        GridBagConstraints gbc_lblDevelopers = new GridBagConstraints();
        gbc_lblDevelopers.anchor = GridBagConstraints.WEST;
        gbc_lblDevelopers.insets = new Insets(0, 0, 5, 5);
        gbc_lblDevelopers.gridx = 0;
        gbc_lblDevelopers.gridy = 8;
        panel.add(lblDevelopers, gbc_lblDevelopers);
        
        JLabel lblName_1 = new JLabel("Mikkel Hooge Sørensen (mhso@itu.dk)");
        GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
        gbc_lblName_1.anchor = GridBagConstraints.WEST;
        gbc_lblName_1.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_1.gridx = 1;
        gbc_lblName_1.gridy = 8;
        panel.add(lblName_1, gbc_lblName_1);
        
        JLabel lblName_2 = new JLabel("Frank Andersen (fand@itu.dk)");
        GridBagConstraints gbc_lblName_2 = new GridBagConstraints();
        gbc_lblName_2.anchor = GridBagConstraints.WEST;
        gbc_lblName_2.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_2.gridx = 1;
        gbc_lblName_2.gridy = 9;
        panel.add(lblName_2, gbc_lblName_2);
        
        JLabel lblName_3 = new JLabel("Frederik Tejner Witte (frwi@itu.dk)");
        GridBagConstraints gbc_lblName_3 = new GridBagConstraints();
        gbc_lblName_3.anchor = GridBagConstraints.WEST;
        gbc_lblName_3.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_3.gridx = 1;
        gbc_lblName_3.gridy = 10;
        panel.add(lblName_3, gbc_lblName_3);
        
        JLabel lblName_4 = new JLabel("Magnus Jacobsen (mjac@itu.dk)");
        GridBagConstraints gbc_lblName_4 = new GridBagConstraints();
        gbc_lblName_4.anchor = GridBagConstraints.WEST;
        gbc_lblName_4.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_4.gridx = 1;
        gbc_lblName_4.gridy = 11;
        panel.add(lblName_4, gbc_lblName_4);
    }
}