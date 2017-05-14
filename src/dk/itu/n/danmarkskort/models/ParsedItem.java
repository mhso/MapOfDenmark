package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

import java.awt.*;
import java.io.Serializable;

public abstract class ParsedItem implements KDComparable, Serializable {

	private static final long serialVersionUID = -8684498422989385773L;

    /**
     * Creates a shape out of associated coordinate information, and returns it.
     * @return
     */
    public abstract Shape getShape();

    /**
     * Deletes all references to ParsedNodes
     */
    public abstract void deleteOldRefs();

    /**
     * Converts ParsedNodes' longitude and latitude information into float array, where the even indices are
     * longitude values, and the even indices are latitude values.
     */
    public abstract void nodesToCoords();

    /**
     * Gives the size of data held by a ParsedWay, og by all the ParsedWays associated with an ParsedRelation.
     *
     * After ParsedNodes has been converted to a float array, the size returned should be double, as the array space
     * has been doubled (one ParsedNode is then represented by two float values, each holding an index).
     *
     * @return the size/length of the arrays, as an integer.
     */
    public abstract int size();
}
