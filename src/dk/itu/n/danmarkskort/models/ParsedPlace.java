package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

public class ParsedPlace extends Point2D.Float implements KDComparable {
	public static final int TOWN = 0;
	public static final int SUBURB = 1;
	
	private String name;
	private int population;
	
	public ParsedPlace(String name, int type, float x, float y) {
		this.name = name;
		setLocation(x, y);
	}
	
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Float getFirstNode() {
		return this;
	}

	@Override
	public Float[] getNodes() {
		return new Point2D.Float[]{this};
	}

	@Override
	public float[] getCoords() {
		return new float[]{x, y};
	}
}
