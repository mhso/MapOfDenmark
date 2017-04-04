package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.map.CanvasListener;

import java.awt.*;

public class BottomPanel extends JPanel implements CanvasListener {

	private static final long serialVersionUID = 4413404136962289564L;
	Style style;
    private JLabel scale;

    public BottomPanel(Style style) {

        this.style = style;

        setLayout(new BorderLayout());
        setOpaque(false);

        Main.map.addCanvasListener(this);
        addLeft();
        addCenter();
        addRight();
    }

    private void addLeft() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        leftPanel.setOpaque(false);

        scale = new ScaleLabel("Scale: ");
        scale.setOpaque(true);
        scale.setFont(new Font(scale.getFont().getName(), Font.PLAIN, 12));
        scale.setHorizontalAlignment(SwingConstants.CENTER);

        leftPanel.add(scale, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
    }

    private void addCenter() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(38, 0, 10, 0));
        centerPanel.setOpaque(false);

        JLabel coordsLabel = new JLabel("Lon: 0, Lat: 0");
        coordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(coordsLabel, BorderLayout.NORTH);
        
        JLabel nearestStreetLabel = new JLabel("<html><body><u>Streetname</u></body></html>");
        nearestStreetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(nearestStreetLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addRight() {
        JPanel rightParent = new JPanel(new GridLayout(2, 1, 0, 5));
        rightParent.setOpaque(false);

        CustomButton zoomIn = style.zoomInButton();
        zoomIn.setBorder(BorderFactory.createLineBorder(style.zoomButtonBG(), 5));
        rightParent.add(zoomIn);

        CustomButton zoomOut = style.zoomOutButton();
        zoomOut.setBorder(BorderFactory.createLineBorder(style.zoomButtonBG(), 5));
        rightParent.add(zoomOut);

        add(rightParent, BorderLayout.EAST);
    }

    private void setScale() {
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
