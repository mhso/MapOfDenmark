package dk.itu.n.danmarkskort.routeplanner;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class RoutePart extends JPanel {

	private final String ROUTE_DESCRIPTION, ROUTE_DISTANCE;
	
	public RoutePart(String RouteDescription, String RouteDistance) {
		
		ROUTE_DESCRIPTION = RouteDescription;
		ROUTE_DISTANCE = RouteDistance;
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel("Icon");
		GridBagConstraints gbc_lblRouteIcon = new GridBagConstraints();
		gbc_lblRouteIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteIcon.gridx = 0;
		gbc_lblRouteIcon.gridy = 0;
		add(lblRouteIcon, gbc_lblRouteIcon);
		
		JLabel lblRoutedescription = new JLabel(ROUTE_DESCRIPTION);
		GridBagConstraints gbc_lblRoutedescription = new GridBagConstraints();
		gbc_lblRoutedescription.anchor = GridBagConstraints.WEST;
		gbc_lblRoutedescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoutedescription.gridx = 1;
		gbc_lblRoutedescription.gridy = 0;
		add(lblRoutedescription, gbc_lblRoutedescription);
		
		JLabel lblRoutedistance = new JLabel(ROUTE_DISTANCE);
		GridBagConstraints gbc_lblRoutedistance = new GridBagConstraints();
		gbc_lblRoutedistance.anchor = GridBagConstraints.EAST;
		gbc_lblRoutedistance.gridx = 13;
		gbc_lblRoutedistance.gridy = 1;
		add(lblRoutedistance, gbc_lblRoutedistance);
		
	}

}
