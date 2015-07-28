package com.innovation.battleships.player.PlayerArchive;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
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
	private Set<Point> opponentHitShots;
	//keep the entire statistics of where we have been hit, while one above only black positions
	//where we have been successfully hit more than 10 time
	private Map<Point,Integer> opponentHitShotsStats;
	private int gameCounter;
	private PositioningType specialPositioning;
	//keep track of how long it takes a ship to be sunk by random positioning
	private Map<Integer,Integer> positionSunkStatsDefault;
	private Map<Integer,Integer> positionSunkStatsCorner;
	private Map<Integer,Integer> positionSunkStatsTogether;
	private Map<Integer,Integer> positionSunkStatsMiddle;
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
		this.opponentHitShots = new HashSet<Point>();
		this.opponentHitShotsStats = new HashMap<Point,Integer>();
		this.gameCounter=0;
		this.positionSunkStatsCorner = new HashMap<Integer,Integer>();
		this.positionSunkStatsDefault = new HashMap<Integer,Integer>();
		this.positionSunkStatsMiddle = new HashMap<Integer,Integer>();
		this.positionSunkStatsTogether = new HashMap<Integer,Integer>();
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
		
		//position where our ship has been placed and hit by opponent many times
		boolean onBlackPosition;
		boolean valid;
		boolean collides;
		ShipOrientation[] orientations = new ShipOrientation[] {ShipOrientation.Up, ShipOrientation.Right, ShipOrientation.Down, ShipOrientation.Left};
		
		Map<Integer,Point> sortedOpponentHitShots= new TreeMap<Integer,Point>();
		
		for (Point p: opponentHitShotsStats.keySet()){
			sortedOpponentHitShots.put(opponentHitShotsStats.get(p), p);
		}
		
		for (Ship s : ships) {
			
			valid = false;
			collides = true;
			onBlackPosition = true;
			
			
//			if (gameCounter>=30 && gameCounter<=60){
			if (gameCounter>=3 && gameCounter<=12){
				int specialPositioningRandom=0;
//				if(gameCounter<40)
				if(gameCounter<6)
					specialPositioningRandom=0;
//				else if(gameCounter<50)
				else if(gameCounter<9)
					specialPositioningRandom=1;
//				else if(gameCounter<=60)
				else if(gameCounter<=12)
					specialPositioningRandom=2;
				
				specialPlace(specialPositioningRandom,orientations,s);
				
				this.ourShips.add(s);
			}
			else{
//				if (gameCounter>60 && specialPositioningIsWorking()!=3){
				int positioninChosen=0;
				//division by zero in getAverage function fix
				if (gameCounter>12)
					positioninChosen=specialPositioningIsWorking();
				
				if (gameCounter>12 && positioninChosen!=3){
					specialPlace(positioninChosen,orientations,s);
				}
				else{
					specialPositioning=PositioningType.DEFAULT;
					while (!valid || collides || onBlackPosition){
						
			
						int randomInt = rand.nextInt(4);
			
						int x = rand.nextInt(12);
						int y = rand.nextInt(12);
			
						while (x >= 6 && y <= 6) {
							x = rand.nextInt(12);
							y = rand.nextInt(12);
						}
						
						s.place(new Point(x, y), orientations[randomInt]);
						
						valid = s.isValid();
						
						//checks if placed on blackPosition
						onBlackPosition=false;
		
						int counter=0;
						ArrayList<Integer> keys = new ArrayList<Integer>(sortedOpponentHitShots.keySet()); 
						for(int i=keys.size()-1;i>=0;i--){
							if (counter>10)
								break;
							counter++;
							if (s.isAt(sortedOpponentHitShots.get(keys.get(i)))){
								onBlackPosition=true;
								break;
							}
							
						}
						
						//if not on blackPosition try again
						if (onBlackPosition)
							continue;
		
						collides=false;
						for (Ship otherS: ships){
							if (s.getType()!=otherS.getType() && otherS.getAllLocations()!=null && s.collidesWith(otherS)){
								collides = true;
								break;
							}
										
						}
					}
					
					this.ourShips.add(s);
				}
			}
		}


		System.out.println("Default:" +positionSunkStatsDefault);
		System.out.println("Corner:" +positionSunkStatsCorner);
		System.out.println("Middle:"+positionSunkStatsMiddle);
		System.out.println("Together: " + positionSunkStatsTogether);
		
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
		
		switch (specialPositioning){
		case DEFAULT:
				positionSunkStatsDefault.put(gameCounter, positionSunkStatsDefault.getOrDefault(gameCounter, 0)+1);
			break;
		case MIDDLE:
			positionSunkStatsMiddle.put(gameCounter, positionSunkStatsMiddle.getOrDefault(gameCounter, 0)+1);
			break;
		case CORNERS:
			positionSunkStatsCorner.put(gameCounter, positionSunkStatsCorner.getOrDefault(gameCounter, 0)+1);
			break;
		case TOGETHER:
			positionSunkStatsTogether.put(gameCounter, positionSunkStatsTogether.getOrDefault(gameCounter, 0)+1);
			break;
		}
		System.out.println("Positioning:"+specialPositioning);
		System.out.println("Default:" +positionSunkStatsDefault);
		System.out.println("Corner:" +positionSunkStatsCorner);
		System.out.println("Middle:"+positionSunkStatsMiddle);
		System.out.println("Together: " + positionSunkStatsTogether);
		
		for (Ship s: ourShips){
			if (s.isAt(shot)){
				opponentHitShotsStats.put(shot, opponentHitShotsStats.getOrDefault(shot, 0)+1);

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
		gameCounter++;
	}

	@Override
	public void gameLost() {
		gameCounter++;
	}

	@Override
	public void matchOver() {

	}

	private enum PositioningType{
		CORNERS,TOGETHER,MIDDLE,DEFAULT
	}
	
	private void specialPlace(int specialPositioningRandom,ShipOrientation[] orientations, Ship s){
		switch (specialPositioningRandom){
		case 0:
			specialPositioning=PositioningType.CORNERS;
			break;
		case 1:
			specialPositioning=PositioningType.MIDDLE;
			break;
		case 2:
			specialPositioning=PositioningType.TOGETHER;
			break;
		}
		
		switch (s.getType()){
			case Destroyer:
				switch (specialPositioning){
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
				switch (specialPositioning){
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
				switch (specialPositioning){
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
				switch (specialPositioning){
				case CORNERS:
					s.place(new Point(1, 0), orientations[3]);
					break;
				case MIDDLE:
					s.place(new Point(4, 7), orientations[0]);//maybe 4/5 for y
					break;
				case TOGETHER:
					s.place(new Point(3, 7), orientations[0]);//maybe 4/5 for y
					break;
				}
				break;
			case Hovercraft:
				switch (specialPositioning){
				case CORNERS:
					s.place(new Point(7, 8), orientations[0]);
					break;
				case MIDDLE:
					s.place(new Point(4, 6), orientations[0]);//maybe 4 for y
					break;
				case TOGETHER:
					s.place(new Point(3, 6), orientations[0]);//maybe 4 for y
					break;
				}
				break;
		}
	}
	
	private int specialPositioningIsWorking(){
		
		//TODO: fix selection of strategy, wrong chosen
		//Average values calculated correctly
		//not the correct strategy selected after this
		int defaultPositioningAverageSunkTime=getAverage(positionSunkStatsDefault);
		int cornerPositioningAverageSunkTime=getAverage(positionSunkStatsCorner);
		int middlePositioningAverageSunkTime=getAverage(positionSunkStatsMiddle);
		int togetherPositioningAverageSunkTime=getAverage(positionSunkStatsTogether);
		
		System.out.println("_____	Stats Below ___________");
		System.out.println("Default Positioining Average sunk time:"+defaultPositioningAverageSunkTime);
		System.out.println("Middle Positioining Average sunk time:"+middlePositioningAverageSunkTime);
		System.out.println("Corner Positioining Average sunk time:"+cornerPositioningAverageSunkTime);
		System.out.println("Together Positioining Average sunk time:"+togetherPositioningAverageSunkTime);
		
		if (cornerPositioningAverageSunkTime>=middlePositioningAverageSunkTime && cornerPositioningAverageSunkTime>=togetherPositioningAverageSunkTime
				&& cornerPositioningAverageSunkTime>=defaultPositioningAverageSunkTime)
			return 0;
		else if (middlePositioningAverageSunkTime>=cornerPositioningAverageSunkTime && middlePositioningAverageSunkTime>=togetherPositioningAverageSunkTime
				&& middlePositioningAverageSunkTime>=defaultPositioningAverageSunkTime )
			return 1;
		else if (togetherPositioningAverageSunkTime>=cornerPositioningAverageSunkTime && togetherPositioningAverageSunkTime>=defaultPositioningAverageSunkTime
				&& togetherPositioningAverageSunkTime>=middlePositioningAverageSunkTime )
			return 2;
		else
			return 3;
		
	
	}
	
	private int getAverage(Map<Integer,Integer> data){
		int total=0;
		int counter=0;
		for (int game: data.values()){
			total+=game;
			counter+=1;
		}
		return total/counter;
	}
}
