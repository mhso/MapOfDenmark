package dk.itu.n.danmarkskort.kdtree.unittests;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.address.AddressHolder;
import dk.itu.n.danmarkskort.address.Housenumber;
import dk.itu.n.danmarkskort.kdtree.KDTree;
import dk.itu.n.danmarkskort.kdtree.KDTreeNode;

public class NearestNeighborTest {

	@Test
	public void testNearestItem() {
		AddressHolder addr = Main.addressController.getAddressHolder();
		KDTree<Housenumber> houseNumberTree = new KDTreeNode<>(addr.getHousenumbers());
		assertTrue(houseNumberTree.nearest(Util.stringCordsToPointFloat("55.65963, 12.59105")).getStreet().equals("Rued Langgaards Vej"));
	}

}
