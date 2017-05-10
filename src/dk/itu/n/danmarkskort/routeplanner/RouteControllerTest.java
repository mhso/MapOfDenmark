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
		
		
		System.out.println(testAll(A1, A6, "A1->A6"));
		System.out.println(testAll(A6, A1, "A1->A6"));
				
		System.out.println(testAll(A1, B3, "A1->B3"));
		System.out.println(testAll(B3, A1, "B3->A1"));
				
		System.out.println(testAll(C1, C2, "C1->C2"));
		System.out.println(testAll(C2, C1, "C2->C1"));
				
		System.out.println(testAll(A6, B2, "A6->B2"));
		System.out.println(testAll(B2, A6, "B2->A6"));
				
		fail("Not yet implemented");
	}

	private String testAll(RouteVertex from, RouteVertex to, String fromToStr){
		
		String DISTANCE_CAR = getResult(from, to, fromToStr, WeightEnum.DISTANCE_CAR);
		String SPEED_CAR = getResult(from, to, fromToStr, WeightEnum.SPEED_CAR);
		String DISTANCE_BIKE = getResult(from, to, fromToStr, WeightEnum.DISTANCE_BIKE);
		String DISTANCE_WALK = getResult(from, to, fromToStr, WeightEnum.DISTANCE_WALK);
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n-- ROUTE " +fromToStr + " --");
		sb.append("\n---> ");
		sb.append("DISTANCE_CAR: " + DISTANCE_CAR);
		sb.append("\n---> ");
		sb.append("SPEED_CAR: " + SPEED_CAR);
		sb.append("\n---> ");
		sb.append("DISTANCE_BIKE: " + DISTANCE_BIKE);
		sb.append("\n---> ");
		sb.append("DISTANCE_WALK: " + DISTANCE_WALK);
		sb.append("\n");
		System.out.println(sb.toString());
		
		
		return strToBool(DISTANCE_CAR) + ", " + strToBool(SPEED_CAR) + ", " + strToBool(DISTANCE_BIKE) + ", " + strToBool(DISTANCE_WALK);
	}
	
	private boolean strToBool(String input){
		if(input.equals("NO ROUTE")) return false;
		return true;
	}
	
	private String getResult(RouteVertex A1, RouteVertex A6, String fromToStr, WeightEnum weightEnum) {
		String result = null;
		if(rc.hasRoute(A1, A6, weightEnum)) {
			Iterable<RouteEdge> edges = rc.getRoute(A1, A6, weightEnum);
			double distSum = 0;
			int count = 0;
			StringBuilder sb =  new StringBuilder();
			for(RouteEdge e : edges){
				distSum += e.getWeight(weightEnum);
				count++;
				sb.append(e.toStringDesr() + ", ");
			}
			result = "Count: " + count + " Total dist: " + distSum + " | " + sb.toString();
		} else {
			result = "NO ROUTE";
		}
		return result;
	}
}