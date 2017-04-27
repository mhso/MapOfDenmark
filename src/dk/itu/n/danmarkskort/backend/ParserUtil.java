package dk.itu.n.danmarkskort.backend;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.ParsedNode;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.WayType;

import java.awt.geom.Point2D;
import java.util.*;

public class ParserUtil {

    private static final int TOP = 0,
            LEFT = 1,
            BOTTOM = 2,
            RIGHT = 3;

    public static void connectCoastline(HashMap<Point2D.Float, ParsedWay> coastlineMap, ParsedWay current) {
        ParsedWay prev = coastlineMap.remove(current.getFirstNode());
        ParsedWay next = coastlineMap.remove(current.getLastNode());
        ParsedWay merged = new ParsedWay();

        if (prev != null) merged.addNodes(prev.getNodes());
        merged.addNodes(current.getNodes());
        if (next != null && next != prev) merged.addNodes(next.getNodes());

        coastlineMap.put(merged.getFirstNode(), merged);
        coastlineMap.put(merged.getLastNode(), merged);
    }

    public static HashSet<ParsedWay> fixUnconnectedCoastlines(HashSet<ParsedWay> unconnected) {
        HashMap<Point2D.Float, ParsedWay> unconnectedEnds = new HashMap<>();
        HashMap<Point2D.Float, ParsedWay> unconnectedStarts = new HashMap<>();

        // index 0 for the top-aligned, 1: left-aligned, 2: bottom-aligned, and 3: right-aligned
        ArrayList<Point2D.Float>[] starts = new ArrayList[4];
        ArrayList<Point2D.Float>[] ends = new ArrayList[4];
        for (int i = 0; i < starts.length; i++) starts[i] = new ArrayList<>();
        for (int i = 0; i < ends.length; i++) ends[i] = new ArrayList<>();

        for (ParsedWay way : unconnected) {
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

        Point2D.Float endNode, startNode;
        ParsedWay endWay, startWay;

        HashSet<ParsedWay> connectedSet = new HashSet<>();

        for (int i = 0; i < ends.length; i++) {
            for (int j = 0; j < ends[i].size(); j++) {
                endNode = ends[i].get(j);
                endWay = unconnectedEnds.get(endNode);
                startNode = findMatch(starts, endNode, endWay, i);
                startWay = unconnectedStarts.get(startNode);

                unconnectedEnds.remove(endWay.getLastNode());
                unconnectedStarts.remove(endWay.getFirstNode());
                unconnectedEnds.remove(startWay.getLastNode());
                unconnectedStarts.remove(startWay.getFirstNode());

                if (endWay == startWay) {
                    endWay.addNode(startWay.getFirstNode());
                    connectedSet.add(endWay);
                } else {
                    endWay.addNodes(startWay.getNodes());
                    unconnectedEnds.put(endWay.getLastNode(), endWay);
                    unconnectedStarts.put(endWay.getFirstNode(), endWay);
                }
            }
        }
        return connectedSet;
    }

    private static Point2D.Float findMatch(ArrayList<Point2D.Float>[] starts, Point2D.Float endNode, ParsedWay currentWay, int startSide) {
        Point2D.Float currentEnd = endNode;
        int runs = 0;
        for (int i = startSide; i < starts.length; ) {
            runs++;
            if (starts[i].size() > 0) {
                for (int j = 0; j < starts[i].size(); j++) {
                    Point2D.Float currentCheck = starts[i].get(j);
                    if (currentCheck != null) {
                        if (runs > 1) {
                            starts[i].set(j, null);
                            return currentCheck;
                        }

                        if (i == 0) {
                            if (currentEnd.x > currentCheck.x) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 1) {
                            if (currentEnd.y > currentCheck.y) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 2) {
                            if (currentEnd.x < currentCheck.x) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        } else if (i == 3) {
                            if (currentEnd.y < currentCheck.y) {
                                starts[i].set(j, null);
                                return currentCheck;
                            }
                        }
                    } else if (j == starts[i].size() - 1) {
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
            } else {
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

    private static void sortSides(ArrayList<Point2D.Float>[] sides) {
        // sorting all sides, counter-clockwise
        // top side. sorting from right to left
        sides[0].sort((Point2D.Float f1, Point2D.Float f2) -> {
            if (f1.x < f2.x) return 1;
            else return -1;
        });

        // left side. sorting from top to bottom
        sides[1].sort((Point2D.Float f1, Point2D.Float f2) -> {
            if (f1.y < f2.y) return 1;
            else return -1;
        });

        // bottom side. sorting from left to right
        sides[2].sort((Point2D.Float f1, Point2D.Float f2) -> {
            if (f1.x > f2.x) return 1;
            else return -1;
        });

        // right side. sorting from bottom to top
        sides[3].sort((Point2D.Float f1, Point2D.Float f2) -> {
            if (f1.y > f2.y) return 1;
            else return -1;
        });
    }

    private static int findSide(Point2D.Float node) {
        int side;

        float distanceToLeft = Math.abs(node.x - Main.model.getMinLon());
        float distanceToRight = Math.abs(node.x - Main.model.getMaxLon());
        float distanceToBottom = Math.abs(node.y - Main.model.getMinLat());
        float distanceToTop = Math.abs(node.y - Main.model.getMaxLat());

        float midLon = Main.model.getMinLon() + ((Main.model.getMaxLon() - Main.model.getMinLon()) / 2);
        float midLat = Main.model.getMinLat() + ((Main.model.getMaxLat() - Main.model.getMinLat()) / 2);

        boolean top, left;
        left = (node.x < midLon);
        top = (node.y < midLat);

        if (top && left) { // top or left
            if (distanceToTop < distanceToLeft) side = TOP;
            else side = LEFT;
        } else if (left) { // left or bottom
            if (distanceToLeft < distanceToBottom) side = LEFT;
            else side = BOTTOM;
        } else if (!top) { // bottom or right
            if (distanceToBottom < distanceToRight) side = BOTTOM;
            else side = RIGHT;
        } else { // right or top
            if (distanceToRight < distanceToTop) side = RIGHT;
            else side = TOP;
        }
        return side;
    }
}
