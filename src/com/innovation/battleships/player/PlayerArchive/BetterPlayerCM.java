package com.innovation.battleships.player.PlayerArchive;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipOrientation;
import com.innovation.battleships.engine.ShipType;

/**
 * Created by Our Shots are Hit and Miss
 */
public class BetterPlayerCM implements Player {

	// NonSpecific elements
	protected Random rand;
	private PositioningType specialPositioning;
	// keep track of how long it takes a ship to be sunk by random positioning
	private Map<Integer, Integer> positionSunkStatsDefault;
	private Map<Integer, Integer> positionSunkStatsCorner;
	private Map<Integer, Integer> positionSunkStatsTogether;
	private Map<Integer, Integer> positionSunkStatsMiddle;
	// keep the entire statistics of where we have been hit, while one above
	// only black positions
	// where we have been successfully hit more than 10 time
	private Map<Point, Integer> opponentHitShotsStats;

	// Eternal Counters
	protected int[][] enemyHitHeatmap = new int[12][12];
	protected int gameCounter;

	// Game Specific Variables
	protected int[][] currentEnemyGrid = new int[12][12];
	protected List<ShipType> currentEnemyShips;
	private List<Ship> ourShips;

	// Turn Specific Variables
	protected boolean attackHit;

	public BetterPlayerCM() {
		rand = new Random();
		gameCounter = 0;
		attackHit = false;
		this.ourShips = new ArrayList<Ship>();
		this.opponentHitShotsStats = new HashMap<Point, Integer>();
		this.positionSunkStatsCorner = new HashMap<Integer, Integer>();
		this.positionSunkStatsDefault = new HashMap<Integer, Integer>();
		this.positionSunkStatsMiddle = new HashMap<Integer, Integer>();
		this.positionSunkStatsTogether = new HashMap<Integer, Integer>();
	}

	@Override
	public String getName() {
		return "Our moves are pretty hit or miss";
	}

