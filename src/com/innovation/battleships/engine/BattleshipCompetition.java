package com.innovation.battleships.engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by kierandouglas on 26/05/15.
 */
public class BattleshipCompetition {

	private Player op1;
	private Player op2;
	private Integer timePerGame;
	private int wins;
	private boolean playOut;

	public BattleshipCompetition(Player op1, Player op2, Integer timePerGame, int wins, boolean playOut) {
		if (op1 == null) {
			throw new IllegalArgumentException("op1");
		}

		if (op2 == null) {
			throw new IllegalArgumentException("op2");
		}

		if (timePerGame <= 0) {
			throw new IllegalArgumentException("timePerGame");
		}

		if (wins <= 0) {
			throw new IllegalArgumentException("wins");
		}

		this.op1 = op1;
		this.op2 = op2;
		this.timePerGame = timePerGame;
		this.wins = wins;
		this.playOut = playOut;
	}

	public HashMap<Player, Integer> runCompetition() {
		Random rand = new Random();

		Player[] opponents = new Player[2];
		Integer[] scores = new Integer[2];
		org.apache.commons.lang3.time.StopWatch[] times = new org.apache.commons.lang3.time.StopWatch[2];
		Ship[][] ships = new Ship[2][];
		Point[][] shots = new Point[2][];

		int first = 0;
		int second = 1;

		opponents[first] = this.op1;
		opponents[second] = this.op2;
		scores[first] = 0;
		scores[second] = 0;
		times[first] = new org.apache.commons.lang3.time.StopWatch();
		times[second] = new org.apache.commons.lang3.time.StopWatch();

		if (rand.nextDouble() >= 0.5) {
			int swap = first;
			first = second;
			second = swap;
		}

		opponents[first].newMatch(opponents[second].getName() + " v" + opponents[second].getVersion());
		opponents[second].newMatch(opponents[first].getName() + " v" + opponents[first].getVersion());

		boolean success;

		masterGameLoop: while (true) {
			if (this.playOut) {
				int scoreSum = 0;
				for (int score : scores)
					scoreSum += score;
				if (scoreSum >= this.wins * 2 - 1)
					break;
			} else {
				for (int score : scores) {
					if (score >= this.wins)
						break masterGameLoop;
				}
			}

			{
				int swap = first;
				first = second;
				second = swap;
			}

			times[first].reset();
			times[second].reset();
			Point[] firstPlayerShots = shots[first];
			Point[] secondPlayerShots = shots[second];
			
			if (firstPlayerShots != null)
				Arrays.fill(firstPlayerShots, null);
			
			if (secondPlayerShots != null)
				Arrays.fill(secondPlayerShots, null);

			times[first].start();
			opponents[first].newGame(this.timePerGame);
			times[first].suspend();
			
			if (times[first].getTime() > this.timePerGame) {
				recordWin(second, first, scores, opponents);
				continue;
			}

			times[second].start();
			opponents[second].newGame(this.timePerGame);
			times[second].suspend();
			
			if (times[second].getTime() > this.timePerGame) {
				recordWin(first, second, scores, opponents);
				continue;
			}

			success = false;
			
			do {
				ships[first] = generateBlankShips();

				times[first].resume();
				times[first].suspend();
				opponents[first].placeShips(Collections.unmodifiableList(Arrays.asList(ships[first])));
				if (times[first].getTime() > this.timePerGame) {
					break;
				}

				boolean allPlacedValidly = true;
				for (int i = 0; i < ships[first].length; i++) {
					if (!ships[first][i].getIsPlaced() || !ships[first][i].isValid()) {
						allPlacedValidly = false;
						break;
					}
				}

				if (!allPlacedValidly) {
					continue;
				}

				boolean noneConflict = true;
				for (int i = 0; i < ships[first].length; i++) {
					for (int j = i + 1; j < ships[first].length; j++) {
						if (ships[first][i].collidesWith(ships[first][j])) {
							noneConflict = false;
							break;
						}
					}

					if (!noneConflict) {
						break;
					}
				}

				if (noneConflict) {
					success = true;
				}
			} while (!success);
			
			if (times[first].getTime() > this.timePerGame) {
				recordWin(second, first, scores, opponents);
				continue;
			}

			success = false;
			do {
				ships[second] = generateBlankShips();

				times[second].resume();
				opponents[second].placeShips(Collections.unmodifiableList(Arrays.asList(ships[second])));
				times[second].suspend();
				if (times[second].getTime() > this.timePerGame) {
					break;
				}

				boolean allPlacedValidly = true;
				for (int i = 0; i < ships[second].length; i++) {
					if (!ships[second][i].getIsPlaced() || !ships[second][i].isValid()) {
						allPlacedValidly = false;
						break;
					}
				}

				if (!allPlacedValidly) {
					continue;
				}

				boolean noneConflict = true;
				for (int i = 0; i < ships[second].length; i++) {
					for (int j = i + 1; j < ships[second].length; j++)
						if (ships[second][i].collidesWith(ships[second][j])) {
							noneConflict = false;
							break;
						}

					if (!noneConflict) {
						break;
					}
				}

				if (noneConflict) {
					success = true;
				}
			} while (!success);
			
			if (times[second].getTime() > this.timePerGame) {
				recordWin(first, second, scores, opponents);
				continue;
			}

			int current = first;

			HashMap<Integer, List<Point>> shotsMap = new HashMap<Integer, List<Point>>();
			shotsMap.put(first, new ArrayList<Point>());
			shotsMap.put(second, new ArrayList<Point>());

			gameLoop: while (true) {
				List<Point> currentPlayerShots = shotsMap.get(current);

				times[current].resume();
				Point shot = opponents[current].getShot();
				times[current].suspend();
				if (times[current].getTime() > this.timePerGame) {
					recordWin(1 - current, current, scores, opponents);
					break;
				}

				for (Point p : currentPlayerShots) {
					if (p.getX() == shot.getX() && p.getY() == shot.getY())
						continue gameLoop;
				}

				currentPlayerShots.add(shot);

				times[1 - current].resume();
				opponents[1 - current].opponentShot(shot);
				times[1 - current].suspend();
				if (times[1 - current].getTime() > this.timePerGame) {
					recordWin(current, 1 - current, scores, opponents);
					break;
				}

				Ship ship = null;

				for (Ship s : ships[1 - current]) {
					if (s.isAt(shot))
						ship = s;
				}

				if (ship != null) {
					boolean sunk = ship.isSunk(currentPlayerShots);

					times[current].resume();
					opponents[current].shotHit(shot, sunk);
					times[current].suspend();
					if (times[current].getTime() > this.timePerGame) {
						recordWin(1 - current, current, scores, opponents);
						break;
					}
				} else {
					times[current].resume();
					opponents[current].shotMiss(shot);
					times[current].suspend();
					if (times[current].getTime() > this.timePerGame) {
						recordWin(1 - current, current, scores, opponents);
						break;
					}
				}

				List<Ship> unSunk = new ArrayList<Ship>();

				for (Ship s : ships[1 - current]) {
					if (!s.isSunk(currentPlayerShots))
						unSunk.add(s);
				}

				if (unSunk.isEmpty()) {
					recordWin(current, 1 - current, scores, opponents);
					break;
				}

				current = 1 - current;
			}
		}

		opponents[first].matchOver();
		opponents[second].matchOver();

		HashMap<Player, Integer> toReturn = new HashMap<Player, Integer>();

		toReturn.put(opponents[0], scores[0]);
		toReturn.put(opponents[1], scores[1]);
		return toReturn;
	}

	private Ship[] generateBlankShips() {
		return new Ship[] {new Ship(ShipType.Destroyer), new Ship(ShipType.Cruiser), new Ship(ShipType.Battleship), new Ship(ShipType.Hovercraft), new Ship(ShipType.AircraftCarrier)};
	}

	private void recordWin(int winner, int loser, Integer[] scores, Player[] opponents) {
		scores[winner]++;
		opponents[winner].gameWon();
		opponents[loser].gameLost();
	}
	
}
