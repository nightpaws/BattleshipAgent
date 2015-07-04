package com.innovation.battleships.player;

import java.awt.Point;
import java.util.Random;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipOrientation;

/**
 * Created by kierandouglas on 20/05/15.
 */
public class RandomPlayer implements Player {

	Random rand = new Random();

	@Override
	public String getName() {
		return "Random";
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
			ShipOrientation[] orientations = new ShipOrientation[] {ShipOrientation.Up, ShipOrientation.Right, ShipOrientation.Down, ShipOrientation.Left};

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
