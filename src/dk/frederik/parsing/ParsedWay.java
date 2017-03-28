package dk.frederik.parsing;

import java.util.ArrayList;

public class ParsedWay {
	long id;
	float[] coords;
	
	public ParsedWay(long id) {
		this.id = id;
	}
	
	public void addCoords(ArrayList<Float> coords) {
		this.coords = new float[coords.size()];
		for(int i=0; i<coords.size(); i++) this.coords[i] = coords.get(i);
	}
	
}
