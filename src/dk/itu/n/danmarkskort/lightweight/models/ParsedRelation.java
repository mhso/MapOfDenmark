package dk.itu.n.danmarkskort.lightweight.models;

import java.util.ArrayList;

public class ParsedRelation extends ParsedItem {

    private long id;
    private ParsedWay[] ways;

    public ParsedRelation(long id) {
        this.id = id;
    }

    public void addWays(ArrayList<ParsedWay> ways) {
        this.ways = new ParsedWay[ways.size()];
        for(int i = 0; i < ways.size(); i++) this.ways[i] = ways.get(i);
    }

    public long getID() { return id; }
}
