package dk.itu.n.danmarkskort.routeplanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class RoutePlannerMain {

	private JFrame frmRouteplanner;
	private JPanel panelRouteImage, panelRouteDescription;
	private final ImageIcon ROUTE_IMAGE;
	private JButton btnHideImage, btnHideDescription;
	private JLabel lblRouteimage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoutePlannerMain window = new RoutePlannerMain();
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
	public RoutePlannerMain() {
		ROUTE_IMAGE = new ImageIcon("resources/routeplanner/demo_routeplanner.PNG");
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRouteplanner = new JFrame();
		frmRouteplanner.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icons/map-icon.png"));
		frmRouteplanner.setTitle("Route planner");
		frmRouteplanner.setBounds(100, 100, 697, 573);
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
		
		btnHideImage = new JButton("Hide Image");
		btnHideImage.addActionListener( e -> toggleShowHideRouteImage());
		panelRouteMenu.add(btnHideImage);
		
		btnHideDescription = new JButton("Hide Description");
		btnHideDescription.addActionListener( e -> toggleShowHideRouteDescription());
		panelRouteMenu.add(btnHideDescription);
		
		JButton btnPrint = new JButton("Print");
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
		
		JLabel lblRouteimage = new JLabel("");
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
		
//		routeImage();
		RoutePart();
		
		frmRouteplanner.setVisible(true);
	}
	
//	private void routeImage(){
//		panelRouteImage.getWidth();
//		ROUTE_IMAGE.getIconWidth();
//		ImageIcon routeImageScaled = new ImageIcon(new ImageIcon("resources/routeplanner/demo_routeplanner.PNG").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//		lblRouteimage.setIcon(routeImageScaled);
//	}
	
	private void toggleShowHideRouteImage(){
		if(panelRouteImage.isVisible()) {
			panelRouteImage.setVisible(false);
			btnHideImage.setText("Show Image");
		} else {
			panelRouteImage.setVisible(true);
			btnHideImage.setText("Hide Image");
		}
	}
	
	private void toggleShowHideRouteDescription(){
		if(panelRouteDescription.isVisible()) {
			panelRouteDescription.setVisible(false);
			btnHideDescription.setText("Show Description");
		} else {
			panelRouteDescription.setVisible(true);
			btnHideDescription.setText("Hide Description");
		}
	}
	
	private void RoutePart(){
		List<JPanel> partList = new ArrayList<JPanel>();
		int pos = 1;
		partList.add(new RoutePart(pos++, "Kør mod Roskildevej", "600m"));
		partList.add(new RoutePart(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePart(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePart(pos++, "Kør mod Sverigesvej", "250m"));
		partList.add(new RoutePart(pos++, "Kør mod Sverigesvej", "250m"));
		
		partList.add(new RoutePart(pos++, "Kør mod Amagerbrogade", "1,5Km"));
		partList.add(new RoutePart(pos++, "Ankommet ved distination Rosenhave", ""));
		
		for(JPanel part : partList){
			panelRouteDescription.add(part);
		}
	}

}
