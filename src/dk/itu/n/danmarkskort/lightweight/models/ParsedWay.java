package dk.itu.n.danmarkskort.lightweight.models;

import java.util.ArrayList;

public class ParsedWay extends ParsedItem{

    private long id;
    private float[] coords;

    public ParsedWay(long id) {
        this.id = id;
    }

    public void setCoords(ArrayList<Float> coords) {
        this.coords = new float[coords.size()];
        for(int i = 0; i < coords.size(); i++) {
            this.coords[i] = coords.get(i);
        }
    }
    public float[] getCoords() { return coords; }
    public long getID() { return id; }

    @Override
    public float getFirstLon() { return coords[0]; }
    @Override
    public float getFirstLat() { return coords[1]; }
}
