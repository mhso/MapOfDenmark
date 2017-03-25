package dk.itu.n.danmarkskort.lightweight.models;

import dk.itu.n.danmarkskort.lightweight.NodeMap;

import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private long id;
    private ParsedWay[] ways;
    private float[] nodes;
    private ParsedRelation[] relations;

    public ParsedRelation(long id) {
        this.id = id;
    }

    public void setWays(ArrayList<ParsedWay> ways) {
        this.ways = new ParsedWay[ways.size()];
        for(int i = 0; i < ways.size(); i++) this.ways[i] = ways.get(i);
    }

    public void setNodes(ArrayList<NodeMap.Node> nodes) {
        this.nodes = new float[nodes.size()];
        for(int i = 0; i < nodes.size();) {
            this.nodes[i] = nodes.get(i++).getLon();
            this.nodes[i] = nodes.get(i++).getLat();
        }
    }

    public void setRelations(ArrayList<ParsedRelation> relations) {
        this.relations = new ParsedRelation[relations.size()];
        for(int i = 0; i < relations.size(); i++) this.relations[i] = relations.get(i);
    }

    public long getID() { return id; }
    public ParsedWay[] getWays() { return ways; }
    public ParsedRelation[] getRelations() { return relations; }
    public float[] getNodes() { return nodes; }

    @Override
    public float getFirstLon() {
        return ways[0].getCoords()[0];
    }

    @Override
    public float getFirstLat() {
        return ways[0].getCoords()[1];
    }
}
