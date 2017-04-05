package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.map.CanvasListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BottomPanel extends JPanel implements CanvasListener {

	private static final long serialVersionUID = 4413404136962289564L;
	private final Color BORDER_HIGHTLIGHT = Color.GRAY;
	private final Color BORDER_SHADOW = Color.DARK_GRAY;
	
	Style style;
    private JLabel scale;
	private JLabel coordsLabel;
	private JSlider zoomSlider;

    public BottomPanel(Style style) {
    	setBorder(new EmptyBorder(0, 10, 0, 10));
    	setOpaque(false);
    	
        this.style = style;

        setLayout(new BorderLayout());

        Main.map.addCanvasListener(this);
        addLeft();
        addCenter();
        addRight();
    }

    private void addLeft() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JPanel southLeftPanel = new JPanel();
        southLeftPanel.setLayout(new BorderLayout());
        southLeftPanel.setBackground(style.panelBG());
        leftPanel.add(southLeftPanel, BorderLayout.SOUTH);

        scale = new ScaleLabel("Scale: ");
        scale.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        scale.setForeground(Color.WHITE);
        scale.setOpaque(false);
        scale.setFont(new Font(scale.getFont().getName(), Font.PLAIN, 12));
        scale.setHorizontalAlignment(SwingConstants.CENTER);

        southLeftPanel.add(scale, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);
    }

    private void addCenter() {
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 300, 0, 300));
        centerPanel.setLayout(new BorderLayout());
        
        JPanel southCenterPanel = new JPanel();
        southCenterPanel.setLayout(new BorderLayout());
        southCenterPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        southCenterPanel.setBackground(style.panelBG());
        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

        coordsLabel = new JLabel("Lon: 0, Lat: 0");
        coordsLabel.setForeground(Color.WHITE);
        coordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southCenterPanel.add(coordsLabel, BorderLayout.CENTER);
        
        JLabel nearestStreetLabel = new JLabel("<html><body><u>Streetname</u></body></html>");
        nearestStreetLabel.setForeground(Color.WHITE);
        nearestStreetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southCenterPanel.add(nearestStreetLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addRight() {
        JPanel rightParent = new JPanel();
        rightParent.setBackground(style.panelBG());

        add(rightParent, BorderLayout.EAST);
        rightParent.setLayout(new BorderLayout(0, 0));
        
        zoomSlider = new JSlider();
        zoomSlider.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        zoomSlider.setOrientation(SwingConstants.VERTICAL);
        zoomSlider.setForeground(Color.LIGHT_GRAY);
        zoomSlider.setOpaque(false);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setMajorTickSpacing(19);
        zoomSlider.setMinimum(1);
        zoomSlider.setMaximum(20);
        
        rightParent.add(zoomSlider, BorderLayout.SOUTH);
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
    public void onMouseMoved() {
    	Point2D mousePoint = Util.toRealCoords(Main.map.getGeographicalMousePosition());
    	coordsLabel.setText("Lon: " + String.format("%.4f", mousePoint.getX()) + ", Lat: " + 
    			String.format("%.4f", mousePoint.getY()));
    }
    
    @Override
	public void onZoomLevelChanged() {
		
	}

	@Override
	public void onZoom() {
		setScale();
	}

	@Override
	public void onSetupDone() {
        zoomSlider.setValue((int)Main.map.getZoom());
        /* zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				Main.map.snapToZoom(zoomSlider.getValue());
			}
        }); */
	}
	
}
