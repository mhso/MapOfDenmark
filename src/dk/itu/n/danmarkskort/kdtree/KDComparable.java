package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.models.ParsedNode;

import java.awt.geom.Point2D;

public interface KDComparable {

    Point2D.Float getFirstNode();
    Point2D.Float[] getNodes();
    float[] getCoords();
}
