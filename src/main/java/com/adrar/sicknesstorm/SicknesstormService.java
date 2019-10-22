package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SicknesstormService{
	@Autowired
	PlayerRepository playerRepo;
	
	@Autowired
	TerritoryRepository territoryRepo;
	
	public Player getAplayer(Integer id){
		return playerRepo.getOne(id);
	}
	
	public List<Player> getAllPlayers(){
		return playerRepo.findAll();
	}
	
	public Territory getAterritory(Integer id) {
		//territoryRepo.getOne(id).testTer(territoryRepo.getOne(id));
		return territoryRepo.findById(id).get();
	}
	
	public List<Territory> getTerritories(){
		return territoryRepo.findAll();
	}

	public boolean isAdjacent(int territoryA, int territoryB) {
		return this.getAterritory(territoryA).isAdjacent(this.getAterritory(territoryB));
	}
	
	public void addPlayer(Player player) {
		 playerRepo.save(player);
	}
	
	public void initializeGame(Game game) {
		game.initialize(this.getAllPlayers());
		for(Player player : game.getPlayerList()) {
			int i = 0;
			int j = 0;
			for(Territory territory : this.getTerritories()) {
				territory.setPlayer(player);
				i++;
				if(i == j +7) {
					j = i;
					break;
				}
			}
			playerRepo.save(player);
		}
	}
	
	public boolean addPawn(int idplayer ,int idTerritory , int pawn ) {
		if(this.getAterritory(idTerritory).getPlayer()!= null) {
			if (this.getAplayer(idplayer).getId() == this.getAterritory(idTerritory).getPlayer().getId()){
			   	this.getAterritory(idTerritory).setPawn(this.getAterritory(idTerritory).getPawn() + pawn);
			   	territoryRepo.save(this.getAterritory(idTerritory));
			   	return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public void movePawns (int idTerritorya , int idTerritoryb , int pawn) {
        this.getAterritory(idTerritorya).setPawn(this.getAterritory(idTerritorya).getPawn()-pawn);
        this.getAterritory(idTerritoryb).setPawn(pawn);
	   	territoryRepo.save(this.getAterritory(idTerritorya));
		territoryRepo.save(this.getAterritory(idTerritoryb));    
	}
	
	public void startFight (int idTerritoryAtk , int idTerritoryDef , int nbAttack , int nbDefense) {
		this.getAterritory(idTerritoryAtk).attack(this.getAterritory(idTerritoryDef), nbAttack , nbDefense);
	}
	
	public void addTerritoryToPlayer ( int idplayer , int idTerritory) {
		this.getAterritory(idTerritory).setPlayer(this.getAplayer(idplayer));
		playerRepo.save(this.getAplayer(idplayer));
		territoryRepo.save(this.getAterritory(idTerritory));
	}

}