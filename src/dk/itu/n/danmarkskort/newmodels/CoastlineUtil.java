package dk.itu.n.danmarkskort.newmodels;

import java.util.HashMap;


public class CoastlineUtil {

    public static void connectCoastline(HashMap<ParsedNode, ParsedItem> coastlineMap, ParsedItem current) {
        ParsedItem before = coastlineMap.remove(current.getFirstNode());
        ParsedItem after = coastlineMap.remove(current.getLastNode());
        ParsedItem merged = new ParsedWay();

        if(before != null) merged.addNodes(before.getNodes());
        merged.addNodes(current.getNodes());
        if(after != null) merged.addNodes(after.getNodes());

        coastlineMap.put(merged.getFirstNode(), merged);
        coastlineMap.put(merged.getLastNode(), merged);
    }

    public static void fixUnfinishedCoastlines(HashMap<ParsedNode, ParsedItem> coastlineMap) {

    }
}
