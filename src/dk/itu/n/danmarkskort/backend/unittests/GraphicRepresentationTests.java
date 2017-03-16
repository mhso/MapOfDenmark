package dk.itu.n.danmarkskort.backend.unittests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import dk.itu.n.danmarkskort.backend.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.GraphicLayer;
import dk.itu.n.danmarkskort.models.WaytypeGraphicSpec;

public class GraphicRepresentationTests {
	@Test
	public void testParsingNotEmpty() {
		GraphicRepresentation.main(new String[]{"resources/ThemeBasic.XML"});
		assertFalse(GraphicRepresentation.size() == 0);
	}

	@Test
	public void testGraphicSpecificationNotEmpty() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphics(1);
		assertTrue(graphicList.size() > 0);
	}
	
	@Test
	public void testGraphicLayerColor() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphics(1);
		assertNotNull(graphicList.get(0));
	}
	
	@Test
	public void testGraphicLayerLineWidth() {
		List<WaytypeGraphicSpec> graphicList = GraphicRepresentation.getGraphics(1);
		assertTrue(graphicList.get(0).transformPrimary(graphics));
	}
	
	@Test
	public void testGraphicLayerLineType() {
		WaytypeGraphicSpec gs = GraphicRepresentation.getGraphics("motorway");
		GraphicLayer graphicLayer = gs.getLayer(0);
		assertNotNull(graphicLayer.getStrokeType());
	}
}