package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.backend.BinaryWrapper;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.models.UserPreferences;

import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

public class SavePage extends JPanel  {
	private static final long serialVersionUID = -4502910705647356589L;
	private Style style;
	
    public SavePage() {
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
        JLabel lblPageHeadline = new JLabel("Save Current Map");
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
        
        JButton btnSaveToFile = new JButton("Save To File");
        btnSaveToFile.addActionListener(e -> saveCurrentMap());
        GridBagConstraints gbc_btnSaveToFile = new GridBagConstraints();
        gbc_btnSaveToFile.anchor = GridBagConstraints.EAST;
        gbc_btnSaveToFile.gridx = 1;
        gbc_btnSaveToFile.gridy = 7;
        panelCenter.add(btnSaveToFile, gbc_btnSaveToFile);
    }
    
    private void saveCurrentMap(){
    	BinaryWrapper binary = new BinaryWrapper();
    	binary.setModel(Main.model);
    	binary.setAddressHolder(Main.addressController.getAddressHolder());
    	Util.writeObjectToFile(Main.userPreferences, DKConstants.USERPREF_PATH);
    	Util.writeObjectToFile(binary, Util.getBinaryFilePath());
	}
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Bin Files", "bin"));
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		return fc;
	}

	private void initContentPanel(JPanel panel){
    	
        
    }
}