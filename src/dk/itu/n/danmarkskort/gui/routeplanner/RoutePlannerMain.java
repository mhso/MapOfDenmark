package dk.itu.n.danmarkskort.gui.routeplanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Image;

import javax.swing.JScrollPane;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.models.RouteEnum;
import dk.itu.n.danmarkskort.models.RouteModel;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.event.ItemEvent;

public class RoutePlannerMain {

	private JFrame frmRouteplanner;
	private JPanel panelRouteImage, panelRouteDescription;
	private JLabel lblRouteimage;
	private final String ROUTE_FROM, ROUTE_TO;
	private final WeightEnum weightEnum;

	/**
	 * Create the application.
	 */
	public RoutePlannerMain(BufferedImage routeImage, String routeFrom, String routeTo, String routeDistance, WeightEnum weightEnum, List<RouteModel> routeModels) {
		ROUTE_FROM = routeFrom;
		ROUTE_TO = routeTo;
		this.weightEnum = weightEnum;
		
		initialize();
		makeRoute(ROUTE_FROM, ROUTE_TO, routeDistance, routeModels);
		frmRouteplanner.setVisible(true);
		routeImage(routeImage);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRouteplanner = new JFrame();
		frmRouteplanner.setIconImage(Main.style.frameIcon());
		frmRouteplanner.setTitle("Route planner");
		frmRouteplanner.setBounds(100, 100, 800, 900);
		frmRouteplanner.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmRouteplanner.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelHeadline = new JPanel();
		frmRouteplanner.getContentPane().add(panelHeadline, BorderLayout.NORTH);
		
		JPanel panelWest = new JPanel();
		frmRouteplanner.getContentPane().add(panelWest, BorderLayout.WEST);
		
		JPanel panelCenter = new JPanel();
		frmRouteplanner.getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRoute = new JPanel();
		panelCenter.add(panelRoute);
		panelRoute.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRouteMenu = new JPanel();
		panelRoute.add(panelRouteMenu, BorderLayout.NORTH);
		
		JCheckBox chckbxShowImage = new JCheckBox("Show Image");
		chckbxShowImage.setSelected(true);
		chckbxShowImage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				toggleShowHideRouteImage();
			}
		});
		panelRouteMenu.add(chckbxShowImage);
		
		JCheckBox chckbxShowDescription = new JCheckBox("Show Description");
		chckbxShowDescription.setSelected(true);
		chckbxShowDescription.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				toggleShowHideRouteDescription();
			}
		});
		panelRouteMenu.add(chckbxShowDescription);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.setToolTipText("Disabled because it is a \"nice to have function\"");
		btnPrint.setEnabled(false);
		panelRouteMenu.add(btnPrint);
		
		JPanel panelRouteContent = new JPanel();
		panelRoute.add(panelRouteContent, BorderLayout.CENTER);
		panelRouteContent.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelRouteContent.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		JPanel panelRouteWest = new JPanel();
		panelRouteImage = new JPanel();
		
		lblRouteimage = new JLabel("");
		lblRouteimage.setVisible(true);
		panelRouteImage.add(lblRouteimage, BorderLayout.NORTH);

		panelRouteDescription = new JPanel();
		panelRouteDescription.setLayout(new BoxLayout(panelRouteDescription, BoxLayout.Y_AXIS));
		panel.setLayout(new BorderLayout(0, 0));

		panel.add(panelRouteWest, BorderLayout.WEST);
		panel.add(panelRouteImage, BorderLayout.NORTH);
		panel.add(panelRouteDescription, BorderLayout.CENTER);
		
		JPanel panelEast = new JPanel();
		frmRouteplanner.getContentPane().add(panelEast, BorderLayout.EAST);
		
		JPanel panelSouth = new JPanel();
		frmRouteplanner.getContentPane().add(panelSouth, BorderLayout.SOUTH);
	}
	
	
	private void routeImage(BufferedImage bufferedImage){
		int newWidth = panelRouteImage.getWidth()-30;
		int oldWidth = bufferedImage.getWidth();
		int oldHeight = bufferedImage.getHeight();
		
		double diff = (double)newWidth / (double) oldWidth;
		int newHeight = (int)(oldHeight * diff);
		ImageIcon routeImageScaled = new ImageIcon(new ImageIcon(bufferedImage)
				.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
		lblRouteimage.setIcon(routeImageScaled);
	}
	
	private void toggleShowHideRouteImage(){
		if(panelRouteImage.isVisible()) {
			panelRouteImage.setVisible(false);
		} else {
			panelRouteImage.setVisible(true);
		}
	}
	
	private void toggleShowHideRouteDescription(){
		if(panelRouteDescription.isVisible()) {
			panelRouteDescription.setVisible(false);
		} else {
			panelRouteDescription.setVisible(true);
		}
	}
	
	private void makeRoute(String routeFrom, String routeTo, String routeDistance,  List<RouteModel> routeModels){
		RoutePartBasic routePartBasic = new RoutePartBasic(routeFrom, routeTo, weightEnum, panelRouteDescription, routeModels);
	}
}