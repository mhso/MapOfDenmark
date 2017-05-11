package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.KDTreeLeaf;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Region;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class KDTreeLeafTest {

    public KDTreeLeaf<KDComparable> makeKDTreeLeaf(int dataSize, int coordsPerElement, float startLon, float startLat) {
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
        return new KDTreeLeaf(list);
    }

    @Test
    public void testGetItems() {
        int numItems = 10;
        KDTreeLeaf<KDComparable> leaf = makeKDTreeLeaf(numItems, 1, 1, 1);

        Region regionSurrounding = new Region(-10, -10, 20, 20);
        Region regionSurrounded = new Region(2, 2, 5, 5);
        Region regionLeftOf = new Region(-20, 1, -10, 10);
        Region regionTopOf = new Region(1, 30, 10, 20);
        Region regionRightOf = new Region(20, 1, 30, 10);
        Region regionBottomOf = new Region(10, -20, 1, -10);
        Region regionBarelyTouching = new Region(-10, -10, 1, 1);
        Region regionReversedSurrounding = new Region(20, 20, -10, -10);

        assertEquals(numItems, leaf.getAllItems().get(0).length);
        assertEquals(numItems, leaf.getItems(regionSurrounding).get(0).length);
        assertEquals(numItems, leaf.getItems(regionSurrounded).get(0).length);
        assertEquals(0, leaf.getItems(regionLeftOf).size());
        assertEquals(0, leaf.getItems(regionTopOf).size());
        assertEquals(0, leaf.getItems(regionRightOf).size());
        assertEquals(0, leaf.getItems(regionBottomOf).size());
        assertEquals(numItems, leaf.getItems(regionBarelyTouching).get(0).length);
        assertEquals(numItems, leaf.getItems(regionReversedSurrounding).get(0).length);
    }
}