	@Override
	public int getVersion() {
		return 2;
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
		currentEnemyShips = new ArrayList<ShipType>();

		// Shows the values in enemyHitHeatmap before a game. Should be 0 at
		// first game
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				System.out.print(enemyHitHeatmap[i][j]);
			}
			System.out.println();
		}
	}

	@Override
	public void placeShips(List<Ship> ships) {

		// position where our ship has been placed and hit by opponent many
		// times
		boolean onBlackPosition;
		boolean valid;
		boolean collides;
		ShipOrientation[] orientations = new ShipOrientation[] { ShipOrientation.Up, ShipOrientation.Right,
				ShipOrientation.Down, ShipOrientation.Left };

		Map<Integer, Point> sortedOppentHitShots = new TreeMap<Integer, Point>();
		for (Point p : opponentHitShotsStats.keySet()) {
			sortedOppentHitShots.put(opponentHitShotsStats.get(p), p);
		}
		// System.out.println(sortedOppentHitShots);

		for (Ship s : ships) {

			valid = false;
			collides = true;
			onBlackPosition = true;

			if (gameCounter >= 30 && gameCounter <= 60) {
				int specialPositioningRandom = 0;
				if (gameCounter < 40)
					specialPositioningRandom = 0;
				else if (gameCounter < 50)
					specialPositioningRandom = 1;
				else if (gameCounter <= 60)
					specialPositioningRandom = 2;

				specialPlace(specialPositioningRandom, orientations, s);

				continue;
			} else {
				if (gameCounter > 60 && specialPositioningIsWorking() != 3) {
					System.out.println("positioning chosen" + specialPositioningIsWorking());
					specialPlace(specialPositioningIsWorking(), orientations, s);
				} else {
					specialPositioning = PositioningType.DEFAULT;
					while (!valid || collides || onBlackPosition) {

						int randomInt = rand.nextInt(4);

						int x = rand.nextInt(12);
						int y = rand.nextInt(12);

						while (x >= 6 && y <= 6) {
							x = rand.nextInt(12);
							y = rand.nextInt(12);
						}

						s.place(new Point(x, y), orientations[randomInt]);

						valid = s.isValid();

						// checks if placed on blackPosition
						onBlackPosition = false;

						int counter = 0;
						ArrayList<Integer> keys = new ArrayList<Integer>(sortedOppentHitShots.keySet());
						for (int i = keys.size() - 1; i >= 0; i--) {
							if (counter > 10)
								break;
							counter++;
							if (s.isAt(sortedOppentHitShots.get(keys.get(i)))) {
								onBlackPosition = true;
								break;
							}

						}

						// if not on blackPosition try again
						if (onBlackPosition)
							continue;

						collides = false;
						for (Ship otherS : ships) {
							if (s.getType() != otherS.getType() && otherS.getAllLocations() != null
									&& s.collidesWith(otherS)) {
								collides = true;
								break;
							}

						}
					}

					this.ourShips.add(s);
				}
			}
		}

		System.out.println("Default:" + positionSunkStatsDefault);
		System.out.println("Corner:" + positionSunkStatsCorner);
		System.out.println("Middle:" + positionSunkStatsMiddle);
		System.out.println("Together: " + positionSunkStatsTogether);
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

		switch (specialPositioning) {
		case DEFAULT:
			positionSunkStatsDefault.put(gameCounter, positionSunkStatsDefault.getOrDefault(gameCounter, 0) + 1);
			break;
		case MIDDLE:
			positionSunkStatsMiddle.put(gameCounter, positionSunkStatsMiddle.getOrDefault(gameCounter, 0) + 1);
			break;
		case CORNERS:
			positionSunkStatsCorner.put(gameCounter, positionSunkStatsCorner.getOrDefault(gameCounter, 0) + 1);
			break;
		case TOGETHER:
			positionSunkStatsTogether.put(gameCounter, positionSunkStatsTogether.getOrDefault(gameCounter, 0) + 1);
			break;
		}

		for (Ship s : ourShips) {
			if (s.isAt(shot)) {
				opponentHitShotsStats.put(shot, opponentHitShotsStats.getOrDefault(shot, 0) + 1);

			}
		}
	}

	@Override
	public void shotHit(Point shot, boolean sunk) {
		attackHit = true; // set hit to true

		enemyHitHeatmap[shot.x][shot.y]++;
		currentEnemyGrid[shot.x][shot.y] = 2;// hit (1 = miss)

		if (sunk) {
			attackHit = false;

		} else {
			// check surrounding positions
			Map<Integer, Point> boardPos = new HashMap<Integer, Point>();
			boardPos.put(1, new Point(shot.x - 1, shot.y));
			boardPos.put(2, new Point(shot.x + 1, shot.y));
			boardPos.put(3, new Point(shot.x, shot.y + 1));
			boardPos.put(4, new Point(shot.x, shot.y - 1));
			Map<Integer, Integer> boardVals = new HashMap<Integer, Integer>();
			boardVals.put(1, enemyHitHeatmap[shot.x - 1][shot.y]);
			boardVals.put(2, enemyHitHeatmap[shot.x + 1][shot.y]);
			boardVals.put(3, enemyHitHeatmap[shot.x][shot.y + 1]);
			boardVals.put(4, enemyHitHeatmap[shot.x][shot.y - 1]);
			for (int i = 0; i < boardPos.size(); i++) {
				if(!boardVals.get(i).equals(0)){
					boardPos.remove(i);
				}
			}
			//get a valid move
			Point p = boardPos.get(rand.nextInt(boardPos.size()-1));
			//Tell shooter to use this!!!

		}
	}

	@Override
	public void shotMiss(Point shot) {
		// enemyHitHeatmap[shot.x][shot.y]--; //Optional
		currentEnemyGrid[shot.x][shot.y] = 1;// miss (2 = hit)

	}

	@Override
	public void gameWon() {
		gameCounter++;
	}

	@Override
	public void gameLost() {
		gameCounter++;

	}

	@Override
	public void matchOver() {
		// TODO Auto-generated method stub

	}

	private enum PositioningType {
		CORNERS, TOGETHER, MIDDLE, DEFAULT
	}

	private void specialPlace(int specialPositioningRandom, ShipOrientation[] orientations, Ship s) {
		switch (specialPositioningRandom) {
		case 0:
			specialPositioning = PositioningType.CORNERS;
			break;
		case 1:
			specialPositioning = PositioningType.MIDDLE;
			break;
		case 2:
			specialPositioning = PositioningType.TOGETHER;
			break;
		}

		switch (s.getType()) {
		case Destroyer:
			switch (specialPositioning) {
			case CORNERS:
				s.place(new Point(0, 0), orientations[0]);
				break;
			case MIDDLE:
				s.place(new Point(2, 10), orientations[1]);
				break;
			case TOGETHER:
				s.place(new Point(2, 9), orientations[1]);
				break;
			}
			break;
		case Cruiser:
			switch (specialPositioning) {
			case CORNERS:
				s.place(new Point(0, 3), orientations[0]);
				break;
			case MIDDLE:
				s.place(new Point(7, 10), orientations[1]);
				break;
			case TOGETHER:
				s.place(new Point(5, 9), orientations[1]);
				break;
			}
			break;
		case Battleship:
			switch (specialPositioning) {
			case CORNERS:
				s.place(new Point(3, 11), orientations[1]);
				break;
			case MIDDLE:
				s.place(new Point(2, 4), orientations[1]);
				break;
			case TOGETHER:
				s.place(new Point(1, 5), orientations[1]);
				break;
			}
			break;
		case AircraftCarrier:
			switch (specialPositioning) {
			case CORNERS:
				s.place(new Point(1, 0), orientations[3]);
				break;
			case MIDDLE:
				s.place(new Point(4, 7), orientations[0]);// maybe 4/5 for y
				break;
			case TOGETHER:
				s.place(new Point(3, 7), orientations[0]);// maybe 4/5 for y
				break;
			}
			break;
		case Hovercraft:
			switch (specialPositioning) {
			case CORNERS:
				s.place(new Point(7, 8), orientations[0]);
				break;
			case MIDDLE:
				s.place(new Point(4, 6), orientations[0]);// maybe 4 for y
				break;
			case TOGETHER:
				s.place(new Point(3, 6), orientations[0]);// maybe 4 for y
				break;
			}
			break;
		}
	}

	private int specialPositioningIsWorking() {
		int defaultPositioningAverageSunkTime = getAverage(positionSunkStatsDefault);
		int cornerPositioningAverageSunkTime = getAverage(positionSunkStatsCorner);
		int middlePositioningAverageSunkTime = getAverage(positionSunkStatsMiddle);
		int togetherPositioningAverageSunkTime = getAverage(positionSunkStatsTogether);
		int positioning;
		int positioningValue;

		if (cornerPositioningAverageSunkTime < middlePositioningAverageSunkTime
				&& cornerPositioningAverageSunkTime < togetherPositioningAverageSunkTime) {
			positioning = 0;
			positioningValue = cornerPositioningAverageSunkTime;
		} else if (middlePositioningAverageSunkTime < cornerPositioningAverageSunkTime
				&& middlePositioningAverageSunkTime < togetherPositioningAverageSunkTime) {
			positioning = 1;
			positioningValue = middlePositioningAverageSunkTime;
		} else {
			positioning = 2;
			positioningValue = cornerPositioningAverageSunkTime;
		}

		// TODO: fix checks when to use which strategy
		System.out.println(defaultPositioningAverageSunkTime);
		System.out.println(middlePositioningAverageSunkTime);
		System.out.println(cornerPositioningAverageSunkTime);
		System.out.println(togetherPositioningAverageSunkTime);
		if (defaultPositioningAverageSunkTime > positioningValue)
			return positioning;
		else
			return 3;
	}

	private int getAverage(Map<Integer, Integer> data) {
		int total = 0;
		int counter = 0;
		for (int game : data.values()) {
			total += game;
			counter += 1;
		}
		return total / counter;
	}

}
