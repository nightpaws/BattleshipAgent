package com.innovation.battleships.player;
import java.awt.Point;
import java.util.List;
import java.util.Random;

import com.innovation.battleships.engine.Player;
import com.innovation.battleships.engine.Ship;
import com.innovation.battleships.engine.ShipOrientation;

public class AIPlayer implements Player {

	Random rand = new Random();
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void newMatch(String opponent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newGame(Integer timeSpan) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeShips(List<Ship> ships) {
		ShipOrientation[] orientations = new ShipOrientation[] {ShipOrientation.Up, ShipOrientation.Right, ShipOrientation.Down, ShipOrientation.Left};
		int randomInt;
		boolean valid;
		boolean collides;
		int x;
		int y;
		for (Ship s: ships){
			
			randomInt = rand.nextInt(4);
			valid = false;
			collides = true;
			
			// while collides is true and valid is false
			while ( !(!collides && valid)){
				x = rand.nextInt(12);
				y = rand.nextInt(12);
				
				//while correct coordinates for the grid found
				while (x >= 6 && y <= 6) {
					x = rand.nextInt(12);
					y = rand.nextInt(12);
				}
				
				s.place(new Point(x, y), orientations[randomInt]);
				valid = s.isValid();
				
				//checks if ships collides with other ships
				for (Ship otherS: ships){
					if (s.getType()!=otherS.getType() && s.collidesWith(otherS)){
						collides = true;
						break;
					}
								
				}
				
			}
			
		}
		
	}
	

	@Override
	public Point getShot() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameLost() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void matchOver() {
		// TODO Auto-generated method stub
		
	}

}
