package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.AddressController;
import dk.itu.n.danmarkskort.models.ParsedAddress;

public class AddressControllerTest {
	AddressController ac;
	@Before
	public void setUp() throws Exception {
		ac = new AddressController();
	}

	@After
	public void tearDown() throws Exception {
			ac = null;
	}

	@Test
	public void testAddressSize() {
		//Test for empty
		assertEquals(0, ac.getAddressSize());
		
		//Add 3 uniq addresses
		ParsedAddress pa1 = new ParsedAddress();
		pa1.setCoords(new Point2D.Float(1f, 2f));
		pa1.setStreet("Rued Langgårdsvej");
		pa1.setHousenumber("7");
		pa1.setPostcode("2300");
		pa1.setCity("København S");
		ac.addressParsed(pa1);
		
		ParsedAddress pa2 = new ParsedAddress();
		pa2.setCoords(new Point2D.Float(2f, 2f));
		pa2.setStreet("Rued Langgårdsvej");
		pa2.setHousenumber("8");
		pa2.setPostcode("2300");
		pa2.setCity("København S");
		ac.addressParsed(pa2);
		
		ParsedAddress pa3 = new ParsedAddress();
		pa3.setCoords(new Point2D.Float(3f, 2f));
		pa3.setStreet("Amagerbrogade");
		pa3.setHousenumber("18");
		pa3.setPostcode("2300");
		pa3.setCity("København S");
		ac.addressParsed(pa3);
		
		// Test size
		assertEquals(3, ac.getAddressSize());
		
		// Add same housenumber again
		ParsedAddress pa4 = new ParsedAddress();
		pa4.setCoords(new Point2D.Float(3f, 2f));
		pa4.setStreet("Amagerbrogade");
		pa4.setHousenumber("18");
		pa4.setPostcode("2300");
		pa4.setCity("København S");
		ac.addressParsed(pa4);
		
		// Test that size is the same
		assertEquals(3, ac.getAddressSize());
	}
	
	@Test
	public void test() {
		String find = "Rued";
		ac.getSearchResult(find);
		ac.getSearchResultByCoords(find);
		ac.getSearchSuggestions(find);
		ac.getSearchSuggestions(find, limitAmountOfResults);
		
	}

}
