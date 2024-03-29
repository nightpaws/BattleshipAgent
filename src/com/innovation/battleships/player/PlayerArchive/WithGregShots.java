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

public class WithGregShots implements Player {
		private boolean largeControl;
		Random rand = new Random();
		// Store opponents remaining ships
		List<Ship> opponentShips = new ArrayList<Ship>();
		// Store all attack moves made? - (Miss = M | Hit = H)
		boardStates[][] opponentGrid = new boardStates[11][11];
		
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
		Map<Integer,Point> squareMap;
		public enum boardStates{
			HIT,MISS,INVALID,UNUSED
		}
		
		
		
		/**
		 * Constructor
		 */
		public WithGregShots() {
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
			
			squareMap = new HashMap<Integer,Point>();
			squareMap.put(0,new Point(0,0));
			squareMap.put(1,new Point(2,0));
			squareMap.put(2,new Point(4,0));
			squareMap.put(3,new Point(0,2));
			squareMap.put(4,new Point(2,2));
			squareMap.put(5,new Point(4,2));
			squareMap.put(6,new Point(0,4));
			squareMap.put(7,new Point(2,4));
			squareMap.put(8,new Point(4,4));
			squareMap.put(9,new Point(0,6));
			squareMap.put(10,new Point(2,6));
			squareMap.put(11,new Point(4,6));
			squareMap.put(12,new Point(6,6));
			squareMap.put(13,new Point(8,6));
			squareMap.put(14,new Point(10,6));
			squareMap.put(15,new Point(0,8));
			squareMap.put(16,new Point(2,8));
			squareMap.put(17,new Point(4,8));
			squareMap.put(18,new Point(6,8));
			squareMap.put(19,new Point(8,8));
			squareMap.put(20,new Point(10,8));
			squareMap.put(21,new Point(0,10));
			squareMap.put(22,new Point(2,10));
			squareMap.put(23,new Point(4,10));
			squareMap.put(24,new Point(6,10));
			squareMap.put(25,new Point(8,10));
			squareMap.put(26,new Point(10,10));
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
			// block off top right of field
			// populate initial grid with empty spaces (for display)
			for (int i = 0; i < 11; i++) {
				for (int j = 0; j < 11; j++) {
					opponentGrid[i][j] = boardStates.UNUSED;
				}
			}
			// Invalidate initial corner of grid
			for (int i = 6; i < 11; i++) {
				for (int j = 0; j < 7; j++) {
					opponentGrid[i][j] = boardStates.INVALID; // invalid
				}
			}
		}

