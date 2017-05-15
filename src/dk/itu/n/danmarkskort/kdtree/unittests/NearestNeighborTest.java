package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.Test;

import dk.itu.n.danmarkskort.address.Housenumber;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighborTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public KDTreeNode<KDComparable> makeKDTreeWithWays(int dataSize, int coordsPerElement, float startLon, float startLat, int leafSize) {
		List<ParsedWay> list = new ArrayList<>();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public KDTree<KDComparable> makeKDTreeWithPoints(int dataSize, float lon, float lat, int leafSize) {
		List<Housenumber> items = new ArrayList<>();
		for(int i = 0; i < dataSize; i++) {
			Housenumber housenumber = new Housenumber(null, null, null, new Point2D.Float(lon+i, lat+i));
			items.add(housenumber);
		}
		return new KDTreeNode(items, leafSize);
	}

    @Test
    public void testNullQuery() {
        KDTree<KDComparable> tree = makeKDTreeWithPoints(1000, 1, 1, 25);
        assertNull(tree.nearest(null));
    }

	@Test
	public void testWithPoints() {
		KDTree<KDComparable> tree = makeKDTreeWithPoints(1000, 1, 1, 25);
		Point2D.Float target1 = new Point2D.Float(1, 1);
		Point2D.Float query1 = target1;
		Point2D.Float target2 = new Point2D.Float(2, 2);
		//Point2D.Float query2 = target2;
		Point2D.Float target3 = new Point2D.Float(500, 500);
		//Point2D.Float query3 = target3;
		Point2D.Float target4 = new Point2D.Float(1000, 1000);
		Point2D.Float query4 = new Point2D.Float(2000, 2000);
        Point2D.Float query5 = new Point2D.Float(-10, -10);

        assertEquals(target1, tree.nearest(query1).getFirstNode());
		assertEquals(target1, tree.nearest(query5).getFirstNode());
		assertEquals(target2, tree.nearest(target2).getFirstNode());
		assertEquals(target3, tree.nearest(target3).getFirstNode());
		assertEquals(target4, tree.nearest(query4).getFirstNode());
		assertNotEquals(target3, tree.nearest(query4).getFirstNode());
		assertNotEquals(target3, tree.nearest(query1).getFirstNode());
	}

	@Test
	public void testWithPointsAndLargeLeafs() {
		KDTree<KDComparable> tree = makeKDTreeWithPoints(1000, 1, 1, 1000);
		Point2D.Float target1 = new Point2D.Float(1, 1);
		Point2D.Float query1 = target1;
        Point2D.Float target2 = new Point2D.Float(1000, 1000);
        Point2D.Float query2 = new Point2D.Float(2000, 2000);

		assertEquals(target1, tree.nearest(query1).getFirstNode());
        assertEquals(target2, tree.nearest(query2).getFirstNode());
        assertNotEquals(target1, tree.nearest(query2).getFirstNode());
	}

    @Test
    public void testWithPointsNegativeValues() {
        KDTree<KDComparable> tree = makeKDTreeWithPoints(1000, -1000, -1000, 25);
        Point2D.Float target1 = new Point2D.Float(-1, -1);
        Point2D.Float query1 = target1;
        Point2D.Float target2 = new Point2D.Float(-1000, -1000);
        Point2D.Float query2 = new Point2D.Float(-2000, -2000);

        assertEquals(target1, tree.nearest(query1).getFirstNode());
        assertEquals(target2, tree.nearest(query2).getFirstNode());
        assertNotEquals(target1, tree.nearest(query2).getFirstNode());
    }
}
