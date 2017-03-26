package dk.itu.n.danmarkskort.lightweight.models;

import java.util.ArrayList;

public class ParsedWay extends ParsedItem{

    private long id;
    private float[] coords;

    public ParsedWay() {}

    public ParsedWay(long id) {
        this.id = id;
    }

    public void addNodes(ArrayList<Float> nodes) {
        coords = new float[nodes.size()];
        for(int i = 0; i < nodes.size(); i++) coords[i] = nodes.get(i);
    }

    public float[] getCoords() { return coords; }
    public long getID() { return id; }

    @Override
    public float getFirstLon() {
        if(coords != null && coords.length > 1) return coords[0];
        return -1;
    }
    @Override
    public float getFirstLat() {
        if(coords != null && coords.length > 1) return coords[1];
        return -1;
    }
}
