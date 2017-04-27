package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class ParsedItem implements KDComparable, Serializable {

	private static final long serialVersionUID = -8684498422989385773L;

    public abstract Shape getShape();
    public abstract void deleteOldRefs();
    public abstract void nodesToCoords();
    public abstract int size();
}
