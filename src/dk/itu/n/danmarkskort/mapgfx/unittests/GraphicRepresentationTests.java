package dk.itu.n.danmarkskort.mapgfx.unittests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecLine;

public class GraphicRepresentationTests {
	private boolean setupDone = false;
	
	@Before
	public void parseGraphicData() {
		if(!setupDone) {
			GraphicRepresentation.parseData("resources/ThemeTest.XML");
			setupDone = true;
		}
	}
	
	@Test
	public void testParsingNotEmpty() {
		assertTrue(GraphicRepresentation.size() > 0);
	}

	@Test
	public void testGraphicSpecificationNotEmpty() {
		assertTrue(GraphicRepresentation.getGraphicSpecs(1).size() > 0);
	}
	
	@Test
	public void testWaytypeNotNull() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(1).get(0).getWayType());
	}
	
	@Test
	public void testGraphicInnerColor() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(1).get(0).getInnerColor());
	}
	
	@Test
	public void testGraphicOuterColor() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(2).get(12).getOuterColor());
	}
	
	@Test
	public void testGraphicLayer() {
		assertTrue(GraphicRepresentation.getGraphicSpecs(1).get(0).getLayer() > 0);
	}
	
	@Test
	public void testGraphicLineNotNull() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(3).get(0));
	}
	
	@Test
	public void testGraphicLineInstance() {
		assertTrue(GraphicRepresentation.getGraphicSpecs(2).get(12) instanceof GraphicSpecLine);
	}
	
	@Test
	public void testGraphicLineWidth() {
		GraphicSpecLine gsl = (GraphicSpecLine) GraphicRepresentation.getGraphicSpecs(2).get(12);
		assertTrue(gsl.getLineWidth() > 0);
	}
	
	@Test
	public void testGraphicLineArr() {
		GraphicSpecLine gsl = (GraphicSpecLine) GraphicRepresentation.getGraphicSpecs(15).get(15);
		assertNotNull(gsl.getDashArr());
	}
	
	@Test
	public void testGraphicAreaNotNull() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(2).get(0));
	}
	
	@Test
	public void testGraphicAreaInstance() {
		assertTrue(GraphicRepresentation.getGraphicSpecs(1).get(1) instanceof GraphicSpecArea);
	}
	
	@Test
	public void testReturnSpecificSpecType() {
		assertTrue(GraphicRepresentation.getSpecificSpec(GraphicRepresentation.getGraphicSpecs(5), 
				GraphicSpecLine.class).get(0) instanceof GraphicSpecLine);
	}
}