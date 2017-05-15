package dk.itu.n.danmarkskort.models.unittest;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.junit.Test;

import dk.itu.n.danmarkskort.models.Region;

public class RegionTest {

	@Test
	public void testGetMiddlePoint() {
		Region region = new Region(1, 1, 3, 3);
		Point2D middlePoint = region.getMiddlePoint();
		assertEquals(2, middlePoint.getX(), 0);
		assertEquals(2, middlePoint.getY(), 0);
	}
	
	@Test
	public void testOverlaps() {
		Region region = new Region(0, 0, 5, 5);
		Region region2 = new Region(4, 4, 9, 9);
		Region region3 = new Region(100, 100, 105, 105);
		assertEquals(true, region.overlapsRegion(region2));
		assertEquals(true, region2.overlapsRegion(region));
		assertEquals(false, region.overlapsRegion(region3));
		assertEquals(false, region2.overlapsRegion(region3));
		assertEquals(false, region3.overlapsRegion(region));
	}
	
	@Test
	public void containsPoint() {
		Point2D pointInside = new Point2D.Double(5, 5);
		Point2D pointOutside = new Point2D.Double(100, 100);
		Region region = new Region(0, 0, 10, 10);
		assertEquals(true, region.containsPoint(pointInside));
		assertEquals(false, region.containsPoint(pointOutside));
	}
	
	@Test
	public void testContainsPoint() {
		
	}
	
}
