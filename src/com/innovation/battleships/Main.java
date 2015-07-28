package com.innovation.battleships;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.innovation.battleships.engine.BattleshipCompetition;
import com.innovation.battleships.engine.Player;
import com.innovation.battleships.player.FinalPlayer;
import com.innovation.battleships.player.RandomPlayer;
import com.innovation.battleships.player.SecondAttempt;

public class Main {

	public static void main(String[] args) {
		Player op1 = new RandomPlayer();
		Player op2 = new SecondAttempt();

		BattleshipCompetition bc = new BattleshipCompetition(op1, op2, 500, 500, false);
		HashMap<Player, Integer> scores = bc.runCompetition();

		Iterator<Map.Entry<Player, Integer>> it = scores.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Player, Integer> pair = it.next();
			System.out.println("Player: " + pair.getKey().toString() + " Score:" + pair.getValue().toString());
			it.remove();
		}
	}

}
