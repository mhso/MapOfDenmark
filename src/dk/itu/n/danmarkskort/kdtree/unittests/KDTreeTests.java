package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedItem;
import dk.itu.n.danmarkskort.models.ParsedPlace;
import dk.itu.n.danmarkskort.models.ParsedWay;
import dk.itu.n.danmarkskort.models.Region;

public class KDTreeTests {
	private final boolean DEBUG = true;
	
	public KDTree<ParsedItem> createKDTreeWithWays(int dataSize, int shiftValueX1, int shiftValueY1, 
			int shiftValueX2, int shiftValueY2, int kdSize) {
		List<ParsedItem> items = new ArrayList<>();
		for(int i = 0; i < dataSize; i++) {
			ParsedWay way = new ParsedWay(i);
			way.addNode(new Point2D.Float(i+shiftValueX1, i+shiftValueY1));
			way.addNode(new Point2D.Float(i+shiftValueX2, i+shiftValueY2));
			items.add(way);
		}
		return new KDTreeNode<>(items, kdSize);
	}

	public KDTree<ParsedPlace> createKDTreeWithPlace(int dataSize, float lon, float lat, int kdSize) {
		List<ParsedPlace> items = new ArrayList<>();
		for(int i = 0; i < dataSize; i++) {
			ParsedPlace place = new ParsedPlace("", lon+i, lat+i);
			items.add(place);
		}
		return new KDTreeNode<>(items, kdSize);
	}
	
	@Test
	public void testKDTreeNotNull() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(101, 0, 0, -10, -10, 25);
		if(DEBUG) System.out.println("Test not Null: " + kdTree);
		assertNotNull(kdTree);
	}
	
	@Test
	public void testNodeAmount() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(101, 0, 0, -10, -10, 25);
		int expected = 4;
		if(DEBUG) System.out.println("Test node amount, expected "+expected+", got " + kdTree.nodeSize());
		assertTrue(kdTree.nodeSize() == expected);
	}
	
	@Test
	public void testLeafAmount() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(101, 0, 0, -10, -10, 25);
		int expected = 5;
		if(DEBUG) System.out.println("Test leaf amount, expected "+expected+", got " + kdTree.leafSize());
		assertTrue(kdTree.leafSize() == expected);
	}
	
	@Test
	public void testNodeAmountWithSkewedValues() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(1, 0, 0, -10, -10, 25);
		int expected = 1;
		if(DEBUG) System.out.println("Test node amount, expected "+expected+", got " + kdTree.nodeSize());
		assertTrue(kdTree.nodeSize() == expected);
	}
	
	@Test
	public void testLeafAmountWithSkewedValues() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(2, 0, 0, -10, -10, 25);
		int expected = 2;
		if(DEBUG) System.out.println("Test leaf amount, expected "+expected+", got " + kdTree.leafSize());
		assertTrue(kdTree.leafSize() == expected);
	}
	
	@Test
	public void testNodeAmountAtDepth() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(150, 0, 0, -10, -10, 25);
		int level = 4;
		int expected = 8;
		if(DEBUG) System.out.println("Test node amount at level "+level+", expected "+expected+", got " + 
				kdTree.nodesAndLeafsAtDepth(level));
		assertTrue(kdTree.nodesAndLeafsAtDepth(level) == expected);
	}
	
	@Test
	public void testSize() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(100, 0, 0, -10, -10, 25);
		int expected = 100;
		if(DEBUG) System.out.println("Test tree size, expected "+expected+", got " + kdTree.size());
		assertTrue(kdTree.size() == expected);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKDSearchNullInput() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(100, 0, 0, -10, -10, 25);
		Iterator<ParsedItem> it = kdTree.iterator(null);
	}
	
	@Test
	public void testKDSearchOneElement() {
		KDTree<ParsedItem> itemTree = createKDTreeWithWays(1, 0, 0, -10, -10, 25);
		KDTree<ParsedPlace> placeTree = createKDTreeWithPlace(1, 0, 0, 25);

		ParsedItem item = null, item2 = null, nullItem = null;
		ParsedPlace place = null, place2 = null, nullPlace = null;

		if(DEBUG) System.out.print("Test search one element, tree size: " + itemTree.size() + ", data size: ");

        for(ParsedItem pt: itemTree) item = pt;
        for(Iterator<ParsedItem> iter = itemTree.iterator(new Region(-1, -1, 1, 1)); iter.hasNext(); ) item2 = iter.next();
		for(Iterator<ParsedItem> iter = itemTree.iterator(new Region(-30, -30, -20, -20)); iter.hasNext();) nullItem = iter.next();

		for(ParsedPlace pl: placeTree) place = pl;
        for(Iterator<ParsedPlace> iter = placeTree.iterator(new Region(-1, -1, 1, 1)); iter.hasNext();) place2 = iter.next();
        for(Iterator<ParsedPlace> iter = placeTree.iterator(new Region(40, 40 , 50, 50)); iter.hasNext();) nullPlace = iter.next();

		assertNotNull(item);
        assertNotNull(item2);
		assertNull(nullItem);

		assertNotNull(place);
		assertNotNull(place2);
		assertNull(nullPlace);
	}
	
	@Test
	public void testKDcreationEmptyData() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(0, 0, 0, -10, -10, 25);
		assertTrue(kdTree.size() == 0);
	}

	@Test
    public void testIterator() {

    }
}
