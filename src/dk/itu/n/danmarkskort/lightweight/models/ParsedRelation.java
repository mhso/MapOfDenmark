package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private long id;
    private float[] coords;
    private ParsedItem[] items;

    public ParsedRelation(long id) { this.id = id; }

    public void addNodes(ArrayList<Float> nodes) {
        coords = new float[nodes.size()];
        for(int i = 0; i < nodes.size(); i++) coords[i] = nodes.get(i);
    }

    public void addItems(ArrayList<ParsedItem> items) {
        this.items = new ParsedItem[items.size()];
        for(int i = 0; i < items.size(); i++) this.items[i] = items.get(i);
    }

    public long getID() { return id; }
    public float[] getCoords() { return coords; }

    public ArrayList<Float> getLons() {
        ArrayList<Float> lons = new ArrayList<>();
        if(coords != null) {
            for(int i = 0; i < coords.length; i = i+2) {
                lons.add(coords[i]);
            }
        }
        if(items != null) {
            for(ParsedItem item : items) {
                lons.addAll(item.getLons());
            }
        }
        return lons;
    }

    public ArrayList<Float> getLats() {
        ArrayList<Float> lats = new ArrayList<>();
        if(coords != null) {
            for(int i = 1; i < coords.length; i = i+2) {
                lats.add(coords[i]);
            }
        }
        if(items != null) {
            for(ParsedItem item : items) {
                lats.addAll(item.getLats());
            }
        }
        return lats;
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(items != null) {
            for(ParsedItem item : items) {
                path.append(item.getPath(), false);
            }
        }
        return path;
    }

    public float getFirstLon() {
        if(coords != null && coords.length > 1) return coords[0];
        else if(items != null) return items[0].getFirstLon();
        return -1;
    }

    public float getFirstLat() {
        if(coords != null && coords.length > 1) return coords[1];
        else if(items != null) return items[0].getFirstLat();
        return -1;
    }
}
