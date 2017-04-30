package dk.itu.n.danmarkskort.models;

public class RouteModel {
	private final String DESCRIPTION;
	private double distance;
	private final RouteEnum DIRECTION;
	
	public RouteModel(RouteEnum direction, String description, double distance){
		DIRECTION = direction;
		DESCRIPTION = getStepDescription(direction, description);
		this.distance = distance;
	}

	public String getDescription() { return DESCRIPTION; }

	public double getDistance() { return distance; }
	public void setDistance(double distance) { this.distance = distance; }

	public RouteEnum getDirection() { return DIRECTION; }
	
	private String getStepDescription(RouteEnum routeEnum, String description){
		String stepDirection = "";
		switch(routeEnum){
		case AT_DESTINATION: stepDirection = "Ankommer til destination, ";
			break;
		case CONTINUE_ON: stepDirection = "Forsæt ad ";
			break;
		case ROUNDABOUND_1_ROAD: stepDirection = "I rundkørsel, tag første vej til højre, mod ";
			break;
		case ROUNDABOUND_2_ROAD: stepDirection = "I rundkørsel, tag anden vej til højre, mod ";
			break;
		case ROUNDABOUND_3_ROAD: stepDirection = "I rundkørsel, tag tredje vej til højre, mod ";
			break;
		case ROUNDABOUND_4_ROAD: stepDirection = "I rundkørsel, tag fjerde vej til højre, mod ";
			break;
		case ROUNDABOUND_5_ROAD: stepDirection = "I rundkørsel, tag femte vej til højre, mod ";
			break;
		case TURN_LEFT: stepDirection = "Drej til venstre ad ";
			break;
		case TURN_RIGHT: stepDirection = "Drej til højre ad ";
			break;
		default:
			break;
		}
		return stepDirection + description;
	}
	
}
