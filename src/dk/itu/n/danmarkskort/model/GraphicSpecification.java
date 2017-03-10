package dk.itu.n.danmarkskort.model;

import java.util.ArrayList;
import java.util.List;

public class GraphicSpecification {
	private List<GraphicLayer> layers = new ArrayList<>();
	private Object mapElement;
	
	public GraphicSpecification(Object mapElement) {
		this.mapElement = mapElement;
	}
	
	public void addLayer(GraphicLayer graphicLayer) {
		layers.add(graphicLayer);
	}
	
	public Iterable<GraphicLayer> getLayers() {
		return layers;
	}
}
