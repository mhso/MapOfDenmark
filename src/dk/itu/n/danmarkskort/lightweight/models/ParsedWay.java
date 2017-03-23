package dk.itu.n.danmarkskort.lightweight.models;

import java.util.ArrayList;

public class ParsedWay{

    private long id;
    private float[] coords;

    public ParsedWay(long id) {
        this.id = id;
    }

    public void nodesToCoords(ArrayList<Float> coords) {
        this.coords = new float[coords.size()];
        for(int i = 0; i < coords.size(); i++) {
            this.coords[i] = coords.get(i);
        }
    }

    public long getID() { return id; }
    public float[] getCoords() { return coords; }
}
