package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedWay;

import dk.itu.n.danmarkskort.models.Region;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class KDIteratorTest {

    Region surrounding;
    Region surroundingReversed;
    Region surrounded;
    Region alwaysOutsideNegative;
    Region alwaysOutsidePositive;
    Region oneToTwentyFive;
    Region twentySevenToFifty;
    Region oneToFiveHundred;
    Region oneToFiftyThousand;
    Region negativeCloseToZero;

    KDTree<KDComparable> tree;

    @Before
    public void setUp() {
        surrounding         = new Region(-1, -1, 1000000, 1000000);
        surroundingReversed = new Region(1000000, 1000000, -1, -1);
        surrounded          = new Region(1, 1, 1, 1);
        alwaysOutsideNegative = new Region(-100000, -100000, -10000, -10000);
        alwaysOutsidePositive = new Region(1000000, 1000000, 100000, 100000);
        oneToTwentyFive     = new Region(1, 1, 25, 25);
        twentySevenToFifty  = new Region(27, 27, 50, 50);
        oneToFiveHundred    = new Region(1, 1, 500, 500);
        oneToFiftyThousand  = new Region(1, 1, 50000, 50000);
        negativeCloseToZero = new Region(-2, -2, -1, -1);
    }

    @After
    public void tearDown() {
        tree = null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public KDTreeNode<KDComparable> makeKDTree(int dataSize, int coordsPerElement, float startLon, float startLat, int leafSize) {
        ArrayList<ParsedWay> list = new ArrayList<>();
        for(int i = 0; i < dataSize; i++) {
            ParsedWay way = new ParsedWay(i);
            Point2D.Float[] nodes = new Point2D.Float[coordsPerElement];
            for(int j = 0; j < coordsPerElement; j++) {
                nodes[j] = new Point2D.Float(startLon + i + j, startLat + i + j);
            }
            way.addNodes(nodes);
            list.add(way);
        }
        return new KDTreeNode(list, leafSize);
    }

	public int nextCount(KDTree<KDComparable> tree, Region region) {
        int count = 0;
        if(region == null) {
            count = tree.size();
        } else {
            for (Iterator<KDComparable> iter = tree.iterator(region); iter.hasNext(); ) {
                iter.next();
                count++;
            }
        }
        return count;
    }

    public KDComparable getElement(KDTree<KDComparable> tree, Region region) {
        for(Iterator<KDComparable> iter = tree.iterator(region); iter.hasNext();) return iter.next();
        return null;
    }

    public int nextCountWithoutRegion(KDTree<KDComparable> tree) {
        return nextCount(tree, null);
    }

    @Test
    public void testOneElement() {
        int size = 1;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals(size, nextCount(tree, surrounding));
        assertEquals(size, nextCount(tree, surroundingReversed));
        assertEquals(size, nextCount(tree, surrounded));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertEquals(0, nextCount(tree, alwaysOutsidePositive));
        assertEquals(size, nextCount(tree, oneToFiftyThousand));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testTwoElements() {
        int size = 2;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals(size, nextCount(tree, surrounding));
        assertEquals(size, nextCount(tree, surrounded));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testUnevenSmallAmountElements() {
        int size = 27;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals((size + 1) / 2, nextCount(tree, surrounded));
        assertEquals(size, nextCount(tree, surrounding));
        assertEquals(size,  nextCount(tree, oneToTwentyFive));
        assertEquals(size / 2, nextCount(tree, twentySevenToFifty));
        assertEquals(0,  nextCount(tree, alwaysOutsidePositive));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testUnevenLargerAmountElements() {
        int size = 51;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals((size + 1) / 4, nextCount(tree, surrounded));
        assertEquals((size + 1) / 2, nextCount(tree, oneToTwentyFive));
        assertEquals(size / 2, nextCount(tree, twentySevenToFifty));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testFiftyElements() {
        int size = 50;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals(size, nextCount(tree, surrounding));
        assertEquals(size, nextCount(tree, surroundingReversed));
        assertEquals((size + 1) / 2, nextCount(tree, surrounded));
        assertEquals(0,  nextCount(tree, alwaysOutsidePositive));
        assertEquals(size / 2, nextCount(tree, oneToTwentyFive));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testEightHundredElements() {
        int size = 800;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertEquals(25, nextCount(tree, twentySevenToFifty));
        assertEquals(500, nextCount(tree, oneToFiveHundred));
    }

    @Test
    public void testOneThousandElements() {
        int size = 1000;
        tree = makeKDTree(size, 1, 1, 1, 25);
        assertNotEquals(25, nextCount(tree, surrounded)); // halved recursively, 1000 does not end with uniform numbers of 25
        assertTrue(nextCount(tree, surrounded) < 25);
        assertTrue(nextCount(tree, surrounded) > 0);
        assertEquals(1000, nextCount(tree, surroundingReversed));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertEquals(500, nextCount(tree, oneToFiveHundred)); // halved once, 1000 still gives equal numbers
        assertEquals(1000, nextCountWithoutRegion(tree));
    }

    @Test
    public void testNegativeCoords() {
        int size = 1000;
        tree = makeKDTree(size, 1, -1000, -1000, 25);
        assertEquals(0, nextCount(tree, surrounded));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertTrue(nextCount(tree, negativeCloseToZero) <= 25 && nextCount(tree, negativeCloseToZero) > 0);
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testOneHundredThousandElements() {
        int size = 100__000;
        tree = makeKDTree(size, 1, 1, 1,25);
        assertEquals(25, nextCount(tree, surrounded));
        assertEquals(size, nextCount(tree, surrounding));
        assertEquals(size, nextCount(tree, surroundingReversed));
        assertEquals(size / 2, nextCount(tree, oneToFiftyThousand));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testLargerLeafSize() {
        int size = 1000;
        int leafSize = 500;
        tree = makeKDTree(size, 1, 1, 1, leafSize);
        assertEquals(size / 2, nextCount(tree, surrounded));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertEquals(0, nextCount(tree, alwaysOutsidePositive));
        assertEquals(size, nextCountWithoutRegion(tree));
    }

    @Test
    public void testMinimumLeafSize() {
        int size = 1000;
        int leafSize = 1;
        tree = makeKDTree(size, 1, 1, 1, leafSize);
        assertEquals(leafSize, nextCount(tree, surrounded));
        assertEquals(size, nextCountWithoutRegion(tree));

        Point2D.Float found = getElement(tree, surrounded).getFirstNode();
        Point2D.Float expected = new Point2D.Float(1, 1);
        Point2D.Float notExpected = new Point2D.Float(2, 2);
        assertEquals(expected, found);
        assertNotEquals(notExpected, found);
    }

    @Test
    public void testManyCoordsPerElement() {
        int size = 100;
        int coordSize = 100;
        tree = makeKDTree(size, coordSize, 1, 1, 25);
        assertEquals(size / 2, nextCount(tree, twentySevenToFifty));
        assertEquals(size / 4, nextCount(tree, surrounded));
        assertEquals(0, nextCount(tree, alwaysOutsideNegative));
        assertEquals(0, nextCount(tree, alwaysOutsidePositive));
        assertEquals(size, nextCountWithoutRegion(tree));
    }
    
    @Test
    public void testIsSortingByLon() {
    	int size = 100;
        int coordSize = 100;
        tree = makeKDTree(size, coordSize, 1, 1, 25);
        assertTrue(tree.isSortingByLon(4));
    }
}
