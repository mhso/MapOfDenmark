package dk.itu.n.danmarkskort.backend.unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;
import dk.itu.n.danmarkskort.mapgfx.WaytypeGraphicSpec;

public class GraphicRepresentationTests {
	private boolean setupDone = false;
	
	@Before
	public void parseGraphicData() {
		if(!setupDone) {
			GraphicRepresentation.main(new String[]{"resources/ThemeTest.XML"});
			setupDone = true;
		}
	}
	
	@Test
	public void testParsingNotEmpty() {
		assertTrue(GraphicRepresentation.size() > 0);
	}

	@Test
	public void testGraphicSpecificationNotEmpty() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphicSpecs(0);
		assertTrue(graphicList.size() > 0);
	}
	
	@Test
	public void testGraphicInnerColor() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphicSpecs(0);
		assertNotNull(graphicList.get(0).getInnerColor());
	}
	
	@Test
	public void testGraphicOuterColor() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphicSpecs(0);
		assertNotNull(graphicList.get(0).getOuterColor());
	}
	
	@Test
	public void testGraphicLineWidth() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphicSpecs(0);
		GraphicSpecLine gsl = (GraphicSpecLine) graphicList.get(0);
		assertTrue(gsl.getLineWidth() > 0);
	}
	
	@Test
	public void testGraphicLineArr() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphicSpecs(14);
		GraphicSpecLine gsl = (GraphicSpecLine) graphicList.get(0);
		assertNotNull(gsl.getDashArr());
	}
}