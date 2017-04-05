package dk.itu.n.danmarkskort.gui;

import javax.swing.JOptionPane;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.gui.map.PinPoint;

public class DropdownRightClick extends CustomDropdown {
	private static final long serialVersionUID = -3776480204582099583L;

	public DropdownRightClick() {
		super();
		addItem("Create point of interest here");
	}
	
	public void onClick(String text) {
		if(text.equals("Create point of interest here")) {
			String pinPointName = JOptionPane.showInputDialog("What should the name of the point of interest be?");
			if(pinPointName == null || pinPointName.length() == 0) return;
			Main.pinPointManager.addPinPoint(pinPointName, new PinPoint(getGeographical(), pinPointName));
		}
	}
}
