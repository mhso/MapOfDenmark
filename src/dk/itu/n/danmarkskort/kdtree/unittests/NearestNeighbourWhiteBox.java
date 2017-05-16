package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedPlace;
import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NearestNeighbourWhiteBox {

    KDTreeNode<ParsedWay> wayTree;
    KDTreeNode<ParsedPlace> placeTree;
    Point2D.Float query;
    ArrayList<ParsedPlace> places;
    ArrayList<ParsedWay> ways;
    double infinity = Double.POSITIVE_INFINITY;
    NearestNeighborTestTree neighborTest;

    @After
    public void tearDown() {
        wayTree = null;
        placeTree = null;
        query = null;
        places = null;
        ways = null;
        neighborTest = null;
    }

    private ArrayList<ParsedPlace> getListOfPlaces(int size, float startLon, float startLat) {
        ArrayList<ParsedPlace> arr = new ArrayList<>();
        for(int i = 0; i < size; i++) arr.add(new ParsedPlace("", startLon + i, startLat + i));
        return arr;
    }

    private ArrayList<ParsedWay> getListOfWays(int size, float startLon, float startLat, float lonVariance, float latVariance) {
        ArrayList<ParsedWay> arr = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            ParsedWay way = new ParsedWay();
            Point2D.Float point1, point2;
            if(i < ((size + 1)/ 2)) {
                point1 = new Point2D.Float(startLon + i, startLat + i);
                point2 = new Point2D.Float(startLon + i + lonVariance, startLat + i + latVariance);
            } else {
                point1 = new Point2D.Float(startLon + i, startLat + i);
                point2 = new Point2D.Float(startLon + i + lonVariance, startLat + i + latVariance);
            }
            way.addNode(point1);
            way.addNode(point2);
            way.nodesToCoords();
            arr.add(way);
        }
        return arr;
    }

    @Test
    public void testDataA() {
        ParsedWay expected = null;
        String expectedBranching = "1T";
        query = null;
        ways = getListOfWays(50, 1, 1, 0, 0);
        wayTree = new KDTreeNode<ParsedWay>(ways);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 0, true, wayTree));
        assertEquals(expectedBranching, neighborTest.getResultString());
    }

    @Test
    public void testDataB() {
        query = new Point2D.Float(0, 0);
        ways = getListOfWays(26, -26, -26, 15, 0);
        ParsedWay expected = ways.get(25);
        String expectedBranching = "1F2T3T4T7F8T9T10F11T12T13F14F17F";
        wayTree = new KDTreeNode<ParsedWay>(ways, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, infinity, true, wayTree));
        assertEquals(expectedBranching, neighborTest.getResultString());
    }

    @Test
    public void testDataC() {
        query = new Point2D.Float(0, 0);
        places = getListOfPlaces(26, -1, -1);
        ParsedPlace expected = places.get(1);
        String expectedBranching1 = "1F2F";
        String expectedBranching2 = "5T6F";
        String expectedBranching3 = "10T";
        placeTree = new KDTreeNode<ParsedPlace>(places, false);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, infinity, false, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching1) && 
        		neighborTest.getResultString().contains(expectedBranching2) && 
        		neighborTest.getResultString().contains(expectedBranching3));
        
    }

    @Test
    public void testDataD() {
    	query = new Point2D.Float(0, 0);
        ways = getListOfWays(26, -26, -26, 15, 0);
        ParsedWay expected = null;
        String expectedBranching1 = "1F2T3F4F";
        String expectedBranching2 = "8F11F";
        String expectedBranching3 = "15F17T18T";
        wayTree = new KDTreeNode<ParsedWay>(ways, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 0.01, true, wayTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching1) &&
        		neighborTest.getResultString().contains(expectedBranching2) &&
        		neighborTest.getResultString().contains(expectedBranching3));
    }

    @Test
    public void testDataE() {
        query = new Point2D.Float(-0.44f, -0.44f);
        places = getListOfPlaces(50, -50, -50);
        ParsedPlace expected = places.get(49);
        String expectedBranching1 = "5F6T";
        String expectedBranching2 = "13E";
        placeTree = new KDTreeNode<ParsedPlace>(places, false);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, infinity, false, placeTree));
        System.out.println(neighborTest.getResultString());
        assertTrue(neighborTest.getResultString().contains(expectedBranching1) &&
        		neighborTest.getResultString().contains(expectedBranching2));
    }

    @Test
    public void testDataF() {
        query = new Point2D.Float(-100, -100);
        places = getListOfPlaces(50, 0, 0);
        ParsedPlace expected = null;
        String expectedBranching = "7T";
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 20, true, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching));
    }

    @Test
    public void testDataG() {
        query = new Point2D.Float(100, 100);
        places = getListOfPlaces(50, 70, -100);
        ParsedPlace expected = null;
        String expectedBranching = "12F";
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 30, true, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching));
    }

    @Test
    public void testDataH() {
        query = new Point2D.Float(-20, -20);
        places = getListOfPlaces(50, -10, -10);
        ParsedPlace expected = places.get(1);
        String expectedBranching = "14T15T16T";
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, infinity, true, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching));
    }

    @Test
    public void testDataI() {
        query = new Point2D.Float(0, 0);
        places = getListOfPlaces(50, -2, 10);
        ParsedPlace expected = null;
        String expectedBranching = "9F";
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 4, true, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching));
    }

    @Test
    public void testDataJ() {
        query = new Point2D.Float(-20, -20);
        places = getListOfPlaces(100, 10, 20);
        ParsedPlace expected = null;
        String expectedBranching1 = "16F";
        String expectedBranching2 = "18F";
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        neighborTest = new NearestNeighborTestTree<>();
        assertEquals(expected, neighborTest.nearest(query, 0, true, placeTree));
        assertTrue(neighborTest.getResultString().contains(expectedBranching1) &&
        		neighborTest.getResultString().contains(expectedBranching2));
        
    }
}
