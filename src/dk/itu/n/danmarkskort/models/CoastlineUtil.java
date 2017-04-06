package dk.itu.n.danmarkskort.models;

import java.util.HashMap;


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

    }
}
