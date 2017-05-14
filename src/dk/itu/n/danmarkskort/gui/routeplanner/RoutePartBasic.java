package dk.itu.n.danmarkskort.gui.routeplanner;

import javax.swing.JPanel;


import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.Style;
import dk.itu.n.danmarkskort.models.RouteModel;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

import javax.swing.JLabel;
import java.util.List;

public class RoutePartBasic extends JPanel {
	private static final long serialVersionUID = 3150387631326599889L;
	private final String ROUTE_FROM, ROUTE_TO;
	private final WeightEnum weightEnum;
	private double routeTotalDistance;
	private int routeSeconds;
	private RouteImageSplit routeImageSplit;
	private Style style;

	public RoutePartBasic(String routeFrom, String routeTo, WeightEnum weightEnum, JPanel parent,  List<RouteModel> routeModels) {
		ROUTE_FROM = routeFrom;
		ROUTE_TO = routeTo;
		this.weightEnum = weightEnum;
		routeImageSplit = new RouteImageSplit();

		style = Main.style;
		setBackground(style.routeInnerBG());
		
		initRouteSteps(parent, routeModels);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblRouteIcon = new JLabel();
		GridBagConstraints gbc_lblRouteIcon = new GridBagConstraints();
		gbc_lblRouteIcon.insets = new Insets(0, 0, 5, 5);
		gbc_lblRouteIcon.gridx = 0;
		gbc_lblRouteIcon.gridy = 0;
		add(lblRouteIcon, gbc_lblRouteIcon);
		
		JLabel lblHereYouGo = new JLabel("Here you go. A wonderful route has been planned just for you");
		lblHereYouGo.setForeground(style.routeTextLight());
		lblHereYouGo.setFont(style.defaultHeadline());
		GridBagConstraints gbc_lblHereYouGo = new GridBagConstraints();
		gbc_lblHereYouGo.anchor = GridBagConstraints.WEST;
		gbc_lblHereYouGo.insets = new Insets(0, 0, 5, 5);
		gbc_lblHereYouGo.gridx = 2;
		gbc_lblHereYouGo.gridy = 0;
		add(lblHereYouGo, gbc_lblHereYouGo);
		
		JLabel lblFrom = new JLabel("From: "+ROUTE_FROM);
		lblFrom.setForeground(style.routeText());
		lblFrom.setFont(style.smallHeadline());
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.anchor = GridBagConstraints.WEST;
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.gridx = 2;
		gbc_lblFrom.gridy = 1;
		add(lblFrom, gbc_lblFrom);
		
		JLabel lblTo = new JLabel("To: "+ROUTE_TO);
		lblTo.setForeground(style.routeText());
		lblTo.setFont(style.smallHeadline());
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.WEST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 2;
		gbc_lblTo.gridy = 2;
		add(lblTo, gbc_lblTo);
		
		JLabel lblRouteDistance = new JLabel("Distance: " + makeDistance(routeTotalDistance) + " Estimated time: " + makeTime(routeSeconds));
		lblRouteDistance.setForeground(style.routeTextLight());
		lblRouteDistance.setFont(style.normalText());
		GridBagConstraints gbc_lblRouteDistance = new GridBagConstraints();
		gbc_lblRouteDistance.insets = new Insets(0, 0, 5, 0);
		gbc_lblRouteDistance.anchor = GridBagConstraints.WEST;
		gbc_lblRouteDistance.gridx = 2;
		gbc_lblRouteDistance.gridy = 3;
		add(lblRouteDistance, gbc_lblRouteDistance);
	}
	
	private void initRouteSteps(JPanel parent, List<RouteModel> routeModels){
		double totalDist = 0;
		int totalTime = 0;
		int pos = 1;
		parent.add(this);
		for(RouteModel rm : routeModels){
			totalDist += rm.getDistance();
			double meters = rm.getDistance();
			
			if(weightEnum == WeightEnum.DISTANCE_CAR || weightEnum == WeightEnum.SPEED_CAR) {
				double maxCarSpeedInMeterPerSec = ((rm.getMaxSpeed()*1000)/60)/60;
				totalTime += meters/maxCarSpeedInMeterPerSec;
			}
			else if(weightEnum == WeightEnum.DISTANCE_BIKE) {
				double averageBikeSpeedInMeterPerSec = ((26*1000)/60)/60;
				totalTime += meters/averageBikeSpeedInMeterPerSec;
			}
			else if(weightEnum == WeightEnum.DISTANCE_WALK) {
				double averageWalkSpeedInMeterPerSec = ((6*1000)/60)/60;
				totalTime += meters/averageWalkSpeedInMeterPerSec;
			}
			parent.add(new RoutePartStep(pos++, routeImageSplit.getStepIcon(rm.getDirection()), rm));
		}
		routeSeconds = totalTime;
		routeTotalDistance = totalDist;
	}

	private String makeDistance(double input){
		if(input == -1) return "";
		if(input / 1000 > 1.0) return String.format("%.1f", input / 1000) + " km.";
		return String.format("%.0f", input) + " metres.";
	}
	
	private String makeTime(int seconds) {
		String result = "";
		if(seconds > 60) {
			int minutes = seconds/60;
			seconds = seconds%60;		
			if(minutes > 60) {
				int hours = minutes/60;
				minutes = minutes%60;
				result = hours + " hours, " + minutes + " minutes and " + seconds + " seconds.";
			}
			else result = minutes + " minutes and " + seconds + " seconds.";
		}
		else result = seconds + " seconds.";
		return result;
	}
}
