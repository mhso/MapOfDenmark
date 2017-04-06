package dk.itu.n.danmarkskort.gui.menu;

import javax.swing.*;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.models.Region;

import java.awt.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.text.DecimalFormat;

public class LoadPage extends JPanel  {
	private static final long serialVersionUID = -3622354925202477780L;
	private Style style;
	private JLabel lblCurrentmapfilename, lblCurrentmapfilesize, lblCurrentmapaddressesfound, lblCurrentmapbounds;
	
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
        gbc_lblCurrentMap.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentMap.insets = new Insets(0, 0, 5, 5);
        gbc_lblCurrentMap.gridx = 0;
        gbc_lblCurrentMap.gridy = 1;
        panelCenter.add(lblCurrentMap, gbc_lblCurrentMap);

        lblCurrentmapfilename = new JLabel(Main.osmReader.getFileName());
        GridBagConstraints gbc_lblCurrentmapfilename = new GridBagConstraints();
        gbc_lblCurrentmapfilename.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapfilename.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilename.gridx = 1;
        gbc_lblCurrentmapfilename.gridy = 1;
        panelCenter.add(lblCurrentmapfilename, gbc_lblCurrentmapfilename);
        
        JLabel lblFilesize = new JLabel("Filesize:");
        GridBagConstraints gbc_lblFilesize = new GridBagConstraints();
        gbc_lblFilesize.anchor = GridBagConstraints.WEST;
        gbc_lblFilesize.insets = new Insets(0, 0, 5, 5);
        gbc_lblFilesize.gridx = 0;
        gbc_lblFilesize.gridy = 2;
        panelCenter.add(lblFilesize, gbc_lblFilesize);
        
        long fileSize = Util.getFileSize(new File(Main.osmReader.getFileName()));
        long kb = fileSize/1024;
		long mb = kb/1024;
        
        lblCurrentmapfilesize = new JLabel(mb + " MB");
        GridBagConstraints gbc_lblCurrentmapfilesize = new GridBagConstraints();
        gbc_lblCurrentmapfilesize.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapfilesize.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapfilesize.gridx = 1;
        gbc_lblCurrentmapfilesize.gridy = 2;
        panelCenter.add(lblCurrentmapfilesize, gbc_lblCurrentmapfilesize);
        
        JButton btnLoadNewMapFile = new JButton("Load new map file");
        btnLoadNewMapFile.addActionListener(e -> loadNewMapFile());
        
        JLabel lblMapBounds = new JLabel("Map bounds:");
        GridBagConstraints gbc_lblMapBounds = new GridBagConstraints();
        gbc_lblMapBounds.anchor = GridBagConstraints.WEST;
        gbc_lblMapBounds.insets = new Insets(0, 0, 5, 5);
        gbc_lblMapBounds.gridx = 0;
        gbc_lblMapBounds.gridy = 3;
        panelCenter.add(lblMapBounds, gbc_lblMapBounds);
        
        Region bounds = Main.model.getMapRegion();
        
        DecimalFormat format = new DecimalFormat("###,###.##");
        double latKm = -bounds.getHeight()*110.574;
        double lonKm = bounds.getWidth()*111.320*Math.cos(Math.toRadians(-bounds.y2));
        double squareKm = latKm*lonKm;
        lblCurrentmapbounds = new JLabel("<html><body>Lontitude: " + format.format(bounds.x1) + " - " + format.format(bounds.x2) + 
        		"<br>Latitude: " + format.format(-bounds.y1) + " - " + format.format(-bounds.y2) + 
        		"<br>Square Kilometres: " + format.format(squareKm) + " km²</body></html>");
        GridBagConstraints gbc_lblCurrentmapbounds = new GridBagConstraints();
        gbc_lblCurrentmapbounds.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapbounds.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapbounds.gridx = 1;
        gbc_lblCurrentmapbounds.gridy = 3;
        panelCenter.add(lblCurrentmapbounds, gbc_lblCurrentmapbounds);
        
        JLabel lblAddressesFound = new JLabel("Addresses found:");
        GridBagConstraints gbc_lblAddressesFound = new GridBagConstraints();
        gbc_lblAddressesFound.anchor = GridBagConstraints.WEST;
        gbc_lblAddressesFound.insets = new Insets(0, 0, 5, 5);
        gbc_lblAddressesFound.gridx = 0;
        gbc_lblAddressesFound.gridy = 4;
        panelCenter.add(lblAddressesFound, gbc_lblAddressesFound);
        
        lblCurrentmapaddressesfound = new JLabel("" + format.format(AddressController.getInstance().getAddressSize()));
        GridBagConstraints gbc_lblCurrentmapaddressesfound = new GridBagConstraints();
        gbc_lblCurrentmapaddressesfound.anchor = GridBagConstraints.WEST;
        gbc_lblCurrentmapaddressesfound.insets = new Insets(0, 0, 5, 0);
        gbc_lblCurrentmapaddressesfound.gridx = 1;
        gbc_lblCurrentmapaddressesfound.gridy = 4;
        panelCenter.add(lblCurrentmapaddressesfound, gbc_lblCurrentmapaddressesfound);
        GridBagConstraints gbc_btnLoadNewMapFile = new GridBagConstraints();
        gbc_btnLoadNewMapFile.anchor = GridBagConstraints.EAST;
        gbc_btnLoadNewMapFile.gridx = 1;
        gbc_btnLoadNewMapFile.gridy = 7;
        panelCenter.add(btnLoadNewMapFile, gbc_btnLoadNewMapFile);
    }
    
    private void loadNewMapFile(){
		JFileChooser fc = viewFileChooser("Load View To File", "Load");
		int fcVal = fc.showOpenDialog(this);
		
		if (fcVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			Main.startup(new String[]{file.getAbsolutePath()});
			Main.window.add(Main.createFrameComponents());
			Main.window.revalidate();
			Main.window.repaint();
			Main.map.zoomToBounds();
        }
    }
	
	private JFileChooser viewFileChooser(String dialogTitle, String approveBtnTxt){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(dialogTitle);
		fc.setApproveButtonText(approveBtnTxt);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter("Map Files", "osm", "zip", "bin"));
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
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