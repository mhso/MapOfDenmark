package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.gui.components.PanelFileInfo;

import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

public class LoadPage extends JPanel  {
	private static final long serialVersionUID = -3622354925202477780L;
	private Style style;
	private JLabel lblCurrentmapfilename, lblCurrentmapfilesize, lblCurrentmapaddressesfound, lblCurrentmapbounds;
	
    public LoadPage() {
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
        JLabel lblPageHeadline = new JLabel("Load New Map");
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
        
        JPanel panelCenter = new PanelFileInfo();
        panelCenter.setBackground(style.menuContentBG());
        panelPage.add(panelCenter, BorderLayout.CENTER);
        initContentPanel(panelCenter);
        
        GridBagConstraints gbc_btnLoadNewMapFile = new GridBagConstraints();
        gbc_btnLoadNewMapFile.anchor = GridBagConstraints.EAST;
        gbc_btnLoadNewMapFile.gridx = 1;
        gbc_btnLoadNewMapFile.gridy = 7;
        JButton btnLoadNewMapFile = new JButton("Load new map file");
        btnLoadNewMapFile.addActionListener(e -> loadNewMapFile());
        panelCenter.add(btnLoadNewMapFile, gbc_btnLoadNewMapFile);
    }
    
    private void loadNewMapFile(){
		JFileChooser fc = viewFileChooser("Load View To File", "Load");
		int fcVal = fc.showOpenDialog(this);
		
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			Main.window.dispose();
			Main.launch(new String[]{file.getAbsolutePath()});
			Main.map.isSetupDone();
        }
    }
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Map Files", "osm", "zip", "bin"));
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		return fc;
	}

	private void initContentPanel(JPanel panel){
    	
    }
	
	public void updateCurrentMapInfo(String mapfilename, String mapfilesize, String mapbounds, String mapaddressesfound){
		lblCurrentmapfilename.setText(mapfilename);
		lblCurrentmapfilesize.setText(mapfilesize);
		lblCurrentmapbounds.setText(mapbounds);
		lblCurrentmapaddressesfound.setText(mapaddressesfound);
	}
	
}