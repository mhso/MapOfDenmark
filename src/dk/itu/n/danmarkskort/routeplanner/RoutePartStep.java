package dk.itu.n.danmarkskort.routeplanner;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class RoutePartStep extends JPanel {

	private final String ROUTE_DESCRIPTION, ROUTE_DISTANCE, ROUTE_POSITION;
	
	public RoutePartStep(int routePosition, String routeDescription, String routeDistance) {
		
		ROUTE_POSITION = routePosition+"";
		ROUTE_DESCRIPTION = routeDescription;
		ROUTE_DISTANCE = routeDistance;
		
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
		
		JLabel lblRoutePosition = new JLabel(ROUTE_POSITION);
		GridBagConstraints gbc_lblRouteposition = new GridBagConstraints();
		gbc_lblRouteposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteposition.gridx = 1;
		gbc_lblRouteposition.gridy = 0;
		add(lblRoutePosition, gbc_lblRouteposition);
		
		JLabel lblRoutedescription = new JLabel(ROUTE_DESCRIPTION);
		GridBagConstraints gbc_lblRoutedescription = new GridBagConstraints();
		gbc_lblRoutedescription.anchor = GridBagConstraints.WEST;
		gbc_lblRoutedescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoutedescription.gridx = 2;
		gbc_lblRoutedescription.gridy = 0;
		add(lblRoutedescription, gbc_lblRoutedescription);
		
		JLabel lblRoutedistance = new JLabel(ROUTE_DISTANCE);
		GridBagConstraints gbc_lblRoutedistance = new GridBagConstraints();
		gbc_lblRoutedistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRoutedistance.anchor = GridBagConstraints.EAST;
		gbc_lblRoutedistance.gridx = 13;
		gbc_lblRoutedistance.gridy = 0;
		add(lblRoutedistance, gbc_lblRoutedistance);
		
	}

}
