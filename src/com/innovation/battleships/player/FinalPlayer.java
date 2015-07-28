package com.innovation.battleships.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipOrientation;
import com.innovation.battleships.engine.ShipType;

/**
 * Created by Our Shots are Hit and Miss
 */
public class FinalPlayer implements Player {

	protected Random rand = new Random();
	private boolean largeControl = true;
	// Store opponents remaining ships
	protected List<Target> targetsLeft = new ArrayList<Target>();

	/*
	 * Targetting and Attack Related
	 */
	protected boardStates[][] opponentGrid = new boardStates[12][12];
	protected Point lastHit;

	/*
	 * Ship Placement Related
	 */

	// holds our ships
	private List<Ship> ourShips;

	// keep the entire statistics of where we have been hit, while one above
	// only black positions
	// where we have been successfully hit more than 10 time

	// private Set<Point> opponentHitShots;
	private Map<Point, Integer> opponentHitShotsStats;
	private int gameCounter;
	private PositioningType specialPositioning;

	// keep track of how long it takes a ship to be sunk by random positioning
	private Map<Integer, Integer> positionSunkStatsDefault;
	private Map<Integer, Integer> positionSunkStatsCorner;
	private Map<Integer, Integer> positionSunkStatsTogether;
	private Map<Integer, Integer> positionSunkStatsMiddle;
	private Map<Integer, Point> squareMap;
	private HitSet hitSet;

	private enum PositioningType {
		CORNERS, TOGETHER, MIDDLE, DEFAULT
	}

	protected enum boardStates {
		HIT, MISS, INVALID, UNUSED
	}

	/**
	 * Constructor
	 */
	public FinalPlayer() {

		this.ourShips = new ArrayList<Ship>();
		this.opponentHitShotsStats = new HashMap<Point, Integer>();
		this.gameCounter = 0;
		this.positionSunkStatsCorner = new HashMap<Integer, Integer>();
		this.positionSunkStatsDefault = new HashMap<Integer, Integer>();
		this.positionSunkStatsMiddle = new HashMap<Integer, Integer>();
		this.positionSunkStatsTogether = new HashMap<Integer, Integer>();

		squareMap = new HashMap<Integer, Point>();
		squareMap.put(0, new Point(0, 0));
		squareMap.put(1, new Point(2, 0));
		squareMap.put(2, new Point(4, 0));
		squareMap.put(3, new Point(0, 2));
		squareMap.put(4, new Point(2, 2));
		squareMap.put(5, new Point(4, 2));
		squareMap.put(6, new Point(0, 4));
		squareMap.put(7, new Point(2, 4));
		squareMap.put(8, new Point(4, 4));
		squareMap.put(9, new Point(0, 6));
		squareMap.put(10, new Point(2, 6));
		squareMap.put(11, new Point(4, 6));
		squareMap.put(12, new Point(6, 6));
		squareMap.put(13, new Point(8, 6));
		squareMap.put(14, new Point(10, 6));
		squareMap.put(15, new Point(0, 8));
		squareMap.put(16, new Point(2, 8));
		squareMap.put(17, new Point(4, 8));
		squareMap.put(18, new Point(6, 8));
		squareMap.put(19, new Point(8, 8));
		squareMap.put(20, new Point(10, 8));
		squareMap.put(21, new Point(0, 10));
		squareMap.put(22, new Point(2, 10));
		squareMap.put(23, new Point(4, 10));
		squareMap.put(24, new Point(6, 10));
		squareMap.put(25, new Point(8, 10));
		squareMap.put(26, new Point(10, 10));
	}

	@Override
	public String getName() {
		return "Our Shots are Hit and Miss";
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public String toString() {
		return getName() + " Version: " + getVersion();
	}

	@Override
	public void newMatch(String opponent) {

	}

	@Override
	public void newGame(Integer timeSpan) {
		// block off top right of field
		// populate initial grid with empty spaces (for display)
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++) {
				opponentGrid[i][j] = boardStates.UNUSED;
			}
		}
		// Invalidate initial corner of grid
		for (int i = 6; i < 12; i++) {
			for (int j = 0; j < 6; j++) {
				opponentGrid[i][j] = boardStates.INVALID; // invalid
			}
		}

