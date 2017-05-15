package dk.itu.n.danmarkskort.backend.unittests;

import static org.junit.Assert.*;

import dk.itu.n.danmarkskort.backend.ParserUtil;
import dk.itu.n.danmarkskort.models.ParsedWay;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashMap;

public class ParserUtilTest {

    private HashMap<Point2D.Float, ParsedWay> makeCoastlineMap(int size, int separation, int startValue) {
        HashMap<Point2D.Float, ParsedWay> map = new HashMap<>();
        for(int i = 0; i < size; i++) {
            int k = startValue + (i * separation);
            Point2D.Float point1 = new Point2D.Float(k, k);
            Point2D.Float point2 = new Point2D.Float(k + 1, k + 1);
            ParsedWay way = new ParsedWay();
            way.addNode(point1);
            way.addNode(point2);
            ParserUtil.connectCoastline(map, way);
        }
        return map;
    }

    @Test
    public void testConnectCoastline() {
        int value1 = makeCoastlineMap(1000, 0, 0).size();
        int expected1 = 2; // The same ParsedWay appears twice in the HashMap, under different keys

        int value2 = makeCoastlineMap(1000, 10, 0).size();
        int expected2 = 2000;

        int value3 = makeCoastlineMap(333, 0, -20).size();
        int expected3 = 2;

        assertEquals(expected1, value1);
        assertEquals(expected2, value2);
        assertEquals(expected3, value3);
    }
}
