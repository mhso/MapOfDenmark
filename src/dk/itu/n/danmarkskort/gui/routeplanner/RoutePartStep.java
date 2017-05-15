package dk.itu.n.danmarkskort.gui.routeplanner;

import javax.swing.JPanel;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.models.RouteModel;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.ImageIcon;

public class RoutePartStep extends JPanel {
	private final String STEP_DESCRIPTION, STEP_DISTANCE, STEP_POSITION;
	private Style style;
	
	public RoutePartStep(int stepPosition, ImageIcon stepIcon, RouteModel routeModel) {
		style = Main.style;
		Color textColor = style.routeText();

		setBackground(style.routeInnerBG());
		STEP_POSITION = stepPosition+"";
		STEP_DESCRIPTION = routeModel.getDescription();
		STEP_DISTANCE = makeDistance(routeModel.getDistance());
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel();
		lblRouteIcon.setIcon(stepIcon);
		lblRouteIcon.setForeground(textColor);
		GridBagConstraints gbc_lblRouteIcon = new GridBagConstraints();
		gbc_lblRouteIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteIcon.gridx = 0;
		gbc_lblRouteIcon.gridy = 0;
		add(lblRouteIcon, gbc_lblRouteIcon);
		
		JLabel lblRoutePosition = new JLabel(STEP_POSITION);
		lblRoutePosition.setFont(style.normalText());
		lblRoutePosition.setForeground(textColor);
		GridBagConstraints gbc_lblRouteposition = new GridBagConstraints();
		gbc_lblRouteposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteposition.gridx = 1;
		gbc_lblRouteposition.gridy = 0;
		add(lblRoutePosition, gbc_lblRouteposition);
		
		JLabel lblRoutedescription = new JLabel(STEP_DESCRIPTION);
		lblRoutedescription.setFont(style.normalText());
		lblRoutedescription.setForeground(textColor);
		GridBagConstraints gbc_lblRoutedescription = new GridBagConstraints();
		gbc_lblRoutedescription.anchor = GridBagConstraints.WEST;
		gbc_lblRoutedescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoutedescription.gridx = 2;
		gbc_lblRoutedescription.gridy = 0;
		add(lblRoutedescription, gbc_lblRoutedescription);
		
		JLabel lblRoutedistance = new JLabel(STEP_DISTANCE);
		lblRoutedistance.setFont(style.normalText());
		lblRoutedistance.setForeground(textColor.darker());
		GridBagConstraints gbc_lblRoutedistance = new GridBagConstraints();
		gbc_lblRoutedistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRoutedistance.anchor = GridBagConstraints.EAST;
		gbc_lblRoutedistance.gridx = 13;
		gbc_lblRoutedistance.gridy = 0;
		add(lblRoutedistance, gbc_lblRoutedistance);
		
	}
	
	private String makeDistance(double input){
		if(input == -1) return "";
		if(input / 1000 > 1.0) return String.format("(%.1f", input / 1000) + " km)";
		return String.format("(%.0f", input) + " metres)";
	}
}
