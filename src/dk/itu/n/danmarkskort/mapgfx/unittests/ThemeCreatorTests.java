package dk.itu.n.danmarkskort.mapgfx.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.n.danmarkskort.mapgfx.ThemeCreatorMain;

public class ThemeCreatorTests {
	@Test
	public void testThemesFound() {
		ThemeCreatorMain main = new ThemeCreatorMain();
		assertTrue(main.getThemeCount() > 0);
	}

}
