package dk.itu.n.danmarkskort.lightweight.models;

import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private long id;
    private ArrayList<Float> nodes;
    private ArrayList<ParsedWay> ways;
    private ArrayList<ParsedRelation> relations;

    public ParsedRelation(long id) {
        this.id = id;
    }

    public void addNode(float[] f) {
        if(nodes == null) nodes = new ArrayList<>();
        nodes.add(f[0]);
        nodes.add(f[1]);
    }
    public void addWay(ParsedWay way) {
        if(ways == null) ways = new ArrayList<>();
        ways.add(way);
    }
    public void addRelation(ParsedRelation rel) {
        if(relations == null) relations = new ArrayList<>();
        relations.add(rel);
    }

    public long getID() { return id; }
    public ArrayList<ParsedWay> getWays() { return ways; }
    public ArrayList<ParsedRelation> getRelations() { return relations; }
    public ArrayList<Float> getNodes() { return nodes; }

    @Override
    public float getFirstLon() {
        return ways[0].getCoords()[0];
    }

    @Override
    public float getFirstLat() {
        return ways[0].getCoords()[1];
    }
}
