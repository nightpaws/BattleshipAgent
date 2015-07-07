package com.innovation.battleships.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipOrientation;
import com.innovation.battleships.engine.ShipType;

/**
 * Created by [Team Name] on [Date]
 */
public class NotAwfulPlayer implements Player {

	Random rand = new Random();
	// Store opponents remaining ships
	List<Ship> opponentShips = new ArrayList<Ship>();
	// Store all attack moves made? - (Miss = M | Hit = H)
	char[][] opponentGrid = new char[11][11];

	/**
	 * Constructor
	 */
	public NotAwfulPlayer() {
		// block off top right of field
		// populate initial grid with empty spaces (for display)
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				opponentGrid[i][j] = 'O';
			}
		}

		// Invalidate initial corner of grid
		for (int i = 6; i < 11; i++) {
			for (int j = 0; j < 7; j++) {
				opponentGrid[i][j] = 'X'; // invalid
			}
		}

		// Print Grid to Console (for testing)
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				System.out.print(opponentGrid[i][j]);
			}
			System.out.println();
		}

		// Add Ships remaining to list
		Ship ac = new Ship(ShipType.AircraftCarrier);
		Ship bs = new Ship(ShipType.Battleship);
		Ship cr = new Ship(ShipType.Cruiser);
		Ship de = new Ship(ShipType.Destroyer);
		Ship hc = new Ship(ShipType.Hovercraft);
		opponentShips.addAll(Arrays.asList(ac, bs, cr, de, hc));

		for (int i = 0; i < opponentShips.size(); i++)
			System.out.println("Ships Remaining: " + opponentShips.get(i).getType());
	}

	@Override
	public String getName() {
		return "Better";
	}

	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public String toString() {
		return getName() + " " + getVersion();
	}

	@Override
	public void newMatch(String opponent) {
	}

	@Override
	public void newGame(Integer timeSpan) {

	}

	@Override
	public void placeShips(java.util.List<Ship> ships) {
		for (Ship s : ships) {
			ShipOrientation[] orientations = new ShipOrientation[] { ShipOrientation.Up, ShipOrientation.Right,
					ShipOrientation.Down, ShipOrientation.Left };

			int randomInt = rand.nextInt(4);

			int x = rand.nextInt(12);
			int y = rand.nextInt(12);

			while (x >= 6 && y <= 6) {
				x = rand.nextInt(12);
				y = rand.nextInt(12);
			}

			s.place(new Point(x, y), orientations[randomInt]);
		}
	}

	@Override
	public Point getShot() {
		// Stuff.....

		// hit!

		// check what ships I could have hit

		// see if they fit in the area I have (use map)

		// if that works, take the shot (why not :) )

		// if I've already got part of a ship and i'm on the second shot, carry
		// on shooting!

		// If I've gone wrong and missed then check if I've hit two ships

		// failing that.... guess randomly from reasonable move set

		int x = rand.nextInt(12);
		int y = rand.nextInt(12);

		while (x >= 6 && y <= 6) {
			x = rand.nextInt(12);
			y = rand.nextInt(12);
		}
		return new Point(x, y);
	}

	@Override
	public void opponentShot(Point shot) {

	}

	@Override
	public void shotHit(Point shot, boolean sunk) {

	}

	@Override
	public void shotMiss(Point shot) {

	}

	@Override
	public void gameWon() {

	}

	@Override
	public void gameLost() {

	}

	@Override
	public void matchOver() {

	}

}
