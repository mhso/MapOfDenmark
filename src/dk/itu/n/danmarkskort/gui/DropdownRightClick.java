package dk.itu.n.danmarkskort.gui;

import javax.swing.JMenuItem;

public class DropdownRightClick extends CustomDropdown {
	private static final long serialVersionUID = -3776480204582099583L;

	public DropdownRightClick() {
		super();
		
		JMenuItem itemPlaceWaypoint = new JMenuItem("Place Waypoint");
		add(itemPlaceWaypoint);
		
		JMenuItem itemRouteTo = new JMenuItem("Route To Point");
		add(itemRouteTo);
		
		JMenuItem itemRouteFrom = new JMenuItem("Route From Point");
		add(itemRouteFrom);
	}
}
