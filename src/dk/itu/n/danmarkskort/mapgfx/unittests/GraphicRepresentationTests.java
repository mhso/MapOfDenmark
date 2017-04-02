package dk.itu.n.danmarkskort.mapgfx.unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.mapgfx.GraphicRepresentation;
import dk.itu.n.danmarkskort.mapgfx.GraphicSpecArea;
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
		assertNotNull(GraphicRepresentation.getGraphicSpecs(1).get(0).getOuterColor());
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
		assertTrue(GraphicRepresentation.getGraphicSpecs(3).get(0) instanceof GraphicSpecLine);
	}
	
	@Test
	public void testGraphicLineWidth() {
		GraphicSpecLine gsl = (GraphicSpecLine) GraphicRepresentation.getGraphicSpecs(1).get(0);
		assertTrue(gsl.getLineWidth() > 0);
	}
	
	@Test
	public void testGraphicLineArr() {
		GraphicSpecLine gsl = (GraphicSpecLine) GraphicRepresentation.getGraphicSpecs(15).get(0);
		assertNotNull(gsl.getDashArr());
	}
	
	@Test
	public void testGraphicAreaNotNull() {
		assertNotNull(GraphicRepresentation.getGraphicSpecs(1).get(1));
	}
	
	@Test
	public void testGraphicAreaInstance() {
		assertTrue(GraphicRepresentation.getGraphicSpecs(1).get(1) instanceof GraphicSpecArea);
	}
}