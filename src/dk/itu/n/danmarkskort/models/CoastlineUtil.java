package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;

import java.util.*;


public class CoastlineUtil {

    private static final int TOP_BOUND = 0,
            LEFT_BOUND = 1,
            BOTTOM_BOUND = 2,
            RIGHT_BOUND = 3;

    public static void connectCoastline(IdentityHashMap<ParsedNode, ParsedWay> coastlineMap, ParsedWay current) {
        ParsedWay prev = coastlineMap.remove(current.getFirstNode());
        ParsedWay next = coastlineMap.remove(current.getLastNode());
        ParsedWay merged = new ParsedWay();

        if(prev != null) merged.addNodes(prev.getNodes());
        merged.addNodes(current.getNodes());
        if(next != null) merged.addNodes(next.getNodes());

        coastlineMap.put(merged.getFirstNode(), merged);
        coastlineMap.put(merged.getLastNode(), merged);
    }

    public static HashSet<ParsedWay> fixUnconnectedCoastlines(HashSet<ParsedWay> unconnected) {
        HashMap<ParsedNode, ParsedWay> unconnectedEnds = new HashMap<>();
        HashMap<ParsedNode, ParsedWay> unconnectedStarts = new HashMap<>();

        // ArrayLists to contain the fixed-to-the-boundary end- and startnodes
        // index 0 for the top-aligned, 1: left-aligned, 2: bottom-aligned, and 3: right-aligned
        ArrayList<ParsedNode>[] starts = new ArrayList[4];
        ArrayList<ParsedNode>[] ends = new ArrayList[4];
        for(int i = 0; i < starts.length; i++) starts[i] = new ArrayList<>();
        for(int i = 0; i < ends.length; i++) ends[i] = new ArrayList<>();

        // adjust all ends and start nodes to bounds of the parsed map, and add them to the relevant arraylists
        // and hashmaps
        for(ParsedWay way : unconnected) {
            FakeNode newFirst = adjustNodeToBounds(way.getSecondNode(), way.getFirstNode());
            FakeNode newLast = adjustNodeToBounds(way.getSecondToLastNode(), way.getLastNode());

            ParsedWay newWay = new ParsedWay();

            newWay.addNodes(way.getNodes());

            newWay.setFirstNode(newFirst);
            newWay.setLastNode(newLast);

            starts[newFirst.getSide()].add(newFirst);
            ends[newLast.getSide()].add(newLast);

            unconnectedStarts.put(newFirst, newWay);
            unconnectedEnds.put(newLast, newWay);
        }

        sortSides(starts);
        sortSides(ends);

        ParsedNode endNode = null, startNode = null;
        ParsedWay endWay = null, startWay = null;
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

                if(endWay == startWay) connectedSet.add(endWay);
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
                    if(j == starts[i].size() - 1) {
                        if (i == 0) {
                            currentEnd = new FakeNode(Main.model.getMinLon(), Main.model.getMinLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break; // without a break we have potential for endless loop
                        } else if (i == 1) {
                            currentEnd = new FakeNode(Main.model.getMinLon(), Main.model.getMaxLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break;
                        } else if (i == 2) {
                            currentEnd = new FakeNode(Main.model.getMaxLon(), Main.model.getMaxLat());
                            currentWay.addNode(currentEnd);
                            i++;
                            break;
                        } else if (i == 3) {
                            currentEnd = new FakeNode(Main.model.getMaxLon(), Main.model.getMinLat());
                            currentWay.addNode(currentEnd);
                            i = 0;
                            break;
                        }
                    }
                }
            }
            else {
                if (i == 0) {
                    currentEnd = new FakeNode(Main.model.getMinLon(), Main.model.getMinLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 1) {
                    currentEnd = new FakeNode(Main.model.getMinLon(), Main.model.getMaxLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 2) {
                    currentEnd = new FakeNode(Main.model.getMaxLon(), Main.model.getMaxLat());
                    currentWay.addNode(currentEnd);
                    i++;
                } else if (i == 3) {
                    currentEnd = new FakeNode(Main.model.getMaxLon(), Main.model.getMinLat());
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

    // returns a double[] with index 0 as the slope, and index 1 as the constant
    private static double[] getLinearEquation(ParsedNode node1, ParsedNode node2) {
        double m = calculateSlope(node1, node2);
        double b = calculateConstant(node1, m);
        return new double[]{m, b};
    }

    // calculates the constant in a linear function, given a coordinate and a slope
    private static double calculateConstant(ParsedNode node, double m) {
        return (double) node.getLat() - (m * (double) node.getLon());
    }

    // calculates the slope for a line, between two coordinates
    private static double calculateSlope(ParsedNode node1, ParsedNode node2) {
        return ((double) node2.getLat() - (double) node1.getLat()) / (double) (node2.getLon() - (double) node1.getLon());
    }

    // return an intersection point as a float array, with x as index 0 and y as index 1
    private static float[] getIntersectionPoints(ParsedNode node1, ParsedNode node2) {
        double m = calculateSlope(node1, node2);
        double b = calculateConstant(node2, m);

        float[] intersection = new float[4];
        if(m == -0.0) {
            intersection[0] = (float) 0.1f;
            // do something sneaky so that we get the correct point!!!
        }
        else {
            intersection[0] = (float) ((Main.model.getMaxLat() - b) / m); // x-intersection on top
            intersection[2] = (float) ((Main.model.getMinLat() - b / m)); // x intersection at bottom
        }
        intersection[1] = (float) ((m * Main.model.getMinLon()) + b); // y-intersection on left
        intersection[3] = (float) (( m * Main.model.getMaxLon() + b)); // y-intersection
        return intersection;
    }

    private static FakeNode adjustNodeToBounds(ParsedNode node1, ParsedNode node2) {
        float minx = Main.model.getMinLon();
        float maxx = Main.model.getMaxLon();
        float miny = Main.model.getMinLat();
        float maxy = Main.model.getMaxLat();
        float midx = minx + ((maxx - minx) / 2);
        float midy = maxy + ((miny - maxy) / 2);

        boolean top, left;
        left = (node2.getLon() < midx);
        top = (node2.getLat() < midy);

        double m = calculateSlope(node1, node2); // hældningen
        if(m == -0.0) Main.log("OH NO");

        float[] intersectionPoints = getIntersectionPoints(node1, node2);

        boolean intersectsTop =     intersectionPoints[0] < maxx && intersectionPoints[0] > minx;
        boolean intersectsLeft =    intersectionPoints[1] < maxy && intersectionPoints[1] > miny;
        boolean intersectsBot =     intersectionPoints[2] < maxx && intersectionPoints[2] > minx;
        boolean intersectsRight =   intersectionPoints[3] < maxy && intersectionPoints[3] > miny;

        if(!intersectsTop && !intersectsLeft && !intersectsBot && !intersectsRight) {
            Main.log("minx: " + minx + ", maxx: " + maxx);
            Main.log("miny: " + miny + ", maxy: " + maxy);
            Main.log(node1.getLon() + " " + node1.getLat());
            Main.log(node2.getLon() + " " + node2.getLat());
            Main.log("intersectsnothing!!");
        }


        if(top && left) { // node2 er placeret omkring top-left
            if(intersectsTop && intersectsLeft) {
                if (m > 0) return new FakeNode(intersectionPoints[0], maxy, TOP_BOUND);
                else return new FakeNode(minx, intersectionPoints[1], LEFT_BOUND);
            }
            else if(intersectsTop) {
                return new FakeNode(intersectionPoints[0], maxy, TOP_BOUND);
            }
            else if(intersectsLeft) return new FakeNode(minx, intersectionPoints[1], LEFT_BOUND);
        }
        else if(left) { // node2 er placeret omkring bottom-left
            if(intersectsLeft && intersectsBot) {
                if (m > 0) return new FakeNode(minx, intersectionPoints[1], LEFT_BOUND);
                else return new FakeNode(intersectionPoints[2], maxy, BOTTOM_BOUND);
            }
            else if(intersectsLeft) return new FakeNode(minx, intersectionPoints[1], LEFT_BOUND);
            else if(intersectsBot) return new FakeNode(intersectionPoints[2], maxy, BOTTOM_BOUND);
        }
        else if(!top) { // node2 er placeret omkring bottom-right
            if(intersectsBot && intersectsRight) {
                if (m < 0) return new FakeNode(intersectionPoints[2], maxy, BOTTOM_BOUND);
                else return new FakeNode(maxx, intersectionPoints[3], RIGHT_BOUND);
            }
            else if(intersectsBot) return new FakeNode(intersectionPoints[2], maxy, BOTTOM_BOUND);
            else if(intersectsRight) return new FakeNode(maxx, intersectionPoints[3], RIGHT_BOUND);
        }
        else { // node2 er placeret omkring top-right
            if(intersectsRight && intersectsTop) {
                if (m < 0) return new FakeNode(maxx, intersectionPoints[3], RIGHT_BOUND);
                else return new FakeNode(intersectionPoints[0], maxy, TOP_BOUND);
            }
            else if(intersectsRight) return new FakeNode(maxx, intersectionPoints[3], RIGHT_BOUND);
            else if(intersectsTop) return new FakeNode(intersectionPoints[0], maxy, TOP_BOUND);
        }
        Main.log("m: " + m + ", b: " + b);
        return null;
    }
}
