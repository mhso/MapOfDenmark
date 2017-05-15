package dk.itu.n.danmarkskort.models.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.models.ParsedRelation;
import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

public class ParsedRelationTest {

    private ParsedRelation relation;
    String roleNothing = "nothing";
    String roleInner = "inner";
    String roleOuter = "outer";

    public void addOuterWays(int numWays, boolean allCorrect) {
        for(int i = 0; i < numWays; i++) {
            ParsedWay way = new ParsedWay();
            relation.addMember(way, roleOuter);
            Point2D.Float point1, point2;
            if(!allCorrect && i % 2 == 0) {
                point1 = new Point2D.Float(i + 1, i + 1);
                point2 = new Point2D.Float(i, i);
            }
            else {
                point1 = new Point2D.Float(i, i);
                point2 = new Point2D.Float(i + 1, i + 1);
            }
            way.addNode(point1);
            way.addNode(point2);
        }
    }

    @Before
    public void setUp() {
        relation = new ParsedRelation(1);
    }

    @After
    public void tearDown() {
        relation = null;
    }

    @Test
    public void testAddMember() {
        ParsedWay way = new ParsedWay();

        assertEquals(0, relation.getInners().size());
        assertEquals(0, relation.getOuters().size());

        relation.addMember(way, roleNothing);
        relation.addMember(way, roleInner);
        relation.addMember(way, roleOuter);

        assertEquals(2, relation.getInners().size());
        assertEquals(1, relation.getOuters().size());
    }

    @Test
    public void testDeleteOldRefs() {
        int numWays = 20;
        int numCoords = 20;

        assertEquals(0, relation.size());

        for(int i = 0; i < numWays; i++) {
            ParsedWay way = new ParsedWay();
            String role = i % 2 == 0 ? roleInner : roleOuter;
            relation.addMember(way, role);
            for(int j = 0; j < numCoords; j++) {
                way.addNode(new Point2D.Float(i, j));
            }
        }
        assertEquals(numWays * numCoords, relation.size());

        relation.deleteOldRefs();
        assertEquals(0, relation.size());
    }

    @Test
    public void testWithCorrectOuters() {
        int numWays = 20;

        addOuterWays(numWays, true);
        assertEquals(numWays, relation.getOuters().size());
        assertEquals(numWays * 2, relation.size());

        relation.correctOuters();
        assertEquals(numWays, relation.getOuters().size());
    }

    @Test
    public void testWithIncorrectOuters() {
        int numWays1 = 30;
        int numWays2 = 31;

        addOuterWays(numWays1, false);
        assertEquals(numWays1, relation.getOuters().size());

        relation.correctOuters();
        assertEquals(1, relation.getOuters().size());

        relation = new ParsedRelation(1);
        addOuterWays(numWays2, false);
        assertEquals(numWays2, relation.getOuters().size());

        relation.correctOuters();
        assertEquals(1, relation.getOuters().size());
    }
}
