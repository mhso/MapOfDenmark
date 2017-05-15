package dk.itu.n.danmarkskort.routeplanner.unittest;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

import org.junit.Test;

import dk.itu.n.danmarkskort.Main;
import dk.itu.n.danmarkskort.Util;
import dk.itu.n.danmarkskort.models.ParsedData;
import dk.itu.n.danmarkskort.routeplanner.RouteEdge;
import dk.itu.n.danmarkskort.routeplanner.RouteEdgeMeta;
import dk.itu.n.danmarkskort.routeplanner.WeightEnum;

public class RouteEdgeTest {
	public RouteEdge getTestEdge() {
		RouteEdge edge = new RouteEdge(new Point2D.Float(0, 0), new Point2D.Float(2, 0), 
				new RouteEdgeMeta(50, true, true, true, true, true), "A road");
		Main.model = new ParsedData();
		Main.model.setLonFactor(0.4f);
		return edge;
	}
	
	@Test
	public void testGetWeightBike() {
		RouteEdge edge = getTestEdge();
		double dist = Util.distanceInMeters(Util.toRealCoords(new Point2D.Float(0, 0)), 
				Util.toRealCoords(new Point2D.Float(2, 0)));
		assertTrue(edge.getWeight(WeightEnum.DISTANCE_BIKE) == dist);
	}

	@Test
	public void testGetWeightCarSpeed() {
		RouteEdge edge = getTestEdge();
		double dist = Util.distanceInMeters(Util.toRealCoords(new Point2D.Float(0, 0)), 
				Util.toRealCoords(new Point2D.Float(2, 0)));
		double speed = dist/(double)edge.getMaxSpeed();
		assertTrue(edge.getWeight(WeightEnum.SPEED_CAR) == speed);
	}
	
	@Test
	public void testGetWeightCarDistance() {
		RouteEdge edge = getTestEdge();
		double dist = Util.distanceInMeters(Util.toRealCoords(new Point2D.Float(0, 0)), 
				Util.toRealCoords(new Point2D.Float(2, 0)));
		assertTrue(edge.getWeight(WeightEnum.DISTANCE_CAR) == dist);
	}
	
	@Test
	public void testWeightWalk() {
		RouteEdge edge = getTestEdge();
		double dist = Util.distanceInMeters(Util.toRealCoords(new Point2D.Float(0, 0)), 
				Util.toRealCoords(new Point2D.Float(2, 0)));
		assertTrue(edge.getWeight(WeightEnum.DISTANCE_WALK) == dist);
	}
	
	@Test
	public void testTravelTypeCar() {
		RouteEdge edge = new RouteEdge(new Point2D.Float(0, 0), new Point2D.Float(2, 0), 
				new RouteEdgeMeta(50, true, true, true, false, false), "Car only road");
		assertTrue(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR));
		assertTrue(edge.isTravelTypeAllowed(WeightEnum.SPEED_CAR));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK));
	}
	
	@Test
	public void testTravelTypeBike() {
		RouteEdge edge = new RouteEdge(new Point2D.Float(0, 0), new Point2D.Float(2, 0), 
				new RouteEdgeMeta(50, true, true, false, true, false), "Bike only road");
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.SPEED_CAR));
		assertTrue(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK));
	}
	
	@Test
	public void testTravelTypeWalk() {
		RouteEdge edge = new RouteEdge(new Point2D.Float(0, 0), new Point2D.Float(2, 0), 
				new RouteEdgeMeta(50, true, true, false, false, true), "Walk only road");
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_CAR));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.SPEED_CAR));
		assertTrue(!edge.isTravelTypeAllowed(WeightEnum.DISTANCE_BIKE));
		assertTrue(edge.isTravelTypeAllowed(WeightEnum.DISTANCE_WALK));
	}
}
