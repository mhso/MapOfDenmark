package dk.itu.n.danmarkskort.lightweight.models;

import dk.itu.n.danmarkskort.Main;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private long id;
    private float[] coords; // not sure if these are needed for a relation
    private ParsedWay[] ways;
    private ParsedRelation[] relations;

    public ParsedRelation(long id) { this.id = id; }

    public void addNodes(ArrayList<Float> nodes) {
        coords = new float[nodes.size()];
        for(int i = 0; i < nodes.size(); i++) coords[i] = nodes.get(i);
    }

    public void addWays(ArrayList<ParsedWay> ways) {
        this.ways = new ParsedWay[ways.size()];
        for(int i = 0; i < ways.size(); i++) this.ways[i] = ways.get(i);
    }

    public void addRelations(ArrayList<ParsedRelation> relations) {
        this.relations = new ParsedRelation[relations.size()];
        for(int i = 0; i < relations.size(); i++) this.relations[i] = relations.get(i);
    }

    public long getID() { return id; }
    public float[] getCoords() { return coords; }
    public ParsedWay[] getWays() { return ways; }
    public ParsedRelation[] getRelations() { return relations; }

    public ArrayList<Float> getLons() {
        ArrayList<Float> lons = new ArrayList<>();
        Main.log(id);
        for(int i = 0; i < coords.length; i = i+2) lons.add(coords[i]);
        for(ParsedWay way : ways) lons.addAll(way.getLons());
        for(ParsedRelation rel : relations) lons.addAll(rel.getLons());
        return lons;
    }

    public ArrayList<Float> getLats() {
        ArrayList<Float> lats = new ArrayList<>();
        for(int i = 1; i < coords.length; i = i+2) lats.add(coords[i]);
        for(ParsedWay way : ways) lats.addAll(way.getLons());
        for(ParsedRelation rel : relations) lats.addAll(rel.getLons());
        return lats;
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        for(ParsedItem item : ways) path.append(item.getPath(), false);
        for(ParsedItem item : relations) path.append(item.getPath(), false);
        return path;
    }

    public float getFirstLon() {
        if(coords != null && coords.length > 1) return coords[0];
        else if (ways != null) return ways[0].getFirstLon();
        else if(relations != null) return relations[0].getFirstLon();
        return -1;
    }

    public float getFirstLat() {
        if(coords != null && coords.length > 1) return coords[1];
        else if (ways != null) return ways[0].getFirstLat();
        else if(relations != null) return relations[0].getFirstLat();
        return -1;
    }
}
