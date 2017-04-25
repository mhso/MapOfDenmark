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
		RouteVertex from = rc.makeVertex(55.658963f, 12.590327f);
		System.out.println(from.toString());
		RouteVertex tofrom = rc.makeVertex(55.659257f, 12.592892f);
		
		rc.addEdge(from, tofrom, 20, false, false, false, false, "Kaj Munks Vej");
		
		RouteVertex to = rc.makeVertex(55.659257f, 12.592892f);
		rc.addEdge(tofrom, to, 20, false, false, false, false, "Kaj Munks Vej");
		
		Iterable<RouteEdge> gr = rc.getRoute(from, to, WeightEnum.DISTANCE);
		for(RouteEdge e : gr){
			System.out.print(e.toString());
		}
		
		fail("Not yet implemented");
	}

}
