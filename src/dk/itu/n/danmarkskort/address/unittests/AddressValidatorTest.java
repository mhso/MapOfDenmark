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
		assertEquals("Kastrupvej 38 2300 København S", str);
		
		str = AddressValidator.cleanAddress("Finsensvej 23, 2000 Frederiksberg");
		assertEquals("Finsensvej 23 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr. Priemes Vej, Frederiksberg C");
		assertEquals("Dr. Priemes Vej Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3, Vanløse");
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress("A.F. Beyers Vej 5, Vanløse");
		assertEquals("A. F. Beyers Vej 5 Vanløse", str);
		
		str = AddressValidator.cleanAddress("ny Carlsberg Vej 6,  Frederiksberg");
		assertEquals("Ny Carlsberg Vej 6 Frederiksberg", str);
	}
	
	@Test
	public void testCleanAddressXtraSpacesTabsDots() {
		assertEquals("", AddressValidator.cleanAddress(""));
		
		String str = null;
		
		str = AddressValidator.cleanAddress("  Kastrupvej 38  , 2300 København S");
		assertEquals("Kastrupvej 38 2300 København S", str);
		
		str = AddressValidator.cleanAddress("		Finsensvej 23, 2000 Frederiksberg");
		assertEquals("Finsensvej 23 2000 Frederiksberg", str);
		
		str = AddressValidator.cleanAddress("Dr.. Priemes Vej, Frederiksberg	 C");
		assertEquals("Dr. Priemes Vej Frederiksberg C", str);
		
		str = AddressValidator.cleanAddress("A. F. Beyers Vej 3,	 Vanløse");
		assertEquals("A. F. Beyers Vej 3 Vanløse", str);
		
		str = AddressValidator.cleanAddress(" A.F. Beyers	... Vej 5, Vanløse");
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
		str = AddressValidator.extractPostcode("Kastrupvej 38-40 1. sal, 2300 København S");
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
		assertEquals("Kastrupvej", str);
		
		str = AddressValidator.prepStreetname(" kastrupvej..");
		assertEquals("Kastrupvej", str);
		
		str = AddressValidator.prepStreetname("10. Rosenvænge Tværvej ");
		assertEquals("10. Rosenvænge Tværvej", str);
		
		str = AddressValidator.prepStreetname("10. vej 9 ");
		assertEquals("10. Vej 9", str); // Sometime used as temp. roadnames, when no names is given.
	}
	
	@Test
	public void testPrepHousenumber() {
		String str = null;
		assertEquals("0", AddressValidator.prepHousenumber("0"));
		assertEquals("1", AddressValidator.prepHousenumber("1"));
		assertEquals("2", AddressValidator.prepHousenumber("2"));
		assertEquals("3", AddressValidator.prepHousenumber("3"));
		assertEquals("4", AddressValidator.prepHousenumber("4"));
		assertEquals("5", AddressValidator.prepHousenumber("5"));
		assertEquals("6", AddressValidator.prepHousenumber("6"));
		assertEquals("7", AddressValidator.prepHousenumber("7"));
		assertEquals("8", AddressValidator.prepHousenumber("8"));
		assertEquals("9", AddressValidator.prepHousenumber("9"));
		
		
		assertEquals("10", AddressValidator.prepHousenumber("10"));
		assertEquals("10", AddressValidator.prepHousenumber(" 10 "));
		assertEquals("10-12", AddressValidator.prepHousenumber("10-12"));
		assertEquals("10-12", AddressValidator.prepHousenumber(" 10 - 12"));
		assertEquals("10-12B", AddressValidator.prepHousenumber("10- 12B"));
		assertEquals("10A-E", AddressValidator.prepHousenumber("10A-E"));
		assertEquals("10A-E", AddressValidator.prepHousenumber("10 A-E"));
		assertEquals("10A-E", AddressValidator.prepHousenumber("10AE"));
		
		assertEquals("312", AddressValidator.prepHousenumber("312"));
		assertEquals("312", AddressValidator.prepHousenumber(" 312 "));
		assertEquals("312-314", AddressValidator.prepHousenumber("312-314"));
		assertEquals("312-314", AddressValidator.prepHousenumber(" 312 - 314"));
		assertEquals("312-314B", AddressValidator.prepHousenumber("312- 314B"));
		assertEquals("312A-E", AddressValidator.prepHousenumber("312A-E"));
		assertEquals("312A-E", AddressValidator.prepHousenumber("312 A-E"));
		assertEquals("312A-E", AddressValidator.prepHousenumber("312AE"));
		
		assertEquals("9", AddressValidator.prepHousenumber("9 1. sal tv"));
		assertEquals("99", AddressValidator.prepHousenumber("99 1. sal tv"));
		assertEquals("999", AddressValidator.prepHousenumber("999 1. sal tv"));
	}
	
	@Test
	public void testPrepPostcode() {
		assertEquals("4000", AddressValidator.prepPostcode("4000"));
		assertEquals("4000", AddressValidator.prepPostcode("DK-4000"));
		assertEquals("4000", AddressValidator.prepPostcode("4000 Roskilde"));
	}
	
	@Test
	public void testPrepCityname() {
		assertEquals("Roskilde", AddressValidator.prepCityname("Roskilde"));
		assertEquals("København S", AddressValidator.prepCityname("københavn 	 s"));
		assertEquals("København Nv", AddressValidator.prepCityname("  københavn NV"));
	}
}
