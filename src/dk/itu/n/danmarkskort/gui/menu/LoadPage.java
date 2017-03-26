package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;

import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class LoadPage extends JPanel  {
	Style style;
    public LoadPage() {
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
        JLabel lblPageHeadline = new JLabel("Load");
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
        gbl_panelCenter.columnWidths = new int[]{0, 0, 0};
        gbl_panelCenter.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panelCenter.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelCenter.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCenter.setLayout(gbl_panelCenter);
        
        JLabel lblCurrentMap = new JLabel("Current Map:");
        GridBagConstraints gbc_lblCurrentMap = new GridBagConstraints();
        gbc_lblCurrentMap.insets = new Insets(0, 0, 5, 5);
        gbc_lblCurrentMap.gridx = 0;
        gbc_lblCurrentMap.gridy = 1;
        panelCenter.add(lblCurrentMap, gbc_lblCurrentMap);
        
        JLabel lblCurrentmapfilename = new JLabel("CurrentMapFileName");
        GridBagConstraints gbc_lblCurrentmapfilename = new GridBagConstraints();
        gbc_lblCurrentmapfilename.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilename.gridx = 1;
        gbc_lblCurrentmapfilename.gridy = 1;
        panelCenter.add(lblCurrentmapfilename, gbc_lblCurrentmapfilename);
        
        JLabel lblFilesize = new JLabel("Filesize:");
        GridBagConstraints gbc_lblFilesize = new GridBagConstraints();
        gbc_lblFilesize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFilesize.gridx = 0;
        gbc_lblFilesize.gridy = 2;
        panelCenter.add(lblFilesize, gbc_lblFilesize);
        
        JLabel lblCurrentmapfilesize = new JLabel("CurrentMapFileSize");
        GridBagConstraints gbc_lblCurrentmapfilesize = new GridBagConstraints();
        gbc_lblCurrentmapfilesize.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilesize.gridx = 1;
        gbc_lblCurrentmapfilesize.gridy = 2;
        panelCenter.add(lblCurrentmapfilesize, gbc_lblCurrentmapfilesize);
        
        JLabel lblAddressesFound = new JLabel("Addresses found:");
        GridBagConstraints gbc_lblAddressesFound = new GridBagConstraints();
        gbc_lblAddressesFound.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddressesFound.gridx = 0;
        gbc_lblAddressesFound.gridy = 3;
        panelCenter.add(lblAddressesFound, gbc_lblAddressesFound);
        
        JLabel lblCurrentmapaddressesfound = new JLabel("CurrentMapAddressesFound");
        GridBagConstraints gbc_lblCurrentmapaddressesfound = new GridBagConstraints();
        gbc_lblCurrentmapaddressesfound.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapaddressesfound.gridx = 1;
        gbc_lblCurrentmapaddressesfound.gridy = 3;
        panelCenter.add(lblCurrentmapaddressesfound, gbc_lblCurrentmapaddressesfound);
        
        JLabel lblMapBounds = new JLabel("Map bounds:");
        GridBagConstraints gbc_lblMapBounds = new GridBagConstraints();
        gbc_lblMapBounds.insets = new Insets(0, 0, 5, 5);
        gbc_lblMapBounds.gridx = 0;
        gbc_lblMapBounds.gridy = 4;
        panelCenter.add(lblMapBounds, gbc_lblMapBounds);
        
        JButton btnLoadNewMapFile = new JButton("Load new map file");
        btnLoadNewMapFile.addActionListener(e -> loadNewMapFile());
        GridBagConstraints gbc_btnLoadNewMapFile = new GridBagConstraints();
        gbc_btnLoadNewMapFile.gridx = 1;
        gbc_btnLoadNewMapFile.gridy = 7;
        panelCenter.add(btnLoadNewMapFile, gbc_btnLoadNewMapFile);
    }
    
    private void loadNewMapFile(){
		JFileChooser fc = viewFileChooser("Load View To File", "Load");
		int fcVal = fc.showOpenDialog(this);
		
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(!file.getAbsolutePath().endsWith(".osm")){
				file = new File(file + ".osm");
			}
			//model.loadNewModel(file.getAbsolutePath());
        } else {
        }

	}
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("*.osm", "osm"));
		fc.addChoosableFileFilter(new FileNameExtensionFilter("*.zip", "zip"));
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		return fc;
	}

	private void initContentPanel(JPanel panel){
    	GridBagLayout gbl_panelCenter = new GridBagLayout();
        
    }
}