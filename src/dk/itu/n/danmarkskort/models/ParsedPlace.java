package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class ParsedPlace extends Point2D.Float {
	private String name;
	private int population;
	private int nodeId;
	
	public ParsedPlace(float x, float y) {
		setLocation(x, y);
	}
	
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public int getPopulation() {
		return population;
	}
}
