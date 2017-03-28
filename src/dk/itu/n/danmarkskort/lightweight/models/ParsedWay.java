package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.*;
import java.awt.geom.Path2D;
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

    public Path2D getPath() {
        Path2D path = new Path2D.Float();
        path.moveTo(coords[0], coords[1]);
        for(int i = 1; i < coords.length;) path.lineTo(coords[i++], coords[i++]);
        if(coords[0] == coords[coords.length - 2] && coords[1] == coords[coords.length - 1]) path.closePath();

        return path;
    }

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
