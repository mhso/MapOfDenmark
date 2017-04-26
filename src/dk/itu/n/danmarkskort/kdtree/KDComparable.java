package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;

public interface KDComparable {

    int compareLon(KDComparable otherItem);
    int compareLat(KDComparable otherItem);
    ParsedNode getFirstNode();
    ParsedNode[] getNodes();
    double shortestDistance(ParsedNode node);
}
