package dk.itu.n.danmarkskort.routeplanner.unittest;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.models.UserPreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dk.itu.n.danmarkskort.routeplanner.RouteController;
import dk.itu.n.danmarkskort.routeplanner.RouteEdge;
import dk.itu.n.danmarkskort.routeplanner.RouteVertex;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;

public class RouteControllerTest {
	private RouteController rc;
	private UserPreferences userPrefs;
	private RouteVertex A1, A2, A3, A4, A5, A6, B1, B2, B3, C1, C2, C3;

	@Before
	public void setUp() {
		rc = new RouteController();
		userPrefs = new UserPreferences();
        Main.routeController = rc;
        Main.userPreferences = userPrefs;

        int i = 0;
        A1 = new RouteVertex(i++, new Point2D.Float(0f, 0f));
        A2 = new RouteVertex(i++, new Point2D.Float(1f, 1f));
        A3 = new RouteVertex(i++, new Point2D.Float(2f, 2f));
        A4 = new RouteVertex(i++, new Point2D.Float(3f, 3f));
        A5 = new RouteVertex(i++, new Point2D.Float(140f, 140f));
        A6 = new RouteVertex(i++, new Point2D.Float(5f, 5f));

        B1 = new RouteVertex(i++, new Point2D.Float(10f, 10f));
        B2 = new RouteVertex(i++, new Point2D.Float(11f, 11f));
        B3 = new RouteVertex(i++, new Point2D.Float(12f, 12f));

        C1 = new RouteVertex(i++, new Point2D.Float(20f, 20f));
        C2 = new RouteVertex(i++, new Point2D.Float(21f, 21f));
        C3 = new RouteVertex(i, new Point2D.Float(-20f, -40f));

        //addEdge(from, to, maxSpeed, forwardAllowed, backwardAllowed, carsAllowed, bikesAllowed, walkAllowed, name)

        //Connection 1 oneway
        rc.addEdge(A1, A2, (short)20, true, true, true, true, true, "A1<->A2");
        rc.addEdge(A2, A3, (short)20, true, true, true, true, true, "A2<->A3");
        rc.addEdge(A3, A4, (short)20, true, true, true, true, true, "A3<->A4");
        rc.addEdge(A4, A5, (short)20, true, true, true, true, true, "A4<->A5");
        rc.addEdge(A5, A6, (short)20, true, true, true, true, true, "A5<->A6");

        //Connection 2
        rc.addEdge(A4, B1, (short)20, true, true, true, false, false, "A4<->B1");
        rc.addEdge(B1, B2, (short)20, true, true, true, false, false, "B1<->B2");
        rc.addEdge(B2, B3, (short)20, true, true, false, false, true, "B2<->B3");

        rc.addEdge(A6, C1, (short)20, true, true, true, true, false, "A6<->C1");
        rc.addEdge(C1, C2, (short)20, true, false, true, true, false, "C1->C2");
        rc.addEdge(C2, A3, (short)20, true, true, true, true, false, "C2<->A3");

        rc.addEdge(B2, C1, (short)20, true, true, false, true, false, "B2<->C1");

        rc.addEdge(C2, C3, (short)20, true, false, true, true, true, "C2->C3");

        rc.makeGraph();
    }

	@After
	public void tearDown() {
        rc = null;
        userPrefs = null;
        Main.routeController = null;
        Main.userPreferences = null;
	}

    private boolean routePossible(RouteVertex from, RouteVertex to, WeightEnum type) {
        return rc.getRoute(from, to, type) != null;
    }

    private ArrayList<RouteEdge> getRouteAsList(RouteVertex from, RouteVertex to, WeightEnum type) {
        Iterable<RouteEdge> iter = rc.getRoute(from, to, type);
        ArrayList<RouteEdge> list = new ArrayList<>();
        for(RouteEdge edge: iter) list.add(edge);
        return list;
    }

	@Test
	public void testRouteAllowedForAll() {
        assertTrue(routePossible(A1, A2, WeightEnum.DISTANCE_BIKE));
        assertTrue(routePossible(A1, A2, WeightEnum.DISTANCE_CAR));
        assertTrue(routePossible(A1, A2, WeightEnum.DISTANCE_WALK));
        assertTrue(routePossible(A1, A2, WeightEnum.SPEED_CAR));
    }

    @Test
    public void testRouteDisallowedForAll() {
        assertFalse(routePossible(A1, B3, WeightEnum.DISTANCE_CAR));
        assertFalse(routePossible(A1, B3, WeightEnum.SPEED_CAR));
        assertFalse(routePossible(A1, B3, WeightEnum.DISTANCE_WALK));
        assertFalse(routePossible(A1, B3, WeightEnum.DISTANCE_BIKE));
    }

    @Test
    public void testRouteAllowedForSome() {
        assertTrue(routePossible(A1, B2, WeightEnum.DISTANCE_CAR));
        assertTrue(routePossible(A1, B2, WeightEnum.SPEED_CAR));
        assertTrue(routePossible(A1, B2, WeightEnum.DISTANCE_BIKE));
        assertFalse(routePossible(A1, B2, WeightEnum.DISTANCE_WALK));
    }

    @Test
    public void testForwardAllowedBackwardDisallowed() {
        assertTrue(routePossible(A1, C3, WeightEnum.SPEED_CAR));
        assertFalse(routePossible(C3, A1, WeightEnum.SPEED_CAR));
    }

    @Test
    public void testDifferentRoutes() {
        ArrayList<RouteEdge> routeCar = getRouteAsList(A1, B2, WeightEnum.DISTANCE_CAR);
        ArrayList<RouteEdge> routeBike = getRouteAsList(A1, B2, WeightEnum.DISTANCE_BIKE);
        boolean same = true;
        if(routeCar.size() > routeBike.size()) {
            for(int i = 0; i < routeCar.size(); i++) {
                if(routeCar.get(i) != routeBike.get(i)) {
                    same = false;
                    break;
                }
            }
        } else {
            for(int i = 0; i < routeBike.size(); i++) {
                if(routeCar.get(i) != routeBike.get(i)) {
                    same = false;
                    break;
                }
            }
        }
        assertFalse(same);
    }

    @Test
    public void testSameRouteWithWithoutAStar() {
        ArrayList<RouteEdge> routeWith = getRouteAsList(A1, C3, WeightEnum.DISTANCE_CAR);
        userPrefs.setUseDjikstraWithAStar(false);
        ArrayList<RouteEdge> routeWithout = getRouteAsList(A1, C3, WeightEnum.DISTANCE_CAR);

        for(int i = 0; i < routeWith.size(); i++) {
            assertEquals(routeWithout.get(i), routeWith.get(i));
        }
    }

    @Test
    public void testDifferentRelaxCountWithWithoutAStar() {
	    int relaxCountWith = rc.getRelaxCount(A1, C3, WeightEnum.SPEED_CAR);
	    userPrefs.setUseDjikstraWithAStar(false);
	    int relaxCountWithout = rc.getRelaxCount(A1, C3, WeightEnum.SPEED_CAR);

	    assertNotEquals(relaxCountWith, relaxCountWithout);
    }
}