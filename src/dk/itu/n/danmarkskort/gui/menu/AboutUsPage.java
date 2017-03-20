package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;

import java.awt.*;

public class AboutUsPage extends JPanel {

    public AboutUsPage() {

        setPreferredSize(new Dimension(400, 800));
        setOpaque(false);
        
        JPanel panelPage = new JPanel();
        panelPage.setLayout(new BorderLayout(0, 0));
        panelPage.setPreferredSize(new Dimension(400, 800));
        add(panelPage);
        
        JPanel panelHeadline = new JPanel();
        panelPage.add(panelHeadline, BorderLayout.NORTH);
        JLabel lblPageHeadline = new JLabel("About Us Page");
        panelHeadline.add(lblPageHeadline);
        
        JPanel panelCenter = new JPanel();
        panelPage.add(panelCenter, BorderLayout.CENTER);
        GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCenter.setLayout(gbl_panelCenter);
        
        JLabel lblName = new JLabel("Name:");
        GridBagConstraints gbc_lblName = new GridBagConstraints();
        gbc_lblName.anchor = GridBagConstraints.WEST;
        gbc_lblName.insets = new Insets(0, 0, 5, 5);
        gbc_lblName.gridx = 0;
        gbc_lblName.gridy = 1;
        panelCenter.add(lblName, gbc_lblName);
        
        JLabel lblAppname = new JLabel("AppName");
        GridBagConstraints gbc_lblAppname = new GridBagConstraints();
        gbc_lblAppname.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppname.gridx = 1;
        gbc_lblAppname.gridy = 1;
        panelCenter.add(lblAppname, gbc_lblAppname);
        
        JLabel lblVersion = new JLabel("Version:");
        GridBagConstraints gbc_lblVersion = new GridBagConstraints();
        gbc_lblVersion.anchor = GridBagConstraints.WEST;
        gbc_lblVersion.insets = new Insets(0, 0, 5, 5);
        gbc_lblVersion.gridx = 0;
        gbc_lblVersion.gridy = 2;
        panelCenter.add(lblVersion, gbc_lblVersion);
        
        JLabel lblAppversion = new JLabel("AppVersion");
        GridBagConstraints gbc_lblAppversion = new GridBagConstraints();
        gbc_lblAppversion.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppversion.gridx = 1;
        gbc_lblAppversion.gridy = 2;
        panelCenter.add(lblAppversion, gbc_lblAppversion);
        
        JLabel lblDescription = new JLabel("Description:");
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.anchor = GridBagConstraints.WEST;
        gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
        gbc_lblDescription.gridx = 0;
        gbc_lblDescription.gridy = 3;
        panelCenter.add(lblDescription, gbc_lblDescription);
        
        JLabel lblAppdescription = new JLabel("AppDescription");
        GridBagConstraints gbc_lblAppdescription = new GridBagConstraints();
        gbc_lblAppdescription.gridheight = 2;
        gbc_lblAppdescription.insets = new Insets(0, 0, 5, 0);
        gbc_lblAppdescription.gridx = 1;
        gbc_lblAppdescription.gridy = 4;
        panelCenter.add(lblAppdescription, gbc_lblAppdescription);
        
        JLabel lblTeam = new JLabel("Team:");
        GridBagConstraints gbc_lblTeam = new GridBagConstraints();
        gbc_lblTeam.anchor = GridBagConstraints.WEST;
        gbc_lblTeam.insets = new Insets(0, 0, 5, 5);
        gbc_lblTeam.gridx = 0;
        gbc_lblTeam.gridy = 6;
        panelCenter.add(lblTeam, gbc_lblTeam);
        
        JLabel lblTeamAi = new JLabel("Team AI");
        GridBagConstraints gbc_lblTeamAi = new GridBagConstraints();
        gbc_lblTeamAi.insets = new Insets(0, 0, 5, 0);
        gbc_lblTeamAi.gridx = 1;
        gbc_lblTeamAi.gridy = 6;
        panelCenter.add(lblTeamAi, gbc_lblTeamAi);
        
        JLabel lblDevelopers = new JLabel("Developers:");
        GridBagConstraints gbc_lblDevelopers = new GridBagConstraints();
        gbc_lblDevelopers.anchor = GridBagConstraints.WEST;
        gbc_lblDevelopers.insets = new Insets(0, 0, 5, 5);
        gbc_lblDevelopers.gridx = 0;
        gbc_lblDevelopers.gridy = 7;
        panelCenter.add(lblDevelopers, gbc_lblDevelopers);
        
        JLabel lblName_1 = new JLabel("Name1");
        GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
        gbc_lblName_1.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_1.gridx = 1;
        gbc_lblName_1.gridy = 7;
        panelCenter.add(lblName_1, gbc_lblName_1);
        
        JLabel lblName_2 = new JLabel("Name2");
        GridBagConstraints gbc_lblName_2 = new GridBagConstraints();
        gbc_lblName_2.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_2.gridx = 1;
        gbc_lblName_2.gridy = 8;
        panelCenter.add(lblName_2, gbc_lblName_2);
        
        JLabel lblName_3 = new JLabel(" Name3");
        GridBagConstraints gbc_lblName_3 = new GridBagConstraints();
        gbc_lblName_3.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_3.gridx = 1;
        gbc_lblName_3.gridy = 9;
        panelCenter.add(lblName_3, gbc_lblName_3);
        
        JLabel lblName_4 = new JLabel("Name4");
        GridBagConstraints gbc_lblName_4 = new GridBagConstraints();
        gbc_lblName_4.insets = new Insets(0, 0, 5, 0);
        gbc_lblName_4.gridx = 1;
        gbc_lblName_4.gridy = 10;
        panelCenter.add(lblName_4, gbc_lblName_4);
        
        JLabel lblOther = new JLabel("Other:");
        GridBagConstraints gbc_lblOther = new GridBagConstraints();
        gbc_lblOther.insets = new Insets(0, 0, 5, 5);
        gbc_lblOther.gridx = 0;
        gbc_lblOther.gridy = 11;
        panelCenter.add(lblOther, gbc_lblOther);
        
        JLabel lblThisApplicationsIs = new JLabel("This applications is using OpenStreetmaps as the foundation for presenting the map");
        GridBagConstraints gbc_lblThisApplicationsIs = new GridBagConstraints();
        gbc_lblThisApplicationsIs.insets = new Insets(0, 0, 5, 0);
        gbc_lblThisApplicationsIs.gridx = 1;
        gbc_lblThisApplicationsIs.gridy = 11;
        panelCenter.add(lblThisApplicationsIs, gbc_lblThisApplicationsIs);

        /*
        JLabel lblAppVersion = new JLabel("Version: "+Main.APP_VERSION);
        panelCenter.add(lblAppVersion);
        
        
        JLabel lblAppName = new JLabel("Name: "+Main.APP_NAME);
        GridBagConstraints gbc_lblAppName = new GridBagConstraints();
        gbc_lblAppName.fill = GridBagConstraints.BOTH;
        gbc_lblAppName.insets = new Insets(0, 0, 0, 5);
        gbc_lblAppName.gridx = 0;
        gbc_lblAppName.gridy = 0;
        panelCenter.add(lblAppName, gbc_lblAppName);
        JLabel lblDescription = new JLabel("Description: ");
        GridBagConstraints gbc_lblDescription = new GridBagConstraints();
        gbc_lblDescription.fill = GridBagConstraints.BOTH;
        gbc_lblDescription.insets = new Insets(0, 0, 0, 5);
        gbc_lblDescription.gridx = 1;
        gbc_lblDescription.gridy = 0;
        panelCenter.add(lblDescription, gbc_lblDescription);
        JLabel lblTeam = new JLabel("Team: ");
        GridBagConstraints gbc_lblTeam = new GridBagConstraints();
        gbc_lblTeam.fill = GridBagConstraints.BOTH;
        gbc_lblTeam.insets = new Insets(0, 0, 0, 5);
        gbc_lblTeam.gridx = 3;
        gbc_lblTeam.gridy = 0;
        panelCenter.add(lblTeam, gbc_lblTeam);
        JLabel lblAuthors = new JLabel(
        		"<html><body>"
        		+"Authors:<br>"
        		+ "<br>Mikkel Hooge Sørensen (mhso@itu.dk)"
        		+ "<br>Frank Andersen (fand@itu.dk)"
        		+ "<br>Frederik Tejner Witte (frwi@itu.dk)"
        		+ "\nMagnus Jacobsen (mjac@itu.dk)"
        		+ "</body></html>");
        GridBagConstraints gbc_lblAuthors = new GridBagConstraints();
        gbc_lblAuthors.fill = GridBagConstraints.BOTH;
        gbc_lblAuthors.insets = new Insets(0, 0, 0, 5);
        gbc_lblAuthors.gridx = 4;
        gbc_lblAuthors.gridy = 0;
        panelCenter.add(lblAuthors, gbc_lblAuthors);
        JLabel lblCopyrightNotits = new JLabel("Copyright TeamAI, 2017 ");
        GridBagConstraints gbc_lblCopyrightNotits = new GridBagConstraints();
        gbc_lblCopyrightNotits.fill = GridBagConstraints.BOTH;
        gbc_lblCopyrightNotits.gridx = 5;
        gbc_lblCopyrightNotits.gridy = 0;
        panelCenter.add(lblCopyrightNotits, gbc_lblCopyrightNotits);
        */

    }

}
