package dk.itu.n.danmarkskort.routeplanner;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;

public class RoutePartStep extends JPanel {
	private final String STEP_DESCRIPTION, STEP_DISTANCE, STEP_POSITION;
	
	public RoutePartStep(int stepPosition, RouteEnum stepDirection, String stepDescription, String stepDistance) {
		
		STEP_POSITION = stepPosition+"";
		STEP_DESCRIPTION = getStepDescription(stepDirection, stepDescription);
		STEP_DISTANCE = stepDistance;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel();
		lblRouteIcon.setIcon(getStepIcon(stepDirection));
		GridBagConstraints gbc_lblRouteIcon = new GridBagConstraints();
		gbc_lblRouteIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteIcon.gridx = 0;
		gbc_lblRouteIcon.gridy = 0;
		add(lblRouteIcon, gbc_lblRouteIcon);
		
		JLabel lblRoutePosition = new JLabel(STEP_POSITION);
		GridBagConstraints gbc_lblRouteposition = new GridBagConstraints();
		gbc_lblRouteposition.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteposition.gridx = 1;
		gbc_lblRouteposition.gridy = 0;
		add(lblRoutePosition, gbc_lblRouteposition);
		
		JLabel lblRoutedescription = new JLabel(STEP_DESCRIPTION);
		GridBagConstraints gbc_lblRoutedescription = new GridBagConstraints();
		gbc_lblRoutedescription.anchor = GridBagConstraints.WEST;
		gbc_lblRoutedescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblRoutedescription.gridx = 2;
		gbc_lblRoutedescription.gridy = 0;
		add(lblRoutedescription, gbc_lblRoutedescription);
		
		JLabel lblRoutedistance = new JLabel(STEP_DISTANCE);
		GridBagConstraints gbc_lblRoutedistance = new GridBagConstraints();
		gbc_lblRoutedistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRoutedistance.anchor = GridBagConstraints.EAST;
		gbc_lblRoutedistance.gridx = 13;
		gbc_lblRoutedistance.gridy = 0;
		add(lblRoutedistance, gbc_lblRoutedistance);
		
	}
	
	private String getStepDescription(RouteEnum routeEnum, String roadOrPlace){
		String stepDirection = "";
		switch(routeEnum){
		case AT_DESTINATION: stepDirection = "Ankommer til destination, ";
			break;
		case CONTINUE_ON: stepDirection = "Forsæt ad ";
			break;
		case ROUNDABOUND_1_ROAD:  stepDirection = "I rundkørsel, tag første vej til højre, mod ";
			break;
		case ROUNDABOUND_2_ROAD:  stepDirection = "I rundkørsel, tag anden vej til højre, mod ";
			break;
		case ROUNDABOUND_3_ROAD:  stepDirection = "I rundkørsel, tag tredje vej til højre, mod ";
			break;
		case ROUNDABOUND_4_ROAD:  stepDirection = "I rundkørsel, tag fjerde vej til højre, mod ";
			break;
		case ROUNDABOUND_5_ROAD:  stepDirection = "I rundkørsel, tag femte vej til højre, mod ";
			break;
		case TURN_LEFT: stepDirection = "Drej til venstre ad ";
			break;
		case TURN_RIGHT: stepDirection = "Drej til højre ad ";
			break;
		default:
			break;
		}
		return stepDirection + roadOrPlace;
	}
	
	private ImageIcon getStepIcon(RouteEnum routeEnum){
		String iconFilename = "map_64.png";
		switch(routeEnum){
		case AT_DESTINATION: iconFilename = "map-location_64.png";
			break;
		case CONTINUE_ON: iconFilename = "road-sign-arrow_forward.png";
			break;
		case TURN_LEFT: iconFilename = "road_sign_arrow_turn_left.png";
			break;
		case TURN_RIGHT: iconFilename = "road_sign_arrow_turn_right.png";
			break;
		default:
			break;
			
		}
		String filePath = "resources/routeplanner/" + iconFilename;
		return new ImageIcon(filePath);
	}
}
