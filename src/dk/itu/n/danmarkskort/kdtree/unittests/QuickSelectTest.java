package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.kdtree.KDComparable;
import dk.itu.n.danmarkskort.kdtree.QuickSelect;
import dk.itu.n.danmarkskort.models.ParsedWay;

public class QuickSelectTest {
	private KDComparable[] arr;
	
	@Before
	public void setUp() {
		ParsedWay way1 = new ParsedWay();
		way1.addNode(new Point2D.Float(1, 0));
		way1.addNode(new Point2D.Float(2, 0));
		ParsedWay way2 = new ParsedWay();
		way2.addNode(new Point2D.Float(3, 0));
		way2.addNode(new Point2D.Float(4, 0));
		ParsedWay way3 = new ParsedWay();
		way3.addNode(new Point2D.Float(5, 0));
		way3.addNode(new Point2D.Float(6, 0));
		ParsedWay way4 = new ParsedWay();
		way4.addNode(new Point2D.Float(7, 0));
		way4.addNode(new Point2D.Float(8, 0));
		
		arr = new KDComparable[]{way4, way2, way3, way1};
	}
	
	@Test
	public void testQuickSelectMedian() {
		assertTrue(QuickSelect.quickSelect(arr, arr.length / 2, true).getFirstNode().x == 5);
	}

	@Test
	public void testLowerHalf() {
		QuickSelect.quickSelect(arr, arr.length / 2, true);
		
		boolean lower = true;
		for(int i = 0; i < arr.length / 2; i++)
			if(arr[i].getFirstNode().x >= arr[arr.length/2].getFirstNode().x)
				lower = false;
		assertTrue(lower);
	}
	
	@Test
	public void testUpperHalf() {
		QuickSelect.quickSelect(arr, arr.length / 2, true);
		
		boolean higher = true;
		for(int i = (arr.length + 1) / 2; i < arr.length; i++)
			if(arr[i].getFirstNode().x < arr[arr.length/2].getFirstNode().x)
				higher = false;
		assertTrue(higher);
	}
}
