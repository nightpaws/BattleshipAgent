package com.innovation.battleships.player;

import com.innovation.battleships.engine.ShipType;

public class Target {
	private ShipType type;
	private boolean vertical;
	
	public Target(ShipType type,boolean vertical){
		this.type=type;
		this.vertical=vertical;
	}

	public ShipType getType() {
		return type;
	}

	public void setType(ShipType type) {
		this.type = type;
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}
	
	
	
}
