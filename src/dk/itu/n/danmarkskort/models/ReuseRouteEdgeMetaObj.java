package dk.itu.n.danmarkskort.models;

import java.util.HashMap;

import dk.itu.n.danmarkskort.routeplanner.RouteEdgeMeta;

public class ReuseRouteEdgeMetaObj {
	private static HashMap<String, RouteEdgeMeta> map = new HashMap<String, RouteEdgeMeta>();
	
	public static RouteEdgeMeta make(RouteEdgeMeta routeEdgeMeta){
		if(routeEdgeMeta == null) return null;
		if(map.containsKey(routeEdgeMeta.getKey())) {
			return map.get(routeEdgeMeta.getKey());
		} else {
				map.put(routeEdgeMeta.getKey(), routeEdgeMeta);
				return routeEdgeMeta;
		}
	}
	
	public static void clear(){
		map.clear();
	}
}
