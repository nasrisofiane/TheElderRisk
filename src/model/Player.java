package model;

import java.util.ArrayList;

public class Player {

	private int id;
	private String name;
	private ArrayList<Territory> playerTerritories = new ArrayList<Territory>();

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
	
	public ArrayList<Territory> getPlayerTerritories() {
		return playerTerritories;
	}
	
	public void setPlayerTerritories(ArrayList<Territory> playerTerritories) {
		this.playerTerritories = playerTerritories;
	}
	public void addTerritory(Territory territory) {
		playerTerritories.add(territory);
	}

	public void deleteTerritory(Territory territory) {
		playerTerritories.remove(territory.getId());
	}
	
	
		
	}




