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
		//territoryRepo.getOne(id).testTer(territoryRepo.getOne(id));
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
	
	public void addPlayer(Player player) {
		 playerRepo.save(player);
	}
	
	//A SUPRIMER CECI EST UN TEST
	public void savePlayerIntoATerritory() {
		this.getAterritory(3).setPlayer(this.getAplayer(1));
		territoryRepo.save(this.getAterritory(3));
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
}
