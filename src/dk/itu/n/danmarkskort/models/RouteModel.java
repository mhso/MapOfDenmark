package dk.itu.n.danmarkskort.models;

public class RouteModel {
	private final String DESCRIPTION;
	private double distance;
	private final RouteEnum DIRECTION;
	private int maxSpeed;
	
	public RouteModel(RouteEnum direction, String description, int maxSpeed, double distance){
		DIRECTION = direction;
		DESCRIPTION = getStepDescription(direction, description);
		this.maxSpeed = maxSpeed;
		this.distance = distance;
	}

	public String getDescription() { return DESCRIPTION; }

	public double getDistance() { return distance; }
	public void setDistance(double distance) { this.distance = distance; }
	
	public int getMaxSpeed() { return maxSpeed; }
	public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }

	public RouteEnum getDirection() { return DIRECTION; }
	
	private String getStepDescription(RouteEnum routeEnum, String description){
		String stepDirection = "";
		switch(routeEnum){
		case AT_DESTINATION: stepDirection = "Arrives at destination, "; // "Ankommer til destination, ";
			break;
		case CONTINUE_ON: stepDirection = "Continue at "; // "Forsæt ad ";
			break;
		case ROUNDABOUND_1_ROAD: stepDirection = "In roundabout, take first road to the right, against "; // "I rundkørsel, tag første vej til højre, mod ";
			break;
		case ROUNDABOUND_2_ROAD: stepDirection = "In roundabout, take second road to the right, against "; // "I rundkørsel, tag anden vej til højre, mod ";
			break;
		case ROUNDABOUND_3_ROAD: stepDirection = "In roundabout, take third road to the right, against "; // "I rundkørsel, tag tredje vej til højre, mod ";
			break;
		case ROUNDABOUND_4_ROAD: stepDirection = "In roundabout, take fourth road to the right, against "; // "I rundkørsel, tag fjerde vej til højre, mod ";
			break;
		case ROUNDABOUND_5_ROAD: stepDirection = "In roundabout, take fifth road to the right, against "; // "I rundkørsel, tag femte vej til højre, mod ";
			break;
		case TURN_LEFT: stepDirection = "Turn left at "; // "Drej til venstre ad ";
			break;
		case TURN_RIGHT: stepDirection = "Turn right at "; // "Drej til højre ad ";
			break;
		default:
			break;
		}
		return stepDirection + description;
	}
	
}
