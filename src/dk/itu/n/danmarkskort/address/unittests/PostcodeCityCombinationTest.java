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
		PostcodeCityCombination.getInstance().add("5000", "Odense C");
		PostcodeCityCombination.getInstance().add("5000", "Copenhagen");
		PostcodeCityCombination.getInstance().add("5000", "Odense V");
		PostcodeCityCombination.getInstance().add("5000", "Odense C");
		
		PostcodeCityCombination.getInstance().add("3400", "Hillerod");
		PostcodeCityCombination.getInstance().add("3400", "Hillerød");
		PostcodeCityCombination.getInstance().add("3400", "Hillerød");
		PostcodeCityCombination.getInstance().add("3400", "Copenhagen");
		PostcodeCityCombination.getInstance().add("3400", "Copenhagen");
		PostcodeCityCombination.getInstance().add("3400", "Hillerød");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		PostcodeCityCombination.getInstance().printCombinationMap();
		PostcodeCityCombination.getInstance().compileBestMatches();
		PostcodeCityCombination.getInstance().sizeBestMatches();
		PostcodeCityCombination.getInstance().printBestMaches();
		PostcodeCityCombination.getInstance().clearCombinations();
		PostcodeCityCombination.getInstance().clearBestMatches();
		fail("Not yet implemented");
		
	}

}
