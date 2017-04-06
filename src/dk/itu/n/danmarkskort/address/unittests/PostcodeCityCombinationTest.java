package dk.itu.n.danmarkskort.address.unittests;

import static org.junit.Assert.*;

import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.AddressHolder;
import dk.itu.n.danmarkskort.address.Postcode;
import dk.itu.n.danmarkskort.address.PostcodeCityCombination;

public class PostcodeCityCombinationTest {

	@Before
	public void setUp() throws Exception {
		PostcodeCityCombination.add("5000", "Odense C");
		PostcodeCityCombination.add("5000", "Copenhagen");
		PostcodeCityCombination.add("5000", "Odense V");
		PostcodeCityCombination.add("5000", "Odense C");
		
		PostcodeCityCombination.add("3400", "Hillerod");
		PostcodeCityCombination.add("3400", "Hillerød");
		PostcodeCityCombination.add("3400", "Hillerød");
		PostcodeCityCombination.add("3400", "Copenhagen");
		PostcodeCityCombination.add("3400", "Copenhagen");
		PostcodeCityCombination.add("3400", "Hillerød");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		PostcodeCityCombination.printCombinationMap();
		PostcodeCityCombination.compileBestMatches();
		PostcodeCityCombination.sizeBestMatches();
		PostcodeCityCombination.printBestMaches();
		PostcodeCityCombination.clearCombinations();
		PostcodeCityCombination.clearBestMatches();
		fail("Not yet implemented");
		
	}

}
