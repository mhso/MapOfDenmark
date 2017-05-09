package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import dk.itu.n.danmarkskort.DKConstants;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;
import dk.itu.n.danmarkskort.models.ParsedItem;
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
				kdTree.nodeSizeAt(level));
		assertTrue(kdTree.nodeSizeAt(level) == expected);
	}
	
	@Test
	public void testSize() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(100, 0, 0, -10, -10, 25);
		int expected = 100;
		if(DEBUG) System.out.println("Test tree size, expected "+expected+", got " + kdTree.size());
		assertTrue(kdTree.size() == expected);
	}
	
	@Test
	public void testKDSearchNullInput() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(100, 0, 0, -10, -10, 25);
		Iterator<ParsedItem> it = kdTree.iterator(null);
		assertNotNull(it);
	}
	
	@Test
	public void testKDcreationEmptyData() {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(0, 0, 0, -10, -10, 25);
		assertTrue(kdTree.size() == 0);
	}
	
	@Test
	public void testKDSearchLeafSize5000() {
		long timePassed = testKDSearchLeafSize(5000);
		if(DEBUG) System.out.println("Test Tree Search, KDSize " + DKConstants.KD_SIZE + ": " + 
		timePassed + " ms.");
		assertTrue(timePassed < 10);
	}
	
	@Test
	public void testKDSearchLeafSize50() {
		long timePassed = testKDSearchLeafSize(50);
		if(DEBUG) System.out.println("Test Tree Search, KDSize " + DKConstants.KD_SIZE + ": " + 
		timePassed + " ms.");
		assertTrue(timePassed < 10);
	}
	
	private long testKDSearchLeafSize(int leafSize) {
		KDTree<ParsedItem> kdTree = createKDTreeWithWays(1_000_000, 10, 10, 0, 0, leafSize);
		long beforeGet = System.currentTimeMillis();
		int itemAmount = 0;
		for (Iterator<ParsedItem> i = kdTree.iterator(new Region(5000, 5000, 5500, 5500)); i.hasNext(); ) {
			ParsedItem item = i.next();
			item.nodesToCoords();
			item.getShape();
			itemAmount++;
		}
		System.out.println("Items: " + itemAmount);
		return System.currentTimeMillis()-beforeGet;
	}
}
