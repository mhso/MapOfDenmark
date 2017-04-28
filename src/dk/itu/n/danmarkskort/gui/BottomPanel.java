package dk.itu.n.danmarkskort.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.map.CanvasListener;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.WayType;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.border.EtchedBorder;

public class BottomPanel extends JPanel implements CanvasListener {

	private static final long serialVersionUID = 4413404136962289564L;
	private final Color BORDER_HIGHTLIGHT = Color.GRAY;
	private final Color BORDER_SHADOW = Color.DARK_GRAY;
	
	Style style;
    private JLabel scale;
	private JLabel coordsLabel;
	private JLabel nearestStreetLabel;
	private JSlider zoomSlider;
    private JPanel southLeftPanel;
    private JPanel southCenterPanel;
    private JButton buttonCentreView;

	private WayType[] highways = new WayType[]{
            WayType.HIGHWAY_MOTORWAY,
            WayType.HIGHWAY_TRUNK,
            WayType.HIGHWAY_PRIMARY,
            WayType.HIGHWAY_SECONDARY,
            WayType.HIGHWAY_TERTIARY,
            WayType.HIGHWAY_RESIDENTIAL,
            WayType.HIGHWAY_SERVICE,
            WayType.HIGHWAY_UNDEFINED,
            WayType.HIGHWAY_UNCLASSIFIED,
            WayType.HIGHWAY_PEDESTRIAN};

    public BottomPanel() {
    	setBorder(new EmptyBorder(0, 10, 0, 10));
    	setOpaque(false);
    	
        this.style = Main.style;

        setLayout(new BorderLayout());

        Main.map.addCanvasListener(this);
        addLeft();
        addCenter();
        addRight();
    }

    private void addLeft() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        southLeftPanel = new JPanel();
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
    	final int PANEL_SIZE = 380;
    	
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, DKConstants.WINDOW_WIDTH/2-PANEL_SIZE/2, 0, Main.window.getWidth()/2-PANEL_SIZE/2));
        centerPanel.setLayout(new BorderLayout());
        
        southCenterPanel = new JPanel();
        southCenterPanel.setLayout(new BorderLayout());
        southCenterPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        southCenterPanel.setBackground(style.panelBG());
        centerPanel.add(southCenterPanel, BorderLayout.SOUTH);

        coordsLabel = new JLabel("Lon: 0, Lat: 0");
        coordsLabel.setForeground(Color.WHITE);
        coordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southCenterPanel.add(coordsLabel, BorderLayout.CENTER);
        
        nearestStreetLabel = new JLabel("Street");
        nearestStreetLabel.setForeground(Color.WHITE);
        nearestStreetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        southCenterPanel.add(nearestStreetLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);   
       
        Main.window.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				centerPanel.setBorder(new EmptyBorder(0, Main.window.getWidth()/2-PANEL_SIZE/2, 0, Main.window.getWidth()/2-PANEL_SIZE/2));
			} 	
        });
    }

    private void addRight() {
        JPanel rightParent = new JPanel();
        rightParent.setOpaque(false);

        add(rightParent, BorderLayout.EAST);
        rightParent.setLayout(new BorderLayout(0, 2));
        
        zoomSlider = new JSlider();
        zoomSlider.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        zoomSlider.setOrientation(SwingConstants.VERTICAL);
        zoomSlider.setForeground(Color.LIGHT_GRAY);
        zoomSlider.setBackground(style.panelBG());
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setMinorTickSpacing(1);
        zoomSlider.setMajorTickSpacing(19);
        zoomSlider.setMinimum(1);
        zoomSlider.setMaximum(20);
        zoomSlider.addMouseListener(new SliderListener());
        
        rightParent.add(zoomSlider, BorderLayout.SOUTH);
        
        buttonCentreView = style.centerViewButton();
        buttonCentreView.setBorder(new EtchedBorder(EtchedBorder.RAISED, BORDER_HIGHTLIGHT, BORDER_SHADOW));
        buttonCentreView.addActionListener(e -> {
            Main.map.zoomToBounds();
        	Main.map.panToPosition(Main.model.getMapRegion().getMiddlePoint());
        	Main.map.repaint();
        });
        buttonCentreView.setFocusPainted(false);
        rightParent.add(buttonCentreView, BorderLayout.NORTH);
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
    	coordsLabel.setText("Lat: " + 
    			String.format("%.6f", mousePoint.getY()) + ", Lon: " + String.format("%.6f", mousePoint.getX()));

    	if(Main.nearest) {
    		ParsedNode query = new ParsedNode(Main.map.getGeographicalMousePosition());
            ParsedWay nearest = null;
            double shortest = Double.POSITIVE_INFINITY;
            WayType type = null;
            for(WayType waytype: highways) {
                KDTree<ParsedItem> tree = Main.model.enumMapKD.get(waytype);
                if(tree == null) continue;
                ParsedItem candidate = tree.nearest(query);
                if(candidate == null) continue;
                else {
                    double distance = KDTree.shortestDistance(query, candidate.getCoords());
                    if (distance < shortest) {
                        shortest = distance;
                        nearest = (ParsedWay) candidate;
                        type = waytype;
                    }
                }
            }
            String text = " ";
            if(nearest != null) {
            	if(Main.map.getHighlightedShape() == null || !nearest.getShape().contains
            			(Main.map.getHighlightedShape().getBounds2D().getCenterX(),
            			Main.map.getHighlightedShape().getBounds2D().getCenterY())) 
            		Main.map.highlightWay(type, nearest.getShape());
            	if(nearest.getName() != null) text = nearest.getName();
            }
            nearestStreetLabel.setText(text);

//            ParsedNode lonLat = new ParsedNode(Main.map.getGeographicalMousePosition());
//            if(lonLat != null)Main.routeController.searchEdgesKDTree(lonLat);
        }
    }
    
    @Override
	public void onZoomLevelChanged() {
		zoomSlider.setValue((int)Main.map.getZoom());
	}

	@Override
	public void onZoom() {
		setScale();
	}

	@Override
	public void onSetupDone() {
        zoomSlider.setValue((int)Main.map.getZoom());
	}
	
	private class SliderListener extends MouseAdapter {
		private int currentZoom;
		
		@Override
		public void mousePressed(MouseEvent e) {
			currentZoom = zoomSlider.getValue();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(zoomSlider.getValue() == currentZoom) return;
			currentZoom = zoomSlider.getValue();
			Point canvasCenter = new Point(Main.map.getWidth()/2, Main.map.getHeight()/2);
			Main.map.pan(-canvasCenter.getX(), -canvasCenter.getY());
			Main.map.snapToZoom(currentZoom);
			Main.map.pan(canvasCenter.getX(), canvasCenter.getY());
		}	
	}

	public void repaintPanels() {
        zoomSlider.repaint();
        southLeftPanel.repaint();
        southCenterPanel.repaint();
        buttonCentreView.repaint();
    }
}
