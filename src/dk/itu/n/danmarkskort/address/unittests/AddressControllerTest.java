package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.Address;
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
		pa3.setStreet("Ny Carlsberg Vej");
		pa3.setHousenumber("35");
		pa3.setPostcode("1760");
		pa3.setCity("København V");
		ac.addressParsed(pa3);
		
		// Test size
		assertEquals(3, ac.getAddressSize());
		
		// Add same housenumber again
		ParsedAddress pa4 = new ParsedAddress();
		pa4.setCoords(new Point2D.Float(12.5894306f, 55.6604767f));
		pa4.setStreet("Rued Langgårdsvej");
		pa4.setHousenumber("8");
		pa4.setPostcode("2300");
		pa4.setCity("København S");
		ac.addressParsed(pa4);
		
		// Test that size is the same
		assertEquals(3, ac.getAddressSize());
	}
	
	public void createTempAdresses(){
		ParsedAddress pa1 = new ParsedAddress();
		pa1.setCoords(new Point2D.Float(12.588997f, 55.6598896f));
		pa1.setStreet("Rued Langgårdsvej");
		pa1.setHousenumber("7");
		pa1.setPostcode("2300");
		pa1.setCity("København S");
		ac.addressParsed(pa1);
		
		ParsedAddress pa2 = new ParsedAddress();
		pa2.setCoords(new Point2D.Float(12.5894306f, 55.6604767f));
		pa2.setStreet("Rued Langgårdsvej");
		pa2.setHousenumber("8");
		pa2.setPostcode("2300");
		pa2.setCity("København S");
		ac.addressParsed(pa2);
		
		ParsedAddress pa3 = new ParsedAddress();
		pa3.setCoords(new Point2D.Float(12.5397563f, 55.6661329f));
		pa3.setStreet("Ny Carlsberg Vej");
		pa3.setHousenumber("35");
		pa3.setPostcode("1760");
		pa3.setCity("København V");
		ac.addressParsed(pa3);
		
		ParsedAddress pa4 = new ParsedAddress();
		pa4.setCoords(new Point2D.Float(12.5355534f, 55.7422686f));
		pa4.setStreet("Anemonevej");
		pa4.setHousenumber("5");
		pa4.setPostcode("2820");
		pa4.setCity("Gentofte");
		ac.addressParsed(pa4);
		
		ParsedAddress pa5 = new ParsedAddress();
		pa5.setCoords(new Point2D.Float(4f, 6f));
		pa5.setStreet("Ny Carlsberg Vej");
		pa5.setHousenumber("36 A");
		pa5.setPostcode("1760");
		pa5.setCity("København V");
		ac.addressParsed(pa5);
	}
	
	@Test
	public void testSuggestionStreetStartsWith() {
		createTempAdresses();
		String find = "Rued";
		List<String> list = ac.getSearchSuggestions(find, 10l);
		assertEquals(3, list.size());
		assertEquals("Rued Langgårdsvej", list.get(0));
		assertEquals("Rued Langgårdsvej 7, 2300 København S", list.get(1));
		assertEquals("Rued Langgårdsvej 8, 2300 København S", list.get(2));
	}
	
	@Test
	public void testSuggestionStreetSpellingError() {
		createTempAdresses();
		String find = "Ny Carlsberg Væj";
		List<String> list = ac.getSearchSuggestions(find, 10l);
		assertEquals("Ny Carlsberg Vej", list.get(0));
		assertEquals("Ny Carlsberg Vej 35, 1760 København V", list.get(1));
	}
	
	@Test
	public void testSuggestionStreetFloorSide() {
		createTempAdresses();
		String find = "Ny Carlsberg Vej st. tv";
		List<String> list = ac.getSearchSuggestions(find, 10l);
		assertEquals("Ny Carlsberg Vej", list.get(0));
		assertEquals("Ny Carlsberg Vej 35, 1760 København V", list.get(1));
	}
	
	@Test
	public void testEqualsResult() {
		createTempAdresses();
		String find = "Rued Langgårdsvej 8, 2300 København S";
		Address addr = ac.getSearchResult(find);
		assertEquals("Rued Langgårdsvej 8, 2300 København S", addr.toStringShort());
		
	}
	
	@Test
	public void testEqualsResultWitoutCommaAndPostcode() {
		createTempAdresses();
		String find = "Rued Langgårdsvej 7 2300";
		Address addr = ac.getSearchResult(find);
		assertEquals("Rued Langgårdsvej 7, 2300 København S", addr.toStringShort());
	}
	
	@Test
	public void testEqualsWithSpellingError() {
		createTempAdresses();
		String find = "Rued Langårdsvej 7 2300";
		Address addr = ac.getSearchResult(find);
		assertEquals("Rued Langgårdsvej 7, 2300 København S", addr.toStringShort());
	}
	
	@Test
	public void testEqualsWithFloorSide() {
		createTempAdresses();
		String find = "Rued Langgårdsvej 7 2. sal th. 2300";
		Address addr = ac.getSearchResult(find);
		assertEquals("Rued Langgårdsvej 7, 2300 København S", addr.toStringShort());
	}
	
	@Test
	public void testSuggestionCoord() {
		createTempAdresses();
		ac.onLWParsingFinished();
		List<String> list = ac.getSearchSuggestionsByCoords(new Point2D.Float(55.6598896f, 12.588997f));
		assertEquals("Rued Langgårdsvej 7, 2300 København S", list.get(0));
	}
//	
//	@Test
//	public void testSuggestionStartsWith() {
//		String find = "Rued";
//		ac.getSearchResult(find);
//		ac.getSearchResultByCoords(find);
//		ac.getSearchSuggestionsByCoords(find);
//		ac.getSearchSuggestions(find, limitAmountOfResults);
//	}

}
