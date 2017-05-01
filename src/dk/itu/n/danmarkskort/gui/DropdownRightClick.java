package dk.itu.n.danmarkskort.gui;

import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.map.PinPoint;
import dk.itu.n.danmarkskort.gui.menu.RoutePage;

public class DropdownRightClick extends CustomDropdown {
	private static final long serialVersionUID = -3776480204582099583L;

	public DropdownRightClick() {
		addItem("Create point of interest here");
		addItem("Create temporary pinpoint here");
		addItem("Route from here");
		addItem("Route to here");
		addItem("Find tweets near this position");
	}
	
	public void onClick(String text) {
		if(text.equals("Create point of interest here")) {
			String pinPointName = JOptionPane.showInputDialog("What should the name of the point of interest be?");
			if(pinPointName == null || pinPointName.length() == 0) return;
			Main.pinPointManager.addPinPoint(pinPointName, new PinPoint(getGeographical(), pinPointName));
		}
		else if(text.equals("Create temporary pinpoint here")) {
			int iconIndex = 5;
			PinPoint systemPinPoint = new PinPoint(getGeographical(), "");
			systemPinPoint.setIconIndex(iconIndex);
			Main.pinPointManager.addSystemPinPoint("", systemPinPoint);
		}
		else if(text.equals("Route from here")) {
			Main.mainPanel.getDropMenu().showDropdown();
			Point2D mousePoint = Util.toRealCoords(Main.map.getGeographicalMousePosition());
			System.out.println(mousePoint.getX() + ", " + mousePoint.getY());
			Main.mainPanel.getDropMenu().addToContentPane(new RoutePage(Main.mainPanel.getDropMenu(), 
					(float)mousePoint.getX() + ", " + (float)mousePoint.getY()));
		}
		else if(text.equals("Route to here")) {
			
		} else if(text.equals("Find tweets near this position")) {
			Point2D mousePoint = Util.toRealCoords(Main.map.getGeographicalMousePosition());
			String url = "https://twitter.com/search?l=&q=near%3A%22" + mousePoint.getY() + "%2C" + mousePoint.getX() + "%20%22%20within%3A15mi&src=typd";
			Util.openWebpage(url);
		}
	}
}
