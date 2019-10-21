package com.adrar.sicknesstorm;

import java.util.ArrayList;
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
		return territoryRepo.getOne(id);
	}
	
	public List<Territory> getTerritories(){
	
		return territoryRepo.findAll();
	}

	
	public boolean movePawns(int idTerritoryA, int idTerritoryB, int nbPawns) {
		return this.getAterritory(idTerritoryA).moveFortify(this.getAterritory(idTerritoryB), nbPawns);
	}
	
	public boolean isAdjacent(int territoryA, int territoryB) {
		return this.getAterritory(territoryA).isAdjacent(this.getAterritory(territoryB));
	}
	

	public  void addplayer(Player player) {
		 playerRepo.save(player);
	}

	
	public boolean addPawn(int idplayer ,int idTerritory , int pawn ) {
		if (this.getAplayer(idplayer).getId() == this.getAterritory(idTerritory).getPlayer().getId()){
		   	this.getAterritory(idTerritory).setPawn(this.getAterritory(idTerritory).getPawn() + pawn);
		   	territoryRepo.save(this.getAterritory(idTerritory));
		   	return true;
		}else {
			return false;
		}
	}
}
