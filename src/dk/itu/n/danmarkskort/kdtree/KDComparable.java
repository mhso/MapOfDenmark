package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;

public interface KDComparable {

    public int compareLon(KDComparable otherItem);
    public int compareLat(KDComparable otherItem);
    public ParsedNode getFirstNode();
    public ParsedNode[] getNodes();
}
