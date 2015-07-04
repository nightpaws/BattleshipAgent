package com.innovation.battleships.engine;

import java.awt.Point;
import java.util.List;

/**
 * Created by kierandouglas on 26/05/15.
 */
public interface Player {

	String getName();

	int getVersion();

	void newMatch(String opponent);

	void newGame(Integer timeSpan);

	void placeShips(List<Ship> ships);

	Point getShot();

	void opponentShot(Point shot);

	void shotHit(Point shot, boolean sunk);

	void shotMiss(Point shot);

	void gameWon();

	void gameLost();

	void matchOver();

	String toString();
	
}
