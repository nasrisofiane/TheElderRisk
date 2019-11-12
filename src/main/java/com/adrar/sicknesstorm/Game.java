package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Game {
	
	private int id; 
	@JsonIgnoreProperties("playerTerritories")
	private List<Player> playerList = new ArrayList();	
	private int turnPlayerNumber = 0;
	
	@JsonIgnoreProperties("playerTerritories")
	private Player playerTurn;
	private int pawnsToPlace;
	private GamePhase phase = GamePhase.INITIALIZE;
	
	@Transient
	private List<Territory> territories = new ArrayList<>();
	
	@Transient
	private String getAttacked;
	
	public int getTurnPlayerNumber() {
		return turnPlayerNumber;
	}

	public void setTurnPlayerNumber(int turnPlayerNumber) {
		this.turnPlayerNumber = turnPlayerNumber;
	}

	public void initialize(List<Player> players) {
		this.playerList = players;
	}
	
	/**
	 * set all boolean all phases of a round to false and return the player that have to play
	 * @return the player that have to play
	 */
	public Player round() {
		this.phase = GamePhase.PLACEPAWN;
		this.playerTurn = this.playerList.get(this.turnPlayerNumber);
		System.out.println("Territoires du joueur => "+this.playerTurn.getPlayerTerritories().size());
		this.pawnsToPlace = (int) Math.ceil(playerTurn.getPlayerTerritories().size() / 3);
		System.out.println("BUG ICI => "+playerTurn.getPlayerTerritories().size());
		System.out.println("Number of territories => "+playerTurn.getPlayerTerritories().size()+" | pawns to place ====> " + playerTurn.getPlayerTerritories().size() / 3);
		this.turnPlayerNumber += 1;
		
		if(turnPlayerNumber == this.playerList.size()) {
			this.turnPlayerNumber = 0;
		}
		System.out.println("Player TURN => "+playerTurn.getName());
		return playerTurn;
	}
	
	
	
	public String getGetAttacked() {
		return getAttacked;
	}

	public void setGetAttacked(String getAttacked) {
		this.getAttacked = getAttacked;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(List<Territory> territories) {
		this.territories = territories;
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
	
	public void endGame() {
		
	}
}


