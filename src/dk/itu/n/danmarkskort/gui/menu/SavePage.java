package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.backend.BinaryWrapper;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.components.PanelFileInfo;

import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

public class SavePage extends JPanel  {
	private static final long serialVersionUID = -4502910705647356589L;
	private Style style;
	
    public SavePage() {
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
        JLabel lblPageHeadline = new JLabel("Save Current Map");
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
        
        JPanel panelCenter = new PanelFileInfo();
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        
        JButton btnSaveToFile = new JButton("Save To File");
        btnSaveToFile.addActionListener(e -> saveCurrentMap());
        GridBagConstraints gbc_btnSaveToFile = new GridBagConstraints();
        gbc_btnSaveToFile.anchor = GridBagConstraints.EAST;
        gbc_btnSaveToFile.gridx = 1;
        gbc_btnSaveToFile.gridy = 7;
        panelCenter.add(btnSaveToFile, gbc_btnSaveToFile);
    }
    
    private void saveCurrentMap(){
    	JFileChooser fc = viewFileChooser("Save Map", "Save");
    	int appv = fc.showSaveDialog(this);
    	if(appv == JFileChooser.APPROVE_OPTION) {
    		String fileName = fc.getSelectedFile().getAbsolutePath();
    		if(!fileName.endsWith(".bin")) fileName = fileName + ".bin";
    		BinaryWrapper binary = new BinaryWrapper();
        	Util.addAllToBinary(binary);
        	Util.writeObjectToFile(binary, fileName);
    	}
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