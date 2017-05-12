package dk.itu.n.danmarkskort.gui;

import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.gui.components.CustomDropdown;
import dk.itu.n.danmarkskort.gui.map.PinPoint;

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
			Point2D mousePoint = Util.toRealCoords(getGeographical());
			Main.mainPanel.getDropMenu().openRoutePage((float)mousePoint.getY() + ", " + (float)mousePoint.getX(), null, true);
		}
		else if(text.equals("Route to here")) {
			Main.mainPanel.getDropMenu().showDropdown();
			Point2D mousePoint = Util.toRealCoords(getGeographical());
			Main.mainPanel.getDropMenu().openRoutePage(null, (float)mousePoint.getY() + ", " + (float)mousePoint.getX(), true);
		} else if(text.equals("Find tweets near this position")) {
			Point2D mousePoint = Util.toRealCoords(getGeographical());
			String url = "https://twitter.com/search?l=&q=near%3A%22" + mousePoint.getY() + "%2C" + mousePoint.getX() + "%20%22%20within%3A1mi&src=typd";
			Util.openWebpage(url);
		}
	}
}
