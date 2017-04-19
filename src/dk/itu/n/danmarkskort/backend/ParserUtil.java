package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.WayType;

import java.util.*;

public class ParserUtil {

    private static final int TOP = 0,
                             LEFT = 1,
                             BOTTOM = 2,
                             RIGHT = 3;

    public static void connectCoastline(HashMap<ParsedNode, ParsedWay> coastlineMap, ParsedWay current) {
        ParsedWay prev = coastlineMap.remove(current.getFirstNode());
        ParsedWay next = coastlineMap.remove(current.getLastNode());
        ParsedWay merged = new ParsedWay();

        if(prev != null) merged.addNodes(prev.getNodes());
        merged.addNodes(current.getNodes());
        if(next != null && next != prev) merged.addNodes(next.getNodes());

        coastlineMap.put(merged.getFirstNode(), merged);
        coastlineMap.put(merged.getLastNode(), merged);
    }

    public static HashSet<ParsedWay> fixUnconnectedCoastlines(HashSet<ParsedWay> unconnected) {
        HashMap<ParsedNode, ParsedWay> unconnectedEnds = new HashMap<>();
        HashMap<ParsedNode, ParsedWay> unconnectedStarts = new HashMap<>();

        // index 0 for the top-aligned, 1: left-aligned, 2: bottom-aligned, and 3: right-aligned
        ArrayList<ParsedNode>[] starts = new ArrayList[4];
        ArrayList<ParsedNode>[] ends = new ArrayList[4];
        for(int i = 0; i < starts.length; i++) starts[i] = new ArrayList<>();
        for(int i = 0; i < ends.length; i++) ends[i] = new ArrayList<>();

        for(ParsedWay way : unconnected) {
            int firstSide = findSide(way.getFirstNode());
            int lastSide = findSide(way.getLastNode());

            ParsedWay clone = new ParsedWay();

            clone.addNodes(way.getNodes());

            starts[firstSide].add(clone.getFirstNode());
            ends[lastSide].add(clone.getLastNode());

            unconnectedStarts.put(clone.getFirstNode(), clone);
            unconnectedEnds.put(clone.getLastNode(), clone);
        }

        sortSides(starts);
        sortSides(ends);

        ParsedNode endNode, startNode;
        ParsedWay endWay, startWay;

        HashSet<ParsedWay> connectedSet = new HashSet<>();

        for(int i = 0; i < ends.length; i++) {
            for(int j = 0; j < ends[i].size(); j++) {
                endNode = ends[i].get(j);
                endWay = unconnectedEnds.get(endNode);
                startNode = findMatch(starts, endNode, endWay, i);
                startWay = unconnectedStarts.get(startNode);

                unconnectedEnds.remove(endWay.getLastNode());
                unconnectedStarts.remove(endWay.getFirstNode());
                unconnectedEnds.remove(startWay.getLastNode());
                unconnectedStarts.remove(startWay.getFirstNode());

                if(endWay == startWay) {
                    endWay.addNode(startWay.getFirstNode());
                    connectedSet.add(endWay);
                }
                else {
                    endWay.addNodes(startWay.getNodes());
                    unconnectedEnds.put(endWay.getLastNode(), endWay);
                    unconnectedStarts.put(endWay.getFirstNode(), endWay);
                }
            }
        }
        return connectedSet;
    }

