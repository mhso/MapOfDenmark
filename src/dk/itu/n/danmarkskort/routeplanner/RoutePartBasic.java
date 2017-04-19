package dk.itu.n.danmarkskort.routeplanner;

import javax.swing.JPanel;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class RoutePartBasic extends JPanel {
	private static final long serialVersionUID = 3150387631326599889L;
	private final String ROUTE_FROM, ROUTE_TO, ROUTE_DISTANCE, ROUTE_POSITION;
	
	public RoutePartBasic(int routePosition, String routeFrom, String routeTo, String routeDistance) {
		
		ROUTE_POSITION = routePosition+"";
		ROUTE_FROM = routeFrom;
		ROUTE_TO = routeTo;
		ROUTE_DISTANCE = routeDistance;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel();
		lblRouteIcon.setIcon(new ImageIcon("resources/routeplanner/map_64.png"));
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
		
		JLabel lblHereYouGo = new JLabel("Here you go, a wonderfull route has been planned");
		GridBagConstraints gbc_lblHereYouGo = new GridBagConstraints();
		gbc_lblHereYouGo.anchor = GridBagConstraints.WEST;
		gbc_lblHereYouGo.insets = new Insets(0, 0, 5, 5);
		gbc_lblHereYouGo.gridx = 2;
		gbc_lblHereYouGo.gridy = 0;
		add(lblHereYouGo, gbc_lblHereYouGo);
		
		JLabel lblRouteDistance = new JLabel(ROUTE_DISTANCE);
		GridBagConstraints gbc_lblRouteDistance = new GridBagConstraints();
		gbc_lblRouteDistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRouteDistance.anchor = GridBagConstraints.EAST;
		gbc_lblRouteDistance.gridx = 13;
		gbc_lblRouteDistance.gridy = 0;
		add(lblRouteDistance, gbc_lblRouteDistance);
		
		JLabel lblFrom = new JLabel("From: "+ROUTE_FROM);
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.anchor = GridBagConstraints.WEST;
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 2;
		gbc_lblFrom.gridy = 1;
		add(lblFrom, gbc_lblFrom);
		
		JLabel lblTo = new JLabel("To: "+ROUTE_TO);
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.WEST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 2;
		gbc_lblTo.gridy = 2;
		add(lblTo, gbc_lblTo);
		
	}
}
