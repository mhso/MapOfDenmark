package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.AddressValidator;

public class AddressValidatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void isStreetStandardReturnTrue() {
		assertEquals(true, AddressValidator.isStreetname("Håndværkervænget"));
	}
	
	@Test
	public void isStreetStartWithNumberReturnTrue() {
		assertEquals(true, AddressValidator.isStreetname("10. Håndværkervænget"));
	}
	
	@Test
	public void isStreetContainsNumberReturnTrue() {
		assertEquals(true, AddressValidator.isStreetname("Håndværker 10 vænget"));
	}
	
	@Test
	public void isStreetEndsWithNumberReturnTrue() {
		assertEquals(true, AddressValidator.isStreetname("Håndværkervænget 10"));
	}
}
