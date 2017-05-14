package dk.itu.n.danmarkskort.kdtree;

import java.awt.geom.Point2D;

public interface KDComparable {

    /**
     * @return The first node (coordinate in this context).
     */
    Point2D.Float getFirstNode();

    /**
     * @return All associated nodes (coordinates).
     */
    Point2D.Float[] getNodes();

    /**
     * @return All coordinates in the form af a float-array, where all the even indices represents longitude values
     * and uneven indicies represent latitude values.
     */
    float[] getCoords();
}
