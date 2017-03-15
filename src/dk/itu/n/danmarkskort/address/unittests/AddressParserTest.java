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
		Address addr = ap.parse(" Rued     Langgaards Vej 7. 2300	 København S. ");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
	
	@Test
	public void testAddrCase1_EvilB() {
		Address addr = ap.parse(" Rued     Langgaards Vej #7./ 2300	 København S. ");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}

	@Test
	public void testAddrCase1_A() {
		Address addr = ap.parse("Rued Langgaards Vej 7, 2300 København S");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("Rued Langgaards Vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("København S", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
	
	@Test
	public void testAddrCase1_B() {
		Address addr = ap.parse("rued langgaards vej 7, 2300 københavn s");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
	
	@Test
	public void testAddrCase1_C() {
		Address addr = ap.parse("rued langgaards vej 7 2300 københavn s");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
	
	@Test
	public void testAddrCase1_D() {
		Address addr = ap.parse("rued langgaards vej 7, 2300");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertNull(addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
	
	@Test
	public void testAddrCase1_E() {
		Address addr = ap.parse("rued langgaards vej 7, københavn s");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertEquals("7", addr.getHousenumber());
		assertNull(addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
	}
	
	@Test
	public void testAddrCase1_F() {
		Address addr = ap.parse("rued langgaards vej, københavn s");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertNull(addr.getHousenumber());
		assertNull(addr.getPostcode());
		assertEquals("københavn s", addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
	}
	
	@Test
	public void testAddrCase1_G() {
		Address addr = ap.parse("rued langgaards vej 2300");
		//Not used in parsing
		assertEquals(-1l, addr.getNodeId());
		assertEquals(-1d, addr.getLat(), 0.0);
		assertEquals(-1d, addr.getLon(), 0.0);
		
		//Might be parsed
		assertEquals("rued langgaards vej", addr.getStreet());
		assertNull(addr.getHousenumber());
		assertEquals("2300", addr.getPostcode());
		assertNull(addr.getCity());
		assertNull(addr.getCountry());
		assertNull(addr.getFloor());
		assertNull(addr.getDoorSide());
		assertNull(addr.getHousename());
		assertNull(addr.getNodeId());
	}
}
