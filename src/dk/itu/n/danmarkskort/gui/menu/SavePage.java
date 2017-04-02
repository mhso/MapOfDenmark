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

public class SavePage extends JPanel  {
	Style style;
	
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
		JFileChooser fc = viewFileChooser("Save Map To File", "Save");
		int fcVal = fc.showOpenDialog(this);
		
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if(!file.getAbsolutePath().endsWith(".bin")){
				file = new File(file + ".bin");
			}
			//model.save(file.getAbsolutePath());
			System.out.println("Save Current Map to bin: "+file.getAbsolutePath());
        } else {
        }	
	}
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.APPROVE_OPTION);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("*.bin", "bin"));
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		return fc;
	}

	private void initContentPanel(JPanel panel){
    	
        
    }
}