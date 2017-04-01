package dk.itu.n.danmarkskort.lightweight.models;

import java.awt.geom.Path2D;

public class ParsedCoastline extends ParsedRelation {

    public ParsedCoastline(long id) {
        super(id);
    }

    public Path2D getPath() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        if(items != null) {
            for(ParsedItem item : items) {
                path.append(item.getPath(), true);
            }
        }
        return path;
    }
}
