package dk.itu.n.danmarkskort.gui;

import javax.swing.*;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.map.CanvasListener;

import java.awt.*;

public class BottomPanel extends JPanel implements CanvasListener {

	private static final long serialVersionUID = 4413404136962289564L;
	Style style;
    GridBagConstraints gbcParent;
    private JLabel scale;

    public BottomPanel(Style style) {

        this.style = style;

        setLayout(new GridBagLayout());
        setOpaque(false);

        gbcParent = new GridBagConstraints();
        gbcParent.anchor = GridBagConstraints.SOUTH;

        Main.map.addCanvasListener(this);
        addLeft();
        addCenter();
        addRight();
    }

    private void addLeft() {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel dummy = new JLabel();
        gbc.weightx = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1;

        JPanel leftPanel = new JPanel(new GridBagLayout());
        gbcParent.gridx = 0;
        gbcParent.gridy = 1;
        leftPanel.add(dummy, gbc);

        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        scale = new ScaleLabel("Scale: ");
        scale.setFont(new Font(scale.getFont().getName(), Font.PLAIN, 12));
        scale.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(scale, gbc);
        add(leftPanel, gbcParent);
    }

    private void addCenter() {
        // the idea is that this panel is gonna contain a box at the bottom center,
        // which displays the longitude-latitude where the mouse is, and a the name
        // of the nearest street

        //right now it contains nothing

        JLabel dummy = new JLabel();
        gbcParent.gridx = 1;
        gbcParent.weightx = 1;
        add(dummy, gbcParent);
    }

    private void addRight() {

        JPanel rightParent = new JPanel(new GridBagLayout());
        rightParent.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel dummy = new JLabel();
        gbc.weighty = 1;
        gbc.weightx = 1;
        rightParent.add(dummy, gbc);

        CustomButton zoomIn = style.zoomInButton();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        zoomIn.setBorder(BorderFactory.createLineBorder(style.zoomButtonBG(), 5));
        rightParent.add(zoomIn, gbc);

        CustomButton zoomOut = style.zoomOutButton();
        zoomOut.setBorder(BorderFactory.createLineBorder(style.zoomButtonBG(), 5));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 0, 0);
        rightParent.add(zoomOut, gbc);

        gbcParent.gridx = 2;
        gbcParent.weightx = 0.0;
        add(rightParent, gbcParent);
    }

    private void setScale() {
    	if(Main.map == null) return;
    	double denmarkWidth = DKConstants.BOUNDS_DENMARK.maxLong - DKConstants.BOUNDS_DENMARK.minLong;
        double denmarkLonKm = denmarkWidth*111.320*Math.cos(Math.toRadians(-DKConstants.BOUNDS_DENMARK.maxLat));
        
    	double viewWidth = Main.map.getGeographicalRegion().getWidth();
        double viewLonKm = viewWidth*111.320*Math.cos(Math.toRadians(-Main.map.getGeographicalRegion().y2));
    	
        String scaleString = " km";
        double kmScale = viewLonKm/12;
        if(kmScale < 1) {
        	kmScale *= 1000;
        	scaleString = " m";
        }
    	scale.setText((int)kmScale + scaleString);
    }
    
    @Override
	public void onZoomLevelChanged() {
		
	}

	@Override
	public void onZoom() {
		setScale();
	}
	
}
