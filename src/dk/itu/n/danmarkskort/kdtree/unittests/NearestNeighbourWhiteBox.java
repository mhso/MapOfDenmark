package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedPlace;
import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NearestNeighbourWhiteBox {

    KDTree<ParsedWay> wayTree;
    KDTree<ParsedPlace> placeTree;
    Point2D.Float query;
    ArrayList<ParsedPlace> places;
    ArrayList<ParsedWay> ways;
    double infinity = Double.POSITIVE_INFINITY;

    @After
    public void tearDown() {
        wayTree = null;
        placeTree = null;
        query = null;
        places = null;
        ways = null;
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
        query = null;
        ways = getListOfWays(50, 1, 1, 0, 0);
        wayTree = new KDTreeNode<ParsedWay>(ways);
        assertEquals(expected, wayTree.nearest(query));
    }

    @Test
    public void testDataB() {
        query = new Point2D.Float(0, 0);
        ways = getListOfWays(26, -26, -26, 15, 0);
        ParsedWay expected = ways.get(25);
        wayTree = new KDTreeNode<ParsedWay>(ways, true);
        assertEquals(expected, wayTree.nearest(query, infinity, true));
    }

    @Test
    public void testDataC() {
        query = new Point2D.Float(0, 0);
        places = getListOfPlaces(26, -1, -1);
        ParsedPlace expected = places.get(1);
        placeTree = new KDTreeNode<ParsedPlace>(places, false);
        assertEquals(expected, placeTree.nearest(query, infinity, false));
    }

    @Test
    public void testDataD() {
        query = new Point2D.Float(-51, -51);
        places = getListOfPlaces(50, -50, -50);
        ParsedPlace expected = places.get(0);
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, 25, true));
    }

    @Test
    public void testDataE() {
        query = new Point2D.Float(-0.44f, -0.44f);
        places = getListOfPlaces(50, -50, -50);
        ParsedPlace expected = places.get(49);
        placeTree = new KDTreeNode<ParsedPlace>(places, false);
        assertEquals(expected, placeTree.nearest(query, infinity, false));
    }

    @Test
    public void testDataF() {
        query = new Point2D.Float(-100, -100);
        places = getListOfPlaces(50, 0, 0);
        ParsedPlace expected = null;
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, 20, true));
    }

    @Test
    public void testDataG() {
        query = new Point2D.Float(100, 100);
        places = getListOfPlaces(50, 70, -100);
        ParsedPlace expected = null;
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, 30, true));
    }

    @Test
    public void testDataH() {
        query = new Point2D.Float(-20, -20);
        places = getListOfPlaces(50, -10, -10);
        ParsedPlace expected = places.get(0);
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, infinity, true));
    }

    @Test
    public void testDataI() {
        query = new Point2D.Float(0, 0);
        places = getListOfPlaces(50, -2, 10);
        ParsedPlace expected = null;
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, 4, true));
    }

    @Test
    public void testData() {
        query = new Point2D.Float(-20, -20);
        places = getListOfPlaces(100, 10, 20);
        ParsedPlace expected = null;
        placeTree = new KDTreeNode<ParsedPlace>(places, true);
        assertEquals(expected, placeTree.nearest(query, 0, true));
    }
}
