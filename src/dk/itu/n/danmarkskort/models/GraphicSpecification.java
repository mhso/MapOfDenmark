package dk.itu.n.danmarkskort.models;

import java.util.ArrayList;
import java.util.List;

public class GraphicSpecification {
	private List<GraphicLayer> layers = new ArrayList<>();
	private Object mapElement;
	
	/**
	 * Create a GraphicSpecification object, used for storing information about the visual representation of a map element (a WayType f.x.).
	 * 
	 * @param mapElement The map element (a WayType, Node) for which the graphical representation should be stored.
	 */
	public GraphicSpecification(Object mapElement) {
		this.mapElement = mapElement;
	}
	
	/**
	 * Add a GraphicLayer to this GraphicSpecification. The order in which layers are added reflect the order, they eventually are drawn in.
	 * 
	 * @param graphicLayer The GraphicLayer to add.
	 */
	public void addLayer(GraphicLayer graphicLayer) {
		layers.add(graphicLayer);
	}
	
	/**
	 * Iterate over the layers in this GraphicSpecification.
	 * 
	 * @return An iterator over the graphical layers.
	 */
	public Iterable<GraphicLayer> getLayers() {
		return layers;
	}
	
	public String toString() {
		String returnString = "Graphic Specification [";
		if(layers != null && !layers.isEmpty()) {
			returnString += "Layers: " + layers.size() + ", ";
			for(GraphicLayer gl : layers) {
				returnString += gl + ", ";
			}
		}
		returnString += "]";
		return returnString;
	}
}
