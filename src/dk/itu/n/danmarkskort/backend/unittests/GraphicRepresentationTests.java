package dk.itu.n.danmarkskort.backend.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.n.danmarkskort.backend.GraphicRepresentation;
import dk.itu.n.danmarkskort.models.GraphicLayer;
import dk.itu.n.danmarkskort.models.GraphicSpecification;

public class GraphicRepresentationTests {
	@Test
	public void testParsingNotEmpty() {
		GraphicRepresentation.main(new String[]{"resources/KortGrafikIde.XML"});
		assertFalse(GraphicRepresentation.size() == 0);
	}

	@Test
	public void testGraphicSpecificationNotEmpty() {
		GraphicSpecification gs = GraphicRepresentation.getGraphics("footway");
		int layers = 0;
		for(GraphicLayer gl : gs.getLayers()) {
			layers++;
		}
		assertTrue(layers > 0);
	}
	
	@Test
	public void testGraphicLayerColor() {
		GraphicSpecification gs = GraphicRepresentation.getGraphics("footway");
		GraphicLayer graphicLayer = gs.getLayer(0);
		assertNotNull(graphicLayer.getColor());
	}
	
	@Test
	public void testGraphicLayerLineWidth() {
		GraphicSpecification gs = GraphicRepresentation.getGraphics("footway");
		GraphicLayer graphicLayer = gs.getLayer(0);
		assertTrue(graphicLayer.getStrokeWidth() > 0);
	}
	
	@Test
	public void testGraphicLayerLineType() {
		GraphicSpecification gs = GraphicRepresentation.getGraphics("footway");
		GraphicLayer graphicLayer = gs.getLayer(0);
		assertNotNull(graphicLayer.getStrokeType());
	}
}