    private static ParsedNode findMatch(ArrayList<ParsedNode>[] starts, ParsedNode endNode, ParsedWay currentWay, int startSide) {
        ParsedNode currentEnd = endNode;
        int runs = 0;
        for(int i = startSide; i < starts.length; ) {
            runs++;
            if(starts[i].size() > 0) {
                for(int j = 0; j < starts[i].size(); j++) {
                    ParsedNode currentCheck = starts[i].get(j);
                    if(currentCheck != null) {
                        if(runs > 1) {
                            starts[i].set(j, null);
                            return currentCheck;
                        }

                        if (i == 0) {
                            if (currentEnd.getLon() > currentCheck.getLon()) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 1) {
                            if (currentEnd.getLat() > currentCheck.getLat()) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 2) {
                            if (currentEnd.getLon() < currentCheck.getLon()) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 3) {
                            if (currentEnd.getLat() < currentCheck.getLat()) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        }
                    }
                    else if(j == starts[i].size() - 1) {
                        if (i == 0) {
                            currentEnd = new ParsedNode(Main.model.getMinLon(), Main.model.getMinLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break; // without a break we have potential for endless loop
                        } else if (i == 1) {
                            currentEnd = new ParsedNode(Main.model.getMinLon(), Main.model.getMaxLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break;
                        } else if (i == 2) {
                            currentEnd = new ParsedNode(Main.model.getMaxLon(), Main.model.getMaxLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break;
                        } else if (i == 3) {
                            currentEnd = new ParsedNode(Main.model.getMaxLon(), Main.model.getMinLat());
                            currentWay.addNode(currentEnd);
                            i = 0;
                            break;
                        }
                    }
                }
            }
            else {
                if (i == 0) {
                    currentEnd = new ParsedNode(Main.model.getMinLon(), Main.model.getMaxLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 1) {
                    currentEnd = new ParsedNode(Main.model.getMinLon(), Main.model.getMinLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 2) {
                    currentEnd = new ParsedNode(Main.model.getMaxLon(), Main.model.getMinLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 3) {
                    currentEnd = new ParsedNode(Main.model.getMaxLon(), Main.model.getMaxLat());
                    currentWay.addNode(currentEnd);
                    i = 0;
                }
            }
        }
        return null;
    }

    private static void sortSides(ArrayList<ParsedNode>[] sides) {
        // sorting all sides, counter-clockwise
        // top side. sorting from right to left
        sides[0].sort((ParsedNode f1, ParsedNode f2) -> {
            if(f1.getLon() < f2.getLon()) return 1;
            else return -1;
        });

        // left side. sorting from top to bottom
        sides[1].sort((ParsedNode f1, ParsedNode f2) -> {
            if(f1.getLat() < f2.getLat()) return 1;
            else return -1;
        });

        // bottom side. sorting from left to right
        sides[2].sort((ParsedNode f1, ParsedNode f2) -> {
            if(f1.getLon() > f2.getLon()) return 1;
            else return -1;
        });

        // right side. sorting from bottom to top
        sides[3].sort((ParsedNode f1, ParsedNode f2) -> {
            if(f1.getLat() > f2.getLat()) return 1;
            else return -1;
        });
    }

    private static int findSide(ParsedNode node) {
        int side;

        float distanceToLeft = Math.abs(node.getLon() - Main.model.getMinLon());
        float distanceToRight = Math.abs(node.getLon() - Main.model.getMaxLon());
        float distanceToBottom = Math.abs(node.getLat() - Main.model.getMinLat());
        float distanceToTop = Math.abs(node.getLat() - Main.model.getMaxLat());

        float midLon = Main.model.getMinLon() + ((Main.model.getMaxLon() - Main.model.getMinLon()) / 2);
        float midLat = Main.model.getMinLat() + ((Main.model.getMaxLat() - Main.model.getMinLat()) / 2);

        boolean top, left;
        left = (node.getLon() < midLon);
        top = (node.getLat() < midLat);

        if (top && left) { // top or left
            if (distanceToTop < distanceToLeft) side = TOP;
            else side = LEFT;
        }
        else if (left) { // left or bottom
            if (distanceToLeft < distanceToBottom) side = LEFT;
            else side = BOTTOM;
        }
        else if (!top) { // bottom or right
            if (distanceToBottom < distanceToRight) side = BOTTOM;
            else side = RIGHT;
        }
        else { // right or top
            if (distanceToRight < distanceToTop) side = RIGHT;
            else side = TOP;
        }
        return side;
    }

    public static WayType tagToType(String k, String v, WayType oldtype) {
        if(oldtype != null && oldtype == WayType.COASTLINE) return oldtype; // otherwise some coastlines will be changed to a different type
        switch (k) {
            case "highway":
                switch(v) {
                    case "motorway_link":
                    case "motorway":
                        return WayType.HIGHWAY_MOTORWAY;
                    case "trunk_link":
                    case "trunk":
                        return WayType.HIGHWAY_TRUNK;
                    case "primary_link":
                    case "primary":
                        return WayType.HIGHWAY_PRIMARY;
                    case "secondary_link":
                    case "secondary":
                        return WayType.HIGHWAY_SECONDARY;
                    case "tertiary_link":
                    case "tertiary":
                        return WayType.HIGHWAY_TERTIARY;
                    case "residential":
                        return WayType.HIGHWAY_RESIDENTIAL;
                    case "pedestrian":
                        return WayType.PEDESTRIAN;
                    case "unclassified":
                        return WayType.HIGHWAY_UNCLASSIFIED;
                    case "service":
                        return WayType.HIGHWAY_SERVICE;
                    case "driveway":
                        return WayType.HIGHWAY_DRIVEWAY;
                    case "cycleway":
                        return WayType.HIGHWAY_CYCLEWAY;
                    case "traffic_signal":
                        return oldtype;
                    case "path":
                    case "footway":
                    case "bridleway":
                    case "track":
                    case "steps":
                        return WayType.HIGHWAY_FOOTWAY;
                    default:
                        return WayType.HIGHWAY_UNDEFINED;
                }
            case "building":
                switch (v) {
                    case "school":
                        return WayType.BUILDING_SCHOOL;
                    case "train_station":
                        return WayType.TRAIN_STATION;
                    case "roof":
                        return WayType.ROOF;
                    default:
                        return WayType.BUILDING;
                }
            case "landuse":
                switch(v) {
                    case "residential":
                        return WayType.RESIDENTIAL;
                    case "farmland":
                        return WayType.FARMLAND;
                    case "forest":
                        return WayType.FOREST;
                    case "industrial":
                        return WayType.INDUSTRIAL;
                    case "recreation_ground":
                    case "grass":
                        return WayType.GRASS;
                    case "retail":
                        return WayType.RETAIL;
                    case "military":
                        return WayType.MILITARY;
                    case "cemetery":
                        return WayType.CEMETERY;
                    case "orchard":
                        return WayType.ORCHARD;
                    case "allotments":
                        return WayType.ALLOTMENTS;
                    case "construction":
                        return WayType.CONSTRUCTION;
                    case "railway":
                        return WayType.RAILWAY;
                }
                return oldtype;
            case "natural":
                switch(v) {
                    case "water":
                        return WayType.WATER;
                    case "coastline":
                        return WayType.COASTLINE;
                    case "scrub":
                        return WayType.SCRUB;
                    case "wood":
                        return WayType.WOOD;
                    case "wetland":
                        return WayType.WETLAND;
                    case "sand":
                        return WayType.SAND;
                }
                break;
            case "railway":
                switch(v) {
                    case "light_rail":
                        return WayType.LIGHT_RAIL;
                    case "platform":
                        return WayType.PLATFORM;
                }
                return oldtype;
            case "man_made":
                switch(v) {
                    case "breakwater":
                        return WayType.BREAKWATER;
                    case "pier":
                        return WayType.PIER;
                    case "embankment":
                        return WayType.EMBANKMENT;
                }
                return oldtype;
            case "waterway":
                switch(v) {
                    case "stream":
                        return WayType.WATER_STREAM;
                    case "river":
                        return WayType.WATER_RIVER;
                }
                return oldtype;
            case "leisure":
                switch(v) {
                    case "stadium":
                        return WayType.STADIUM;
                    case "park":
                    case "garden":
                        return WayType.PARK;
                    case "playground":
                        return WayType.PLAYGROUND;
                    case "track":
                    case "pitch":
                        return WayType.SPORT;
                }
                return oldtype;
            case "amenity":
                switch(v) {
                    case "parking":
                        return WayType.PARKING;
                }
                return oldtype;
        }
        return oldtype;
    }
}
