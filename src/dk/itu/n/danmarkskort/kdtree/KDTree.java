package dk.itu.n.danmarkskort.kdtree;

import dk.itu.n.danmarkskort.lightweight.models.ParsedItem;

import java.util.ArrayList;

public abstract class KDTree {

    public static ParsedItem[] listToArray(ArrayList<ParsedItem> list) {
        ParsedItem[] array = new ParsedItem[list.size()];
        for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        return array;
    }

    public abstract KDTree getParent();
    
    public KDTree getRoot() {
    	KDTree parent = getParent();
    	while(parent != null) parent = parent.getParent();
    	return parent;
    }
    
    public abstract int size();
}