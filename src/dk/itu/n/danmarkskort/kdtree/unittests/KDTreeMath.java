package dk.itu.n.danmarkskort.kdtree.unittests;

import dk.itu.n.danmarkskort.kdtree.KDTree;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class KDTreeMath {

    @Test
    public void testCalcDistance() {
        Point2D.Float point1 = new Point2D.Float(0f, 0f);
        float difference1 = 5f;
        Point2D.Float point2 = new Point2D.Float(0, difference1);
        Point2D.Float point2_2 = new Point2D.Float(difference1, difference1);

        float start = -10f;
        Point2D.Float point3 = new Point2D.Float(start, start);
        float difference2 = 30f;
        Point2D.Float point4 = new Point2D.Float(start + difference2, start);
        float difference4 = -5f;
        Point2D.Float point5 = new Point2D.Float(start, difference4);

        assertEquals(difference1, KDTree.calcDistance(point1, point2), 0f);
        assertNotEquals(difference1, KDTree.calcDistance(point1, point2_2), 0f);
        assertEquals(difference1, KDTree.calcDistance(point2, point1), 0f);

        assertEquals(difference2, KDTree.calcDistance(point3, point4), 0f);
        assertNotEquals(difference4, KDTree.calcDistance(point3, point5), 0f);
        assertEquals(Math.abs(difference4), KDTree.calcDistance(point3, point5), 0f);
    }

    @Test
    public void testDistancePointToLine() {
        Point2D.Float query1 = new Point2D.Float(0, 0);
        float difference1 = 10f;
        Point2D.Float line1a = new Point2D.Float(difference1 * 2, difference1 * 2);
        Point2D.Float line1b = new Point2D.Float(0, difference1);

        assertEquals(difference1, KDTree.distancePointToLine(line1a, line1b, query1), 0f);
        assertEquals(difference1, KDTree.distancePointToLine(line1b, line1a, query1), 0f);
        assertNotEquals(difference1, KDTree.distancePointToLine(query1, line1b, line1a), 0f);

        float start = -10f;
        Point2D.Float query2 = new Point2D.Float(start, start);
        float difference2 = 5f;
        Point2D.Float line2a = new Point2D.Float(start + difference2, 99f);
        Point2D.Float line2b = new Point2D.Float(start + difference2, -13f);

        assertEquals(difference2, KDTree.distancePointToLine(line2a, line2b, query2), 0f);
        assertEquals(difference2, KDTree.distancePointToLine(line2b, line2a, query2), 0f);
        assertNotEquals(difference2, KDTree.distancePointToLine(query2, line2b, line2a), 0f);
    }

    @Test
    public void testShortestDistance() {
        Point2D.Float query1 = new Point2D.Float(-1, -1);
        float[] horizontal = new float[]{-10, 4, -8, 4, -4, 4, 20, 4};
        float[] vertical = new float[]{5, 20, 5, 10, 5, 0, 5, -33};
        float distanceToHorizontal = 5;
        float distanceToVertical = 6;

        assertEquals(distanceToHorizontal, KDTree.shortestDistance(query1, horizontal), 0f);
        assertEquals(distanceToVertical, KDTree.shortestDistance(query1, vertical), 0f);
    }
}
