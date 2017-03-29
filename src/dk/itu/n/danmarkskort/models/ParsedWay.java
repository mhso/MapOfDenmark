package dk.itu.n.danmarkskort.models;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import dk.itu.n.danmarkskort.Util;

public class ParsedWay extends ParsedObject {

	private List<ParsedNode> nodes = new ArrayList<ParsedNode>();
	private List<Long> nodeIds = new ArrayList<Long>();
	private boolean closedShape = false;
	public WayType type = WayType.UNDEFINED;
	
	public ParsedNode[] getNodes() {
		return nodes.toArray(new ParsedNode[nodes.size()]);
	}
	
	public List<Long> getNodeIds() {
		return nodeIds;
	}
	
	public void addNodeId(long nodeId) {
		nodeIds.add(nodeId);
	}
	
	public void addNode(ParsedNode node) {
		nodes.add(node);
		long id = node.getId();
		if(nodeIds.contains(id)) {
			nodeIds.remove(id);
			isCompletelyLinked();
		}
	}
	
	public Shape getShape() {
		Path2D path = new Path2D.Float();
		ParsedNode first = get(0);
		Point2D firstPost = Util.coordinateToScreen(first.getPosition());
		path.moveTo(firstPost.getX(), firstPost.getY());
		
		for(int i=1; i<nodes.size(); i++) {
			ParsedNode node = get(i);
			Point2D post = Util.coordinateToScreen(node.getPosition());
			path.lineTo(post.getX(), post.getY());
		}
		
		if(closedShape) path.closePath();
		return path;
	}
	
	public void determineType() {
		if(attributes.containsKey("building")) {//|| "residential".equals(attributes.get("landuse"))) {
			type = WayType.BUILDING;
			if("train_station".equals(attributes.get("buidling"))) {
				type = WayType.TRAIN_STATION;
			}
			closedShape = true;
		} else if("coastline".equals(attributes.get("natural"))) {
			type = WayType.COASTLINE;
		} else if(attributes.containsKey("highway")) {
			if(attributes.get("highway").equals("track")) 			type = WayType.WAY_TRACK;
			else if(attributes.get("highway").equals("path")) 		type = WayType.WAY_PATH;
			else if(attributes.get("highway").equals("service")) 	type = WayType.WAY_SERVICE;
			else if(attributes.get("highway").equals("tertiary"))	type = WayType.HIGHWAY_TERTIARY;
			else if(attributes.get("highway").equals("secondary"))	type = WayType.HIGHWAY_SECONDARY;
			else if(attributes.get("highway").equals("primary"))	type = WayType.HIGHWAY_PRIMARY;
			else if(attributes.get("highway").equals("driveway"))	type = WayType.HIGHWAY_DRIVEWAY;
			else if(attributes.get("highway").equals("residental"))	type = WayType.HIGHWAY_RESIDENTAL;
			else if(attributes.get("highway").equals("cycleway"))	type = WayType.HIGHWAY_CYCLEWAY;
			else if(attributes.get("highway").equals("footway"))	type = WayType.HIGHWAY_FOOTWAY;
			else if(attributes.get("highway").equals("steps"))		type = WayType.HIGHWAY_STEPS;
			else if(attributes.get("highway").equals("motorway"))	type = WayType.HIGHWAY_MOTORWAY;
			else if(attributes.get("highway").equals("trunk"))		type = WayType.HIGHWAY_TRUNK;
			else type = WayType.WAY_UNDEFINED;
		}
		else if("ferry".equals(attributes.get("route"))) {
			type = WayType.ROUTE_FERRY;
		} 
		else if("forest".equals(attributes.get("landuse"))) {
			type = WayType.FOREST;
			closedShape = true;
		}
		else if("industrial".equals(attributes.get("landuse"))) {
			type = WayType.INDUSTRIAL;
			closedShape = true;
		}
		else if("grass".equals(attributes.get("landuse"))) {
			type = WayType.GRASS;
			closedShape = true;
		}
		else if("grass".equals(attributes.get("landuse"))) {
			type = WayType.FARMLAND;
			closedShape = true;
		}
		else if("scrub".equals(attributes.get("natural"))) {
			type = WayType.SCRUB;
			closedShape = true;
		}
		else if("retail".equals(attributes.get("landuse"))) {
			type = WayType.RETAIL;
			closedShape = true;
		}
		else if("military".equals(attributes.get("landuse"))) {
			type = WayType.MILITARY;
			closedShape = true;
		}
		else if("cemetary".equals(attributes.get("landuse"))) {
			type = WayType.CEMETERY;
			closedShape = true;
		}
		else if("leisure".equals(attributes.get("stadium"))) {
			type = WayType.STADIUM;
			closedShape = true;
		}
		else if("orchard".equals(attributes.get("landuse"))) {
			type = WayType.ORCHARD;
			closedShape = true;
		}
		else if("allotments".equals(attributes.get("landuse"))) {
			type = WayType.ALLOTMENTS;
			closedShape = true;
		}
		else if("construction".equals(attributes.get("landuse"))) {
			type = WayType.CONSTRUCTION;
			closedShape = true;
		}
		else if("pitch".equals(attributes.get("leisure")) && attributes.containsKey("sport")) {
			type = WayType.SPORT;
			closedShape = true;
		}
		else if("wetland".equals(attributes.get("natural"))) {
			type = WayType.WETLAND;
			closedShape = true;
		}
		else if("sand".equals(attributes.get("natural"))) {
			type = WayType.SAND;
			closedShape = true;
		}
		else if("school".equals(attributes.get("amenity"))) {
			type = WayType.SAND;
			closedShape = true;
		}
		else if("park".equals(attributes.get("leisure"))) {
			type = WayType.PARK;
			closedShape = true;
		}
		else if("playground".equals(attributes.get("leisure"))) {
			type = WayType.PLAYGROUND;
			closedShape = true;
		}
		else if("hedge".equals(attributes.get("barrier"))) {
			type = WayType.HEDGE;
		}
		else if("breakwater".equals(attributes.get("man_made"))) {
			type = WayType.WAY_BREAKWATER;
			closedShape = true;
		} 
		else if("pier".equals(attributes.get("man_made"))) {
			type = WayType.WAY_PIER;
		} 
		else if("embankment".equals(attributes.get("man_made"))) {
			type = WayType.WAY_EMBANKMENT;
		}
		else if("route".equals(attributes.get("type")) && "power".equals(attributes.get("route"))) {
			type = WayType.WAY_POWER_LINE;
		} else if("stream".equals(attributes.get("waterway"))) {
			type = WayType.WATER_STREAM;
		} else if("river".equals(attributes.get("waterway"))) {
			type = WayType.WATER_RIVER;
		} else if("ditch".equals(attributes.get("ditch"))) {
			type = WayType.WATER_DITCH;
			if(attributes.get("tunnel") != null) type = WayType.WATER_DITCH_TUNNEL;
		}
	}
	
	public boolean isCompletelyLinked() {
		return (nodeIds.size() == 0);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public ParsedNode get(int index) {
		return nodes.get(index);
	}
	
	public void parseAttributes() {
		determineType();
		attributes.clear();
	}
	
}
