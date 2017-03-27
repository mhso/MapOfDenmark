package dk.itu.n.danmarkskort.mapdata;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.util.ArrayList;

public class KDUtil {

    public static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        ParsedItem[] array = new ParsedItem[list.size()];
        for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        return array;
    }

}
