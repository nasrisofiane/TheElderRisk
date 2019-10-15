package model;

import java.util.ArrayList;

public class Game {
	private int id ; 
	private ArrayList<Integer> playerList = new ArrayList();	
	
	/*public  void round(Player player) {
		int pawnToPlace = 3;
		Territory territory = player.getPlayerTerritories().get(0);
		Territory targetTerritory = player.getPlayerTerritories().get(0);
		territory.attack(territory, 1, 2);
		territory.moveFortify(territory, targetTerritory);
	}*/
	
	public  void addPlayer(Player player) {
		playerList.add(player.getId());
	}
	public  void deletePlayer() {
		
	}
	public  void startGame() {
		
	}
	public  void endGame() {
		
	}
}


