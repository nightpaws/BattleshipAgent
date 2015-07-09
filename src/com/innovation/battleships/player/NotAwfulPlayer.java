package com.innovation.battleships.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.util.Pair;

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
	
	/*
	 * Ship placement related
	 */
	
	//our ships
	private List<Ship> ourShips;
	//Point where our ship has been positioned and hit n number of times over the matches
	private List<Point> opponentHitShots;
	//keep the entire statistics of where we have been hit, while one above only black positions
	//where we have been successfully hit more than 10 time
	// TODO: discuss whether 10 times(more or less)
	private Map<Point,Integer> opponentHitShotsStats;
	private int matchCounter;
	private SpecialPositioning specialPositioning;
	//keep track of how long it takes a ship to be sunk by random positioning
	private Map<SpecialPositioning,Integer> defaultPositioningSunkStats;
	//keep track of how long it takes a ship to be sunk by special positioning t
	private Map<SpecialPositioning,Integer> specialPositioningSunkStats;

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
		
		this.ourShips = new ArrayList<Ship>();
		this.opponentHitShots = new ArrayList<Point>();
		this.opponentHitShotsStats = new HashMap<Point,Integer>();
		this.matchCounter=0;
		this.defaultPositioningSunkStats= new HashMap<SpecialPositioning,Integer>();
		this.specialPositioningSunkStats= new HashMap<SpecialPositioning,Integer>();
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
		matchCounter++;
	}

	@Override
	public void newGame(Integer timeSpan) {

	}

	@Override
	public void placeShips(java.util.List<Ship> ships) {
//		for (Ship s : ships) {
//			ShipOrientation[] orientations = new ShipOrientation[] { ShipOrientation.Up, ShipOrientation.Right,
//					ShipOrientation.Down, ShipOrientation.Left };
//
//			int randomInt = rand.nextInt(4);
//
//			int x = rand.nextInt(12);
//			int y = rand.nextInt(12);
//
//			while (x >= 6 && y <= 6) {
//				x = rand.nextInt(12);
//				y = rand.nextInt(12);
//			}
//
//			s.place(new Point(x, y), orientations[randomInt]);
//		}
		
		ShipOrientation[] orientations = new ShipOrientation[] {ShipOrientation.Up, ShipOrientation.Right, ShipOrientation.Down, ShipOrientation.Left};
		int randomInt;
		boolean valid;
		boolean collides;
		//position where our ship has been placed and hit by opponent many times
		boolean onBlackPosition;
		int specialPositioningRandom;
		int x;
		int y;
		for (Ship s: ships){
			
			if (matchCounter%10==0){
				//special positioning:
				//1.Place ships in corners
				//2.Together
				//3.in middle
				specialPositioningRandom= rand.nextInt(3);
				
				switch (specialPositioningRandom){
				case 0:
					specialPositioning=SpecialPositioning.CORNERS;
					break;
				case 1:
					specialPositioning=SpecialPositioning.MIDDLE;
					break;
				case 2:
					specialPositioning=SpecialPositioning.TOGETHER;
					break;
				}
				
				//add logic
			}
			
			randomInt = rand.nextInt(4);
			valid = false;
			collides = true;
			onBlackPosition = false;
			// while collides is true and valid is false
			while ( !(!collides && valid) && !onBlackPosition){
				x = rand.nextInt(12);
				y = rand.nextInt(12);
				
				//while correct coordinates for the grid found
				while (x >= 6 && y <= 6) {
					x = rand.nextInt(12);
					y = rand.nextInt(12);
				}
				
				s.place(new Point(x, y), orientations[randomInt]);
				

				//checks if placed on blackPosition
				for (Point p:opponentHitShots){
					if (s.isAt(p))
						onBlackPosition=true;
				}
				
				//if not on blackPosition continue verifying if placement is valid
				if (onBlackPosition)
					continue;
				
				valid = s.isValid();
				
				//checks if ships collides with other ships
				for (Ship otherS: ships){
					if (s.getType()!=otherS.getType() && s.collidesWith(otherS)){
						collides = true;
						break;
					}
								
				}
				
			}
			
			this.ourShips.add(s);
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
		for (Ship s: ourShips){
			if (s.isAt(shot)){
				
				//check if special positioning
				//add logic here
				
				opponentHitShotsStats.put(shot, opponentHitShotsStats.getOrDefault(shot, 0)+1);
				if (opponentHitShotsStats.get(shot)>=10)
					opponentHitShots.add(shot);
			}
		}
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

	private enum SpecialPositioning{
		CORNERS,TOGETHER,MIDDLE
	}
}
