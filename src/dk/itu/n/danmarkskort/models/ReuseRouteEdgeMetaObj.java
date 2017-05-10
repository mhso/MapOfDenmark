package dk.itu.n.danmarkskort.models;

import java.util.HashMap;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.routeplanner.RouteEdgeMeta;

public class ReuseRouteEdgeMetaObj {
	private static HashMap<String, RouteEdgeMeta> map = new HashMap<String, RouteEdgeMeta>();
	private static int keysTotal, keysReused, keysCreated;
	
	public static RouteEdgeMeta make(RouteEdgeMeta routeEdgeMeta){
		keysTotal++;
		if(routeEdgeMeta == null) return null;
		if(map.containsKey(routeEdgeMeta.getKey())) {
			keysReused++;
			return map.get(routeEdgeMeta.getKey());
		} else {
			keysCreated++;
			map.put(routeEdgeMeta.getKey(), routeEdgeMeta);
			return routeEdgeMeta;
		}
	}
	
	public static void clear(){
		Main.log("ReuseRouteEdgeMetaObj, keysTotal: " + keysTotal + " keysReused: " + keysReused + " keysCreated: " + keysCreated);
		map.clear();
	}
}
