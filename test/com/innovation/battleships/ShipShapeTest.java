package com.innovation.battleships;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.innovation.battleships.engine.ShipOrientation;
import com.innovation.battleships.engine.ShipShape;
import com.innovation.battleships.engine.ShipType;

/**
 * Created by kierandouglas on 31/05/15.
 */
public class ShipShapeTest {

	@Test
	public void testDestroyerUp() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));

		Assert.assertEquals(ShipShape.get(ShipType.Destroyer, ShipOrientation.Up, 0, 0), points);
	}

	@Test
	public void testDestroyerDown() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));

		Assert.assertEquals(ShipShape.get(ShipType.Destroyer, ShipOrientation.Down, 0, 0), points);
	}

	@Test
	public void testDestroyerRight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Destroyer, ShipOrientation.Right, 0, 0), points);
	}

	@Test
	public void testDestroyerLeft() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Destroyer, ShipOrientation.Left, 0, 0), points);
	}

	@Test
	public void testCruiserUp() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Cruiser, ShipOrientation.Up, 0, 0), points);
	}

	@Test
	public void testCruiserDown() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Cruiser, ShipOrientation.Down, 0, 0), points);
	}

	@Test
	public void testCruiserRight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Cruiser, ShipOrientation.Right, 0, 0), points);
	}

	@Test
	public void testCruiserLeft() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Cruiser, ShipOrientation.Left, 0, 0), points);
	}

	@Test
	public void testBattleshipUp() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));

		Assert.assertEquals(ShipShape.get(ShipType.Battleship, ShipOrientation.Up, 0, 0), points);
	}

	@Test
	public void testBattleshipDown() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		points.add(new Point(0, 3));

		Assert.assertEquals(ShipShape.get(ShipType.Battleship, ShipOrientation.Down, 0, 0), points);
	}

	@Test
	public void testBattleshipRight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 0));
		points.add(new Point(3, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Battleship, ShipOrientation.Right, 0, 0), points);
	}

	@Test
	public void testBattleshipLeft() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 0));
		points.add(new Point(3, 0));

		Assert.assertEquals(ShipShape.get(ShipType.Battleship, ShipOrientation.Left, 0, 0), points);
	}

	@Test
	public void testHovercraftUp() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 2));
		points.add(new Point(0, 1));
		points.add(new Point(1, 0));
		points.add(new Point(2, 1));
		points.add(new Point(2, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Hovercraft, ShipOrientation.Up, 0, 0), points);
	}

	@Test
	public void testHovercraftDown() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(2, 0));
		points.add(new Point(0, 1));
		points.add(new Point(2, 1));
		points.add(new Point(1, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Hovercraft, ShipOrientation.Down, 0, 0), points);
	}

	@Test
	public void testHovercraftRight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 1));
		points.add(new Point(1, 2));
		points.add(new Point(0, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Hovercraft, ShipOrientation.Right, 0, 0), points);
	}

	@Test
	public void testHovercraftLeft() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(2, 0));
		points.add(new Point(1, 0));
		points.add(new Point(0, 1));
		points.add(new Point(1, 2));
		points.add(new Point(2, 2));

		Assert.assertEquals(ShipShape.get(ShipType.Hovercraft, ShipOrientation.Left, 0, 0), points);
	}

	@Test
	public void testAircraftCarrierUp() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(1, 0));
		points.add(new Point(1, 1));
		points.add(new Point(1, 2));
		points.add(new Point(1, 3));
		points.add(new Point(0, 3));
		points.add(new Point(2, 3));

		Assert.assertEquals(ShipShape.get(ShipType.AircraftCarrier, ShipOrientation.Up, 0, 0), points);
	}

	@Test
	public void testAircraftCarrierDown() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(1, 0));
		points.add(new Point(2, 0));
		points.add(new Point(1, 1));
		points.add(new Point(1, 2));
		points.add(new Point(1, 3));

		Assert.assertEquals(ShipShape.get(ShipType.AircraftCarrier, ShipOrientation.Down, 0, 0), points);
	}

	@Test
	public void testAircraftCarrierRight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(0, 2));
		points.add(new Point(1, 1));
		points.add(new Point(2, 1));
		points.add(new Point(3, 1));

		Assert.assertEquals(ShipShape.get(ShipType.AircraftCarrier, ShipOrientation.Right, 0, 0), points);
	}

	@Test
	public void testAircraftCarrierLeft() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(0, 1));
		points.add(new Point(1, 1));
		points.add(new Point(2, 1));
		points.add(new Point(3, 1));
		points.add(new Point(3, 0));
		points.add(new Point(3, 2));

		Assert.assertEquals(ShipShape.get(ShipType.AircraftCarrier, ShipOrientation.Left, 0, 0), points);
	}

	@Test
	public void testHovercraftDownAtEightTen() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(8, 10));
		points.add(new Point(8, 11));
		points.add(new Point(9, 12));
		points.add(new Point(10, 11));
		points.add(new Point(10, 10));

		Assert.assertEquals(ShipShape.get(ShipType.Hovercraft, ShipOrientation.Down, 8, 10), points);
	}

	@Test
	public void testAircraftCarrierAtOneEight() {
		Set<Point> points = new HashSet<Point>();

		points.add(new Point(1, 9));
		points.add(new Point(2, 9));
		points.add(new Point(3, 9));
		points.add(new Point(4, 9));
		points.add(new Point(4, 8));
		points.add(new Point(4, 10));

		Assert.assertEquals(ShipShape.get(ShipType.AircraftCarrier, ShipOrientation.Left, 1, 8), points);
	}
}
