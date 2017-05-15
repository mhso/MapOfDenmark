package dk.itu.n.danmarkskort.unittest;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.n.danmarkskort.TimerUtil;

public class TimerUtilTest {
	@Test
	public void testMakeTime() {
		TimerUtil timer = new TimerUtil();
		assertEquals(timer.getSimpleTimeString(3700), "1 Hour, 1 Minute and 40 Seconds");
		assertEquals(timer.getSimpleTimeString(87800), "1 Day, 23 Minutes and 20 Seconds");
		assertEquals(timer.getSimpleTimeString(24), "24 Seconds");
	}
}
