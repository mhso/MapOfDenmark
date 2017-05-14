package dk.itu.n.danmarkskort;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Point2D realCoords = new Point2D.Float(55.659632f, 12.591018f);
		System.out.println("realCoords: " + realCoords);
		Point2D toFakeCoords = toFakeCoords(realCoords);
		System.out.println("toFakeCoords: " + toFakeCoords);
		Point2D toRealCoords = toRealCoords(toFakeCoords);
		System.out.println("toRealCoords: " + toRealCoords);
		
		fail("Not yet implemented");
	}
	
	public static Point2D toRealCoords(Point2D fakeCoords) {
		return new Point2D.Float((float)fakeCoords.getX()/0.55f, (float)-fakeCoords.getY());
	}

	public static Point2D toFakeCoords(Point2D realCoords) {
		return new Point2D.Float((float)realCoords.getX()*0.55f, (float)-realCoords.getY());
	}}
