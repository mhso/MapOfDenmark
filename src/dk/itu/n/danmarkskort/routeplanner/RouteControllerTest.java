package dk.itu.n.danmarkskort.routeplanner;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RouteControllerTest {
	RouteController rc;
	@Before
	public void setUp() throws Exception {
		rc = new RouteController();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		RouteVertex A1 = rc.makeVertex(100f, 1f);
		RouteVertex A2 = rc.makeVertex(110f, 11f);
		RouteVertex A3 = rc.makeVertex(120f, 12f);
		RouteVertex A4 = rc.makeVertex(130f, 13f);
		RouteVertex A5 = rc.makeVertex(140f, 14f);
		RouteVertex A6 = rc.makeVertex(150f, 15f);
		
		RouteVertex B1 = rc.makeVertex(200f, 20f);
		RouteVertex B2 = rc.makeVertex(210f, 21f);
		RouteVertex B3 = rc.makeVertex(220f, 22f);
		
		//Connection 1 oneway
		rc.addEdge(A1, A2, 20, true, true, false, false, "A1, A2");
		rc.addEdge(A2, A3, 20, true, true, false, false, "A2, A3");
		rc.addEdge(A3, A4, 20, true, true, false, false, "A3, A4");
		rc.addEdge(A4, A5, 20, true, true, false, false, "A4, A5");
		rc.addEdge(A5, A6, 20, true, true, false, false, "A5, A6");
		
		//Connection 2
		rc.addEdge(A4, B1, 20, true, true, false, false, "A4, B1");
		rc.addEdge(B1, B2, 20, true, true, false, false, "B1, B2");
		rc.addEdge(B2, B3, 20, true, true, false, false, "B2, B3");
		
		rc.addEdge(B3, A5, 20, true, true, false, false, "B3,A5");
		
		
		printResult(A1, A6, "A1->A6, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
		printResult(A1, A6, "A1->A6, SPEED_CAR", WeightEnum.SPEED_CAR);
				
		fail("Not yet implemented");
	}

	private void printResult(RouteVertex A1, RouteVertex A6, String fromToStr, WeightEnum weightEnum) {
		Iterable<RouteEdge> result = rc.getRoute(A1, A6, weightEnum);
		System.out.println("SHOW ME THE ROUTE " + fromToStr);
		double distSum = 0;
		for(RouteEdge e : result){
			distSum += e.getWeight(WeightEnum.DISTANCE_CAR);
			System.out.println(e.toStringDesr());
		}
		System.out.println(fromToStr + " dist: " + distSum);
	}

}
