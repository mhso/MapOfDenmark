package dk.itu.n.danmarkskort.routeplanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class RoutePlannerMain {

	private JFrame frmRouteplanner;
	private JPanel panelRouteImage, panelRouteDescription;
	private final ImageIcon ROUTE_IMAGE;
	private JLabel lblRouteimage;
	String routeTo, routeFrom;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoutePlannerMain window = new RoutePlannerMain("","");
					window.frmRouteplanner.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RoutePlannerMain(String routeFrom, String routeTo) {
		ROUTE_IMAGE = new ImageIcon("resources/routeplanner/demo_routeplanner.PNG");
		this.routeTo = routeTo;
		this.routeFrom = routeFrom;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRouteplanner = new JFrame();
		frmRouteplanner.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
		frmRouteplanner.setTitle("Route planner");
		frmRouteplanner.setBounds(100, 100, 800, 900);
		frmRouteplanner.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmRouteplanner.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelHeadline = new JPanel();
		frmRouteplanner.getContentPane().add(panelHeadline, BorderLayout.NORTH);
		
		JLabel lblRoutePlannerResult = new JLabel("Route planner result");
		lblRoutePlannerResult.setFont(new Font("Tahoma", Font.BOLD, 24));
		panelHeadline.add(lblRoutePlannerResult);
		
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
		chckbxShowImage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				toggleShowHideRouteImage();
			}
		});
		panelRouteMenu.add(chckbxShowImage);
		
		JCheckBox chckbxShowDescription = new JCheckBox("Show Description");
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
		lblRouteimage.setIcon(ROUTE_IMAGE);
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
		
		RoutePart();
		
		frmRouteplanner.setVisible(true);
		routeImage();
	}
	
	private void routeImage(){
		int newWidth = panelRouteImage.getWidth()-30;
		int oldWidth = ROUTE_IMAGE.getIconWidth();
		int oldHeight = ROUTE_IMAGE.getIconHeight();
		
		double diff = (double)newWidth / (double) oldWidth;
		int newHeight = (int)(oldHeight * diff);
		
		ImageIcon routeImageScaled = new ImageIcon(new ImageIcon("resources/routeplanner/demo_routeplanner.PNG")
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
	
	private void RoutePart(){
		List<JPanel> partList = new ArrayList<JPanel>();
		int pos = 1;
		
		panelRouteDescription.add(new RoutePartBasic(pos++, routeFrom, routeTo, "101Km"));
		
		partList.add(new RoutePartStep(pos++, "Kør mod Roskildevej", "600m"));
		partList.add(new RoutePartStep(pos++, "Kør mod Roskildevej", "600m"));
		partList.add(new RoutePartStep(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePartStep(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePartStep(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePartStep(pos++, "Kør mod Sverigesvej", "250m"));
		
		partList.add(new RoutePartStep(pos++, "Kør mod Amagerbrogade", "1,5Km"));
		partList.add(new RoutePartStep(pos++, "Ankommet ved distination Rosenhave", ""));
		
		for(JPanel part : partList){
			panelRouteDescription.add(part);
		}
	}

}
