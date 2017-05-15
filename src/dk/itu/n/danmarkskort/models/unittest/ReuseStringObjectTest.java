package dk.itu.n.danmarkskort.models.unittest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.models.ReuseStringObj;

public class ReuseStringObjectTest {
	private String str;
	private int keysBefore;
	private int reusedKeysBefore;
	
	@Before
	public void setUp() {
		str = "test123";
		
		keysBefore = ReuseStringObj.keysCreated();
		reusedKeysBefore = ReuseStringObj.keysReused();
		ReuseStringObj.make(str);
	}

	@Test
	public void testStringReuse() {
		assertEquals(str, ReuseStringObj.make(str));
	}

	@Test
	public void testKeyIncrease() {
		assertTrue(keysBefore < ReuseStringObj.keysCreated());
	}
	
	@Test
	public void testReuseKeyIncrease() {
		assertTrue(reusedKeysBefore < ReuseStringObj.keysReused());
	}
}
