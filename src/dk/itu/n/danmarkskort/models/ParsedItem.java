package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.kdtree.KDComparable;

import java.awt.*;
import java.io.Serializable;

public abstract class ParsedItem implements KDComparable, Serializable {

	private static final long serialVersionUID = -8684498422989385773L;
	@Override
    public int compareLon(KDComparable item) {
        float a = getFirstNode().getLon();
        float b = item.getFirstNode().getLon();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    @Override
    public int compareLat(KDComparable item) {
        float a = getFirstNode().getLat();
        float b = item.getFirstNode().getLat();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public abstract ParsedNode[] getNodes();
    public abstract Shape getShape();
    public abstract void deleteOldRefs();
    public abstract void nodesToCoords();
}
