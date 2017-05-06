package dk.itu.n.danmarkskort.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.Region;

public class PanelFileInfo extends JPanel {
	public PanelFileInfo() {
		GridBagLayout gbl_panelCenter = new GridBagLayout();
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gbl_panelCenter);
        
        JLabel labelCurrentMap = new JLabel("Current Map:");
        GridBagConstraints gbc_lblCurrentMap = new GridBagConstraints();
        gbc_lblCurrentMap.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentMap.insets = new Insets(0, 0, 5, 5);
        gbc_lblCurrentMap.gridx = 0;
        gbc_lblCurrentMap.gridy = 1;
        add(labelCurrentMap, gbc_lblCurrentMap);

        JLabel labelCurrentmapfilename = new JLabel(new File(Main.osmReader.getFileName()).getName());
        GridBagConstraints gbc_lblCurrentmapfilename = new GridBagConstraints();
        gbc_lblCurrentmapfilename.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapfilename.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilename.gridx = 1;
        gbc_lblCurrentmapfilename.gridy = 1;
        add(labelCurrentmapfilename, gbc_lblCurrentmapfilename);
        
        JLabel labelFilesize = new JLabel("Filesize:");
        GridBagConstraints gbc_lblFilesize = new GridBagConstraints();
        gbc_lblFilesize.anchor = GridBagConstraints.WEST;
        gbc_lblFilesize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFilesize.gridx = 0;
        gbc_lblFilesize.gridy = 2;
        add(labelFilesize, gbc_lblFilesize);
        
        long fileSize = Util.getFileSize(new File(Main.osmReader.getFileName()));
        long kb = fileSize/1024;
		long mb = kb/1024;
        
        JLabel labelCurrentmapfilesize = new JLabel(mb + " MB");
        GridBagConstraints gbc_lblCurrentmapfilesize = new GridBagConstraints();
        gbc_lblCurrentmapfilesize.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapfilesize.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilesize.gridx = 1;
        gbc_lblCurrentmapfilesize.gridy = 2;
        add(labelCurrentmapfilesize, gbc_lblCurrentmapfilesize);
        
        JLabel labelMapBounds = new JLabel("Map bounds:");
        GridBagConstraints gbc_lblMapBounds = new GridBagConstraints();
        gbc_lblMapBounds.anchor = GridBagConstraints.WEST;
        gbc_lblMapBounds.insets = new Insets(0, 0, 5, 5);
        gbc_lblMapBounds.gridx = 0;
        gbc_lblMapBounds.gridy = 3;
        add(labelMapBounds, gbc_lblMapBounds);
        
        Region bounds = Main.model.getMapRegion();
        
        DecimalFormat format = new DecimalFormat("###,###.##");
        double latKm = -bounds.getHeight()*110.574;
        double lonKm = bounds.getWidth()*111.320*Math.cos(Math.toRadians(-bounds.y2));
        double squareKm = latKm*lonKm;
        JLabel lblCurrentmapbounds = new JLabel("<html><body>Longitude: " + format.format(bounds.x1) + " - " + format.format(bounds.x2) +
        		"<br>Latitude: " + format.format(-bounds.y1) + " - " + format.format(-bounds.y2) + 
        		"<br>Square Kilometres: " + format.format(squareKm) + " km²</body></html>");
        GridBagConstraints gbc_lblCurrentmapbounds = new GridBagConstraints();
        gbc_lblCurrentmapbounds.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapbounds.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapbounds.gridx = 1;
        gbc_lblCurrentmapbounds.gridy = 3;
        add(lblCurrentmapbounds, gbc_lblCurrentmapbounds);
        
        JLabel lblAddressesFound = new JLabel("Addresses found:");
        GridBagConstraints gbc_lblAddressesFound = new GridBagConstraints();
        gbc_lblAddressesFound.anchor = GridBagConstraints.WEST;
        gbc_lblAddressesFound.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddressesFound.gridx = 0;
        gbc_lblAddressesFound.gridy = 4;
        add(lblAddressesFound, gbc_lblAddressesFound);
        
        JLabel lblCurrentmapaddressesfound = new JLabel("" + format.format(Main.addressController.getAddressSize()));
        GridBagConstraints gbc_lblCurrentmapaddressesfound = new GridBagConstraints();
        gbc_lblCurrentmapaddressesfound.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapaddressesfound.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapaddressesfound.gridx = 1;
        gbc_lblCurrentmapaddressesfound.gridy = 4;
        add(lblCurrentmapaddressesfound, gbc_lblCurrentmapaddressesfound);
	}
}
