package dk.itu.n.danmarkskort.gui.routeplanner;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.models.RouteEnum;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

public class RoutePartBasic extends JPanel {
	private static final long serialVersionUID = 3150387631326599889L;
	private final String ROUTE_FROM, ROUTE_TO, ROUTE_DISTANCE;
	
	public RoutePartBasic(String routeFrom, String routeTo, String routeDistance) {
		
		ROUTE_FROM = routeFrom;
		ROUTE_TO = routeTo;
		ROUTE_DISTANCE = routeDistance;
		RouteImageSplit routeImageSplit = new RouteImageSplit();
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel();
		lblRouteIcon.setIcon(routeImageSplit.getStepIcon(RouteEnum.START_AT));
		GridBagConstraints gbc_lblRouteIcon = new GridBagConstraints();
		gbc_lblRouteIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteIcon.gridx = 0;
		gbc_lblRouteIcon.gridy = 0;
		add(lblRouteIcon, gbc_lblRouteIcon);
		
		JLabel lblHereYouGo = new JLabel("Here you go, a wonderfull route has been planned");
		lblHereYouGo.setFont(new Font("Tahoma", Font.BOLD, 17));
		GridBagConstraints gbc_lblHereYouGo = new GridBagConstraints();
		gbc_lblHereYouGo.anchor = GridBagConstraints.WEST;
		gbc_lblHereYouGo.insets = new Insets(0, 0, 5, 5);
		gbc_lblHereYouGo.gridx = 2;
		gbc_lblHereYouGo.gridy = 0;
		add(lblHereYouGo, gbc_lblHereYouGo);
		
		JLabel lblRouteDistance = new JLabel("(" + ROUTE_DISTANCE + ")");
		lblRouteDistance.setFont(new Font("Tahoma", Font.BOLD, 17));
		GridBagConstraints gbc_lblRouteDistance = new GridBagConstraints();
		gbc_lblRouteDistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRouteDistance.anchor = GridBagConstraints.EAST;
		gbc_lblRouteDistance.gridx = 13;
		gbc_lblRouteDistance.gridy = 0;
		add(lblRouteDistance, gbc_lblRouteDistance);
		
		JLabel lblFrom = new JLabel("From: "+ROUTE_FROM);
		lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.anchor = GridBagConstraints.WEST;
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 2;
		gbc_lblFrom.gridy = 1;
		add(lblFrom, gbc_lblFrom);
		
		JLabel lblTo = new JLabel("To: "+ROUTE_TO);
		lblTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.WEST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 2;
		gbc_lblTo.gridy = 2;
		add(lblTo, gbc_lblTo);
		
	}
}
