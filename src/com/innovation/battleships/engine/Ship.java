package com.innovation.battleships.engine;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

/**
 * Created by kierandouglas on 26/05/15.
 */
public class Ship {
	
	private Set<Point> locations;
	private ShipType type;
	private boolean isPlaced;

	public Ship(ShipType type) {
		this.type = type;
	}

	public boolean getIsPlaced() {
		return isPlaced;
	}

	public ShipType getType() {
		return this.type;
	}

	public void place(Point location, ShipOrientation orientation) {
		this.isPlaced = true;
		this.locations = ShipShape.get(this.type, orientation, location.x, location.y);
	}

	public boolean isValid() {
		if (!this.isPlaced) {
			return false;
		}

		for (Point shipLocation : this.getAllLocations()) {
			Rectangle board = new Rectangle(12, 12);

			if (!board.contains(shipLocation))
				return false;

			if (shipLocation.x >= 6 && shipLocation.y <= 6)
				return false;
		}

		return true;
	}

	public boolean isAt(Point location) {
		for (Point shipLocation : this.getAllLocations()) {
			if (location.equals(shipLocation))
				return true;
		}

		return false;
	}

	public Iterable<Point> getAllLocations() {
		return locations;
	}

	public boolean collidesWith(Ship otherShip) {
		for (Point otherShipLocation : otherShip.getAllLocations()) {
			if (this.isAt(otherShipLocation)) {
				return true;
			}
		}

		return false;
	}

	public boolean isSunk(Iterable<Point> shots) {
		boolean isSunk = true;
		
		for (Point location : this.getAllLocations()) {
			boolean isHit = false;
			
			for (Point shot : shots) {
				if (shot.getX() == location.getX() && shot.getY() == location.getY()) {
					isHit = true;
				}
			}
			
			if (!isHit)
				isSunk = false;
		}

		return isSunk;
	}

}
