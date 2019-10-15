package model;

import java.util.ArrayList;

public class Territory {
	private int id;
	private String name;
	private int continentId;
	private ArrayList<Territory> territoryAdjacent;
	private int pawn;
	private int playerId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getContinentId() {
		return continentId;
	}
	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}
	public ArrayList<Territory> getTerritoryAdjacent() {
		return territoryAdjacent;
	}
	public void setTerritoryAdjacent(ArrayList<Territory> territoryAdjacent) {
		this.territoryAdjacent = territoryAdjacent;
	}
	public int getPawn() {
		return pawn;
	}
	public void setPawn(int pawn) {
		this.pawn = pawn;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public void conquerTerritory(){ // take the control of a territory.
		
	}
	
	public void moveFortify() { //move pawn from a territory to another.
		
	}
	
	public void shifumi() {
		
	}

	
}
