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
		RouteVertex A1 = rc.makeVertex(0f, 0f);
		RouteVertex A2 = rc.makeVertex(1f, 1f);
		RouteVertex A3 = rc.makeVertex(2f, 2f);
		RouteVertex A4 = rc.makeVertex(3f, 3f);
		RouteVertex A5 = rc.makeVertex(140f, 140f);
		RouteVertex A6 = rc.makeVertex(5f, 5f);
		
		RouteVertex B1 = rc.makeVertex(10f, 10f);
		RouteVertex B2 = rc.makeVertex(11f, 11f);
		RouteVertex B3 = rc.makeVertex(12f, 12f);
		
		RouteVertex C1 = rc.makeVertex(20f, 20f);
		RouteVertex C2 = rc.makeVertex(21f, 21f);
		
		//Connection 1 oneway
		rc.addEdge(A1, A2, 20, true, true, true, true, true, "A1<->A2");
		rc.addEdge(A2, A3, 20, true, true, true, true, true, "A2<->A3");
		rc.addEdge(A3, A4, 20, true, true, true, true, true, "A3<->A4");
		rc.addEdge(A4, A5, 20, true, true, true, true, true, "A4<->A5");
		rc.addEdge(A5, A6, 20, true, true, true, true, true, "A5<->A6");
		
		//Connection 2
		rc.addEdge(A4, B1, 20, true, true, true, false, false, "A4<->B1");
		rc.addEdge(B1, B2, 20, true, true, true, false, false, "B1<->B2");
		rc.addEdge(B2, B3, 20, true, true, false, false, true, "B2<->B3");
		
		rc.addEdge(A6, C1, 20, true, true, true, true, false, "A6<->C1");
		rc.addEdge(C1, C2, 20, true, false, true, true, false, "C1->C2");
		rc.addEdge(C2, A3, 20, true, true, true, true, false, "C2<->A3");
		
		rc.addEdge(B2, C1, 20, false, true, false, true, false, "B2<-C1");
		
		
//		printResult(A1, A6, "A1->A6, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(A1, A6, "A1->A6, SPEED_CAR", WeightEnum.SPEED_CAR);
//		
//		printResult(A1, B3, "A1->B3, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(B3, A1, "B3->A1, SPEED_CAR", WeightEnum.SPEED_CAR);
//		
//		printResult(C1, C2, "C1->C2, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(C1, C2, "C1->C2, SPEED_CAR", WeightEnum.SPEED_CAR);
//		printResult(C2, C1, "C2->C1, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(C2, C1, "C2->C1, SPEED_CAR", WeightEnum.SPEED_CAR);
//		
//		printResult(A6, B2, "A6->B2, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(A6, B2, "A6->B2, SPEED_CAR", WeightEnum.SPEED_CAR);
//		printResult(B2, A6, "B2->A6, DISTANCE_CAR", WeightEnum.DISTANCE_CAR);
//		printResult(B2, A6, "B2->A6, SPEED_CAR", WeightEnum.SPEED_CAR);
//		printResult(B2, A6, "B2->A6, SPEED_CAR", WeightEnum.SPEED_CAR);
		testAll(B2, A6, "B2->A6");
				
		fail("Not yet implemented");
	}

	private String testAll(RouteVertex A1, RouteVertex A6, String fromToStr){
		
		StringBuilder sb = new StringBuilder();
		sb.append("-- ROUTE " +fromToStr + " --");
		sb.append("\n---> ");
		sb.append("DISTANCE_CAR: " + getResult(A1, A6, fromToStr, WeightEnum.DISTANCE_CAR));
		sb.append("\n---> ");
		sb.append("SPEED_CAR: " + getResult(A1, A6, fromToStr, WeightEnum.SPEED_CAR));
		sb.append("\n---> ");
		sb.append("DISTANCE_BIKE: " + getResult(A1, A6, fromToStr, WeightEnum.DISTANCE_BIKE));
		sb.append("\n---> ");
		sb.append("DISTANCE_WALK: " + getResult(A1, A6, fromToStr, WeightEnum.DISTANCE_WALK));
		sb.append("\n");
		System.out.println(sb.toString());
		
		return sb.toString();
	}
	
	private String getResult(RouteVertex A1, RouteVertex A6, String fromToStr, WeightEnum weightEnum) {
		String result = null;
		if(rc.hasRoute(A1, A6, weightEnum)) {
			Iterable<RouteEdge> edges = rc.getRoute(A1, A6, weightEnum);
			//System.out.println("SHOW ME THE ROUTE: " + fromToStr);
			double distSum = 0;
			int count = 0;
			StringBuilder sb =  new StringBuilder();
			for(RouteEdge e : edges){
				distSum += e.getWeight(weightEnum);
				count++;
				sb.append(e.toStringDesr() + ", ");
				
				//System.out.print(e.toStringDesr() + ", ");
			}
			result = "Count: " + count + " Total dist: " + distSum + " | " + sb.toString();
				//System.out.println("\nCount: " + count + ", Distance: " + distSum + "\n");
		} else {
			result = "NO ROUTE";
			//System.out.println("NO ROUTE FOR: " + fromToStr);
		}
		return result;
	}
}