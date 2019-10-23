package com.adrar.sicknesstorm;

import java.util.ArrayList;
import java.util.Collections;
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
		List<Territory> territories = this.getTerritories();
		Collections.shuffle(territories);
		int j = 0;
		for(Player player : game.getPlayerList()) {
			for(int i = j; i < territories.size(); i++) {
				territories.get(i).setPlayer(player);
				territories.get(i).setPawn(5);
				territoryRepo.save(territories.get(i));
				if(i == j + territories.size() / game.getPlayerList().size()) {
					j = i;
					break;
				}
			}
		}
		System.out.println(j);
		game.round();
	}
	
	public boolean addPawn(int idTerritory , int pawn, Game game ) {
			Territory territory = this.getAterritory(idTerritory);
			if(game.getPlayerTurn().getId() == territory.getPlayer().getId()) {
				if(game.getPawnsToPlace() > 0 && pawn <= game.getPawnsToPlace()) {
					
					game.setPawnsToPlace(game.getPawnsToPlace() - pawn);
					territory.setPawn(territory.getPawn() + pawn);
				   	territoryRepo.save(territory);
				   	if(game.getPawnsToPlace() == 0) {
				   		game.setPlacePawnDone(true);
				   	}
				   	else {
				   		System.out.println("YOU HAVE TO PLACE ALL YOUR AVAILABLE PAWNS");
				   	}
				   	return true;
				}
				else {
					System.out.println("NOT ENOUGH PAWNS");
				}
			   	
			}
			else {
				System.out.println("NOT YOUR TERRITORY => " + game.getPlayerTurn().getName()+ " => TERRITORY OWNER" + territory.getPlayer().getName());
				return false;
			}
		return false;
	}
	
	public void movePawns (int idTerritorya , int idTerritoryb , int pawn, Game game) {
		if(game.checkRoundStep() == "moveFortifyStep") {
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritorya).getPlayer().getId()) {
					this.getAterritory(idTerritorya).moveFortify(this.getAterritory(idTerritoryb), pawn);
				   	territoryRepo.save(this.getAterritory(idTerritorya));
					territoryRepo.save(this.getAterritory(idTerritoryb));  
			}
		}
		else {
			System.out.println("MOVE PAWNS NOT AVAILABLE");
		}
	}
	
	public void startFight (int idTerritoryAtk , int idTerritoryDef , int nbAttack , int nbDefense, Game game) {
		if(game.checkRoundStep() == "attackStep") {
			if(game.getPlayerTurn().getId() == this.getAterritory(idTerritoryAtk).getPlayer().getId()) {
				this.getAterritory(idTerritoryAtk).attack(this.getAterritory(idTerritoryDef), nbAttack , nbDefense);
				territoryRepo.save(this.getAterritory(idTerritoryAtk));
				territoryRepo.save(this.getAterritory(idTerritoryDef));
			}
			else {
				System.out.println("NOT YOUR TERRITORY");
			}
		}
		else {
			System.out.println("ATTACK IS NOT AVAILABLE");
		}
		
		
	}
	
	public void addTerritoryToPlayer ( int idplayer , int idTerritory) {
		this.getAterritory(idTerritory).setPlayer(this.getAplayer(idplayer));
		playerRepo.save(this.getAplayer(idplayer));
		territoryRepo.save(this.getAterritory(idTerritory));
	}
	
	public void closeFightStep(Game game) {
		game.setAttackDone(true);
	}
	
	public void closeMoveFortifyStep(Game game) {
		game.setMoveFortifyDone(true);
		game.checkRoundStep();
	}
}