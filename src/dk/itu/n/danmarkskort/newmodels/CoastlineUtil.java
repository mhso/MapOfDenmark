package dk.itu.n.danmarkskort.newmodels;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.kdtree.KDTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CoastlineUtil {

    public static void connect(ArrayList<ParsedItem> items){
        HashMap<ParsedNode, ParsedItem> coastlineMap = new HashMap<>();
        HashMap<ParsedNode, ParsedItem> unfinishedCoastlines = new HashMap<>();
        ArrayList<ParsedItem> finishedCoastlines = new ArrayList<>();
        for(ParsedItem item : items) {
            coastlineMap.put(item.getFirstNode(), item);
        }
        ParsedItem firstItem = null;
        int countouter = 0;
        while(coastlineMap.size() > 0) {
            Main.log("outerloop" + ++countouter);
            ArrayList<ParsedItem> values = new ArrayList<>(coastlineMap.values());
            if(firstItem != null) unfinishedCoastlines.put(firstItem.getFirstNode(), firstItem);
            firstItem = values.get(0);
            // error her!! Der er nogle coastline ways der bare fjernes, og aldrig tilføjes!!

            coastlineMap.remove(firstItem.getFirstNode());
            int count = 0;
            ParsedNode lastNode = firstItem.getLastNode();
            while(coastlineMap.containsKey(lastNode)) {
                firstItem.appendParsedItem(coastlineMap.get(lastNode));
                coastlineMap.remove(lastNode);
                lastNode = firstItem.getLastNode();
                Main.log("innerloop: " + ++count);
            }

            if(firstItem.getFirstNode() == lastNode) {
                finishedCoastlines.add(firstItem);
                firstItem = null;
            }
            //else unfinishedCoastlines.();
        }
        if(firstItem != null) finishedCoastlines.add(firstItem);

        items = finishedCoastlines;
        Main.log("did stuff with coastlines");
    }
}