		targetsLeft = new ArrayList<Target>();
		targetsLeft.add(new Target(ShipType.Destroyer, true));
		targetsLeft.add(new Target(ShipType.Destroyer, false));
		targetsLeft.add(new Target(ShipType.Cruiser, true));
		targetsLeft.add(new Target(ShipType.Cruiser, false));
		targetsLeft.add(new Target(ShipType.Battleship, true));
		targetsLeft.add(new Target(ShipType.Battleship, false));
		targetsLeft.add(new Target(ShipType.Hovercraft, true));
		targetsLeft.add(new Target(ShipType.Hovercraft, false));
		targetsLeft.add(new Target(ShipType.AircraftCarrier, true));
		targetsLeft.add(new Target(ShipType.AircraftCarrier, false));
		hitSet = new HitSet();
		largeControl = true;

	}

	@Override
	public void placeShips(java.util.List<Ship> ships) {

		// position where our ship has been placed and hit by opponent many
		// times
		boolean onBlackPosition;
		boolean valid;
		boolean collides;
		ShipOrientation[] orientations = new ShipOrientation[] { ShipOrientation.Up, ShipOrientation.Right,
				ShipOrientation.Down, ShipOrientation.Left };

		Map<Integer, Point> sortedOpponentHitShots = new TreeMap<Integer, Point>();

		for (Point p : opponentHitShotsStats.keySet()) {
			sortedOpponentHitShots.put(opponentHitShotsStats.get(p), p);
		}

		for (Ship s : ships) {

			valid = false;
			collides = true;
			onBlackPosition = true;

			// if (gameCounter>=30 && gameCounter<=60){
			if (gameCounter >= 3 && gameCounter <= 12) {
				int specialPositioningRandom = 0;
				// if(gameCounter<40)
				if (gameCounter < 6)
					specialPositioningRandom = 0;
				// else if(gameCounter<50)
				else if (gameCounter < 9)
					specialPositioningRandom = 1;
				// else if(gameCounter<=60)
				else if (gameCounter <= 12)
					specialPositioningRandom = 2;

				specialPlace(specialPositioningRandom, orientations, s);

				this.ourShips.add(s);
			} else {
				// if (gameCounter>60 && specialPositioningIsWorking()!=3){
				int positioninChosen = 0;
				// division by zero in getAverage function fix
				if (gameCounter > 12)
					positioninChosen = specialPositioningIsWorking();

				if (gameCounter > 12 && positioninChosen != 3) {
					specialPlace(positioninChosen, orientations, s);
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
						ArrayList<Integer> keys = new ArrayList<Integer>(sortedOpponentHitShots.keySet());
						for (int i = keys.size() - 1; i >= 0; i--) {
							if (counter > 10)
								break;
							counter++;
							if (s.isAt(sortedOpponentHitShots.get(keys.get(i)))) {
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
		Point returnedShot;
		if (largeControl) {
			returnedShot = largeShot();
		} else {
			returnedShot = smallShot();
		}
		System.out.println("Large control :" + largeControl);
		System.out.println("Next Shot x:" + returnedShot.x + " y:" + returnedShot.y);
		return returnedShot;
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
		// System.out.println("Positioning:" + specialPositioning);
		// System.out.println("Default:" + positionSunkStatsDefault);
		// System.out.println("Corner:" + positionSunkStatsCorner);
		// System.out.println("Middle:" + positionSunkStatsMiddle);
		// System.out.println("Together: " + positionSunkStatsTogether);

		for (Ship s : ourShips) {
			if (s.isAt(shot)) {
				opponentHitShotsStats.put(shot, opponentHitShotsStats.getOrDefault(shot, 0) + 1);

			}
		}

	}

	@Override
	public void shotHit(Point shot, boolean sunk) {
		System.out.println("Hit");
		largeControl = false;
		opponentGrid[shot.x][shot.y] = boardStates.HIT;
		hitSet.add(shot);
		if (sunk) {
			System.out.println("Sunk");
			List<Target> possiblySunk = new ArrayList<Target>(targetsLeft);
			for (Target current : targetsLeft) {
				if (shipMatches(current)) {
					possiblySunk.add(current);
				}
			}
			if (possiblySunk.size() == 2) {
				targetsLeft.removeAll(possiblySunk);
			}
			if (!(possiblySunk.size() == 0)) {
				largeControl = true;
				hitSet = new HitSet();
			}
		}
	}

	private boolean shipMatches(Target target) {
		System.out.println("Hitset x's:" + hitSet.numberOfEachX().size() + " y's:" + hitSet.numberOfEachY().size());
		switch (target.getType()) {
		case Destroyer:
			System.out.println("Trying to match destroyer");
			if (hitSet.size() == 2) {
				System.out.println("Matched");
				return true;
			}
			break;
		case Cruiser:
			System.out.println("Trying to match cruiser");
			if (hitSet.numberOfEachX().size() == 3 && hitSet.numberOfEachY().size() == 1) {
				System.out.println("Matched");
				return true;
			}
			if (hitSet.numberOfEachX().size() == 1 && hitSet.numberOfEachY().size() == 3) {
				System.out.println("Matched");
				return true;
			}
			break;
		case Battleship:
			System.out.println("Trying to match battleship");
			if (hitSet.numberOfEachX().size() == 4 && hitSet.numberOfEachY().size() == 1) {
				System.out.println("Matched");
				return true;
			}
			if (hitSet.numberOfEachX().size() == 1 && hitSet.numberOfEachY().size() == 4) {
				System.out.println("Matched");
				return true;
			}
			break;
		case AircraftCarrier:
			System.out.println("Trying to match aircraft carrier");
			if (hitSet.numberOfEachX().size() == 4 && hitSet.numberOfEachY().size() == 3) {
				System.out.println("Matched");
				return true;
			}
			if (hitSet.numberOfEachX().size() == 3 && hitSet.numberOfEachY().size() == 4) {
				System.out.println("Matched");
				return true;
			}
		case Hovercraft:
			System.out.println("Trying to match hovercraft");
			if (hitSet.numberOfEachX().size() == 3 && hitSet.numberOfEachY().size() == 3) {
				System.out.println("Matched");
				return true;
			}
		}
		return false;

	}

	@Override
	public void shotMiss(Point shot) {
		System.out.println("Miss");
		opponentGrid[shot.x][shot.y] = boardStates.MISS;
	}

	@Override
	public void gameWon() {
		System.out.println("Won------------------------------------------------------------");
		gameCounter++;
	}

	@Override
	public void gameLost() {
		System.out.println("Lost------------------------------------------------------------");
		gameCounter++;
	}

	@Override
	public void matchOver() {

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
			default:
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
			default:
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
			default:
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
			default:
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
			default:
				break;
			}
			break;
		}
	}

	private int specialPositioningIsWorking() {

		// TODO: fix selection of strategy, wrong chosen
		// Average values calculated correctly
		// not the correct strategy selected after this
		int defaultPositioningAverageSunkTime = getAverage(positionSunkStatsDefault);
		int cornerPositioningAverageSunkTime = getAverage(positionSunkStatsCorner);
		int middlePositioningAverageSunkTime = getAverage(positionSunkStatsMiddle);
		int togetherPositioningAverageSunkTime = getAverage(positionSunkStatsTogether);
		// int positioning;
		// int positioningValue;

		// System.out.println("_____ Stats Below ___________");
		// System.out.println("Default Positioining Average sunk time:" +
		// defaultPositioningAverageSunkTime);
		// System.out.println("Middle Positioining Average sunk time:" +
		// middlePositioningAverageSunkTime);
		// System.out.println("Corner Positioining Average sunk time:" +
		// cornerPositioningAverageSunkTime);
		// System.out.println("Together Positioining Average sunk time:" +
		// togetherPositioningAverageSunkTime);

		if (cornerPositioningAverageSunkTime >= middlePositioningAverageSunkTime
				&& cornerPositioningAverageSunkTime >= togetherPositioningAverageSunkTime
				&& cornerPositioningAverageSunkTime >= defaultPositioningAverageSunkTime)
			return 0;
		else if (middlePositioningAverageSunkTime >= cornerPositioningAverageSunkTime
				&& middlePositioningAverageSunkTime >= togetherPositioningAverageSunkTime
				&& middlePositioningAverageSunkTime >= defaultPositioningAverageSunkTime)
			return 1;
		else if (togetherPositioningAverageSunkTime >= cornerPositioningAverageSunkTime
				&& togetherPositioningAverageSunkTime >= defaultPositioningAverageSunkTime
				&& togetherPositioningAverageSunkTime >= middlePositioningAverageSunkTime)
			return 2;
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

	private Point smallShot() {
		Set<Shot> nextPossibleShots = new HashSet<Shot>();
		for (Point currentHit : hitSet) {
			if (pointIsValid(new Point(currentHit.x - 1, currentHit.y - 1))) {
				nextPossibleShots.add(new Shot(currentHit.x - 1, currentHit.y - 1));
			}
			if (pointIsValid(new Point(currentHit.x, currentHit.y - 1))) {
				nextPossibleShots.add(new Shot(currentHit.x, currentHit.y - 1));
			}
			if (pointIsValid(new Point(currentHit.x + 1, currentHit.y - 1))) {
				nextPossibleShots.add(new Shot(currentHit.x + 1, currentHit.y - 1));
			}
			if (pointIsValid(new Point(currentHit.x - 1, currentHit.y))) {
				nextPossibleShots.add(new Shot(currentHit.x - 1, currentHit.y));
			}
			if (pointIsValid(new Point(currentHit.x + 1, currentHit.y))) {
				nextPossibleShots.add(new Shot(currentHit.x + 1, currentHit.y));
			}
			if (pointIsValid(new Point(currentHit.x - 1, currentHit.y + 1))) {
				nextPossibleShots.add(new Shot(currentHit.x - 1, currentHit.y + 1));
			}
			if (pointIsValid(new Point(currentHit.x, currentHit.y + 1))) {
				nextPossibleShots.add(new Shot(currentHit.x, currentHit.y + 1));
			}
			if (pointIsValid(new Point(currentHit.x + 1, currentHit.y + 1))) {
				nextPossibleShots.add(new Shot(currentHit.x + 1, currentHit.y + 1));
			}
		}
		List<Shot> usedShots = new ArrayList<Shot>();
		for (Shot current : nextPossibleShots) {
			if (opponentGrid[current.getX()][current.getY()] != boardStates.UNUSED) {
				usedShots.add(current);
			}
		}
		nextPossibleShots.removeAll(usedShots);
		List<Target> possibleTargets = new ArrayList<Target>();
		for (Target target : targetsLeft) {
			if (shipFits(target)) {
				possibleTargets.add(target);
			}
		}
		System.out.println("Still could be:");
		for (Target possibility : possibleTargets) {
			System.out.println(possibility.getType() + " which is vertical " + possibility.isVertical());
			addPossibilities(nextPossibleShots, possibility);
		}
		Shot nextShot = new Shot(-1, -1);
		int maxNoUses = -1;
		for (Shot currentShot : nextPossibleShots) {
			if (currentShot.getUsefullnness() > maxNoUses) {
				nextShot = currentShot;
				maxNoUses = currentShot.getUsefullnness();
			}
		}
		if (nextShot.getX() == -1) {
			largeControl = true;
			return largeShot();
		}
		return new Point(nextShot.getX(), nextShot.getY());
	}

	private void addPossibilities(Set<Shot> nextShots, Target target) {
		switch (target.getType()) {
		case Destroyer:
			// Know hit set has size one or couldn't be a destroyer so add one
			// to four surrounding squares
			for (Point startingPoint : hitSet) {
				for (Shot possibleShot : nextShots) {
					if ((Math.abs(startingPoint.x - possibleShot.getX()) == 1
							&& Math.abs(startingPoint.y - possibleShot.getY()) == 0)
							|| (Math.abs(startingPoint.y - possibleShot.getY()) == 1
									&& Math.abs(startingPoint.x - possibleShot.getX()) == 0)) {
						possibleShot.possibleUse();
					}
				}
			}
			break;
		case Cruiser:
			if (target.isVertical()) {
				for (Point startingPoint : hitSet) {
					for (Shot possibleShot : nextShots) {
						if ((Math.abs(startingPoint.x - possibleShot.getX()) == 0
								&& Math.abs(startingPoint.y - possibleShot.getY()) == 1)) {
							// Know possible shots is only where you might want
							// to shoot so if one up
							// or down is in it you must be at the edge.
							possibleShot.possibleUse();
						}
					}
				}
			} else {
				for (Point startingPoint : hitSet) {
					for (Shot possibleShot : nextShots) {
						if ((Math.abs(startingPoint.x - possibleShot.getX()) == 1
								&& Math.abs(startingPoint.y - possibleShot.getY()) == 0)) {
							// Know possible shots is only where you might want
							// to shoot so if one left
							// or right is in it you must be at the edge.
							possibleShot.possibleUse();
						}
					}
				}
			}
			break;
		case Battleship:
			if (target.isVertical()) {
				for (Point startingPoint : hitSet) {
					for (Shot possibleShot : nextShots) {
						if ((Math.abs(startingPoint.x - possibleShot.getX()) == 0
								&& Math.abs(startingPoint.y - possibleShot.getY()) == 1)) {
							// Know possible shots is only where you might want
							// to shoot so if one up
							// or down is in it you must be at the edge.
							possibleShot.possibleUse();
						}
					}
				}
			} else {
				for (Point startingPoint : hitSet) {
					for (Shot possibleShot : nextShots) {
						if ((Math.abs(startingPoint.x - possibleShot.getX()) == 1
								&& Math.abs(startingPoint.y - possibleShot.getY()) == 0)) {
							// Know possible shots is only where you might want
							// to shoot so if one up
							// or down is in it you must be at the edge.
							possibleShot.possibleUse();
						}
					}
				}
			}
			break;
		case Hovercraft:
			switch (hitSet.size()) {
			case 1:
				// Only one so far could be anywhere around it
				for (Shot possibleShot : nextShots) {
					for (Point current : hitSet) {
						if (Math.abs(possibleShot.getX() - current.x) < 2
								&& Math.abs(possibleShot.getY() - current.y) < 2) {
							possibleShot.possibleUse();
						}
					}
				}
				break;
			case 2:
				if (twoTouching() != null) {
					// Two in a row
					List<Shot> usefulShots = new ArrayList<Shot>();
					List<Point> hitList = new ArrayList<Point>();
					if (hitSet.numberOfEachY().size() == 2) {
						// The two are going vertically
						hitList.addAll(hitSet);
						int biggerY;
						int smallerY;
						if (hitList.get(0).getY() > hitList.get(1).getY()) {
							biggerY = 0;
							smallerY = 1;
						} else {
							biggerY = 1;
							smallerY = 0;
						}
						usefulShots.add(new Shot(hitList.get(biggerY).y + 1, hitList.get(biggerY).x - 1));
						usefulShots.add(new Shot(hitList.get(biggerY).y + 1, hitList.get(biggerY).x + 1));
						usefulShots.add(new Shot(hitList.get(smallerY).y - 1, hitList.get(biggerY).x - 1));
						usefulShots.add(new Shot(hitList.get(smallerY).y - 1, hitList.get(biggerY).x + 1));
					} else {
						// The two are going horizontally
						hitList.addAll(hitSet);
						int biggerX;
						int smallerX;
						if (hitList.get(0).getX() > hitList.get(1).getX()) {
							biggerX = 0;
							smallerX = 1;
						} else {
							biggerX = 1;
							smallerX = 0;
						}
						usefulShots.add(new Shot(hitList.get(biggerX).x + 1, hitList.get(biggerX).y - 1));
						usefulShots.add(new Shot(hitList.get(biggerX).x + 1, hitList.get(biggerX).y + 1));
						usefulShots.add(new Shot(hitList.get(smallerX).x - 1, hitList.get(biggerX).y - 1));
						usefulShots.add(new Shot(hitList.get(smallerX).x - 1, hitList.get(biggerX).y + 1));
					}
					for (Shot current : nextShots) {
						if (usefulShots.contains(current)) {
							current.possibleUse();
						}
					}
				} else {
					List<Point> hitList = new ArrayList<Point>(hitSet);
					List<Shot> usefulShots = new ArrayList<Shot>();
					if (hitList.get(0).x > hitList.get(1).x) {
						usefulShots.add(new Shot(hitList.get(0).x + 1, hitList.get(0).y));
						usefulShots.add(new Shot(hitList.get(1).x - 1, hitList.get(1).y));
					} else {
						usefulShots.add(new Shot(hitList.get(1).x + 1, hitList.get(1).y));
						usefulShots.add(new Shot(hitList.get(0).x - 1, hitList.get(0).y));
					}
					if (hitList.get(0).y > hitList.get(1).y) {
						usefulShots.add(new Shot(hitList.get(0).x, hitList.get(0).y + 1));
						usefulShots.add(new Shot(hitList.get(1).x, hitList.get(1).y - 1));
					} else {
						usefulShots.add(new Shot(hitList.get(1).x, hitList.get(1).y + 1));
						usefulShots.add(new Shot(hitList.get(0).x, hitList.get(0).y - 1));
					}
				}
				break;
			default:
				List<Point> twoTouching = twoTouching();
				List<Shot> usefulShots = new ArrayList<Shot>();
				if (twoTouching == null) {
					return;
				}
				if (twoTouching.get(0).x == twoTouching.get(1).x) {
					// vertical
					for (Point current : hitSet) {
						if (current.x != twoTouching.get(0).x) {
							// Current is single square
							if (current.x > twoTouching.get(0).x) {
								usefulShots.add(new Shot(twoTouching.get(0).x + 2, twoTouching.get(0).y));
								usefulShots.add(new Shot(twoTouching.get(1).x + 2, twoTouching.get(1).y));
							} else {
								usefulShots.add(new Shot(twoTouching.get(0).x - 2, twoTouching.get(0).y));
								usefulShots.add(new Shot(twoTouching.get(1).x - 2, twoTouching.get(1).y));
							}
						}
					}
				} else {
					// Horizontal
					for (Point current : hitSet) {
						if (current.y != twoTouching.get(0).y) {
							// Current is single square
							if (current.y > twoTouching.get(0).y) {
								usefulShots.add(new Shot(twoTouching.get(0).x, twoTouching.get(0).y + 2));
								usefulShots.add(new Shot(twoTouching.get(1).x, twoTouching.get(1).y + 2));
							} else {
								usefulShots.add(new Shot(twoTouching.get(0).x - 2, twoTouching.get(0).y - 2));
								usefulShots.add(new Shot(twoTouching.get(1).x - 2, twoTouching.get(1).y - 2));
							}
						}
					}
				}
				for (Shot current : nextShots) {
					if (usefulShots.contains(current)) {
						current.possibleUse();
					}
				}
			}
			break;
		case AircraftCarrier:
			List<int[]> numberOfEachY = hitSet.numberOfEachY();
			List<int[]> numberOfEachX = hitSet.numberOfEachX();
			int xLimit;
			int yLimit;
			if (target.isVertical()) {
				xLimit = 3;
				yLimit = 4;
			} else {
				xLimit = 4;
				yLimit = 3;
			}
			if (numberOfEachX.size() < xLimit) {
				// We are still looking for another x
				boolean oneYIsBigger = false;
				int biggestY = 1;
				for (int[] current : numberOfEachY) {
					if (current[1] > 1) {
						oneYIsBigger = true;
						biggestY = current[0];
					}
				}
				if (oneYIsBigger) {
					// Know which y line the x will be on
					for (Shot possibleShot : nextShots) {
						if (possibleShot.getY() == biggestY) {
							possibleShot.possibleUse();
						}
					}
				} else {
					// Don't know which y line it'll be on could be anywhere
					// there's a y
					for (Shot possibleShot : nextShots) {
						for (Point blah : hitSet) {
							if (possibleShot.getY() == blah.y) {
								possibleShot.possibleUse();
							}
						}
					}
				}
			}
			if (numberOfEachY.size() < yLimit) {
				// We are still looking for another Y
				boolean oneXIsBigger = false;
				int biggestX = 1;
				for (int[] current : numberOfEachX) {
					if (current[1] > 1) {
						oneXIsBigger = true;
						biggestX = current[0];
					}
				}
				if (oneXIsBigger) {
					// Know which x line the y will be on
					for (Shot possibleShot : nextShots) {
						if (possibleShot.getX() == biggestX) {
							possibleShot.possibleUse();
						}
					}
				} else {
					// Don't know which x line it'll be on could be anywhere
					// there's a x
					for (Shot possibleShot : nextShots) {
						for (Point blah : hitSet) {
							if (possibleShot.getX() == blah.x) {
								possibleShot.possibleUse();
							}
						}
					}
				}
			}
			break;
		}
	}

	private List<Point> twoTouching() {
		List<Point> twoTouching = new ArrayList<Point>();
		for (Point first : hitSet) {
			for (Point second : hitSet) {
				if (((first.getX() == second.getX()) && Math.abs(first.getY() - second.getY()) == 1)
						|| ((first.getY() == second.getY()) && Math.abs(first.getX() - second.getX()) == 1)) {
					twoTouching.add(first);
					twoTouching.add(second);
					return twoTouching;
				}
			}
		}
		return null;
	}

	private Point largeShot() {
		int currentMinUse = getSquareUse(0);
		ArrayList<Integer> posSquares = new ArrayList<Integer>();
		posSquares.add(0);
		/*
		 * for (int i = 0; i < 12; i++) { System.out.println(); for (int j = 0;
		 * j < 12; j++) { System.out.print(" "+opponentGrid[i][j]+" "); } }
		 */
		for (int i = 1; i < 27; i++) {
			if (getSquareUse(i) < currentMinUse) {
				currentMinUse = getSquareUse(i);
				posSquares.clear();
				posSquares.add(i);
			}
		}
		// System.out.println("So choosing squares from:");
		for (int current : posSquares) {
			System.out.println(current);
		}
		int nextSquare = posSquares.get(rand.nextInt(posSquares.size()));
		return bestPointFromSquare(nextSquare);
	}

	private int getSquareUse(int square) {
		int uses = 0;
		Point startCoOrd = squareMap.get(square);
		if (!opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.UNUSED)) {
			// System.out.println("Square "+square+" first");
			uses++;
		}
		if (!opponentGrid[startCoOrd.x][startCoOrd.y + 1].equals(boardStates.UNUSED)) {
			// System.out.println("Square "+square+" second");
			uses++;
		}
		if (!opponentGrid[startCoOrd.x + 1][startCoOrd.y].equals(boardStates.UNUSED)) {
			// System.out.println("Square "+square+" third");
			uses++;
		}
		if (!opponentGrid[startCoOrd.x + 1][startCoOrd.y + 1].equals(boardStates.UNUSED)) {
			// System.out.println("Square "+square+" fourth");
			uses++;
		}
		// System.out.println("Square use "+square+" uses: "+uses);
		// System.out.println(opponentGrid[startCoOrd.x][startCoOrd.y]);
		// System.out.println(opponentGrid[startCoOrd.x+1][startCoOrd.y]);
		// System.out.println(opponentGrid[startCoOrd.x][startCoOrd.y+1]);
		// System.out.println(opponentGrid[startCoOrd.x+1][startCoOrd.y+1]);
		return uses;
	}

	private Point bestPointFromSquare(int square) {
		int minUsage;
		int currentUsage;
		ArrayList<Integer> possibleMinima = new ArrayList<Integer>();
		List<Point> possiblePoints = new ArrayList<Point>();
		possiblePoints.add(squareMap.get(square));
		possiblePoints.add(new Point(squareMap.get(square).x, squareMap.get(square).y + 1));
		possiblePoints.add(new Point(squareMap.get(square).x + 1, squareMap.get(square).y));
		possiblePoints.add(new Point(squareMap.get(square).x + 1, squareMap.get(square).y + 1));
		List<Point> pointlessPoints = new ArrayList<Point>();
		for (Point current : possiblePoints) {
			if (opponentGrid[current.x][current.y] != boardStates.UNUSED) {
				pointlessPoints.add(current);
			}
		}
		possiblePoints.removeAll(pointlessPoints);
		minUsage = getLocalUsage(possiblePoints.get(0));
		possibleMinima.add(0);
		for (int i = 1; i < possiblePoints.size(); i++) {
			currentUsage = getLocalUsage(possiblePoints.get(i));
			if (currentUsage < minUsage) {
				minUsage = currentUsage;
				possibleMinima.clear();
				possibleMinima.add(i);
			} else if (currentUsage == minUsage) {
				possibleMinima.add(i);
			}
		}
		return possiblePoints.get(possibleMinima.get(rand.nextInt(possibleMinima.size())));
		// Work out points around square to be used using pointIsValid method.
		// Loop through these if they've been used add 1 to a square diagonally
		// next to and 2 to a square directly next to.
		// Return point with lowest score.
	}

	private int getLocalUsage(Point possibleTarget) {
		int localUsage = 0;
		List<Point> lowImportancePossibilities = new ArrayList<Point>();
		if (pointIsValid(new Point(possibleTarget.x - 1, possibleTarget.y - 1))) {
			lowImportancePossibilities.add(new Point(possibleTarget.x - 1, possibleTarget.y - 1));
		}
		if (pointIsValid(new Point(possibleTarget.x - 1, possibleTarget.y + 1))) {
			lowImportancePossibilities.add(new Point(possibleTarget.x - 1, possibleTarget.y + 1));
		}
		if (pointIsValid(new Point(possibleTarget.x + 1, possibleTarget.y - 1))) {
			lowImportancePossibilities.add(new Point(possibleTarget.x + 1, possibleTarget.y - 1));
		}
		if (pointIsValid(new Point(possibleTarget.x + 1, possibleTarget.y + 1))) {
			lowImportancePossibilities.add(new Point(possibleTarget.x + 1, possibleTarget.y + 1));
		}
		List<Point> highImportancePossibilities = new ArrayList<Point>();
		if (pointIsValid(new Point(possibleTarget.x - 1, possibleTarget.y))) {
			highImportancePossibilities.add(new Point(possibleTarget.x - 1, possibleTarget.y));
		}
		if (pointIsValid(new Point(possibleTarget.x + 1, possibleTarget.y))) {
			highImportancePossibilities.add(new Point(possibleTarget.x + 1, possibleTarget.y));
		}
		if (pointIsValid(new Point(possibleTarget.x, possibleTarget.y + 1))) {
			highImportancePossibilities.add(new Point(possibleTarget.x, possibleTarget.y + 1));
		}
		if (pointIsValid(new Point(possibleTarget.x, possibleTarget.y - 1))) {
			highImportancePossibilities.add(new Point(possibleTarget.x, possibleTarget.y - 1));
		}
		for (Point currentPoint : lowImportancePossibilities) {
			if (opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.HIT)
					|| opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.MISS)) {
				localUsage++;
			}
		}
		for (Point currentPoint : highImportancePossibilities) {
			if (opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.HIT)
					|| opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.MISS)) {
				localUsage++;
				localUsage++;
			}
		}
		return localUsage;
	}

	private boolean pointIsValid(Point p) {
		return (p.x < 12 && p.x > -1 && p.y < 12 && p.y > -1 && (!(p.x > 5 && p.y < 6)));
	}

	private boolean shipFits(Target target) {
		switch (target.getType()) {
		case Destroyer:
			if (hitSet.size() == 1) {
				return true;
			}
			break;
		case Cruiser:
			if (hitSet.numberOfEachX().size() < 3 && hitSet.numberOfEachY().size() < 2 && (!target.isVertical())) {
				return true;
			}
			if (hitSet.numberOfEachX().size() < 2 && hitSet.numberOfEachY().size() < 3 && target.isVertical()) {
				return true;
			}
			break;
		case Battleship:
			if (hitSet.numberOfEachX().size() < 4 && hitSet.numberOfEachY().size() < 2 && (!target.isVertical())) {
				return true;
			}
			if (hitSet.numberOfEachX().size() < 2 && hitSet.numberOfEachY().size() < 4 && target.isVertical()) {
				return true;
			}
			break;
		case AircraftCarrier:
			if (hitSet.numberOfEachX().size() < 5 && hitSet.numberOfEachY().size() < 4 && (!target.isVertical())) {
				return true;
			}
			if (hitSet.numberOfEachX().size() < 4 && hitSet.numberOfEachY().size() < 5 && target.isVertical()) {
				return true;
			}
			break;
		case Hovercraft:
			switch (hitSet.size()) {
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				// If there's 3 in the same row can't be hovercraft
				for (int[] current : hitSet.numberOfEachX()) {
					if (current[1] > 2) {
						return false;
					}
				}
				for (int[] current : hitSet.numberOfEachY()) {
					if (current[1] > 2) {
						return false;
					}
				}
			}
		}
		return false;

	}

	/*
	 * Inner Classes Contained Below:
	 */
	private class Target {
		private ShipType type;
		private boolean vertical;

		public Target(ShipType type, boolean vertical) {
			this.type = type;
			this.vertical = vertical;
		}

		public ShipType getType() {
			return type;
		}

		/*
		 * public void setType(ShipType type) {this.type=type;}
		 */

		public boolean isVertical() {
			return vertical;
		}
		/*
		 * public void setVertical(boolean vertical) {this.vertical=vertical;}
		 */
	}

	public class Shot {
		private int x;
		private int y;
		private int possibleUsefullness;

		public Shot(int x, int y) {
			this.x = x;
			this.y = y;
			this.possibleUsefullness = 0;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getUsefullnness() {
			return possibleUsefullness;
		}

		public void possibleUse() {
			possibleUsefullness++;
		}

		public boolean isValid() {
			return (x < 12 && x > -1 && y < 12 && y > -1 && (!(x > 5 && y < 6)));
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Shot)) {
				return false;
			}
			Shot otherShot = (Shot) o;
			return ((this.getX() == otherShot.getX()) && (this.getY() == otherShot.getY()));
		}

		@Override
		public int hashCode() {
			return (x * 50) + y;
		}
	}

	private class HitSet extends HashSet<Point> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1469575265874868882L;

		public List<int[]> numberOfEachX() {
			Set<Integer> possibleXs = new HashSet<Integer>();
			for (Point current : this) {
				possibleXs.add(current.x);
			}
			List<int[]> numberOfEachX = new ArrayList<int[]>();
			for (Integer nextX : possibleXs) {
				numberOfEachX.add(new int[] { nextX, 0 });
			}
			for (Point current : this) {
				for (int[] numOfX : numberOfEachX) {
					if (current.x == numOfX[0]) {
						numOfX[1]++;
						break;
					}
				}
			}
			return numberOfEachX;
		}

		public List<int[]> numberOfEachY() {
			Set<Integer> possibleYs = new HashSet<Integer>();
			for (Point current : this) {
				possibleYs.add(current.y);
			}
			List<int[]> numberOfEachY = new ArrayList<int[]>();
			for (Integer nextY : possibleYs) {
				numberOfEachY.add(new int[] { nextY, 0 });
			}
			for (Point current : this) {
				for (int[] numOfY : numberOfEachY) {
					if (current.y == numOfY[0]) {
						numOfY[1]++;
						break;
					}
				}
			}
			return numberOfEachY;
		}
	}

}
