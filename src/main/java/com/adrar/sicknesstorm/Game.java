package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
	private int id; 
	private List<Player> playerList = new ArrayList();	
	private int turnPlayerNumber = 0;
	private Player playerTurn;
	private int pawnsToPlace;
	private GamePhase phase = GamePhase.PLACEPAWN;
	/*private boolean placePawnDone;
	private boolean attackDone;
	private boolean moveFortifyDone;
	*/
	
	public int getTurnPlayerNumber() {
		return turnPlayerNumber;
	}

	public void setTurnPlayerNumber(int turnPlayerNumber) {
		this.turnPlayerNumber = turnPlayerNumber;
	}

	/*public boolean isPlacePawnDone() {
		return placePawnDone;
	}

	public void setPlacePawnDone(boolean placePawnDone) {
		this.placePawnDone = placePawnDone;
	}

	public boolean isAttackDone() {
		return attackDone;
	}

	public void setAttackDone(boolean attackDone) {
		this.attackDone = attackDone;
	}

	public boolean isMoveFortifyDone() {
		return moveFortifyDone;
	}

	public void setMoveFortifyDone(boolean moveFortifyDone) {
		this.moveFortifyDone = moveFortifyDone;
	}*/

	public void initialize(List<Player> players) {
		this.playerList = players;
	}
	/**
	 * set all boolean all phases of a round to false and return the player that have to play
	 * @return the player that have to play
	 */
	public Player round() {
		/*this.placePawnDone = false;
		this.attackDone = false;
		this.moveFortifyDone = false;*/
		this.phase = GamePhase.PLACEPAWN;
		this.playerTurn = this.playerList.get(this.turnPlayerNumber);
		this.pawnsToPlace = playerTurn.getPlayerTerritories().size() / 3;
		if(this.turnPlayerNumber < this.playerList.size()) {
			this.turnPlayerNumber += 1;
		}
		else {
			this.turnPlayerNumber += 0;
		}
		System.out.println("Player TURN => "+playerTurn.getName());
		return playerTurn;
	}
	
	
	
	public Player getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(Player playerTurn) {
		this.playerTurn = playerTurn;
	}

	public int getPawnsToPlace() {
		return pawnsToPlace;
	}

	public void setPawnsToPlace(int pawnsToPlace) {
		this.pawnsToPlace = pawnsToPlace;
	}

	/**
	 * 
	 * @return return a String with the step that have not been done.
	 */
	/*public String checkRoundStep() {
		if(this.placePawnDone == true) {
			if(this.attackDone == true) {
				if(this.moveFortifyDone == true) {
					this.round();
				}
				else {
					return "moveFortifyStep";
				}
			}
			else {
				return "attackStep";
			}
		}
		else {
			return "placePawnStep";
		}
		return null;
	}*/
	
	
	
	public int getId() {
		return id;
	}

	

	public GamePhase getPhase() {
		return phase;
	}

	public void setPhase(GamePhase phase) {
		this.phase = phase;
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
	
	public void startGame() {
		
		
	}
	
	public void endGame() {
		
	}
}


