package com.web.theelderrisk;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "playerTerritories"})
@Table(name="players")
public class Player {
	
	
	private int id;
	
	private String name;
	
	@Id
	private String sessionId;

	@Transient
	private ArrayList<ArrayList<Integer>> lastFightResults;

	@Transient
	private int gameId;

	@Transient
	private boolean readyToPlay = false;
	
	@JsonIgnoreProperties({"territoryAdjacent", "player"})
	@Transient
	private Set<Territory> playerTerritories = new HashSet<Territory>();

	@Transient
	private int pawnsToPlace;

	@Transient
	@JsonIgnore
	private Date endTurn;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		this.readyToPlay = true;
	}
	
	public Set<Territory> getPlayerTerritories() {
		return playerTerritories;
	}
	public void setPlayerTerritories(Set<Territory> playerTerritories) {
		this.playerTerritories = playerTerritories;
	}
	
	public void addTerritory(Territory territory) {
		playerTerritories.add(territory);
	}
                                             
	public void deleteTerritory(Territory territory) {
		playerTerritories.remove(territory);
	}

	public ArrayList<ArrayList<Integer>> getLastFightResults() {
		return lastFightResults;
	}

	public void setLastFightResults(ArrayList<ArrayList<Integer>> lastFightResults) {
		this.lastFightResults = lastFightResults;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	public void setReadyToPlay(boolean readyToPlay) {
		this.readyToPlay = readyToPlay;
	}

	public int getPawnsToPlace() {
		return pawnsToPlace;
	}

	public void setPawnsToPlace(int pawnsToPlace) {
		this.pawnsToPlace = pawnsToPlace;
	}

	public Date getEndTurn() {
		return endTurn;
	}

	public void setEndTurn(Date endTurn) {
		this.endTurn = endTurn;
	}
}




