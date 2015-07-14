package com.innovation.battleships.player;

import java.awt.Point;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipType;

/**
 * Created by [Team Name] on [Date]
 */
public class BetterPlayerCM implements Player {

	//NonSpecific elements
	protected Random rand;

	//Eternal Counters
	protected int[][] enemyHitHeatmap = new int[11][11];
	protected int gameCounter;

	//Game Specific Variables
	protected int[][] currentEnemyGrid = new int[11][11];
	protected Set<ShipType> currentEnemyShips;
	
	//Turn Specific Variables
	protected boolean attackHit;
	

	public BetterPlayerCM() {
		rand = new Random();
		gameCounter = 0;
		attackHit = false;
		
	}

	@Override
	public String getName() {
		return "Potshot";
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void newMatch(String opponent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void newGame(Integer timeSpan) {
		currentEnemyShips = Collections.synchronizedSet(EnumSet.noneOf(ShipType.class));
		
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
		// TODO Auto-generated method stub

	}

	@Override
	public Point getShot() {
		// Stuff.....

		// hit!
		attackHit = true;
		
		////////MY TURN////////
		
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
		
		attackHit = false;
		//DONE NOW
		return new Point(x, y);
	}

	@Override
	public void opponentShot(Point shot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shotHit(Point shot, boolean sunk) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shotMiss(Point shot) {
		// TODO Auto-generated method stub

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

}
