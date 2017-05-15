package dk.itu.n.danmarkskort.models.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

public class ParsedWayTest {

    ParsedWay way;

    @Before
    public void setUp() {
        way = new ParsedWay();
    }

    @After
    public void tearDown() {
        way = null;
    }

    @Test
    public void testGetReversedNodes() {
        int numNodes = 20;
        for(int i = 0; i < numNodes; i++) {
            way.addNode(new Point2D.Float(i, i));
        }

        Point2D.Float[] normalNodes = way.getNodes();
        Point2D.Float[] revNodes = way.getReversedNodes();

        int k = numNodes;
        for(Point2D.Float revNode : revNodes) {
            assertEquals(--k, revNode.x, 0f);
            assertEquals(k, revNode.y, 0f);
        }

        for(int i = 0; i < normalNodes.length; i++) {
            assertEquals(i, normalNodes[i].x, 0f);
            assertEquals(i, normalNodes[i].x, 0f);
        }
    }
}
