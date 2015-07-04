package com.innovation.battleships.engine;

import static com.innovation.battleships.engine.ShipOrientation.Down;
import static com.innovation.battleships.engine.ShipOrientation.Up;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kierandouglas on 31/05/15.
 */
public class ShipShape {

	private static boolean X = true;
	private static boolean _ = false;

	public static Set<Point> get(ShipType type, ShipOrientation orientation, int topLeftX, int topLeftY) {
		boolean[][] grid = null;

		switch (type) {
			case Destroyer:
				if (orientation.equals(Up) || orientation.equals(Down)) {
					grid = new boolean[][] {
							new boolean[] {X}, 
							new boolean[] {X}
						   };
				} else {
					grid = new boolean[][] {
							new boolean[] {X, X}
						   };
				}
				break;

			case Cruiser:
				if (orientation.equals(Up) || orientation.equals(Down)) {
					grid = new boolean[][] {
							new boolean[] {X},
							new boolean[] {X},
							new boolean[] {X}
						   };
				} else {
					grid = new boolean[][] {
							new boolean[] {X, X, X}
						   };
				}
				break;

			case Battleship:
				if (orientation.equals(Up) || orientation.equals(Down)) {
					grid = new boolean[][] {
							new boolean[] {X},
							new boolean[] {X},
							new boolean[] {X},
							new boolean[] {X}
						   };
				} else {
					grid = new boolean[][] {
							new boolean[] {X, X, X, X}
						   };
				}
				break;

			case Hovercraft:
				switch (orientation) {
					case Up:
						grid = new boolean[][] {
								new boolean[] {_, X, _}, 
								new boolean[] {X, _, X}, 
								new boolean[] {X, _, X}
							   };
						break;
					case Right:
						grid = new boolean[][] {
								new boolean[] {X, X, _},
								new boolean[] {_, _, X}, 
								new boolean[] {X, X, _}
							   };
						break;
					case Down:
						grid = new boolean[][] {
								new boolean[] {X, _, X}, 
								new boolean[] {X, _, X}, 
								new boolean[] {_, X, _}
							   };
						break;
					case Left:
						grid = new boolean[][] {
								new boolean[] {_, X, X}, 
								new boolean[] {X, _, _}, 
								new boolean[] {_, X, X}
							   };
						break;
				}
				break;

			case AircraftCarrier:
				switch (orientation) {
					case Up:
						grid = new boolean[][] {
								new boolean[] {_, X, _}, 
								new boolean[] {_, X, _}, 
								new boolean[] {_, X, _}, 
								new boolean[] {X, X, X}
							   };
						break;
					case Right:
						grid = new boolean[][] {
								new boolean[] {X, _, _, _}, 
								new boolean[] {X, X, X, X}, 
								new boolean[] {X, _, _, _}
							   };
						break;
					case Down:
						grid = new boolean[][] {
								new boolean[] {X, X, X}, 
								new boolean[] {_, X, _}, 
								new boolean[] {_, X, _}, 
								new boolean[] {_, X, _}
							   };
						break;
					case Left:
						grid = new boolean[][] {
								new boolean[] {_, _, _, X}, 
								new boolean[] {X, X, X, X}, 
								new boolean[] {_, _, _, X}
							   };
						break;
				}
				break;
		}

		Set<Point> locations = new HashSet<Point>();

		for (int y = 0; y < grid.length; ++y) {
			for (int x = 0; x < grid[y].length; ++x) {
				if (grid[y][x]) {
					locations.add(new Point(topLeftX + x, topLeftY + y));
				}
			}
		}

		return locations;
	}

}
