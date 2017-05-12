package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedWay;

import dk.itu.n.danmarkskort.models.Region;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

public class KDIteratorTest {

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
            for(KDComparable item: tree) count++;
        } else {
            for (Iterator<KDComparable> iter = tree.iterator(region); iter.hasNext(); ) {
                KDComparable item = iter.next();
                count++;
            }
        }
        return count;
    }

    @Test
    public void testWithRegions() {
        KDTree<KDComparable> oneElement =
                makeKDTree(1, 1, 1, 1, 25);
        KDTree<KDComparable> twoElements =
                makeKDTree(2, 1, 1, 1, 25);
        KDTree<KDComparable> fiftyElements =
                makeKDTree(50, 1, 1, 1, 50);
        KDTree<KDComparable> manyElements =
                makeKDTree(1000, 1, 1, 1, 25);
        KDTree<KDComparable> ExtremeElements =
                makeKDTree(100000, 1, 1, 1,25);

        Region surrounding = new Region(-1, -1, 100001, 100001);
        Region surroundingReversed = new Region(100001, 100001, -1, -1);
        Region surrounded = new Region(1, 1, 1, 1);
        Region alwaysOutside = new Region(-1000, -1000, -100, -100);
        Region alwaysOutside2 = new Region(1000000, 1000000, 1000001, 1000001);
        Region halfOfFifty = new Region(1, 1, 24, 24);
        Region halfOfMany = new Region(1, 1, 499, 499);
        Region halfOfExtreme = new Region(1, 1, 49999, 49999);

        // tests on tree with one element
        assertEquals(1, nextCount(oneElement, surrounding));
        assertEquals(1, nextCount(oneElement, surroundingReversed));
        assertEquals(1, nextCount(oneElement, surrounded));
        assertEquals(0, nextCount(oneElement, alwaysOutside));
        assertEquals(0, nextCount(oneElement, alwaysOutside2));
        assertEquals(1, nextCount(oneElement, halfOfExtreme));

        // tests on tree with two elements
        assertEquals(2, nextCount(twoElements, surrounding));
        assertEquals(1, nextCount(twoElements, surrounded));
        assertEquals(0, nextCount(twoElements, alwaysOutside));

        // tests on tree with fifty elements
        assertEquals(50, nextCount(fiftyElements, surrounding));
        assertEquals(25, nextCount(fiftyElements, surrounded));
        assertEquals(50, nextCount(fiftyElements, surroundingReversed));
        assertEquals(0, nextCount(fiftyElements, alwaysOutside));
        assertEquals(25, nextCount(fiftyElements, halfOfFifty));

    }
/*
    @Test
    public void testWithoutRegions() {

    }*/

}
