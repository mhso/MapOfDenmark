package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;

import java.util.HashMap;
import java.util.Map;


public class CoastlineUtil {

    public static void connectCoastline(HashMap<ParsedNode, ParsedWay> coastlineMap, ParsedWay current) {
        ParsedWay before = coastlineMap.remove(current.getFirstNode());
        ParsedWay after = coastlineMap.remove(current.getLastNode());
        ParsedWay merged = new ParsedWay();

        if(before != null) merged.addNodes(before.getNodes());
        merged.addNodes(current.getNodes());
        if(after != null) merged.addNodes(after.getNodes());

        coastlineMap.put(merged.getFirstNode(), merged);
        coastlineMap.put(merged.getLastNode(), merged);
    }

    public static void fixUnfinishedCoastlines(HashMap<ParsedNode, ParsedWay> coastlineMap) {
        float minlat = Main.model.getMinLat();
        float maxlat = Main.model.getMaxLat();
        float minlon = Main.model.getMinLon();
        float maxlon = Main.model.getMaxLon();

        // fix all first and lastnodes, so they are on the edge of the bounds
        for(Map.Entry<ParsedNode, ParsedWay> e : coastlineMap.entrySet()) {
            ParsedWay current = e.getValue();
            if(current.getFirstNode() != current.getLastNode()) {

            }
        }
    }
}
