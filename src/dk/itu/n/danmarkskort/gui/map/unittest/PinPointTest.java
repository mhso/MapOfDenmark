package dk.itu.n.danmarkskort.gui.map.unittest;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.junit.Test;

import dk.itu.n.danmarkskort.gui.map.PinPoint;
import dk.itu.n.danmarkskort.models.Region;

public class PinPointTest {
	
	@Test
	public void testIsInRegion() {
		PinPoint pinPoint = new PinPoint(new Point2D.Double(1, 1), "Name");
		Region itIsInThisRegion = new Region(0, 0, 2, 2);
		Region itIsNotThisRegion = new Region(3, 3, 5, 5);
		assertEquals(true, pinPoint.isInRegion(itIsInThisRegion));
		assertEquals(false, pinPoint.isInRegion(itIsNotThisRegion));
	}
	
}
