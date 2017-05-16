package dk.itu.n.danmarkskort.backend.unittests;

import dk.itu.n.danmarkskort.backend.NodeMap;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.models.ParsedNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

public class NodeMapTest {

    NodeMap map;
    long[] ids;

    private boolean noDuplicates(long[] keys) {
        for(int i = 0; i < keys.length; i++) {
            for(int j = i + 1; j < keys.length; j++ ) {
                int hash1 = map.getHash(keys[i]);
                int hash2 = map.getHash(keys[j]);
                if(hash1 == hash2) return false;
            }
        }
        return true;
    }

    @Before
    public void setUp() {
        map = new NodeMap();
        ids = new long[]{1,2,3,4,5,6,-1,-100};
        for(long id1 : ids) map.put(id1, id1, id1);
    }

    @After
    public void tearDown() {
        map = null;
        ids = null;
    }

    @Test
    public void testGet() {
        for(long id: ids) assertNotNull(map.get(id));
        assertNull(map.get(-9999));
    }

    @Test
    public void testPut() {
        // test that the right values have been set and can be returned
        for(int i = 0; i < ids.length; i++) {
            ParsedNode node = map.get(ids[i]);
            assertEquals(ids[i], node.getKey(), 0f);
            assertEquals(ids[i], node.getLon(), 0f);
            assertEquals(ids[i], node.getLat(), 0f);

            if(i < ids.length - 2) assertNotEquals(ids[i + 1], node.getKey(), 0f);
        }
    }

    @Test
    public void testGetHash() {
        // testing that on smaller ranges, getHash doesn't return same hash
        long[] thousand = new long[1000];
        long[] tenThousand = new long[10_000];

        for(int i = 0; i < thousand.length; i++) thousand[i] = i;
        for(int i = 0; i < tenThousand.length; i++) tenThousand[i] = i;

        assertTrue(noDuplicates(ids));
        assertTrue(noDuplicates(thousand));
        assertTrue(noDuplicates(tenThousand));
    }
}
