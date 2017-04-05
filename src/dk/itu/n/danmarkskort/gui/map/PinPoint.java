package dk.itu.n.danmarkskort.gui.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.newmodels.Region;

public class PinPoint implements Serializable {

	private static final long serialVersionUID = -3229202388000112060L;
	private Point2D location;
	private String name;
	private int iconIndex = 0;
	private boolean hover;
	
	public PinPoint(Point2D location, String name) {
		this.location = location;
		this.name = name;
	}
	
	public void setIconIndex(int iconIndex) {
		this.iconIndex = iconIndex;
	}
	
	public int getIconIndex() {
		return iconIndex;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean checkHover() {
		boolean mouseIsOver = mouseIsOver();
		boolean change = hover != mouseIsOver;
		hover = mouseIsOver;
		return change;
	}
	
	private boolean mouseIsOver() {
		Point2D mouse = Main.map.getRelativeMousePosition();
		Point2D screenPos = Main.map.toScreenCoords(location);
		return (mouse.distance(screenPos) < 16);
	}
	
	public void draw(Graphics2D g) {
		Point2D screenPos = Main.map.toScreenCoords(location);
		g.setTransform(new AffineTransform());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(Main.pinPointManager.getIcon(), (int)screenPos.getX() - 12, (int)screenPos.getY() - 24, null);
		if(mouseIsOver()) {
			int width = g.getFontMetrics().stringWidth(name);
			int height = g.getFontMetrics().getHeight();
			int x = (int)screenPos.getX() - width / 2;
			int y = (int)screenPos.getY() + 18;
			
			g.setColor(new Color(237, 198, 255));
			g.fillRect(x - 2, y + 3 - height * 4 - height / 2, width + 4, height);
			g.setColor(Color.BLACK);
			g.drawRect(x - 2, y + 3 - height * 4 - height / 2, width + 4, height);
			
			g.drawString(name, x, y - height * 3 - height / 2);
		}
	}
	
	public boolean isInRegion(Region region) {
		return region.containsPoint(location);
	}
	
}
