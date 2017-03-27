package dk.itu.n.danmarkskort.routeplanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;

public class RoutePlannerMain {

	private JFrame frmRouteplanner;

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRouteplanner = new JFrame();
		frmRouteplanner.setTitle("RoutePlanner");
		frmRouteplanner.setBounds(100, 100, 697, 573);
		frmRouteplanner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRouteplanner.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		frmRouteplanner.getContentPane().add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblRoutePlannerResult = new JLabel("Route planner result");
		lblRoutePlannerResult.setFont(new Font("Tahoma", Font.BOLD, 24));
		panelNorth.add(lblRoutePlannerResult);
		
		JPanel panelWest = new JPanel();
		frmRouteplanner.getContentPane().add(panelWest, BorderLayout.WEST);
		
		JPanel panelCenter = new JPanel();
		frmRouteplanner.getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRouteContent = new JPanel();
		panelCenter.add(panelRouteContent);
		panelRouteContent.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRouteMenu = new JPanel();
		panelRouteContent.add(panelRouteMenu, BorderLayout.NORTH);
		
		JButton btnHideImage = new JButton("Hide Image");
		panelRouteMenu.add(btnHideImage);
		
		JButton btnHideDescription = new JButton("Hide Description");
		panelRouteMenu.add(btnHideDescription);
		
		JButton btnPrint = new JButton("Print");
		panelRouteMenu.add(btnPrint);
		
		JPanel panelRouteImage = new JPanel();
		panelRouteContent.add(panelRouteImage, BorderLayout.CENTER);
		panelRouteImage.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panelRouteImage.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblRouteimage = new JLabel("RouteImage");
		panel.add(lblRouteimage, BorderLayout.NORTH);
		
		JPanel panelEast = new JPanel();
		frmRouteplanner.getContentPane().add(panelEast, BorderLayout.EAST);
		
		JPanel panelSouth = new JPanel();
		frmRouteplanner.getContentPane().add(panelSouth, BorderLayout.SOUTH);
	}

}
