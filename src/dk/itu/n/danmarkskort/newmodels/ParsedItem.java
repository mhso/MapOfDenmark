package dk.itu.n.danmarkskort.newmodels;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public abstract class ParsedItem {

    // Dette har nok ikke så høj prioritet
    //private boolean isVisible = true;

    public int compareLon(ParsedItem item) {
        float a = getFirstLon();
        float b = item.getFirstLon();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    public int compareLat(ParsedItem item) {
        float a = getFirstLat();
        float b = item.getFirstLat();
        if(a > b) return 1;
        if(a == b) return 0;
        return 0;
    }

    //public void setVisible(boolean b) { isVisible = b; }
    //public boolean isVisible() { return isVisible; }

    public abstract ParsedNode getFirstNode();
    public abstract ParsedNode getLastNode();
    public abstract ArrayList<ParsedNode> getNodes();
    public abstract Path2D getPath();
    public abstract float getFirstLon();
    public abstract float getFirstLat();
}
