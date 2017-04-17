package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.PostcodeCityBestMatch;

public class PostcodeCityBestMatchTest {
	PostcodeCityBestMatch pc;
	
	@Before
	public void setUp(){
		pc = new PostcodeCityBestMatch();
		pc.add("3400", "Hillerød");
		pc.add("3400", "Hillerød");
		pc.add("3400", "Roskilde");
		pc.add("5000", "Odense V");
		pc.add("5000", "Odense C");
		pc.add("5000", "Odense C");
		pc.add("3400", "Hillerød");
		pc.add("4000", "Roskilde");
		pc.add("2200", "København N");
		pc.add("2200", "København N");
		pc.add("8100", "Aarhus C");
		pc.add("8100", "Aarhus C");
		pc.add("8100", "Aarhus");
		pc.add("8100", "Aarhus");
	}
	
	@After
	public void tearDown() throws Exception {
		pc = null;
	}
	
	@Test
	public void threeRightOneWrongReturnWithMostCounts() {
		assertEquals("Hillerød", pc.getMatch("3400"));
	}
	
	@Test
	public void firstWrongTwoRightReturnWithMostCounts() {
		assertEquals("Odense C", pc.getMatch("5000"));
	}
	@Test
	public void twoOfEqualCountReturnFirstIn() {
		assertEquals("Aarhus C", pc.getMatch("8100"));
	}
	@Test
	public void oneRightReturnTheOnlyOne() {
		assertEquals("Roskilde", pc.getMatch("4000"));
	}
	
	@Test
	public void inputNull() {
		pc.add("2100", null);
		pc.add(null, "København Ø");
		assertEquals(null, pc.getMatch("2100"));
	}
	
	@Test
	public void keyValueNull() {
		pc.add(null, null);
		assertEquals(null, pc.getMatch("2100"));
	}
	
	@Test
	public void inputEmpty() {
		pc.add("2300", "");
		pc.add("", "København S");
		assertEquals(null, pc.getMatch("2300"));
	}
	
	@Test
	public void keyValueEmpty() {
		pc.add("", "");
		assertEquals(null, pc.getMatch(""));
	}

}
