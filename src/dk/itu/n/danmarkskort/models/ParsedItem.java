package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.models.Model;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public abstract class ParsedItem extends Model {

    // Dette har nok ikke så høj prioritet
    //private boolean isVisible = true;

    public int compareLon(ParsedItem item) {
        float a = getFirstNode().getLon();
        float b = item.getFirstNode().getLon();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public int compareLat(ParsedItem item) {
        float a = getFirstNode().getLat();
        float b = item.getFirstNode().getLat();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    //public void setVisible(boolean b) { isVisible = b; }
    //public boolean isVisible() { return isVisible; }

    public abstract void addNodes(ArrayList<ParsedNode> nodes);
    public abstract ParsedNode getFirstNode();
    public abstract ParsedNode getLastNode();
    public abstract ArrayList<ParsedNode> getNodes();
    public abstract Path2D getPath();
    public abstract Path2D getReversedPath();
}
