package dk.itu.n.danmarkskort.models;

import dk.itu.n.danmarkskort.Main;

import java.util.*;


public class CoastlineUtil {

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
            FakeNode newFirst = adjustNodeToBounds(way.getFirstNode());
            FakeNode newLast = adjustNodeToBounds(way.getLastNode());

            ParsedWay newWay = new ParsedWay();


            way.setFirstNode(newFirst);
            way.setLastNode(newLast);

            starts[newFirst.getSide()].add(newFirst);
            ends[newLast.getSide()].add(newLast);

            unconnectedStarts.put(newFirst, way);
            unconnectedEnds.put(newLast, way);
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
                Main.log("looking for a match");
                startNode = findMatch(starts, endNode, endWay, i);
                Main.log("found a match");
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

    // returns a float[] with index 0 as the slope, and index 1 as the constant
    private double[] getLinearEquation(ParsedNode node1, ParsedNode node2) {
        double m = calculateSlope(node1, node2);
        double b = calculateConstant(node1, m);
        return new double[]{m, b};
    }

    private double calculateConstant(ParsedNode node, double m) {
        return (double) node.getLat() - (m * (double) node.getLon());
    }

    private double calculateSlope(ParsedNode node1, ParsedNode node2) {
        return ((double) node2.getLat() - (double) node1.getLat()) / (double) (node2.getLon() - (double) node1.getLon());
    }

    private static FakeNode adjustNodeToBounds(ParsedNode node) {
        float xvsmin = Math.abs(node.getLon() - Main.model.getMinLon());
        float xvsmax = Math.abs(node.getLon() - Main.model.getMaxLon());

        float yvsmin = Math.abs(node.getLat() + (-Main.model.getMinLat()));
        float yvsmax = Math.abs(node.getLat() + (-Main.model.getMaxLat()));

        if(xvsmin < xvsmax) { // left-aligned
            if(yvsmin < yvsmax) { // top-aligned
                if(xvsmin < yvsmin) return new FakeNode(Main.model.getMinLon(), node.getLat(), FakeNode.LEFT); // closest to left
                else return new FakeNode(node.getLon(), Main.model.getMinLat(), FakeNode.TOP); // closest to top
            }
            else { // bottom-aligned
                if(xvsmin < yvsmax) return new FakeNode(Main.model.getMinLon(), node.getLat(), FakeNode.LEFT); // closes to left
                else return new FakeNode(node.getLon(), Main.model.getMaxLat(), FakeNode.BOTTOM); // closest to bottom
            }
        }
        else { // right-aligned
            if(yvsmin < yvsmax) { // top-aligned
                if(xvsmax < yvsmin) return new FakeNode(Main.model.getMaxLon(), node.getLat(), FakeNode.RIGHT); // closest to right
                else return new FakeNode(node.getLon(), Main.model.getMinLat(), FakeNode.TOP); // closest to top
            }
            else { // bottom-aligned
                if(xvsmax < yvsmax) return new FakeNode(Main.model.getMaxLon(), node.getLat(), FakeNode.RIGHT); // closest to right
                else return new FakeNode(node.getLon(), Main.model.getMaxLat(), FakeNode.BOTTOM); // closest to bottom
            }
        }
    }
}
