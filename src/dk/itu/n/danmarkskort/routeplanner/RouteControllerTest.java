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
		rc.addEdge(A1, A2, 20, false, false, false, false, "A1, A2");
		rc.addEdge(A2, A3, 20, false, false, false, false, "A2, A3");
		rc.addEdge(A3, A4, 20, false, false, false, false, "A3, A4");
		rc.addEdge(A4, A5, 20, false, false, false, false, "A4, A5");
		rc.addEdge(A5, A6, 20, false, false, false, false, "A5, A6");
		
		rc.addEdge(A6, A5, 20, false, false, false, false, "A6, A5");
		rc.addEdge(A5, A4, 20, false, false, false, false, "A5, A4");
		rc.addEdge(A4, A3, 20, false, false, false, false, "A4, A3");
		rc.addEdge(A3, A2, 20, false, false, false, false, "A3, A2");
		rc.addEdge(A2, A1, 20, false, false, false, false, "A2, A1");
		
//		//Connection 2
//		rc.addEdge(A4, B1, 20, false, false, false, false, "A4, B1");
//		rc.addEdge(B1, B2, 20, false, false, false, false, "B1, B2");
//		rc.addEdge(B2, B3, 20, false, false, false, false, "B2, B3");
//		
//		//Connection 3
//		rc.addEdge(B3, B2, 20, false, false, false, false, "B3, B2");
//		rc.addEdge(B1, B3, 20, false, false, false, false, "B1, B3");
//		rc.addEdge(B3, A4, 20, false, false, false, false, "B3, A4");
//		rc.addEdge(B3, A6, 20, false, false, false, false, "B3, A6");
//		rc.addEdge(A6, B2, 20, false, false, false, false, "A6, B2");
		
		Iterable<RouteEdge> A1_A6 = rc.getRoute(A1, A6, WeightEnum.DISTANCE);
		System.out.println("SHOW ME THE ROUTE A1 -> A6");
		for(RouteEdge e : A1_A6){
			System.out.println(e.toStringDesr());
		}
		
		Iterable<RouteEdge> A6_A1 = rc.getRoute(A6, A1, WeightEnum.DISTANCE);
		System.out.println("SHOW ME THE ROUTE A6 -> A1");
		for(RouteEdge e : A6_A1){
			System.out.println(e.toStringDesr());
		}
		
//		Iterable<RouteEdge> A1_B3 = rc.getRoute(A1, B3, WeightEnum.DISTANCE);
//		System.out.println("SHOW ME THE ROUTE A1 -> B3");
//		for(RouteEdge e : A1_B3){
//			System.out.println(e.toStringDesr());
//		}
//		
//		Iterable<RouteEdge> A2_A6 = rc.getRoute(A2, A6, WeightEnum.DISTANCE);
//		System.out.println("SHOW ME THE ROUTE A2 -> A6");
//		for(RouteEdge e : A2_A6){
//			System.out.println(e.toStringDesr());
//		}
//		
//		Iterable<RouteEdge> A5_A6 = rc.getRoute(A5, A6, WeightEnum.DISTANCE);
//		System.out.println("SHOW ME THE ROUTE A5 -> A6");
//		for(RouteEdge e : A5_A6){
//			System.out.println(e.toStringDesr());
//		}
//		
//		Iterable<RouteEdge> B3_A1 = rc.getRoute(B3, A1, WeightEnum.DISTANCE);
//		System.out.println("SHOW ME THE ROUTE B3 -> A1");
//		for(RouteEdge e : B3_A1){
//			System.out.println(e.toStringDesr());
//		}
//		
//		Iterable<RouteEdge> B3_A6 = rc.getRoute(B3, A6, WeightEnum.DISTANCE);
//		System.out.println("SHOW ME THE ROUTE B3 -> A1");
//		for(RouteEdge e : B3_A6){
//			System.out.println(e.toStringDesr());
//		}
		
		fail("Not yet implemented");
	}

}