		@Override
		public void placeShips(java.util.List<Ship> ships) {
			
			//position where our ship has been placed and hit by opponent many times
			boolean onBlackPosition;
			boolean valid;
			boolean collides;
			ShipOrientation[] orientations = new ShipOrientation[] {ShipOrientation.Up, ShipOrientation.Right, ShipOrientation.Down, ShipOrientation.Left};
			
			Map<Integer,Point> sortedOppentHitShots= new TreeMap<Integer,Point>();
			for (Point p: opponentHitShotsStats.keySet()){
				sortedOppentHitShots.put(opponentHitShotsStats.get(p), p);
			}
//			System.out.println(sortedOppentHitShots);
			
			for (Ship s : ships) {
				
				valid = false;
				collides = true;
				onBlackPosition = true;
				
				
				if (gameCounter>=30 && gameCounter<=60){
					int specialPositioningRandom=0;
					if(gameCounter<40)
						specialPositioningRandom=0;
					else if(gameCounter<50)
						specialPositioningRandom=1;
					else if(gameCounter<=60)
						specialPositioningRandom=2;
					
					specialPlace(specialPositioningRandom,orientations,s);
					
					continue;
				}
				else{
					if (gameCounter>60 && specialPositioningIsWorking()!=3){
						System.out.println("positioning chosen"+ specialPositioningIsWorking());
						specialPlace(specialPositioningIsWorking(),orientations,s);
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
							ArrayList<Integer> keys = new ArrayList<Integer>(sortedOppentHitShots.keySet()); 
							for(int i=keys.size()-1;i>=0;i--){
								if (counter>10)
									break;
								counter++;
								if (s.isAt(sortedOppentHitShots.get(keys.get(i)))){
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

		    if(largeControl){
		       return largeShot();
		    }else{
		    	return smallShot();
		    }
		}

		private Point smallShot() {
			// TODO Auto-generated method stub
			return null;
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
			int defaultPositioningAverageSunkTime=getAverage(positionSunkStatsDefault);
			int cornerPositioningAverageSunkTime=getAverage(positionSunkStatsCorner);
			int middlePositioningAverageSunkTime=getAverage(positionSunkStatsMiddle);
			int togetherPositioningAverageSunkTime=getAverage(positionSunkStatsTogether);
			int positioning;
			int positioningValue;
			
			if (cornerPositioningAverageSunkTime<middlePositioningAverageSunkTime && cornerPositioningAverageSunkTime<togetherPositioningAverageSunkTime){
				positioning=0;
				positioningValue=cornerPositioningAverageSunkTime;
			}
			else if (middlePositioningAverageSunkTime<cornerPositioningAverageSunkTime && middlePositioningAverageSunkTime<togetherPositioningAverageSunkTime){
				positioning=1;
				positioningValue=middlePositioningAverageSunkTime;
			}
			else{
				positioning=2;
				positioningValue=cornerPositioningAverageSunkTime;
			}
			
			//TODO: fix checks when to use which strategy
			System.out.println(defaultPositioningAverageSunkTime);
			System.out.println(middlePositioningAverageSunkTime);
			System.out.println(cornerPositioningAverageSunkTime);
			System.out.println(togetherPositioningAverageSunkTime);
			if (defaultPositioningAverageSunkTime>positioningValue)
				return positioning;
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
		
		
	    private Point largeShot(){
	        int currentMinUse=getSquareUse(0);
	        int nextSquare;
	        ArrayList<Integer> posSquares = new ArrayList<Integer>();
	        for(int i=1;i<27;i++){
	            if(getSquareUse(i)<currentMinUse){
	                posSquares.clear();
	                posSquares.add(i);
	            }
	        }
	        nextSquare=posSquares.get(rand.nextInt(posSquares.size()-1));
	        return bestPointFromSquare(nextSquare);
	    }

	    private int getSquareUse(int square){
	        int uses=0;
	        Point startCoOrd=squareMap.get(square);
	        if(opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.HIT)||opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.MISS)){
	            uses++;
	        }
	        if(opponentGrid[startCoOrd.x][startCoOrd.y+1].equals(boardStates.HIT)||opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.MISS)){
	            uses++;
	        }
	        if(opponentGrid[startCoOrd.x+1][startCoOrd.y].equals(boardStates.HIT)||opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.MISS)){
	            uses++;
	        }
	        if(opponentGrid[startCoOrd.x+1][startCoOrd.y+1].equals(boardStates.HIT)||opponentGrid[startCoOrd.x][startCoOrd.y].equals(boardStates.MISS)){
	            uses++;
	        }
	        return uses;
	    }

	    private Point bestPointFromSquare(int square){
	    	int minUsage;
	    	int currentUsage;
	    	ArrayList<Integer> possibleMinima = new ArrayList<Integer>();
	    	List<Point> possiblePoints = new ArrayList<Point>();
	        possiblePoints.add(squareMap.get(square));
	        possiblePoints.add(new Point(squareMap.get(square).x,squareMap.get(square).y+1));
	        possiblePoints.add(new Point(squareMap.get(square).x+1,squareMap.get(square).y));
	        possiblePoints.add(new Point(squareMap.get(square).x+1,squareMap.get(square).y+1));
	        minUsage=getLocalUsage(possiblePoints.get(0));
	        for(int i=1;i<4;i++){
	        	currentUsage=getLocalUsage(possiblePoints.get(i));
	        	if(currentUsage<minUsage){
	        		minUsage=currentUsage;
	        		possibleMinima.clear();
	        		possibleMinima.add(i);
	        	}else if(currentUsage==minUsage){
	        		possibleMinima.add(i);
	        	}
	        }
	        return possiblePoints.get(possibleMinima.get(rand.nextInt(possibleMinima.size()-1)));
	        //Work out points around square to be used using pointIsValid method.
	        //Loop through these if they've been used add 1 to a square diagonally
	        //next to and 2 to a square directly next to.
	        //Return point with lowest score.
	    }

	    private int getLocalUsage(Point possibleTarget){
	    	int localUsage=0;
	    	List<Point> lowImportancePossibilities = new ArrayList<Point>();
	    	lowImportancePossibilities.add(new Point(possibleTarget.x-1,possibleTarget.y-1));
	    	lowImportancePossibilities.add(new Point(possibleTarget.x-1,possibleTarget.y+1));
	    	lowImportancePossibilities.add(new Point(possibleTarget.x+1,possibleTarget.y-1));
	    	lowImportancePossibilities.add(new Point(possibleTarget.x+1,possibleTarget.y+1));
	    	lowImportancePossibilities = removeInvalid(lowImportancePossibilities);
	    	List<Point> highImportancePossibilities = new ArrayList<Point>();
	    	highImportancePossibilities.add(new Point(possibleTarget.x-1,possibleTarget.y));
	    	highImportancePossibilities.add(new Point(possibleTarget.x+1,possibleTarget.y));
	    	highImportancePossibilities.add(new Point(possibleTarget.x,possibleTarget.y+1));
	    	highImportancePossibilities.add(new Point(possibleTarget.x,possibleTarget.y-1));
	    	highImportancePossibilities= removeInvalid(highImportancePossibilities);
	    	for(Point currentPoint:lowImportancePossibilities){
	    		if(opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.HIT)||opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.MISS)){
	    			localUsage++;
	    		}
	    	}
	    	for(Point currentPoint:highImportancePossibilities){
	    		if(opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.HIT)||opponentGrid[currentPoint.x][currentPoint.y].equals(boardStates.MISS)){
	    			localUsage++;
	    		}
	    	}
	    	return localUsage;
	    }

	    private List<Point> removeInvalid(List<Point> clearUp){
	    	List<Point> invalidPoints=new ArrayList<Point>();
	    	for(Point currentPoint:clearUp){
	    		if(pointIsInvalid(currentPoint)){
	    			invalidPoints.add(currentPoint);
	    		}
	    	}
	    	clearUp.removeAll(invalidPoints);
	    	return clearUp;
	    }



	private boolean pointIsInvalid(Point p){
        return ((p.x>5)&&(p.y<6));
	}
	
	private Point getNearestSquare(Point currentPoint){
		int newX = currentPoint.x/2;
		newX=newX*2;
		int newY=currentPoint.y/2;
		newY=newY*2;
		return new Point(newX,newY);
	}

}
