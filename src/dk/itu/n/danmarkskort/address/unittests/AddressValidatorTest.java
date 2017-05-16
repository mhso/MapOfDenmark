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
	public void testCleanAddressNormal() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		
		str = AddressValidator.cleanAddress("Kastrupvej 38, 2300 København S");
		System.out.println(str);
		assertEquals("Kastrupvej 38 2300 København S", str);
		
		str = AddressValidator.cleanAddress("Finsensvej 23, 2000 Frederiksberg");
		System.out.println(str);
		assertEquals("Finsensvej 23 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr. Priemes Vej, Frederiksberg C");
		System.out.println(str);
		assertEquals("Dr. Priemes Vej Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3, Vanløse");
		System.out.println(str);
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress("A.F. Beyers Vej 5, Vanløse");
		System.out.println(str);
		assertEquals("A. F. Beyers Vej 5 Vanløse", str);
		
		str = AddressValidator.cleanAddress("ny Carlsberg Vej 6,  Frederiksberg");
		System.out.println(str);
		assertEquals("Ny Carlsberg Vej 6 Frederiksberg", str);
	}
	
	@Test
	public void testCleanAddressXtraSpacesTabsDots() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		
		str = AddressValidator.cleanAddress("  Kastrupvej 38  , 2300 København S");
		System.out.println(str);
		assertEquals("Kastrupvej 38 2300 København S", str);
		
		str = AddressValidator.cleanAddress("		Finsensvej 23, 2000 Frederiksberg");
		System.out.println(str);
		assertEquals("Finsensvej 23 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr.. Priemes Vej, Frederiksberg	 C");
		System.out.println(str);
		assertEquals("Dr. Priemes Vej Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3,	 Vanløse");
		System.out.println(str);
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress(" A.F. Beyers	... Vej 5, Vanløse");
		System.out.println(str);
		assertEquals("A. F. Beyers Vej 5 Vanløse", str);
	}
	
	@Test
	public void testCleanAddressFloorSide() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		
		str = AddressValidator.cleanAddress("Kastrupvej 38 1. sal, 2300 København S");
		assertEquals("Kastrupvej 38 2300 København S", str);
		
		str = AddressValidator.cleanAddress("Finsensvej 23 st. th 2000 Frederiksberg");
		assertEquals("Finsensvej 23 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr. Priemes Vej stuen, Frederiksberg C");
		assertEquals("Dr. Priemes Vej Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3 2 th, Vanløse");
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress("A.F. Beyers Vej 5 4 sal mf, Vanløse");
		assertEquals("A. F. Beyers Vej 5 Vanløse", str);
		
		str = AddressValidator.cleanAddress("ny Carlsberg Vej 6, kl. midt Frederiksberg");
		assertEquals("Ny Carlsberg Vej 6 Frederiksberg", str);
	}
	
	@Test
	public void testCleanAddressHousenumberTypes() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		
		str = AddressValidator.cleanAddress("Kastrupvej 38B 1. sal, 2300 København S");
		assertEquals("Kastrupvej 38b 2300 København S", str);
		
		str = AddressValidator.cleanAddress("Finsensvej 23-24 st. th 2000 Frederiksberg");
		assertEquals("Finsensvej 23-24 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr. Priemes Vej 10A - 14B stuen, Frederiksberg C");
		assertEquals("Dr. Priemes Vej 10a-14b Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3 2 th, Vanløse");
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress("A.F. Beyers Vej 5 4 sal mf, Vanløse");
		assertEquals("A. F. Beyers Vej 5 Vanløse", str);
		
		str = AddressValidator.cleanAddress("ny Carlsberg Vej 6, kl. midt Frederiksberg");
		assertEquals("Ny Carlsberg Vej 6 Frederiksberg", str);
	}
	
	@Test
	public void testFindPostcode() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		str = AddressValidator.findPostcode("Kastrupvej 38-40 1. sal, 2300 København S");
		assertEquals("2300", str);
		
	}
	
	@Test
	public void testIsCityname() {
		assertTrue(AddressValidator.isCityname("København S"));
		assertTrue(AddressValidator.isCityname("københavn s"));
		assertTrue(AddressValidator.isCityname("Øster Doense"));
		
		assertFalse(AddressValidator.isCityname("4 København S"));
		assertFalse(AddressValidator.isCityname("Kastrupvej 38"));
		assertTrue(AddressValidator.isCityname("Faxe Ladeplads"));
	}
	
	@Test
	public void testPrepStreetname() {
		String str = null;
		str = AddressValidator.prepStreetname(" kastrupvej..");
		System.out.println(str);
		assertEquals("Kastrupvej", str);
		
		str = AddressValidator.prepStreetname(" kastrupvej..");
		System.out.println(str);
		assertEquals("Kastrupvej", str);
	}
}
