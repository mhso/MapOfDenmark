package dk.itu.n.danmarkskort.search.unittest;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.n.danmarkskort.search.SearchController;

public class SearchControllerTest {

	@Test
	public void testIsCoordinates() {
		assertEquals(true, SearchController.isCoordinates("55.676097, 12.568337"));
		assertEquals(false, SearchController.isCoordinates("this is not coordinates"));
	}
	
}
