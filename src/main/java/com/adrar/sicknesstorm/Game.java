package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
	private int id; 
	private List<Player> playerList = new ArrayList();	
	
	public void round(Player player) {
		/*int pawnToPlace = 3;
		Territory territory = player.getPlayerTerritories().get(0);
		Territory targetTerritory = player.getPlayerTerritories().get(0);
		territory.attack(territory, 1, 2);
		territory.moveFortify(territory, targetTerritory);*/
	}
	
	public void initialize(List<Player> players) {
		this.playerList = players;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}

	public void addPlayer(Player player) {
		playerList.add(player);
	}

	public  void deletePlayer(Player player) {
		playerList.remove(player);	

	}
	/**
	 * start the game with a while loop that call a round with the next player passed in parameter
	 */
	public void startGame() {
		int playerTurn = 0;
		while(this.playerList.size() > 1) {
			this.round(this.playerList.get(playerTurn));
			if(playerList.size() >= playerTurn+1){
				playerTurn += 1;
			}
			else {
				playerTurn = 0;
			}
		}
	}
	
	public void endGame() {
		
	}
}


