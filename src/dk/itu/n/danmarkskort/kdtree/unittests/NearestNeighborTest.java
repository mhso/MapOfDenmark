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

	public KDTreeNode<KDComparable> makeKDTreeWithWays(int dataSize, int coordsPerElement, float startLon, float startLat, int leafSize) {
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

	public KDTree<KDComparable> makeKDTreeWithHousenumbers(int dataSize, float lon, float lat, int leafSize) {
		List<Housenumber> items = new ArrayList<>();
		for(int i = 0; i < dataSize; i++) {
			Housenumber housenumber = new Housenumber(null, null, null, new Point2D.Float(lon+i, lat+i));
			items.add(housenumber);
		}
		return new KDTreeNode(items, leafSize);
	}

	@Test
	public void testPositiveValues() {
		KDTree<KDComparable> tree = makeKDTreeWithHousenumbers(1000, 1, 1, 25);
		Point2D.Float target = new Point2D.Float(1, 1);
		Point2D.Float target2 = new Point2D.Float(2, 2);
		Point2D.Float target3 = new Point2D.Float(500, 500);

		assertEquals(target, tree.nearest(target).getFirstNode());
		assertEquals(target2, tree.nearest(target2).getFirstNode());
		assertEquals(target3, tree.nearest(target3).getFirstNode());
		assertNotEquals(target3, tree.nearest(target).getFirstNode());
	}

	@Test
	public void testOnTreeWithLargeLeafs() {
		KDTree<KDComparable> tree = makeKDTreeWithHousenumbers(1000, 1, 1, 1000);
		Point2D.Float target = new Point2D.Float(1, 1);
		Point2D.Float target2 = new Point2D.Float(2, 2);
		Point2D.Float target3 = new Point2D.Float(500, 500);

		assertEquals(target, tree.nearest(target).getFirstNode());
		assertEquals(target2, tree.nearest(target2).getFirstNode());
		assertEquals(target3, tree.nearest(target3).getFirstNode());
		assertNotEquals(target3, tree.nearest(target).getFirstNode());
	}

    @Test
    public void testNegativeValues() {
        KDTree<KDComparable> tree = makeKDTreeWithHousenumbers(1000, -1000, -1000, 25);
        Point2D.Float target = new Point2D.Float(-1, -1);
        Point2D.Float target2 = new Point2D.Float(-2, -2);
        Point2D.Float target3 = new Point2D.Float(-500, -500);

        assertEquals(target, tree.nearest(target).getFirstNode());
        assertEquals(target2, tree.nearest(target2).getFirstNode());
        assertEquals(target3, tree.nearest(target3).getFirstNode());
        assertNotEquals(target3, tree.nearest(target).getFirstNode());
    }

	/*@Test
	public void testNearestItem() {
		AddressHolder addr = Main.addressController.getAddressHolder();
		KDTree<Housenumber> houseNumberTree = new KDTreeNode<>(addr.getHousenumbers());
		assertTrue(houseNumberTree.nearest(Util.stringCordsToPointFloat("55.65963, 12.59105")).getStreet().equals("Rued Langgaards Vej"));
	}*/

}
