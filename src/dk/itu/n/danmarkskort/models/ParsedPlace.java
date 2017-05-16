package dk.itu.n.danmarkskort.models;

import java.awt.geom.Point2D;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

public class ParsedPlace extends Point2D.Float implements KDComparable {
	private static final long serialVersionUID = 3611768063854888413L;
	
	private String name;
	private int population;
	
	public ParsedPlace(String name, float x, float y) {
		this.name = ReuseStringObj.make(name);
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
