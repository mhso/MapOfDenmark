package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.Address;
import dk.itu.n.danmarkskort.address.AddressParser;

public class AddressParserTest {
	AddressParser ap;
	@Before
	public void setUp() throws Exception {
		AddressParser ap = new AddressParser();
	}

	@After
	public void tearDown() throws Exception {
		ap = null;
	}
	
	@Test
	public void testAddrCase1_EvilA() {
		Address addr = AddressParser.parse(" Rued     Langgaards Vej 7. 2300	 København S. ", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
	}
	
	@Test
	public void testAddrCase1_EvilB() {
		Address addr = AddressParser.parse(" Rued     Langgaards Vej #7./ 2300	 København S. ", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
	}

	@Test
	public void testAddrCase1_A() {
		Address addr = AddressParser.parse("Rued Langgaards Vej 7, 2300 København S", false);
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
	}
	
	@Test
	public void testAddrCase1_B() {
		Address addr = AddressParser.parse("rued langgaards vej 7, 2300 københavn s", false);	
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
	}
	
	@Test
	public void testAddrCase1_C() {
		Address addr = AddressParser.parse("rued langgaards vej 7 2300 københavn s", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
	}
	
	@Test
	public void testAddrCase1_D() {
		Address addr = AddressParser.parse("rued langgaards vej 7, 2300", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertNull(addr.getCity());
	}
	
	@Test
	public void testAddrCase1_E() {
		Address addr = AddressParser.parse("rued langgaards vej 7, københavn s", false);
		//Not used in parsing);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getPostcode());
	}
	
	@Test
	public void testAddrCase1_F() {
		Address addr = AddressParser.parse("rued langgaards vej, københavn s", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getHousenumber());
		assertNull(addr.getPostcode());
		
	}
	
	@Test
	public void testAddrCase1_G() {
		Address addr = AddressParser.parse("rued langgaards vej 2300", false);
		//Not used in parsing
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertNull(addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertNull(addr.getCity());
	}
}
